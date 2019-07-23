package com.kybss.ulocked.widget.bluetooth;

import android.bluetooth.BluetoothAdapter;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.inuker.bluetooth.library.utils.StringUtils;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;

/**
 * Created by Administrator on 2018\5\8 0008.
 */

public class BleService {

    public static final UUID CHARACTER= UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static final UUID SERVICE=UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static IBlueToothCallBack iBlueToothCallBack;
    private static String mac_address;
    private static boolean mConnected;
    private static boolean mIsNotify;
    public static void init(IBlueToothCallBack mCallback){
        ClientManager.getClient().openBluetooth();
        iBlueToothCallBack = mCallback;
        mIsNotify = false;
    }

    //查找设备
    public static void searchDevice(String mac) {
        mac_address = mac;
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2).build();
        ClientManager.getClient().search(request, mSearchResponse);
    }
    //查找设备监听器
    private static final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            BluetoothLog.w("MainActivity.onSearchStarted");
            iBlueToothCallBack.onStartDiscovery();
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            if (device.getAddress().equals(mac_address)) {
                iBlueToothCallBack.onFindTargetble(device.getAddress());
                //ClientManager.getClient().stopSearch();
            }
        }

        @Override
        public void onSearchStopped() {
            BluetoothLog.w("MainActivity.onSearchStopped");
            iBlueToothCallBack.onFailedFindTargetble();
        }

        @Override
        public void onSearchCanceled() {
        }
    };
    //停止扫描
    private static void stopSearch(){
        ClientManager.getClient().stopSearch();
    }

    //连接
    public static void connect(){
        stopSearch();
        ClientManager.getClient().registerConnectStatusListener(mac_address, mConnectStatusListener);
        connectDeviceIfNeeded();
    }

    private static final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            mConnected = (status == STATUS_CONNECTED);
            connectDeviceIfNeeded();
        }
    };

    private static void connectDeviceIfNeeded() {
        if (!mConnected) {
            connectDevice();
        }
    }

    private static void connectDevice() {
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();

        ClientManager.getClient().connect(mac_address, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    iBlueToothCallBack.onConnectSuccess();
                }
                else {
                    iBlueToothCallBack.onConnectFiled();
                }
            }
        });
    }


    public static void write(String str){
        ClientManager.getClient().write(mac_address, SERVICE, CHARACTER,
                ByteUtils.stringToBytes(StringUtils.toHexString(str)), mWriteRsp);
    }
    private static final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                iBlueToothCallBack.onSendMessegeSuccess();
                if(!mIsNotify){
                    read();
                    mIsNotify = true;
                }

            } else {
               iBlueToothCallBack.onSendMessegeFiled();
            }
        }
    };

    public static boolean isAllowble(){
        BluetoothAdapter blueadapter= BluetoothAdapter.getDefaultAdapter();
        return blueadapter.isEnabled();
    }


    private static void read(){
        ClientManager.getClient().notify(mac_address,SERVICE, CHARACTER, mNotifyRsp);
    }
    private static final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(SERVICE) && character.equals(CHARACTER)) {
                String response =StringUtils.toStringHex(String.format("%s", ByteUtils.byteToString(value)));
                iBlueToothCallBack.onReecivedSuccess(response);
            }
        }
        @Override
        public void onResponse(int code) {
        }
    };

    public static void close(){
        ClientManager.getClient().disconnect(mac_address);
        //ClientManager.getClient().unregisterConnectStatusListener(mac_address, mConnectStatusListener);
        ClientManager.getClient().closeBluetooth();
    }
}

