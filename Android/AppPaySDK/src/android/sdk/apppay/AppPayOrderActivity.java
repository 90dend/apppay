package android.sdk.apppay;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.sdk.tasks.VerifiTask;
import android.util.Log;
import android.widget.TextView;

public class AppPayOrderActivity extends Activity {

	JSONObject jsonObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_activity);
		String orderStatus = "";
		TextView status = (TextView) findViewById(R.id.textView1);
		Bundle extras = getIntent().getExtras().getBundle("params");

		VerifiTask verifyTask = new VerifiTask(AppPayOrderActivity.this);
		Bundle verifyBundle = new Bundle();
		verifyBundle.putString("authKey", extras.getString("authKey"));
		verifyBundle.putString("date", extras.getString("date"));
		verifyBundle.putString("merchantId", extras.getString("merchantId"));
		verifyBundle.putString("order", extras.getString("order"));
		verifyTask.execute(verifyBundle);
		try {
			orderStatus = verifyTask.get();
			Log.d("verify", orderStatus);
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		try {
			jsonObject = new JSONObject(orderStatus);
			if (jsonObject.getString("status").equals("failed")) {
				status.setTextColor(Color.RED);
				status.setText("Платеж не прошел");
			} else if (jsonObject.getString("status").equals("waiting")) {
				status.setTextColor(Color.YELLOW);
				status.setText("Ожидание");
			} else {
				status.setTextColor(Color.GREEN);
				status.setText("Платеж  прошел");
			}
		} catch (JSONException e) {
		}

	}

	public void showAlert() {
		new AlertDialog.Builder(this)
				.setTitle("Ошибка проверки платежа")
				.setMessage(
						"Из-за отсутствия соединения с интернетом не удается проверить статус платежа")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}
}
