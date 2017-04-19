package org.demo.cn.http.net;

/*
 * CNetHelperException
 */

public class CNetHelperException extends Exception {
	private int mErrcode = 0;

	public static final int ERR_ENCODE_PARSE = -1;

	public static final int ERR_NET_TIMEOUT = -2;

	public static final int ERR_INIT_SSL = -3;

	public static final int ERR_NET_STATUES_CODE = -4;

	public static final int ERR_UNKNOWN = -100;

	public CNetHelperException(int paramErrcode, String strExceptionInfo) {
		super(strExceptionInfo);
		mErrcode = paramErrcode;
	}

	public int getErrcode() {
		return mErrcode;
	}

	public String getErrDesc() {
		return this.getMessage();
	}
}