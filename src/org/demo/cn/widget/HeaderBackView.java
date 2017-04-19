package org.demo.cn.widget;

import org.demo.cn.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class HeaderBackView {

	private Activity mActivity;
	
	private ImageView mImageViewBack;
	
    public HeaderBackView(){
    	
    } 
    
    public HeaderBackView(Activity activity, View view){
    	this.mActivity = activity;
    	
    	mImageViewBack = (ImageView) view.findViewById(R.id.header_back);
    	
    	mImageViewBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivity.finish();
			}
    		
    	});
    }
    
    
	
}
