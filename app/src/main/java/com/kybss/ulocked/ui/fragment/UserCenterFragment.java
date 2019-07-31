package com.kybss.ulocked.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseFragment;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.User;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ToastUtil;
import com.kybss.ulocked.widget.section.SectionTextItemView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\4\8 0008.
 */
@ContentView(R.layout.fragment_user_center)
public class UserCenterFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.UserCenterUi{
    @BindView(R.id.layout_login_before)
    View mLoginBeforeLayout;

    @BindView(R.id.layout_login_after)
    View mLoginAfterLayout;





    @BindView(R.id.btn_my_code)
    SectionTextItemView mManageCodeBtn;

    @BindView(R.id.btn_my_lock)
    SectionTextItemView mManageLockBtn;

    @BindView(R.id.btn_my_token)
    SectionTextItemView mHelpTokenBtn;



    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_user_center;
    }




    @Override
    protected boolean isShowBack() {
        return false;
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
    }


    @Override
    public void showUserInfo(User user) {
        if (user != null) {
            mLoginBeforeLayout.setVisibility(View.GONE);
            mLoginAfterLayout.setVisibility(View.VISIBLE);

        } else {
            mLoginBeforeLayout.setVisibility(View.VISIBLE);
            mLoginAfterLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void uploadAvatarFinish() {

    }

    @Override
    public void logoutFinish() {
        ToastUtil.showToast(R.string.toast_success_logout);
    }

    @OnClick({
            R.id.layout_login_before,
            R.id.layout_login_after,
            R.id.img_avatar,
            R.id.btn_my_code,
            R.id.btn_my_lock,
            R.id.btn_my_token,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_login_before:
                //ToastUtil.showToast("111");
                getCallbacks().showLogin();
                break;
            case R.id.layout_login_after:
                getCallbacks().showUserProfile();
           //     ToastUtil.showToast("111");
                break;
            case R.id.img_avatar:
                //selectUpdateAvatarMethod();
                getCallbacks().showUserProfile();
                //  ToastUtil.showToast("111");
                break;
            case R.id.btn_my_code:
                getCallbacks().showCode();
                break;
            case R.id.btn_my_lock:
                getCallbacks().showRecord();

                break;
            case R.id.btn_my_token:
                getCallbacks().showToken();
                break;
            case R.id.btn_my_other:
            //    ToastUtil.showToast("111");
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_center, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                if (AppCookie.isLoggin())
                {
                    handleLogoutClick();
                }
                else {
                    ToastUtil.showToast("处于未登录状态！");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 退出提示框
     */
    private void handleLogoutClick() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCallbacks().logout();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .create()
                .show();
    }
}
