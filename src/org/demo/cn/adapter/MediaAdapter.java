package org.demo.cn.adapter;

import java.util.List;

import org.demo.cn.MediaActivity;
import org.demo.cn.R;
import org.demo.cn.utils.AppUtil;
import org.demo.cn.vo.ArticleVo;
import org.demo.cn.vo.VideoVo;
import org.demo.cn.widget.ShadowImageView;
import org.demo.cn.widget.TextImageView;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MediaAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<VideoVo> mDatas;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	

	public MediaAdapter(Context context, List<VideoVo> datas) {
		this.mDatas = datas;
		mInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}
	
	public void addAll(List<VideoVo> mDatas)
	{
		this.mDatas.addAll(mDatas);
	}

	public void setDatas(List<VideoVo> mDatas)
	{
		this.mDatas.clear();
		this.mDatas.addAll(mDatas);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder2 holder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.media_gridview_test, null);
			holder = new ViewHolder2(convertView);

			//holder.mTitle = (TextView) convertView.findViewById(R.id.id_grid_item_text);
			
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder2) convertView.getTag();
		}
		final VideoVo video = mDatas.get(position);
		
		holder.mImageViewCover.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent((Activity)mInflater.getContext(), MediaActivity.class);
				intent.putExtra("medias_id",Integer.valueOf( video.getId()) );
				intent.putExtra("vo", video);
				mInflater.getContext().startActivity(intent);
			}
			
		});
		
		
		//holder.mTitle.setText(video.getName()) ;
		holder.mTextViewLikesCount.setTextImageStart(18, R.mipmap.ic_thumb_up_gray_18dp, " " + "字体测试字体大小".toString());
		holder.mTextViewRecommendCaption.setText("title");
		Glide.with(mInflater.getContext())
             .load(video.getImg())
             .placeholder(R.mipmap.bg_video_default).fitCenter()
             .into(holder.mImageViewCover);
		AppUtil.loadSmallUserAvata((Activity)mInflater.getContext() ,null, holder.mImageViewAvatar);
		return convertView;
	}
	
	private final class ViewHolder
	{
		TextView mTitle;
		//TextView mContent;
		//ImageView mImg;
		//TextView mDate;
	}
	
	
	public static class ViewHolder2 extends RecyclerView.ViewHolder {
        public final BGABadgeImageView mImageViewAvatar;
        public final ShadowImageView mImageViewCover;
        public final TextImageView mTextViewLikesCount;
        public final TextView mTextViewRecommendCaption;

        public ViewHolder2(View view) {
            super(view);
            mImageViewAvatar = (BGABadgeImageView) view.findViewById(R.id.imageView_avatar);
            mImageViewCover = (ShadowImageView) view.findViewById(R.id.imageview_cover_pic);
            mTextViewLikesCount = (TextImageView) view.findViewById(R.id.textView_likes_count);
            mTextViewRecommendCaption = (TextView) view.findViewById(R.id.textView_recommend_caption);
        }
    }


}
