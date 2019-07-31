package com.kybss.ulocked.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.common.base.Preconditions;
import com.kybss.ulocked.R;
import com.kybss.ulocked.model.bean.HelpRecords;
import com.kybss.ulocked.model.bean.Lock;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.Picker;
import com.kybss.ulocked.ui.activity.BluetoothActivity;
import com.kybss.ulocked.ui.activity.DetailActivity;
import com.kybss.ulocked.ui.activity.DormActivity;
import com.kybss.ulocked.ui.activity.HelpManagerActivity;
import com.kybss.ulocked.ui.activity.HelpTokenActivity;
import com.kybss.ulocked.ui.activity.LockRecordActivity;
import com.kybss.ulocked.ui.activity.LoginActivity;
import com.kybss.ulocked.ui.activity.GeneralCodeActivity;
import com.kybss.ulocked.ui.activity.ManagerActivity;
import com.kybss.ulocked.ui.activity.MeetingDNActivity;
import com.kybss.ulocked.ui.activity.MeetingQGActivity;
import com.kybss.ulocked.ui.activity.MeetingZDActivity;
import com.kybss.ulocked.ui.activity.OrderRoomActivity;
import com.kybss.ulocked.ui.activity.RegisterActivity;
import com.kybss.ulocked.ui.activity.ResetCodeActivity;
import com.kybss.ulocked.ui.activity.ScanActivity;
import com.kybss.ulocked.ui.activity.UserActivity;
import com.kybss.ulocked.ui.fragment.ResetCodeStepOneFragment;
import com.kybss.ulocked.ui.fragment.ResetCodeStepTwoFragment;
import com.kybss.ulocked.ui.fragment.UserMessageFragment;
import com.kybss.ulocked.ui.fragment.UserSubmitInfoFragment;
import com.kybss.ulocked.ui.fragment.UserVerifyMobileFragment;
import com.kybss.ulocked.util.EventUtil;
import com.kybss.ulocked.util.PickedListener;
import com.kybss.ulocked.util.RegisterStep;
import com.kybss.ulocked.util.ResetCodeStep;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2018\4\4 0004.
 */

public class Display {

    public static final String PARAM_ID = "_id";
    public static final String PARAM_OBJ = "_obj";
    public static final String PARAM_OBK = "_obk";

    private final AppCompatActivity mActivity;
    private Calendar calender ;
    private final Picker picker;


    public Display(AppCompatActivity activity) {
        mActivity = Preconditions.checkNotNull(activity, "activity cannot be null");
        picker = new Picker();
        EventUtil.register(this);
    }



    public void UserProfile() {

        Intent intent = new Intent(mActivity, UserActivity.class);
        mActivity.startActivity(intent);

    }

    /**
     * 设置app bar上的主标题
     * @param title
     */
    public void setActionBarTitle(CharSequence title) {
        ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }
    }

    /**
     * 设置app bar上的子标题
     * @param title
     */
    public void setActionBarSubtitle(CharSequence title) {
        ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setSubtitle(title);
        }
    }

    /**
     * 显示向上导航的按钮
     * @param isShow
     */
    public void showUpNavigation(boolean isShow) {
        final ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(isShow);
            ab.setHomeButtonEnabled(isShow);
            if (isShow) {
                ab.setHomeAsUpIndicator(R.drawable.ic_back);
            }
        }
    }

    /**
     * 判断当前activity是否已经嵌套了fragment
     * @return
     */
    public boolean hasMainFragment() {
        return getBackStackFragmentCount() > 0;
    }

    /**
     * 获取回退栈里fragment的数量
     * @return
     */
    public int getBackStackFragmentCount() {
        return mActivity.getSupportFragmentManager().getBackStackEntryCount();
    }

    /**
     * 弹出回退栈里最顶上的fragment
     * 如果栈里只有一个fragment的话,则在弹出的同时并结束掉当前的activity
     * @return
     */
    public boolean popTopFragmentBackStack() {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        final int backStackCount = fm.getBackStackEntryCount();
        if (backStackCount > 1) {
            fm.popBackStack();
            return true;
        }

        return false;
    }

    /**
     * 弹出回退栈里的所有fragment
     * @return
     */
    public boolean popEntireFragmentBackStack() {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        final int backStackCount = fm.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            fm.popBackStack();
        }
        return backStackCount > 0;
    }

    /**
     * 回退
     * @return
     */
    public void navigateUp() {
        final Intent intent = NavUtils.getParentActivityIntent(mActivity);
        if (intent != null) {
            NavUtils.navigateUpTo(mActivity, intent);
        } else {
            finishActivity();
        }
    }

    public void finishActivity() {
        mActivity.finish();
    }

    /**
     * 拨打电话
     * @param phone
     */
    public void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        mActivity.startActivity(intent);
    }


    public void showTimePickerDialog(PickedListener mPickedListener){
        calender = Calendar.getInstance(Locale.CHINA);
        TimePickerDialog dialog = new TimePickerDialog(mActivity,  mPickedListener,
                calender.get(Calendar.HOUR_OF_DAY),calender.get(Calendar.MINUTE),false);
        dialog.show();
    }

    public void showDatePickerDialog(PickedListener mPickedListener){
        calender = Calendar.getInstance(Locale.CHINA);
        DatePickerDialog dialog = new
                DatePickerDialog(mActivity,mPickedListener,calender.get(Calendar.YEAR), calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void showFragment(Fragment fragment) {
        mActivity.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(hasMainFragment() ? R.anim.right_in : 0, R.anim.left_out,
                        R.anim.left_in, R.anim.right_out)
                .replace(R.id.fragment_main, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showLogin() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        mActivity.startActivity(intent);
    }

    public void showRegister() {
        Intent intent = new Intent(mActivity, RegisterActivity.class);
        mActivity.startActivity(intent);
    }

    public void showresetCode() {
        Intent intent = new Intent(mActivity, ResetCodeActivity.class);
        mActivity.startActivity(intent);
    }


    public void showCode() {
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        Intent intent = new Intent(mActivity, GeneralCodeActivity.class);
        mActivity.startActivity(intent);
    }

    public void showHelpToken(){
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        Intent intent = new Intent(mActivity, HelpTokenActivity.class);
        mActivity.startActivity(intent);
    }

    public void showOrder(Meeting meeting) {
        Intent intent = new Intent(mActivity, OrderRoomActivity.class);
        intent.putExtra(PARAM_OBJ, meeting);
        mActivity.startActivity(intent);
    }

    public void showRecode() {
        Intent intent = new Intent(mActivity, LockRecordActivity.class);
        mActivity.startActivity(intent);

    }

    public void showScan() {
        Intent intent = new Intent(mActivity, ScanActivity.class);
        mActivity.startActivity(intent);
    }

    public void showRegisterStep(RegisterStep step, String mobile) {
        switch (step) {
            case STEP_FIRST:
                showFragment(new UserVerifyMobileFragment());
                break;
            case STEP_SECOND:
                showFragment(UserSubmitInfoFragment.create(mobile));
                break;
        }
    }

    public void showResetCodeStep(ResetCodeStep step, String mobile) {
        switch (step) {
            case STEP_FIRST:
                showFragment(new ResetCodeStepOneFragment());
                break;
            case STEP_SECOND:
                showFragment(ResetCodeStepTwoFragment.create(mobile));
                break;
        }
    }


    public void showUserFragment() {

        showFragment(new UserMessageFragment());

    }

    public void showActivityDetail(Meeting meeting) {
        Intent intent = new Intent(mActivity, DetailActivity.class);
        intent.putExtra(PARAM_OBJ, meeting);
        mActivity.startActivity(intent);
    }

    public void showManagerDetail(Meeting meeting) {
        Intent intent = new Intent(mActivity, ManagerActivity.class);
        intent.putExtra(PARAM_OBJ, meeting);
        mActivity.startActivity(intent);

    }

    public void showBluetooth(Lock lock) {
        Intent intent = new Intent(mActivity, BluetoothActivity.class);
        intent.putExtra(PARAM_OBJ, lock);
        mActivity.startActivity(intent);

    }

    public void showHelpManager(HelpRecords recode) {
        Intent intent = new Intent(mActivity, HelpManagerActivity.class);
        intent.putExtra(PARAM_OBJ, recode);
        mActivity.startActivity(intent);
    }
    public void showQGActivity() {
        Intent intent = new Intent(mActivity, MeetingQGActivity.class);

        mActivity.startActivity(intent);
    }
    public void showDNActivity() {
        Intent intent = new Intent(mActivity, MeetingDNActivity.class);

        mActivity.startActivity(intent);
    }
    public void showZDActivity() {
        Intent intent = new Intent(mActivity, MeetingZDActivity.class);

        mActivity.startActivity(intent);
    }

    public void showDormActivity() {
        Intent intent = new Intent(mActivity, DormActivity.class);

        mActivity.startActivity(intent);
    }


}

