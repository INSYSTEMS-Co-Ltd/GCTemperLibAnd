package com.greencross.gctemperlib.hana;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.hana.component.CDialog;
import com.greencross.gctemperlib.hana.util.SharedPref;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_AreaSetup;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Login;
import com.greencross.gctemperlib.util.GpsInfo;
import com.greencross.gctemperlib.util.Util;

public class SettingAddressFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = SettingAddressFragment.class.getSimpleName();

    private TextView mAddressTv;

    private String mAddressDo = "";
    private String mAddressGu = "";

//    private ImageButton common_left_btn;

    public static Fragment newInstance() {
        SettingAddressFragment fragment = new SettingAddressFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_setting_address, container, false);
        if (getActivity() instanceof DummyActivity) {
            getActivity().setTitle(getString(R.string.title_setting_address));
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddressTv = (TextView) view.findViewById(R.id.btn_search_address);
//        common_left_btn  = (ImageButton) view.findViewById(R.id.common_left_btn);
        Tr_Login login = SharedPref.getInstance(getContext()).getLoginInfo();
        mAddressDo = login.heat_do;
        mAddressGu = login.heat_si;
        if (mAddressDo.length() > 0 && mAddressGu.length() > 0) {
            mAddressTv.setText(mAddressDo + " "  + mAddressGu);
        }
//        else {
//            mBtnSearchAddress.setText(getString(R.string.none));
//        }

        view.findViewById(R.id.gps_icon).setOnClickListener(this);
        mAddressTv.setOnClickListener(this);

        view.findViewById(R.id.temper_regist_done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registAddress();
            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_search_address) {
//            Intent intent = new Intent(SettingAddressActivity.this, SearchAddressActivity.class);
//            startActivityForResult(intent, CommonData.REQUEST_ADDRESS_SEARCH);
            DummyActivity.startActivityForResult(getActivity(), CommonData.REQUEST_ADDRESS_SEARCH, SearchAddressFragment.class, new Bundle());
//        } else
//            if (id == R.id.btn_save_address) {
//            if (mAddressDo.length() > 0 && mAddressGu.length() > 0) {
//                registAddress();
//            } else {
//                CustomAlertDialog customAlertDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_A);
//                customAlertDialog.setTitle(getString(R.string.app_name_kr));
//                customAlertDialog.setContent(getString(R.string.non_address));
//                customAlertDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
//                customAlertDialog.show();
//            }
        } else if (id == R.id.gps_icon) {
            GpsInfo gps = new GpsInfo(getContext());
            if (gps.isGetLocation()) {
                registerLocationUpdates();
            } else {
                gps.showSettingsAlert();
            }
//        } else if (id == R.id.common_left_btn) {
//            getActivity().setResult(-2);
//            getActivity().finish();
        }
    }

    /**
     * 열지도 지역정보 넣기 api
     */
    public void registAddress() {
        String text = mAddressTv.getText().toString();
        if (TextUtils.isEmpty(text.trim()) || TextUtils.isEmpty(mAddressDo) || TextUtils.isEmpty(mAddressGu) ) {
            CDialog.showDlg(getContext(), "알림", getString(R.string.non_address));
            return;
        }

        Tr_AreaSetup.RequestData requestData = new Tr_AreaSetup.RequestData();
        requestData.heat_do = mAddressDo;
        requestData.heat_si = mAddressGu;

        getData(Tr_AreaSetup.class, requestData, (isSuccess, message, data) -> {
            if (data instanceof Tr_AreaSetup) {
                Tr_AreaSetup recv = (Tr_AreaSetup) data;
                if (recv.isSuccess(recv.resultcode)) {
                    CDialog.showDlg(getContext(), "알림", recv.message);
                } else {
                    CDialog.showDlg(getContext(), "알림", "데이터 수신 실패");
                }
            } else {
                CDialog.showDlg(getContext(), "알림", "데이터 수신 실패");
            }
        });
    }

    /**
     * 위치정보 찾기
     */
    private LocationManager mLM;
    private void registerLocationUpdates() {
        showProgress();

        GpsInfo gps = new GpsInfo(getContext());
        if(gps.isGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //GLog.d("위치", "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude);
            if(latitude != 0.0d && longitude != 0.0d){
                String address = Util.FindAddress(getContext(), latitude, longitude);

                if(TextUtils.isEmpty(address) == false){
                    address = address.replace("대한민국 ", "");
//                    address = address.replace("경기도 ", "");
                    String[] addrArr = address.split(" ");
                    mAddressDo = addrArr[0];
                    mAddressGu = addrArr[1];

                    mAddressTv.setText(addrArr[0]+" "+addrArr[1]);
                    hideProgress();
                }
            }
        }

        hideProgress();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CommonData.REQUEST_ADDRESS_SEARCH: // 열지도 주소 세팅
                String address = data.getStringExtra(CommonData.EXTRA_ADDRESS);
                String[] addresss = address.split(" ");
                mAddressDo = addresss[0];
                mAddressGu = addresss[1];
                Log.i(TAG, "inputaddress: " + mAddressGu + mAddressDo);
                mAddressTv.setText(mAddressDo + " " + mAddressGu);

                // 지역 로그인 정보 갱신
                Tr_Login login = SharedPref.getInstance(getContext()).getLoginInfo();
                login.heat_do = mAddressDo;
                login.heat_si = mAddressGu;
                SharedPref.getInstance(getContext()).saveLoginInfo(login);
                break;
        }
    }



//    /**
//     * 네트워크 리스너
//     */
//    public CustomAsyncListener networkListener = new CustomAsyncListener() {
//
//        @Override
//        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
//            // TODO Auto-generated method stub
//
//            switch (type) {
//                case NetworkConst.NET_MBER_USER_HEAT_AREA_ADD:    // 비밀번호 변경 완료
//                    switch (resultCode) {
//                        case CommonData.API_SUCCESS:
//                            JsonLogPrint.printJson(resultData.toString());
//                            GLog.i("API_SUCCESS", "dd");
//                            getActivity().setResult(Activity.RESULT_OK);
////                            commonData.setAddressDo(mAddressDo);
////                            commonData.setAddressGu(mAddressGu);
//                            getActivity().finish();
//                            break;
//                    }
//                    break;
//            }
//            hideProgress();
//        }
//
//        @Override
//        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
//            // TODO Auto-generated method stub
//            hideProgress();
//            dialog.show();
//        }
//
//        @Override
//        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
//            // TODO Auto-generated method stub
//            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
//            hideProgress();
//            dialog.show();
//
//        }
//    };

}
