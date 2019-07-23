package com.kybss.ulocked.widget.bluetooth;

import com.inuker.bluetooth.library.BluetoothClient;
import com.kybss.ulocked.context.AppContext;

/**
 * Created by Administrator on 2018\5\8 0008.
 */

public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(AppContext.getContext());
                }
            }
        }
        return mClient;
    }
}
