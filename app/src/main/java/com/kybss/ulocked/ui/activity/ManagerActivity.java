package com.kybss.ulocked.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.controller.ManagerController;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.DateUtil;
import com.kybss.ulocked.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\4\26 0026.
 */
@ContentView(R.layout.activity_detail_manager)
public class ManagerActivity extends BaseActivity<ManagerController.ManagerUiCallbacks>
        implements ManagerController.ManagerDetailUi {

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


    @BindView(R.id.btn_approve)
    Button mApproveBtn;

    @BindView(R.id.btn_refuse)
    Button mRefuseBtn;

    private Meeting mMeeting;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getManagerController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mMeeting = (Meeting) intent.getSerializableExtra(Display.PARAM_OBJ);
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {

        if (mMeeting == null) {
            ToastUtil.showToast("信息为空");
            return;
        } else {
            mUserEdit.setText(mMeeting.getUser_id());
            mReasonEdit.setText(mMeeting.getTheme());
            mStartDateEdit.setText(DateUtil.getStampDate(mMeeting.getStart_time()));
            mStartTimeEdit.setText(DateUtil.getStampTime(mMeeting.getStart_time()));
            mEndDateEdit.setText(DateUtil.getStampDate(mMeeting.getEnd_time()));
            mEndTimeEdit.setText(DateUtil.getStampTime(mMeeting.getEnd_time()));
            if(!mMeeting.getStatus().equals("0")){
                mApproveBtn.setEnabled(false);
                mRefuseBtn.setEnabled(false);
            }
        }

    }

    @OnClick({
            R.id.btn_approve,
            R.id.btn_refuse

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_approve:
                getCallbacks().review(mMeeting,1);
                break;
            case  R.id.btn_refuse:
                getCallbacks().review(mMeeting,0);
                break;
        }
        showLoading(R.string.loading_submit_review);
        mApproveBtn.setEnabled(false);
        mRefuseBtn.setEnabled(false);
    }

    @Override
    public void onResponseError(ResponseError error) {
        ToastUtil.showToast(error.getMessage());
        cancelLoading();
        mApproveBtn.setEnabled(true);
        mRefuseBtn.setEnabled(true);
    }

    @Override
    public void onFinishReview() {
        ToastUtil.showToast(R.string.toast_success_submit_review);
        getCallbacks().finish();
    }

}
