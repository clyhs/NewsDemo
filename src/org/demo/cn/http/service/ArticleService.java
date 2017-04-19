package org.demo.cn.http.service;

import java.util.HashMap;

import org.demo.cn.http.net.CNetHelper;
import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.net.HeaderParamsManger;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.utils.Constant;

import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

public class ArticleService {
	
	private Context mContext;
	
	private CNetHelper mHttpBase = null;
	
	
	public ArticleService(Context context){
		this.mContext = context;
	}
	
	public ArticleListResponse getArticleListResponse(Integer pageNo,Integer typeId) 
			throws CNetHelperException{
		mHttpBase = new CNetHelper();
		int iRet = 0;
		HashMap<String, String> aParamMap = new HashMap<String, String>();
		Integer page = (pageNo == null )? 0 : pageNo;
		Integer type = (typeId==null)?0:typeId;
		aParamMap.put("pageNo", String.valueOf(page));
		aParamMap.put("typeId", String.valueOf(type));
		
		iRet = CNetHelper.getNetState(this.mContext);
		if (iRet == CNetHelper.NET_NOT_CONNECTED) {
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE,
					"ÍøÂçÎ´Á¬½Ó");
		}
		String strResult = mHttpBase.doGetData(this.mContext, 
				Constant.BASEURL+Constant.GET_ARTICLELIST, aParamMap, 
				HeaderParamsManger.getHeaderMap(this.mContext));
		
		
		Gson gson = new Gson();
		ArticleListResponse alr = gson.fromJson(strResult.replaceAll("\\s*", ""), ArticleListResponse.class);
		Log.v("CNetHelper", alr.getData().size()+"");
		return alr;
		
	}
	
	

}
