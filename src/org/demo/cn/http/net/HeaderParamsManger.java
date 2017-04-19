package org.demo.cn.http.net;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class HeaderParamsManger {

	public static Map<String, String> getHeaderMap(Context context) {
		String version = "";
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		
		HashMap<String, String> aHeaderMap = new HashMap<String, String>();
		StringBuffer buffer = new StringBuffer();
		buffer.append(android.os.Build.BRAND+";").append("android/")
				.append(Build.VERSION.RELEASE + ";").append("v/"+version);
		aHeaderMap.put("x-demo-agent", buffer.toString());
		aHeaderMap.put("x-demo-appcode", "1");
		aHeaderMap.put("x-demo-sp", "1");

		return aHeaderMap;
	}
}
