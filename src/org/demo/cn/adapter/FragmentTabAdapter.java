package org.demo.cn.adapter;

import java.util.List;

import org.demo.cn.R;
import org.demo.cn.fragment.RootFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioGroup;

public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener {

	private List<RootFragment> fragments; // 一个tab页面对应一个Fragment
	private RadioGroup rgs; // 用于切换tab
	private FragmentActivity fragmentActivity;// Fragment所属的Activity
	private int fragmentContentId; // Activity中所要被替换的区域的id

	private FragmentManager fragmentManager;


	private int currentTab; // 当前Tab页面索引

	private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

	public FragmentTabAdapter(FragmentActivity fragmentActivity,
			List<RootFragment> fragments, int fragmentContentId,
			RadioGroup rgs, FragmentManager fragmentManager) {
		this.fragments = fragments;
		this.rgs = rgs;
		this.fragmentActivity = fragmentActivity;
		this.fragmentContentId = fragmentContentId;
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();

		if (fragmentActivity.getSupportFragmentManager().getFragments() != null
				&& fragmentActivity.getSupportFragmentManager().getFragments().size() > 0) {
			for (Fragment cf : fragmentActivity.getSupportFragmentManager().getFragments()) {
				ft.remove(cf);
			}
		}

		ft.add(fragmentContentId, fragments.get(0));
		ft.commit();
		currentTab = 0;
		rgs.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		// TODO Auto-generated method stub
		for (int i = 0; i < rgs.getChildCount(); i++) {
			if (rgs.getChildAt(i).getId() == checkedId) {
				// replace(i);
				RootFragment fragment = fragments.get(i);
				FragmentTransaction ft = obtainFragmentTransaction(i);
				getCurrentFragment().onPause(); // 暂停当前tab
				// getCurrentFragment().onStop(); // 暂停当前tab

				if (fragment.isAdded()) {
					// fragment.onStart(); // 启动目标tab的onStart()
					fragment.onResume(); // 启动目标tab的onResume()
				} else {
					ft.add(fragmentContentId, fragment);
				}
				showTab(i); // 显示目标tab
				ft.commit();
			}
			if (null != onRgsExtraCheckedChangedListener) {
				onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(
						radioGroup, checkedId, i);
			}
		}
	}

	public void replace(int index) {
		FragmentTransaction ft = obtainFragmentTransaction(index);
		ft.replace(fragmentContentId, fragments.get(index));
		//ft.addToBackStack(null);

		ft.commit();

		currentTab = index;

	}

	/**
	 * 切换tab
	 * 
	 * @param idx
	 */
	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			RootFragment fragment = fragments.get(i);

			FragmentTransaction ft = obtainFragmentTransaction(idx);
			if (idx == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commit();
		}
		currentTab = idx; // 更新目标tab为当前tab
	}
	
	
	private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        
        return ft;
    }

	public int getCurrentTab() {
		return currentTab;
	}

	public RootFragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
		return onRgsExtraCheckedChangedListener;
	}

	public void setOnRgsExtraCheckedChangedListener(
			OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
		this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
	}

	public static class OnRgsExtraCheckedChangedListener {
		public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
				int checkedId, int index) {

		}
	}

}
