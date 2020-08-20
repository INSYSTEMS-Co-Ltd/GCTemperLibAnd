package com.greencross.gctemperlib.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.greencross.gctemperlib.IGCResult;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.hana.component.CDialog;
import com.greencross.gctemperlib.hana.util.SharedPref;
import com.greencross.gctemperlib.hana.util.cameraUtil.RuntimeUtil;
import com.greencross.gctemperlib.hana.network.tr.BaseData;
import com.greencross.gctemperlib.hana.network.tr.CConnAsyncTask;
import com.greencross.gctemperlib.hana.network.tr.HNApiData;
import com.greencross.gctemperlib.hana.network.tr.HNCConnAsyncTask;
import com.greencross.gctemperlib.hana.util.NetworkUtil;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Login;
import com.greencross.gctemperlib.hana.util.PermissionUtils;
import com.greencross.gctemperlib.util.Util;


/**
 * Created by jihoon on 2016-03-28.
 * 뒤로가기 부모 클래스
 *
 * @since 0, 1
 */
public class BackBaseActivity extends BaseActivity {
    protected final String TAG = getClass().getSimpleName();

    protected TextView titleTextView;
    protected ImageView backImg;
    protected Button mCompleteBtn;
    protected RelativeLayout mBgLayout;

    Intent intent = null;

    protected IGCResult mIPermissionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        intent = getIntent();
    }

    /**
     * 제목 설정
     *
     * @param title 제목 문구
     */
    public void setTitle(CharSequence title) {
        if (titleTextView != null)
            titleTextView.setText(title);
    }

    /**
     * 제목 폰트 사이즈
     *
     * @param size 사이즈
     */
    public void setTitleSize(int size) {
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    /**
     * 제목 문구 색상 변경
     *
     * @param color 색상
     */
    public void setTitleColor(int color) {
        titleTextView.setTextColor(color);
    }

    /**
     * 버튼 설정
     *
     * @param title 버튼 문구
     */
    public void setCompleteTitle(CharSequence title) {
        if (mCompleteBtn != null)
            mCompleteBtn.setText(title);
    }

    public void setCompleteTitleColor(int color) {
        mCompleteBtn.setTextColor(color);
    }

    /**
     * 네비바 색상 설정
     *
     * @param color
     */
    public void setBgColor(int color) {
        mBgLayout.setBackgroundColor(color);
    }

    /**
     * 네비바 높이 설정
     *
     * @param height
     */
    public void setBgHeight(int height) {
        mBgLayout.getLayoutParams().height = height;
    }

    /**
     * 뒤로가기 이미지 반환
     *
     * @return backimg;
     */
    public ImageView getBackImg() {
        return backImg;
    }

    /**
     * 오른쪽 버튼
     *
     * @return button
     */
    public Button getCompleteBtn() {
        return mCompleteBtn;
    }

    /**
     * 백그라운드 레이아웃 반환
     *
     * @return mBgLayout
     */
    public RelativeLayout getmBgLayout() {
        return mBgLayout;
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
        titleTextView = (TextView) findViewById(R.id.common_title_tv);
        backImg = (ImageView) findViewById(R.id.common_left_btn);
        mCompleteBtn = (Button) findViewById(R.id.common_right_btn);
        mBgLayout = (RelativeLayout) findViewById(R.id.common_bg_layout);


        backImg.setOnClickListener(v -> finish());

    }

//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        // TODO Auto-generated method stub
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }


    protected void getData(Class<? extends BaseData> cls, final Object obj, final IGCResult iGCResult) {
        if (NetworkUtil.getConnectivityStatus(this) == false) {
            CDialog.showDlg(this, "네트워크 연결 상태를 확인해주세요.");
            return;
        }

        HNCConnAsyncTask.CConnectorListener queryListener = new HNCConnAsyncTask.CConnectorListener() {
            @Override
            public Object run() throws Exception {
                HNApiData data = new HNApiData();
                try {
                    Object recv = data.getData(BackBaseActivity.this, cls, obj);
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

    public boolean isLocationPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 위치권한 설정하기
     *
     * @param iPermissionResult
     */
    public void requestPermissionLocation(final IGCResult iPermissionResult) {
        mIPermissionResult = iPermissionResult;
        if (isLocationPermission()) {
            if (mIPermissionResult != null) {
                mIPermissionResult.onResult(true, null, null);
                mIPermissionResult = null;
            } else
                Log.e(TAG, "위치 권한설정 Interface 설정이 안됨.");
        } else {
            ActivityCompat.requestPermissions(this, PermissionUtils.LOCATION_PERMS, CommonData.PERMISSION_REQUEST_GPS);
        }
    }

    /**
     * 재 로그인
     */
    public void reLogin() {
        // 최초 인증, 고객번호 저장
        Tr_Login.RequestData requestData = new Tr_Login.RequestData();
        requestData.cust_id = SharedPref.getInstance(BackBaseActivity.this).getPreferences(SharedPref.PREF_CUST_NO);
        requestData.devicetoken = SharedPref.getInstance(BackBaseActivity.this).getPreferences(SharedPref.PREF_PUSH_TOKEN);
        getData(Tr_Login.class, requestData, new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                if (data instanceof Tr_Login) {
                    Tr_Login recv = (Tr_Login) data;
                    boolean isLogin = recv.isSuccess(recv.resultcode);
                    if (isLogin) {
                        SharedPref.getInstance(BackBaseActivity.this).saveLoginInfo(recv);
                        reLoginComplete();
//                        SharedPref.getInstance(BackBaseActivity.this).saveLoginInfo(recv);
//                        SharedPref.getInstance(BackBaseActivity.this).savePreferences(SharedPref.PREF_CUST_NO, customerNo);     // 사용자 번호 저장
                    }
//                    iGCResult.onResult(isLogin, recv.message, recv);
                } else {
//                    iGCResult.onResult(isSuccess, message, data);
                }
            }
        });
    }

    /**
     * 재 로그인 완료
     */
    protected void reLoginComplete() {};

    /**
     * 위치권한 설정 후
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CommonData.PERMISSION_REQUEST_GPS) {
        boolean isGranted = RuntimeUtil.verifyPermissions(grantResults);
        if (mIPermissionResult != null)
            mIPermissionResult.onResult(isGranted, null, null);

//            if (RuntimeUtil.verifyPermissions(this, grantResults)) {
//                final GCTemperLib gcLib = new GCTemperLib(this);
//                if (gcLib.isAvailableGCToken()) {
////                    gcLib.startGCMainActivity();
//                } else {
//                    CDialog.showDlg(this, "인증 후 이용 가능합니다.");
//                }
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        reLogin();
    }

    @Override
    public void finish() {
        Util.BackAnimationEnd(this);    // Activity 종료시 뒤로가기 animation
        super.finish();
    }

}

