package android.sdk.apppay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

public class AppPayRequest {
	public String getServerTimeRequest() {
		InputStream inputStream = new InputStream() {
			@Override
			public int read() throws IOException {
				return 0;
			}
		};
		String result = null;

		try {

			inputStream = new DefaultHttpClient()
					.execute(
							new HttpGet(
									"http://f-2-f.net/app.php/api/currentdate"))
					.getEntity().getContent();
		} catch (Exception exception) {
		}

		try {
			final BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			final StringBuilder stringBuilder = new StringBuilder();
			String line = "";

			while (line != null) {
				line = bufferedReader.readLine();
				if (line != null) {
					stringBuilder.append(line + "\n");
				}
			}

			inputStream.close();

			result = stringBuilder.toString();
		} catch (IOException convertException) {

		}

		return result;
	}

	public String postData(Bundle params) {
		String result = "";
		InputStream in = new InputStream() {
			@Override
			public int read() throws IOException {
				return 0;
			}
		};

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://f-2-f.net/app.php/api/form");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("amount", params
					.getString("amount")));
			nameValuePairs.add(new BasicNameValuePair("merchantId", params
					.getString("merchantId")));
			nameValuePairs.add(new BasicNameValuePair("authKey", params
					.getString("authKey")));
			nameValuePairs.add(new BasicNameValuePair("appId", "2"));
			nameValuePairs.add(new BasicNameValuePair("date", params
					.getString("date")));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			in = response.getEntity().getContent();
			StringBuilder stringbuilder = new StringBuilder();
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(in),
					1024);
			String line;
			while ((line = bfrd.readLine()) != null) {
				if (line != null) {
					stringbuilder.append(line);
				}
			}

			result = stringbuilder.toString();

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		Log.d("res", result);
		return result;
	}

	public String verify(Bundle params) {
		Log.d("params", params.toString());
		String result = "";
		InputStream in = new InputStream() {
			@Override
			public int read() throws IOException {
				return 0;
			}
		};

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://f-2-f.net/app.php/api/check");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("order", params
					.getString("order")));
			nameValuePairs.add(new BasicNameValuePair("merchantId", params
					.getString("merchantId")));
			nameValuePairs.add(new BasicNameValuePair("authKey", params
					.getString("authKey")));
			nameValuePairs.add(new BasicNameValuePair("date", params
					.getString("date")));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			in = response.getEntity().getContent();
			StringBuilder stringbuilder = new StringBuilder();
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(in),
					1024);
			String line;
			while ((line = bfrd.readLine()) != null) {
				if (line != null) {
					stringbuilder.append(line);
				}
			}

			result = stringbuilder.toString();

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		Log.d("res", result);
		return result;
	}

	public String bmRequest(String url, String fields) {
		String result = "";
		String value = "";
		InputStream in = new InputStream() {
			@Override
			public int read() throws IOException {
				return 0;
			}
		};

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(fields);
				Iterator keysIterator = jsonObject.keys();

				while (keysIterator.hasNext()) {
					Object obj = keysIterator.next();
					nameValuePairs.add(new BasicNameValuePair(String
							.valueOf(obj), jsonObject.getString(String
							.valueOf(obj))));
				}
			} catch (JSONException e) {
			}

			Log.d("params_bm", Arrays.asList(nameValuePairs).toString());

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			in = response.getEntity().getContent();
			StringBuilder stringbuilder = new StringBuilder();
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(in),
					1024);
			String line;
			while ((line = bfrd.readLine()) != null) {
				if (line != null) {
					stringbuilder.append(line);
				}
			}

			result = stringbuilder.toString();

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		return result;
	}
}
