package android.sdk.apppay;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppPaySDKHelper {
	private transient Context context;

	public AppPaySDKHelper(Context context) {
		this.context = context;
	}

	public void callDefaultActivity() {
		context.startActivity(new Intent(context, AppPaySDKActivity.class));
	}

	public void callCustomizeActivity(String picName, int bgColor,
			String editLabel, String buttonLabel) {
		String merchantId = "";
		String authKey = "";
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			merchantId = String.valueOf(applicationInfo.metaData.getInt("mId"));
			authKey = applicationInfo.metaData.getString("authKey");
		} catch (NameNotFoundException e) {
		}
		context.startActivity(new Intent(context, AppPaySDKActivity.class)
				.putExtra("picName", picName)
				.putExtra("packName", context.getPackageName())
				.putExtra("bgColor", bgColor).putExtra("editLabel", editLabel)
				.putExtra("buttonLabel", buttonLabel)
				.putExtra("mId", merchantId).putExtra("authKey", authKey));
	}
}
