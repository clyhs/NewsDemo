package org.demo.cn.http.service;

import java.util.HashMap;

import org.demo.cn.http.net.CNetHelper;
import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.net.HeaderParamsManger;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.http.response.CommentResponse;
import org.demo.cn.utils.Constant;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public class CommentService {

private Context mContext;
	
	private CNetHelper mHttpBase = null;
	
	
	public CommentService(Context context){
		this.mContext = context;
	}
	
	public CommentResponse getCommentListResponse(Integer pageNo,Integer id) 
			throws CNetHelperException{
		mHttpBase = new CNetHelper();
		int iRet = 0;
		HashMap<String, String> aParamMap = new HashMap<String, String>();
		Integer page = (pageNo == null )? 0 : pageNo;
		Integer nid = (id==null)?0:id;
		aParamMap.put("pageNo", String.valueOf(page));
		aParamMap.put("id", String.valueOf(nid));
		
		iRet = CNetHelper.getNetState(this.mContext);
		if (iRet == CNetHelper.NET_NOT_CONNECTED) {
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE,
					"ÍøÂçÎ´Á¬½Ó");
		}
		String strResult = mHttpBase.doGetData(this.mContext, 
				Constant.BASEURL+Constant.GET_COMMENTS, aParamMap, 
				HeaderParamsManger.getHeaderMap(this.mContext));
		
		
		Gson gson = new Gson();
		CommentResponse alr = gson.fromJson(strResult.replaceAll("\\s*", ""), CommentResponse.class);
		Log.v("CNetHelper", alr.getData().size()+"");
		return alr;
		
	}
}
