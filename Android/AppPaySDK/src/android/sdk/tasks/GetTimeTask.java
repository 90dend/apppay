package android.sdk.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.sdk.apppay.AppPayRequest;
import android.sdk.apppay.AppPaySDKActivity;

public class GetTimeTask extends AsyncTask<Void, Void, String> {
	private transient AppPayRequest payRequest;
	private transient Context context;

	public GetTimeTask(Context context) {
		payRequest = new AppPayRequest();
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		String result = "";
		if (isNetworkConnected()) {
			result = payRequest.getServerTimeRequest();
		} else {
			((AppPaySDKActivity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					((AppPaySDKActivity) context).showAlert("internet_error");
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
