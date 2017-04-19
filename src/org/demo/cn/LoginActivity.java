package org.demo.cn;

import org.demo.cn.app.App;
import org.demo.cn.base.BaseActivity;
import org.demo.cn.dao.UserDao;
import org.demo.cn.fragment.AccountFragment;
import org.demo.cn.http.asy.LoginAsy;
import org.demo.cn.http.response.VideoListResponse;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.SafeHandler;
import org.demo.cn.utils.SystemUtil;
import org.demo.cn.utils.UIUtils;
import org.demo.cn.vo.UserVo;
import org.demo.cn.widget.HeaderBackView;
import org.demo.cn.widget.LoadingDialog;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {
	
	private TextView mOtherLoginTV;
	private TextView mRegisterTv;
	private HeaderBackView mHeaderBackView;
	private EditText mUsername;
	private EditText mPassword;
	private Button mLoginBtn;
	
	private UserVo mUserVo;
	private LoadingDialog	mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}
	
	private void initView(){
		mOtherLoginTV = (TextView) findViewById(R.id.login_others_tv);
		mRegisterTv = (TextView) findViewById(R.id.login_reg_tv);
		mRegisterTv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
			
		});
		
		mHeaderBackView = new HeaderBackView(this,findViewById(R.id.header_back));
		mUsername = (EditText) findViewById(R.id.login_num_phone_edt);
		mPassword = (EditText) findViewById(R.id.login_pwd_edt);
		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mLoginBtn.setOnClickListener(mLoginListener);
		
		
	}
	
	private void login(){
		
		String username = mUsername.getText().toString().trim();
		String password = mPassword.getText().toString().trim();
	    if("".equals(username) || "".equals(password) || null == username || null == password){
			SystemUtil.showToast(this, "请输入用户名和密码！");
		}else{

			showProgressDialog();
			new LoginAsy(this,username,password).execute(mHandle);
		}
		
		
	}
	
	private void loginforresult(UserVo userVo){
		dismissProgressDiaog();
		if(userVo==null || null == userVo.getUsername()){
			SystemUtil.showToast(this, "登录失败");
		}else{
			UserDao userDao = new UserDao(this);
			userDao.add(userVo);
			Log.v("CNetHelper", "add end:"+userVo.getUsername());
			App.getInstance().userVo = userVo;
			Log.v("CNetHelper", "set end:"+userVo.getUsername());
			Intent intent = new Intent();
			intent.putExtra("uservo", userVo);
			setResult(AccountFragment.REQUEST_CODE_SUCCESS, intent);
			finish();
		}
		
	}
	
	/** 显示等待提示框 */
	private void showProgressDialog() {
		if (null == mProgressDialog)
			
			mProgressDialog = UIUtils.getInstance().getProgressDialog(this);
		mProgressDialog.show();
	}

	/** 取消等待提示框 */
	public void dismissProgressDiaog() {
		if (null != mProgressDialog)
			mProgressDialog.dismiss();
	}
	
	OnClickListener mLoginListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			login();
		}
		
	};
	
	
	private SafeHandler	mHandle	= new SafeHandler(this) {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int id = msg.what;
			switch(id){
			case Constant.NET_SUCCESS:
				UserVo vo = (UserVo) msg.obj;
				if (vo == null) {
					return;
				}
				mUserVo = vo;
				loginforresult(vo);
				break;
			case Constant.NET_UNKNOW:
			case Constant.NET_FAIL:
			default :
				dismissProgressDiaog();
			break;
			}
			
		}
		
	};

}
