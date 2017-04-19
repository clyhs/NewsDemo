package org.demo.cn;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

import org.demo.cn.adapter.CommentAdapter;
import org.demo.cn.base.BaseActivity;
import org.demo.cn.http.asy.CommentListAsy;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.http.response.CommentResponse;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.DensityUtil;
import org.demo.cn.utils.SafeHandler;
import org.demo.cn.utils.SystemUtil;
import org.demo.cn.vo.ArticleVo;
import org.demo.cn.vo.CommentVo;
import org.demo.cn.vo.VideoVo;
import org.demo.cn.widget.MediaControllerView;
import org.demo.cn.widget.MediaHeaderView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class MediaActivity extends BaseActivity implements 
IXListViewRefreshListener, IXListViewLoadMore{
	
	private VideoVo mVideoVo;
	private MediaHeaderView mMediaHeaderView;
	private RelativeLayout mMediaComment;
	private RelativeLayout mMediaRelative;
	private TextView tv;
	
	
	private Context mContext;
	private boolean isFirstIn = true;
	private boolean isConnNet = false;
	private boolean isLoadingDataFromNetWork;
	
	private int currentPage = 1;
	private XListView mXListView;
	private List<CommentVo> mDatas = new ArrayList<CommentVo>();
	private CommentAdapter mCommentAdapter;
    private int mLoadMode = 0;
	private int mTypeId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Vitamio.isInitialized(MediaActivity.this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_media);
		this.mContext =this;
		this.mVideoVo = (VideoVo) getIntent().getSerializableExtra("vo");
		initView();
		initComment();
	}
	
	private void initView() {
		mMediaHeaderView = new MediaHeaderView(this,findViewById(R.id.media_header));	
		mMediaHeaderView.binddata(this.mVideoVo);
		
		//tv = (TextView) findViewById(R.id.mediatextview);
		//tv.setText("helloworldsss");
		
		mMediaComment = (RelativeLayout) findViewById(R.id.media_comment);
		mMediaHeaderView.getmMediaControllerView().setmMediaComment(mMediaComment);
		
		mMediaRelative = (RelativeLayout) findViewById(R.id.media_relate);
		
		if(mMediaRelative!=null){
			mMediaHeaderView.getmMediaControllerView().setmMediaRelative(mMediaRelative);
		}
		
		//RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) mMediaRelative.getLayoutParams();
		//lp.height = DensityUtil.dip2px(this, 296);
		//mMediaRelative.setLayoutParams(lp);
		if(!mMediaHeaderView.getmMediaControllerView().isFullScreen()){
			LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,DensityUtil.dip2px(this, Constant.MEDIA_HEIGHT)); 
	    	if(mMediaRelative!=null){	
				mMediaRelative.setLayoutParams(lp);
			}
			if(mMediaComment!=null){
				mMediaComment.setVisibility(View.VISIBLE);
			}
			mMediaHeaderView.getmMediaControllerView().initVideoView();
	    	mMediaHeaderView.getmMediaControllerView().getVideoView().setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
	    	
			
		}
	
	}
	
	
	
	private void initComment(){
		mCommentAdapter = new CommentAdapter(this);
		mXListView = (XListView) findViewById(R.id.media_comment_xlv);
		mXListView.setAdapter(mCommentAdapter);
		mXListView.setPullRefreshEnable(this);
		mXListView.setPullLoadEnable(this);
		
		if (isFirstIn)
		{
			/**
			 * 进来时直接刷新
			 */
			mXListView.startRefresh();
			isFirstIn = false;
		} else
		{
			mXListView.NotRefreshAtBegin();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		return mMediaHeaderView.getmMediaControllerView().onTouchEvent(event);
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    
	    Log.v("CNetHelper", "here is config");

	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

	    	mMediaHeaderView.getmMediaControllerView().getVideoView().setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
	        Log.v("org.demo.cn", "landscape");

	    } else {

	    	mMediaHeaderView.getmMediaControllerView().getVideoView().setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
	    	Log.v("org.demo.cn", "stretch");
	    }

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		this.mLoadMode = Constant.LOAD_MORE;
		new CommentListAsy(this.mContext,this.currentPage+1,0).execute(mHandle);
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		this.mLoadMode = Constant.LOAD_REFREASH;
		new CommentListAsy(this.mContext,this.currentPage,0).execute(mHandle);
		
	}
	
	private SafeHandler	mHandle	= new SafeHandler(mContext) {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int id = msg.what;
			switch(id){
			case Constant.NET_SUCCESS:
				
				List<CommentVo> result = new ArrayList<CommentVo>();
				CommentResponse alr = (CommentResponse) msg.obj;
				if (alr == null) {
					return;
				}
				result = alr.getData();
				
				
				if(mLoadMode==Constant.LOAD_MORE){
					mDatas.addAll(result);
					mCommentAdapter.addAll(mDatas);
				}else if(mLoadMode==Constant.LOAD_REFREASH){
					
					mDatas = result;
					
					mCommentAdapter.setDatas(mDatas);
				}else{
					mCommentAdapter.setDatas(mDatas);
				}
				
				//mXListView.setRefreshTime(getRefreashTime(mTypeId));
				mXListView.stopRefresh();
				mXListView.stopLoadMore();
				
				break;
			case Constant.NET_UNKNOW:
			case Constant.NET_FAIL:
				
			default :
				//mXListView.setRefreshTime(getRefreashTime(mTypeId));
				mXListView.stopRefresh();
				mXListView.stopLoadMore();
				SystemUtil.showToast(mContext, msg.obj.toString());
			break;
			
			}
			
			mCommentAdapter.notifyDataSetChanged();
			
			
		}
		
		
		
	};
	
	
	
	
	

	
	
	

}
