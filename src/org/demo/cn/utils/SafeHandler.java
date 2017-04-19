package org.demo.cn.utils;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class SafeHandler extends Handler {
	WeakReference<Context> mActivityReference;

	public SafeHandler(Context context) {
		mActivityReference = new WeakReference<Context>(context);
	}

	@Override
	public void handleMessage(Message msg) {
		final Context context = mActivityReference.get();
		if (context != null) {
			// mImageView.setImageBitmap(mBitmap);
		}
	}
}