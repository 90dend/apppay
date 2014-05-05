package android.sdk.tasks;

import android.os.AsyncTask;
import android.sdk.apppay.BCrypt;
import android.util.Log;

public class CryptTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		Log.d("crypt", params[0]);
		return BCrypt.hashpw(params[0], BCrypt.gensalt(12));

	}

}
