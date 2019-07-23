package com.kybss.ulocked.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.MeetController;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.model.bean.User;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.DateUtil;
import com.kybss.ulocked.util.PickedListener;
import com.kybss.ulocked.util.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\4\9 0009.
 */

@ContentView(R.layout.activity_order_room)
public class OrderRoomActivity extends BaseActivity<MeetController.MeetUiCallbacks>
        implements MeetController.RoomOrderUi {

    private SimpleDateFormat simpleDateFormat;
    private Date date;
    private Date date_end;
    private Meeting mMeeting;
    private String current_time;

    @BindView(R.id.edit_name)
    EditText mUserNameEdit;

    @BindView(R.id.edit_theme)
    EditText mThemeEdit;

    @BindView(R.id.edit_number)
    EditText mNumberEdit;

    @BindView(R.id.edit_start_date)
    EditText mStartDayEdit;

    @BindView(R.id.edit_start_time)
    EditText mStartTimeEdit;

    @BindView(R.id.edit_end_date)
    EditText mEndDayEdit;

    @BindView(R.id.edit_end_time)
    EditText mEndTimeEdit;

    @BindView(R.id.btn_confirm)
    Button mConfirmBtn;

    @BindView(R.id.ImgBtn_start_date)
    ImageButton mStartDateImgBtn;

    @BindView(R.id.ImgBtn_start_time)
    ImageButton mStartTimeImgBtn;

    @BindView(R.id.ImgBtn_end_date)
    ImageButton mEndDateImgBtn;

    @BindView(R.id.ImgBtn_end_time)
    ImageButton mEndTimeImgBtn;



    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getMeetController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mMeeting = (Meeting) intent.getSerializableExtra(Display.PARAM_OBJ);
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        super.initializeViews(savedInstanceState);
        if(mMeeting.getDescription().startsWith("会议室")){
            mNumberEdit.setText("3人或3人以上");
            mThemeEdit.setText("组织会议");
        }else{
            mNumberEdit.setText("1人");
            mThemeEdit.setText("门禁解锁");
        }
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date(System.currentTimeMillis());
        current_time = simpleDateFormat.format(date);
        mStartDayEdit.setText(current_time.substring(0,10));
        mStartTimeEdit.setText(current_time.substring(11));
        mEndTimeEdit.setText(current_time.substring(11));
    }

    @OnClick({
            R.id.btn_confirm,
            R.id.ImgBtn_start_date,
            R.id.ImgBtn_end_date,
            R.id.ImgBtn_start_time,
            R.id.ImgBtn_end_time,
            R.id.edit_start_date,
            R.id.edit_end_date,
            R.id.edit_start_time,
            R.id.edit_end_time

    })
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_confirm:
                orderRoom();
                break;

            case R.id.ImgBtn_start_date:
            case R.id.edit_start_date:

                getCallbacks().showDatePicker(new PickedListener() {
                    @Override
                    public void onPickedFinish(String data) {
                        mStartDayEdit.setText(data);
                    }
                });
                break;
            case R.id.ImgBtn_end_date:
            case R.id.edit_end_date:
                getCallbacks().showDatePicker(new PickedListener() {
                    @Override
                    public void onPickedFinish(String data) {
                        mEndDayEdit.setText(data);
                    }
                });
                break;

            case R.id.ImgBtn_start_time:
            case R.id.edit_start_time:
                getCallbacks().showTiemPicker(new PickedListener() {
                    @Override
                    public void onPickedFinish(String data) {
                        mStartTimeEdit.setText(data);
                    }
                });
                break;
            case R.id.ImgBtn_end_time:
            case R.id.edit_end_time:
                getCallbacks().showTiemPicker(new PickedListener() {
                    @Override
                    public void onPickedFinish(String data) {
                        mEndTimeEdit.setText(data);
                    }
                });
                break;
        }
    }


    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        ToastUtil.showToast(error.getMessage());
    }

    @Override
    public Meeting getRequestParameter() {
        return mMeeting;
    }

    @Override
    public void showUserInfo(User user) {
        mUserNameEdit.setText(user.getUsername());
    }

    @Override
    public void onOrdeRoomFinish() {
        cancelLoading();
        ToastUtil.showToast(R.string.toast_success_order_room);
        getCallbacks().close();
    }


    private void orderRoom() {
        String start_day,end_day,start_time,end_time,theme;
        showLoading(R.string.loading_room_order);
        start_day = mStartDayEdit.getText().toString();
        end_day =mEndDayEdit.getText().toString();
        start_time=mStartTimeEdit.getText().toString();
        end_time =mEndTimeEdit.getText().toString();
        theme = mThemeEdit.getText().toString();
        if(TextUtils.isEmpty(theme))
        {
            ToastUtil.showToast("请提供申请原因");
            cancelLoading();
            return;
        }
        if(TextUtils.isEmpty(start_day) || TextUtils.isEmpty(end_day) ||
                TextUtils.isEmpty(start_time) || TextUtils.isEmpty(end_time))
        {
            ToastUtil.showToast("请选择日期");
            cancelLoading();
            return;
        }


        mMeeting.setStart_time(DateUtil.DateToStamp(start_day,start_time));
        mMeeting.setEnd_time(DateUtil.DateToStamp(end_day,end_time));
        mMeeting.setTheme(theme);
        getCallbacks().orderRoom(mMeeting);
    }
}