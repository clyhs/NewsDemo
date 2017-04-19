package org.demo.cn.fragment;

import org.demo.cn.R;
import org.demo.cn.adapter.MediaTabAdapter;
import org.demo.cn.adapter.TabAdapter;

import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MediaFragment extends RootFragment {

	private String TAG = "MediaFragment";
	
	private TabPageIndicator mPageIndicator;
	private ViewPager mViewPager;
	private FragmentPagerAdapter fragPagerAdapter;
	
	public MediaFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		
		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), R.style.StyledIndicators);
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		
		View oView = localInflater.inflate(R.layout.fragment_media_layout, container, false);
		
		mPageIndicator = (TabPageIndicator) oView
				.findViewById(R.id.page_indicator2);

		mViewPager = (ViewPager) oView.findViewById(R.id.view_pager2);
		
		FragmentManager fm = getChildFragmentManager();

		fragPagerAdapter = new MediaTabAdapter(fm);
		
		mViewPager.setAdapter(fragPagerAdapter);
		mPageIndicator.setViewPager(mViewPager, 0);
		
		return oView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}
}
