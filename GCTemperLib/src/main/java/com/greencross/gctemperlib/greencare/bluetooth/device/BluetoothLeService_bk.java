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
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
@SuppressLint("NewApi") public class BluetoothLeService_bk extends Service {
    private final static String TAG = BluetoothLeService_bk.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String byte_string46 = "byte_string46";

    public final static String test = "test";

    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(WeightGattAttributes.HEART_RATE_MEASUREMENT);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    @SuppressLint("NewApi") private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    @SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) private void broadcastUpdate(final String action,
                                                                                                        final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }

            String mValue = "";
            try {
            	StringBuilder sb = new StringBuilder();
            	StringBuilder sb2 = new StringBuilder();

            	// ��ü ������ (0~19) (��/�ҹ��� ������ ���� Ȯ���� ��)
            	String str = getHexString(characteristic.getValue()).toUpperCase();

                sb.append(str.substring(6, 10));
                sb2.append(str.substring(4, 6));
                float mValue1 = (float)getHexToDec(sb.toString());
                float mValue2 = (float)getHexToDec(sb2.toString());
                //Intent dataintent = new Intent(getApplicationContext(), result.class);

                NumberFormat numberformat = new DecimalFormat("###,###.##");
                // UI�󿡼� ������ ��
                String strVal = numberformat.format(mValue1/100.0D);
                String test = "test";
                intent.putExtra(EXTRA_DATA, strVal);
                intent.putExtra(byte_string46, str.substring(4, 6));
                intent.putExtra("test", test);
                //dataintent.putExtra(EXTRA_DATA, strVal);
                Log.e("","!!!");
                Log.e("", strVal + " Kg");

            	// ������ ���Ž� (Lock)
//            	if("09".equals(str.substring(4, 6))){
//            		sb.append(str.substring(6, 10));
//            		sb2.append(str.substring(4, 6));
//                	float mValue1 = (float)getHexToDec(sb.toString());
//                	float mValue2 = (float)getHexToDec(sb2.toString());
//                	//Intent dataintent = new Intent(getApplicationContext(), result.class);
//
//                	NumberFormat numberformat = new DecimalFormat("###,###.##");
//                	// UI�󿡼� ������ ��
//                	String strVal = numberformat.format(mValue1/100.0D);
//                	String test = "test";
//            		intent.putExtra(EXTRA_DATA, strVal);
//            		intent.putExtra(byte_string46, str.substring(4, 6));
//            		intent.putExtra("test", test);
//            		//dataintent.putExtra(EXTRA_DATA, strVal);
//            		LogUtil.e("!!!");
//            		LogUtil.e(strVal + " Kg");
//            	}
//            	else {
//            		sb.append(str.substring(6, 10));
//            		float mValue1 = (float)getHexToDec(sb.toString());
//
//            		NumberFormat numberformat = new DecimalFormat("###,###.##");
//            		// UI�󿡼� ������ ��
//            		String strVal = numberformat.format(mValue1/100.0D);
//
//            		//intent.putExtra(EXTRA_DATA, strVal);
//            		LogUtil.e("???");
//            		LogUtil.e(strVal + " Kg");
//            	}



//            	int val = hexToTen(String.format("%02X", characteristic.getValue()[3]) + hexToTen(String.format("%02X", characteristic.getValue()[4])));
//            	LogUtil.i("" + val);
//            	LogUtil.i("" + val/100.D );
//            	for(int i=0; i<characteristic.getValue().length; i++){
//            		if(i == 3 || i == 4)
//
////            		// 3.75Kg  01 77
////            		LogUtil.i(String.format("%02X",Byte.valueOf(characteristic.getValue()[i])));
//
//            		LogUtil.i("" + dec(String.format("%02X", characteristic.getValue()[i])));
//
////            		if(i == 3 || i == 4 || i == 19)
////            		LogUtil.i("["+i+"] => " + getHexString((byte)characteristic.getValue()[i]));
////            		LogUtil.i("[" + i + "]" + String.format("%02X", 111111characteristic.getValue()[i]));
//
//
//            	}

//            	LogUtil.e("[4]" + Byte.toString(characteristic.getValue()[4]));
            } catch (Exception e) {e.printStackTrace();}
            //
            final int heartRate = characteristic.getIntValue(format, 1);

//            StringBuilder sb = new StringBuilder();
//            for(int i=0; i<characteristic.getValue().length; i++){
//            	Object[] arrayOfObject3 = new Object[1];
//            	arrayOfObject3[0] = Byte.valueOf(characteristic.getValue()[i]);
//            	sb.append(String.format("%02X", arrayOfObject3));
////        		Byte.valueOf(characteristic.getValue()[i]);
////        		LogUtil.i(String.format("%02X", characteristic.getValue()[i]));
//        	}


//            LogUtil.i("" + Integer.parseInt(String.format("%02X", characteristic.getValue()[3])));
//            LogUtil.i("" + Integer.parseInt(String.format("%02X", characteristic.getValue()[4])));

 //           GLog.d(TAG, String.format("Received heart rate: %d", heartRate));
 //           intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));

        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    public static float changeOnePoint(float paramFloat, int paramInt)
    {
      return new BigDecimal(paramFloat).setScale(paramInt, 4).floatValue();
    }

    public static int dec(String hex){
		 String[] temp = hex.split("0x");
		 StringBuffer dec = null;
		 for(String strArr : temp){
			 dec = new StringBuffer();
			 dec.append(strArr);
		 }
		 return Integer.parseInt(dec.toString(), 16);
	 }

    public static int hexToTen(String paramString)
    {
      if ((paramString == null) || ((paramString != null) && ("".equals(paramString)))) {}
      for (int i = 0;; i = Integer.valueOf(paramString, 16).intValue()) {
        return i;
      }
    }

    private float getHexToDec(String hex) {
    	   long v = Long.parseLong(hex, 16);
    	   return Float.parseFloat(String.valueOf(v));
    	 }

	public String byteToBinaryString(byte n) {
	    StringBuilder sb = new StringBuilder("00000000");
	    for (int bit = 0; bit < 8; bit++) {
	        if (((n >> bit) & 1) > 0) {
	            sb.setCharAt(7 - bit, '1');
	        }
	    }
	    return sb.toString();
	}

    private String getHexString(byte b) {
		try {
			return Integer.toString( ( b & 0xff ) + 0x100, 16).substring( 1 );
		} catch (Exception e) {
            return null;
        }
	}

    private String getHexString(byte[] b) {
		String result = "";
		try {
			for (int i=0; i < b.length; i++) {
				result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
			}
			return result;
		} catch (Exception e) {	return null; }
	}

    public class LocalBinder extends Binder {
        public BluetoothLeService_bk getService() {
            return BluetoothLeService_bk.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(andr
     * 0.0oid.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                                                                             boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(WeightGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     *
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;
        return mBluetoothGatt.getServices();
    }
}