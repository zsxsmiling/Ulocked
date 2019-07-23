package com.kybss.ulocked.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;

import com.kybss.ulocked.R;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.widget.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

import com.igexin.sdk.PushManager;


public abstract class BaseActivity<UC> extends CoreActivity<UC> {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private LoadingDialog mLoading;
    private Unbinder mUnBinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUnBinder=ButterKnife.bind(this);
        initializeToolbar();
        handleIntent(getIntent(), getDisplay());
        initializeViews(savedInstanceState);

        // com.getui.demo.PushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(AppContext.getContext(), com.kybss.ulocked.push.PushService.class);
// com.getui.demo.PushIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(AppContext.getContext(), com.kybss.ulocked.push.PushIntentService.class);

        //PushManager.getInstance().getClientid(this.getApplicationContext());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, getDisplay());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    protected int getLayoutId() {
        for (Class c = getClass(); c != Context.class; c = c.getSuperclass()) {
            ContentView annotation = (ContentView) c.getAnnotation(ContentView.class);
            if (annotation != null) {
                return annotation.value();
            }
        }
        return 0;
    }

    private void initializeToolbar() {
        if (mToolbar != null) {
            //mToolbar.setTitle(getTitle());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("");
            /*getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        }
    }

    protected void handleIntent(Intent intent, Display display) {}

    protected void initializeViews(Bundle savedInstanceState) {}

    @Override
    public void setTitle(CharSequence title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    @Override
    public void setTitle(@StringRes int titleResId) {
        if (mToolbar != null) {
            mToolbar.setTitle(titleResId);
        }
    }

    @Override
    protected void onSupersetContenView() {

    }

    protected final void showLoading(@StringRes int textResId) {
        showLoading(getString(textResId));
    }

    protected final void showLoading(String text) {
        cancelLoading();
        if (mLoading == null) {
            mLoading = new LoadingDialog(this);
            mLoading.setCancelable(true);
            mLoading.setCanceledOnTouchOutside(false);
        }
        mLoading.setTitle(text);
        mLoading.show();
    }

    protected final void cancelLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }
}
