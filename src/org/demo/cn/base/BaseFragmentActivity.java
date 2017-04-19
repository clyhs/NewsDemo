package org.demo.cn.base;

import org.demo.cn.adapter.FragmentTabAdapter;
import org.demo.cn.fragment.RootFragment;

import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;

public abstract class BaseFragmentActivity extends FragmentActivity {
	
	public SparseArray<RootFragment> states = new SparseArray<RootFragment>();
	
	public FragmentTabAdapter tabAdapter ;
	
	private int rootContentId;
	protected RootFragment currentFragment;
	
	public void putFragment(RootFragment fragment) {
		//states.put(fragment.getRootId(), fragment);
	}

	

	
}
