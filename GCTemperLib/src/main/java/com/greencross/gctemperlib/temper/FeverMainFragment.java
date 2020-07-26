package com.greencross.gctemperlib.temper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencross.gctemperlib.base.BaseFragment;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.FeverItem;
import com.greencross.gctemperlib.collection.RemedyItem;
import com.greencross.gctemperlib.common.ApplinkDialog;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.RequestApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by MobileDoctor on 2017-02-28.
 */

public class FeverMainFragment extends BaseFragment implements View.OnClickListener, TemperMainActivity.onKeyBackPressedListener {


    private View view;

    private ImageButton mBtnFeverPut, mBtnRemedyPut, mBtnMemoPut, mBtnFeverFaq, mBtnFeverHx, mBtnVideo;
    private TextView mTxtFeverMain, mTxtRemedyVolume_1, mTxtRemedyVolume_2, mTxtLastFeverDate;
    private ImageView mBtnRemedyHelp;

    CustomAlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fever_main_fragment, null);

        init(view);
        setEvent();



        Intent intent =  getActivity().getIntent();
        int chl_sn = intent.getIntExtra("chl_sn", 0);
        if(chl_sn != 0){
            intent.putExtra("chl_sn", 0);
            getActivity().startActivity(new Intent(getContext(), FeverInputActivity.class));
            Util.BackAnimationStart(getActivity());
        }

        return view;
    }

    /**
     * 객체 초기화
     * @param view view
     */
    public void init(View view){

        mTxtFeverMain = (TextView)view.findViewById(R.id.txt_fever_main);
        mTxtLastFeverDate = (TextView)view.findViewById(R.id.txt_last_fever_date);
        mTxtRemedyVolume_1 = (TextView)view.findViewById(R.id.txt_remedy_volume_1);
        mTxtRemedyVolume_2 = (TextView)view.findViewById(R.id.txt_remedy_volume_2);

        mBtnFeverHx = (ImageButton)view.findViewById(R.id.btn_fever_hx);
        mBtnFeverPut = (ImageButton)view.findViewById(R.id.btn_fever_put) ;
        mBtnRemedyPut = (ImageButton)view.findViewById(R.id.btn_remedy_put) ;
        mBtnMemoPut = (ImageButton)view.findViewById(R.id.btn_memo_put) ;
        mBtnFeverFaq = (ImageButton)view.findViewById(R.id.btn_fever_faq) ;
        mBtnVideo = (ImageButton)view.findViewById(R.id.btn_video) ;

        mBtnRemedyHelp = (ImageView)view.findViewById(R.id.btn_remedy_help);

    }


    public void setEvent(){
        mBtnFeverHx.setOnClickListener(this);
        mBtnFeverPut.setOnClickListener(this);
        mBtnRemedyPut.setOnClickListener(this);
        mBtnMemoPut.setOnClickListener(this);
        mBtnFeverFaq.setOnClickListener(this);
        mBtnRemedyHelp.setOnClickListener(this);
        mBtnVideo.setOnClickListener(this);
        view.findViewById(R.id.share_btn).setOnClickListener(this);



        //click 저장
        OnClickListener mClickListener = new OnClickListener(this,view, getContext());

        //아이 체온
        view.findViewById(R.id.share_btn).setOnTouchListener(mClickListener);
        mBtnFeverPut.setOnTouchListener(mClickListener);
        mBtnRemedyPut.setOnTouchListener(mClickListener);
        mBtnFeverHx.setOnTouchListener(mClickListener);
        mBtnMemoPut.setOnTouchListener(mClickListener);
        mBtnVideo.setOnTouchListener(mClickListener);
        mBtnFeverFaq.setOnTouchListener(mClickListener);

        //코드 부여(아이 체온)
        view.findViewById(R.id.share_btn).setContentDescription(getString(R.string.ShareBtn5));
        mBtnFeverPut.setContentDescription(getString(R.string.BtnFeverPut));
        mBtnRemedyPut.setContentDescription(getString(R.string.BtnRemedyPut));
        mBtnFeverHx.setContentDescription(getString(R.string.BtnFeverHx));
        mBtnMemoPut.setContentDescription(getString(R.string.BtnMemoPut));
        mBtnVideo.setContentDescription(getString(R.string.BtnVideo));
        mBtnFeverFaq.setContentDescription(getString(R.string.BtnFeverFaq));
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        TemperMainActivity activity = (TemperMainActivity)getActivity();

        int id = v.getId();
        if (id == R.id.btn_fever_put) {
            intent = new Intent(getContext(), FeverInputActivity.class);
            getActivity().startActivity(intent);
            Util.BackAnimationStart(getActivity());
        } else if (id == R.id.btn_remedy_put) {
            intent = new Intent(getContext(), RemedyInputActivity.class);
            getActivity().startActivity(intent);
            Util.BackAnimationStart(getActivity());
        } else if (id == R.id.btn_fever_hx) {
            intent = new Intent(getContext(), FeverHxActivity.class);
            getActivity().startActivity(intent);
            Util.BackAnimationStart(getActivity());
        } else if (id == R.id.btn_memo_put) {
            intent = new Intent(getContext(), MemoInputActivity.class);
            getActivity().startActivity(intent);
            Util.BackAnimationStart(getActivity());
        } else if (id == R.id.btn_fever_faq) {
            switchFragment(new FeverFAQFragment());
            activity.switchActionBarTheme(TemperMainActivity.THEME_YELLOW);
            activity.switchActionBarTitle("열 F A Q");
//        } else if (id == R.id.btn_video) {
//            intent = new Intent(getContext(), EduVideoActivity.class);
//            intent.putExtra("TITLE", getString(R.string.edu_video_title_04));
//            intent.putExtra("ML_MCODE", "3");
//            startActivity(intent);
        } else if (id == R.id.share_btn) {
            requestFeverShareApi();
        }
    }

    public void RemedyVolumeSet(){

        if(MainActivity.mLastWeight.length() > 0 && !MainActivity.mLastWeight.equals("0")){
            TemperMainActivity.max_reducer_1 = Util.getMaxReducer_A(MainActivity.mLastWeight);
            TemperMainActivity.max_reducer_2 = Util.getMaxReducer_I(MainActivity.mLastWeight);

            TemperMainActivity.cur_reducer_1 = 0f;
            TemperMainActivity.cur_reducer_2 = 0f;

            for(int i = 0; i < TemperMainActivity.mRemedyItems.size(); i++){
                if(TemperMainActivity.mRemedyItems.get(i).getmInputKind().equals("0")){
                    if(TemperMainActivity.mRemedyItems.get(i).getmInputType().equals("0")){
                        TemperMainActivity.cur_reducer_1 += Double.parseDouble(TemperMainActivity.mRemedyItems.get(i).getmInputVolume());
                    }else{
                        TemperMainActivity.cur_reducer_1 += Util.converterMGtoCC(Double.parseDouble(TemperMainActivity.mRemedyItems.get(i).getmInputVolume()), false);
                    }
                }else{
                    if(TemperMainActivity.mRemedyItems.get(i).getmInputType().equals("0")){
                        TemperMainActivity.cur_reducer_2 += Double.parseDouble(TemperMainActivity.mRemedyItems.get(i).getmInputVolume());
                    }else{
                        TemperMainActivity.cur_reducer_2 += Util.converterMGtoCC(Double.parseDouble(TemperMainActivity.mRemedyItems.get(i).getmInputVolume()), false);
                    }
                }
            }

            mTxtRemedyVolume_1.setText( String.format("%.1f", TemperMainActivity.cur_reducer_1) + " / " + String.format("%.1f", TemperMainActivity.max_reducer_1) + getString(R.string.ml));
            mTxtRemedyVolume_2.setText( String.format("%.1f", TemperMainActivity.cur_reducer_2) + " / " + String.format("%.1f", TemperMainActivity.max_reducer_2) + getString(R.string.ml));
        }else{
            mDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_A);
            mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
            mDialog.setContent(getString(R.string.empty_height));
            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
            mDialog.show();

            mTxtRemedyVolume_1.setText("- / -" + getString(R.string.ml));
            mTxtRemedyVolume_2.setText("- / -" + getString(R.string.ml));
        }

    }

    /**
     * 오늘 체온 리스트 가져오기
     */
    public void requestFeverRecordApi(String chl_sn) {
//        GLog.i("requestAppInfo");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // {   "api_code": "chldrn_growth_list",   "insures_code": "106", "mber_sn": "10035"  ,"chl_sn": "1000" ,"pageNumber": "1" , "growth_typ": "1"}
        try {
            SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE);
            Date today = new Date();
            Date yesterday = new Date();
            yesterday.setHours(yesterday.getHours()-24);
            String startDe = format.format(yesterday);
            String endDe = format.format(today);

            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HF003);    //  api 코드명
            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_START_DE_F, startDe);
            object.put(CommonData.JSON_END_DE_F, endDe);

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(getActivity(), NetworkConst.NET_FEVER_LIST, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgress());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }


    /**
     * 오늘 해열제 리스트 가져오기
     */
    public void requestRemedyRecordApi(String chl_sn) {
//        GLog.i("requestAppInfo");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // {   "api_code": "chldrn_growth_list",   "insures_code": "106", "mber_sn": "10035"  ,"chl_sn": "1000" ,"pageNumber": "1" , "growth_typ": "1"}
        try {
            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE);

            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HR002);    //  api 코드명
            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_SEL_DE_F, format.format(today));
            object.put(CommonData.JSON_INPUT_KIND_F, CommonData.JSON_INPUT_KIND_ALL);

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(getActivity(), NetworkConst.NET_REMEDY_LIST, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgress());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    /**
     * 우리아이 성장현황 공유
     */
    public void requestFeverShareApi() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // {"api_code":"asstb_mber_cntr_bdheat"
        //,"insures_code":"108"
        //,"cntr_typ":"12"
        //,"mber_sn":"115232"
        //,"bdheat_ncl":"35"
        //,"fever_tylenol":"-"
        //,"fever_burpen":"-"
        //}
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, "asstb_mber_cntr_bdheat");    //  api 코드명
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);          //  insures 코드
            object.put(CommonData.JSON_CNTR_TYP, "12");
            object.put(CommonData.JSON_MBER_SN, CommonData.getInstance(getContext()).getMberSn());             //  회원고유값
            object.put("bdheat_ncl",mTxtFeverMain.getText().toString());
            if(MainActivity.mLastWeight.length() > 0 && !MainActivity.mLastWeight.equals("0")) {
                object.put("fever_tylenol", String.format("%.1f", TemperMainActivity.cur_reducer_1));
                object.put("fever_burpen", String.format("%.1f", TemperMainActivity.cur_reducer_2));
            } else{
                object.put("fever_tylenol", "-");
                object.put("fever_burpen", "-");
            }

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(getActivity(), NetworkConst.NET_ASSTB_MBER_CNTR_BDHEAT, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgress());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }


    /**
     * 최근 키 몸무게 가져오기
     */
    public void requestGrowthLastDataApi(String chl_sn) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // {"api_code":"chldrn_growth_last_cm_height","insures_code":"108","app_code":"android","mber_sn":"18622","chl_sn":"1312"}
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_CHLDRN_GROWTH_LAST_CM_HEIGHT);    //  api 코드명
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);          //  insures 코드
            object.put(CommonData.JSON_APP_CODE, CommonData.APP_CODE_ANDROID);          //  os
            object.put(CommonData.JSON_MBER_SN, CommonData.getInstance(getContext()).getMberSn());             //  회원고유값
            object.put(CommonData.JSON_CHL_SN, chl_sn);               //  자녀키값

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(getActivity(), NetworkConst.NET_GROWTH_LAST_DATA, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgress());
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

            switch ( type ) {
                case NetworkConst.NET_FEVER_LIST:
                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                if (data_yn.equals(CommonData.YES)) {
                                    JSONArray feverArr = resultData.getJSONArray(CommonData.JSON_DATA_F);
                                    TemperMainActivity.mFeverItems.clear();
                                    // 데이터가 있을 시
                                    if (feverArr.length() > 0) {
                                        for(int i = 0; i < feverArr.length(); i++){
                                            JSONObject object = feverArr.getJSONObject(i);

                                            FeverItem item = new FeverItem();
                                            item.setmFeverSn(object.getString(CommonData.JSON_FEVER_SN_F));
                                            item.setmInputDe(object.getString(CommonData.JSON_INPUT_DE_F));
                                            item.setmInputFever(object.getString(CommonData.JSON_INPUT_FEVER_F));

                                            TemperMainActivity.mFeverItems.add(item);
                                        }

                                        SimpleDateFormat format_1 = new SimpleDateFormat(CommonData.PATTERN_DATETIME_S);
                                        SimpleDateFormat format_2 = new SimpleDateFormat(CommonData.PATTERN_DATE_DOT);
                                        mTxtFeverMain.setText(TemperMainActivity.mFeverItems.get(0).getmInputFever());
                                        mTxtLastFeverDate.setText( format_2.format(format_1.parse(TemperMainActivity.mFeverItems.get(0).getmInputDe())));
                                        mTxtLastFeverDate.setVisibility(View.VISIBLE);
                                    }else{
                                        mTxtFeverMain.setText("0");
                                        mTxtLastFeverDate.setVisibility(View.GONE);
                                    }
                                }else {
                                    mTxtFeverMain.setText("0");
                                    mTxtLastFeverDate.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                GLog.e(e.toString());
                                mTxtFeverMain.setText("0");
                                mTxtLastFeverDate.setVisibility(View.GONE);
                            }

                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            break;
                    }
                    break;

                case NetworkConst.NET_REMEDY_LIST:
                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);
                                TemperMainActivity.mRemedyItems.clear();
                                if (data_yn.equals(CommonData.YES)) {



                                    JSONArray remedyArr = resultData.getJSONArray(CommonData.JSON_DATA_F);
                                    // 데이터가 있을 시
                                    if (remedyArr.length() > 0) {

                                        for(int i = 0; i < remedyArr.length(); i++){
                                            JSONObject object = remedyArr.getJSONObject(i);

                                            RemedyItem item = new RemedyItem();
                                            item.setmRemedySn(object.getString(CommonData.JSON_REMEDY_SN_F));
                                            item.setmInputType(object.getString(CommonData.JSON_INPUT_TYPE_F));
                                            item.setmInputKind(object.getString(CommonData.JSON_INPUT_KIND_F));
                                            item.setmInputVolume(object.getString(CommonData.JSON_INPUT_VOLUME_F));
                                            item.setmInputDe(object.getString(CommonData.JSON_INPUT_DE_F));

                                            TemperMainActivity.mRemedyItems.add(item);
                                        }
                                    }


                                }

                            } catch (Exception e) {
                                GLog.e(e.toString());
                            }

                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            break;
                    }
                    requestGrowthLastDataApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());



                    break;

                case NetworkConst.NET_ASSTB_MBER_CNTR_BDHEAT:
                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN);
                                if (data_yn.equals(CommonData.YES)) {
                                    String imgUrl = "https://wkd.walkie.co.kr/HL_FV/info/image/01_12.png";

                                    String cntr_url = resultData.getString("cntr_url");

                                    if(cntr_url.contains("https://wkd.walkie.co.kr"));
                                    String param = cntr_url.replace("https://wkd.walkie.co.kr","");

                                    View view = LayoutInflater.from(getContext()).inflate(R.layout.applink_dialog_layout, null);
                                    ApplinkDialog dlg = ApplinkDialog.showDlg(getContext(), view);
//                                    dlg.setSharing(imgUrl, "img", "", "","[현대해상 "+ KakaoLinkUtil.getAppname(getContext().getPackageName(),getContext())+"]","우리 아이 체온 현황","자세히보기","",false,"chl_fever.png",param,cntr_url);

                                }else {
                                }

                            } catch (Exception e) {
                                GLog.e(e.toString());
                            }

                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            break;
                    }
                    break;

                case NetworkConst.NET_GROWTH_LAST_DATA:

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN);
                                if (data_yn.equals(CommonData.YES)) {
                                    MainActivity.mLastHeight = resultData.getString(CommonData.JSON_LAST_HRIGTH);
                                    MainActivity.mLastWeight = resultData.getString(CommonData.JSON_LAST_BDWGH);
                                    MainActivity.mLastCmResult = resultData.getString(CommonData.JSON_CM_REUSLT);
                                    MainActivity.mLastKgResult = resultData.getString(CommonData.JSON_KG_REUSLT);
                                    MainActivity.mLastHeightDe = resultData.getString(CommonData.JSON_LAST_HRIGTH_DE);
                                    MainActivity.mLastWeightDe = resultData.getString(CommonData.JSON_LAST_BDWGH_DE);
                                } else {
                                    MainActivity.mLastHeight = "0";
                                    MainActivity.mLastWeight = "0";
                                    MainActivity.mLastCmResult = "";
                                    MainActivity.mLastKgResult = "";
                                    MainActivity.mLastHeightDe = "";
                                    MainActivity.mLastWeightDe = "";
                                }
                            } catch (Exception e) {
                                GLog.e(e.toString());
                                MainActivity.mLastHeight = "0";
                                MainActivity.mLastWeight = "0";
                                MainActivity.mLastCmResult = "";
                                MainActivity.mLastKgResult = "";
                                MainActivity.mLastHeightDe = "";
                                MainActivity.mLastWeightDe = "";
                            }

                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            MainActivity.mLastHeight = "0";
                            MainActivity.mLastWeight = "0";
                            MainActivity.mLastCmResult = "";
                            MainActivity.mLastKgResult = "";
                            MainActivity.mLastHeightDe = "";
                            MainActivity.mLastWeightDe = "";
                            break;
                    }
                    // 해열제 모두 받아온 뒤 해열제 용량 계산 실시
                    RemedyVolumeSet();

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

    @Override
    public void onResume(){
        super.onResume();
//        requestFeverRecordApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());
//        requestRemedyRecordApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());

    }

    @Override
    public void onStart() {
        super.onStart();
        ((TemperMainActivity) getContext()).setOnKeyBackPressedListener(this);
    }

    @Override
    public void onBack() {
        TemperMainActivity activity = (TemperMainActivity)getActivity();
        activity.finish();
    }
}
