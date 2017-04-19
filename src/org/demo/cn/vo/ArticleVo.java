package org.demo.cn.vo;

import java.io.Serializable;

public class ArticleVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArticleVo() {
	}

	private String id;

	private String title;

	private String content;

	private String images;

	private String hit;

	private String shortContent;

	private String createTime;
	
	private int type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.type = 1;
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.type = 2;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
		this.type = 3;
	}

	public String getHit() {
		return hit;
	}

	public void setHit(String hit) {
		this.hit = hit;
	}

	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static abstract interface ArticleType {
		public static final int TITLE = 1;
		public static final int CONTENT = 2;
		public static final int IMG = 3;
	}

}
