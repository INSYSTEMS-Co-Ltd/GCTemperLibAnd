package com.greencross.gctemperlib.greencare.bluetooth.manager;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;

public class BluetoothManager {
    private final String TAG = BluetoothManager.class.getSimpleName();


    private BluetoothAdapter bluetoothAdapter;
    private boolean bluetoothOpenCheck = false;

    private Fragment mMainFragment;

//    public BluetoothManager(Fragment mainFragment) {
//        mMainFragment = mainFragment;
//        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//
//            CDialog.showDlg(mainFragment.getContext(), "해당 단말은 블루투스를 지원하지 않습니다.");
//            return;
//        }
//        Logger.i(TAG, "BluetoothManager start");
//
//        connectDevices();
//    }
//
//    public void onResume() {
//        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled() && !bluetoothOpenCheck) {
//            Toast.makeText(mMainFragment.getContext(), "해당 기능을 진행하기위해 블루투스를 켜주세요.", Toast.LENGTH_LONG).show();
//            openBluetoothSetting();
//            bluetoothOpenCheck = true;
//            return;
//        }
//        connectDevices();
//    }
//
//    public void onPause() {
//        disconnectDevices();
//    }
//
//    private void connectDevices() {
//        Logger.i(TAG, "Bluetooth.connectDevices()");
//        connectBloodDevice();       // 혈당계 연결 하기
////        connectPressureDevice();
////        connectWeightDevice();
//
////        // 블루투스 밴드 일때만 블루투스 켜기
////        int dataSource = SharedPref.getInstance().getPreferences(SharedPref.STEP_DATA_SOURCE_TYPE, Define.STEP_DATA_SOURCE_GOOGLE_FIT);
////        if (Define.STEP_DATA_SOURCE_BAND == dataSource) {
////            connectBandDevice();
////        }
//    }
//
//    private boolean isPairedDevice(BluetoothDevice device) {
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//        if (pairedDevices != null) {
//            for (BluetoothDevice pairedDevice : pairedDevices) {
//                if (pairedDevice.equals(device)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private void connectBloodDevice() {
//
//        if (mDeviceBlood != null) {
//            mDeviceBlood.disconnect();
//        }
//
//        if (DeviceManager.isRegDevice(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_BLOOD)) {
//            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DeviceManager.getRegDeviceAddress(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_BLOOD));
//
//            if (isPairedDevice(device)) {
//                mDeviceBlood = new BloodDevice(device);
//                mDeviceBlood.setOnBluetoothListener(onBloodDeviceListener);
//                mDeviceBlood.connect(mMainFragment.getContext());
//            } else {
//
////                String message = "해당 단말은 페어링 모드에서 페어링이 필요합니다.(설정창에서 페어링해주세요.)";
////                CDialog.showDlg(mMainFragment.getContext(), message, new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                        openBluetoothSetting();
////                    }
////                }, null);
//
//            }
//        } else {
//            Logger.e(TAG, "BlueTooth 혈당계가 등록되어 있지 않습니다.");
//        }
//    }
//
//    private void connectPressureDevice() {
//
//        if (mDevicePressure != null) {
//            mDevicePressure.disconnect();
//        }
//        if (DeviceManager.isRegDevice(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_PRESSURE)) {
//            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DeviceManager.getRegDeviceAddress(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_PRESSURE));
//            mDevicePressure = new PressureDevice(device);
//            mDevicePressure.setOnBluetoothListener(onPressureDeviceListener);
//            mDevicePressure.connect(mMainFragment.getContext());
//        } else {
//            Logger.e(TAG, "BlueTooth 혈압계가 등록되어 있지 않습니다.");
//        }
//
//    }
//
//    private void connectWeightDevice() {
//
//        if (mDeviceWeight != null) {
//            mDeviceWeight.disconnect();
//        }
//
//        if (DeviceManager.isRegDevice(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_WEIGHT)) {
//
//            /*
//            회원정보로 벤드업데이트-벤트초기화 되어 보류합니다. */
//            Tr_login info = Define.getInstance().getLoginInfo();
//            int mber_height = StringUtil.getIntVal(info.mber_height);
//            int mber_sex = StringUtil.getIntVal(info.mber_sex);
//            int rBirth      = StringUtil.getIntVal(info.mber_lifyea.substring(0, 4));                      // 회원 생년
//            String nowYear  = CDateUtil.getFormattedString_yyyy(System.currentTimeMillis());                // 현재 년도
//            int rAge        = (StringUtil.getIntVal(nowYear) - rBirth);                                     // 회원 나이
//            BaseDevice.Gender sex = mber_sex==1?WeightDevice.Gender.MAN:WeightDevice.Gender.WOMAN;
//
//            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DeviceManager.getRegDeviceAddress(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_WEIGHT));
//            mDeviceWeight = new WeightDevice(device);
//            mDeviceWeight.setOnBluetoothListener(onWeightDeviceListener);
//            mDeviceWeight.setUserInfo(rAge, mber_height, sex);
//            mDeviceWeight.connect(mMainFragment.getContext());
//        } else {
//            Logger.e(TAG, "BlueTooth 체중계가 등록되어 있지 않습니다.");
//        }
//    }
//
//    public void connectBandDevice() {
//        if (mDeviceBand != null) {
//            mDeviceBand.disconnect();
//        }
//
//
//        if (DeviceManager.isRegDevice(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_BAND)) {
//            /*
//            회원정보로 벤드업데이트-벤트초기화 되어 보류합니다.*/
//            Tr_login info = Define.getInstance().getLoginInfo();
//            int mber_height = StringUtil.getIntVal(info.mber_height);
//            int mber_sex = StringUtil.getIntVal(info.mber_sex);
//            int rBirth      = StringUtil.getIntVal(info.mber_lifyea.substring(0, 4));                      // 회원 생년
//            String nowYear  = CDateUtil.getFormattedString_yyyy(System.currentTimeMillis());                // 현재 년도
//            int rAge        = (StringUtil.getIntVal(nowYear) - rBirth);                                     // 회원 나이
//            BaseDevice.Gender sex = mber_sex==1?WeightDevice.Gender.MAN:WeightDevice.Gender.WOMAN;
//
//            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DeviceManager.getRegDeviceAddress(mMainFragment.getContext(), DeviceManager.FLAG_BLE_DEVICE_BAND));
//            mDeviceBand = new BandDevice(device);
//            mDeviceBand.setOnBluetoothListener(onStepBluetoothListener);
//            mDeviceBand.setUserInfo(rAge, mber_height, 65, sex);
//            mDeviceBand.connect(mMainFragment.getContext());
//
//        } else {
//            Logger.e(TAG, "BlueTooth 활동량계가 등록되어 있지 않습니다.");
//        }
//    }
//
//
//    private void disconnectDevices() {
//        if (mDeviceBlood != null) {
//            mDeviceBlood.disconnect();
//            mDeviceBlood = null;
//        }
//        if (mDevicePressure != null) {
//            mDevicePressure.disconnect();
//            mDevicePressure = null;
//        }
//        if (mDeviceWeight != null) {
//            mDeviceWeight.disconnect();
//            mDeviceWeight = null;
//        }
//    }
//
//
//    private BaseDevice.OnBluetoothListener<SparseArray<BloodModel>> onBloodDeviceListener = new BaseDevice.OnBluetoothListener<SparseArray<BloodModel>>() {
//        @Override
//        public void onStart() {
//            Logger.i(TAG, "BlueTooth onBloodDeviceListener onStart()");
//        }
//
//        @Override
//        public void onConnected() {
//            Logger.i(TAG, "BlueTooth onBloodDeviceListener onConnected()");
//
//            mDeviceBlood.readData();
//        }
//
//        @Override
//        public void onDisConnected() {
//            Logger.i(TAG, "BlueTooth onBloodDeviceListener onDisConnected()");
//        }
//
//        @Override
//        public void onReceivedData(SparseArray<BloodModel> dataModel) {
//            GLog.d(TAG, "BlueTooth Read Blood: size="+dataModel.size() +", " + dataModel.toString());
//            if (dataModel.size() > 0) {
//                BloodModel data = dataModel.get(dataModel.keyAt(dataModel.size() - 1));
//                data.setRegtype(BloodModel.INPUT_TYPE_DEVICE);
//                data.setRegTime(CDateUtil.getForamtyyyyMMddHHmmss(data.getTime()));
//
//
//                data.setBefore(""+data.getEatType());
//                Logger.i(TAG, "onReceivedData getEatType="+data.getEatType());
//                Logger.i(TAG, "onReceivedData getTime="+data.getTime());
//                Logger.i(TAG, "onReceivedData getIdx="+data.getIdx());
//                Logger.i(TAG, "onReceivedData getSequenceNumber="+data.getSequenceNumber());
//                Logger.i(TAG, "onReceivedData getSugar="+data.getSugar());
//                Logger.i(TAG, "onReceivedData getRegTime="+data.getRegTime());
//
//                //건강메시지 전달
//                new DeviceDataUtil().uploadSugarData(mMainFragment, dataModel, new IBluetoothResult() {
//                    @Override
//                    public void onResult(boolean isSuccess) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mMainFragment.notifyAdapter();
//                            }
//                        }, 500);
//                    }
//                });
//            }
//        }
//    };
//
//
//    /**
//     * 혈압 등록 리스너
//     */
//    private BaseDevice.OnBluetoothListener<PressureModel> onPressureDeviceListener = new BaseDevice.OnBluetoothListener<PressureModel>() {
//        @Override
//        public void onStart() {
//            Logger.i(TAG, "BlueTooth onPressureDeviceListener onStart()");
//        }
//
//        @Override
//        public void onConnected() {
//            Logger.i(TAG, "BlueTooth onPressureDeviceListener onConnected()");
//
//            mDevicePressure.readData();
//        }
//
//        @Override
//        public void onDisConnected() {
//            Logger.i(TAG, "BlueTooth onPressureDeviceListener onDisConnected()");
//        }
//
//        @Override
//        public void onReceivedData(PressureModel dataModel) {
//            GLog.d(TAG, "BlueTooth Read Pressure:" + dataModel.toString());
//
//            dataModel.setRegtype("D");
//            dataModel.setIdx(CDateUtil.getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
//            dataModel.setRegdate(CDateUtil.getForamtyyyyMMddHHmmss(dataModel.getTime()));
//
//
//            //건강메시지 전달
//            new DeviceDataUtil().uploadPresure(mMainFragment, dataModel, new IBluetoothResult() {
//                @Override
//                public void onResult(boolean isSuccess) {
//                    mMainFragment.notifyAdapter();
//                }
//            });
//        }
//    };
//
//    /**
//     * 체중계 리스너
//     */
//    private BaseDevice.OnBluetoothListener<WeightModel> onWeightDeviceListener = new BaseDevice.OnBluetoothListener<WeightModel>() {
//        @Override
//        public void onStart() {
//            Logger.i(TAG, "BlueTooth onWeightDeviceListener onStart()");
//        }
//
//        @Override
//        public void onConnected() {
//            Logger.i(TAG, "BlueTooth onWeightDeviceListener onConnected()");
//            mDeviceWeight.readData();
//        }
//
//        @Override
//        public void onDisConnected() {
//            Logger.i(TAG, "BlueTooth onWeightDeviceListener onDisConnected()");
//        }
//
//        @Override
//        public void onReceivedData(final WeightModel dataModel) {
//            GLog.d(TAG, "BlueTooth Read Weight:" + dataModel.toString());
//            dataModel.setRegType("D");
//            dataModel.setIdx(CDateUtil.getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
//            dataModel.setRegDate(CDateUtil.getForamtyyyyMMddHHmmss(new Date(System.currentTimeMillis())));
//
//            new DeviceDataUtil().uploadWeight(mMainFragment, dataModel, new IBluetoothResult() {
//                @Override
//                public void onResult(boolean isSuccess) {
//                    CommonData login = CommonData.getInstance();
//
//                    DBHelper helper = new DBHelper(mMainFragment.getContext());
//                    DBHelperWeight weightDb = helper.getWeightDb();
//                    DBHelperWeight.WeightStaticData bottomData = weightDb.getResultStatic();
//                    if(!bottomData.getWeight().isEmpty()) {
//                        login.mber_bdwgh_app = Float.toString(dataModel.getWeight());
//                    }
//
//                    Define.getInstance().setLoginInfo(login);
//
//                    mMainFragment.notifyAdapter();
//                }
//            });
//        }
//    };
//
//    /**
//     * 활동량계
//     [ {
//         calories: 23,
//         distance: 0.39,
//         heartRate: 0,
//         latestTimeMs: 0,
//         step: 549,
//         stress: 0,
//         time: 11,
//         serialVersionUID: 7579522124476046343,
//         serialVersionUID: 7888357950147579467,
//         shadow$_klass_: class com.greencross.hanwha.diabetes.bluetooth.model.BandModel,
//         shadow$_monitor_: 0
//     }]
//
//     * */
//    private BandDevice.OnBluetoothListener<BandModel> onStepBluetoothListener = new BandDevice.OnBluetoothListener<BandModel>() {
//        @Override
//        public void onStart() {
//        }
//
//        @Override
//        public void onConnected() {
//            //시간당 걸음데이터 가져오기
//            mDeviceBand.readTimeData(); //1.
//            //시간당 심박데이터 가져오기
//            mDeviceBand.readTimePPGData();//2.
//        }
//
//        @Override
//        public void onDisConnected() {
//
//        }
//
//        @Override
//        public void onReceivedData(BandModel dataModel) {
//            GLog.d(TAG, "Read Band:" + dataModel.toString());
//
//            dataModel.setRegtype("D");
//            dataModel.setIdx(CDateUtil.getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
//            dataModel.setRegDate(CDateUtil.getForamtyyyyMMddHHmmss(new Date(System.currentTimeMillis())));
//
//            new DeviceDataUtil().uploadStepRealTimeData(mMainFragment, dataModel, new IBluetoothResult() {
//                @Override
//                public void onResult(boolean isSuccess) {
//                    Logger.i(TAG, "Bluetooth uploadStepRealTimeData:"+isSuccess);
//                    mMainFragment.notifyAdapter();
//                }
//            });
//        }
//
//        @Override
//        public void onReceivedPPGData(List<BandModel> dataModel) {
//            if (dataModel != null) {
//                GLog.d(TAG, "Read PPG.size="+dataModel.size() +":" + dataModel.toString());
//
//                new DeviceDataUtil().uploadPPGData(mMainFragment, dataModel, new IBluetoothResult() {
//                    @Override
//                    public void onResult(boolean isSuccess) {
//                        Logger.i(TAG, "Bluetooth uploadPPGData:"+isSuccess);
////                        mMainFragment.notifyAdapter();  // 호출이필요할까?
//                    }
//                });
//
//            } else {
//                Logger.e(TAG, "Read PPG:데이터가 없습니다.");
//            }
//
//        }
//
//        @Override
//        public void onReceivedTimeData(List<BandModel> dataModel) {
//            if (dataModel != null) {
//                GLog.d(TAG, "Read Band.size="+dataModel.size() +":" + dataModel.toString());
//
//                new DeviceDataUtil().uploadStepData(mMainFragment, dataModel, new IBluetoothResult() {
//                    @Override
//                    public void onResult(boolean isSuccess) {
//                        Logger.i(TAG, "Bluetooth uploadStepData:"+isSuccess);
//                        mMainFragment.notifyAdapter();
//                    }
//                });
//
//            } else {
//                Logger.e(TAG, "Read Band:데이터가 없습니다.");
//            }
//
//        }
//        @Override
//        public void onHeartRateMeasured(int heartRate) {
//            Logger.i(TAG, "BlueTooth.heartRate="+heartRate);
//        }
//
//        @Override
//        public void onAltitudeMeasured(int altitude) {
//            Logger.i(TAG, "BlueTooth.altitude="+altitude +" 미터");
//
//        }
//    };
//
//    protected void openBluetoothSetting() {
//        Intent intentOpenBluetoothSettings = new Intent();
//        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
//        mMainFragment.startActivity(intentOpenBluetoothSettings);
//    }

    public interface IBluetoothResult {
        void onResult(boolean isSuccess);
    }
}
