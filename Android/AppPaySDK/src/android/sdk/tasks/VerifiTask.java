package android.sdk.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sdk.apppay.AppPayOrderActivity;
import android.sdk.apppay.AppPayRequest;

public class VerifiTask extends AsyncTask<Bundle, Void, String> {
	private transient AppPayRequest appPayRequest;
	private transient Context context;

	public VerifiTask(Context context) {
		this.context = context;
		appPayRequest = new AppPayRequest();
	}

	@Override
	protected String doInBackground(Bundle... arg0) {
		String result = "";

		if (isNetworkConnected()) {
			result = appPayRequest.verify(arg0[0]);
		}

		else {
			((AppPayOrderActivity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					((AppPayOrderActivity) context).showAlert();
				}
			});
		}
		return result;
	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

}
