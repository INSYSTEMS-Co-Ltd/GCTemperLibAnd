package com.greencross.gctemperlib;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.greencross.gctemperlib.hana.component.CDialog;
import com.greencross.gctemperlib.hana.network.tr.BaseUrl;
import com.greencross.gctemperlib.hana.network.tr.HNApiData;
import com.greencross.gctemperlib.hana.network.tr.BaseData;
import com.greencross.gctemperlib.hana.network.tr.CConnAsyncTask;
import com.greencross.gctemperlib.hana.network.tr.HNCConnAsyncTask;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Setup;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Temperature;
import com.greencross.gctemperlib.util.NetworkUtil;
import com.greencross.gctemperlib.util.SharedPref;
import com.greencross.gctemperlib.hana.GCAlramType;
import com.greencross.gctemperlib.util.GpsInfo;
import com.greencross.gctemperlib.util.Util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class GCTemperLib {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private final String APP_TOKEN = "APA91bGkmKwWBjCso94R3sM3CUEk79";  // 앱 토큰
    public static int ALRAM_REPEAT_1HOUR = 202020;

    public GCTemperLib(Context context) {
        mContext = context;
        if (TextUtils.isEmpty(BaseUrl.COMMON_URL)) {
            boolean isDebug = SharedPref.getInstance(mContext).getPreferences(SharedPref.IS_DBUG, false);
            BaseUrl.setCommonUrl(isDebug);
        }
    }

    /**
     * 디버그모드여부 설정
     * 요청 URL 처리를 다르게 한다.
     * @return
     */
    public boolean getDebugMode() {
        boolean isDebug = SharedPref.getInstance(mContext).getPreferences(SharedPref.IS_DBUG, false);
        return isDebug;
    }

    public void setDebugMode(boolean isDebug) {
        SharedPref.getInstance(mContext).savePreferences(SharedPref.IS_DBUG, isDebug);
        BaseUrl.setCommonUrl(isDebug);
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
            if (TextUtils.isEmpty(customerNo)) {
                iGCResult.onResult(false, "고객 번호를 입력해주세요.", null);
                return;
            }

            SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_CUST_NO, customerNo);     // 사용자 번호 저장
            iGCResult.onResult(true, "고객 번호 등록 완료", null);

//            // 최초 인증, 고객번호 저장
//            Tr_Login.RequestData requestData = new Tr_Login.RequestData();
//            requestData.cust_id = customerNo;
//            getData(Tr_Login.class, requestData, new IGCResult() {
//                @Override
//                public void onResult(boolean isSuccess, String message, Object data) {
//                    if (data instanceof Tr_Login) {
//                        Tr_Login recv = (Tr_Login) data;
//                        boolean isLogin = recv.isSuccess(recv.resultcode);
//                        if (isLogin) {
//                            SharedPref.getInstance(mContext).saveLoginInfo(recv);
//                            SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_CUST_NO, customerNo);     // 사용자 번호 저장
//                            iGCResult.onResult(isLogin, recv.message, recv);
//                        } else {
//                            iGCResult.onResult(false, recv.message, recv);
//                        }
//                    } else {
//                        iGCResult.onResult(isSuccess, message, data);
//                    }
//                }
//            });
        }
    }


    /**
     * 고객번호 저장
     *
     * @param customerNo 고객번호
     * @param iGCResult  결과값 전달 Interface
     */
    public void registEncryptCustomerNo(@Nullable String customerNo, IGCResult iGCResult) {
        if (checkGCToken(iGCResult) == false) {
            return;
        } else {
            if (TextUtils.isEmpty(customerNo)) {
                iGCResult.onResult(false, "고객 번호를 입력해주세요.", null);
                return;
            }

            SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_CUST_ENCRYPT_NO, customerNo);     // 사용자 번호 저장
            iGCResult.onResult(true, "고객 번호 등록 완료", null);
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

            SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_PUSH_TOKEN, pushToken);   // 푸시키
            iGCResult.onResult(true, "푸시토큰 등록 완료", null);

//            Tr_Login.RequestData requestData = new Tr_Login.RequestData();
//            requestData.cust_id = custId;
//            requestData.devicetoken = pushToken;
//            getData(Tr_Login.class, requestData, new IGCResult() {
//                @Override
//                public void onResult(boolean isSuccess, String message, Object data) {
//                    if (data instanceof Tr_Login) {
//                        Tr_Login recv = (Tr_Login) data;
//                        boolean isSuccessed = recv.isSuccess(recv.resultcode);
//                        if (isSuccessed) {
//                            if (isSuccess) {
//                                SharedPref.getInstance(mContext).saveLoginInfo(recv);
//                                SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_PUSH_TOKEN, pushToken);   // 푸시키
//                                iGCResult.onResult(isSuccessed, recv.message, recv);
//                            } else {
//                                iGCResult.onResult(false, recv.message, recv);
//                            }
//                        } else {
//                            iGCResult.onResult(false, "푸시 토큰 등록 실패", null);
//                        }
//                    } else {
//                        iGCResult.onResult(isSuccess, message, data);
//                    }
//                }
//            });
        }
    }


    /**
     * 체온값 서버 전송
     *
     * @param temper    체온
     * @param iGCResult 결과값 전달 Interface
     */
    public void registGCTemperServer(@Nullable String temper, final IGCResult iGCResult) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String registTime = sdf.format(new Date());
        registGCTemperServer(temper, registTime, true, iGCResult);
    }

    public void registGCTemperServer(@Nullable String temper, String registTime, boolean isWearable, final IGCResult iGCResult) {
        if (checkGCToken(iGCResult) == false) {
            return;
        } else {
            int permissionState = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionState == PackageManager.PERMISSION_GRANTED) {
                GpsInfo gps = new GpsInfo(mContext);
                if (gps.isGetLocation()) {
                    // 위치권한과 GPS 설정이 된 경우 위치정보를 얻은 후, 전문 전송
                    registerLocationUpdates(temper, registTime, isWearable, iGCResult);
                    return;
                }
            }

            // 위치 설정이 되지 않은경우 바로 등록
            Tr_Temperature.RequestData requestData = new Tr_Temperature.RequestData();
            requestData.fever = temper;
            requestData.area = "";
            requestData.est1 = "";
            requestData.est2 = "";
            requestData.la = "";
            requestData.lo = "";
            requestData.Input_de = registTime;
            requestData.is_wearable = isWearable ? "1" : "0";
            registTemperAPI(requestData, iGCResult);
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

//        Log.i(TAG, "setAlram::" + alramType.name() + " " + isEnable);
//        Log.i(TAG, "setAlram.pushToken=" + pushToken);
//        Log.i(TAG, "setAlram.custNo=" + custNo);
        if (checkGCToken(iGCResult) == false) {
            return;
        } else if (TextUtils.isEmpty(custNo)) {
            iGCResult.onResult(false, "사용자 정보 등록 후 이용 가능합니다.", null);
            return;
        } else if (TextUtils.isEmpty(pushToken)) {
            iGCResult.onResult(false, "PUSH 토큰 등록 후 이용 가능합니다.", null);
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
                        boolean isSuccessed = recv.isSuccess(recv.resultcode);
                        SharedPref.getInstance(mContext).savePreferences(alramType.getAlramName(), isEnable);
                        iGCResult.onResult(isSuccessed, recv.message, recv);
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
        mContext.startActivity(new Intent(mContext, TemperActivity.class));
//        int permissionState = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permissionState == PackageManager.PERMISSION_GRANTED) {
//            if (validCheck()) {
//                mContext.startActivity(new Intent(mContext, TemperActivity.class));
//            }
//        } else {
//            CDialog.showDlg(mContext, mContext.getString(R.string.toast_permission_location));
//        }
    }

    /**
     * 테스트용 기능
     * 인증토큰, Push토큰, 고객번호 데이터를 초기화 한다.
     */
    public void resetGCData() {
        SharedPref.getInstance(mContext).removeAllPreferences();
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


    /**
     * 위치정보 찾기
     */
    private void registerLocationUpdates(String temper, String registTime, boolean isWearable, final IGCResult iGCResult) {
//        showProgress();
        GpsInfo gps = new GpsInfo(mContext);
        if(gps.isGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //GLog.d("위치", "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude);
            if(latitude != 0.0d && longitude != 0.0d){
                String address = Util.FindAddress(mContext, latitude, longitude);

                if(TextUtils.isEmpty(address) == false){
                    address = address.replace("대한민국 ", "");
//                    address = address.replace("경기도 ", "");
                    String[] addrArr = address.split(" ");
                    android.util.Log.i(TAG, "Gps_info: " + Arrays.toString(addrArr));

                    if (TextUtils.isEmpty(temper)) {
                        iGCResult.onResult(false, mContext.getString(R.string.temper_input_request), null);
                    }

                    Tr_Temperature.RequestData requestData = new Tr_Temperature.RequestData();
                    requestData.fever = temper;
                    for (int i = 0; i < addrArr.length; i++) {
                        if (i == 0) requestData.area = addrArr[i];
                        if (i == 1) requestData.est1 = addrArr[i];
                        if (i == 2) { requestData.est2 = addrArr[i];  break; }
                    }
                    requestData.la = ""+gps.getLatitude();
                    requestData.lo = ""+gps.getLongitude();
                    requestData.Input_de = registTime;//    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    requestData.is_wearable = isWearable ? "1" : "0";

                    registTemperAPI(requestData, iGCResult);
                }
            }
        }
//        hideProgress();
    }

    /**
     * 체온등록 전문 요청
     * @param requestData
     * @param iGCResult
     */
    private void registTemperAPI(Tr_Temperature.RequestData requestData, final IGCResult iGCResult) {
        getData(Tr_Temperature.class, requestData, new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                if (data instanceof Tr_Temperature) {
                    Tr_Temperature recv = (Tr_Temperature) data;
                    boolean isSuccessed = recv.isSuccess(recv.resultcode);

                    if (isSuccessed) {
                        // 한시간뒤 독려 알람
                        boolean isAfter1hour = SharedPref.getInstance(mContext).getPreferences(GCAlramType.GC_ALRAM_TYPE_독려.getAlramName() , false);
                        if (isAfter1hour) {
                            // TODO 1시간뒤 독력 알람 처리. 하나측으로 부터 BroadCastReceiver class를 받아놔야 하는데 2차 개발로 미뤄짐

                        }
                    }
                    iGCResult.onResult(recv.isSuccess(recv.resultcode), ((Tr_Temperature) data).message, null);
                } else {
                    iGCResult.onResult(false, "데이터 수신 실패", null);
                }
            }
        });
    }


    /**
     * 전문요청 모듈
     * @param cls
     * @param obj
     * @param iGCResult
     */
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
//                Log.i(TAG, "result1="+result);
                if (result.result == CConnAsyncTask.CQueryResult.SUCCESS && result.data != null) {
//                    Log.e(TAG, "데이터 수신 성공::"+result);
                    if (iGCResult != null) {
                        iGCResult.onResult(true, "데이터 수신 성공", result.data);
                    }
                } else {
//                    Log.e(TAG, "데이터 수신 실패");
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
     * 체온값 저장
     *
     * @param temper    체온
     * @param iGCResult 결과값 전달 Interface
     */
    public void registGCTemper(@Nullable String temper, final IGCResult iGCResult) {
        SharedPref.getInstance(mContext).savePreferences(SharedPref.PREF_TEMPERATE,temper);    // 체온
        iGCResult.onResult(true,"성공적으로 저장되었습니다.", null);
    }

}
