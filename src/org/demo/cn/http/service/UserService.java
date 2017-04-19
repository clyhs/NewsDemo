package org.demo.cn.http.service;

import java.util.HashMap;

import org.demo.cn.http.net.CNetHelper;
import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.net.HeaderParamsManger;
import org.demo.cn.utils.Constant;
import org.demo.cn.vo.UserVo;
import org.demo.cn.vo.VideoVo;

import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

public class UserService {

private Context mContext;
	
	private CNetHelper mHttpBase = null;
	
	public UserService(Context context){
		this.mContext = context;
	}
	
    public UserVo login(String username,String password) throws CNetHelperException{
		
		mHttpBase = new CNetHelper();
		int iRet = 0;
		HashMap<String, String> aParamMap = new HashMap<String, String>();
		String uname = (username == null )? "" : username;
		String upass = (password == null )? "" : password;
		aParamMap.put("username", uname);
		aParamMap.put("password", upass);
		iRet = CNetHelper.getNetState(this.mContext);
		if (iRet == CNetHelper.NET_NOT_CONNECTED) {
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE,
					"ÍøÂçÎ´Á¬½Ó");
		}
		String strResult = mHttpBase.doGetData(this.mContext, 
				Constant.BASEURL+Constant.GET_USER, aParamMap, 
				HeaderParamsManger.getHeaderMap(this.mContext));
		
		Gson gson = new Gson();
		UserVo vo = gson.fromJson(strResult.replaceAll("\\s*", ""), UserVo.class);
		
		Log.v("CNetHelper", "userVo:"+vo.getUsername());
		return vo;

	}
    
    public static void main(String[] args ){
    	Gson gson = new Gson();
    	String result = "{\"id\":1,\"username\":\"admin\",\"tel\":\"\",\"email\":\"clygd@126.com\",\"loginTime\":\"2016-03-14\",\"img\":\"\"}";
    	UserVo vo = gson.fromJson(result.replaceAll("\\s*", ""), UserVo.class);
    	
    	System.out.println(vo.getId());
    }
}
