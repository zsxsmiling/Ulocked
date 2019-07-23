package com.kybss.ulocked.context;


import com.kybss.ulocked.BuildConfig;
import com.kybss.ulocked.util.SDCardUtil;

import java.io.File;

public class AppConfig {


    public static final String APP_ID = "2017092508920843";
    public static final String SELLER_ID="2088102018931672";
    /**
     * 是否是debug模式
     */
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static boolean LOCAL= false;

    public static int APP_RANK = 2;


    public static boolean ADMIN = false;


    /**
     * 服务器地址
     */
    // 我的域名lazywaimai.com没有备案，最近被封了，只能使用ip地址了，url路径有点长。。。。。
//    public static final String SERVER_URL = "http://api.beta.lazywaimai.com/v1/";
   // public static final String SERVER_URL = "http://115.159.89.14/LazyWaimai-Api/web/v1/";
    public static final String SERVER_URL = LOCAL ? "http://192.168.8.203:81/v1/" : "http://101.132.123.35:84/v1/";
    public static final String SERVER_URL_new = "http://123.207.99.248:8090/";

    /**
     * 连接超时时间
     */
    public static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s

    /**
     * 响应超时时间
     */
    public static final int READ_TIMEOUT_MILLIS = 20 * 1000; // 20s

    /**
     * App ID
     */
    public static final String APP_KEY = "android";

    /**
     * App Secret
     */
    public static final String APP_SECRET = "afegewlnbnl987nfelwn";

    public static final String APP_NAME = "lazy_waimai";

    public static String getAppRootPath() {
        return SDCardUtil.getRootPath() + File.separator + APP_NAME;
    }

    public static String getAppImagePath() {
        return getAppRootPath() + File.separator + "image";
    }

    public static int getAppRank() {
        return APP_RANK;
    }

    public static void setAppRank(int appRank) {
        APP_RANK = appRank;
    }

}
