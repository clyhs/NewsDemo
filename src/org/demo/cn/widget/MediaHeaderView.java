package org.demo.cn.widget;

import org.demo.cn.R;
import org.demo.cn.vo.VideoVo;

import android.app.Activity;
import android.util.Log;
import android.view.View;

public class MediaHeaderView {
	
	private Activity mActivity;
	
	private MediaControllerView mMediaControllerView;
	
	public MediaHeaderView(Activity activity, View view){
		this.mActivity = activity;
		
		mMediaControllerView = (MediaControllerView) view.findViewById(R.id.media_controller_view);
		
		if(mMediaControllerView==null){
			Log.v("CNetHelper", "mMediaControllerView is null");
		}else{
			Log.v("CNetHelper", "mMediaControllerView  is not null");
		}
		
	}
	
	public void binddata(final VideoVo vo){
		
		Log.v("CNetHelper", vo.getUrl().trim());
		//mMediaControllerView.setVideoPath("http://www.androidbook.com/akc/filestorage/android/documentfiles/3389/movie.mp4");
		
		mMediaControllerView.setmVideoVo(vo);
		
		//mMediaControllerView.setVideoPath(vo.getUrl());
		mMediaControllerView.setVideoFileName(vo.getName());
		
		//mMediaControllerView.start();
		Log.v("CNetHelper",vo.getUrl());
		
	}

	public MediaControllerView getmMediaControllerView() {
		return mMediaControllerView;
	}
	
	

}
