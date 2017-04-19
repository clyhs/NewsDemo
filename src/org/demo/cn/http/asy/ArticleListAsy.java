package org.demo.cn.http.asy;

import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.http.service.ArticleService;
import org.demo.cn.utils.AppUtil;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.SystemUtil;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class ArticleListAsy extends AsyncTask<Handler, Object, Object> {
	
	private Context mContext;
	private Integer mPageNo ;
	private Integer mTypeId;
	
	
	public ArticleListAsy(Context context,Integer pageNo,Integer typeId){
		this.mContext = context;
		this.mPageNo = pageNo;
		this.mTypeId = typeId;
	}

	@Override
	protected Object doInBackground(Handler... params) {
		// TODO Auto-generated method stub
		
		ArticleService articleService = new ArticleService(this.mContext);
		
		try {
			Thread.sleep(1000);
			
			AppUtil.setRefreashTime(this.mContext, this.mTypeId);
			
			ArticleListResponse alr = articleService.getArticleListResponse(this.mPageNo,this.mTypeId);
			
			SystemUtil.SendMSG(params[0], Constant.NET_SUCCESS,
					alr);
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
