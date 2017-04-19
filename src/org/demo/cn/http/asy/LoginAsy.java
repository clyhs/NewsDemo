package org.demo.cn.http.asy;

import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.http.service.ArticleService;
import org.demo.cn.http.service.UserService;
import org.demo.cn.utils.AppUtil;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.SystemUtil;
import org.demo.cn.vo.UserVo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class LoginAsy extends AsyncTask<Handler, Object, Object> {
	
	private Context mContext;
	private String mUsername ;
	private String mPassword;
	
	public LoginAsy(Context context,String username,String password){
		this.mContext = context;
		this.mUsername = username;
		this.mPassword = password;
	}

	@Override
	protected Object doInBackground(Handler... params) {
		// TODO Auto-generated method stub
		
        UserService us = new UserService(this.mContext);
		
		try {
			Thread.sleep(1000);

			UserVo vo = us.login(this.mUsername, this.mPassword);
			
			SystemUtil.SendMSG(params[0], Constant.NET_SUCCESS,
					vo);
		} catch (CNetHelperException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			SystemUtil.SendMSG(params[0], Constant.NET_FAIL,
					e.getMessage());
		}catch(Exception e){
			//e.printStackTrace();
			SystemUtil.SendMSG(params[0], Constant.NET_UNKNOW,
					e.getMessage());
		}
		
		return null;
	}

}
