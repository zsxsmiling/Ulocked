package com.kybss.ulocked.ui.fragment;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseListFragment;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.ManagerController;
import com.kybss.ulocked.controller.MeetController;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.wrapper.adapter.ManagerListAdapter;
import com.kybss.ulocked.wrapper.adapter.MeetingActivityListAdapter;

import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_ACTIVITY_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_MANAGER_CLICKED;
import static com.kybss.ulocked.util.Constants.HttpCode.HTTP_UNAUTHORIZED;

/**
 * Created by Administrator on 2018\4\25 0025.
 */

public class ManagerFragment extends BaseListFragment<Meeting, ManagerController.ManagerUiCallbacks>
        implements ManagerController.ManagerListUi{
    @Override
    protected boolean isShowToolBar() {
        return false;
    }

    @Override
    protected boolean isShowBack() {
        return false;
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getManagerController();
    }

    @Override
    protected ManagerListAdapter getAdapter() {
        return new ManagerListAdapter();
    }

    @Override
    protected boolean isDisplayError(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            return true;
        }
        return super.isDisplayError(error);
    }

    @Override
    protected int getErrorIcon(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            return R.drawable.ic_unauth;
        }
        return super.getErrorIcon(error);
    }

    @Override
    protected String getErrorTitle(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            return error.getMessage();
        }
        return super.getErrorTitle(error);
    }

    @Override
    protected String getErrorButton(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            return getString(R.string.btn_go_login);
        }
        return super.getErrorTitle(error);
    }

    protected void onRetryClick(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            getCallbacks().showLogin();
        }
        super.onRetryClick(error);
    }

    @Override
    protected void refreshPage() {
        getCallbacks().refreshManagers();
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_null;
    }

    @Override
    protected void onItemClick(int actionId, Meeting meeting) {
        switch (actionId) {
            case CLICK_TYPE_ROOM_MANAGER_CLICKED:
                getCallbacks().showManagerDetail(meeting);
                break;
        }
    }
}
