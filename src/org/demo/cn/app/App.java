package org.demo.cn.app;

import org.demo.cn.utils.UIUtils;
import org.demo.cn.vo.UserVo;

import android.app.Application;

public class App extends Application {
    private static final int CONNECT_TIMEOUT_MILLIS = 10 * 1000; // 15s
    private static final int READ_TIMEOUT_MILLIS = 15 * 1000; // 20s
    private static App instance;
    
    public UserVo userVo;
    
   

	@Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        UIUtils.initUIUtil(this);
    }

    public static App getInstance() {
        return instance;
    }
}