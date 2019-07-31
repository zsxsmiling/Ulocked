package com.kybss.ulocked.ui.activity;

import android.content.Intent;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ResetCodeStep;

@ContentView(R.layout.activity_register)
public class ResetCodeActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserRegisterUi {

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }
    @Override
    protected void handleIntent(Intent intent, Display display) {
        if (!display.hasMainFragment()) {
            display.showResetCodeStep(ResetCodeStep.STEP_FIRST, null);
        }
    }


}