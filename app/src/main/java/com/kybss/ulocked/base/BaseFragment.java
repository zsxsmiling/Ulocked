package com.kybss.ulocked.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kybss.ulocked.R;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.widget.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment<UC> extends CoreFragment<UC> {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private LoadingDialog mLoading;
    private Unbinder mUnBinder;
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mUnBinder= ButterKnife.bind(this, view);
        initialToolbar();
        handleArguments(getArguments());
        initializeViews(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    protected int getLayoutResId() {
        for (Class c = getClass(); c != Fragment.class; c = c.getSuperclass()) {
            ContentView annotation = (ContentView) c.getAnnotation(ContentView.class);
            if (annotation != null) {
                return annotation.value();
            }
        }
        return 0;
    }

    protected void initialToolbar() {
        if (mToolbar != null) {
            if (isShowToolBar()) {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setTitle(getTitle());
                if (isShowBack()) {
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }else {
                mToolbar.setVisibility(View.GONE);
            }
        }

    }

    protected void handleArguments(Bundle arguments) {}

    protected void initializeViews(Bundle savedInstanceState) {}

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected String getTitle() {
        return "";
    }

    protected boolean isShowBack() {
        return true;
    }

    protected void setTitle(CharSequence title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    protected void setTitle(@StringRes int titleRes) {
        if (mToolbar != null) {
            mToolbar.setTitle(titleRes);
        }
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(getMenuResourceId());
    }

    protected ActionBar getSupportActionBar() {
        return ((BaseActivity) getActivity()).getSupportActionBar();
    }

    protected final void showLoading(@StringRes int textResId) {
        showLoading(getString(textResId));
    }

    protected final void showLoading(String text) {
        cancelLoading();
        if (mLoading == null) {
            mLoading = new LoadingDialog(getContext());
            mLoading.setCancelable(false);
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
