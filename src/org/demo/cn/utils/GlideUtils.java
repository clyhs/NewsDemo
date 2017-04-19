package org.demo.cn.utils;
import org.demo.cn.app.App;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


import cn.bingoogolapple.badgeview.BGABadgeImageView;

public class GlideUtils {
    /**
     * ����Բ��ͼƬ
     *
     * @param
     * @param
     */
    public static void loadCircleImage(BitmapTypeRequest bitmapTypeRequest, final Object object) {
        bitmapTypeRequest.into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable circleDrawable = DrawableUtils.roundedBitmap(resource);
                if (object instanceof ImageView) {
                    ((ImageView) object).setImageDrawable(circleDrawable);
                    return;
                }
                if (object instanceof Preference) {
                    ((Preference) object).setIcon(circleDrawable);
                }

            }

        });
    }


    /**
     * ���ش�VЧ��
     *
     * @param imageView
     */
    public static void loadBadgeImage(int vIcon, BitmapTypeRequest bitmapTypeRequest, final BGABadgeImageView imageView) {
        final Bitmap avatorBadgeBitmap = BitmapFactory.decodeResource(App.getInstance().getResources(), vIcon);
        bitmapTypeRequest.into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable circleDrawable = DrawableUtils.roundedBitmap(resource);
                imageView.setImageDrawable(circleDrawable);
                imageView.showDrawableBadge(avatorBadgeBitmap);
            }

        });
    }
}
