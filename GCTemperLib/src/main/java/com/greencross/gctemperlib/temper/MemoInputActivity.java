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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.Database;
import com.greencross.gctemperlib.collection.MemoItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.DataUploadAsyncTask;
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

public class MemoInputActivity extends BackBaseActivity implements View.OnClickListener {

    LinearLayout mInputLay;
    TextView mCheckDateEdt, mCheckTimeEdt;
    ImageButton mConfirmBtn;
    EditText mMemoEdt;
    TextView mBtnSelNum;

    FrameLayout[] mBtnTabs;
    TextView[] mTextTabs;
    int[] mImgTabSel = {R.drawable.tab_symtom_sel, R.drawable.tab_ill_sel, R.drawable.tab_memo_sel};
    int[] mImgTab = {R.drawable.tab_symtom, R.drawable.tab_ill, R.drawable.tab_memo};

    private String mCheckDate;
    private Date mCurDate;
    GregorianCalendar mCalendar;

    int mTabNum = 0;
    int mInputNum = -1;    // 인풋 타입

    String[] mSymptomList;   // 증상 리스트
    String[] mDiagnosisList;   // 증상 리스트
    String[] mMemoList;   // 증상 리스트

    String[] mTumpList;   // 더미 리스트

    Intent mIntent;

    String mMemoSn = "";
    String mIsEdit;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_record_input);

        mIntent = getIntent();

        setTitle(getString(R.string.memo_title));

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
        mSymptomList = getResources().getStringArray(R.array.symptom_list);
        mDiagnosisList = getResources().getStringArray(R.array.diagnosis_list);
        mMemoList = getResources().getStringArray(R.array.memo_list);

        mInputLay = (LinearLayout)findViewById(R.id.input_lay);

        mCheckDateEdt = (TextView)findViewById(R.id.check_date_edt);
        mCheckTimeEdt = (TextView)findViewById(R.id.check_time_edt);
        mConfirmBtn = (ImageButton)findViewById(R.id.confirm_btn);

        mMemoEdt = (EditText)findViewById(R.id.memo_edt);
        mBtnSelNum = (TextView)findViewById(R.id.btn_sel_num);

        mBtnTabs = new FrameLayout[3];
        mBtnTabs[0] = (FrameLayout)findViewById(R.id.symptom_btn);
        mBtnTabs[1] = (FrameLayout)findViewById(R.id.diagnosis_btn);
        mBtnTabs[2] = (FrameLayout)findViewById(R.id.memo_btn);

        mTextTabs = new TextView[3];
        mTextTabs[0] = (TextView) findViewById(R.id.img_tab_1);
        mTextTabs[1] = (TextView) findViewById(R.id.img_tab_2);
        mTextTabs[2] = (TextView) findViewById(R.id.img_tab_3);

        view = findViewById(R.id.root_view);

    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){
        mInputLay.setOnClickListener(this);

        mConfirmBtn.setOnClickListener(this);
        mCheckDateEdt.setOnClickListener(this);
        mCheckTimeEdt.setOnClickListener(this);

        mBtnTabs[0].setOnClickListener(this);
        mBtnTabs[1].setOnClickListener(this);
        mBtnTabs[2].setOnClickListener(this);

        mBtnSelNum.setOnClickListener(this);

        //click 저장
        OnClickListener mClickListener = new OnClickListener(this,view, MemoInputActivity.this);

        //아이 체온
        mBtnTabs[0].setOnTouchListener(mClickListener);
        mBtnTabs[1].setOnTouchListener(mClickListener);
        mBtnTabs[2].setOnTouchListener(mClickListener);

        //코드 부여(아이 체온)
        mBtnTabs[0].setContentDescription(getString(R.string.BtnTabs0));
        mBtnTabs[1].setContentDescription(getString(R.string.BtnTabs1));
        mBtnTabs[2].setContentDescription(getString(R.string.BtnTabs2));
    }


    public void initView() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
        mCalendar = new GregorianCalendar();

        mIsEdit = mIntent.getStringExtra(CommonData.EXTRA_IS_EDIT);

        if( mIsEdit != null && mIsEdit.equals(CommonData.YES)){     //  수정
            setTab(mIntent.getIntExtra(CommonData.EXTRA_MEMO_TYPE, 0));
            mMemoEdt.setText(mIntent.getStringExtra(CommonData.EXTRA_MEMO));
            mInputNum = Integer.parseInt(mIntent.getStringExtra(CommonData.EXTRA_NUM));
            mBtnSelNum.setText(mTumpList[mInputNum]);
            mCheckDate = mIntent.getStringExtra(CommonData.EXTRA_DATE);
            mMemoSn = mIntent.getStringExtra(CommonData.EXTRA_SN);

            mCurDate = format.parse(mCheckDate);
        }else{                                  // 신규
            mCurDate = new Date();
            mCheckDate = format.format(mCurDate);
            setTab(0);
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
            InputMethodManager imm = (InputMethodManager) getSystemService(MemoInputActivity.this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mMemoEdt.getWindowToken(), 0);
        } else if (id == R.id.check_date_edt) {
            try {
                if (mCurDate == null) {
                    mCurDate = new Date();
                }
                mCalendar.setTime(mCurDate);
                int nNowYear = mCalendar.get(Calendar.YEAR);
                int nNowMonth = mCalendar.get(Calendar.MONTH);
                int nNowDay = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog alert = new DatePickerDialog(MemoInputActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);
                        Date checkDate = new Date();
                        if (mCalendar.getTime().compareTo(checkDate) >= 0) {    // 오늘 지남
                            Toast.makeText(MemoInputActivity.this, getString(R.string.over_time), Toast.LENGTH_LONG).show();
                            return;
                        }
                        mCurDate = mCalendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
                        mCheckDate = format.format(mCurDate);
                        format = new SimpleDateFormat(CommonData.PATTERN_DATE_KR);
                        GLog.i("mCheckDate---> " + mCheckDate, "dd");
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(MemoInputActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        Date checkDate = new Date();
                        if (mCalendar.getTime().compareTo(checkDate) >= 0) {    // 오늘 지남
                            Toast.makeText(MemoInputActivity.this, getString(R.string.over_time), Toast.LENGTH_LONG).show();
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
        } else if (id == R.id.symptom_btn) {
            if (mIsEdit != null && mIsEdit.equals(CommonData.YES))
                Toast.makeText(MemoInputActivity.this, getString(R.string.dont_change), Toast.LENGTH_SHORT).show();
            else
                setTab(0);
        } else if (id == R.id.diagnosis_btn) {
            if (mIsEdit != null && mIsEdit.equals(CommonData.YES))
                Toast.makeText(MemoInputActivity.this, getString(R.string.dont_change), Toast.LENGTH_SHORT).show();
            else
                setTab(1);
        } else if (id == R.id.memo_btn) {
            if (mIsEdit != null && mIsEdit.equals(CommonData.YES))
                Toast.makeText(MemoInputActivity.this, getString(R.string.dont_change), Toast.LENGTH_SHORT).show();
            else
                setTab(2);
        } else if (id == R.id.btn_sel_num) {
            AlertDialog.Builder ab = new AlertDialog.Builder(MemoInputActivity.this);
            ab.setSingleChoiceItems(mTumpList, mInputNum,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 각 리스트를 선택했을때
                            mInputNum = whichButton;
                            mBtnSelNum.setText(mTumpList[whichButton]);
                            dialog.dismiss();
                        }
                    });
            ab.show();
        } else if (id == R.id.confirm_btn) {
//            if (isConfirm()) {
//                requestRemedyRecordInputApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());
//            } else {
//                switch (mTabNum) {
//                    case 0:
//                        Toast.makeText(MemoInputActivity.this, getString(R.string.empty_symptom), Toast.LENGTH_SHORT).show();
//                        break;
//                    case 1:
//                        Toast.makeText(MemoInputActivity.this, getString(R.string.empty_diagnosis), Toast.LENGTH_SHORT).show();
//                        break;
//                    case 2:
//                        Toast.makeText(MemoInputActivity.this, getString(R.string.empty_memo), Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
        }
    }

    public void setTab(int num){
        mTabNum = num;
        mInputNum = -1;
        for(int i = 0; i < 3; i++){
            if(num == i) {
//                mBtnTabs[i].setBackgroundColor(getResources().getColor(R.color.h_orange));
                mTextTabs[i].setSelected(true);
            } else {
//                mBtnTabs[i].setBackgroundColor(Color.WHITE);
                mTextTabs[i].setSelected(false);
            }
        }

        switch (num){
            case 0:
                mTumpList = mSymptomList;
                mBtnSelNum.setHint(getString(R.string.empty_symptom));
                mMemoEdt.setHint(getString(R.string.empty_symptom_hint));
                break;
            case 1:
                mTumpList = mDiagnosisList;
                mBtnSelNum.setHint(getString(R.string.empty_diagnosis));
                mMemoEdt.setHint(getString(R.string.empty_diagnosis_hint));
                break;
            case 2:
                mTumpList = mMemoList;
                mBtnSelNum.setHint(getString(R.string.empty_memo));
                mMemoEdt.setHint(getString(R.string.empty_memo_hint));
                break;
        }
    }

    /**
     * 메모 입력 수정 삭제
     */
    public void requestRemedyRecordInputApi(String chl_sn) {
//        GLog.i("requestAppInfo");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();
            switch (mTabNum){
                case 0:         // 증상
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HS002);    //  api 코드명
                    object.put(CommonData.JSON_SYM_SN_F, mMemoSn);
                    break;
                case 1:         // 질환
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HD002);    //  api 코드명
                    object.put(CommonData.JSON_DISE_SN_F, mMemoSn);
                    break;
                case 2:         // 메모
                    object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HM002);    //  api 코드명
                    object.put(CommonData.JSON_MEMO_SN_F, mMemoSn);
                    break;
            }

            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_INPUT_NUM_F, mInputNum);               //  메모 타입
            object.put(CommonData.JSON_INPUT_MEMO_F, mMemoEdt.getText().toString());               //  메모
            object.put(CommonData.JSON_INPUT_DE_F, mCheckDate);               //  입력날짜
            if(mIsEdit != null && mIsEdit.equals(CommonData.YES)) {
                object.put(CommonData.JSON_TYPE_F, CommonData.JSON_UPDATE_F);
            }else {
                object.put(CommonData.JSON_TYPE_F, CommonData.JSON_INPUT_F);
                if(mTabNum == 1)
                    sendData();
            }
            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(MemoInputActivity.this, NetworkConst.NET_MEMO_INPUT, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    public boolean isConfirm(){
        boolean bool = true;
        if(mInputNum < 0) {
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
                case NetworkConst.NET_MEMO_INPUT:

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                if (data_yn.equals(CommonData.YES)) {
                                    Intent i = new Intent(MemoInputActivity.this, FeverHxActivity.class);
                                    i.putExtra(CommonData.EXTRA_IS_TIMELIEN, 1);
                                    startActivity(i);
                                    Util.BackAnimationStart(MemoInputActivity.this);
                                    MemoInputActivity.this.finish();
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

    public void sendData(){
//        MemoItem memo = new MemoItem(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), 3, mInputNum, mMemoEdt.getText().toString(), mCheckDate);
//        ArrayList<MemoItem> arrMemo = new ArrayList<MemoItem>();
//        arrMemo.add(memo);
//        Database db = new Database(CommonData.getInstance(this).getMberSn(), null, arrMemo);
//        new DataUploadAsyncTask().execute(db, null, null);
    }
}
