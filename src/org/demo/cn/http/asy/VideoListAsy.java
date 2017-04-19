package org.demo.cn.http.asy;

import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.response.ArticleListResponse;
import org.demo.cn.http.response.VideoListResponse;
import org.demo.cn.http.service.VideoService;
import org.demo.cn.utils.AppUtil;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.SystemUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class VideoListAsy extends AsyncTask<Handler, Object, Object> {

	private Context mContext;
	private Integer mPageNo;

	public VideoListAsy(Context context, Integer pageNo) {
		this.mContext = context;
		this.mPageNo = pageNo;
	}

	@Override
	protected Object doInBackground(Handler... params) {
		// TODO Auto-generated method stub

		VideoService vs = new VideoService(this.mContext);

		try {
			Thread.sleep(1000);
			VideoListResponse vlr = vs.getVideoListResponse(this.mPageNo);
			SystemUtil.SendMSG(params[0], Constant.NET_SUCCESS,
					vlr);
		} catch (CNetHelperException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			SystemUtil.SendMSG(params[0], Constant.NET_FAIL,
					e.getMessage());
		} catch (Exception e) {
			SystemUtil.SendMSG(params[0], Constant.NET_UNKNOW,
					e.getMessage());
		}
		return null;
	}

}
