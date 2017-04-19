package org.demo.cn.http.asy;

import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.http.response.CommentResponse;
import org.demo.cn.http.service.ArticleService;
import org.demo.cn.http.service.CommentService;
import org.demo.cn.utils.AppUtil;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.SystemUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class CommentListAsy extends AsyncTask<Handler, Object, Object>{

	
	private Context mContext;
	private Integer mPageNo ;
	private Integer mId;
	
	
	public CommentListAsy(Context context,Integer pageNo,Integer id){
		this.mContext = context;
		this.mPageNo = pageNo;
		this.mId = id;
	}
	@Override
	protected Object doInBackground(Handler... params) {
		// TODO Auto-generated method stub
        CommentService commentService = new CommentService(this.mContext);
		
		try {
			Thread.sleep(1000);
			
			AppUtil.setRefreashTime(this.mContext, this.mId);
			
			CommentResponse alr = commentService.getCommentListResponse(this.mPageNo,this.mId);
			
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
