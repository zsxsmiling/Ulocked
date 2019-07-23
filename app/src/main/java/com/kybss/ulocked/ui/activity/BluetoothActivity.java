package com.kybss.ulocked.ui.activity;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.MeetController;
import com.kybss.ulocked.cublicble.BLEDevice.RFStarBLEBroadcastReceiver;
import com.kybss.ulocked.cublicble.BLEHelper;
import com.kybss.ulocked.cublicble.BLEManager;
import com.kybss.ulocked.cublicble.BLEManager.RFStarManageListener;
import com.kybss.ulocked.cublicble.RFStarBLEService;
import com.kybss.ulocked.model.bean.Lock;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ToastUtil;
import com.kybss.ulocked.util.Y_Spin;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\4\26 0026.
 */
@ContentView(R.layout.activity_bluetooth)
public class BluetoothActivity extends BaseActivity<MeetController.MeetUiCallbacks>
        implements MeetController.BluetoothUi, RFStarBLEBroadcastReceiver,RFStarManageListener {

    private Lock mLock;
    private boolean prohibit_back;
    private CountDownTimer mTimer;
    private boolean first_receive;
    private boolean find_target;
    private static int sign;
    private Timer timer;

    @BindView(R.id.bluetooth)
    ImageView mBluetoothImg;

    @BindView(R.id.timer)
    TextView mTimerTxt;

    @BindView(R.id.btn_try)
    Button mTryBtn;

    @BindView(R.id.showmsg)
    TextView mShowmsgTxt;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getMeetController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mLock = (Lock) intent.getSerializableExtra(Display.PARAM_OBJ);
        if(null == mLock){
            ToastUtil.showToast(R.string.toast_error_empty_object);
        }
    }

    @Override
    protected void onSupersetContenView() {

    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {

        if (!mLock.isAdmin()) {
        }
        find_target=false;
        first_receive=true;
        prohibit_back=false;
        timer = new Timer();
        sign=0;
        mShowmsgTxt.setVisibility(View.INVISIBLE);
        mTryBtn.setVisibility(View.INVISIBLE);
        startAnim(mBluetoothImg);
        if(BLEHelper.isEnable()){
            BLEHelper.bindListener(this);
            BLEHelper.startScan();
        }else{
            BLEHelper.enableBle(this);
            BLEHelper.bindListener(this);
        }
        initTimer();
    }

    private void initTimer(){
        if (mTimer == null) {
            mTimer = new CountDownTimer((long) (5 * 1000), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int remainTime = (int) (millisUntilFinished / 1000L);
                    mTimerTxt.setText(""+remainTime);
                    }
                @Override
                public void onFinish() {
                    ToastUtil.showToast("门已重新锁定，开锁请重新扫码");
                    BLEHelper.closeBLE();
                    mBluetoothImg.clearAnimation();
                    getCallbacks().close();
                }
             };
            }
        }

    @OnClick({
            R.id.btn_try,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_try:
                find_target=false;
                BLEHelper.startScan();
                mTryBtn.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public Meeting getRequestParameter() {
        return null;
    }




    @Override
    public void onResponseError(ResponseError error) {
        ToastUtil.showToast(error.getMessage());
    }




    private void startAnim(View imageView) {
        Y_Spin myYAnimation = new Y_Spin();
        myYAnimation.setRepeatCount(Animation.INFINITE); //旋转的次数（无数次）
        imageView.startAnimation(myYAnimation);
    }

    @Override
    public void onReceive(Context context, Intent intent, String macData, String uuid) {
        if (RFStarBLEService.ACTION_DATA_AVAILABLE.equals(intent.getAction()))
        {
            if (uuid.contains("ffe4")) {
                byte[] data = intent
                        .getByteArrayExtra(RFStarBLEService.EXTRA_DATA);
                try {
                    timerCancel();
                    String response = new String(data, "GB2312");
                    if(response.startsWith("2")){
                        if(first_receive){
                            mShowmsgTxt.setVisibility(View.VISIBLE);
                            mTimer.start();
                            prohibit_back=true;
                            cancelLoading();
                            first_receive=false;
                        }

                    }else if(response.startsWith("0")){
                            ToastUtil.showToast(R.string.toast_success_ble_unlock);
                    }else if(response.startsWith("1")){
                            ToastUtil.showToast(R.string.toast_failed_ble_unlock);
                    }
                       /* mTimer.cancel();
                        BLEHelper.closeBLE();
                        mBluetoothImg.clearAnimation();
                        getCallbacks().close();*/
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else if(RFStarBLEService.ACTION_GATT_CONNECTED.equals(intent.getAction())){
            showLoading("连接成功，尝试开锁中");
            final String text;
            if (mLock.isAdmin()){
            text = "#666"+mLock.getCurrentPwd()+mLock.getNextPwd()+"*";}
            else text = "#000"+mLock.getData().getCurrentPwd()+mLock.getData().getNextPwd()+"*";
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    BLEHelper.writeValue(text);
                    Log.e("timer",text);
                }
            },0,500);
        }
    }

    @Override
    public void RFstarBLEManageListener(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.e("mac",device.getAddress()+";"+mLock.getMac());
        if(device.getAddress().equals(mLock.getMac()))
        {
            //String text = (mLock.isAdmin()?"0":"1")+mLock.getCurrent_pwd()+mLock.getNext_pwd();
            find_target=true;
            BLEHelper.findAndConn(device,this);
            showLoading(R.string.loading_connecting_target_ble);
        }
    }

    @Override
    public void RFstarBLEManageStartScan() {
        showLoading("开始扫描目标设备");
    }

    @Override
    public void RFstarBLEManageStopScan() {
        if(!find_target){
            if(getSign()==1){
                find_target=false;
                BLEHelper.startScan();
                mTryBtn.setVisibility(View.INVISIBLE);
            }else{
                mTryBtn.setVisibility(View.VISIBLE);
                ToastUtil.showToast("没有发现目标，请重新尝试");
                cancelLoading();
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestcode",""+requestCode+";"+resultCode);
        if (BLEManager.REQUEST_CODE == requestCode) {
            if(resultCode==-1){
                //用户开启蓝牙
                BLEHelper.startScan();
            }
            if(resultCode==0){
                ToastUtil.showToast("拒绝开启蓝牙");
               this.finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        if(prohibit_back){
            ToastUtil.showToast("请等待关闭");
        }else{
            if(BLEHelper.isEnable()){
                BLEHelper.closeBLE();
            }
            super.onBackPressed();
        }

    }

    private int getSign(){
        return sign=sign==0?1:0;
    }

    private void timerCancel(){
        if(timer!=null){
            timer.cancel();
        }
    }
}
