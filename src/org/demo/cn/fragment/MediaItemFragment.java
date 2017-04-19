package org.demo.cn.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.demo.cn.R;
import org.demo.cn.adapter.MediaAdapter;
import org.demo.cn.adapter.NewsAdapter;
import org.demo.cn.http.asy.VideoListAsy;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.http.response.VideoListResponse;
import org.demo.cn.utils.AppUtil;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.SafeHandler;
import org.demo.cn.utils.SystemUtil;
import org.demo.cn.vo.ArticleVo;
import org.demo.cn.vo.VideoVo;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class MediaItemFragment extends ItemFragment {

	//private LinkedList<String> mListItems;
	private PullToRefreshGridView mPullRefreshListView;
	//private ArrayAdapter<String> mAdapter;
	
	private Context mContext;

	/**
	 * 是否是第一次进入
	 */
	private boolean isFirstIn = true;

	/**
	 * 是否连接网络
	 */
	private boolean isConnNet = false;
	
	private int currentPage = 1;

	//private int mItemCount = 10;
	
	private MediaAdapter mediaAdapter;
	private List<VideoVo> mDatas = new ArrayList<VideoVo>();

	private int mLoadMode = 0;

	public MediaItemFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.mContext = getActivity();
					
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View oView = inflater.inflate(R.layout.fragment_media_item_layout,
				container, false);
		return oView;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mediaAdapter = new MediaAdapter(getActivity(), mDatas);
		mPullRefreshListView = (PullToRefreshGridView) getView()
				.findViewById(R.id.pull_refresh_grid);
		mPullRefreshListView.setAdapter(mediaAdapter);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				Log.e("TAG", "onPullDownToRefresh"); // Do work to  
                String label = DateUtils.formatDateTime(  
                        getActivity().getApplicationContext(),  
                        System.currentTimeMillis(),  
                        DateUtils.FORMAT_SHOW_TIME  
                                | DateUtils.FORMAT_SHOW_DATE  
                                | DateUtils.FORMAT_ABBREV_ALL);  
                refreshView.getLoadingLayoutProxy()  
                        .setLastUpdatedLabel(label);  
                mLoadMode = Constant.LOAD_REFREASH;
                new VideoListAsy(mContext,currentPage).execute(mHandle);  
			}
			
			

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh  
                // the list here.  
				mLoadMode = Constant.LOAD_MORE;
                new VideoListAsy(mContext,currentPage+1).execute(mHandle); 
			}
		});
		
		
		
		if (isFirstIn)
		{
			/**
			 * 进来时直接刷新
			 */
			new VideoListAsy(mContext,currentPage).execute(mHandle);
			//mPullRefreshListView.onRefreshComplete();
			
			
			//mPullRefreshListView.setRefreshing(true);
			//
			isFirstIn = false;
		}
		
		
	}
    /*
	private void initDatas() {
		mListItems = new LinkedList<String>();

		for (int i = 0; i < mItemCount; i++) {
			mListItems.add(i + "");
		}
	}

	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mListItems.add("" + mItemCount++);
			mAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();
		}
	}*/
	
	private SafeHandler	mHandle	= new SafeHandler(mContext) {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int id = msg.what;
			switch(id){
			case Constant.NET_SUCCESS:
				VideoListResponse alr = (VideoListResponse) msg.obj;
				if (alr == null) {
					return;
				}
				mDatas = alr.getData();
				if(mLoadMode==Constant.LOAD_MORE){
					mediaAdapter.addAll(mDatas);
				}else if(mLoadMode==Constant.LOAD_REFREASH){
					mediaAdapter.setDatas(mDatas);
				}else{
					mediaAdapter.setDatas(mDatas);
				}
				mediaAdapter.notifyDataSetChanged();
				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshListView.onRefreshComplete();
				break;
			case Constant.NET_UNKNOW:
			case Constant.NET_FAIL:
			default :
				mediaAdapter.notifyDataSetChanged();
				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshListView.onRefreshComplete();
				SystemUtil.showToast(mContext, msg.obj.toString());
			break;
			}
			
		}
		
	};
}
