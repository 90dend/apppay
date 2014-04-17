package android.sdk.apppay;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.sdk.tasks.BmRequestTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("SetJavaScriptEnabled")
public class AppPayFormActivity extends Activity {
	private transient String[] expMonths = { "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12" };
	private transient String[] expYears = { "14", "15", "16" };
	private transient String[] cvcCodes = { "CVC2 присутствует",
			"CVC2 не предоставлен", "CVC2 не читаетсЯ", "CVC2 отсутствует" };
	private transient String[] cvcCodesNum = { "1", "0", "2", "9" };
	private transient String[] replaceString = { "%CARD%", "%EXP%",
			"%EXP_YEAR%", "%CVC2%", "%CVC2_RC%" };

	JSONObject jsonObject;

	private transient Bundle extras;

	private transient EditText cardNumEdit;
	private transient EditText cvcEdit;
	private transient EditText sumEdit;
	private transient Spinner expMonSpinner;
	private transient Spinner expYearSpinner;
	private transient Spinner cvc2crSpinner;

	private transient WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bm_app_pay);

		webView = (WebView) findViewById(R.id.webView1);
		webView.setWebChromeClient(new WebChromeClient());

		if (!getIntent().getExtras().isEmpty()) {
			extras = getIntent().getExtras();
		}

		try {
			jsonObject = new JSONObject(getIntent().getExtras()
					.getBundle("params").getString("fields"));

		} catch (JSONException e1) {
		}

		webView.clearCache(true);
		webView.clearHistory();
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

		cardNumEdit = (EditText) findViewById(R.id.cardNum);
		cvcEdit = (EditText) findViewById(R.id.cvcEdit);

		ArrayAdapter<String> expMonAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, expMonths);
		expMonAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<String> expYearAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, expYears);
		expMonAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<String> cvc2crAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cvcCodes);
		expMonAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		expMonSpinner = (Spinner) findViewById(R.id.expMonth);
		expMonSpinner.setSelection(1);
		expMonSpinner.setAdapter(expMonAdapter);

		expYearSpinner = (Spinner) findViewById(R.id.exYear);
		expYearSpinner.setAdapter(expYearAdapter);

		cvc2crSpinner = (Spinner) findViewById(R.id.cvcCode);
		cvc2crSpinner.setAdapter(cvc2crAdapter);

		Button submitBtn = (Button) findViewById(R.id.submit);

		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (cardNumEdit.getEditableText().length() > 0
						&& cvcEdit.getEditableText().length() > 0) {
					try {
						Bundle bmBundle = new Bundle();
						JSONObject jsonObject = new JSONObject(getIntent()
								.getExtras().getBundle("params")
								.getString("fields"));

						String fields = exchPar(jsonObject.getString("fields"));

						bmBundle.putString("fields", fields);
						Log.d("fields", fields);

						BmRequestTask bmRequestTask = new BmRequestTask(
								AppPayFormActivity.this);
						Log.d("json", jsonObject.toString());
						Log.d("url", jsonObject.getString("url"));
						bmBundle.putString("url", jsonObject.getString("url"));
						bmRequestTask.execute(bmBundle);
						String html = bmRequestTask.get();
						Log.d("html", html);

						WebViewClient client = new WebViewClient() {
							@Override
							public void onPageFinished(WebView view, String url) {

								webView.loadUrl("javascript:(function(){document.getElementsByName('SEND_BUTTON')[0].click();})()");

							}

						};

						webView.setWebViewClient(client);
						webView.loadDataWithBaseURL(
								jsonObject.getString("url"), html, "text/html",
								"utf-8", null);

					} catch (JSONException | InterruptedException
							| ExecutionException e) {

					}
				} else {
					showAlert("empty_edit_check");
				}
			}
		});

		Button checkBtn = (Button) findViewById(R.id.checkBtn);
		checkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.d("params", extras.toString());
				startActivity(new Intent(AppPayFormActivity.this,
						AppPayOrderActivity.class).putExtra("params",
						extras.getBundle("params")));
			}

		});
	}

	private String exchPar(String source) {
		Log.d("fields_str", source);
		String newString = source
				.replace("%CARD%", cardNumEdit.getEditableText().toString())
				.replace("%EXP%",
						expMonths[expMonSpinner.getSelectedItemPosition()])
				.replace("%EXP_YEAR%",
						expYears[expYearSpinner.getSelectedItemPosition()])
				.replace("%CVC2%", cvcEdit.getEditableText().toString())
				.replace("%CVC2_RC%",
						cvcCodesNum[cvc2crSpinner.getSelectedItemPosition()]);

		return newString;
	}

	public void showAlert(String actionName) {
		if (actionName.equals("bm_request_error")) {
			new AlertDialog.Builder(this)
					.setTitle("Ошибка запроса формы банка")
					.setMessage(
							"Из-за отсутствия соединения с интернетом не удается получить форму Банка Москвы")
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
					.setMessage("Заполните  поля для номера карты и CVC")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}

							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}
	}

}
