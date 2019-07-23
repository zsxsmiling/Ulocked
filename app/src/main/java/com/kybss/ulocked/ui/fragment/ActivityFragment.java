package com.kybss.ulocked.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseListFragment;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.MeetController;
import com.kybss.ulocked.model.bean.Door;
import com.kybss.ulocked.model.bean.Lock;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.model.event.AccountChangedEvent;
import com.kybss.ulocked.model.event.ItemSelectedEvent;
import com.kybss.ulocked.util.EventUtil;
import com.kybss.ulocked.util.PreferenceUtil;
import com.kybss.ulocked.util.ToastShow;
import com.kybss.ulocked.util.ToastUtil;
import com.kybss.ulocked.wrapper.adapter.MeetingActivityListAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_ACTIVITY_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_EAST_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_WEST_CLICKED;
import static com.kybss.ulocked.util.Constants.HttpCode.HTTP_UNAUTHORIZED;

/**
 * Created by Administrator on 2018\4\25 0025.
 */

public class ActivityFragment extends BaseListFragment<Meeting, MeetController.MeetUiCallbacks>
        implements MeetController.ActivityListUi{


    private String mac_add;
    @Override
    protected boolean isShowToolBar() {
        return false;
    }
    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        super.initializeViews(savedInstanceState);
        EventUtil.register(this);
    }

    @Override
    protected boolean isShowBack() {
        return false;
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getMeetController();
    }

    @Override
    protected MeetingActivityListAdapter getAdapter() {
        return new  MeetingActivityListAdapter();
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
        getCallbacks().refreshActivities();
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_null;
    }



    @Override
    public Meeting getRequestParameter() {
        return null;
    }

    @Subscribe
    public void onItemSelected(ItemSelectedEvent event){
        if(null!=event.getD_id() && null!=event.getMac()){
            showDialog(event.getMac(),event.getD_id());
        }else{
            ToastUtil.showToast("地址丢失，请联系管理员");
        }

    }


    @Override
    protected void onItemClick(int actionId, Meeting meeting) {
        switch (actionId) {
            case CLICK_TYPE_ROOM_ACTIVITY_CLICKED:
                getCallbacks().showActivityDetail(meeting);
                break;
            case CLICK_TYPE_ROOM_EAST_CLICKED:
                //showDialog(meeting,1);
               // AfterDoorIconClick(meeting,1);
                break;
            case CLICK_TYPE_ROOM_WEST_CLICKED:
                //showDialog(meeting,0);
               // AfterDoorIconClick(meeting,0);
                break;
        }
    }

    @Override
    public void onRequestDoorSuccess(Lock lock) {
        Log.e("mac",mac_add);
        showBluetooth(lock,false);
    }

    private void showBluetooth(Lock lock, boolean b) {
        lock.setMac(mac_add);
        lock.setAdmin(false);
        getCallbacks().showBluetooth(lock);
    }

    private void AfterDoorIconClick(String Mac,String d_id){
            mac_add = Mac;
            getCallbacks().requestDoor(d_id);


    }

    private void showDialog(final String Mac, final String d_id) {
        if (!PreferenceUtil.getBoolean("donotShow", false)) {

            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_warn_dialog, null);
            final CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.donotRemind);
            checkBox.setChecked(true);
            dialog.setTitle("警告！");
            dialog.setView(dialogView);
            dialog.setCancelable(false);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (checkBox.isChecked()) {
                        PreferenceUtil.set("donotShow",true);
                    }
                    AfterDoorIconClick(Mac,d_id);
                }
            });
            dialog.show();
        }else{
            AfterDoorIconClick(Mac,d_id);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtil.unregister(this);
    }
}
