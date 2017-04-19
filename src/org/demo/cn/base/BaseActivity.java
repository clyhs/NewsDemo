package org.demo.cn.base;

import org.demo.cn.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.mContext = this;
		init();
	}
	
	private void init(){
		imageLoader.init(ImageLoaderConfiguration.createDefault(this.mContext));
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images).showImageOnFail(R.drawable.images).cacheInMemory()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}
	
	

}
