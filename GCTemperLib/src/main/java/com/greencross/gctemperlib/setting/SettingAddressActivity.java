package com.greencross.gctemperlib.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.RequestApi;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.GpsInfo;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.util.JsonLogPrint;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class SettingAddressActivity extends BackBaseActivity implements View.OnClickListener {
    private final String TAG = SettingAddressActivity.class.getSimpleName();

    TextView mTvAddressView, mBtnSearchAddress;
    Button mBtnSaveAddress ;

    String mAddressDo = "";
    String mAddressGu = "";

    private ImageButton common_left_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_address);

        setTitle(getString(R.string.title_setting_address));

        init();

        mAddressDo = CommonData.getInstance(SettingAddressActivity.this).getAddressDo();
        mAddressGu = CommonData.getInstance(SettingAddressActivity.this).getAddressGu();
        if (mAddressDo.length() > 0 && mAddressGu.length() > 0) {
            mTvAddressView.setText(mAddressDo + CommonData.STRING_SPACE + mAddressGu);
        } else {
            mTvAddressView.setText(getString(R.string.none));
        }


    }

    /**
     * 초기화
     */
    public void init() {
        mBtnSearchAddress = (TextView) findViewById(R.id.btn_search_address);
        mTvAddressView = (TextView) findViewById(R.id.tv_address_view);
        mBtnSaveAddress = (Button) findViewById(R.id.btn_save_address);
        common_left_btn  = (ImageButton) findViewById(R.id.common_left_btn);

        findViewById(R.id.gps_icon).setOnClickListener(this);
        mBtnSearchAddress.setOnClickListener(this);
        mBtnSaveAddress.setOnClickListener(this);
        common_left_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_search_address) {
            Intent intent = new Intent(SettingAddressActivity.this, SearchAddressActivity.class);
            startActivityForResult(intent, CommonData.REQUEST_ADDRESS_SEARCH);
        } else if (id == R.id.btn_save_address) {
            if (mAddressDo.length() > 0 && mAddressGu.length() > 0) {
                requestAddressData();
            } else {
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(SettingAddressActivity.this, CustomAlertDialog.TYPE_A);
                customAlertDialog.setTitle(getString(R.string.app_name_kr));
                customAlertDialog.setContent(getString(R.string.non_address));
                customAlertDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                customAlertDialog.show();
            }
        } else if (id == R.id.gps_icon) {
            GpsInfo gps = new GpsInfo(this);
            if (gps.isGetLocation()) {
                registerLocationUpdates();
            } else {
                gps.showSettingsAlert();
            }
        } else if (id == R.id.common_left_btn) {
            setResult(-2);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            setResult(-2);
            finish();

            return true;

        }

        return super.onKeyDown(keyCode, event);

    }

    /**
     * 열지도 지역정보 넣기 api
     */
    public void requestAddressData() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_MBER_USER_HEAT_AREA_ADD);
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
            object.put(CommonData.JSON_MBER_SN, commonData.getMberSn());
//            object.put(CommonData.JSON_MBER_ID, mber_id);
            object.put(CommonData.JSON_AREA_DO, mAddressDo);
            object.put(CommonData.JSON_AREA_SI, mAddressGu);

            android.util.Log.i(TAG, "address: " + mAddressGu + mAddressDo);


            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(SettingAddressActivity.this, NetworkConst.NET_MBER_USER_HEAT_AREA_ADD, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }


    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub

            switch (type) {
                case NetworkConst.NET_MBER_USER_HEAT_AREA_ADD:    // 비밀번호 변경 완료
                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            JsonLogPrint.printJson(resultData.toString());
                            GLog.i("API_SUCCESS", "dd");
                            setResult(RESULT_OK);
                            commonData.setAddressDo(mAddressDo);
                            commonData.setAddressGu(mAddressGu);
                            finish();
                            break;
                    }
                    break;
            }
            hideProgress();
        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            hideProgress();
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            hideProgress();
            dialog.show();

        }
    };

    /**
     * 위치정보 찾기
     */
    private LocationManager mLM;
    private void registerLocationUpdates() {
        showProgress();

        GpsInfo gps = new GpsInfo(this);
        if(gps.isGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //GLog.d("위치", "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude);
            if(latitude != 0.0d && longitude != 0.0d){
                String address = Util.FindAddress(this, latitude, longitude);

                if(TextUtils.isEmpty(address) == false){
                    address = address.replace("대한민국 ", "");
//                    address = address.replace("경기도 ", "");
                    String[] addrArr = address.split(" ");
                    mAddressDo = addrArr[0];
                    mAddressGu = addrArr[1];
                    setResult(RESULT_OK);
                    android.util.Log.i(TAG, "Gps_info: " + mAddressDo + mAddressGu);

                    mTvAddressView.setText(addrArr[0]+" "+addrArr[1]);
                    hideProgress();
                }
            }
        }

        hideProgress();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        GLog.i("requestCode = " + requestCode, "dd");
        GLog.i("resultCode = " + resultCode, "dd");
        GLog.i("data = " + data, "dd");

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CommonData.REQUEST_ADDRESS_SEARCH: // 열지도 주소 세팅
                String address = data.getStringExtra(CommonData.EXTRA_ADDRESS);
                String[] addresss = address.split(CommonData.STRING_SPACE);
                mAddressDo = addresss[0];
                mAddressGu = addresss[1];
                android.util.Log.i(TAG, "inputaddress: " + mAddressGu + mAddressDo);
                mTvAddressView.setText(mAddressDo + CommonData.STRING_SPACE + mAddressGu);
                break;
        }
    }
}
