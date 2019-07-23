package com.kybss.ulocked.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.MainController;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ActivityStack;


public abstract class CoreActivity<UC> extends AppCompatActivity
        implements BaseController.Ui<UC> {

    private MainController mMainController;
    private Display mDisplay;
    private UC mCallbacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSupersetContenView();
        setContentView(getLayoutId());

        mDisplay = new Display(this);
        mMainController = AppContext.getContext().getMainController();

        getController().attachUi(this);
        ActivityStack.create().add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainController.init();
        mMainController.attachDisplay(mDisplay);
        getController().startUi(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainController.suspend();
        mMainController.detachDisplay(mDisplay);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getController().detachUi(this);
        ActivityStack.create().remove(this);
        //AppContext.getContext().getRefWatcher().watch(this);
    }

    protected abstract int getLayoutId();
    protected abstract BaseController getController();
    protected abstract void onSupersetContenView();
    @Override
    public void setCallbacks(UC callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public UC getCallbacks() {
        return mCallbacks;
    }

    @Override
    public void onResponseError(ResponseError error) {}

    public final Display getDisplay() {
        return mDisplay;
    }

    protected final MainController getMainController() {
        return mMainController;
    }

    @Override
    public void onBackPressed() {
        getMainController().onBackButtonPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getMainController().onBackButtonPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
