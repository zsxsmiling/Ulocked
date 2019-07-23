package com.kybss.ulocked.ui.fragment;

import android.support.v4.app.Fragment;

import com.google.common.base.Preconditions;
import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseTabFragment;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.ManagerController;
import com.kybss.ulocked.controller.MeetController;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.util.StringFetcher;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018\5\25 0025.
 */

public class ActivityTabFragment extends BaseTabFragment<MeetController.MeetUiCallbacks>
        implements MeetController.ActivityTabUi{

    private MeetController.ActivityTab[] mTabs;


    @Override
    protected boolean isShowBack() {
        return false;
    }

    @Override
    protected String getTabTitle(int position) {
        if (mTabs != null) {
            return StringFetcher.getString(mTabs[position]);
        }
        return null;
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getMeetController();
    }


    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_null;
    }


    @Override
    public void setTabs(MeetController.ActivityTab... tabs) {
        Preconditions.checkNotNull(tabs, "tabs cannot be null");
        mTabs = tabs;

        if (getAdapter().getCount() != tabs.length) {
            ArrayList<Fragment> fragments = new ArrayList<>();
            for (MeetController.ActivityTab tab : tabs) {
                fragments.add(createFragmentForTab(tab));
            }
            setFragments(fragments);
        }
    }

    private Fragment createFragmentForTab(MeetController.ActivityTab tab) {
        switch (tab) {
            case ACTIVITY:
                return new ActivityFragment();
            case DORM:
                return new ActivityDormFragment();

        }
        return null;
    }

    @Override
    public Meeting getRequestParameter() {
        return null;
    }
}

