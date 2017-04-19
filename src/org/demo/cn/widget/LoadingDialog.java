package org.demo.cn.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context, AnimationDrawable ad) {
		super(context);
		this.aDrawable = ad;
	}

	public LoadingDialog(Context context, int theme, AnimationDrawable ad) {
		super(context, theme);
		this.aDrawable = ad;
	}

	private AnimationDrawable aDrawable;

	@Override
	public void show() {
		if (aDrawable != null) {
			aDrawable.start();
		}
		super.show();
	}

	@Override
	public void dismiss() {
		if (aDrawable != null) {
			aDrawable.stop();
		}
		super.dismiss();
	}

}