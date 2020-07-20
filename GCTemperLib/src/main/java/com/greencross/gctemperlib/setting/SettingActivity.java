package com.greencross.gctemperlib.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.network.RequestApi;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.webview.BackWebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jihoon on 2016-03-28.
 * 설정 클래스
 * @since 0, 1
 */
public class SettingActivity extends BackBaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private final String TAG = getClass().getSimpleName();

    public static Activity SettingActivity;

    private RelativeLayout mMonJinLayout, mAlarmLayout, mAuthLayout, mAboutServiceLayout,
                           mPersonalTerms_1_Layout, mPersonalTerms_2_Layout, mLocationTermsLayout,
                           mVersionLayout, mLogoutLayout, mJunMemberLayout, mJungSwitchLayout;
    private TextView mVersionTv, mAuthTitleTv, mJunMemberTv, mJungSwitchTv;
    private ImageView mVersionImg;
    private View view;
    private View junline, memline;

    private String mCurrentPush_1, mCurrentPush_2;
    private Switch mPushCb_1, mPushCb_2;
    private boolean is_onResume = false;

    private boolean pushCb1_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        view = findViewById(R.id.root_view);


        SettingActivity = this;

        setTitle(getString(R.string.setting));

        init();
        setEvent();



        mVersionTv.setText(commonData.getAppVersion()); // 버전 정보

        switch(commonData.getLoginType()){
            case CommonData.LOGIN_TYPE_PARENTS: // 로그인 타입 부모
                mAuthTitleTv.setText(getString(R.string.id_info));
                break;
            case CommonData.LOGIN_TYPE_CHILD:   // 로그인 타입 자녀
                mAuthTitleTv.setText(getString(R.string.auth_management));
                break;
        }

    }

    /**
     * 초기화
     */
    public void init(){

        mMonJinLayout = (RelativeLayout) findViewById(R.id.MonJin_layout);
        mAlarmLayout    =   (RelativeLayout)    findViewById(R.id.alarm_layout);
        mAuthLayout    =   (RelativeLayout)    findViewById(R.id.auth_layout);
        mAboutServiceLayout=    (RelativeLayout)    findViewById(R.id.about_service_layout);
        mPersonalTerms_1_Layout = (RelativeLayout)findViewById(R.id.personal_terms_1_layout);
        mPersonalTerms_2_Layout = (RelativeLayout)findViewById(R.id.personal_terms_2_layout);
        mLocationTermsLayout = (RelativeLayout)findViewById(R.id.location_terms_layout);
        mVersionLayout    =   (RelativeLayout)    findViewById(R.id.version_info_layout);
        mLogoutLayout    =   (RelativeLayout)    findViewById(R.id.logout_layout);
        mJunMemberLayout = (RelativeLayout) findViewById(R.id.junmember_layout);
        mJungSwitchLayout = (RelativeLayout) findViewById(R.id.memberswitch_layout);

        mVersionTv      =   (TextView)          findViewById(R.id.version_info_tv);
        mAuthTitleTv    =   (TextView)          findViewById(R.id.auth_tv);
        mJunMemberTv    =   (TextView)          findViewById(R.id.junmember_tv);
        mJungSwitchTv    =   (TextView)          findViewById(R.id.memberswitch_tv);

        mVersionImg     =   (ImageView)         findViewById(R.id.version_info_img);
        junline  = findViewById(R.id.jun_line);
        memline = findViewById(R.id.mem_line);

        mPushCb_1 =   (Switch)  findViewById(R.id.push_alarm_cb_1);
        mPushCb_2 =   (Switch)  findViewById(R.id.push_alarm_cb_2);



        if(CommonData.getInstance(SettingActivity.this).getMberGrad().equals("10")){
            mJungSwitchTv.setTextColor(ContextCompat.getColor(SettingActivity.this,R.color.color_BFBFBF));
            mJunMemberTv.setTextColor(ContextCompat.getColor(SettingActivity.this,R.color.color_BFBFBF));

            mJunMemberLayout.setVisibility(View.GONE);
            mJungSwitchLayout.setVisibility(View.GONE);
            junline.setVisibility(View.GONE);
            memline.setVisibility(View.GONE);

            mJungSwitchLayout.setEnabled(false);
            mJunMemberLayout.setEnabled(false);
        }else{
            mJungSwitchTv.setTextColor(ContextCompat.getColor(SettingActivity.this,R.color.txt_dark_bold));
            mJunMemberTv.setTextColor(ContextCompat.getColor(SettingActivity.this,R.color.txt_dark_bold));

            mJunMemberLayout.setVisibility(View.VISIBLE);
            mJungSwitchLayout.setVisibility(View.VISIBLE);

            junline.setVisibility(View.VISIBLE);
            memline.setVisibility(View.VISIBLE);

            mJungSwitchLayout.setEnabled(true);
            mJunMemberLayout.setEnabled(true);
        }

    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){
        //사전 체크 문항 설정 추가 2018.5.30
        mMonJinLayout.setOnClickListener(this);
        mAlarmLayout.setOnClickListener(this);
        mAuthLayout.setOnClickListener(this);
        mAboutServiceLayout.setOnClickListener(this);
        mPersonalTerms_1_Layout.setOnClickListener(this);
        mPersonalTerms_2_Layout.setOnClickListener(this);
        mLocationTermsLayout.setOnClickListener(this);
        mVersionLayout.setOnClickListener(this);
        mLogoutLayout.setOnClickListener(this);
        mJunMemberLayout.setOnClickListener(this);
        mJungSwitchLayout.setOnClickListener(this);

        mPushCb_1.setOnCheckedChangeListener(this);
        mPushCb_2.setOnCheckedChangeListener(this);
        mPushCb_1.setOnClickListener(this);
        mPushCb_2.setOnClickListener(this);

        //click 저장
        OnClickListener mClickListener = new OnClickListener(this,view,SettingActivity.this);

        //설정
        mMonJinLayout.setOnTouchListener(mClickListener);
        mJunMemberLayout.setOnTouchListener(mClickListener);
        mJungSwitchLayout.setOnTouchListener(mClickListener);
        mAlarmLayout.setOnTouchListener(mClickListener);
        mAuthLayout.setOnTouchListener(mClickListener);
        mAboutServiceLayout.setOnTouchListener(mClickListener);

        //코드 부여(설정)
        mMonJinLayout.setContentDescription(getString(R.string.MonJin));
        mJunMemberLayout.setContentDescription(getString(R.string.JunMember));
        mJungSwitchLayout.setContentDescription(getString(R.string.JungSwitch));
        mAlarmLayout.setContentDescription(getString(R.string.Alarm));
        mAuthLayout.setContentDescription(getString(R.string.Auth));
        mAboutServiceLayout.setContentDescription(getString(R.string.AboutService));
    }

    private void setCheck(String YN, Switch sb){
        if(YN.equals(commonData.YES)){
            sb.setChecked(true);
        } else {
            sb.setChecked(false);
        }
    }

    /**
     * 알림 설정 변경값이 있는지 체크
     * @return boolean ( true - 변경, false - 변경하지 않음 )
     */
    public boolean isChange(){
        boolean change = false;

        if(!mCurrentPush_1.equals(mPushCb_1.isChecked()?commonData.YES:commonData.NO)){    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        if(!mCurrentPush_2.equals(mPushCb_2.isChecked()?commonData.YES:commonData.NO)){    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        return change;

    }

    @Override
    protected void onResume() {
        super.onResume();
        is_onResume = true;

        mCurrentPush_1 = commonData.getMarketing_yn(); // 개인정보 마케팅 동의
        mCurrentPush_2 = commonData.getLocation_yn(); // 위치정보 수집 동의

        setCheck(commonData.getMarketing_yn(),mPushCb_1);
        setCheck(commonData.getLocation_yn(),mPushCb_2);
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        int id = v.getId();
//        if (id == R.id.MonJin_layout) { // 사전체크문항
//            intent = new Intent(SettingActivity.this, MotherHealthRegActivity.class);
//            startActivity(intent);
//            Util.BackAnimationStart(SettingActivity.this);
//        } else
            if (id == R.id.alarm_layout) { // 알림설정
            intent = new Intent(SettingActivity.this, AlarmSettingActivity.class);
            startActivity(intent);
            Util.BackAnimationStart(SettingActivity.this);
//        } else if (id == R.id.auth_layout) {  // 인증관리
//            intent = new Intent(SettingActivity.this, AuthManageActivity.class);
//            startActivity(intent);
//            Util.BackAnimationStart(SettingActivity.this);
        } else if (id == R.id.about_service_layout) { // 서비스 소개
            intent = new Intent(SettingActivity.this, AboutServiceActivity.class);
            startActivity(intent);
            Util.PopupAnimationStart(SettingActivity.this);
        } else if (id == R.id.personal_terms_1_layout) {  // 개인정보 제공 동의
//                if(commonData.getMberGrad().equals("10")) {
//                    intent = new Intent(SettingActivity.this, BackWebViewActivity.class);
//                    intent.putExtra(CommonData.EXTRA_URL, commonData.PERSONAL_TERMS_1_URL);
//                    intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.setting_personal_terms));
//                }else{
            intent = new Intent(SettingActivity.this, BackWebViewActivity.class);
            intent.putExtra(CommonData.EXTRA_URL, commonData.PERSONAL_TERMS_URL);
            intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.setting_personal_terms));
//                }
            startActivity(intent);
            Util.BackAnimationStart(SettingActivity.this);
        } else if (id == R.id.personal_terms_2_layout) {  // 개인민감정보 제공 동의
//                if(commonData.getMberGrad().equals("10")) {
//                    intent = new Intent(SettingActivity.this, BackWebViewActivity.class);
//                    intent.putExtra(CommonData.EXTRA_URL, commonData.PERSONAL_TERMS_2_URL);
//                    intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.setting_martketing_terms));
//                }else {
            intent = new Intent(SettingActivity.this, BackWebViewActivity.class);
            intent.putExtra(CommonData.EXTRA_URL, commonData.MOBILE_TERMS_URL);
            intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.setting_martketing_terms));
//                }
            startActivity(intent);
            Util.BackAnimationStart(SettingActivity.this);
            //            case R.id.personal_terms_3_layout:  // 개인정보 제3자 제공 동의
//                if(commonData.getMberGrad().equals("10")) {
//                    intent = new Intent(SettingActivity.this, BackWebViewActivity.class);
//                    intent.putExtra(CommonData.EXTRA_URL, commonData.PERSONAL_TERMS_3_URL);
//                    intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.personal_terms_3));
//                }else {
//                    intent = new Intent(SettingActivity.this, BackWebViewActivity.class);
//                    intent.putExtra(CommonData.EXTRA_URL, commonData.PERSONAL_TERMS_JUN_3_URL);
//                    intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.personal_terms_3));
//                }
//                startActivity(intent);
//                Util.BackAnimationStart(SettingActivity.this);
//                break;
        } else if (id == R.id.location_terms_layout) {  // 위치정보 이용 동의
            if (commonData.getMberGrad().equals("10")) {
                intent = new Intent(SettingActivity.this, BackWebViewActivity.class);
                intent.putExtra(CommonData.EXTRA_URL, commonData.LOCATION_TERMS_URL);
                intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.setting_location_terms));
            } else {
                intent = new Intent(SettingActivity.this, BackWebViewActivity.class);
                intent.putExtra(CommonData.EXTRA_URL, commonData.LOCATION_TERMS_JUN_URL);
                intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.setting_location_terms));
            }
            startActivity(intent);
            Util.BackAnimationStart(SettingActivity.this);
//        } else if (id == R.id.version_info_layout) {  // 버전정보
//            intent = new Intent(SettingActivity.this, VersionActivity.class);
//            startActivity(intent);
//            Util.BackAnimationStart(SettingActivity.this);
        } else if (id == R.id.logout_layout) {  // 로그아웃

            mDialog = new CustomAlertDialog(SettingActivity.this, CustomAlertDialog.TYPE_B);
            mDialog.setTitle(getString(R.string.title_logout));
            mDialog.setContent(getString(R.string.title_logout_text));
            mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {

                commonData.setMberPwd(null);
                commonData.setMain_Category("");
                commonData.setAutoLogin(false);
                commonData.setRememberId(false);
                commonData.setMberSn("");
                commonData.setHpMjYnJun(true);

                Util.setSharedPreference(SettingActivity.this, "MonJin_bef_cm", "");
                Util.setSharedPreference(SettingActivity.this, "MonJin_bef_kg", "");
                Util.setSharedPreference(SettingActivity.this, "MonJin_mber_kg", "");
                Util.setSharedPreference(SettingActivity.this, "MonJin_mber_term_kg", "");
                Util.setSharedPreference(SettingActivity.this, "MonJin_mber_chl_birth_de", "");
                Util.setSharedPreference(SettingActivity.this, "MonJin_mber_milk_yn", "");
                Util.setSharedPreference(SettingActivity.this, "MonJin_mber_birth_due_de", "");
                Util.setSharedPreference(SettingActivity.this, "MonJin_mber_chl_typ", "");
                Util.setSharedPreference(SettingActivity.this, "MonJin_actqy", "");


                commonData.setLoginType(CommonData.LOGIN_TYPE_PARENTS);
                mDialog.dismiss();
                finish();

//                Intent intent2 = new Intent(SettingActivity.this, LoginActivity.class);
//                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent2);

//                GoogleFitInstance.signOutGoogleClient(SettingActivity.this, new ApiData.IStep() {
//                    @Override
//                    public void next(Object obj) {
//                        Log.i(TAG, "구글피트니스 로그아웃 됨.");
//                    }
//                });

//                    BleUtil.BackAnimationStart(SettingActivity.this);
            });
            mDialog.show();
//        } else if (id == R.id.junmember_layout) {
//            intent = new Intent(SettingActivity.this, JunMemberAlert1Activity.class);
//            startActivity(intent);
//            Util.BackAnimationStart(SettingActivity.this);
//        } else if (id == R.id.memberswitch_layout) {
//            intent = new Intent(SettingActivity.this, SwitchMemberActivity.class);
//            startActivity(intent);
//            Util.BackAnimationStart(SettingActivity.this);
        } else if (id == R.id.push_alarm_cb_1 || id == R.id.push_alarm_cb_2) {
            is_onResume = false;
        }
    }

    /**
     * 마케팅 정보 및 위치정보 동의
     */
    public void requestAgreeAlarmSetting(String push_1, String push_2){
        GLog.i("requestAgreeAlarmSetting", "dd");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_MBER_CHECK_AGREE_YN);
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
            object.put(CommonData.JSON_MBER_SN, commonData.getMberSn());
            object.put(CommonData.JSON_MARKETING_YN, push_1);
            object.put(CommonData.JSON_LOCATION_YN, push_2);

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(SettingActivity.this, NetworkConst.NET_MBER_CHECK_AGREE_YN, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgressLayout());
        }catch(Exception e){
            GLog.i(e.toString(), "dd");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        GLog.i("onCheckedChanged", "dd");
        int id = buttonView.getId();

        if (id == R.id.push_alarm_cb_1) {
            if (isChange())
                if (!is_onResume) {
                    if (mPushCb_1.isChecked()) {
                        pushCb1_check = true;
//                            Toast.makeText(this, String.format(getResources().getString(R.string.popup_dialog_contactor_complete1_5),CDateUtil.getToday_year_month_day(),"동의")
//                                    , Toast.LENGTH_SHORT).show();
                    } else {
                        pushCb1_check = false;
//                            Toast.makeText(this, String.format(getResources().getString(R.string.popup_dialog_contactor_complete1_5),CDateUtil.getToday_year_month_day(),"동의 철회")
//                                    , Toast.LENGTH_SHORT).show();
                    }

                    requestAgreeAlarmSetting(mPushCb_1.isChecked() ? commonData.YES : commonData.NO, "");

                }
        } else if (id == R.id.push_alarm_cb_2) {
            if (isChange())
                if (!is_onResume)
                    requestAgreeAlarmSetting("", mPushCb_2.isChecked() ? commonData.YES : commonData.NO);
        }
    }


    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            if (getProgressLayout() != null)
                getProgressLayout().setVisibility(View.GONE);

            switch ( type ) {
                case NetworkConst.NET_MBER_CHECK_AGREE_YN :

                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");

                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN);
                                if(data_yn.equals(CommonData.YES)){
                                    commonData.setMarketing_yn(resultData.getString(CommonData.JSON_MARKETING_YN));
                                    commonData.setLocation_yn(resultData.getString(CommonData.JSON_LOCATION_YN));
                                    commonData.setEvent_alert_yn(resultData.getString(CommonData.JSON_EVENT_ALERT_YN).equals(commonData.YES)?true:false);
                                    commonData.setMapPushAlarm(resultData.getString(CommonData.JSON_HEAT_YN).equals(commonData.YES)?true:false);
                                    CommonData.getInstance(SettingActivity.this).setDietPushAlarm(resultData.getString(CommonData.JSON_DIET_YN).equals(CommonData.YES) ? true : false);
                                    CommonData.getInstance(SettingActivity.this).setDisease_alert_yn(resultData.getString(CommonData.JSON_DISEASE_ALERT_YN).equals(CommonData.YES) ? true : false);
                                    mCurrentPush_1 = resultData.getString(CommonData.JSON_MARKETING_YN);
                                    mCurrentPush_2 = resultData.getString(CommonData.JSON_LOCATION_YN);

                                    if(pushCb1_check){
                                        Toast.makeText(SettingActivity.this, String.format(getResources().getString(R.string.popup_dialog_contactor_complete1_5),CDateUtil.getToday_year_month_day(),"동의")
                                                , Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SettingActivity.this, String.format(getResources().getString(R.string.popup_dialog_contactor_complete1_5),CDateUtil.getToday_year_month_day(),"동의 철회")
                                                , Toast.LENGTH_SHORT).show();
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:	// 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:	// 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            break;
                    }
                    break;
            }
        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            dialog.show();

        }
    };
}
