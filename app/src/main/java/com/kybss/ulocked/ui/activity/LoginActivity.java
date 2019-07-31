package com.kybss.ulocked.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.EditTextWithDel;
import com.kybss.ulocked.util.SystemUtil;
import com.kybss.ulocked.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserLoginUi {

    @BindView(R.id.stu_id_edit)
    EditTextWithDel stu_id_edit;

    @BindView(R.id.pass_edit)
    EditTextWithDel pass_edit;


    @BindView(R.id.bt_login)
    Button bt_login;



    @BindView(R.id.gotoreg)
    Button gotoreg;


    @BindView(R.id.forget_code)
    Button forget_code;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        bt_login.setEnabled(true);
        ToastUtil.showToast(error.getMessage());
    }

    @Override
    public void userLoginFinish() {
        cancelLoading();
        Display display = getDisplay();
        if (display != null) {
            display.finishActivity();
        }
    }

    @Override
    public void userLoginError() {
        cancelLoading();
        bt_login.setEnabled(true);
    }



    @OnClick({ R.id.bt_login,  R.id.gotoreg,R.id.forget_code})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_login:
                login();
                break;

            case R.id.gotoreg:
                getCallbacks().showRegister();
                break;

            case R.id.forget_code:
                getCallbacks().showResetCode();
                break;

        }
    }

    /**
     * 执行登录操作
     */
    private void login() {
        // 隐藏软键盘
        //SystemUtil.hideKeyBoard(this);

        // 验证用户名是否为空
        final String account =stu_id_edit.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtil.showToast("学号（工号）不能为空");
            return;
        }
        // 验证密码是否为空
        final String password = pass_edit.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(R.string.toast_error_empty_password);
            return;
        }
        // 禁用登录按钮,避免重复点击
        bt_login.setEnabled(false);
        // 显示提示对话框
        showLoading(R.string.label_being_something);
        // 发起登录的网络请求
        getCallbacks().login(account, password);
    }
}
