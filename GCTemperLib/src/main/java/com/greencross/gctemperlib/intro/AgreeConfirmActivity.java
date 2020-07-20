package com.greencross.gctemperlib.intro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.greencross.gctemperlib.base.IntroBaseActivity;
import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.RequestApi;
import com.greencross.gctemperlib.webview.BackWebViewActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jihoon on 2016-03-28.
 *
 * @since 0, 1
 */
public class AgreeConfirmActivity extends IntroBaseActivity implements View.OnClickListener{

    private TextView mPersonalTv_1, mPersonalTv_2, mPersonalTv_3;

    private CheckBox mPersonalCb_1, mPersonalCb_2, mPersonalCb_3;

    private Button mConfirmBtn, mAllAgreeBtn, mCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agree_confirm_activity);

        setTitle("개인정보정책 변경 안내");

        init();
        setEvent();

        Util.setTextViewCustom(AgreeConfirmActivity.this, mPersonalTv_1, mPersonalTv_1.getText().toString(), mPersonalTv_1.getText().toString(), 0, 15, Typeface.BOLD, true);
        Util.setTextViewCustom(AgreeConfirmActivity.this, mPersonalTv_2, mPersonalTv_2.getText().toString(), mPersonalTv_2.getText().toString(), 0, 15, Typeface.BOLD, true);
        Util.setTextViewCustom(AgreeConfirmActivity.this, mPersonalTv_3, mPersonalTv_3.getText().toString(), mPersonalTv_3.getText().toString(), 0, 15, Typeface.BOLD, true);


    }

    /**
     * 초기화
     */
    public void init(){

        mPersonalTv_1      =   (TextView)          findViewById(R.id.personal_terms_1_tv);
        mPersonalTv_2      =   (TextView)          findViewById(R.id.personal_terms_2_tv);
        mPersonalTv_3      =   (TextView)          findViewById(R.id.personal_terms_3_tv);

        mPersonalCb_1      =   (CheckBox)          findViewById(R.id.personal_terms_1_cb);
        mPersonalCb_2      =   (CheckBox)          findViewById(R.id.personal_terms_2_cb);
        mPersonalCb_3      =   (CheckBox)          findViewById(R.id.personal_terms_3_cb);

        mConfirmBtn     =   (Button)            findViewById(R.id.confirm_btn);
        mAllAgreeBtn    =   (Button)            findViewById(R.id.agree_btn);
        mCancelBtn = (Button) findViewById(R.id.cancel_btn);

    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){
        //mBirthTv.setOnClickListener(this);
        mPersonalTv_1.setOnClickListener(this);
        mPersonalTv_2.setOnClickListener(this);
        mPersonalTv_3.setOnClickListener(this);

        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mAllAgreeBtn.setOnClickListener(this);

    }

    /**
     * 인증 가능한지 체크
     * @return bool ( true - 인증가능, false - 인증 불가 )
     */
    public boolean invaildCerfiti(){
        boolean bool = true;


        if(!mPersonalCb_1.isChecked() || !mPersonalCb_2.isChecked()|| !mPersonalCb_3.isChecked()){    // 약관 동의가 안되어 있다면
            mDialog =   new CustomAlertDialog(AgreeConfirmActivity.this, CustomAlertDialog.TYPE_A);
            mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));;
            mDialog.setContent(getString(R.string.popup_dialog_terms_error));
            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
            mDialog.show();
            bool = false;
            return bool;
        }



        return bool;

    }

    /**
     * * 회원인증 가능한지 체크
    * @return boolean ( true - 저장 가능, false - 저장 불가 )
    */
    public boolean isConfirm(){
        boolean bool = true;


        if(!mPersonalCb_1.isChecked() || !mPersonalCb_2.isChecked()|| !mPersonalCb_3.isChecked()){    // 약관 동의가 안되어 있다면
            bool = false;
            return bool;
        }

        return bool;
    }



    /**
     * 동의api
     *
     */
    public void requestAgreeComfirm() {

//        {"api_code":"login_agreement_yn" ,"insures_code":"108" ,"mber_sn":"21182" ,"mber_agreement_yn":"Y"  }

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, "login_agreement_yn");
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
            object.put(CommonData.JSON_MBER_SN,     commonData.getMberSn());
            object.put("mber_agreement_yn",     "Y");

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(AgreeConfirmActivity.this, NetworkConst.NET_LOGIN_AGREEMENT_YN, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgressLayout());
        }catch(Exception e){
            GLog.i(e.toString(), "dd");
        }

    }



    @Override
    public void onClick(View v) {

        Intent intent = null;

        int id = v.getId();
        if (id == R.id.personal_terms_1_tv) {  // 개인정보 제공 동의
            intent = new Intent(AgreeConfirmActivity.this, BackWebViewActivity.class);
            intent.putExtra(CommonData.EXTRA_URL, commonData.PERSONAL_TERMS_1_URL);
            intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.personal_terms_1));
            startActivity(intent);
            Util.BackAnimationStart(AgreeConfirmActivity.this);
        } else if (id == R.id.personal_terms_2_tv) {  // 개인민감정보 제공 동의
            intent = new Intent(AgreeConfirmActivity.this, BackWebViewActivity.class);
            intent.putExtra(CommonData.EXTRA_URL, commonData.PERSONAL_TERMS_2_URL);
            intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.personal_terms_2));
            startActivity(intent);
            Util.BackAnimationStart(AgreeConfirmActivity.this);
        } else if (id == R.id.personal_terms_3_tv) {  // 개인정보 제3자 제공 동의
            intent = new Intent(AgreeConfirmActivity.this, BackWebViewActivity.class);
            intent.putExtra(CommonData.EXTRA_URL, commonData.PERSONAL_TERMS_3_URL);
            intent.putExtra(CommonData.EXTRA_ACTIVITY_TITLE, getString(R.string.personal_terms_3));
            startActivity(intent);
            Util.BackAnimationStart(AgreeConfirmActivity.this);
        } else if (id == R.id.confirm_btn) { // 인증 버튼
            GLog.i("certifi_btn", "dd");

            if (invaildCerfiti()) {   // 인증 가능하다면
                requestAgreeComfirm();
            }
//        } else if (id == R.id.cancel_btn) {
//            commonData.setMberPwd(null);
//            commonData.setMain_Category("");
//            commonData.setAutoLogin(false);
//            commonData.setRememberId(false);
//            commonData.setMberSn("");
//
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_bef_cm", "");
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_bef_kg", "");
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_mber_kg", "");
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_mber_term_kg", "");
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_mber_chl_birth_de", "");
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_mber_milk_yn", "");
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_mber_birth_due_de", "");
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_mber_chl_typ", "");
//            Util.setSharedPreference(AgreeConfirmActivity.this, "MonJin_actqy", "");
//
//
//            commonData.setLoginType(CommonData.LOGIN_TYPE_PARENTS);
//            finish();
//
//            Intent intent2 = new Intent(AgreeConfirmActivity.this, LoginActivity.class);
//            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent2);
        } else if (id == R.id.agree_btn) {
            mPersonalCb_1.setChecked(true);
            mPersonalCb_2.setChecked(true);
            mPersonalCb_3.setChecked(true);
        }
    }



    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            if (getProgressLayout() != null)
                getProgressLayout().setVisibility(View.GONE);

            switch ( type ) {
                case NetworkConst.NET_LOGIN_AGREEMENT_YN:   // 회원인증 체크
                   switch ( resultCode ) {
                        case CommonData.API_SUCCESS:

                            try {

                               String data_yn = resultData.getString(CommonData.JSON_REG_YN);
                                if(data_yn.equals(CommonData.YES)){
                                    Intent intent = null;
                                    intent = new Intent(AgreeConfirmActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    activityClear();
                                    finish();

                                }else{  // 현대해상 미가입자
                                    mDialog =   new CustomAlertDialog(AgreeConfirmActivity.this, CustomAlertDialog.TYPE_A);
                                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                                    mDialog.setContent("잠시후 다시 시도해 주세요");
                                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(CustomAlertDialog dialog, Button button) {
                                            dialog.dismiss();
                                        }
                                    });
                                    mDialog.show();

                                }


                            } catch ( Exception e ) {
                                e.printStackTrace();

                                mDialog =   new CustomAlertDialog(AgreeConfirmActivity.this, CustomAlertDialog.TYPE_A);
                                mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                                mDialog.setContent("잠시후 다시 시도해 주세요");
                                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(CustomAlertDialog dialog, Button button) {
                                        dialog.dismiss();
                                    }
                                });
                                mDialog.show();
                            }


                            break;


                        default:
                            if(dialog != null){
                                dialog.show();
                            }

                            break;
                    }
                    break;
            }
            hideProgress();
        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            hideProgress();
            if (getProgressLayout() != null)
                getProgressLayout().setVisibility(View.GONE);
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            hideProgress();
            if (getProgressLayout() != null)
                getProgressLayout().setVisibility(View.GONE);
            dialog.show();

        }
    };

}
