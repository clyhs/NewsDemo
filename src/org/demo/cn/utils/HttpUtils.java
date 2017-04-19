package org.demo.cn.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;



public class HttpUtils {

    
	public static String httpGetTool(List<NameValuePair> nvps, String urlPath,
			String methodName, String reusltType) {
		HttpClient http = new DefaultHttpClient();
		StringBuilder url = new StringBuilder();
		
		if (null != nvps && nvps.size() > 0) {
			url.append("?");
			for (int i = 0; i < nvps.size(); i++) {
				try {
					url.append(nvps.get(i).getName()).append("=").append(
							URLEncoder.encode(nvps.get(i).getValue(),"utf-8")
							).append("&");
				} catch (UnsupportedEncodingException e) {
				
					e.printStackTrace();
				}
			}
			
			if(url.toString().endsWith("&")){
				url.deleteCharAt(url.lastIndexOf("&"));
			}
		}
		HttpGet httpget = new HttpGet(url.toString());
		StringBuffer sb = new StringBuffer();
		try {
//			setHeader(httpget, nvps);
			HttpResponse response = http.execute(httpget);
			HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			if (reader != null) {
				reader.close();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String httpPostTool(List<NameValuePair> nvps, String urlPath) {
		HttpClient http = new DefaultHttpClient();
		StringBuilder url = new StringBuilder();
		url.append(urlPath);
		HttpPost httpPost = new HttpPost(url.toString());
		
		
		UrlEncodedFormEntity entityform;
		StringBuffer sb = new StringBuffer();
		try {
			entityform = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);

//			setHeader(httpPost, nvps);
			httpPost.setEntity(entityform);
			HttpResponse response = http.execute(httpPost);
			HttpEntity entity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			if (reader != null) {
				reader.close();
			}
			System.out.println("***********************");
			System.out.println("***********************");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

//	public static void setHeader(HttpRequestBase http, List<NameValuePair> nvps) {
//		http.setHeader("spid", Configuration.SPID);
//		http.setHeader("password", Configuration.PASSWORD);
//		java.text.SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
//		String timestamp = date.format(new Date());
//		http.setHeader("timestamp", timestamp);
//		http.setHeader("sourcetype",
//				Configuration.SOURCETYPE);
//	}

	


}
