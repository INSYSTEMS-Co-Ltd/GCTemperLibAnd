package com.greencross.gctemperlib.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.RequestApi;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.R;
//import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jihoon on 2016-03-28.
 * 알림설정 클래스
 *
 * @since 0, 1
 */
public class AlarmSettingActivity extends BackBaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView mBackImg;
    private Switch mPushCb_1, mPushCb_2, mPushCb_3, mPushCb_4, mPushCb_5, mPushCb_6, mPushCb_7, mBellCb, mVibCb;

    // 1 : 체온 측정, 2: 열지도 , 3: 다이어트, 4 : 유행설 질환 5 : 알림게시판 , 6 : 이벤트 및 건강뉴스, 7: 게시글 좋아요 / 댓글
    private boolean mCurrentPush_1, mCurrentPush_2, mCurrentPush_3, mCurrentPush_4, mCurrentPush_5, mCurrentPush_6, mCurrentPush_7, mCurrentBell, mCurrentVib;

    private boolean event_news_push = false;
    private boolean event_news_push1 = false;
    private boolean event_news_push2 = false;
    private boolean heat_map_push = false;

    private boolean mPastPush_2, mPastPush_3, mPastPush_4, mPastPush_6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting_activity);

        setTitle(getString(R.string.setting_alarm));

        init();
        setEvent();

        // 변경하기전 비교값
        mCurrentPush_1 = commonData.getPushAlarm(); // 체온알림여부
        mCurrentPush_2 = commonData.getMapPushAlarm(); // 열지도
        mCurrentPush_3 = commonData.getDietPushAlarm(); //다이어트
        mCurrentPush_4 = commonData.getDisease_alert_yn(); //유행성질
        mCurrentPush_5 = commonData.getNotityPushAlarm(); //알림게시판
        mCurrentPush_6 = commonData.getEvent_alert_yn(); //이벤트 및 건강뉴스
        mCurrentPush_7 = commonData.getReplay_alert_yn(); //게시글, 댓글 알림 여부

        mPastPush_2 = commonData.getMapPushAlarm(); // 열지도
        mPastPush_6 = commonData.getEvent_alert_yn(); //이벤트 및 건강뉴스
        mPastPush_3 = commonData.getDietPushAlarm(); // 다이어트
        mPastPush_4 = commonData.getDisease_alert_yn(); //유행성질

        if (commonData.getMberGrad().equals("20")) {
            mCurrentPush_3 = false;
            mCurrentPush_4 = false;

            mPushCb_3.setChecked(false);
            mPushCb_4.setChecked(false);

            mPushCb_3.setEnabled(false);
            mPushCb_4.setEnabled(false);
        } else {
            mPushCb_3.setEnabled(true);
            mPushCb_4.setEnabled(true);
        }


        mCurrentBell = commonData.getAlarmMode() == CommonData.PUSH_MODE_BELL || commonData.getAlarmMode() == CommonData.PUSH_MODE_BELL_VIBRATE ? true : false;
        mCurrentVib = commonData.getAlarmMode() == CommonData.PUSH_MODE_VIBRATE || commonData.getAlarmMode() == CommonData.PUSH_MODE_BELL_VIBRATE ? true : false;

        if (!mCurrentPush_1 && !mCurrentPush_2 && !mCurrentPush_3 && !mCurrentPush_4 && !mCurrentPush_5 && !mCurrentPush_6 && !mCurrentPush_7) {
            mPushCb_1.setChecked(false);
            mPushCb_2.setChecked(false);
            mPushCb_3.setChecked(false);
            mPushCb_4.setChecked(false);
            mPushCb_5.setChecked(false);
            mPushCb_6.setChecked(false);
            mBellCb.setChecked(false);
            mVibCb.setChecked(false);

            mBellCb.setEnabled(false);
            mVibCb.setEnabled(false);
        } else {
            if (mCurrentPush_1)  // 체온 측정 알람
                mPushCb_1.setChecked(true);

            if (mCurrentPush_2)  // 열지도 알람
                mPushCb_2.setChecked(true);

            if (commonData.getMberGrad().equals("20")) { // 다이어트 알람
                mPushCb_3.setChecked(false);
                mPushCb_3.setEnabled(false);
            } else {
                if (mCurrentPush_3)
                    mPushCb_3.setChecked(true);

                mPushCb_3.setEnabled(true);
            }

            if (commonData.getMberGrad().equals("20")) { // 유행설 질환 알람
                mPushCb_4.setChecked(false);
                mPushCb_4.setEnabled(false);
            } else {
                if (mCurrentPush_4)
                    mPushCb_4.setChecked(true);

                mPushCb_4.setEnabled(true);
            }

            if (mCurrentPush_5)  // 알림게시판 알람
                mPushCb_5.setChecked(true);

            if (mCurrentPush_6)  // 이벤트 및 건강뉴스 알람
                mPushCb_6.setChecked(true);

            if (mCurrentPush_7)  // 게시글 좋아요 / 댓글 알람
                mPushCb_7.setChecked(true);

            if (mCurrentBell)  // 벨소리를 설정했다면
                mBellCb.setChecked(true);

            if (mCurrentVib) // 진동을 설정했다면
                mVibCb.setChecked(true);

        }

    }

    /**
     * 초기화
     */
    public void init() {

        mBackImg = getBackImg();

        mPushCb_1 = (Switch) findViewById(R.id.push_alarm_cb_1);
        mPushCb_2 = (Switch) findViewById(R.id.push_alarm_cb_2);
        mPushCb_3 = (Switch) findViewById(R.id.push_alarm_cb_3);
        mPushCb_4 = (Switch) findViewById(R.id.push_alarm_cb_4);
        mPushCb_5 = (Switch) findViewById(R.id.push_alarm_cb_5);
        mPushCb_6 = (Switch) findViewById(R.id.push_alarm_cb_6);
        mPushCb_7 = (Switch) findViewById(R.id.push_alarm_cb_7);
        mBellCb = (Switch) findViewById(R.id.bell_cb);
        mVibCb = (Switch) findViewById(R.id.vibrate_cb);

    }

    /**
     * 이벤트 연결
     */
    public void setEvent() {
        mBackImg.setOnClickListener(this);
        mPushCb_1.setOnCheckedChangeListener(this);
        mPushCb_2.setOnCheckedChangeListener(this);
        mPushCb_3.setOnCheckedChangeListener(this);
        mPushCb_4.setOnCheckedChangeListener(this);
        mPushCb_5.setOnCheckedChangeListener(this);
        mPushCb_6.setOnCheckedChangeListener(this);
        mPushCb_7.setOnCheckedChangeListener(this);
        mBellCb.setOnCheckedChangeListener(this);
        mVibCb.setOnCheckedChangeListener(this);
    }

    /**
     * 알림 설정 변경값이 있는지 체크
     *
     * @return boolean ( true - 변경, false - 변경하지 않음 )
     */
    public boolean isChange() {
        boolean change = false;

        if (mCurrentPush_1 != mPushCb_1.isChecked()) {    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        if (mCurrentPush_2 != mPushCb_2.isChecked()) {    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        if (mCurrentPush_3 != mPushCb_3.isChecked()) {    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        if (mCurrentPush_4 != mPushCb_4.isChecked()) {    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        if (mCurrentPush_5 != mPushCb_5.isChecked()) {    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        if (mCurrentPush_6 != mPushCb_6.isChecked()) {    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        if (mCurrentPush_7 != mPushCb_7.isChecked()) {    // 푸시설정값을 변경했다면
            change = true;
            return change;
        }

        if (mCurrentBell != mBellCb.isChecked()) {    // 벨소리 설정값을 변경했다면
            change = true;
            return change;
        }

        if (mCurrentVib != mVibCb.isChecked()) {  // 진동 설정값을 변경했다면
            change = true;
            return change;
        }


        return change;

    }

    /**
     * 알림 설정
     */
    public void requestAlarmSetting(String push_1, String fx_mth, String push_2, String push_3, String push_4, String push_5, String push_6, String push_7) {
        GLog.i("requestAlarmSetting", "dd");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_CALL_REG_FX_ADD);
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
            object.put(CommonData.JSON_MBER_SN, commonData.getMberSn());
            object.put(CommonData.JSON_FX_YN, push_1);
            object.put(CommonData.JSON_FX_MTH, fx_mth);
            object.put(CommonData.JSON_HEAT_YN, push_2);
            object.put(CommonData.JSON_DIET_YN, push_3);
            object.put(CommonData.JSON_DISEASE_ALERT_YN, push_4);
            object.put(CommonData.JSON_NOTITY_YN, push_5);
            object.put(CommonData.JSON_EVENT_ALERT_YN, push_6);
            object.put(CommonData.JSON_REPLAY_ALERT_YN, push_7);

            object.put(CommonData.JSON_NEWS_YN, "N");
            object.put(CommonData.JSON_NOTICE_YN, "N");


            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(AlarmSettingActivity.this, NetworkConst.NET_CALL_REG_FX_ADD, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }


    /**
     * 마케팅 정보 및 위치정보 동의
     */
    public void requestAgreeAlarmSetting() {
        GLog.i("requestAgreeAlarmSetting", "dd");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_MBER_CHECK_AGREE_YN);
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
            object.put(CommonData.JSON_MBER_SN, commonData.getMberSn());

            if (event_news_push || event_news_push1 || event_news_push2)
                object.put(CommonData.JSON_MARKETING_YN, "Y");
            else
                object.put(CommonData.JSON_MARKETING_YN, "");

            if (heat_map_push)
                object.put(CommonData.JSON_LOCATION_YN, "Y");
            else
                object.put(CommonData.JSON_LOCATION_YN, "");

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(AlarmSettingActivity.this, NetworkConst.NET_MBER_CHECK_AGREE_YN, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    @Override
    public void onBackPressed() {

        if (event_news_push || event_news_push1 || event_news_push2 || heat_map_push) {
            GLog.i("개인정보 마케팅 또는 위치정보 수집 동의 변경", "dd");
            requestAgreeAlarmSetting();

        } else {
            if (isChange()) {

                // 알림설정 api 호출하자
                String push_1;
                String push_2;
                String push_3;
                String push_4;
                String push_5;
                String push_6;
                String push_7;
                String bell_or_vib;


                if (mPushCb_1.isChecked()) {    // 체온 푸시알림 설정 유무
                    push_1 = CommonData.YES;
                } else {
                    push_1 = CommonData.NO;
                }

                if (mPushCb_2.isChecked()) {    // 열지 푸시알림 설정 유무
                    push_2 = CommonData.YES;
                } else {
                    push_2 = CommonData.NO;
                }

                if (mPushCb_3.isChecked()) {    // 다이어트 푸시알림 설정 유무
                    push_3 = CommonData.YES;
                } else {
                    push_3 = CommonData.NO;
                }

                if (mPushCb_4.isChecked()) {    // 유행성 질환 푸시알림 설정 유무
                    push_4 = CommonData.YES;
                } else {
                    push_4 = CommonData.NO;
                }

                if (mPushCb_5.isChecked()) {    // 알림게시판 설정 유무
                    push_5 = CommonData.YES;
                } else {
                    push_5 = CommonData.NO;
                }

                if (mPushCb_6.isChecked()) {    // 이벤트 및 건강뉴스 설정 유무
                    push_6 = CommonData.YES;
                } else {
                    push_6 = CommonData.NO;
                }

                if (mPushCb_7.isChecked()) {    // 게시글 좋아요 / 댓글 설정 유무
                    push_7 = CommonData.YES;
                } else {
                    push_7 = CommonData.NO;
                }

                if (mBellCb.isChecked() && !mVibCb.isChecked()) {
                    GLog.i("벨소리 설정 완료", "dd");
                    bell_or_vib = "1";
                } else if (mVibCb.isChecked() && !mBellCb.isChecked()) {
                    GLog.i("진동 설정 완료", "dd");
                    bell_or_vib = "2";
                } else if (mVibCb.isChecked() && mBellCb.isChecked()) {
                    GLog.i("벨 진동 모두 설정", "dd");
                    bell_or_vib = "3";
                } else {
                    GLog.i("무음", "dd");
                    bell_or_vib = "0";
                }

                requestAlarmSetting(push_1, bell_or_vib, push_2, push_3, push_4, push_5, push_6, push_7);

            } else {
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.common_left_btn) {  // 뒤로가기

            if (event_news_push || event_news_push1 || event_news_push2 || heat_map_push) {
                GLog.i("개인정보 마케팅 또는 위치정보 수집 동의 변경", "dd");
                requestAgreeAlarmSetting();

            } else {
                if (isChange()) {

                    // 알림설정 api 호출하자
                    String push_1;
                    String push_2;
                    String push_3;
                    String push_4;
                    String push_5;
                    String push_6;
                    String push_7;
                    String bell_or_vib;


                    if (mPushCb_1.isChecked()) {    // 체온 푸시알림 설정 유무
                        push_1 = CommonData.YES;
                    } else {
                        push_1 = CommonData.NO;
                    }

                    if (mPushCb_2.isChecked()) {    // 열지 푸시알림 설정 유무
                        push_2 = CommonData.YES;
                    } else {
                        push_2 = CommonData.NO;
                    }

                    if (mPushCb_3.isChecked()) {    // 다이어트 푸시알림 설정 유무
                        push_3 = CommonData.YES;
                    } else {
                        push_3 = CommonData.NO;
                    }

                    if (mPushCb_4.isChecked()) {    // 유행성 질환 푸시알림 설정 유무
                        push_4 = CommonData.YES;
                    } else {
                        push_4 = CommonData.NO;
                    }

                    if (mPushCb_5.isChecked()) {    // 알림게시판 설정 유무
                        push_5 = CommonData.YES;
                    } else {
                        push_5 = CommonData.NO;
                    }

                    if (mPushCb_6.isChecked()) {    // 이벤트 및 건강뉴스 설정 유무
                        push_6 = CommonData.YES;
                    } else {
                        push_6 = CommonData.NO;
                    }

                    if (mPushCb_7.isChecked()) {    // 게시글 좋아요 / 댓글 설정 유무
                        push_7 = CommonData.YES;
                    } else {
                        push_7 = CommonData.NO;
                    }

                    if (mBellCb.isChecked() && !mVibCb.isChecked()) {
                        GLog.i("벨소리 설정 완료", "dd");
                        bell_or_vib = "1";
                    } else if (mVibCb.isChecked() && !mBellCb.isChecked()) {
                        GLog.i("진동 설정 완료", "dd");
                        bell_or_vib = "2";
                    } else if (mVibCb.isChecked() && mBellCb.isChecked()) {
                        GLog.i("벨 진동 모두 설정", "dd");
                        bell_or_vib = "3";
                    } else {
                        GLog.i("무음", "dd");
                        bell_or_vib = "0";
                    }

                    requestAlarmSetting(push_1, bell_or_vib, push_2, push_3, push_4, push_5, push_6, push_7);

                } else {
                    finish();
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        GLog.i("onCheckedChanged", "dd");
        int id = buttonView.getId();

        if (id == R.id.push_alarm_cb_1) {    // 체온 푸시알림
            mPushCb_1.setChecked(isChecked);
            setAlarmOnOff();
        } else if (id == R.id.push_alarm_cb_2) {    // 열지도 푸시알림
            if (!mPastPush_2 && isChecked) {
                if (commonData.getAddressDo().length() > 0 && commonData.getAddressGu().length() > 0) {
                    mPushCb_2.setChecked(isChecked);
                    heat_map_push = true;
                    setAlarmOnOff();
                } else {
                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(AlarmSettingActivity.this, CustomAlertDialog.TYPE_B);
                    customAlertDialog.setTitle(getString(R.string.app_name_kr));
                    customAlertDialog.setContent(getString(R.string.popup_setting_address));
                    customAlertDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), (dialog, button) -> {
                        mPushCb_2.setChecked(false);
                        heat_map_push = false;
                        dialog.dismiss();
                    });
                    customAlertDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                        Intent intent = new Intent(AlarmSettingActivity.this, SettingAddressActivity.class);
                        startActivityForResult(intent, CommonData.REQUEST_ADDRESS_SETTING);
                        dialog.dismiss();
                    });
                    customAlertDialog.show();
                }
            } else {
                heat_map_push = false;
            }
            mPastPush_2 = isChecked;
        } else if (id == R.id.push_alarm_cb_3) {    // 다이어트 푸시알림
            if (!mPastPush_3 && isChecked) {
                mPushCb_3.setChecked(isChecked);
                event_news_push1 = true;
                setAlarmOnOff();
            } else {
                event_news_push1 = false;
            }
            mPastPush_3 = isChecked;
        } else if (id == R.id.push_alarm_cb_4) {    // 유행성 질환 푸시알림
            if (!mPastPush_4 && isChecked) {
                mPushCb_4.setChecked(isChecked);
                event_news_push2 = true;
                setAlarmOnOff();
            } else {
                event_news_push2 = false;
            }
            mPastPush_4 = isChecked;
        } else if (id == R.id.push_alarm_cb_5) {    // 알림게시판 푸시알림
            mPushCb_5.setChecked(isChecked);
            setAlarmOnOff();
        } else if (id == R.id.push_alarm_cb_6) {    // 이벤트 및 건강뉴스 푸시알림

            if (!mPastPush_6 && isChecked) {
                mPushCb_6.setChecked(isChecked);
                event_news_push = true;

                CustomAlertDialog customAlertDialog = new CustomAlertDialog(AlarmSettingActivity.this, CustomAlertDialog.TYPE_A);
                customAlertDialog.setTitle("");
                customAlertDialog.setContent(getString(R.string.event_news_info));
                customAlertDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
                    @Override
                    public void onClick(CustomAlertDialog dialog, Button button) {
                        setAlarmOnOff();
                        dialog.dismiss();
                    }
                });
                customAlertDialog.show();
            } else {
                event_news_push = false;
            }
            mPastPush_6 = isChecked;
        } else if (id == R.id.push_alarm_cb_7) {    // 게시글 좋아요 / 댓글 푸시알림
            mPushCb_7.setChecked(isChecked);
            setAlarmOnOff();
        } else if (id == R.id.bell_cb) {  // 벨소리
            mBellCb.setChecked(isChecked);
        } else if (id == R.id.vibrate_cb) {   // 진동
            mVibCb.setChecked(isChecked);
        }

    }

    public void setAlarmOnOff() {
        if (!mPushCb_1.isChecked() && !mPushCb_2.isChecked() && !mPushCb_3.isChecked() && !mPushCb_4.isChecked() && !mPushCb_5.isChecked() && !mPushCb_6.isChecked() && !mPushCb_7.isChecked()) {
            mBellCb.setChecked(false);
            mVibCb.setChecked(false);

            mBellCb.setEnabled(false);
            mVibCb.setEnabled(false);
        } else {
            mBellCb.setEnabled(true);
            mVibCb.setEnabled(true);
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
                case NetworkConst.NET_CALL_REG_FX_ADD:                                    // 알림 설정

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");

                            commonData.setPushAlarm(mPushCb_1.isChecked());
                            commonData.setMapPushAlarm(mPushCb_2.isChecked());
                            commonData.setDietPushAlarm(mPushCb_3.isChecked());
                            commonData.setDisease_alert_yn(mPushCb_4.isChecked());
                            commonData.setNotityPushAlarm(mPushCb_5.isChecked());
                            commonData.setEvent_alert_yn(mPushCb_6.isChecked());
                            commonData.setReplay_alert_yn(mPushCb_7.isChecked());

                            if (mBellCb.isChecked() && mVibCb.isChecked()) {  // 벨+진동
                                commonData.setAlarmMode(CommonData.PUSH_MODE_BELL_VIBRATE);
                            } else if (mBellCb.isChecked()) {  // 벨
                                commonData.setAlarmMode(CommonData.PUSH_MODE_BELL);
                            } else if (mVibCb.isChecked()) {   // 진동
                                commonData.setAlarmMode(CommonData.PUSH_MODE_VIBRATE);
                            } else {
                                commonData.setAlarmMode(0);
                            }


                            setResult(RESULT_OK);
                            finish();


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

                case NetworkConst.NET_MBER_CHECK_AGREE_YN:

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");

                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN);
                                if (data_yn.equals(CommonData.YES)) {
                                    commonData.setMarketing_yn(resultData.getString(CommonData.JSON_MARKETING_YN));
                                    commonData.setLocation_yn(resultData.getString(CommonData.JSON_LOCATION_YN));
                                    CommonData.getInstance(AlarmSettingActivity.this).setEvent_alert_yn(resultData.getString(CommonData.JSON_EVENT_ALERT_YN).equals(CommonData.YES) ? true : false);
                                    CommonData.getInstance(AlarmSettingActivity.this).setMapPushAlarm(resultData.getString(CommonData.JSON_HEAT_YN).equals(CommonData.YES) ? true : false);
                                    CommonData.getInstance(AlarmSettingActivity.this).setDietPushAlarm(resultData.getString(CommonData.JSON_DIET_YN).equals(CommonData.YES) ? true : false);
                                    CommonData.getInstance(AlarmSettingActivity.this).setDisease_alert_yn(resultData.getString(CommonData.JSON_DISEASE_ALERT_YN).equals(CommonData.YES) ? true : false);

                                    if (isChange()) {

                                        // 알림설정 api 호출하자
                                        String push_1;
                                        String push_2;
                                        String push_3;
                                        String push_4;
                                        String push_5;
                                        String push_6;
                                        String push_7;
                                        String bell_or_vib;


                                        if (mPushCb_1.isChecked()) {    // 체온 푸시알림 설정 유무
                                            push_1 = CommonData.YES;
                                        } else {
                                            push_1 = CommonData.NO;
                                        }

                                        if (mPushCb_2.isChecked()) {    // 열지 푸시알림 설정 유무
                                            push_2 = CommonData.YES;
                                        } else {
                                            push_2 = CommonData.NO;
                                        }

                                        if (mPushCb_3.isChecked()) {    // 다이어트 푸시알림 설정 유무
                                            push_3 = CommonData.YES;
                                        } else {
                                            push_3 = CommonData.NO;
                                        }

                                        if (mPushCb_4.isChecked()) {    // 유행성 질환 푸시알림 설정 유무
                                            push_4 = CommonData.YES;
                                        } else {
                                            push_4 = CommonData.NO;
                                        }

                                        if (mPushCb_5.isChecked()) {    // 알림게시판 설정 유무
                                            push_5 = CommonData.YES;
                                        } else {
                                            push_5 = CommonData.NO;
                                        }

                                        if (mPushCb_6.isChecked()) {    // 이벤트 및 건강뉴스 설정 유무
                                            push_6 = CommonData.YES;
                                        } else {
                                            push_6 = CommonData.NO;
                                        }

                                        if (mPushCb_7.isChecked()) {    // 게시글 좋아요 / 댓글 설정 유무
                                            push_7 = CommonData.YES;
                                        } else {
                                            push_7 = CommonData.NO;
                                        }

                                        if (mBellCb.isChecked() && !mVibCb.isChecked()) {
                                            GLog.i("벨소리 설정 완료", "dd");
                                            bell_or_vib = "1";
                                        } else if (mVibCb.isChecked() && !mBellCb.isChecked()) {
                                            GLog.i("진동 설정 완료", "dd");
                                            bell_or_vib = "2";
                                        } else if (mVibCb.isChecked() && mBellCb.isChecked()) {
                                            GLog.i("벨 진동 모두 설정", "dd");
                                            bell_or_vib = "3";
                                        } else {
                                            GLog.i("무음", "dd");
                                            bell_or_vib = "0";
                                        }

                                        requestAlarmSetting(push_1, bell_or_vib, push_2, push_3, push_4, push_5, push_6, push_7);

                                    } else {
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        GLog.i("requestCode = " + requestCode, "dd");
        GLog.i("resultCode = " + resultCode, "dd");
        GLog.i("data = " + data, "dd");

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            mPushCb_2.setChecked(false);
            return;
        } else {
            heat_map_push = true;
        }
    }

}
