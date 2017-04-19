package org.demo.cn.http.asy;

import org.demo.cn.http.net.CNetHelperException;
import org.demo.cn.http.response.VideoListResponse;
import org.demo.cn.http.service.VideoService;
import org.demo.cn.utils.Constant;
import org.demo.cn.utils.SystemUtil;
import org.demo.cn.vo.VideoVo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class VideoAsy extends AsyncTask<Handler, Object, Object> {
	
	private Context mContext;
	
	private Integer mId;
	
	public VideoAsy(Context context,Integer id){
		this.mId = id;
		this.mContext = context;
	}

	@Override
	protected Object doInBackground(Handler... params) {
		// TODO Auto-generated method stub
		
		VideoService vs = new VideoService(this.mContext);
		
		try {
			Thread.sleep(1000);
			VideoVo vo = vs.getVideo(this.mId);
			SystemUtil.SendMSG(params[0], Constant.NET_SUCCESS,
					vo);
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
