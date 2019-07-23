package com.kybss.ulocked.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.kybss.ulocked.BuildConfig;
import com.kybss.ulocked.R;
import com.kybss.ulocked.context.AppConfig;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.model.bean.Token;
import com.kybss.ulocked.util.StringFetcher;
import com.orhanobut.logger.Logger;


import org.apache.http.conn.ConnectTimeoutException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;


import static com.kybss.ulocked.util.Constants.HttpCode.HTTP_NETWORK_ERROR;
import static com.kybss.ulocked.util.Constants.HttpCode.HTTP_SERVER_ERROR;
import static com.kybss.ulocked.util.Constants.HttpCode.HTTP_UNAUTHORIZED;
import static com.kybss.ulocked.util.Constants.HttpCode.HTTP_UNKNOWN_ERROR;
import static com.kybss.ulocked.util.Constants.Key.PARAM_CLIENT_ID;
import static com.kybss.ulocked.util.Constants.Key.PARAM_CLIENT_SECRET;
import static com.kybss.ulocked.util.Constants.Key.PARAM_GRANT_TYPE;
import static com.kybss.ulocked.util.Constants.Key.PARAM_REFRESH_TOKEN;

public class ResponseErrorProxy implements InvocationHandler {

    public static final String TAG = ResponseErrorProxy.class.getSimpleName();

    private Object mProxyObject;
    private RestApiClient mRestApiClient;

    public ResponseErrorProxy(Object proxyObject, RestApiClient restApiClient) {
        mProxyObject = proxyObject;
        mRestApiClient = restApiClient;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) {
        Log.e("ss","ss");
        return Observable.just("")
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Object o) throws Exception {
                       try{
                           return (ObservableSource<?>)method.invoke(mProxyObject, args);
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                        return  null;
                    }
                })
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Observable<? extends Throwable> throwableObservable)  {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception
                         {
                                ResponseError error = null;
                                if (throwable instanceof ConnectTimeoutException
                                        || throwable instanceof SocketTimeoutException
                                        || throwable instanceof UnknownHostException
                                        || throwable instanceof ConnectException) {
                                    error = new ResponseError(HTTP_NETWORK_ERROR,
                                            StringFetcher.getString(R.string.toast_error_network));
                                } else if (throwable instanceof HttpException) {
                                    HttpException exception = (HttpException) throwable;
                                    try {
                                        error = GsonHelper.builderGson().fromJson(
                                                exception.response().errorBody().string(), ResponseError.class);
                                    } catch (Exception e) {
                                        if (e instanceof JsonParseException) {
                                            error = new ResponseError(HTTP_SERVER_ERROR,
                                                    StringFetcher.getString(R.string.toast_error_server));
                                        } else {
                                            error = new ResponseError(HTTP_UNKNOWN_ERROR,
                                                    StringFetcher.getString(R.string.toast_error_unknown));
                                        }
                                    }
                                } else if (throwable instanceof JsonParseException) {
                                    error = new ResponseError(HTTP_SERVER_ERROR,
                                            StringFetcher.getString(R.string.toast_error_server));
                                } else {
                                    error = new ResponseError(HTTP_UNKNOWN_ERROR,
                                            StringFetcher.getString(R.string.toast_error_unknown));
                                }

                                if (BuildConfig.DEBUG) {
                                    Logger.e("网络请求出现错误: " + error.toString());
                                }

                                if (error.getStatus() == HTTP_UNAUTHORIZED) {
                                    return Observable.error(error);
                                } else {
                                    return Observable.error(error);
                                }
                            }
                        });
                    }
                });
    }


}
