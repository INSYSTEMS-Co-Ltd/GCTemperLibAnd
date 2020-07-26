package com.greencross.gctemperlib;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.network.tr.HNApiData;
import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.CConnAsyncTask;
import com.greencross.gctemperlib.greencare.network.tr.HNCConnAsyncTask;
import com.greencross.gctemperlib.greencare.network.tr.hnData.Tr_Setup;
import com.greencross.gctemperlib.greencare.network.tr.hnData.Tr_Temperature;
import com.greencross.gctemperlib.greencare.network.tr.hnData.Tr_login;
import com.greencross.gctemperlib.greencare.util.NetworkUtil;
import com.greencross.gctemperlib.greencare.util.SharedPref;


public class GCTemperLib {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;


    private final String APP_TOKEN = "APA91bGkmKwWBjCso94R3sM3CUEk79";  // 앱 토큰

    public GCTemperLib(Context context) {
        mContext = context;
    }

    /**
     * 녹십자 체온관리를 사용하기 위한  인증토큰을 등록
     *
     * @param gcToken GC 체온관리 라이브러리를 사용하기 위한 토큰
     */
    public boolean registGCToken(@Nullable String gcToken) {
        // 최초 인증
        // 인증토큰, 푸시토큰, 고객번호, 성별을 저장
        SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_APP_TOKEN, gcToken);
        return APP_TOKEN.equals(gcToken);
    }

    /**
     * 녹십자 체온관리 인증토큰 인증 여부 확인
     */
    public boolean isAvailableGCToken() {
        // 최초 인증
        String gcToken = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_APP_TOKEN);
        return APP_TOKEN.equals(gcToken);
    }

    /**
     * 등록여부 체크
     *
     * @param IGCResult 결과값 전달 Interface
     * @return
     */
    private boolean checkGCToken(IGCResult IGCResult) {
        if (isAvailableGCToken())
            return true;
        else {
            if (IGCResult != null) {
                IGCResult.onResult(false, "인증 후 이용 가능합니다.", null);
            }
        }
        return false;
    }

    /**
     * 고객번호 저장
     *
     * @param customerNo 고객번호
     * @param iGCResult  결과값 전달 Interface
     */
    public void registCustomerNo(@Nullable String customerNo, IGCResult iGCResult) {
        if (checkGCToken(iGCResult) == false) {
            return;
        } else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    if (TextUtils.isEmpty(customerNo)) {
//                        iGCResult.onResult(false, "고객 번호를 입력해주세요.", null);
//                    } else {
//                        iGCResult.onResult(true, "고객 번호 등록 완료", null);
//                    }
//                }
//            }, 500);

            if (TextUtils.isEmpty(customerNo)) {
                iGCResult.onResult(false, "고객 번호를 입력해주세요.", null);
                return;
            }

            // 최초 인증, 고객번호 저장
            Tr_login.RequestData requestData = new Tr_login.RequestData();
            requestData.cust_id = customerNo;
            getData(Tr_login.class, requestData, new IGCResult() {
                @Override
                public void onResult(boolean isSuccess, String message, Object data) {
                    if (data instanceof Tr_login) {
                        Tr_login recv = (Tr_login) data;
                        if (recv.status.equals("Y")) {
                            iGCResult.onResult(true, "고객 번호 등록 완료", null);
                            SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_CUST_NO, customerNo);     // 사용자 번호
                        } else {
                            iGCResult.onResult(false, "고객 번호 등록 실패 하였습니다.", null);
                        }
                    } else {
                        iGCResult.onResult(isSuccess, message, data);
                    }
                }
            });
        }
    }


    /**
     * Push 토큰을 저장
     *
     * @param pushToken 푸시 토큰
     * @param iGCResult 결과값 전달 Interface
     */
    public void registPushToken(@Nullable String pushToken, final IGCResult iGCResult) {
        if (checkGCToken(iGCResult) == false) {
            return;
        } else {
            if (TextUtils.isEmpty(pushToken)) {
                iGCResult.onResult(false, "푸시 토큰을 입력해주세요.", null);
                return;
            }

            Tr_login.RequestData requestData = new Tr_login.RequestData();
            requestData.cust_id = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_CUST_NO);
            ;
            requestData.devicetoken = pushToken;
            getData(Tr_login.class, requestData, new IGCResult() {
                @Override
                public void onResult(boolean isSuccess, String message, Object data) {
                    if (data instanceof Tr_login) {
                        Tr_login recv = (Tr_login) data;
                        if (recv.status.equals("Y")) {
                            iGCResult.onResult(true, "푸시 토큰 등록 완료", null);
                            SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_PUSH_TOKEN, pushToken);   // 푸시키
                        } else {
                            iGCResult.onResult(false, "푸시 토큰 등록 실패", null);
                        }
                    } else {
                        iGCResult.onResult(isSuccess, message, data);
                    }
                }
            });

        }
    }


    /**
     * 체온값 저장
     *
     * @param temper    체온
     * @param iGCResult 결과값 전달 Interface
     */
    public void registGCTemper(@Nullable String temper, final IGCResult iGCResult) {
        if (checkGCToken(iGCResult) == false) {
            return;
        } else {
            if (TextUtils.isEmpty(temper)) {
                iGCResult.onResult(false, "체온을 입력해 주세요.", null);
            }

            Tr_Temperature.RequestData requestData = new Tr_Temperature.RequestData();
            requestData.fever = temper;
            getData(Tr_Temperature.class, requestData, new IGCResult() {
                @Override
                public void onResult(boolean isSuccess, String message, Object data) {
                    if (data instanceof Tr_Temperature) {
                        Tr_Temperature recv = (Tr_Temperature) data;
                        if (recv.status.equals("Y")) {
                            iGCResult.onResult(true, "체온 등록 완료", null);
                        } else {
                            iGCResult.onResult(false, "체온 등록 실패", null);
                        }
                    } else {
                        iGCResult.onResult(false, "데이터 수신 실패", null);
                    }
                }
            });
        }
    }


    /**
     * 알림 수신여부
     *
     * @param alramType 알람 타입
     * @param isEnable  알람 수신 여부
     */
    public void settingAlramService(GCAlramType alramType, boolean isEnable, IGCResult iGCResult) {
        String pushToken = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_PUSH_TOKEN);    // 푸시키
        String custNo = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_CUST_NO);          // 사용자 번호

        Log.i(TAG, "setAlram::" + alramType.name() + " " + isEnable);
        Log.i(TAG, "setAlram.pushToken=" + pushToken);
        Log.i(TAG, "setAlram.custNo=" + custNo);

        if (checkGCToken(iGCResult) == false) {
            return;
        } else if (TextUtils.isEmpty(pushToken)) {
            iGCResult.onResult(false, "PUSH 토큰 등록 후 이용 가능합니다.", null);
            return;
        } else if (TextUtils.isEmpty(custNo)) {
            iGCResult.onResult(false, "사용자 정보 등록 후 이용 가능합니다.", null);
            return;
        } else {

            Tr_Setup.RequestData requestData = new Tr_Setup.RequestData();
            if (alramType == GCAlramType.GC_ALRAM_TYPE_독려) {
                requestData.ncrgd_yn = isEnable ? "Y" : "N";
            } else if (alramType == GCAlramType.GC_ALRAM_TYPE_지역) {
                requestData.area_thmt_yn = isEnable ? "Y" : "N";
            }

            getData(Tr_Setup.class, requestData, new IGCResult() {
                @Override
                public void onResult(boolean isSuccess, String message, Object data) {
                    if (data instanceof Tr_Setup) {
                        Tr_Setup recv = (Tr_Setup) data;
                        if (recv.status.equals("Y")) {
                            iGCResult.onResult(true, String.format(alramType.getDesc() + " 알람 %1$s 완료", isEnable ? "등록" : "해지"), null);
                            SharedPref.getInstance(mContext).savePreferences(alramType.getAlramName(), isEnable);
                        } else {
                            iGCResult.onResult(true, String.format(alramType.getDesc() + " 알람 %1$s 완료", isEnable ? "등록" : "해지"), null);
                        }
                    } else {
                        iGCResult.onResult(isSuccess, message, data);
                    }
                }
            });

        }
    }

    /**
     * 설정된 알람 확인
     *
     * @param alramType
     * @return
     */
    public boolean getSettingAlramService(GCAlramType alramType) {
        return SharedPref.getInstance(mContext).getPreferences(alramType.getAlramName(), false);
    }

    /**
     * GC 체온관리 실행
     * 위치권한 등록이 안된 경우 실행 불가.
     */
    public void startGCMainActivity() {
        int permissionState = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            if (validCheck()) {
                mContext.startActivity(new Intent(mContext, TemperActivity.class));
//                DummyActivity.startActivity(((Activity)mContext), TemperFragment.newInstance(mContext), null);
//                DummyActivity.startActivity(((Activity)mContext), SearchAddressFragment.newInstance(mContext), null);
            }
        } else {
            CDialog.showDlg(mContext, mContext.getString(R.string.toast_permission_location));
        }
    }


    /**
     * 테스트용 기능
     * 인증토큰, Push토큰, 고객번호 데이터를 초기화 한다.
     */
    public void resetGCData() {
        SharedPref.getInstance(mContext).removeAllPreferences();
    }


    private void getData(Class<? extends BaseData> cls, final Object obj, final IGCResult iGCResult) {
        if (NetworkUtil.getConnectivityStatus(mContext) == false) {
            CDialog.showDlg(this.mContext, "네트워크 연결 상태를 확인해주세요.");
            return;
        }

        HNCConnAsyncTask.CConnectorListener queryListener = new HNCConnAsyncTask.CConnectorListener() {
            @Override
            public Object run() throws Exception {
                HNApiData data = new HNApiData();
                try {
                    Object recv = data.getData(mContext, cls, obj);
                    return recv;
                } catch (Exception e) {
                    e.printStackTrace();
                   return null;
                }
            }

            @Override
            public void view(HNCConnAsyncTask.CQueryResult result) {
                Log.i(TAG, "result1="+result);
                if (result.result == CConnAsyncTask.CQueryResult.SUCCESS && result.data != null) {
                    Log.e(TAG, "데이터 수신 성공::"+result);
                    if (iGCResult != null) {
                        iGCResult.onResult(true, "데이터 수신 성공", result.data);
                    }
                } else {
                    Log.e(TAG, "데이터 수신 실패");
                    if (iGCResult != null) {
                        iGCResult.onResult(false, "데이터 수신 실패", null);
                    }
                }
            }
        };

        HNCConnAsyncTask asyncTask = new HNCConnAsyncTask();
        asyncTask.execute(queryListener);
    }

    /**
     * 인증체크
     *
     * @return
     */
    private boolean validCheck() {
        String pushToken = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_PUSH_TOKEN);    // 푸시키
        String custNo = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_CUST_NO);          // 사용자 번호
//        String gender = SharedPref.getInstance(mContext).getPreferences(PREF_GENDER);           // 사용자 성별

        if (TextUtils.isEmpty(pushToken)) {
            CDialog.showDlg(mContext, "PUSH 토큰 등록 후 이용 가능합니다.");
            return false;
        } else if (TextUtils.isEmpty(custNo)) {
            CDialog.showDlg(mContext, "사용자 정보 등록 후 이용 가능합니다.");
            return false;
        }

        return true;
    }


}
