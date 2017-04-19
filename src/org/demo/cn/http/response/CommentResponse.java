package org.demo.cn.http.response;

import java.util.ArrayList;
import java.util.List;

import org.demo.cn.vo.CommentVo;

public class CommentResponse extends CommonResponse {
	
	private String totals;
	private List<CommentVo> data = new ArrayList<CommentVo>();
	
	public CommentResponse(){}

	public String getTotals() {
		return totals;
	}

	public void setTotals(String totals) {
		this.totals = totals;
	}

	public List<CommentVo> getData() {
		return data;
	}

	public void setData(List<CommentVo> data) {
		this.data = data;
	}
	
	
	
	

}
