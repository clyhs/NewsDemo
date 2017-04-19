package org.demo.cn.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.demo.cn.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;


import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

public class ImageUtil {

	public static final String TAG = "ImageUtil";

	public static final int IMAGE_CACHE_MAX_SIZE = 100 * 1024 * 1024;
	public static final long IMAGE_CLEAR_LIMIT = 50 * 1024 * 1024;
	public static final int CACHE_MAX_FILE_COUNT = 30;

	/** 添加参数 isDel 缓存是否可删除---zss---2014.12.05--- */
	@SuppressWarnings("deprecation")
	public static ImageLoaderConfiguration getSimpleImageLoaderConfig(
			Context context, boolean isDel) {
		// 根据SDK等级设置内存缓存大小(Android 2.0以前没有提供获取系统分配内存大小的API)
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			// Android 2.0及以后的版本设置内存缓存大小为系统分配内存的1/6
			int memClass = ((ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 6) * 1024 * 1024; // 1/6 of app memory
															// limit
		} else {
			// Android 2.0以前的设置默认缓存大小4M
			memoryCacheSize = 4 * 1024 * 1024;
		}

		// This configuration tuning is custom. You can tune every option, you
		// may
		// tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context);
		builder.threadPoolSize(5)
				// 3 5
				// 设置线程池同时进行的线程数
				.threadPriority(Thread.NORM_PRIORITY - 2)
				//new WeakMemoryCache()
				.memoryCache(new LruMemoryCache(memoryCacheSize))
				// 设置线程优先级
				.memoryCacheSize(memoryCacheSize)
				// 设置内存缓存大小
				.denyCacheImageMultipleSizesInMemory()
				// 设置同一张图片以不同尺寸载入内存时,前面的会被后面的取代
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.defaultDisplayImageOptions(getDefaultOption())
		/* .enableLogging() */;
		// 根据设置决定是否限制磁盘缓存大小
		// if (PreferenceManager.getDefaultSharedPreferences(context)
		// .getBoolean(SettingActivity.SP_KEY_CACHE_LIMITE, true)) {
		// builder.clearForAll(IMAGE_CACHE_MAX_SIZE);
		// }

		// builder.discCache(new
		// TotalSizeLimitedDiscCache(DiskLruCache.getDiskCacheDir(context,
		// Constants.PIC_CACHE_NAME),
		// new Md5FileNameGenerator(), IMAGE_CACHE_MAX_SIZE));
		if (isDel) {
//			builder.discCache(new LimitedAgeDiskCache(DiskLruCache
//					.getDiskCacheDir(context, Constants.PIC_CACHE_NAME),
//					DiskLruCache.getDiskCacheDir(context,
//							Constants.PIC_CACHE_NAME),
//					new Md5FileNameGenerator(), IMAGE_CACHE_MAX_SIZE));
			try {
				builder.diskCache(new LruDiskCache(DiskLruCache
						.getDiskCacheDir(context, Constant.PIC_CACHE_NAME),
						DiskLruCache.getDiskCacheDir(context,
								Constant.PIC_CACHE_NAME),
						new Md5FileNameGenerator(), IMAGE_CACHE_MAX_SIZE, CACHE_MAX_FILE_COUNT));
				
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
//			builder.discCache(new LimitedAgeDiskCache(DiskLruCache
//					.getDiskCacheDir(context, Constants.REC_PIC_CACHE_NAME),
//					DiskLruCache.getDiskCacheDir(context,
//							Constants.REC_PIC_CACHE_NAME),
//					new Md5FileNameGenerator(), IMAGE_CACHE_MAX_SIZE));
	      try {
			builder.diskCache(new LruDiskCache(DiskLruCache
						.getDiskCacheDir(context, Constant.REC_PIC_CACHE_NAME),
						DiskLruCache.getDiskCacheDir(context,
								Constant.REC_PIC_CACHE_NAME),
						new Md5FileNameGenerator(), IMAGE_CACHE_MAX_SIZE, CACHE_MAX_FILE_COUNT));
	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		ImageLoaderConfiguration config = builder.build();
		return config;
	}

	/**
	 * 默认装在额外配置
	 * 
	 * @return
	 */
	public static DisplayImageOptions getDefaultOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.color.white)
				.showImageForEmptyUri(R.color.white)
				.showImageOnFail(R.color.white).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		return options;
	}

	/**
	 * 图片是否在缓存中
	 * 
	 * @param imageURL
	 * @return
	 */
	public static boolean hasImageCache(String imageURL) {
		File file = ImageLoader.getInstance().getDiskCache().get(imageURL);
		if(file != null){
			return file.exists();
		}else{
			return false;
		}
		
	}

	public static void loadMemImageView(ImageView imageView, String url,
			int drawableId, ImageSize imageSize) {

		if (url == null || url.length() == 0 || imageSize == null) {
			imageView.setImageResource(drawableId);
		} else {


			// ImageSize imageSize = getImageSizeScaleTo (imageView);
			String memKey = MemoryCacheUtils.generateKey(url, imageSize);
			Bitmap memBitmap = ImageLoader.getInstance().getMemoryCache()
					.get(memKey);
			if (memBitmap != null && !memBitmap.isRecycled()) {
				imageView.setImageBitmap(memBitmap);
			} else {
				imageView.setImageResource(drawableId);
			}
		}
	}

	public static void loadMemImageView(ImageView imageView, String url,
			ImageSize imageSize) {
		loadMemImageView(imageView, url, android.R.color.transparent, imageSize);// R.drawable.default_img
	}

	public static ImageSize getImageSizeScaleTo(ImageView imageView) {
		LayoutParams params = imageView.getLayoutParams();
		int width = params.width; // Get layout width parameter
		if (width <= 0)
			width = getFieldValue(imageView, "mMaxWidth"); // Check maxWidth
															// parameter

		int height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getFieldValue(imageView, "mMaxHeight"); // Check maxHeight
																// parameter

		// Consider device screen orientation
		int screenOrientation = imageView.getContext().getResources()
				.getConfiguration().orientation;
		if ((screenOrientation == Configuration.ORIENTATION_PORTRAIT && width > height)
				|| (screenOrientation == Configuration.ORIENTATION_LANDSCAPE && width < height)) {
			int tmp = width;
			width = height;
			height = tmp;
		}
		return new ImageSize(width, height);
	}

	private static int getFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception e) {
		}
		return value;
	}

	public static void putMemCache(Bitmap bitmap, ImageSize imageSize,
			String url) {
		String memKey = MemoryCacheUtils.generateKey(url, imageSize);
		ImageLoader.getInstance().getMemoryCache().put(memKey, bitmap);
	}

	/**
	 * 用于显示图片，若图片加载器未初始化这直接返回
	 * 
	 * @param paramImageLoader
	 *            图片加载器
	 * @param paramPath
	 *            图片路径，若是http/https则使用网络请求，否则使用asserts中文件
	 * @param paramImageView
	 *            图片视图
	 * @param paramDispOptions
	 *            图片显示设置项
	 */
	public static void showImage(ImageLoader paramImageLoader,
			String paramPath, ImageView paramImageView,
			DisplayImageOptions paramDispOptions) {

		String strFileName = null;
		// String strPre = "http://";
		// String strPres = "https://";
		// String strAssertFileName = "assets://listimage/";

		// if (!paramPath.startsWith(strPre) && !paramPath.startsWith(strPres))
		// {
		// // 本地图片
		// strFileName = strAssertFileName + paramPath;
		// } else {
		// 网络图片
		strFileName = paramPath;
		// }

		if (paramImageLoader == null) {
			return;
		}

		paramImageLoader.displayImage(strFileName, paramImageView,
				paramDispOptions);
	}

}