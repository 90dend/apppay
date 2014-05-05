package android.sdk.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Years;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.sdk.apppay.AppPayRequest;
import android.sdk.apppay.AppPaySDKActivity;
import android.sdk.apppay.AppPaySplashActivity;
import android.util.Log;

public class GetTimeTask extends AsyncTask<Void, Void, String> {
	private transient AppPayRequest payRequest;
	private transient Context context;
	private transient final SimpleDateFormat serverformat = new SimpleDateFormat(
			"yyyyMMddHHmmss", Locale.getDefault());
	private transient Date serverDate = null;
	private transient SharedPreferences preferences;

	public GetTimeTask(Context context) {
		payRequest = new AppPayRequest();
		this.context = context;
		this.preferences = context.getSharedPreferences("AppPaySDKPref",
				Context.MODE_PRIVATE);
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
		try {

			serverDate = serverformat.parse(result);
			DateTime serverDateTime = new DateTime(serverDate);
			DateTime currentDateTime = new LocalDateTime(new Date())
					.toDateTime(DateTimeZone.UTC);

			Log.d("server date", serverformat.format(serverDate));
			Log.d("years delta",
					String.valueOf(Years.yearsBetween(serverDateTime,
							currentDateTime).getYears()));
			Log.d("months delta",
					String.valueOf(Months.monthsBetween(serverDateTime,
							currentDateTime).getMonths()));
			Log.d("days delta",
					String.valueOf(Days.daysBetween(serverDateTime,
							currentDateTime).getDays()));
			Log.d("hours delta",
					String.valueOf(Hours.hoursBetween(serverDateTime,
							currentDateTime).getHours()));

			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("timestamp", result);
			editor.putString(
					"years_delta",
					String.valueOf(Years.yearsBetween(serverDateTime,
							currentDateTime).getYears()));
			editor.putString(
					"months_delta",
					String.valueOf(Months.monthsBetween(serverDateTime,
							currentDateTime).getMonths()));
			editor.putString(
					"days_delta",
					String.valueOf(Days.daysBetween(serverDateTime,
							currentDateTime).getDays()));
			editor.putString(
					"hours delta",
					String.valueOf(Hours.hoursBetween(serverDateTime,
							currentDateTime).getHours()));
			editor.commit();
		} catch (ParseException e) {
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

	// private void diffTime() {
	//
	// }

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (context instanceof AppPaySplashActivity) {
			((AppPaySplashActivity) context).finish();
			context.startActivity(new Intent(context, AppPaySDKActivity.class)
					.putExtra("extras", ((AppPaySplashActivity) context)
							.getIntent().getExtras()));
		}
	}

}
