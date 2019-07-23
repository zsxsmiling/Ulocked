package com.kybss.ulocked.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.ManagerController;
import com.kybss.ulocked.model.bean.HelpRecords;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.DateUtil;
import com.kybss.ulocked.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018\4\26 0026.
 */
@ContentView(R.layout.activity_detail_help_manager)
public class HelpManagerActivity extends BaseActivity<ManagerController.ManagerUiCallbacks>
        implements ManagerController.ManagerDetailUi {


    @BindView(R.id.edit_name)
    TextView mNameTxt;

    @BindView(R.id.edit_theme)
    TextView mThemeTxt;

    @BindView(R.id.edit_contact)
    TextView mContactTxt;

    @BindView(R.id.edit_start_date)
    TextView mStartDateTxt;

    @BindView(R.id.edit_start_time)
    TextView mStartTimeTxt;

    @BindView(R.id.btn_approve)
    Button mApproveBtn;

    @BindView(R.id.btn_refuse)
    Button mRefuseBtn;

    private HelpRecords helper;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getManagerController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        helper = (HelpRecords) intent.getSerializableExtra(Display.PARAM_OBJ);
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        if (helper == null) {
            ToastUtil.showToast("信息为空");
            return;
        } else {
            mNameTxt.setText(helper.getHelper());
            mThemeTxt.setText(helper.getReason());
            mContactTxt.setText(helper.getContact());
            mStartDateTxt.setText(DateUtil.getStampDate(helper.getCreated_at()));
        }

    }

    @OnClick({R.id.btn_approve, R.id.btn_refuse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_approve:
                getCallbacks().reviewHelp(helper.getId(), 1);
                break;
            case R.id.btn_refuse:
                getCallbacks().reviewHelp(helper.getId(), 0);
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
