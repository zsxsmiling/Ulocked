package com.kybss.ulocked.cublicble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;


import com.kybss.ulocked.context.AppContext;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018\11\19 0019.
 */

public class BLEHelper {

    private static BLEManager manager;
    private BLEHelper(){
    }
    public static void init(){
       manager = new BLEManager(AppContext.getContext());
    }

    public static void disconnectDevice(){
        if (manager.cubicBLEDevice != null) {
            manager.cubicBLEDevice.disconnectedDevice();
            manager.cubicBLEDevice = null;
        }
    }

    public static void bindListener(BLEManager.RFStarManageListener listener){
        manager.setRFstarBLEManagerListener(listener);
    }
    public static void enableBle(Activity ac){
        if (manager.cubicBLEDevice != null) {
            manager.cubicBLEDevice.disconnectedDevice();
            manager.cubicBLEDevice = null;
        }
        manager.isEdnabled(ac);
    }
    public static void startScan(){
        if (manager.cubicBLEDevice != null) {
            manager.cubicBLEDevice.disconnectedDevice();
            manager.cubicBLEDevice = null;
        }
        manager.startScanBluetoothDevice();

    }

    public static void writeValue(String str){
        if (manager.cubicBLEDevice != null){
            manager.cubicBLEDevice.writeValue("ffe5", "ffe9", str.getBytes());
            manager.cubicBLEDevice.setNotification("ffe0", "ffe4",
                    true);
        }
    }

    public static void weakBLE(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (manager.cubicBLEDevice != null){
                    manager.cubicBLEDevice.writeValue("ffe5", "ffe9", "0".getBytes());
                    manager.cubicBLEDevice.setNotification("ffe0", "ffe4",
                            true);
                }
            }
        }, 4000);
    }

    public static void writeValue(final String str, long time){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (manager.cubicBLEDevice != null){
                    manager.cubicBLEDevice.writeValue("ffe5", "ffe9", str.getBytes());
                }
            }
        }, time);
    }
    public static void findAndConn(BluetoothDevice device,
                                    BLEDevice.RFStarBLEBroadcastReceiver delegate){
        manager.stopScanBluetoothDevice();
        manager.bluetoothDevice = device;
        manager.cubicBLEDevice = new CubicBLEDevice(
                AppContext.getContext(), manager.bluetoothDevice);
        manager.cubicBLEDevice.setBLEBroadcastDelegate(delegate);
    }

    public static ArrayList<String> getBLEs(){
        return manager.getScanBluetoothAdds();
    }

    public static boolean isEnable(){
        BluetoothAdapter blueadapter= BluetoothAdapter.getDefaultAdapter();
        return blueadapter.isEnabled();
    }
    public static void closeBLE(){
        disconnectDevice();
        BluetoothAdapter blueadapter= BluetoothAdapter.getDefaultAdapter();
        blueadapter.disable();
    }
}
