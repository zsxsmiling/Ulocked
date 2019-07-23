package com.kybss.ulocked.ui.fragment;



import android.os.Bundle;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.base.BaseMeetFragment;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.MeetController;
import com.kybss.ulocked.model.bean.Floor;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.util.StringUtil;
import com.kybss.ulocked.util.ToastUtil;
import com.kybss.ulocked.widget.BootomDialog;
import com.kybss.ulocked.widget.ChooseFloorDialogDN;
import com.kybss.ulocked.widget.ChooseFloorDialogQG;
import com.kybss.ulocked.wrapper.adapter.MeetingListAdapter;
import java.util.List;

import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_ORDER_CLICKED;

/**
 * Created by Administrator on 2018\4\6 0006.
 */

public class MeetDNFragment extends BaseMeetFragment<Meeting, MeetController.MeetUiCallbacks>
        implements MeetController.MeetListUi, BootomDialog.OnViewClickListener {


    private List<Meeting> floors;


    @Override
    protected MeetingListAdapter getAdapter() {
        return new MeetingListAdapter();
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getMeetController();
    }

    @Override
    protected int getMenuResourceId() {
        return 0;
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        super.initializeViews(savedInstanceState);


    }

    @Override
    public Meeting getRequestParameter() {
        return null;
    }

    @Override
    protected void search(String s) {
        String door_num = StringUtil.getNumFromString(s);
        if(door_num.length() != 3){
            ToastUtil.showToast("请输入完整房间号");
            return;
        }
        getCallbacks().serachBuilding(door_num);
    }

    @Override
    protected void showMenu() {
        BootomDialog dialog = new BootomDialog();
        dialog.setListener(this);
        dialog.show(getActivity().getSupportFragmentManager());
    }

    @Override
    protected void refreshPage(){
        getCallbacks().searchDN();
    }

    @Override
    protected void nextPage(){

    }
    @Override
    protected void onItemClick(int actionId, Meeting meeting) {
        switch (actionId) {
            case CLICK_TYPE_ROOM_ORDER_CLICKED:
                getCallbacks().showOrderRoom(meeting);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mRlScan:
                getCallbacks().showScan();
                break;
            case R.id.mRlLoc:
                new ChooseFloorDialogDN(getActivity(), new ChooseFloorDialogDN.OnConfirmListener() {
                    @Override
                    public void onClick(String curr_building, String curr_floor) {
                        ToastUtil.showToast(curr_building + ";"+curr_floor);
                        getCallbacks().searchFloor("DNY",curr_floor);
                    }
                }).setDate(floors).show();
                break;
        }
    }


    @Override
    public void onFetchAllFloorsFinish(List<Meeting> floors) {
        this.floors=floors;
        getCallbacks().searchDN();
    }


}
