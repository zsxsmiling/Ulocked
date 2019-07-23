package com.kybss.ulocked.network;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.widget.LoadingDialog;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018\4\8 0008.
 */

public abstract class RequestCallback<T> implements Observer<T>{
    private static final String TAG = "RequestCallback";


    @Override
    public final void onNext(T t) {
        onResponse(t);
    }

    @Override
    public final void onError(Throwable throwable) {
        if (throwable instanceof ResponseError) {
            onFailure((ResponseError) throwable);
        } else {
            Log.e(TAG, "throwable isn't instance of ResponseError");
            Log.e(TAG, String.valueOf(throwable));


                ResponseError responseError=new ResponseError(500,"用户名或密码错误");
            onFailure(responseError);

        }
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    public void onResponse(T response) {}

    public void onFailure(ResponseError error) {}


    @Override
    public void onComplete() {

    }
  }
