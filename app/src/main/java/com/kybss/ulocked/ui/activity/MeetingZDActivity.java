package com.kybss.ulocked.ui.activity;



import android.Manifest;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.MainController;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.ui.fragment.ActivityFragment;
import com.kybss.ulocked.ui.fragment.ActivityTabFragment;
import com.kybss.ulocked.ui.fragment.AdminFragment;
import com.kybss.ulocked.ui.fragment.MeetQGFragment;
import com.kybss.ulocked.ui.fragment.MeetZDFragment;
import com.kybss.ulocked.ui.fragment.UserCenterFragment;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.DoubleExitUtil;
import com.kybss.ulocked.util.MainTab;
import com.kybss.ulocked.util.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.kybss.ulocked.util.Constants.Permission.EASY_PERMISSION_BLUETOOTH;

/**
 * Created by Administrator on 2018\4\5 0005.
 */
@ContentView(R.layout.activity_main)
public class MeetingZDActivity extends BaseActivity<MainController.MainUiCallbacks>
        implements MainController.MainHomeUi,EasyPermissions.PermissionCallbacks{


    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.viewpager_tab)
    SmartTabLayout mViewpagerTab;

    private DoubleExitUtil mDoubleClickExit;


    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController();
    }
    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        final LayoutInflater inflater = LayoutInflater.from(this);


        final int[] tabIcons = {R.drawable.tab_ic_home,R.drawable.tab_ic_activity,
                R.drawable.tab_ic_manage ,R.drawable.tab_ic_me} ;
        final int[] tabTitles = {R.string.tab_home,R.string.tab_activity,
                R.string.tab_manager,R.string.tab_me};
        FragmentPagerItems pages = FragmentPagerItems.with(this)
                .add(R.string.tab_home, MeetZDFragment.class)
                .add(R.string.tab_activity, ActivityTabFragment.class)
                .add(R.string.tab_manager, AdminFragment.class)
                .add(R.string.tab_me,UserCenterFragment.class)
                .create();

       /* final int[] tabIcons = {R.drawable.tab_ic_home,R.drawable.tab_ic_activity,
                R.drawable.tab_ic_me} ;
        final int[] tabTitles = {R.string.tab_home,R.string.tab_activity,
                R.string.tab_me};
        FragmentPagerItems pages = FragmentPagerItems.with(this)
                .add(R.string.tab_home, MeetQGFragment.class)
                .add(R.string.tab_activity,ActivityFragment.class)
                .add(R.string.tab_me,UserCenterFragment.class)
                .create();*/

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                pages);

        mViewPager.setOffscreenPageLimit(pages.size());
        mViewPager.setAdapter(adapter);
        mViewpagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                View view = inflater.inflate(R.layout.layout_navigation_bottom_item, container, false);
                ImageView iconView = (ImageView) view.findViewById(R.id.img_icon);
                iconView.setBackgroundResource(tabIcons[position % tabIcons.length]);
                TextView titleView = (TextView) view.findViewById(R.id.txt_title);
                titleView.setText(tabTitles[position % tabTitles.length]);
                return view;
            }
        });
        mViewpagerTab.setViewPager(mViewPager);
        mDoubleClickExit = new DoubleExitUtil(this);
        requireSomePermission();

    }



    @Override
    protected void handleIntent(Intent intent, Display display) {
        if (intent != null && intent.hasExtra(Display.PARAM_OBJ)) {
            MainTab tab = (MainTab) intent.getSerializableExtra(Display.PARAM_OBJ);
            switch (tab) {
                default:
                case DOORSTATE:
                    mViewPager.setCurrentItem(0);
                    break;
                case PERSON:
                    mViewPager.setCurrentItem(1);
                    break;
            }
        }
    }

    @AfterPermissionGranted(EASY_PERMISSION_BLUETOOTH)
    private void requireSomePermission() {
        String[] perms = {
                // 把你想要申请的权限放进这里就行，注意用逗号隔开
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            ToastUtil.showToast("已授予蓝牙权限");

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "请授予权限以下权限",
                    EASY_PERMISSION_BLUETOOTH, perms);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        String s = requestCode + perms.toString();

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        String s = requestCode + perms.toString();
    }




    private void beginAnimator(View mView){
        ValueAnimator colorAnim = ObjectAnimator.ofFloat(mView,"Alpha",
                0.2f,1.0f);
        colorAnim.setDuration(300);
        //colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setEvaluator(new FloatEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    @Override
    public void getWeather(String weather) {

    }
}

