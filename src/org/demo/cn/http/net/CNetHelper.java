package org.demo.cn.http.net;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.demo.cn.R;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;



/**
 * class name：CNetHelper class description：对http(s)网络通信进行接口封装,参照 PS：参考资料 1.
 * http://hc.apache.org/httpcomponents-client-ga/examples.html 2.
 * http://android.tgbus.com/Android/tutorial/201108/364645_2.shtml
 * 
 * @version 1.0.0 2013/06/26
 * @author dzyssssss
 * 
 * @version 1.1.0 2013/08/13
 * @author dzyssssss 修改：1.增加SSLSocketFactoryEx类，用于不需要验证服务器端证书
 *         2.修改SSL操作时不对服务器端证书进行验证，因为在手机上联网失败，东信互联互通测试时 对与建立双向SSL通道未作测试
 *         3.SSL设置只需要在初始化时进行设置即可，不需要每次调用都进行设置，去除该部分 4.去除没有使用到的一些局部变量的定义
 * 
 * @since
 * 
 *        开发过程中遇到问题： 1. 没有加入访问权限，需要在AndroidMainfest.xml中加入INTERNET访问权限
 * 
 *        信息： 100-199 用于指定客户端应相应的某些动作。 200-299 用于表示请求成功。 300-399
 *        用于已经移动的文件并且常被包含在定位头信息中指定新的地址信息。 400-499 用于指出客户端的错误。 500-599 用于支持服务器错误。
 */
public class CNetHelper {

	private static final String			TAG								= "CNetHelper";

	/*
	 * 访问的网络端口号
	 */
	private static final int			HTTPS_PORT						= 443;

	/*
	 * 网络字符编码格式
	 */
	private static final String			CHARSET							= HTTP.UTF_8;

	/*
	 * 使用apach的httpclient进行http通信
	 */
	private static DefaultHttpClient	mHttpClient						= null;

	/*
	 * 网络是否需要使用代理
	 */
	private static boolean				mNeedSetProxy					= false;

	private static final int			DEFAULT_MAX_CONNECTIONS			= 30;

	/**
	 * keystore密钥
	 */
	public static final String			SERVER_KEYSTORE_PWD				= "111111";

	/*
	 * 网络连接、获取返回超时设置
	 */
	private static final int			DEFAULT_SOCKET_TIMEOUT			= 30 * 1000;

	private static final int			DEFAULT_SOCKET_BUFFER_SIZE		= 8192;

	/*
	 * 连接池获取超时时间
	 */
	private static final int			DEFAULT_MANGER_CONNECT_TIMEOUT	= 30 * 1000;

	/*
	 * 网络为WiFi
	 */
	public static final int				NET_WIFI						= 1;

	/*
	 * 网络为3G
	 */
	public static final int				NET_3G							= 2;

	/*
	 * 没有网络
	 */
	public static final int				NET_NOT_CONNECTED				= 3;

	/*
	 * 发送数据
	 */
	char[]								mBufPost						= null;

	public static synchronized HttpClient getHttpClient() {

		try {
			if (mHttpClient == null) {

				HttpParams params = new BasicHttpParams();

				// 设置一些基本参数
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, CHARSET);
				HttpProtocolParams.setUseExpectContinue(params, true);

				/*
				 * 根据需要，可以伪装为浏览器发送的数据 IE7:Mozilla/4.0 (compatible; MSIE 7.0b;
				 * Windows NT 6.0) Firefox3.03:Mozilla/5.0 (Windows; U; Windows
				 * NT 5.2; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3
				 * 但是对客户端来说没有必要,可以随意设置
				 */
				HttpProtocolParams.setUserAgent(params, "Android-TSM-Client");

				/* 从连接池中取连接的超时时间 */
				ConnManagerParams.setTimeout(params, DEFAULT_MANGER_CONNECT_TIMEOUT);

				/* 连接超时 */
				HttpConnectionParams.setConnectionTimeout(params, DEFAULT_SOCKET_TIMEOUT);

				/* 请求超时 */
				HttpConnectionParams.setSoTimeout(params, DEFAULT_SOCKET_TIMEOUT);

				// *************** 注意
				// ***********************************************
				// ************** 设置不验证服务器端证书，使用SSLSocketFactoryEx ******
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				// 设置我们的HttpClient支持HTTP和HTTPS两种模式
				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));

				// 使用线程安全的连接管理来创建HttpClient
				ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

				// 支持Android 1.5及以上
				mHttpClient = new DefaultHttpClient(ccm, params);
			}
		} catch (Exception e) {


			if (mHttpClient == null) {
				mHttpClient = new DefaultHttpClient();
			}
		}

		return mHttpClient;
	}

	/*
	 * 支持多线程操作
	 */
	public static synchronized HttpClient getHttpClientOld() {

		if (mHttpClient == null) {
			HttpParams params = new BasicHttpParams();

			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);

			/*
			 * 根据需要，可以伪装为浏览器发送的数据 IE7:Mozilla/4.0 (compatible; MSIE 7.0b;
			 * Windows NT 6.0) Firefox3.03:Mozilla/5.0 (Windows; U; Windows NT
			 * 5.2; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3
			 * 但是对客户端来说没有必要,可以随意设置
			 */
			HttpProtocolParams.setUserAgent(params, "Android-TSM-Client");

			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(params, DEFAULT_MANGER_CONNECT_TIMEOUT);

			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params, DEFAULT_SOCKET_TIMEOUT);

			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, DEFAULT_SOCKET_TIMEOUT);

			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

			// 支持Android 1.5及以上
			mHttpClient = new DefaultHttpClient(conMgr, params);
		}

		return mHttpClient;
	}

	/**
	 * 初始化SSL握手密钥
	 * 
	 * 对服务器端证书不做认证，增加该接口主要针对某些手机无法联网 即使设置了服务器端证书但是还是返回No Perr cert..
	 * 
	 * @param paramActivity
	 */
	private void initSSLEx(Context paramContext, String paramClientBKSFileName, String paramClientBKSPwd)
			throws CNetHelperException {

		FileInputStream fileIputStream = null;

		try {
			String StrKeyStoreType = "BKS"; // KeyStore.getDefaultType()
			KeyStore trustStore = KeyStore.getInstance(StrKeyStoreType);

			/*
			 * 加载客户端证书
			 */
			if (paramClientBKSFileName != null && paramClientBKSPwd != null) {
				fileIputStream = new FileInputStream(new File(paramClientBKSFileName));
				trustStore.load(fileIputStream, paramClientBKSPwd.toCharArray());
			}

			/*
			 * 加载服务器端证书,固定位置与raw文件夹
			 */
			InputStream instream = paramContext.getResources().openRawResource(R.raw.server_trust);

			trustStore.load(instream, SERVER_KEYSTORE_PWD.toCharArray());

			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			Scheme sch = new Scheme("https", socketFactory, HTTPS_PORT);

			/*
			 * 设置ssl信息
			 */
			mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);

		} catch (KeyManagementException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (UnrecoverableKeyException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (KeyStoreException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (CertificateException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (FileNotFoundException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (IOException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} finally {
			if (fileIputStream != null) {
				try {
					fileIputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 初始化SSL握手密钥
	 * 
	 * @param paramActivity
	 */
	private void initSSL(Context paramContext, String paramClientBKSFileName, String paramClientBKSPwd)
			throws CNetHelperException {

		FileInputStream fileIputStream = null;

		try {
			String StrKeyStoreType = "BKS"; // KeyStore.getDefaultType()
			KeyStore trustStore = KeyStore.getInstance(StrKeyStoreType);

			/*
			 * 加载客户端证书
			 */
			if (paramClientBKSFileName != null && paramClientBKSPwd != null) {
				fileIputStream = new FileInputStream(new File(paramClientBKSFileName));
				trustStore.load(fileIputStream, paramClientBKSPwd.toCharArray());
			}

			/*
			 * 加载服务器端证书,固定位置与raw文件夹
			 */
			InputStream instream = paramContext.getResources().openRawResource(R.raw.server_trust);

			trustStore.load(instream, SERVER_KEYSTORE_PWD.toCharArray());

			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			Scheme sch = new Scheme("https", socketFactory, HTTPS_PORT);

			/*
			 * 设置ssl信息
			 */
			mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);

		} catch (KeyManagementException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (UnrecoverableKeyException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (KeyStoreException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (CertificateException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (FileNotFoundException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} catch (IOException e) {
			throw new CNetHelperException(CNetHelperException.ERR_INIT_SSL, e.getMessage());
		} finally {
			if (fileIputStream != null) {
				try {
					fileIputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 使用Post方法获取HTTP(S)返回值
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String doPostData(Context paramContext, String url, Map<String, String> params,
			Map<String, String> paramHeader) throws CNetHelperException {

		InputStreamReader isReader = null;
		InputStream is = null;
		try {
			int iStatusCode = 0;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			/*
			 * 获取通信client
			 */
			if (mHttpClient == null) {
				getHttpClient();
			}

			Log.d(TAG, "Post 联网请求地址：" + url);
			HttpPost httpPost = new HttpPost(url);

			// 组织请求参数
			if (params != null && params.size() > 0) {
				StringBuffer param = new StringBuffer();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().trim()));
						// URLEncoder.encode(entry.getValue().trim(),
						// CHARSET)));
						param.append(entry.getKey()).append("=")
								.append(URLEncoder.encode(entry.getValue().trim(), CHARSET));
						param.append("&");
						Log.v(TAG, "Post Body:" + entry.getKey() + " = " + entry.getValue().trim());
					} else {
						nvps.add(new BasicNameValuePair(entry.getKey(), ""));
						param.append(entry.getKey().trim()).append("=").append("");
						param.append("&");
					}
				}
				// 设置报文头信息
				url += "?" + param.substring(0, param.length() - 1).toString();
				/*
				 * 设置Post的Data部分
				 */
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, CHARSET));
			}

			// 增加Header信息
			if (paramHeader != null && paramHeader.size() > 0) {

				for (Map.Entry<String, String> entry : paramHeader.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());

					Log.v(TAG, "Post Header:" + entry.getKey() + " = " + entry.getValue().trim());
				}
			}

			/*
			 * 发送Post请求，并且判断网络返回状态码是否ok，若是ok进行数据解析
			 */
			HttpResponse response = mHttpClient.execute(httpPost);

			iStatusCode = response.getStatusLine().getStatusCode();
			Log.i(TAG, "Post response code:" + String.valueOf(iStatusCode));

			if (iStatusCode == 200) {
				HttpEntity he = response.getEntity();
				is = he.getContent();

				// 解析返回值
				// StringBuffer sb = new StringBuffer();
				// byte[] bytes = new byte[4096];
				// for (int len = 0; (len = is.read(bytes)) != -1;) {
				// sb.append(new String(bytes, 0, len, CHARSET));
				// }

				isReader = new InputStreamReader(is, CHARSET);
				StringBuffer sb = new StringBuffer();

				int length = 0;
				char[] c = new char[1024];
				while ((length = isReader.read(c)) != -1) {
					sb.append(c, 0, length);
				}

				// byte[] base64 = Base64.decode(sb.toString(), Base64.DEFAULT);
				// String strRet = new String(base64);

				return sb.toString();
			} else {
				throw new CNetHelperException(CNetHelperException.ERR_NET_STATUES_CODE,
						paramContext.getString(R.string.net_server_exception) + String.valueOf(iStatusCode));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE, e.getMessage());
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_NET_TIMEOUT, e.getMessage());
		} catch (ConnectTimeoutException e) {
			// onResponseError("time out");
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_NET_TIMEOUT, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_UNKNOWN, e.getMessage());
		} finally {
			if (isReader != null) {
				try {
					isReader.close();
				} catch (Throwable t) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Throwable t) {
				}
			}
		}
	}

	/**
	 * 使用Post方法获取HTTP(S)返回值
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String doPostData(Context paramContext, String url, String paramJson) throws CNetHelperException {

		try {
			int iStatusCode = 0;

			/*
			 * 获取通信client
			 */
			if (mHttpClient == null) {
				getHttpClient();
			}

			Log.d(TAG, "Post 联网请求地址：" + url);
			HttpPost httpPost = new HttpPost(url);

			// 绑定到请求 Entry，对于数据部分需要专门设置下格式，否则按照iso-188xxx编码
			StringEntity se = new StringEntity(paramJson, CHARSET);
			/*
			 * 设置Post的Data部分
			 */
			httpPost.setEntity(se);

			/*
			 * 发送Post请求，并且判断网络返回状态码是否ok，若是ok进行数据解析
			 */
			HttpResponse response = mHttpClient.execute(httpPost);
			httpPost.addHeader("charset", CHARSET);

			iStatusCode = response.getStatusLine().getStatusCode();
			Log.i(TAG, "Post response code:" + String.valueOf(iStatusCode));

			if (iStatusCode == 200) {
				HttpEntity he = response.getEntity();
				InputStream is = he.getContent();

				// 解析返回值
				StringBuffer sb = new StringBuffer();
				byte[] bytes = new byte[1024];
				for (int len = 0; (len = is.read(bytes)) != -1;) {
					sb.append(new String(bytes, 0, len, CHARSET));
				}
				is.close();


				return sb.toString();
			} else {
				throw new CNetHelperException(CNetHelperException.ERR_NET_STATUES_CODE, "net err, StatusCode is"
						+ String.valueOf(iStatusCode));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE, e.getMessage());
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_NET_TIMEOUT, e.getMessage());
		} catch (ConnectTimeoutException e) {
			// onResponseError("time out");
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_NET_TIMEOUT, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_UNKNOWN, e.getMessage());
		}
	}

	/**
	 * 使用Post方法获取HTTP(S)返回值
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String doPostData(Activity paramActivity, String url, char[] paramPostBuf) throws CNetHelperException {

		try {
			int iStatusCode = 0;

			/*
			 * 获取通信client
			 */
			if (mHttpClient == null) {
				getHttpClient();
			}

			Log.d(TAG, "Post 联网请求地址：" + url);
			HttpPost httpPost = new HttpPost(url);

			/**
			 * 通常的HTTP实体需要在执行上下文的时候动态生成的。
			 * HttpClient的提供使用EntityTemplate实体类和ContentProducer接口支持动态实体。
			 * 内容制作是通过写需求的内容到一个输出流，每次请求的时候都会产生。
			 * 因此，通过EntityTemplate创建实体通常是独立的，重复性好。
			 */
			mBufPost = paramPostBuf;

			ContentProducer cp = new ContentProducer() {
				public void writeTo(OutputStream outstream) throws IOException {
					Writer writer = new OutputStreamWriter(outstream, "UTF-8");
					writer.write(mBufPost);
					writer.flush();
					writer.close();
				}
			};
			HttpEntity entity = new EntityTemplate(cp);

			/*
			 * 设置Post的Data部分
			 */
			httpPost.setEntity(entity);

			byte[] pbData = new byte[100];

			new ByteArrayInputStream(pbData);

			/*
			 * 发送Post请求，并且判断网络返回状态码是否ok，若是ok进行数据解析
			 */
			HttpResponse response = mHttpClient.execute(httpPost);

			iStatusCode = response.getStatusLine().getStatusCode();
			Log.i(TAG, "Post response code:" + String.valueOf(iStatusCode));

			if (iStatusCode == 200) {
				HttpEntity he = response.getEntity();
				InputStream is = he.getContent();

				// 解析返回值
				StringBuffer sb = new StringBuffer();
				byte[] bytes = new byte[1024];
				for (int len = 0; (len = is.read(bytes)) != -1;) {
					sb.append(new String(bytes, 0, len, CHARSET));
				}
				is.close();

				byte[] base64 = Base64.decode(sb.toString(), Base64.DEFAULT);
				String strRet = new String(base64);
				Log.d(TAG, "Post请求结果：" + strRet);

				return strRet;
			} else {
				throw new CNetHelperException(CNetHelperException.ERR_NET_STATUES_CODE, "net err, StatusCode is"
						+ String.valueOf(iStatusCode));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE, e.getMessage());
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_NET_TIMEOUT, e.getMessage());
		} catch (ConnectTimeoutException e) {
			// onResponseError("time out");
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_NET_TIMEOUT, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CNetHelperException(CNetHelperException.ERR_UNKNOWN, e.getMessage());
		}
	}

	/**
	 * 使用get方法获取HTTP(S)返回值
	 * 
	 * @param paramActivity
	 * @param url
	 * @param params
	 * @param paramHeader
	 * @return
	 * @throws CNetHelperException
	 */
	public String doGetData(Context paramActivity, String url, Map<String, String> params,
			Map<String, String> paramHeader) throws CNetHelperException {

		try {
			
			Log.v(TAG, "Get联网请求参数：" + url);
			int iStatusCode = 0;

			/*
			 * 获取通信client
			 */
			if (mHttpClient == null) {
				getHttpClient();
			}

			/*
			 * 若是https，设置SSL
			 */
			if (url.substring(0, 5).compareToIgnoreCase("https") == 0) {
				initSSL(paramActivity, null, null);
			}

			// 组织请求参数
			if (params != null) {
				StringBuffer param = new StringBuffer();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						param.append(entry.getKey()).append("=")
								.append(URLEncoder.encode(entry.getValue().trim(), CHARSET));
						param.append("&");
					} else {
						param.append(entry.getKey().trim()).append("=").append("");
						param.append("&");
					}
				}

				// 设置报文头信息
				url += "?" + param.substring(0, param.length() - 1).toString();
			}

			Log.v(TAG, "Get联网请求参数：" + url);
			HttpGet httpGet = new HttpGet(url);

			// 增加Header信息
			if (paramHeader != null && paramHeader.size() > 0) {
				StringBuffer param = new StringBuffer();
				for (Map.Entry<String, String> entry : paramHeader.entrySet()) {
					httpGet.addHeader(entry.getKey(), entry.getValue());
					param.append(entry.getKey()).append("=")
							.append(URLEncoder.encode(entry.getValue().trim(), "gbk"));
					param.append("&");
					Log.v(TAG, "Get Header:" + entry.getKey() + " = " + entry.getValue().trim());
				}
				url += param.substring(0, param.length() - 1).toString();
			}
			/*
			 * 发送get请求，并且判断网络返回状态码是否ok，若是ok进行数据解析
			 */
			HttpResponse response = mHttpClient.execute(httpGet);

			iStatusCode = response.getStatusLine().getStatusCode();
			Log.i(TAG, "Get response code:" + String.valueOf(iStatusCode));

			if (iStatusCode == 200) {
				HttpEntity he = response.getEntity();
				InputStream is = he.getContent();

				// 解析返回值
				StringBuffer sb = new StringBuffer();
				byte[] bytes = new byte[1024];
				for (int len = 0; (len = is.read(bytes)) != -1;) {
					sb.append(new String(bytes, 0, len, CHARSET));
				}
				is.close();

				// byte[] base64 = Base64.decode(sb.toString(), Base64.DEFAULT);
				// String strRet = new String(base64);
				Log.d(TAG, "Get请求结果：" + sb.toString());

				return sb.toString();
			} else {
				throw new CNetHelperException(CNetHelperException.ERR_NET_STATUES_CODE, "net err, StatusCode is"
						+ String.valueOf(iStatusCode));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.v(TAG,   e.getMessage());
			throw new CNetHelperException(CNetHelperException.ERR_ENCODE_PARSE, e.getMessage());
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			Log.v(TAG,   e.getMessage());
			throw new CNetHelperException(CNetHelperException.ERR_NET_TIMEOUT, e.getMessage());
		} catch (ConnectTimeoutException e) {
			// onResponseError("time out");
			e.printStackTrace();
			Log.v(TAG,   e.getMessage());
			throw new CNetHelperException(CNetHelperException.ERR_NET_TIMEOUT, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.v(TAG,   e.getMessage());
			throw new CNetHelperException(CNetHelperException.ERR_UNKNOWN, e.getMessage());
		}
	}

	/*
	 * 设置代理，获取到mHttpClient之后使用
	 */
	private void setWapProxy(String paramHostUrl, int paramHostPort) {

		if (paramHostUrl != null) {
			HttpHost proxy = new HttpHost(paramHostUrl, paramHostPort);

			mHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
	}

	/*
	 * 设置认证信息
	 */
	private void setAuth(String paramUserName, String strPwd) {

		if (paramUserName != null && strPwd != null) {
			mHttpClient.getCredentialsProvider().setCredentials(new AuthScope("localhost", 8080),
					new UsernamePasswordCredentials(paramUserName, strPwd));

		}
	}

	/**
	 * 获取网络联网状态 1:wifi网络 2:3G网络 3:无网络
	 * 
	 * @return
	 */
	public static int getNetState(Context paramContext) {

		
		ConnectivityManager NewworkInfo = (ConnectivityManager) paramContext
				.getSystemService(paramContext.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = NewworkInfo.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isAvailable()) {
			NetworkInfo WiFi_net = NewworkInfo.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo MOBILE_net = NewworkInfo.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (WiFi_net.isConnected()) {
				Log.v(TAG, "WiFi_net 联网333");
				return NET_WIFI;
			}

			if (MOBILE_net.isConnected()) {
				Log.v(TAG, "MOBILE_net 联网");

				// 电信的CTWAP连接问题
				if (MOBILE_net.getExtraInfo().equalsIgnoreCase("ctwap")) {
					mNeedSetProxy = true;
					Log.v(TAG, "MOBILE_net wap 联网");
				} else {
					mNeedSetProxy = false;
				}

				return NET_3G;
			}
		}

		return NET_NOT_CONNECTED;
	}

}
