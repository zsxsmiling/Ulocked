package com.kybss.ulocked.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseFragment;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.EditTextWithDel;
import com.kybss.ulocked.util.ResetCodeStep;
import com.kybss.ulocked.util.StringUtil;
import com.kybss.ulocked.util.ToastUtil;
import com.kybss.ulocked.widget.CountDownTimerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\4\25 0025.
 */
@ContentView(R.layout.fragment_verify_mobile)
public class ResetCodeStepOneFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.UserVerifyMobileUi {

    @BindView(R.id.edit_mobile)
    EditTextWithDel mMobileEdit;

    @BindView(R.id.edit_code)
    EditTextWithDel mCodeEdit;

    @BindView(R.id.btn_send_code)
    CountDownTimerView mSendCodeBtn;

    @BindView(R.id.btn_submit_code)
    Button mSubmitCodeBtn;

    @BindView(R.id.btn_test)
    Button mTestBtn;

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        super.initializeViews(savedInstanceState);
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_null;
    }

    @Override
    protected boolean isShowBack() {
        return false;
    }

    @OnClick({
            R.id.btn_send_code,
            R.id.btn_submit_code,
            R.id.btn_test
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_code:
                submitCode();
              break;
            case R.id.btn_send_code:
                sendSmsCode();
                break;
            case R.id.btn_test:
                goToSubminFragment();
                break;
        }
    }



    /**
     * 执行发送验证码的操作
     */
    private void sendSmsCode() {

        if(!StringUtil.isCellPhone(mMobileEdit.getText().toString().trim())){
            ToastUtil.showToast(R.string.toast_error_not_phone);
            return;
        }
        showLoading(R.string.label_being_something);
        // 发起发送验证码的API请求
        getCallbacks().sendResetCode(mMobileEdit.getText().toString().trim());
    }

    /**
     * 执行发送验证码的操作
     */
    private void submitCode() {
        // 隐藏软键盘
       // SystemUtil.hideKeyBoard(getContext());

        // 验证验证码是否为空
        final String code = mCodeEdit.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast(R.string.toast_error_empty_code);
            return;
        }

        showLoading(R.string.label_being_something);
        // 避免重复点击
        mSubmitCodeBtn.setEnabled(false);
        // 发起发送验证码的API请求
        getCallbacks().resetCheckCode(mMobileEdit.getText().toString().trim(), code);
    }

    @Override
    public void sendCodeFinish() {
        mSendCodeBtn.startCountDown(30000);
        cancelLoading();
        ToastUtil.showToast(R.string.toast_success_send_sms_code);
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        mSendCodeBtn.setEnabled(true);
        mSubmitCodeBtn.setEnabled(true);
        ToastUtil.showToast(error.getMessage());
    }

    @Override
    public void verifyMobileFinish() {
        cancelLoading();
        mSubmitCodeBtn.setEnabled(true);
        final String code = mCodeEdit.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast(R.string.toast_error_empty_code);
            return;
        }
       goToSubminFragment();
    }

    @Override
    public void verifyMobileError() {

        cancelLoading();
        mSubmitCodeBtn.setEnabled(true);
    }

    private void goToSubminFragment() {
        if(!StringUtil.isCellPhone(mMobileEdit.getText().toString().trim())){
            ToastUtil.showToast(R.string.toast_error_not_phone);
            return;
        }
        Display display = getDisplay();
        if (display != null) {
            display.showResetCodeStep(ResetCodeStep.STEP_SECOND,
                    mMobileEdit.getText().toString().trim());
        }
    }
}
