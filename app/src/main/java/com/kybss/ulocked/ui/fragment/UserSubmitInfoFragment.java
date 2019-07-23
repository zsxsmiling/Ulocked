package com.kybss.ulocked.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseFragment;
import com.kybss.ulocked.context.AppConfig;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.EditTextWithDel;
import com.kybss.ulocked.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;

import static com.kybss.ulocked.util.Constants.Key.PARAM_HELP_TOKEN;
import static com.kybss.ulocked.util.Constants.Key.PARAM_PASSWORD;
import static com.kybss.ulocked.util.Constants.Key.PARAM_RANK;
import static com.kybss.ulocked.util.Constants.Key.PARAM_STUDENT_ID;
import static com.kybss.ulocked.util.Constants.Key.PARAM_USER_CARD_ID;
import static com.kybss.ulocked.util.Constants.Key.PARAM_USER_NAME;
import static com.kybss.ulocked.util.Constants.Key.PARAM_USER_PHONE;

/**
 * Created by Administrator on 2018\4\25 0025.
 */
@ContentView(R.layout.fragment_submit_info)
public class UserSubmitInfoFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.UserSubmitInfoUi {

    @BindView(R.id.edit_user_id)
    EditTextWithDel mUserIdEdit;

    @BindView(R.id.edit_user_name)
    EditTextWithDel mUserNameEdit;

    @BindView(R.id.edit_user_stuid)
    EditTextWithDel mUserStuidEdit;

    @BindView(R.id.edit_user_password)
    EditTextWithDel mUserPasswordEdit;

    @BindView(R.id.edit_user_help_token)
    EditTextWithDel mUserHelpTokenEdit;

    @BindView(R.id.edit_password_confirm)
    EditTextWithDel mPasswordConfirm;

    @BindView(R.id.btn_reg)
    Button mRegBtn;

    private String mMobile;

    public static UserSubmitInfoFragment create(String mobile) {
        UserSubmitInfoFragment fragment = new UserSubmitInfoFragment();
        if (!TextUtils.isEmpty(mobile)) {
            Bundle bundle = new Bundle();
            bundle.putString(Display.PARAM_OBJ, mobile);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected boolean isShowBack() {
        return false;
    }

    @Override
    protected void handleArguments(Bundle arguments) {
        if (arguments != null) {
            mMobile = arguments.getString(Display.PARAM_OBJ);
        }
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_null;
    }




    @OnClick({ R.id.btn_reg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reg:
                register();
                break;
        }
    }
    private void register() {
    Map<String, String> params = new HashMap<>();
    final String stu_id = mUserStuidEdit.getText().toString();
    final String name = mUserNameEdit.getText().toString();
    final String card_id = mUserIdEdit.getText().toString();
    final String pwd = mUserPasswordEdit.getText().toString();
    final String help_token = mUserHelpTokenEdit.getText().toString();
        final String pwd_confirm = mPasswordConfirm.getText().toString();


    if (TextUtils.isEmpty(card_id)) {
        ToastUtil.showToast(R.string.toast_error_empty_card_id);
        return;
    }
    if (TextUtils.isEmpty(name)) {
        ToastUtil.showToast(R.string.toast_error_empty_name);
        return;
    }
    if (TextUtils.isEmpty(stu_id)) {
        ToastUtil.showToast(R.string.toast_error_empty_id);
        return;
    }
        if (TextUtils.isEmpty(help_token) || help_token.length()<8) {
            ToastUtil.showToast("口令至少为8位");
            return;
        }
    if (TextUtils.isEmpty(pwd)) {
        ToastUtil.showToast(R.string.toast_error_empty_password);
        return;
    }
        if(!pwd.equals(pwd_confirm)){
            ToastUtil.showToast("两次密码不一致，请确认");
            return;
        }

        params.put(PARAM_USER_PHONE, mMobile);
        params.put(PARAM_USER_NAME,name);
        params.put(PARAM_STUDENT_ID, stu_id);
        params.put(PARAM_PASSWORD,pwd);
        params.put(PARAM_HELP_TOKEN, help_token);
/*        params.put(PARAM_USER_CARD_ID,card_id);
        params.put(PARAM_RANK, ""+AppConfig.getAppRank());*/

    showLoading(R.string.label_being_something);
    mRegBtn.setEnabled(false);
    getCallbacks().createUser(params);
}

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        mRegBtn.setEnabled(true);
        ToastUtil.showToast(error.getMessage()+"请尝试更换口令");
    }


    @Override
    public void userRegisterFinish() {
        cancelLoading();
        ToastUtil.showToast(R.string.toast_success_create_user);
        Display display = getDisplay();
        if (display != null) {
            display.finishActivity();
        }
    }



}
