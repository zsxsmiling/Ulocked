package com.kybss.ulocked.ui.activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ToastUtil;
import com.kybss.ulocked.widget.CountDownTimerView;

import butterknife.BindView;
import butterknife.OnClick;

@ContentView(R.layout.activity_general_code)
public class GeneralCodeActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserCodeUi {

    @BindView(R.id.bt_gen_qcode)
    CountDownTimerView reGenCodeBtn;

    @BindView(R.id.bt_submit_qr)
    Button qrSubmiitBtn;

    @BindView(R.id.img_qcode)
    ImageView qCodeImg;

    @BindView(R.id.bt_close)
    ImageButton mCloseBtn;


    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected void onSupersetContenView() {
        initWindow();
    }


    private void initWindow() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.dimAmount = 0.0f; //设置窗口之外部分透明程度
        attributes.x = 0;
        attributes.y = 0;
        attributes.width = 500;
        attributes.height = 809;
        getWindow().setAttributes(attributes);
    }


    @OnClick({
            R.id.bt_submit_qr,
            R.id.bt_gen_qcode,
            R.id.bt_close
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_gen_qcode:
                showLoading(R.string.loading_refresh_code);
                getCallbacks().refreshCode();
                break;
            case R.id.bt_submit_qr:
                showLoading(R.string.loading_submit_code);
                getCallbacks().submitCode();
                break;
            case R.id.bt_close:
                getCallbacks().close();
                break;
        }
    }



    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        ToastUtil.showToast(error);
    }

    @Override
    public void refreshCodeFinish(Bitmap bitmap) {
        cancelLoading();
        qCodeImg.setImageBitmap(bitmap);
        reGenCodeBtn.startCountDown(5000);
        ToastUtil.showToast(R.string.toast_success_refresh_code);
    }

    @Override
    public void submitCodeFinish() {
        reGenCodeBtn.cancelCountDown();
        cancelLoading();
        reGenCodeBtn.setEnabled(false);
        qrSubmiitBtn.setEnabled(false);
        ToastUtil.showToast(R.string.toast_success_submit_code);
    }


}
