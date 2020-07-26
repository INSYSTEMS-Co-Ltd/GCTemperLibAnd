package com.greencross.gctemperlib.temper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.CustomTextWatcher;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.RequestApi;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class RemedyInputActivity extends BackBaseActivity implements View.OnClickListener {

    LinearLayout mInputLay;
    TextView mCheckDateEdt, mCheckTimeEdt, mTxtUnitRemedy;
    ImageButton mConfirmBtn;
    ImageView[] mBtnRemedyTypes;
    EditText mRemedyEatVolumeEdt;
    TextView mRemedyKindSel;
    Button mBtnRemedyInfo;

    public CustomAlertDialog mDialog;

    private String mCheckDate;
    private Date mCurDate;
    GregorianCalendar mCalendar;

    int mRemedyType = -1;    // 해열제 타입 : 물약 알약 가루약 좌약
    int mRemedyKind = -1;    // 해열제 성분 : 아세트아미노펜, 부루펜, 맥시부펜

    String[] mRemedyKinds;  //  해열제 성분 리스트
    String[] mSuppositoryKinds; // 좌약 성분 리스트

    int[] mImgRemedyBtnOff = {R.drawable.btn_med_liquid, R.drawable.btn_med_pill, R.drawable.btn_med_powder, R.drawable.btn_med_sup};
    int[] mImgRemedyBtnOn = {R.drawable.btn_med_liquid_sel, R.drawable.btn_med_pill_sel, R.drawable.btn_med_powder_sel, R.drawable.btn_med_sup_sel};

    double mCurVolume;

    Intent mIntent;

    String mRemedySn;
    String mIsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remedy_record_input);

        mIntent = getIntent();

        setTitle(getString(R.string.remedy_input));

        init();
        setEvent();
        try {
            initView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 초기화
     */
    public void init(){
        mCheckDateEdt = (TextView)findViewById(R.id.check_date_edt);
        mCheckTimeEdt = (TextView)findViewById(R.id.check_time_edt);
        mConfirmBtn = (ImageButton)findViewById(R.id.confirm_btn);

        mInputLay = (LinearLayout)findViewById(R.id.input_lay);

        mBtnRemedyTypes = new ImageView[4];

        mBtnRemedyTypes[0] = (ImageView)findViewById(R.id.btn_remedy_type_1);
        mBtnRemedyTypes[1] = (ImageView)findViewById(R.id.btn_remedy_type_2);
        mBtnRemedyTypes[2] = (ImageView)findViewById(R.id.btn_remedy_type_3);
        mBtnRemedyTypes[3] = (ImageView)findViewById(R.id.btn_remedy_type_4);

        mRemedyKindSel = (TextView)findViewById(R.id.remedy_kind_sel);
        mRemedyEatVolumeEdt = (EditText)findViewById(R.id.remedy_eat_volume_edt);
        mTxtUnitRemedy = (TextView)findViewById(R.id.txt_unit_remedy);

        mBtnRemedyInfo = (Button)findViewById(R.id.btn_remedy_info);

        mRemedyEatVolumeEdt.addTextChangedListener(new CustomTextWatcher(RemedyInputActivity.this, mRemedyEatVolumeEdt, mTxtUnitRemedy));

        mRemedyKinds = getResources().getStringArray(R.array.remedy_kind_list);
        mSuppositoryKinds = getResources().getStringArray(R.array.suppository_list);

        mTxtUnitRemedy.setVisibility(View.GONE);
    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){
        mInputLay.setOnClickListener(this);

        mConfirmBtn.setOnClickListener(this);
        mCheckDateEdt.setOnClickListener(this);
        mCheckTimeEdt.setOnClickListener(this);

        mRemedyKindSel.setOnClickListener(this);
        mBtnRemedyTypes[0].setOnClickListener(this);
        mBtnRemedyTypes[1].setOnClickListener(this);
        mBtnRemedyTypes[2].setOnClickListener(this);
        mBtnRemedyTypes[3].setOnClickListener(this);

        mBtnRemedyInfo.setOnClickListener(this);
    }

    public void initView() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
        mCalendar = new GregorianCalendar();

        mIsEdit = mIntent.getStringExtra(CommonData.EXTRA_IS_EDIT);

        if( mIsEdit != null && mIsEdit.equals(CommonData.YES)){     //  수정
            setRemedyType(Integer.parseInt(mIntent.getStringExtra(CommonData.EXTRA_TYPE)));
            mRemedyEatVolumeEdt.setText(mIntent.getStringExtra(CommonData.EXTRA_VOLUME));
            mRemedyKind = Integer.parseInt(mIntent.getStringExtra(CommonData.EXTRA_KIND));
            if(mRemedyType == 3){
                mRemedyKindSel.setText(mSuppositoryKinds[mRemedyKind]);
            }else{
                mRemedyKindSel.setText(mRemedyKinds[mRemedyKind]);
            }
            mCheckDate = mIntent.getStringExtra(CommonData.EXTRA_DATE);
            mRemedySn = mIntent.getStringExtra(CommonData.EXTRA_SN);

            mCurDate = format.parse(mCheckDate);
        }else{                                  // 신규
            mCurDate = new Date();
            mCheckDate = format.format(mCurDate);
            setRemedyType(0);
        }
        format = new SimpleDateFormat(CommonData.PATTERN_DATE_KR);
        mCheckDateEdt.setText( format.format(mCurDate));
        format = new SimpleDateFormat(CommonData.PATTERN_TIME);
        mCheckTimeEdt.setText( format.format(mCurDate));
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        int id = v.getId();
        if (id == R.id.input_lay) {
            InputMethodManager imm = (InputMethodManager) getSystemService(RemedyInputActivity.this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mRemedyEatVolumeEdt.getWindowToken(), 0);
        } else if (id == R.id.check_date_edt) {
            try {
                if (mCurDate == null) {
                    mCurDate = new Date();
                }
                mCalendar.setTime(mCurDate);
                int nNowYear = mCalendar.get(Calendar.YEAR);
                int nNowMonth = mCalendar.get(Calendar.MONTH);
                int nNowDay = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog alert = new DatePickerDialog(RemedyInputActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        Date checkDate = new Date();
                        if (mCalendar.getTime().compareTo(checkDate) >= 0) {    // 오늘 지남
                            Toast.makeText(RemedyInputActivity.this, getString(R.string.over_time), Toast.LENGTH_LONG).show();
                            return;
                        }
                        mCurDate = mCalendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
                        mCheckDate = format.format(mCurDate);
                        format = new SimpleDateFormat(CommonData.PATTERN_DATE_KR);
                        //GLog.i("mCheckDate---> " + mCheckDate);
                        mCheckDateEdt.setText(format.format(mCurDate));
                    }
                }, nNowYear, nNowMonth, nNowDay);

                alert.setCancelable(false);

                alert.show();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else if (id == R.id.check_time_edt) {
            try {
                if (mCurDate == null) {
                    mCurDate = new Date();
                }
                mCalendar.setTime(mCurDate);
                int nHourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);
                int nMinute = mCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(RemedyInputActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        Date checkDate = new Date();
                        if (mCalendar.getTime().compareTo(checkDate) >= 0) {    // 오늘 지남
                            Toast.makeText(RemedyInputActivity.this, getString(R.string.over_time), Toast.LENGTH_LONG).show();
                            return;
                        }
                        mCurDate = mCalendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_TIME);
                        mCheckTimeEdt.setText(format.format(mCurDate));
                        format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
                        mCheckDate = format.format(mCurDate);
                    }
                }, nHourOfDay, nMinute, false);

                timePickerDialog.setCancelable(false);

                timePickerDialog.show();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else if (id == R.id.btn_remedy_type_1) {
            if (mRemedyType != 0) {
                setRemedyType(0);
                mRemedyKind = -1;
                mRemedyKindSel.setText("");
            }
        } else if (id == R.id.btn_remedy_type_2) {
            if (mRemedyType != 1) {
                setRemedyType(1);
                mRemedyKind = -1;
                mRemedyKindSel.setText("");
            }
        } else if (id == R.id.btn_remedy_type_3) {
            if (mRemedyType != 2) {
                setRemedyType(2);
                mRemedyKind = -1;
                mRemedyKindSel.setText("");
            }
        } else if (id == R.id.btn_remedy_type_4) {
            if (mRemedyType != 3) {
                setRemedyType(3);
                mRemedyKind = -1;
                mRemedyKindSel.setText("");
            }
        } else if (id == R.id.remedy_kind_sel) {
            if (mRemedyType < 0) {
                Toast.makeText(RemedyInputActivity.this, getString(R.string.empty_type), Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder ab = new AlertDialog.Builder(RemedyInputActivity.this);
                if (mRemedyType == 3) {       // 좌약
                    ab.setSingleChoiceItems(mSuppositoryKinds, mRemedyKind,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 각 리스트를 선택했을때
                                    mRemedyKind = whichButton;
                                    mRemedyKindSel.setText(mSuppositoryKinds[whichButton]);
                                    dialog.dismiss();
                                }
                            });
                } else {
                    ab.setSingleChoiceItems(mRemedyKinds, mRemedyKind,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 각 리스트를 선택했을때
                                    mRemedyKind = whichButton;
                                    mRemedyKindSel.setText(mRemedyKinds[whichButton]);
                                    dialog.dismiss();
                                }
                            });
                }
                ab.show();
            }
        } else if (id == R.id.btn_remedy_info) {
            mDialog = new CustomAlertDialog(RemedyInputActivity.this, CustomAlertDialog.TYPE_A);
            mDialog.setTitle(getString(R.string.popup_remedy_info));
            mDialog.setContent(getString(R.string.remedy_list_1) + "\n\n" + getString(R.string.remedy_list_2) + "\n\n" + getString(R.string.remedy_list_3));
            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    dialog.dismiss();
                }
            });
            mDialog.show();
        } else if (id == R.id.confirm_btn) {
            if (isConfirm()) {
                double above_volume;
                mCurVolume = Double.parseDouble(mRemedyEatVolumeEdt.getText().toString());

                if (mRemedyKind == 0)
                    above_volume = TemperMainActivity.max_reducer_1 - TemperMainActivity.cur_reducer_1;
                else
                    above_volume = TemperMainActivity.max_reducer_2 - TemperMainActivity.cur_reducer_2;


                if (mRemedyType != 0) {
                    if (Util.converterMGtoCC(mCurVolume, false) <= 0) {
                        Toast.makeText(RemedyInputActivity.this, getString(R.string.volume_lack), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // 120일 미만 아이 체크
                if (MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).born_to_day <= 120) {
                    mDialog = new CustomAlertDialog(RemedyInputActivity.this, CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.under_120_reducer));
                    mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                        @Override
                        public void onClick(CustomAlertDialog dialog, Button button) {
                            requestRemedyRecordInputApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), mCurVolume);
                            dialog.dismiss();
                        }
                    });
                    mDialog.show();
                    return;
                }

                double checkVolume = 0d;
                if (mRemedyType == 0)
                    checkVolume = mCurVolume;
                else
                    checkVolume = Util.converterMGtoCC(mCurVolume, false);

                // 하루 허용치
                if (above_volume < checkVolume) {
                    mDialog = new CustomAlertDialog(RemedyInputActivity.this, CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    if (mRemedyKind == 0)
                        mDialog.setContent(getString(R.string.volume_over_1));
                    else
                        mDialog.setContent(getString(R.string.volume_over_2));
                    mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                        @Override
                        public void onClick(CustomAlertDialog dialog, Button button) {
                            requestRemedyRecordInputApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), mCurVolume);
                            dialog.dismiss();
                        }
                    });
                    mDialog.show();
                    return;
                }

                requestRemedyRecordInputApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), mCurVolume);
            } else {
                Toast.makeText(RemedyInputActivity.this, getString(R.string.empty_remedy), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setRemedyType(int num){
        mRemedyType = num;
        if(num == 0)
            mTxtUnitRemedy.setText(R.string.ml);
        else
            mTxtUnitRemedy.setText(R.string.mg);

        for (int i = 0; i < 4; i++){
            if(i == num)
                mBtnRemedyTypes[i].setImageResource(mImgRemedyBtnOn[i]);
            else
                mBtnRemedyTypes[i].setImageResource(mImgRemedyBtnOff[i]);
        }
    }

    /**
     * 해열제 입력 수정 삭제
     */
    public void requestRemedyRecordInputApi(String chl_sn, double curVolume) {
//        GLog.i("requestAppInfo");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HR004);    //  api 코드명
            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_INPUT_TYPE_F, mRemedyType);               //  제형
            object.put(CommonData.JSON_INPUT_KIND_F, mRemedyKind);               //  성분
            object.put(CommonData.JSON_INPUT_VOLUME_F, curVolume);               //  용량
            object.put(CommonData.JSON_INPUT_DE_F, mCheckDate);               //  입력날짜
            if(mIsEdit != null && mIsEdit.equals(CommonData.YES)) {
                object.put(CommonData.JSON_TYPE_F, CommonData.JSON_UPDATE_F);
                object.put(CommonData.JSON_REMEDY_SN_F, mRemedySn);
            }else
                object.put(CommonData.JSON_TYPE_F, CommonData.JSON_INPUT_F);

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(RemedyInputActivity.this, NetworkConst.NET_REMEDY_INPUT, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    public boolean isConfirm(){
        boolean bool = true;
        if(mRemedyType < 0) {
            bool = false;
            return bool;
        }
        if(mRemedyKind < 0) {
            bool = false;
            return bool;
        }
        if(mRemedyEatVolumeEdt.getText().length() > 0){
            try {
                double d = Double.parseDouble(mRemedyEatVolumeEdt.getText().toString());
                if(d > 0)
                    bool = true;
                else {
                    bool = false;
                    return bool;
                }
            }catch (Exception e){
                e.printStackTrace();
                bool = false;
                return bool;
            }
        }else{
            bool = false;
            return bool;
        }
        return bool;
    }

    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub

            switch ( type ) {
                case NetworkConst.NET_REMEDY_INPUT:

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                if (data_yn.equals(CommonData.YES)) {

                                    GregorianCalendar mCalendar = new GregorianCalendar();
                                    mCalendar.setTime(mCurDate);
                                    mCalendar.add(Calendar.HOUR_OF_DAY, 2);
                                    Date checkTime = mCalendar.getTime();

                                    if(checkTime.compareTo(new Date()) > 0)
                                        Util.setRemedyAlarms(RemedyInputActivity.this, checkTime, Integer.parseInt(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn()));

                                    Intent i = new Intent(RemedyInputActivity.this, FeverHxActivity.class);
                                    i.putExtra(CommonData.EXTRA_IS_TIMELIEN, 1);
                                    startActivity(i);
                                    Util.BackAnimationStart(RemedyInputActivity.this);
                                    RemedyInputActivity.this.finish();
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
}
