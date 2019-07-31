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
import com.kybss.ulocked.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kybss.ulocked.util.Constants.Key.PARAM_PASSWORD;

/**
 * Created by Administrator on 2018\4\25 0025.
 */
@ContentView(R.layout.fragment_submit_info_reset)
public class ResetCodeStepTwoFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.UserSubmitInfoUi {




    @BindView(R.id.edit_user_password)
    EditTextWithDel mUserPasswordEdit;



    @BindView(R.id.edit_password_confirm)
    EditTextWithDel mPasswordConfirm;

    @BindView(R.id.btn_reg)
    Button mRegBtn;

    private String mMobile;

    public static ResetCodeStepTwoFragment create(String mobile) {
        ResetCodeStepTwoFragment fragment = new ResetCodeStepTwoFragment();
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

    final String pwd = mUserPasswordEdit.getText().toString();

        final String pwd_confirm = mPasswordConfirm.getText().toString();


    if (TextUtils.isEmpty(pwd)) {
        ToastUtil.showToast(R.string.toast_error_empty_password);
        return;
    }
        if(!pwd.equals(pwd_confirm)){
            ToastUtil.showToast("两次密码不一致，请确认");
            return;
        }

        params.put("mobile", mMobile);


        params.put(PARAM_PASSWORD,pwd);

/*        params.put(PARAM_USER_CARD_ID,card_id);
        params.put(PARAM_RANK, ""+AppConfig.getAppRank());*/

    showLoading(R.string.label_being_something);
    mRegBtn.setEnabled(false);
    getCallbacks().ResetCode(params);
}

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        mRegBtn.setEnabled(true);
        ToastUtil.showToast(error.getMessage());
    }


    @Override
    public void userRegisterFinish() {
        cancelLoading();
        ToastUtil.showToast(R.string.toast_success_reset_code);
        Display display = getDisplay();
        if (display != null) {
            display.finishActivity();
        }
    }



}
