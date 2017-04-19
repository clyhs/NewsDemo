package org.demo.cn.adapter;

import java.util.List;

import org.demo.cn.R;
import org.demo.cn.vo.ArticleVo;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;





import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	
	private List<ArticleVo> mDatas;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public NewsAdapter(Context context,List<ArticleVo> datas){
		this.mDatas = datas;
		mInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.images)
				.showImageForEmptyUri(R.drawable.images).showImageOnFail(R.drawable.images).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).displayer(new FadeInBitmapDisplayer(300))
				.build();
	}
	
	public void addAll(List<ArticleVo> mDatas)
	{
		this.mDatas.addAll(mDatas);
	}

	public void setDatas(List<ArticleVo> mDatas)
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
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.news_item, null);
			holder = new ViewHolder();

			holder.mContent = (TextView) convertView.findViewById(R.id.id_content);
			holder.mTitle = (TextView) convertView.findViewById(R.id.id_title);
			holder.mDate = (TextView) convertView.findViewById(R.id.id_date);
			holder.mImg = (ImageView) convertView.findViewById(R.id.id_newsImg);

			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		ArticleVo newsItem = mDatas.get(position);
		holder.mTitle.setText(newsItem.getTitle());
		holder.mContent.setText(newsItem.getShortContent());
		holder.mDate.setText(newsItem.getCreateTime());
		if (newsItem.getImages() != null)
		{
			holder.mImg.setVisibility(View.VISIBLE);
			imageLoader.displayImage(newsItem.getImages(), holder.mImg, options);
		} else
		{
			holder.mImg.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private final class ViewHolder
	{
		TextView mTitle;
		TextView mContent;
		ImageView mImg;
		TextView mDate;
	}

}
