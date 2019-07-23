package com.kybss.ulocked.ui.fragment;


import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseFragment;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.MeetController;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.Floor;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.User;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ContentView(R.layout.fragment_contents)
public class ContentsFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.UserCenterUi {
    @BindView(R.id.btn_qiangong)
    Button bt_qiangong;
    @BindView(R.id.btn_dongnan)
    Button bt_dongnan;
    @BindView(R.id.btn_zhongda)
    Button bt_zhongda;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_null;
    }


    @SuppressLint("ResourceType")
    @OnClick({
            R.id.btn_qiangong,
            R.id.btn_dongnan,
            R.id.btn_zhongda
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_qiangong:
                getCallbacks().showQGActivity();
                break;
            case R.id.btn_dongnan:
                getCallbacks().showDNActivity();
                break;
            case R.id.btn_zhongda:
                getCallbacks().showZDActivity();
                break;
        }
    }

    @Override
    public void showUserInfo(User user) {

    }

    @Override
    public void uploadAvatarFinish() {

    }

    @Override
    public void logoutFinish() {

    }
    @Override
    protected boolean isShowBack() {
        return false;
    }
}
