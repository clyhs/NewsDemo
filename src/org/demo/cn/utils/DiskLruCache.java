package org.demo.cn.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class DiskLruCache {

	private static final String CACHE_FILENAME_PREFIX = "cache_";
	private static final int    MAX_REMOVALS = 4;
	private static final int    INITIAL_CAPACITY = 32;
	private static final float  LOAD_FACTOR = 0.75f;

	private final File mCacheDir;
	private int        cacheSize = 0;
	private int        cacheByteSize = 0;
	private final int  maxCacheItemSize = 64; // 64 item default
	private long       maxCacheByteSize = 1024 * 1024 * 5; // 5MB default

	private final Map<String, String> mLinkedHashMap = Collections.synchronizedMap(new LinkedHashMap<String, String>(
			INITIAL_CAPACITY, LOAD_FACTOR, true));

	/**
	 * A filename filter to use to identify the cache filenames which have
	 * CACHE_FILENAME_PREFIX prepended.
	 */
	private static final FilenameFilter cacheFileFilter = new FilenameFilter() {
		public boolean accept(File dir, String filename) {
			return filename.startsWith(CACHE_FILENAME_PREFIX);
		}
	};

	private DiskLruCache(File cacheDir, long maxByteSize) {
		mCacheDir = cacheDir;
		maxCacheByteSize = maxByteSize;
	}

	/**
	* Used to fetch an instance of DiskLruCache.
	*
	* @param context
	* @param cacheDir
	* @param maxByteSize
	* @return
	*/
	public static DiskLruCache openCache(Context context, File cacheDir, long maxByteSize) {
		if (!cacheDir.exists()) {
			//2.3�Ժ������
			//   cacheDir.mkdir();
			//2.3 ��ǰǿ�ƴ���
			cacheDir.mkdirs();
		}

		if (cacheDir.isDirectory() && cacheDir.canWrite() && CacheUtils.getUsableSpace(cacheDir) > maxByteSize) {
			return new DiskLruCache(cacheDir, maxByteSize);
		}

		return null;
	}

	public void put(String key, String file) {
		mLinkedHashMap.put(key, file);
		cacheSize = mLinkedHashMap.size();
		cacheByteSize += new File(file).length();
	}

	/**
	 * Flush the cache, removing oldest entries if the total size is over the specified cache size.
	 * Note that this isn't keeping track of stale files in the cache directory that aren't in the
	 * HashMap. If the images and keys in the disk cache change often then they probably won't ever
	 * be removed.
	 */
//	public void flushCache() {
//		Entry<String, String> eldestEntry;
//		File eldestFile;
//		long eldestFileSize;
//		int count = 0;
////		if (HoYangApplication.autoClearCache) {
////			if (ConfigCache.isCacheTooBig()) {
////				ConfigCache.clearCache();
////			}
////		} else {
//			while (count < MAX_REMOVALS && (cacheSize > maxCacheItemSize || cacheByteSize > maxCacheByteSize)) {
//				eldestEntry = mLinkedHashMap.entrySet().iterator().next();
//				eldestFile = new File(eldestEntry.getValue());
//				eldestFileSize = eldestFile.length();
//				mLinkedHashMap.remove(eldestEntry.getKey());
//				eldestFile.delete();
//				cacheSize = mLinkedHashMap.size();
//				cacheByteSize -= eldestFileSize;
//				count++;
//
////			}
//		}
//	}

	/**
	 * Checks if a specific key exist in the cache.
	 *
	 * @param key The unique identifier for the bitmap
	 * @return true if found, false otherwise
	 */
	public boolean containsKey(String key) {
		// See if the key is in our HashMap
		if (mLinkedHashMap.containsKey(key)) {
			return true;
		}

		// Now check if there's an actual file that exists based on the key
		final String existingFile = createFilePath(mCacheDir, key);
		if (new File(existingFile).exists()) {
			// File found, add it to the HashMap for future use
			put(key, existingFile);
			return true;
		}
		return false;
	}

	/**
	 * Removes all disk cache entries from this instance cache dir
	 */
	public void clearCache() {
		DiskLruCache.clearCache(mCacheDir);
	}

	/**
	 * Removes all disk cache entries from the application cache directory in the uniqueName
	 * sub-directory.
	 *
	 * @param context The context to use
	 * @param uniqueName A unique cache directory name to append to the app cache directory
	 */
	public static void clearCache(Context context, String uniqueName) {
		File cacheDir = getDiskCacheDir(context, uniqueName);
		clearCache(cacheDir);
	}

	/**
	 * Removes all disk cache entries from the given directory. This should not be called directly,
	 * call {@link DiskLruCache#clearCache(Context, String)} or {@link DiskLruCache#clearCache()}
	 * instead.
	 *
	 * @param cacheDir The directory to remove the cache files from
	 */
	private static void clearCache(File cacheDir) {
		final File[] files = cacheDir.listFiles(cacheFileFilter);
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/**
	 * Get a usable cache directory (external if available, internal otherwise).
	 *
	 * @param context The context to use
	 * @param uniqueName A unique directory name to append to the cache dir
	 * @return The cache dir
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {

		// Check if media is mounted or storage is built-in, if so, try and use external cache dir
		// otherwise use internal cache dir
		//����豸�Ƿ��в����Ƴ��Ĵ������Ĵ洢�����еĻ������������棬û�о����á�
		final String cachePath = (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
				|| !CacheUtils.isExternalStorageRemovable())&& CacheUtils.getExternalCacheDir(context)!=null ? CacheUtils.getExternalCacheDir(context).getPath()
				: context.getCacheDir().getPath();
		if (TextUtils.isEmpty(uniqueName)) {
			return new File(cachePath);
		} else {
			return new File(cachePath + File.separator + uniqueName);
		}
	}

	/**
	 * Creates a constant cache file path given a target cache directory and an image key.
	 *
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	public static String createFilePath(File cacheDir, String key) {
		/* try {*/
		// Use URLEncoder to ensure we have a valid filename, a tad hacky but it will do for
		// this example
		/*return cacheDir.getAbsolutePath() + File.separator +
		        CACHE_FILENAME_PREFIX + URLEncoder.encode(key.replace("*", ""), "UTF-8");*/
		return cacheDir.getAbsolutePath();
		/* } catch (final UnsupportedEncodingException e) {
		 	LogUtil.LogD( "createFilePath - " + e);
		     
		 }*/

		//        return null;
	}

	/**
	 * Create a constant cache file path using the current cache directory and an image key.
	 *
	 * @param key
	 * @return
	 */
	public String createFilePath(String key) {
		return createFilePath(mCacheDir, key);
	}

}
