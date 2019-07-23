package com.kybss.ulocked.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.Lock;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.util.Constants;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.StringUtil;
import com.kybss.ulocked.util.ToastUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.kybss.ulocked.util.Constants.Permission.EASY_PERMISSION_CAMERA;


/**
 * Created by Administrator on 2018\4\22 0022.
 */
@ContentView(R.layout.activity_scan)
public class ScanActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserScanUi,EasyPermissions.PermissionCallbacks {
    public static boolean isOpen =false; //Led灯是否打开
    private CaptureFragment captureFragment;
   // private static final int num =123;//用于验证获取的权
    private static String pin,door_id,mac; //蓝牙配对密码和门号
    @BindView(R.id.layout_light)
    LinearLayout mLightBtn;

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        super.initializeViews(savedInstanceState);
        requireSomePermission();// 申请权限
        captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_scan);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container,captureFragment)
                .commit();
        /*if(BLEHelper.isEnable()){
            BLEHelper.closeBLE();
        }*/
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }


    @OnClick({
            R.id.layout_light
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_light:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;
        }
    }

    @Override
    public void onResponseError(ResponseError error) {
        ToastUtil.showToast(error.getMessage());
        getCallbacks().close();
    }

    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String code) {
            if(code.length()<69)
            {
                ToastUtil.showToast("请扫描正确的二维码");
                getCallbacks().close();
                return;
            }
            String result = code.substring(38);
            if(!StringUtil.isDoorCode(result)){
                ToastUtil.showToast("请扫描正确的二维码");
                getCallbacks().close();
                return;
            }
            int length = result.length();
            door_id = result.substring(27,length);
            mac = result.substring(4,21);
            if(AppCookie.getUserInfo().getRank()< Constants.Rank.RANK_USER){
                handleManagerClick(door_id);
            }else{
                getCallbacks().requestDoor(door_id);
            }
        }

        @Override
        public void onAnalyzeFailed() {
            ToastUtil.showToast(R.string.toast_error_scan_error);
            getCallbacks().close();
        }
    };


    @Override
    public void onRequestDoorSuccess(Lock lock) {
        showBluetooth(lock,false);
    }

    @Override
    public void onResetPwdSuccess(Lock lock) {
        showBluetooth(lock,true);
    }
    private void showBluetooth(Lock lock, boolean isAdmin) {
        lock.setMac(mac);
        lock.setAdmin(isAdmin);
        getCallbacks().showBluetooth(lock);
        getCallbacks().close();
    }

    @AfterPermissionGranted(EASY_PERMISSION_CAMERA )
    private void requireSomePermission() {
        String[] perms = {
                // 把你想要申请的权限放进这里就行，注意用逗号隔开
                Manifest.permission.CAMERA,
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
          ToastUtil.showToast("相机权限已获取");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "请授予相机权限",
                    EASY_PERMISSION_CAMERA , perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Toast.makeText(this, "Permissions Granted!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //Toast.makeText(this, "Permissions Denied!", Toast.LENGTH_LONG).show();
    }

    /**
     * 管理员选择操作提示框
     */
    private void handleManagerClick(final String mDoor_id) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_option_title)
                .setMessage(R.string.dialog_option_message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_common_user, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCallbacks().requestDoor(mDoor_id);
                    }
                })
                .setNegativeButton(R.string.dialog_admin_user, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCallbacks().resetPwd(mDoor_id);
                    }
                })
                .create()
                .show();
    }

}
