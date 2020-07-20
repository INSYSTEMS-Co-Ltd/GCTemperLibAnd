/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greencross.gctemperlib.greencare.bluetooth.device;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class WeightDeviceControl {
    private final static String TAG = WeightDeviceControl.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static Activity devicecontrolactivity;
    String action;

    Handler handle = new Handler();

    int num = 0;

    static String weightval = "0";

    private TextView mDataField = null;
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    String tall = "0";

    int bt_clicktime = 0;
    int click_false = 0;
    int click_true = 1;

    private Context mContext;

    private boolean isToast = false;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WeightDeviceControl(Context context, Intent intent, TextView textView) {
        mContext = context;
        mDataField = textView;
        registReceiver();


        PackageInfo pi = null;

        try {
            pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    handle.post(new Runnable() {

                        @Override
                        public void run() {
                            mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
                            mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
                            Intent a = new Intent();
                            Log.e("!!", mDeviceAddress);

                            mGattUpdateReceiver.onReceive(context, intent);
                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        t.start();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.disconnected);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                mConnected = true;
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                onChildClick(7, 0);
                Logger.i(TAG, "mGattUpdateReceiver.ACTION_GATT_SERVICES_DISCOVERED");
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                mConnected = true;
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                onChildClick(7, 0);
                Logger.i(TAG, "mGattUpdateReceiver.ACTION_DATA_AVAILABLE");
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

                bt_clicktime = click_true;
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean onChildClick(int groupPosition,
                                int childPosition) {
        if (mGattCharacteristics != null) {

            final BluetoothGattCharacteristic characteristic =
                    mGattCharacteristics.get(groupPosition).get(childPosition);

            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
            }
            return true;
        }
        return false;
    }

    private void updateConnectionState(final int resourceId) {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mConnectionState.setText(resourceId);
                Logger.i(TAG, "updateConnectionState.resourceId="+resourceId);
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {

            data = String.format("%.2f", StringUtil.getFloat(data));
            mDataField.setText(data);
            mDataField.setTag("D"); // 디바이스
            String bmitext = String.valueOf(Math.round((Float.parseFloat(data) / (((Float.parseFloat(tall)) * (Float.parseFloat(tall))) / 10000)) * 10d) / 10d);

            if (Float.parseFloat(bmitext) <= 18.5) {
                Logger.i(TAG, "bmipurpose:::저체중");
            }
            if (Float.parseFloat(bmitext) > 18.5 && Float.parseFloat(bmitext) < 25) {
                Logger.i(TAG, "bmipurpose:::표준");

            }
            if (Float.parseFloat(bmitext) >= 25 && Float.parseFloat(bmitext) < 30) {
                Logger.i(TAG, "bmipurpose:::과체중");
            }
            if (Float.parseFloat(bmitext) >= 30 && Float.parseFloat(bmitext) < 40) {
                Logger.i(TAG, "bmipurpose:::비만");
            }
            if (Float.parseFloat(bmitext) >= 40) {
                Logger.i(TAG, "bmipurpose:::고비만");
            }
        }
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = "unknown_service";//getResources().getString(R.string.unknown_service);
        String unknownCharaString = "unknown_characteristic";//getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, WeightGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, WeightGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    public void registReceiver() {
        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    protected void onPause() {
        if (mGattUpdateReceiver != null )
            try {
                mContext.unregisterReceiver(mGattUpdateReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    protected void onDestroy() {
        mContext.unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }
}
