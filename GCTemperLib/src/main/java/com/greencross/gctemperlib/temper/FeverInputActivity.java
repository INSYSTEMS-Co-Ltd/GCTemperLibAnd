package com.greencross.gctemperlib.temper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.Database;
import com.greencross.gctemperlib.collection.Fever;
import com.greencross.gctemperlib.collection.FeverItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.greencare.util.TextWatcherUtil;
import com.greencross.gctemperlib.network.DataUploadAsyncTask;
import com.greencross.gctemperlib.network.RequestApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class FeverInputActivity extends BackBaseActivity implements View.OnClickListener {

    LinearLayout mInputLay;

    EditText mCheckFeverEd;
    Button mBtnCroise;
    TextView mCheckDateEdt, mCheckTimeEdt;
    ImageButton mConfirmBtn;

    private String mCheckDate;
    private Date mCurDate;
    GregorianCalendar mCalendar;

    Double fever;
    String result_code;
    Intent intent;

    String fever_update;
    String fever_sn;
    String is_edit;

    CustomAlertDialog mDialog;

    int is_wearable = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fever_record_input);

        intent = getIntent();

        setTitle(getString(R.string.input_fever_title));

        is_wearable = 0;

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

        mInputLay = (LinearLayout)findViewById(R.id.input_lay);

        mBtnCroise = (Button) findViewById(R.id.btn_croise);
        mCheckFeverEd = (EditText)findViewById(R.id.check_fever_ed);
        mCheckDateEdt = (TextView)findViewById(R.id.check_date_edt);
        mCheckTimeEdt = (TextView)findViewById(R.id.check_time_edt);
        mConfirmBtn = (ImageButton)findViewById(R.id.confirm_btn);

        //ssshin add 2018.10.30 체온 50도 이상 입력금지
        new TextWatcherUtil().setTextWatcher(mCheckFeverEd, 50, 1);
        //mCheckFeverEd.addTextChangedListener(new CustomTextWatcher(FeverInputActivity.this, mCheckFeverEd, null));
    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){
        mInputLay.setOnClickListener(this);

        mBtnCroise.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        mCheckDateEdt.setOnClickListener(this);
        mCheckTimeEdt.setOnClickListener(this);
    }

    public void initView() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
        mCalendar = new GregorianCalendar();

        is_edit = intent.getStringExtra(CommonData.EXTRA_IS_EDIT);

        if( is_edit != null && is_edit.equals(CommonData.YES)){     //  수정
            fever_update = intent.getStringExtra(CommonData.EXTRA_FEVER);
            mCheckDate = intent.getStringExtra(CommonData.EXTRA_DATE);
            fever_sn  = intent.getStringExtra(CommonData.EXTRA_SN);

            mCurDate = format.parse(mCheckDate);
            mCheckFeverEd.setText(fever_update);
        }else{                                  // 신규
            mCurDate = new Date();
            mCheckDate = format.format(mCurDate);
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
            InputMethodManager imm = (InputMethodManager) getSystemService(FeverInputActivity.this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mCheckFeverEd.getWindowToken(), 0);
        } else if (id == R.id.btn_croise) { // 크로이스 호출

            if (getPackageManager().getLaunchIntentForPackage(CommonData.PACKAGE_CROISE) == null) {
                mDialog = new CustomAlertDialog(FeverInputActivity.this, CustomAlertDialog.TYPE_B);
                mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                mDialog.setContent(getString(R.string.play_store_croise));
                mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CommonData.MARKET_URL + CommonData.PACKAGE_CROISE)));
                    dialog.dismiss();
                });
                mDialog.show();
            } else {
                Intent i = new Intent();
                i.setClassName(CommonData.PACKAGE_CROISE, CommonData.PACKAGE_CROISE_MAIN);
                i.putExtra(CommonData.SEND, CommonData.FEVER_CHECK);
                startActivityForResult(i, CommonData.REQUEST_CODE_CROISE);       // 700
            }
        } else if (id == R.id.check_date_edt) {
            try {
                if (mCurDate == null) {
                    mCurDate = new Date();
                }
                mCalendar.setTime(mCurDate);
                int nNowYear = mCalendar.get(Calendar.YEAR);
                int nNowMonth = mCalendar.get(Calendar.MONTH);
                int nNowDay = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog alert = new DatePickerDialog(FeverInputActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                    mCalendar.set(year, monthOfYear, dayOfMonth);
                    Date checkDate = new Date();
                    if (mCalendar.getTime().compareTo(checkDate) >= 0) {    // 오늘 지남
                        Toast.makeText(FeverInputActivity.this, getString(R.string.over_time), Toast.LENGTH_LONG).show();
                        return;
                    }
                    mCurDate = mCalendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
                    mCheckDate = format.format(mCurDate);
                    format = new SimpleDateFormat(CommonData.PATTERN_DATE_KR);
                    GLog.i("mCheckDate---> " + mCheckDate, "dd");
                    mCheckDateEdt.setText(format.format(mCurDate));
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(FeverInputActivity.this, (view, hourOfDay, minute) -> {
                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendar.set(Calendar.MINUTE, minute);
                    Date checkDate = new Date();
                    if (mCalendar.getTime().compareTo(checkDate) >= 0) {    // 오늘 지남
                        Toast.makeText(FeverInputActivity.this, getString(R.string.over_time), Toast.LENGTH_LONG).show();
                        return;
                    }
                    mCurDate = mCalendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_TIME);
                    mCheckTimeEdt.setText(format.format(mCurDate));
                    format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
                    mCheckDate = format.format(mCurDate);
                }, nHourOfDay, nMinute, false);

                timePickerDialog.setCancelable(false);

                timePickerDialog.show();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else if (id == R.id.confirm_btn) {
//            try {
//                if (mCheckFeverEd.getText().length() > 0) {
//                    fever = Double.parseDouble(mCheckFeverEd.getText().toString());
//                    if (fever > 0) {
//                        requestFeverRecordInputApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());
//                    } else {
//                        Toast.makeText(FeverInputActivity.this, getString(R.string.empty_fever), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(FeverInputActivity.this, getString(R.string.empty_fever), Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                Toast.makeText(FeverInputActivity.this, getString(R.string.empty_fever), Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
        }
    }


    /**
     * 체온 입력 수정
     */
    public void requestFeverRecordInputApi(String chl_sn) {
//        GLog.i("requestAppInfo");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // {   "api_code": "chldrn_growth_list",   "insures_code": "106", "mber_sn": "10035"  ,"chl_sn": "1000" ,"pageNumber": "1" , "growth_typ": "1"}
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HF004);    //  api 코드명
            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_FEVER_F, String.valueOf(fever));               //  체온
            object.put(CommonData.JSON_INPUT_DE_F, mCheckDate);     // 입력날짜
            object.put(CommonData.JSON_IS_WEARABLE_F, String.valueOf(is_wearable));     // 웨어러블 여부

            if(is_edit != null && is_edit.equals(CommonData.YES)) {
                object.put(CommonData.JSON_TYPE_F, CommonData.JSON_UPDATE_F);
                object.put(CommonData.JSON_FEVER_SN_F, fever_sn);
            }else {
                object.put(CommonData.JSON_TYPE_F, CommonData.JSON_INPUT_F);
                sendData();
            }

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(FeverInputActivity.this, NetworkConst.NET_FEVER_INPUT, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }


    /**
     * 결과 레포트 입력 수정
     */
    public void requestFeverResultRecordInputApi(String chl_sn, String type) {
//        GLog.i("requestAppInfo");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // {   "api_code": "chldrn_growth_list",   "insures_code": "106", "mber_sn": "10035"  ,"chl_sn": "1000" ,"pageNumber": "1" , "growth_typ": "1"}
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HP002);    //  api 코드명
            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_INPUT_DE_F, mCheckDate);     // 입력날짜
            object.put(CommonData.JSON_INPUT_CODE_F, result_code);
            object.put(CommonData.JSON_TYPE_F, type);

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(FeverInputActivity.this, NetworkConst.NET_FEVER_RESULT_INPUT, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }


    // 체온 리스트 갱신
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

            RequestApi.requestApi(this, NetworkConst.NET_FEVER_LIST, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgressLayout());
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
                case NetworkConst.NET_FEVER_INPUT:

//                    switch (resultCode) {
//                        case CommonData.API_SUCCESS:
//                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
//                            try {
//                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);
//
//                                if (data_yn.equals(CommonData.YES)) {
//
//                                    GregorianCalendar mCalendar = new GregorianCalendar();
//                                    mCalendar.setTime(mCurDate);
//                                    mCalendar.add(Calendar.HOUR_OF_DAY, 1);
//                                    Date checkTime = mCalendar.getTime();
//
//                                    if(checkTime.compareTo(new Date()) > 0){
//                                        if(fever < 37.5d)
//                                            Util.cancelAlarm(FeverInputActivity.this, Integer.parseInt(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn()));
//                                        else
//                                            Util.setFeverAlarms(FeverInputActivity.this, checkTime, Integer.parseInt(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn()));
//                                    }
//
//                                    mCalendar.setTime(new Date());
//                                    mCalendar.add(Calendar.HOUR_OF_DAY, -1);
//                                    Date checkDate = mCalendar.getTime();
//
//                                    if(checkDate.compareTo(mCurDate) < 0){      // 1시간 이내에 체온입력임   레포트 생성 함.
//                                        result_code = FeverDiagnosis.shared().getDiagnosis(fever, new Date());
//                                        requestFeverRecordApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());
//                                    }else{
//                                        Intent i = new Intent(FeverInputActivity.this, FeverHxActivity.class);
//                                        i.putExtra(CommonData.EXTRA_IS_TIMELIEN, 1);
//                                        startActivity(i);
//                                        Util.BackAnimationStart(FeverInputActivity.this);
//                                        Toast.makeText(FeverInputActivity.this, getString(R.string.empty_1_hour), Toast.LENGTH_LONG).show();
//                                        FeverInputActivity.this.finish();
//                                    }
//
//                                }
//
//                            } catch (Exception e) {
//                                GLog.e(e.toString());
//                            }
//
//                            break;
//                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
//                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");
//
//                            break;
//                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
//                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
//                            break;
//
//                        default:
//                            GLog.i("NET_GET_APP_INFO default", "dd");
//                            break;
//                    }
//                    break;

//                case NetworkConst.NET_FEVER_LIST:
//                    switch (resultCode) {
//                        case CommonData.API_SUCCESS:
//                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
//                            try {
//                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);
//
//                                if (data_yn.equals(CommonData.YES)) {
//                                    JSONArray feverArr = resultData.getJSONArray(CommonData.JSON_DATA_F);
//                                    TemperMainActivity.mFeverItems.clear();
//                                    // 데이터가 있을 시
//                                    if (feverArr.length() > 0) {
//                                        for(int i = 0; i < feverArr.length(); i++){
//                                            JSONObject object = feverArr.getJSONObject(i);
//
//                                            FeverItem item = new FeverItem();
//                                            item.setmFeverSn(object.getString(CommonData.JSON_FEVER_SN_F));
//                                            item.setmInputDe(object.getString(CommonData.JSON_INPUT_DE_F));
//                                            item.setmInputFever(object.getString(CommonData.JSON_INPUT_FEVER_F));
//
//                                            TemperMainActivity.mFeverItems.add(item);
//                                        }
//                                    }
//                                }
//
//                                requestFeverResultRecordInputApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), CommonData.JSON_INPUT_F);
//
//                            } catch (Exception e) {
//                                GLog.e(e.toString());
//                            }
//
//                            break;
//                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
//                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");
//
//                            break;
//                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
//                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
//                            break;
//
//                        default:
//                            GLog.i("NET_GET_APP_INFO default", "dd");
//                            break;
//                    }
//                    break;

                case NetworkConst.NET_FEVER_RESULT_INPUT:        // 체온 결과 입력수정삭제

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);
                                if (data_yn.equals(CommonData.YES)) {

                                    Intent intent = new Intent(FeverInputActivity.this, FeverResultActivity.class);
                                    intent.putExtra(CommonData.EXTRA_RESULT_CODE, result_code);
                                    intent.putExtra(CommonData.EXTRA_FEVER, fever);
                                    startActivity(intent);
                                    Util.BackAnimationStart(FeverInputActivity.this);

                                    FeverInputActivity.this.finish();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        GLog.i("resultCode : " + resultCode, "dd");
        GLog.i("requestCode :" + requestCode, "dd");

        if(resultCode != RESULT_OK){        // -1
            GLog.i("resultCode != RESULT_OK", "dd");
            return;
        }

        try {
            switch (requestCode) {
                case CommonData.REQUEST_CODE_CROISE:        // 700
                    GLog.i("REQUEST_CODE_CROISE", "dd");
                    mCheckFeverEd.setText(data.getStringExtra(CommonData.REPLY));
                    is_wearable = 1;
                    break;
            }
        }catch(Exception e){
            GLog.e(e.toString());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
    }

    public void sendData(){
//        Fever fever = new Fever(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), ""+this.fever, mCheckDate,null);
//        ArrayList<Fever> arrFever = new ArrayList<Fever>();
//        arrFever.add(fever);
//        Database db = new Database(CommonData.getInstance(FeverInputActivity.this).getMberSn(), arrFever, null);
//        new DataUploadAsyncTask().execute(db, null, null);
    }
}
