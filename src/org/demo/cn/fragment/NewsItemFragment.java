package org.demo.cn.fragment;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

import org.demo.cn.NewsActivity;
import org.demo.cn.R;
import org.demo.cn.adapter.NewsAdapter;
import org.demo.cn.http.asy.ArticleListAsy;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.utils.AppUtil;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.SafeHandler;
import org.demo.cn.utils.SystemUtil;
import org.demo.cn.vo.ArticleVo;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NewsItemFragment extends ItemFragment implements 
IXListViewRefreshListener, IXListViewLoadMore {
	

	
	
	
	private Context mContext;

	/**
	 * 是否是第一次进入
	 */
	private boolean isFirstIn = true;

	/**
	 * 是否连接网络
	 */
	private boolean isConnNet = false;

	/**
	 * 当前数据是否是从网络中获取的
	 */
	private boolean isLoadingDataFromNetWork;
	
	/**
	 * 当前页面
	 */
	private int currentPage = 1;
	
	/**
	 * 扩展的ListView
	 */
	private XListView mXListView;
	
	private NewsAdapter newsAdapter;
	
	private List<ArticleVo> mDatas = new ArrayList<ArticleVo>();
	
	private int mLoadMode = 0;
	
	private int mTypeId = 0;
	
	public NewsItemFragment(){}
	
	public NewsItemFragment(int typeId){
		this.mTypeId = typeId;
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
		View oView = inflater.inflate(R.layout.fragment_news_item_layout, container, false);
		
		return oView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		newsAdapter = new NewsAdapter(getActivity(), mDatas);
		
		/**
		 * 初始化
		 */
		mXListView = (XListView) getView().findViewById(R.id.id_xlistView);
		mXListView.setAdapter(newsAdapter);
		mXListView.setPullRefreshEnable(this);
		mXListView.setPullLoadEnable(this);
		mXListView.setRefreshTime(getRefreashTime(this.mTypeId));
		
		mXListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				ArticleVo vo = mDatas.get(position-1);
				Intent intent = new Intent(getActivity(), NewsActivity.class);
				intent.putExtra("articlevo", vo);
				startActivity(intent);
			}

		});
		
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
	public void onLoadMore() {
		// TODO Auto-generated method stub
		this.mLoadMode = Constant.LOAD_MORE;
		new ArticleListAsy(this.mContext,this.currentPage+1,this.mTypeId).execute(mHandle);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		this.mLoadMode = Constant.LOAD_REFREASH;
		new ArticleListAsy(this.mContext,this.currentPage,this.mTypeId).execute(mHandle);
		
	}
	
	private SafeHandler	mHandle	= new SafeHandler(mContext) {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int id = msg.what;
			switch(id){
			case Constant.NET_SUCCESS:
				
				List<ArticleVo> result = new ArrayList<ArticleVo>();
				ArticleListResponse alr = (ArticleListResponse) msg.obj;
				if (alr == null) {
					return;
				}
				result = alr.getData();
				
				
				if(mLoadMode==Constant.LOAD_MORE){
					mDatas.addAll(result);
					newsAdapter.addAll(mDatas);
				}else if(mLoadMode==Constant.LOAD_REFREASH){
					
					mDatas = result;
					
					newsAdapter.setDatas(mDatas);
				}else{
					newsAdapter.setDatas(mDatas);
				}
				
				mXListView.setRefreshTime(getRefreashTime(mTypeId));
				mXListView.stopRefresh();
				mXListView.stopLoadMore();
				
				break;
			case Constant.NET_UNKNOW:
			case Constant.NET_FAIL:
				
			default :
				mXListView.setRefreshTime(getRefreashTime(mTypeId));
				mXListView.stopRefresh();
				mXListView.stopLoadMore();
				SystemUtil.showToast(mContext, msg.obj.toString());
			break;
			
			}
			
			newsAdapter.notifyDataSetChanged();
			
			
		}
		
		
		
	};
	
	
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	
	

}
