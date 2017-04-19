package org.demo.cn.fragment;

import org.demo.cn.utils.AppUtil;

import android.support.v4.app.Fragment;

public class ItemFragment extends Fragment {

	public ItemFragment(){}
	
	public String getRefreashTime(int typeId){
		StringBuffer sb = new StringBuffer();
		
		try{
			sb.append(AppUtil.getRefreashTime(getActivity(), typeId));
		}
		catch(Exception e){
			
		}
		
		
		return sb.toString();
	}
}
