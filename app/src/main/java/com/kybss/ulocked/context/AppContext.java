package com.kybss.ulocked.context;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.kybss.ulocked.controller.MainController;
import com.kybss.ulocked.cublicble.BLEHelper;
import com.kybss.ulocked.module.component.ContextComponent;
import com.kybss.ulocked.module.component.DaggerContextComponent;
import com.kybss.ulocked.module.component.DaggerNetworkComponent;
import com.kybss.ulocked.module.provider.ContextProvider;
import com.kybss.ulocked.module.provider.NetworkProvider;
import com.kybss.ulocked.module.qualifiers.ShareDirectory;
import com.kybss.ulocked.network.GsonHelper;
import com.kybss.ulocked.util.PreferenceUtil;
import com.kybss.ulocked.util.ToastUtil;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.File;

import javax.inject.Inject;

public class AppContext extends Application {

    private static AppContext mInstance;

    @Inject
    MainController mMainController;

    @Inject
    @ShareDirectory
    File mShareLocation;

    public static AppContext getContext() {
        return mInstance;
    }


    public MainController getMainController() {
        return mMainController;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //设置APP等级,0:管理员；1:维修员；2:用户
        AppConfig.setAppRank(2);

        mInstance = this;

        ZXingLibrary.initDisplayOpinion(this);

        // 吐司初始化
        ToastUtil.init(this);

        // 本地存储工具类初始化
        PreferenceUtil.init(this, GsonHelper.builderGson());

        // 日志打印器初始化
        Logger.init(getPackageName()).setLogLevel(AppConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);

        //蓝牙初始化
        BLEHelper.init();


        // 依赖注解初始化
        ContextComponent contextComponent = DaggerContextComponent
                .builder()
                .contextProvider(new ContextProvider(this))
                .build();
        DaggerNetworkComponent.builder()
                .contextComponent(contextComponent)
                .networkProvider(new NetworkProvider())
                .build()
                .inject(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}