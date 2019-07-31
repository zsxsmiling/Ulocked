package com.kybss.ulocked.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseFragment;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.User;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ToastUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\4\8 0008.
 */
@ContentView(R.layout.fragment_user_message)
public class UserMessageFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.UserMessageUi{


    private String mMobile;

    @BindView(R.id.txt_user_name)
    TextView nameTxt;

    @BindView(R.id.txt_user_phone)
    TextView mUserPhoneTxt;

    @BindView(R.id.txt_user_house)
    TextView mUserHouseTxt;

    @BindView(R.id.txt_user_more)
    TextView mUserMoreTxt;



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
    protected void handleArguments(Bundle arguments) {
        if (arguments != null) {
            mMobile = arguments.getString(Display.PARAM_OBJ);
        }
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
    }


    @Override
    public void showUserMessage(User user) {



        nameTxt.setText(user.getUsername());
        mUserPhoneTxt.setText(user.getMobile());
        mUserHouseTxt.setText(user.getUserdorm());
        mUserMoreTxt.setText(user.getUsernumber());
    }



    @Override
    public void logoutFinish() {
        ToastUtil.showToast(R.string.toast_success_logout);
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
                        getCallbacks().logout2();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .create()
                .show();
    }
}

