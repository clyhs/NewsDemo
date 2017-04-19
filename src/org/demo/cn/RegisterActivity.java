package org.demo.cn;

import org.demo.cn.base.BaseActivity;
import org.demo.cn.widget.HeaderBackView;

import android.os.Bundle;

public class RegisterActivity extends BaseActivity {

	private HeaderBackView mHeaderBackView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
	}
	
	private void initView(){
		mHeaderBackView = new HeaderBackView(this,findViewById(R.id.header_back));
	}

}
