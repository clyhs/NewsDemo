package org.demo.cn.http.response;

import java.util.ArrayList;
import java.util.List;

import org.demo.cn.vo.VideoVo;

public class VideoListResponse extends CommonResponse {

	private String totals;
	
	private List<VideoVo> data = new ArrayList<VideoVo>();
	
	public VideoListResponse(){}

	public String getTotals() {
		return totals;
	}

	public void setTotals(String totals) {
		this.totals = totals;
	}

	public List<VideoVo> getData() {
		return data;
	}

	public void setData(List<VideoVo> data) {
		this.data = data;
	}
	
	
}
