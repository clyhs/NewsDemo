package org.demo.cn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.demo.cn.R;
import org.demo.cn.vo.SimpleUserEntity;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

public class AppUtil
{
	/**
	 * 根据新闻类型获取上次更新的时间
	 * 
	 * @param newType
	 * @return
	 */
	public static String getRefreashTime(Context context, int newType)
	{
		String timeStr = PreferenceUtil.readString(context, "NEWS_" + newType);
		if (TextUtils.isEmpty(timeStr))
		{
			return "我好笨，忘记了...";
		}
		return timeStr;
	}
	
	
	/**
	 * 根据新闻类型设置上次更新的时间
	 * 
	 * @param newType
	 * @return
	 */
	public static void setRefreashTime(Context context, int newType)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PreferenceUtil.write(context, "NEWS_" + newType,df.format(new Date()));
	}
	
	
	public static void loadBigUserAvata(Activity activity, SimpleUserEntity entity, BGABadgeImageView imageView) {
        loadImage(activity, R.mipmap.ic_verified_account_24dp, entity, imageView);
    }

    public static void loadSmallUserAvata(Activity activity, SimpleUserEntity entity, BGABadgeImageView imageView) {
        loadImage(activity, R.mipmap.ic_verified_account_16dp, entity, imageView);
    }

    private static void loadImage(Activity activity, int v, SimpleUserEntity entity, BGABadgeImageView imageView) {
        //if (TextUtils.isEmpty(entity.getAvatar()))
            //return;
        BitmapTypeRequest builder = Glide.with(activity).load("avtor").asBitmap();
        if (/*entity.getVerified()*/false) {
            GlideUtils.loadBadgeImage(v, builder, imageView);
        } else {
            GlideUtils.loadCircleImage(builder, imageView);
        }
    }
}
