package com.kybss.ulocked.ui.activity;

import android.content.Intent;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;


/**
 * Created by Administrator on 2018\4\11 0011.
 */
@ContentView(R.layout.activity_register)
public class UserActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserActivityUi {

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }
    @Override
    protected void handleIntent(Intent intent, Display display) {
        if (!display.hasMainFragment()) {
            display.showUserFragment();
        }
    }


}
