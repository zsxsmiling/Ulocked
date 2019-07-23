package com.kybss.ulocked.controller;


import android.util.Log;

import com.google.common.base.Preconditions;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.model.event.AccountChangedEvent;
import com.kybss.ulocked.network.RestApiClient;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.EventUtil;

import javax.inject.Inject;

public class MainController extends BaseController<MainController.MainUi, MainController.MainUiCallbacks> {


    private final MeetController mMeetController;
    private final UserController mUserController;
    private final ManagerController mManagerController;
    private final DormController mDormController;

    private final RestApiClient mRestApiClient;


    @Inject
    public MainController(MeetController meetController,
                          UserController userController,
                          ManagerController managerController,
                          DormController dormController,

                          RestApiClient restApiClient) {
        super();
        mUserController = Preconditions.checkNotNull(userController, "restApiClient cannot be null");
        mMeetController = Preconditions.checkNotNull(meetController, "restApiClient cannot be null");
        mManagerController=Preconditions.checkNotNull(managerController, "restApiClient cannot be null");
        mDormController=Preconditions.checkNotNull(dormController, "restApiClient cannot be null");

        mRestApiClient = Preconditions.checkNotNull(restApiClient, "restApiClient cannot be null");

    }

    @Override
    protected void onInited() {
        super.onInited();
        mMeetController.init();
        mUserController.init();
        mManagerController.init();
        mDormController.init();

    }

    @Override
    protected void onSuspended() {
        mMeetController.suspend();
        mUserController.suspend();
        mManagerController.suspend();
        mDormController.suspend();

        super.onSuspended();
    }

    public void attachDisplay(Display display) {
        Preconditions.checkNotNull(display, "display is null");
        Preconditions.checkState(getDisplay() == null, "we currently have a display");
        setDisplay(display);
    }

    public void detachDisplay(Display display) {
        Preconditions.checkNotNull(display, "display is null");
        Preconditions.checkState(getDisplay() == display, "display is not attached");
        setDisplay(null);
    }

    @Override
    public void setDisplay(Display display) {
        super.setDisplay(display);
        mMeetController.setDisplay(display);
        mUserController.setDisplay(display);
        mManagerController.setDisplay(display);
        mDormController.setDisplay(display);

    }

    @Override
    protected void populateUi(MainUi ui) {
        if (ui instanceof MainHomeUi) {
            populateMainHomeUi((MainHomeUi) ui);
        }
    }

    private void populateMainHomeUi(MainHomeUi ui) {
        doFetchSetting(getId(ui));
    }

    /**
     * 获取应用配置
     * @param callingId
     */
    private void doFetchSetting(final int callingId) {

    }

    public final MeetController getMeetController() {
        return mMeetController;
    }
    public final UserController getUserController() {
        return mUserController;
    }
    public final ManagerController getManagerController(){return  mManagerController;}
    public final DormController getDormController() {
        return mDormController;
    }


    /**
     * 当返回键被按下时
     */
    public void onBackButtonPressed() {
        Display display = getDisplay();
        if (display != null) {
            if (!display.popTopFragmentBackStack()) {
                display.navigateUp();
            }
        }
    }

    @Override
    protected MainUiCallbacks createUiCallbacks(final MainUi ui) {
        return new MainUiCallbacks() {
            @Override
            public void showCode() {
                Log.e("showcode","showcode");
            }

            @Override
            public void showScan() {
                Display display = getDisplay();
                if (display != null) {
                    if (AppCookie.isLoggin()) {
                        display.showScan();
                    } else {
                        display.showLogin();
                    }
                }
            }
        };
    }


    public interface MainUi extends BaseController.Ui<MainUiCallbacks> {

    }

    public interface MainHomeUi extends MainUi {
        void getWeather(String weather);
    }

    public interface MainUiCallbacks {
        void showCode();

        void showScan();
    }


}
