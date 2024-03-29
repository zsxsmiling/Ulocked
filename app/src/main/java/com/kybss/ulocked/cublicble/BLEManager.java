package com.kybss.ulocked.cublicble;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kybss.ulocked.util.ToastUtil;

import java.util.ArrayList;

/*
 管理所有的 蓝牙设备 
 * 			功能:
 * 			   1)扫描所有的蓝牙设备 
 * 			   2)判断蓝牙权限是否打开
 * @author Kevin.wu
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BLEManager {
	private static int SCAN_TIME = 10000; // 扫描的时间为7秒
	public static final int REQUEST_CODE = 0x01;// 返回的唯一标识
	private Context context = null;
	public static BluetoothAdapter bleAdapter = null;

	private CountDownTimer mTimer=null;
	private Handler handler = null;
	private boolean isScanning = false; // 是否正在扫描

	private RFStarManageListener listener = null;

	private ArrayList<BluetoothDevice> scanBlueDeviceArray = new ArrayList<BluetoothDevice>();// 扫描到的数据
	private ArrayList<String> scanBlueDeviceAdd = new ArrayList<String>();

	public BluetoothDevice bluetoothDevice = null; // 选中的设备
	public CubicBLEDevice cubicBLEDevice = null; // 选中的cubicBLEDevice

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public BLEManager(Context context) {
		// TODO Auto-generated constructor stub
		handler = new Handler();

		if (!context.getPackageManager().hasSystemFeature( // 检察系统是否包含蓝牙低功耗的jar包
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(context, "不包含蓝牙4.0的标准Jar包", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		this.context = context;
		BluetoothManager manager = (BluetoothManager) this.context
				.getSystemService(Context.BLUETOOTH_SERVICE);
		bleAdapter = manager.getAdapter();

		if (bleAdapter == null) { // 检察手机硬件是否支持蓝牙低功耗
			Toast.makeText(context, "不劫持ble蓝牙4.0", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	/**
	 * 判断是否开启蓝牙权限
	 * 
	 * @return
	 */
	public boolean isEdnabled(Activity activity) {
		if (!bleAdapter.isEnabled()) {
			if (!bleAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(enableBtIntent, REQUEST_CODE);
			}
			return true;
		}
		return false;
	}

	public ArrayList<BluetoothDevice> getScanBluetoothDevices() {
		return this.scanBlueDeviceArray;
	}
	public ArrayList<String> getScanBluetoothAdds() {
		return this.scanBlueDeviceAdd;
	}
	/**
	 * 设置权限后，返回时调用
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onRequestResult(int requestCode, int resultCode, Intent data) {
		// User chose not to enable Bluetooth.
		if (requestCode == REQUEST_CODE
				&& resultCode == Activity.RESULT_CANCELED) {
			((Activity) this.context).finish();
			return;
		}

	}

	/**
	 * 扫描蓝牙设备
	 */
	public void startScanBluetoothDevice() {
		if (scanBlueDeviceArray != null) {
			scanBlueDeviceArray = null;
		}
		scanBlueDeviceArray = new ArrayList<BluetoothDevice>();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				stopScanBluetoothDevice();
			}
		}, SCAN_TIME); // 10秒后停止扫描
		isScanning = true;
		bleAdapter.startLeScan(bleScanCallback);
		listener.RFstarBLEManageStartScan();
		//mTimer.start();
	}

	/**
	 * 停止扫描蓝牙设备
	 */
	public void stopScanBluetoothDevice() {
		if (isScanning) {
			isScanning = false;
			bleAdapter.stopLeScan(bleScanCallback);
			listener.RFstarBLEManageStopScan();
		}
	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback bleScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
							 final byte[] scanRecord) {
			// TODO 添加扫描到的device，并刷新数据
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!scanBlueDeviceArray.contains(device)) {
						scanBlueDeviceArray.add(device);
						scanBlueDeviceAdd.add(device.getAddress());
						Log.e("device",device.getAddress());
						listener.RFstarBLEManageListener(device, rssi,
								scanRecord);
					}
				}
			});
		}
	};

	/**
	 * 每扫描到一个蓝牙设备调用一次
	 * 
	 * @param listener
	 */
	public void setRFstarBLEManagerListener(RFStarManageListener listener) {
		this.listener = listener;
	}


	private void initTimer() {
		if (mTimer == null) {
			mTimer = new CountDownTimer((long) (SCAN_TIME), 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
				}

				@Override
				public void onFinish() {
					listener.RFstarBLEManageStopScan();
				}
			};
		}
	}
	/**
	 * 用于处理，刷新到设备时更新界面
	 * 
	 * @author Kevin.wu
	 * 
	 */
	public interface RFStarManageListener {
		public void RFstarBLEManageListener(BluetoothDevice device, int rssi,
											byte[] scanRecord);

		public void RFstarBLEManageStartScan();

		public void RFstarBLEManageStopScan();
	}
}
