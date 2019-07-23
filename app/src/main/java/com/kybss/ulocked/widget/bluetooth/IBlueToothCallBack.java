package com.kybss.ulocked.widget.bluetooth;

/**
 * Created by Administrator on 2018\5\8 0008.
 */

public interface IBlueToothCallBack {
    void onConnectSuccess();

    void onConnectFiled();

    void onSendMessegeSuccess();

    void onSendMessegeFiled();

    void onFindTargetble(String mac);

    void onStartDiscovery();

    void onFailedFindTargetble();

    void onDisConnect();

    void onReecivedSuccess(String str);
}
