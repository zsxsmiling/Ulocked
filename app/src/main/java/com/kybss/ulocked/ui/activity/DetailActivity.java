package com.kybss.ulocked.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.controller.MeetController;
import com.kybss.ulocked.model.bean.MeetRoomStatus;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.DateUtil;
import com.kybss.ulocked.util.StringUtil;
import com.kybss.ulocked.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\4\26 0026.
 */
@ContentView(R.layout.activity_detail_activity)
public class DetailActivity extends BaseActivity<MeetController.MeetUiCallbacks>
        implements MeetController.ActivityDetailUi {

    @BindView(R.id.edit_user)
    EditText mUserEdit;

    @BindView(R.id.edit_theme)
    EditText mReasonEdit;

    @BindView(R.id.edit_start_date)
    EditText mStartDateEdit;

    @BindView(R.id.edit_start_time)
    EditText mStartTimeEdit;

    @BindView(R.id.edit_end_date)
    EditText mEndDateEdit;

    @BindView(R.id.edit_end_time)
    EditText mEndTimeEdit;

    @BindView(R.id.edit_result)
    EditText mResultEdit;

    @BindView(R.id.edit_admin)
    EditText mAdminEdit;

    @BindView(R.id.edit_admin_time)
    EditText mAdminTimeEdit;

    @BindView(R.id.btn_confirm)
    Button mConfirmBtn;

    @BindView(R.id.btn_finish)
    Button mFinishBtn;

    @BindView(R.id.layout_verify_msg)
    LinearLayout mVerifyMsg;

    private Meeting mMeeting;

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

        if(mMeeting == null ){
            ToastUtil.showToast("信息为空");
            return;
        }else {
            mUserEdit.setText(AppCookie.getUserInfo().getUsername());
            mReasonEdit.setText(mMeeting.getTheme());
            mStartDateEdit.setText(DateUtil.getStampDate(mMeeting.getStart_time()));
            mStartTimeEdit.setText(DateUtil.getStampTime(mMeeting.getStart_time()));
            mEndDateEdit.setText(DateUtil.getStampDate(mMeeting.getEnd_time()));
            mEndTimeEdit.setText(DateUtil.getStampTime(mMeeting.getEnd_time()));
            mResultEdit.setText(StringUtil.decodeStatus(mMeeting.getStatus()));
            if(mMeeting.getStatus().equals(MeetRoomStatus.STATUS_PEDNDING)){
                mVerifyMsg.setVisibility(View.INVISIBLE);
                showCloseOrFinish(false);
            }else {
                showCloseOrFinish(mMeeting.getStatus().equals(MeetRoomStatus.STATUS_ALLOWED));
                mAdminEdit.setText(mMeeting.getAuditor());
                mAdminTimeEdit.setText(DateUtil.getStampDate(mMeeting.getUpdated_at())
                +DateUtil.getStampTime(mMeeting.getUpdated_at()));
            }
        }

    }

    @Override
    public Meeting getRequestParameter() {
        return null;
    }


    @OnClick({
            R.id.btn_confirm,
            R.id.btn_finish

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                getCallbacks().close();
                break;
            case R.id.btn_finish:
                getCallbacks().finishMeeting(mMeeting);
                break;
        }
    }


    private void showCloseOrFinish(boolean isFinish){
        if(isFinish) {
        mFinishBtn.setVisibility(View.VISIBLE);
        mConfirmBtn.setVisibility(View.GONE);
        }else {
            mFinishBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onFinishMeeting() {
        getCallbacks().close();
    }
}
