package android.sdk.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sdk.apppay.AppPayFormActivity;
import android.sdk.apppay.AppPayRequest;

public class BmRequestTask extends AsyncTask<Bundle, Void, String> {
	private transient AppPayRequest payRequest;
	private transient Context context;

	public BmRequestTask(Context context) {
		payRequest = new AppPayRequest();
		this.context = context;
	}

	@Override
	protected String doInBackground(Bundle... params) {
		String result = "";
		if (isNetworkConnected()) {
			result = payRequest.bmRequest(params[0].getString("url"),
					params[0].getString("fields"));
		} else {
			((AppPayFormActivity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					((AppPayFormActivity) context)
							.showAlert("bm_request_error");
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
