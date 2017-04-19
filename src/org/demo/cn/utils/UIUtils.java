package org.demo.cn.utils;

import org.demo.cn.R;
import org.demo.cn.widget.LoadingDialog;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UIUtils {
	private Application	app	= null;
	private final DisplayMetrics	mDisplayMetrics;
	private boolean					inited	= false;

	private UIUtils(Application app) {
		this.app = app;
		this.mDisplayMetrics = new DisplayMetrics();
	}

	private static UIUtils	__instance	= null;

	public static void initUIUtil(Application app) {
		if (__instance == null)
			__instance = new UIUtils(app);
		__instance.init();
	}

	public synchronized static UIUtils getInstance() {
		return __instance;
	}

	

	private void init() {
		if (inited) {
			return;
		}
		WindowManager mWindowManager = ((WindowManager) app.getSystemService(Context.WINDOW_SERVICE));
		mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
		inited = true;
	}

	public DisplayMetrics getDisplayMetrics() {
		return mDisplayMetrics;
	}

	/**
	 * 获取屏幕density属性的等级 包括:low, mid, high, x
	 */
	public float getDensity() {
		return mDisplayMetrics.density;
	}

	/**
	 * 
	 * 获取屏幕宽度
	 */
	public int getmScreenWidth() {
		return mDisplayMetrics.widthPixels;
	}

	/**
	 * 
	 * 获取屏幕高度
	 */
	public int getmScreenHeight() {
		return mDisplayMetrics.heightPixels;
	}

	/**
	 * 
	 * <p>
	 * Title: setListViewHeightBasedOnChildren
	 * </p>
	 * <p>
	 * Description: 解决一个界面多个ListView界面数据显示不全的问题
	 * </p>
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		listView.setLayoutParams(params);
	}

	private AnimationDrawable	ad;

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public LoadingDialog getProgressDialog(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading_layout, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		ad = (AnimationDrawable) spaceshipImage.getBackground();
		final LoadingDialog loadingDialog = new LoadingDialog(context, R.style.dialog_loading_style, ad);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		loadingDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				loadingDialog.dismiss();
				return true;
			}
		});
		return loadingDialog;

	}

//	public static DisplayImageOptions getDetailImageOptions() {
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.default_icon_cover).showImageForEmptyUri(R.drawable.default_icon_cover)
//				.showImageOnFail(R.drawable.default_icon_cover).cacheInMemory(true).cacheOnDisk(true)
//				.bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).build();
//		return options;
//	}
}
