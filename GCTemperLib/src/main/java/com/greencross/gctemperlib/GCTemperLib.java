package com.greencross.gctemperlib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.greencross.gctemperlib.fever.TemperControlActivity;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.CConnAsyncTask;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.NetworkUtil;
import com.greencross.gctemperlib.greencare.util.SharedPref;

import java.lang.reflect.Constructor;


public class GCTemperLib {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;

    private final String PREF_APP_TOKEN = "pref_app_token";     // 라이브러리 인증키

    private final String PREF_PUSH_TOKEN = "pref_push_token";   // 푸시키
    private final String PREF_CUST_NO = "pref_cust_no";         // 사용자 번호
    private final String PREF_GENDER = "pref_gender";     // 사용자 성별


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
        SharedPref.getInstance(mContext).savePreferences(PREF_APP_TOKEN, gcToken);
        return APP_TOKEN.equals(gcToken);
    }

    /**
     * 녹십자 체온관리 인증토큰 인증 여부 확인
     */
    public boolean isAvailableGCToken() {
        // 최초 인증
        String gcToken = SharedPref.getInstance(mContext).getPreferences(PREF_APP_TOKEN);
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
            SharedPref.getInstance(mContext).savePreferences(PREF_CUST_NO, customerNo);     // 사용자 번호
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (TextUtils.isEmpty(customerNo)) {
                        iGCResult.onResult(false, "고객 번호를 입력해주세요.", null);
                    } else {
                        iGCResult.onResult(true, "고객 번호 등록 완료", null);
                    }
                }
            }, 500);
            // TODO 알람등록 전문 처리 해야 함


            // 최초 인증
            // 인증토큰, 푸시토큰, 고객번호, 성별을 저장
//        getData(mContext, Tr_asstb_kbtg_alimi.class, requestData, true, new ApiData.IStep() {
//            @Override
//            public void next(Object obj) {
//                if (obj instanceof Tr_asstb_kbtg_alimi) {
//                    Tr_asstb_kbtg_alimi data = (Tr_asstb_kbtg_alimi) obj;
//                    if(data.data_yn.equals("Y")) {
//                        Logger.i(TAG, "MSG : " + data.DATA_LENGTH);
//
//                        if(!EVENT_POP.equals("")){
//                            Bundle bundle = new Bundle();
//                            bundle.putString("IDX",EVENT_POP);
//                            activity.replaceFragment(new AlramContentFragment(),true, true, bundle);
//                        } else {
//                            mMaxPage = data.dataList.size();
//                            Adapter.setData(data.dataList);
//                            Adapter.notifyDataSetChanged();
//                        }
//                    }else{
//                        Logger.i(TAG,"KA001 : 기타오류");
//                    }
//
//                }
//            }
//        }, null);
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
            SharedPref.getInstance(mContext).savePreferences(PREF_PUSH_TOKEN, pushToken);   // 푸시키

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(pushToken)) {
                        iGCResult.onResult(false, "푸시 토큰을 입력해주세요.", null);
                    } else {
                        iGCResult.onResult(true, "푸시 토큰 등록 완료", null);
                    }
                }
            }, 500);
            // TODO 알람등록 전문 처리 해야 함


            // 최초 인증
            // 인증토큰, 푸시토큰, 고객번호, 성별을 저장
//        getData(mContext, Tr_asstb_kbtg_alimi.class, requestData, true, new ApiData.IStep() {
//            @Override
//            public void next(Object obj) {
//                if (obj instanceof Tr_asstb_kbtg_alimi) {
//                    Tr_asstb_kbtg_alimi data = (Tr_asstb_kbtg_alimi) obj;
//                    if(data.data_yn.equals("Y")) {
//                        Logger.i(TAG, "MSG : " + data.DATA_LENGTH);
//
//                        if(!EVENT_POP.equals("")){
//                            Bundle bundle = new Bundle();
//                            bundle.putString("IDX",EVENT_POP);
//                            activity.replaceFragment(new AlramContentFragment(),true, true, bundle);
//                        } else {
//                            mMaxPage = data.dataList.size();
//                            Adapter.setData(data.dataList);
//                            Adapter.notifyDataSetChanged();
//                        }
//                    }else{
//                        Logger.i(TAG,"KA001 : 기타오류");
//                    }
//
//                }
//            }
//        }, null);
        }
    }


    /**
     * 체온저
     *
     * @param temper    체온
     * @param iGCResult 결과값 전달 Interface
     */
    public void registGCTemper(@Nullable String temper, final IGCResult iGCResult) {
        if (checkGCToken(iGCResult) == false) {

            return;
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(temper)) {
                        iGCResult.onResult(false, "체온을 입력해 주세요.", null);
                    } else {
                        iGCResult.onResult(true, "체온 등록 완료", null);
                    }
                }
            }, 500);

            // 최초 인증
            // 인증토큰, 푸시토큰, 고객번호, 성별을 저장
//        getData(mContext, Tr_asstb_kbtg_alimi.class, requestData, true, new ApiData.IStep() {
//            @Override
//            public void next(Object obj) {
//                if (obj instanceof Tr_asstb_kbtg_alimi) {
//                    Tr_asstb_kbtg_alimi data = (Tr_asstb_kbtg_alimi) obj;
//                    if(data.data_yn.equals("Y")) {
//                        Logger.i(TAG, "MSG : " + data.DATA_LENGTH);
//
//                        if(!EVENT_POP.equals("")){
//                            Bundle bundle = new Bundle();
//                            bundle.putString("IDX",EVENT_POP);
//                            activity.replaceFragment(new AlramContentFragment(),true, true, bundle);
//                        } else {
//                            mMaxPage = data.dataList.size();
//                            Adapter.setData(data.dataList);
//                            Adapter.notifyDataSetChanged();
//                        }
//                    }else{
//                        Logger.i(TAG,"KA001 : 기타오류");
//                    }
//
//                }
//            }
//        }, null);
        }
    }


    /**
     * 알림 수신여부
     *
     * @param alramType 알람 타입
     * @param isEnable  알람 수신 여부
     */
    public void settingAlramService(GCAlramType alramType, boolean isEnable, IGCResult IGCResult) {
        String pushToken = SharedPref.getInstance(mContext).getPreferences(PREF_PUSH_TOKEN);    // 푸시키
        String custNo = SharedPref.getInstance(mContext).getPreferences(PREF_CUST_NO);          // 사용자 번호
        String gender = SharedPref.getInstance(mContext).getPreferences(PREF_GENDER);           // 사용자 성별

        Log.i(TAG, "setAlram::" + alramType.name() + " " + isEnable);
        Log.i(TAG, "setAlram.pushToken=" + pushToken);
        Log.i(TAG, "setAlram.custNo=" + custNo);

        if (checkGCToken(IGCResult) == false) {
            return;
        } else if (TextUtils.isEmpty(pushToken)) {
            IGCResult.onResult(false, "PUSH 토큰 등록 후 이용 가능합니다.", null);
//            if (IGCResult != null) {
//                IGCResult.onResult(false, "PUSH 토큰 등록 후 이용 가능합니다.", null);
//            } else {
//                CDialog.showDlg(mContext, "PUSH 토큰 등록 후 이용 가능합니다.");
//            }
            return;
        } else if (TextUtils.isEmpty(custNo)) {
            IGCResult.onResult(false, "사용자 정보 등록 후 이용 가능합니다.", null);
//            if (IGCResult != null) {
//                IGCResult.onResult(false, "사용자 정보 등록 후 이용 가능합니다.", null);
//            } else {
//                CDialog.showDlg(mContext, "사용자 정보 등록 후 이용 가능합니다.");
//            }
            return;
        } else {
            SharedPref.getInstance(mContext).savePreferences(alramType.getAlramName(), isEnable);
            if (alramType == GCAlramType.GC_ALRAM_TYPE_독려) {
                // TODO
            } else if (alramType == GCAlramType.GC_ALRAM_TYPE_지역) {

            }

            // TODO 알람등록 전문 처리 해야 함
            IGCResult.onResult(true, String.format(alramType.getDesc() + " 알람 %1$s 완료", isEnable ? "등록" : "해지"), null);
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


    private void getData(final Class<? extends BaseData> cls, final Object obj, boolean isShowProgress, final IGCResult IGCResult) {
        BaseData tr = createTrClass(cls, mContext);
        if (NetworkUtil.getConnectivityStatus(mContext) == false) {
            CDialog.showDlg(mContext, "네트워크 연결 상태를 확인해주세요.");
            return;
        }
        String REAL_SERVER = "https://wkd.walkie.co.kr";    // (182.162.143.4)" - 운영
        String DEV_SERVER = "https://api.devgc.com";        //(222.121.76.58) - 개발 

        String url = REAL_SERVER;
        if (BuildConfig.DEBUG)
            url = REAL_SERVER;

        Logger.i(TAG, "LoadBalance.cls=" + cls + ", url=" + url);
        CConnAsyncTask.CConnectorListener queryListener = new CConnAsyncTask.CConnectorListener() {

            @Override
            public Object run() throws Exception {

                ApiData data = new ApiData();
                return data.getData(mContext, tr, obj);
            }

            @Override
            public void view(CConnAsyncTask.CQueryResult result) {
//                hideProgress();

                if (result.result == CConnAsyncTask.CQueryResult.SUCCESS && result.data != null) {
                    if (IGCResult != null) {
                        IGCResult.onResult(true, "데이터 수신 성공", result.data);
                    }

                } else {
                    if (IGCResult != null) {
                        IGCResult.onResult(false, "데이터 수신 실패", null);
                    } else {

                        CDialog.showDlg(mContext, "데이터 수신에 실패 하였습니다.");
                        Log.e(TAG, "CConnAsyncTask error=" + result.errorStr);
//                        hideProgress();
                    }
                }
            }
        };

        CConnAsyncTask asyncTask = new CConnAsyncTask();
        asyncTask.execute(queryListener);
    }

    private static BaseData createTrClass(Class<? extends BaseData> cls, Context context) {
        BaseData trClass = null;
        try {
            Constructor<? extends BaseData> co = cls.getConstructor();
            trClass = co.newInstance();
        } catch (Exception e) {
            try {
                Constructor<? extends BaseData> co = cls.getConstructor(Context.class);
                trClass = co.newInstance(context);
            } catch (Exception e2) {
                Log.e("BaseFragment", "createTrClass", e2);
            }
        }

        return trClass;
    }

    /**
     * 인증체크
     *
     * @return
     */
    private boolean validCheck() {
        String pushToken = SharedPref.getInstance(mContext).getPreferences(PREF_PUSH_TOKEN);    // 푸시키
        String custNo = SharedPref.getInstance(mContext).getPreferences(PREF_CUST_NO);          // 사용자 번호
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
