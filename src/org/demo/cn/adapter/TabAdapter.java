package org.demo.cn.adapter;

import org.demo.cn.fragment.NewsItemFragment;
import org.demo.cn.utils.Constant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.ViewGroup;

public class TabAdapter extends FragmentPagerAdapter {

	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

	public TabAdapter(FragmentManager fm) {

		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub

		Fragment f = new NewsItemFragment(Integer.valueOf(Constant.TITLESID[position]));
		Bundle b = new Bundle();
		String title = Constant.TITLES[position];
		b.putString("TITLES", title);
		f.setArguments(b);
		return f;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return Constant.TITLES[position];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Constant.TITLES.length;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container,
				position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	public Fragment getRegisteredFragment(int position) {
		return registeredFragments.get(position);
	}

}
