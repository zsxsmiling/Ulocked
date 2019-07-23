package com.kybss.ulocked.network;

import android.content.Context;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kybss.ulocked.context.AppConfig;
import com.kybss.ulocked.model.bean.Token;
import com.kybss.ulocked.network.service.AccountService;
import com.kybss.ulocked.network.service.HelperService;
import com.kybss.ulocked.network.service.LabService;
import com.kybss.ulocked.network.service.ManagerService;
import com.kybss.ulocked.network.service.MeetRoomService;
import com.kybss.ulocked.network.service.TokenService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.kybss.ulocked.util.Constants.Header;
import com.kybss.ulocked.util.SystemUtil;

public class RestApiClient {

    private static final String DEVICE_TYPE = "android";

    private Context mContext;
    private final File mCacheLocation;
    private Retrofit mRetrofit;
    private String mToken;

    public RestApiClient(Context context, File cacheLocation) {
        mContext = context;
        mCacheLocation = cacheLocation;
    }

    public RestApiClient setToken(String token) {
        mToken = token;
        mRetrofit = null;
        return this;
    }

    private OkHttpClient newRetrofitClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置缓存路径
        if (mCacheLocation != null) {
            File cacheDir = new File(mCacheLocation, UUID.randomUUID().toString());
            Cache cache = new Cache(cacheDir, 1024);
            builder.cache(cache);
        }
        // 设置超时时间
        builder.connectTimeout(AppConfig.CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        builder.readTimeout(AppConfig.READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        // 添加请求签名拦截器
         builder.addInterceptor(new RequestSignInterceptor());
        // 添加请求头
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder newRequest = chain.request().newBuilder();
                newRequest.addHeader(Header.HTTP_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
                newRequest.addHeader(Header.HTTP_APP_VERSION, SystemUtil.getAppVersionName(mContext));
                newRequest.addHeader(Header.HTTP_APP_KEY, AppConfig.APP_KEY);
                newRequest.addHeader(Header.HTTP_DEVICE_ID, SystemUtil.getDeviceId(mContext));
                newRequest.addHeader(Header.HTTP_DEVICE_TYPE, DEVICE_TYPE);
                if (mToken != null) {
                    newRequest.addHeader(Header.AUTHORIZATION, "Bearer " + mToken);
                }

                return chain.proceed(newRequest.build());
            }
        });
        // 添加OkHttp日志打印器
        return builder.build();
    }

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.client(newRetrofitClient());
            builder.baseUrl(AppConfig.SERVER_URL_new);
            builder.addConverterFactory(GsonConverterFactory.create());
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

            mRetrofit = builder.build();
        }

        return mRetrofit;
    }

    private <T> T get(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

    private <T> T getByProxy(Class<T> clazz) {
        T t = get(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz },
                new ResponseErrorProxy(t, this));
    }

    public TokenService tokenService() {
        return getByProxy(TokenService.class);
        //return get(TokenService.class);
    }

    public AccountService accountService() {
        return getByProxy(AccountService.class);
        //return get(AccountService.class);

    }

    public MeetRoomService meetroomService() {
        return getByProxy(MeetRoomService.class);
        //return get(MeetRoomService.class);

    }

    public ManagerService managerService() {
        return getByProxy(ManagerService.class);
        //return get(MeetRoomService.class);
    }

    public HelperService helperService() {
        return getByProxy(HelperService.class);
    }

    public LabService labService() {
        return getByProxy(LabService.class);
    }
}
