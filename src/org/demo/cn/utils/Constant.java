package org.demo.cn.utils;

import android.os.Environment;

public class Constant {
	//Titles的标识
	public static  String[] TITLES = new String[]{"全部", "新闻", "知识", "动态", "公告","博文"};
	
	public static  String[] TITLESID = new String[]{"0", "2", "17", "18", "75","80"};
	
	public static  String[] MTITLES  = new String[]{"热门", "推荐"};
	
    public static  String  BASEURL = "http://www.capitalfamily.cn";
    
    public static  String  GET_ARTICLELIST = "/articlelist.php";
    
    public static  String  GET_VIDEOLIST = "/videolistforjson.php";
    
    public static  String  GET_VIDEO = "/videoforjson.php";
    
    public static  String  GET_USER = "/loginforjson.php";
    
    public static  String  GET_COMMENTS = "/commentforjson.php";
    
    public static  String  PRE_CSDN_APP = "newsapp";
    
    public static final int LOAD_MORE = 0x110;
    public static final int LOAD_REFREASH = 0x111;

    public static final int TIP_ERROR_NO_NETWORK = 0X112;
    public static final int TIP_ERROR_SERVER = 0X113;
	
    public static final int PAGESIZE = 15;
    
    public static final  int NET_SUCCESS = 10001;
    
    public static final int NET_FAIL = 10002;
    
    public static final int NET_UNKNOW = 10003;
    
    /** sd卡根目录 */
	public static String SD = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
    
    public static final String PIC_PATH = SD + "/.newsdemo";
	public static final String PIC_CACHE_NAME = "image";
	
	public static final String REC_PIC_CACHE_NAME = "rec_img";
	
	public static final int MEDIA_HEIGHT = 286;
    
    
	
}
