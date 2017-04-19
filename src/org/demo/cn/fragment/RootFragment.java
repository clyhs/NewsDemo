package org.demo.cn.fragment;

import org.demo.cn.adapter.BackPressImpl;
import org.demo.cn.adapter.OnBackPressListener;
import org.demo.cn.utils.ImageUtil;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class RootFragment extends Fragment {
	/*
	private RootFragment previousFragment;
	private int indexId;
	public static String BUNDLE_INDEXID = "INDEXID";*/
	protected boolean inited;
	protected Context context;
	
	abstract protected void initData(Bundle savedInstanceState);

	public RootFragment(){
		super();
	}
	
	public RootFragment(Bundle bd) {
		super();
		this.setArguments(bd);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		if (ImageLoader.getInstance().isInited()) {
			ImageLoader.getInstance().destroy();
		}
		initImageLoader();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!inited) {
			initData(savedInstanceState);
			inited = true;
		}
	}
	
	public void initImageLoader(){
		// ³õÊ¼»¯Í¼Æ¬¼ÓÔØ»º´æÄ£¿é
	    ImageLoader.getInstance().init(
						ImageUtil.getSimpleImageLoaderConfig(getActivity(),true));
	}
	
	/*
	public RootFragment(Bundle bd) {
		super();
		this.setArguments(bd);
	}
	
	public RootFragment(int indexId) {
		super();
		Bundle bd = new Bundle();
		bd.putInt(BUNDLE_INDEXID, indexId);
		this.setArguments(bd);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Bundle bd = getArguments();
			if (bd != null) {
				indexId = getArguments().getInt(BUNDLE_INDEXID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getRootId() {
		return indexId;
	}
	
	public void setRootId(int rootId) {
		Bundle bd = new Bundle();
		bd.putInt(BUNDLE_INDEXID, rootId);
		this.setArguments(bd);
		this.indexId = rootId;
	}
	
	public void setPreviousFragment(RootFragment previousFragment) {
		this.previousFragment = previousFragment;
	}

	public RootFragment getPrevious() {
		return previousFragment;
	}*/

	
	
	
	
}
