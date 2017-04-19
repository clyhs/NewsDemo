package org.demo.cn.vo;

import java.io.Serializable;

public class UserVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Integer id;
	
	private String username;
	
	private String tel;
	
	private String img;
	
	private String email;
	
	private String loginTime;

	

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
