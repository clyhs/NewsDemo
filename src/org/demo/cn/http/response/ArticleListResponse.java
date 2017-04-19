package org.demo.cn.http.response;

import java.util.ArrayList;
import java.util.List;

import org.demo.cn.vo.ArticleVo;

public class ArticleListResponse extends CommonResponse {
	
	private String totals;
	
	private List<ArticleVo> data = new ArrayList<ArticleVo>();
	
	public ArticleListResponse(){
		
	}

	public String getTotals() {
		return totals;
	}

	public void setTotals(String totals) {
		this.totals = totals;
	}

	public List<ArticleVo> getData() {
		return data;
	}

	public void setData(List<ArticleVo> data) {
		this.data = data;
	}
	
	

}
