package org.demo.cn.adapter;

import java.util.ArrayList;
import java.util.List;

import org.demo.cn.R;
import org.demo.cn.vo.ArticleVo;
import org.demo.cn.vo.CommentVo;
import org.demo.cn.widget.CommentTextView;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	
	private List<CommentVo> mDatas= new ArrayList<CommentVo>();
	private LayoutInflater mInflater;
	
	public CommentAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
	}
	
	public void addAll(List<CommentVo> mDatas)
	{
		this.mDatas.addAll(mDatas);
	}

	public void setDatas(List<CommentVo> mDatas)
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
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.media_comment_list,null);
			holder = new ViewHolder();
			holder.commentInfo = (CommentTextView) convertView.findViewById(R.id.media_comment_txt);
			holder.mImageViewAvatar = (BGABadgeImageView) convertView.findViewById(R.id.media_comment_avatar);
			holder.mUsername = (TextView) convertView.findViewById(R.id.media_comment_screen_name);
			holder.mCreateTime = (TextView) convertView.findViewById(R.id.media_comment_created_at);
			holder.mLikeCount = (TextView) convertView.findViewById(R.id.media_comment_liked_count);
			
			
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		CommentVo vo = (CommentVo) getItem(position);
		holder.commentInfo.setText(vo.getCommentInfo());
		holder.mLikeCount.setText("100");
		holder.mCreateTime.setText(vo.getCreateTime());
		holder.mUsername.setText(vo.getUsername());
		
		return convertView;
	}
	
	private final class ViewHolder
	{
		CommentTextView commentInfo;
		BGABadgeImageView mImageViewAvatar;
		TextView mUsername;
		TextView mCreateTime;
		TextView mLikeCount;
	}

}
