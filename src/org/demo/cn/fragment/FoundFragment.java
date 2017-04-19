package org.demo.cn.fragment;

import org.demo.cn.R;
import org.demo.cn.adapter.FoundCategoryAdapter;
import org.demo.cn.widget.MyGridView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FoundFragment extends RootFragment {

	private String TAG = "FoundFragment";
	
	private MyGridView mGridView;
	
	public FoundFragment(){}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		View oView = inflater.inflate(R.layout.fragment_found_layout, container, false);
		mGridView =(MyGridView)oView.findViewById(R.id.gridview);
		
		return oView;
	}
	
	private void initView(){
		mGridView.setAdapter(new FoundCategoryAdapter(getActivity()));
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

