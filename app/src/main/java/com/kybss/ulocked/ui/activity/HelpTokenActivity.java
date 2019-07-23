package com.kybss.ulocked.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


@ContentView(R.layout.activity_change_token)
public class HelpTokenActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserHelpTokenUi {


    @BindView(R.id.bt_close)
    ImageButton mCloseImg;

    @BindView(R.id.token_edit)
    EditText mTokenEdit;

    @BindView(R.id.token_confirm_edit)
    EditText mConfirmTokenEdit;

    @BindView(R.id.bt_cancel)
    Button mCancelBtn;

    @BindView(R.id.bt_submit)
    Button mSubmitBtn;

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
        attributes.height = 909;
        getWindow().setAttributes(attributes);
    }

    @OnClick({
            R.id.bt_close,
            R.id.bt_cancel,
            R.id.bt_submit
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_close:
            case R.id.bt_cancel:
                getCallbacks().close();
                break;
            case R.id.bt_submit:
                showLoading("口令更换中...");
                submitNewToken();
                break;
        }
    }

    private void submitNewToken() {
        String mtoken = mTokenEdit.getText().toString().trim();
        String mtoken_confirm = mConfirmTokenEdit.getText().toString().trim();
        if(TextUtils.isEmpty(mtoken) ||
                TextUtils.isEmpty(mtoken_confirm)){
            cancelLoading();
            ToastUtil.showToast("请输入新口令");
            return;
        }else if(mtoken.length()<8){
            cancelLoading();
            ToastUtil.showToast("口令8位以上");
            return;
        }else if(!mtoken.equals(mtoken_confirm)){
            cancelLoading();
            ToastUtil.showToast("口令不一致");
            return;
        }
        getCallbacks().submitNewToken(mTokenEdit.getText().toString());
    }


    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        if(error.getStatus() == 500){
            ToastUtil.showToast("口令与他人相同，请更换");
        }else{
        ToastUtil.showToast(error);
        }
    }


    @Override
    public void submitHelpTokenFinish() {
        cancelLoading();
        ToastUtil.showToast("重置成功");
        getCallbacks().close();
    }

}
