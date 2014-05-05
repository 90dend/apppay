package android.sdk.apppay;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sdk.tasks.CryptTask;
import android.sdk.tasks.GetTimeTask;
import android.sdk.tasks.PostFormRequest;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppPaySDKActivity extends Activity {
	private transient Button submitBtn;
	private transient EditText valueEdit;
	private transient String date = "";
	private transient String hash = "";
	private transient Bundle extras;
	private transient SharedPreferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_pay_sdk);
		extras = getIntent().getExtras().getBundle("extras");

		preference = getSharedPreferences("AppPaySDKPref", MODE_PRIVATE);

		submitBtn = (Button) findViewById(R.id.submitBtn);
		valueEdit = (EditText) findViewById(R.id.valueEdit);

		StringBuilder builder = new StringBuilder();
		if (preference.getString("timestamp", "").equals("")) {
			GetTimeTask getTimeTask = new GetTimeTask(AppPaySDKActivity.this);
			getTimeTask.execute();
			try {
				date = getTimeTask.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			date = preference.getString("timestamp", "");
		}

		builder.append(extras.getString("mId")).append(":").append(date)
				.append(":").append(extras.getString("authKey"));
		CryptTask cryptTask = new CryptTask();
		cryptTask.execute(builder.toString());
		try {
			hash = cryptTask.get();
		} catch (InterruptedException | ExecutionException e1) {
		}

		Log.d("date", date);
		Log.d("hash", hash);

		if (!extras.isEmpty()) {
			if (extras.containsKey("picName")
					&& !extras.getString("picName").equals("")) {
				ImageView picName = (ImageView) findViewById(R.id.pic);
				picName.setImageResource(getResources().getIdentifier(
						extras.getString("picName"), "drawable",
						extras.getString("packName")));
			}

			if (extras.containsKey("editLabel")
					&& !extras.getString("editLabel").equals("")) {
				TextView editLabel = (TextView) findViewById(R.id.editLabel);
				editLabel.setText(extras.getString("editLabel"));
			}

			if (extras.containsKey("buttonLabel")
					&& !extras.getString("buttonLabel").equals("")) {
				submitBtn.setText(extras.getString("buttonLabel"));
			}

			if (extras.containsKey("bgColor") && extras.getInt("bgColor") != 0) {
				LinearLayout layout = (LinearLayout) findViewById(R.id.container);
				layout.setBackgroundColor(extras.getInt("bgColor"));
			}

			submitBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String summ = valueEdit.getEditableText().toString();
					if (summ.length() > 0 && !summ.equals("0")
							&& Integer.parseInt(summ) > 0
							&& summ.matches("\\d+")) {
						Bundle paramsBundle = new Bundle();
						paramsBundle.putString("merchantId",
								extras.getString("mId"));
						paramsBundle.putString("authKey", hash);
						paramsBundle.putString("amount", valueEdit
								.getEditableText().toString());
						paramsBundle.putString("date", date);

						PostFormRequest formRequest = new PostFormRequest(
								AppPaySDKActivity.this);
						formRequest.execute(paramsBundle);

						JSONObject jsonObject = new JSONObject();
						try {
							jsonObject = new JSONObject(formRequest.get());
							Bundle sendParams = new Bundle();
							sendParams.putString("fields",
									jsonObject.toString());

							sendParams.putString("merchantId",
									extras.getString("mId"));
							sendParams.putString("authKey", hash);
							sendParams.putString("amount", valueEdit
									.getEditableText().toString());
							sendParams.putString("date", date);

							if (jsonObject.getJSONObject("fields").has("ORDER")) {
								sendParams.putString("order",
										jsonObject.getJSONObject("fields")
												.getString("ORDER"));
							}

							startForm(sendParams);
						} catch (InterruptedException | ExecutionException
								| JSONException e) {
						}
					} else {
						valueEdit.getEditableText().clear();
						showAlert("empty_edit_check");
					}
				}
			});
		}
	}

	public void showAlert(String actionName) {
		if (actionName.equals("internet_error")) {
			new AlertDialog.Builder(this)
					.setTitle("Ошибка получения времени")
					.setMessage(
							"Из-за отсутствия соединения с интернетом не удается синхронизировать время с ервером")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}

							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		} else if (actionName.equals("empty_edit_check")) {
			new AlertDialog.Builder(this)
					.setTitle("Ошибка")
					.setMessage("Заполните поле для суммы платежа")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}

							}).setIcon(android.R.drawable.ic_dialog_alert)

					.show();
		}

		else if (actionName.equals("post_form_error")) {
			new AlertDialog.Builder(this)
					.setTitle("Ошибка")
					.setMessage("Ошибка получения формы банка")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}

							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}
	}

	private void startForm(Bundle params) {
		Log.d("par", params.getString("fields"));
		startActivity(new Intent(AppPaySDKActivity.this,
				AppPayFormActivity.class).putExtra("params", params));
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
