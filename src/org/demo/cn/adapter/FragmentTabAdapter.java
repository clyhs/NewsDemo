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

	private List<RootFragment> fragments; // һ��tabҳ���Ӧһ��Fragment
	private RadioGroup rgs; // �����л�tab
	private FragmentActivity fragmentActivity;// Fragment������Activity
	private int fragmentContentId; // Activity����Ҫ���滻�������id

	private FragmentManager fragmentManager;


	private int currentTab; // ��ǰTabҳ������

	private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // �����õ��������л�tabʱ�������µĹ���

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
				getCurrentFragment().onPause(); // ��ͣ��ǰtab
				// getCurrentFragment().onStop(); // ��ͣ��ǰtab

				if (fragment.isAdded()) {
					// fragment.onStart(); // ����Ŀ��tab��onStart()
					fragment.onResume(); // ����Ŀ��tab��onResume()
				} else {
					ft.add(fragmentContentId, fragment);
				}
				showTab(i); // ��ʾĿ��tab
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
	 * �л�tab
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
		currentTab = idx; // ����Ŀ��tabΪ��ǰtab
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
