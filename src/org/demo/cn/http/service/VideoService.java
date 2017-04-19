package org.demo.cn.http.service;

import java.util.HashMap;

import org.demo.cn.http.net.CNetHelper;
import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.net.HeaderParamsManger;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.http.response.VideoListResponse;
import org.demo.cn.utils.Constant;
import org.demo.cn.vo.VideoVo;

import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

public class VideoService {
	
    private Context mContext;
	
	private CNetHelper mHttpBase = null;
	
	public VideoService(Context context){
		this.mContext = context;
	}
	
	public VideoListResponse getVideoListResponse(Integer pageNo) throws CNetHelperException{
		mHttpBase = new CNetHelper();
		int iRet = 0;
		HashMap<String, String> aParamMap = new HashMap<String, String>();
		Integer page = (pageNo == null )? 0 : pageNo;
		aParamMap.put("pageNo", String.valueOf(page));
		
		iRet = CNetHelper.getNetState(this.mContext);
		if (iRet == CNetHelper.NET_NOT_CONNECTED) {
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE,
					"网络未连接");
		}
		
		String strResult = mHttpBase.doGetData(this.mContext, 
				Constant.BASEURL+Constant.GET_VIDEOLIST, aParamMap, 
				HeaderParamsManger.getHeaderMap(this.mContext));
		
		
		Gson gson = new Gson();
		
		VideoListResponse vlr = gson.fromJson(strResult.replaceAll("\\s*", ""), VideoListResponse.class);
		Log.v("CNetHelper", vlr.getData().size()+"");
		return vlr;
		
	}
	
	
	public VideoVo getVideo(Integer id) throws CNetHelperException{
		
		mHttpBase = new CNetHelper();
		int iRet = 0;
		HashMap<String, String> aParamMap = new HashMap<String, String>();
		Integer nId = (id == null )? 0 : id;
		aParamMap.put("id", String.valueOf(nId));
		iRet = CNetHelper.getNetState(this.mContext);
		if (iRet == CNetHelper.NET_NOT_CONNECTED) {
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE,
					"网络未连接");
		}
		String strResult = mHttpBase.doGetData(this.mContext, 
				Constant.BASEURL+Constant.GET_VIDEO, aParamMap, 
				HeaderParamsManger.getHeaderMap(this.mContext));
		
		Gson gson = new Gson();
		VideoVo vo = gson.fromJson(strResult.replaceAll("\\s*", ""), VideoVo.class);
		Log.v("CNetHelper", "");
		return vo;
		
	}

}
