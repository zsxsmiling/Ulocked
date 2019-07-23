package com.kybss.ulocked.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.RegisterStep;


/**
 * Created by Administrator on 2018\4\11 0011.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserRegisterUi {

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }
    @Override
    protected void handleIntent(Intent intent, Display display) {
        if (!display.hasMainFragment()) {
            display.showRegisterStep(RegisterStep.STEP_FIRST, null);
        }
    }


}
