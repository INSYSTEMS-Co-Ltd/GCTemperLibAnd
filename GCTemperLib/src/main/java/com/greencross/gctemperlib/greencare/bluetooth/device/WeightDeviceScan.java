package com.greencross.gctemperlib.greencare.bluetooth.device;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WeightDeviceScan {
    private final String TAG = WeightDeviceScan.class.getSimpleName();

//    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<BluetoothDevice>();
    final int[] time = {0};
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 20000;
    private Handler handler = new Handler();
//    private ListView listview;
    private ImageView pairedimage;

    private WeightDeviceControl mWeightDeviceControl;

    private Context mContext;
    private TextView mValueTv;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public WeightDeviceScan(Context context, TextView valueTv) {
        mContext = context;
        mValueTv = valueTv;
        mHandler = new Handler();

        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "블루투스가 지원 되지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
//            Toast.makeText(this, R.string.error_bluetooth_not_supregistReceiverported, Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "블루투스가 지원 되지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        startBluetooth(context);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
//            finish();
            return;
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void stopScan() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    // Device scan callback.
    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mLeDeviceListAdapter.addDevice(device);
//                    mLeDeviceListAdapter.notifyDataSetChanged();
                    /*addDevice(device);
//                    Toast.makeText(getApplicationContext(), String.valueOf(mLeDevices.size()), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < mLeDevices.size(); i++) {
                        if (mLeDevices.get(i).getName() != null && !mLeDevices.get(i).getName().equals("") && mLeDevices.get(i).getName().equals("Chipsea-BLE")) {
                            selectDevice(i);
                            time[0] = -1;
                        }
                    }

                   */
                    //ssshin add 2018.11.07 체중계 이슈
                    if(device != null && device.getName() != null){
                        if(device.getName().equals("Chipsea-BLE")){
                            final Intent intent = new Intent();
                            intent.putExtra(WeightDeviceControl.EXTRAS_DEVICE_NAME, device.getName());
                            intent.putExtra(WeightDeviceControl.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                            if (mScanning) {
                                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                                mScanning = false;
                            }

                            mWeightDeviceControl = new WeightDeviceControl(mContext, intent, mValueTv);
                        }
                    }



                }
            });
        }
    };

    public void addDevice(BluetoothDevice device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void selectDevice(int i) {
//        if (isToast == false) {
//            isToast = true;
//            Toast.makeText(mContext, "체중계에 올라가주세요", Toast.LENGTH_SHORT).show();
//        }

        final BluetoothDevice device = mLeDevices.get(i);
        if (device == null) return;
//        final Intent intent = new Intent(mContext, WeightDeviceControl.class);
        final Intent intent = new Intent();
        intent.putExtra(WeightDeviceControl.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(WeightDeviceControl.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }

        mWeightDeviceControl = new WeightDeviceControl(mContext, intent, mValueTv);
//        startActivity(intent);
//        finish();
    }

    private void startBluetooth(Context context) {
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity)mContext).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        scanLeDevice(true);
    }

    public void onPause() {
        scanLeDevice(false);
        mLeDevices.clear();

        if (mWeightDeviceControl != null)
            mWeightDeviceControl.onPause();
    }

    public void onDestroy() {
        if (mWeightDeviceControl != null)
            mWeightDeviceControl.onDestroy();
    }



}
