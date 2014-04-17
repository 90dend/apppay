package apppay.sdk.apppaysdksample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.sdk.apppay.AppPaySDKHelper;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AppPaySDKHelper helper = new AppPaySDKHelper(MainActivity.this);
		helper.callCustomizeActivity("logo", Color.GREEN,
				"‚ведите сумму платежа", "Submit");
	}
}
