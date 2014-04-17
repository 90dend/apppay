package android.sdk.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sdk.apppay.AppPayRequest;
import android.sdk.apppay.AppPaySDKActivity;

public class PostFormRequest extends AsyncTask<Bundle, Void, String> {

	private transient AppPayRequest payRequest;
	private transient Context context;

	public PostFormRequest(Context context) {
		payRequest = new AppPayRequest();
		this.context = context;
	}

	@Override
	protected String doInBackground(Bundle... params) {
		String result = "";
		if (isNetworkConnected()) {
			result = payRequest.postData(params[0]);
		} else {
			((AppPaySDKActivity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					((AppPaySDKActivity) context).showAlert("post_form_error");
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
			return false;
		} else
			return true;
	}

}
