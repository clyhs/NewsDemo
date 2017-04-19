package org.demo.cn.adapter;

import org.demo.cn.R;
import org.demo.cn.base.BaseViewHolder;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FoundCategoryAdapter extends BaseAdapter {

	private Context mContext;

	public  String[] mTitles = { "股票", "基金", "证券", "汽车", "美食", "彩票" };
	
	public FoundCategoryAdapter(Context context) {
		// TODO Auto-generated constructor stub
		super();
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTitles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mTitles[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.found_category_gridview_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		tv.setText(mTitles[position]);
		return convertView;
	}

}
