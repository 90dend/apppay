package android.sdk.apppay;

import android.app.Activity;
import android.os.Bundle;
import android.sdk.tasks.GetTimeTask;

public class AppPaySplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		GetTimeTask getTimeTask = new GetTimeTask(AppPaySplashActivity.this);
		getTimeTask.execute();
	}
}
