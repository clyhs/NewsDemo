package org.demo.cn.utils;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class SystemUtil {

	public static final int REQUEST_CODE = 0;

	/*
	 * ����֪ͨ�ӿ�
	 */
	public static void SendMSG(Handler paramHanler, int paramMsgID,
			Object paramInfo) {
		Message msg = paramHanler.obtainMessage(paramMsgID);
		msg.obj = paramInfo;
		msg.sendToTarget();
	}
	
	

	/**
	 * ֧��int��String,boolean,Serializable�������ʹ���
	 * 
	 * @param paramContext
	 * @param paramTo
	 * @param paramData
	 */
	public static void switchActivity(Context paramContext, Class paramTo,
			Map<?, ?> paramData) {

		Intent aIntent = new Intent();
		aIntent.setClass(paramContext, paramTo);

		if (paramData != null) {
			Bundle aBundle = new Bundle();

			for (Object obj : paramData.keySet()) {
				if (obj == null) {
					continue;
				}

				Object key = obj;
				Object value = paramData.get(key);

				if (value.getClass().equals(Integer.class)) {
					aBundle.putInt((String) key, (Integer) value);
				} else if (value.getClass().equals(String.class)) {
					aBundle.putString((String) key, (String) value);
				} else if (value.getClass().equals(Boolean.class)) {
					aBundle.putBoolean((String) key, (Boolean) value);
				}
			}

			aIntent.putExtras(aBundle);
		}

		paramContext.startActivity(aIntent);
	}

	/**
	 * ֧��int��String,boolean,Serializable�������ʹ���
	 * 
	 * @param paramContext
	 * @param paramTo
	 * @param paramData
	 */
	public static void switchActivityForResult(Activity paramContext,
			Class paramTo, Map<?, ?> paramData) {

		Intent aIntent = new Intent();
		aIntent.setClass(paramContext, paramTo);

		if (paramData != null) {
			Bundle aBundle = new Bundle();

			for (Object obj : paramData.keySet()) {
				if (obj == null) {
					continue;
				}

				Object key = obj;
				Object value = paramData.get(key);

				if (value.getClass().equals(Integer.class)) {
					aBundle.putInt((String) key, (Integer) value);
				} else if (value.getClass().equals(String.class)) {
					aBundle.putString((String) key, (String) value);
				} else if (value.getClass().equals(Boolean.class)) {
					aBundle.putBoolean((String) key, (Boolean) value);
				}
			}

			aIntent.putExtras(aBundle);
		}
		paramContext.startActivityForResult(aIntent, REQUEST_CODE);
	}

	/**
	 * Toast������ʾ��
	 * */
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 2014.5.13
	 * 
	 * @param context
	 * @param msgId
	 */
	public static void showToast(Context context, int msgId) {
		showToast(context, context.getResources().getString(msgId));
	}

	/**
	 * ��ȡimei
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}

	/**
	 * ��ȡӦ�ð汾��
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}

	/**
	 * ��ȡ��������
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

}
