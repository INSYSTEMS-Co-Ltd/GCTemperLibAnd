package com.greencross.gctemperlib.temper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.FeverResultItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeverResultActivity extends BackBaseActivity implements View.OnClickListener {

    LinearLayout mRemedyLay, mSubLay;
    LinearLayout mRemedyRay1, mRemedyRay2, mRemedyRay3;
    LinearLayout mRemedyVolumeRay1, mRemedyVolumeRay2, mRemedyVolumeRay3, mSubLay2;
    ImageButton mOkBtn;
    TextView mTxtFeverResult, mTxtFeverEmResult, mTxtFeverNextResult;
    TextView mBtnRemedyInput, mTxtRemedyEatResult, mTxtRemedyCrossResult, mBtnRemedyType1, mBtnRemedyType2, mBtnRemedyType3;
    TextView mBtnSubType1, mBtnSubType2, mTepidWaterRay;
    Button mBtnWaterCare;

    Intent mIntent;

    double fFever;
    FeverResultItem mCurItem;

    public CustomAlertDialog mDialog;

    String[] szAppetites;
    String[] szEatStyles;

    String mIsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fever_result);

        setTitle(getString(R.string.fever_result_title));

        init();
        setEvent();

        initView();

    }

    /**
     * 초기화
     */
    public void init(){
        mIntent = getIntent();

        mRemedyLay = (LinearLayout)findViewById(R.id.remedy_lay);               // 해열제 정보 전체 레이어
        mSubLay = (LinearLayout)findViewById(R.id.sub_lay);                      // 보조 수단 전체 레이어
        mRemedyRay1 = (LinearLayout)findViewById(R.id.remedy_ray_1);           // 타이레놀 계열 전체 레이어
        mRemedyRay2 = (LinearLayout)findViewById(R.id.remedy_ray_2);           // 부루펜 계열 전체 레이어
        mRemedyRay3 = (LinearLayout)findViewById(R.id.remedy_ray_3);           // 맥시부펜 계열 전체 레이어

        mRemedyVolumeRay1 = (LinearLayout)findViewById(R.id.remedy_volume_ray_1);     // 타이레놀 용량 레이어
        mRemedyVolumeRay2 = (LinearLayout)findViewById(R.id.remedy_volume_ray_2);     // 부루펜 용량 레이어
        mRemedyVolumeRay3 = (LinearLayout)findViewById(R.id.remedy_volume_ray_3);     // 맥시부펜 용량 레이어

        mTxtFeverResult = (TextView)findViewById(R.id.txt_fever_result);               // 체온 수치
        mTxtFeverEmResult = (TextView)findViewById(R.id.txt_fever_em_result);         // 응급 메세지
        mTxtFeverNextResult = (TextView)findViewById(R.id.txt_fever_next_result);     // 다음 측정 시간

        mBtnRemedyInput = (TextView)findViewById(R.id.btn_remedy_input);               // 해열제 입력 페이지로 넘기기
        mTxtRemedyEatResult = (TextView)findViewById(R.id.txt_remedy_eat_result);      // 해열제 먹임 여부
        mTxtRemedyCrossResult = (TextView)findViewById(R.id.txt_remedy_cross_result); // 해열제 교차복용 여부
        mBtnRemedyType1 = (TextView)findViewById(R.id.btn_remedy_type_1);                 // 타이레놀 용량 버튼
        mBtnRemedyType2 = (TextView)findViewById(R.id.btn_remedy_type_2);                 // 부루펜 용량 버튼
        mBtnRemedyType3 = (TextView)findViewById(R.id.btn_remedy_type_3);                 // 맥시부펜 용량 버튼

        mBtnSubType1 = (TextView)findViewById(R.id.btn_sub_type_1);                       // 마사지 메세지 버튼
        mBtnSubType2 = (TextView)findViewById(R.id.btn_sub_type_2);                       // 수분 섭취 계산 버튼
        mSubLay2 = (LinearLayout)findViewById(R.id.sub_2_lay);                               // 수분 섭취 컨텐츠

        mTepidWaterRay = (TextView)findViewById(R.id.tepid_water_ray);                     // 미온수 마사지 컨텐츠
        mBtnWaterCare = (Button)findViewById(R.id.btn_water_care);

        mOkBtn = (ImageButton)findViewById(R.id.ok_btn);
    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){
        mBtnRemedyInput.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
        mBtnRemedyType1.setOnClickListener(this);
        mBtnRemedyType2.setOnClickListener(this);
        mBtnRemedyType3.setOnClickListener(this);
        mBtnSubType1.setOnClickListener(this);
        mBtnSubType2.setOnClickListener(this);
        mBtnWaterCare.setOnClickListener(this);
    }

    public void initView(){
//        szAppetites = getResources().getStringArray(R.array.appetite_list);
//        szEatStyles = getResources().getStringArray(R.array.eat_style_list);
//
//        String[] result_code = null;
//
//        mIsEdit = mIntent.getStringExtra(CommonData.EXTRA_IS_EDIT);
//
//        if( mIsEdit != null && mIsEdit.equals(CommonData.YES)) {     //  수정
//            result_code = mIntent.getStringExtra(CommonData.EXTRA_CODE).split("_");
//        }else{
//            fFever = mIntent.getDoubleExtra(CommonData.EXTRA_FEVER, 0d);
//           result_code = mIntent.getStringExtra(CommonData.EXTRA_RESULT_CODE).split("_");
//            mTxtFeverResult.setText(""+ fFever);
//        }
//
//        if(result_code[0].equals("0")){
//            mTxtFeverEmResult.setVisibility(View.GONE);
//        }else{
//            mTxtFeverEmResult.setText(getString(getResources().getIdentifier("em_msr_" + result_code[0], "string", getPackageName())));
//        }
//
//        if(result_code[1].equals("0")){     //  해열제 안먹임
//            mRemedyLay.setVisibility(View.GONE);
//            mTxtFeverNextResult.setText(R.string.measure_fever_1);
//        }else{                              // 해열제 먹임
//            mRemedyLay.setVisibility(View.VISIBLE);
//            mTxtFeverNextResult.setText(R.string.measure_fever_2);
//
//            if(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).born_to_day < 120){
//                mTxtRemedyEatResult.setText(R.string.remedy_no_eat_120);
//                mRemedyRay1.setVisibility(View.GONE);
//                mRemedyRay2.setVisibility(View.GONE);
//                mRemedyRay3.setVisibility(View.GONE);
//            }else if(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).born_to_day < 180){
//                mTxtRemedyEatResult.setText(R.string.remedy_eat_1);
//                mRemedyRay1.setVisibility(View.VISIBLE);
//                mRemedyRay2.setVisibility(View.GONE);
//                mRemedyRay3.setVisibility(View.GONE);
//            }else{
//                mTxtRemedyEatResult.setText(R.string.remedy_eat_2);
//                mRemedyRay1.setVisibility(View.VISIBLE);
//                mRemedyRay2.setVisibility(View.VISIBLE);
//                mRemedyRay3.setVisibility(View.VISIBLE);
//            }
//
//            if(result_code[2].equals("0")) {      // 교차복용 없음
//                mTxtRemedyCrossResult.setVisibility(View.GONE);
//            }else{
//                mTxtRemedyCrossResult.setVisibility(View.VISIBLE);
//            }
//        }
//
//        if(result_code[3].equals("0") && result_code[4].equals("0")) {    // 보조 수단 없음
//            mSubLay.setVisibility(View.GONE);
//        }else{
//            mSubLay.setVisibility(View.VISIBLE);
//            if(result_code[3].equals("0")) {        //  미온수
//                mBtnSubType1.setVisibility(View.GONE);
//            }else{
//                mBtnSubType1.setVisibility(View.VISIBLE);
//            }
//
//            if(result_code[4].equals("0")) {        //  수분
//                mBtnSubType2.setVisibility(View.GONE);
//            }else{
//                mBtnSubType2.setVisibility(View.VISIBLE);
//            }
//        }


    }

    @Override
    public void onClick(View v) {

//        Intent intent = null;
//        int id = v.getId();
//        if (id == R.id.btn_remedy_input) {
//            intent = new Intent(FeverResultActivity.this, RemedyInputActivity.class);
//            startActivity(intent);
//            Util.BackAnimationStart(FeverResultActivity.this);
//        } else if (id == R.id.ok_btn) {
//            Intent i = new Intent(FeverResultActivity.this, FeverHxActivity.class);
//            i.putExtra(CommonData.EXTRA_IS_TIMELIEN, 1);
//            startActivity(i);
//            Util.BackAnimationStart(FeverResultActivity.this);
//            finish();
//        } else if (id == R.id.btn_remedy_type_1) {
//            if (mBtnRemedyType1.getText().equals(getString(R.string.remedy_type_and_volume_1_off))) {   //  닫힘
//                mBtnRemedyType1.setText(getString(R.string.remedy_type_and_volume_1_on));
//                mRemedyVolumeRay1.setVisibility(View.VISIBLE);
//            } else {                                                                                              //  열림
//                mBtnRemedyType1.setText(getString(R.string.remedy_type_and_volume_1_off));
//                mRemedyVolumeRay1.setVisibility(View.GONE);
//            }
//        } else if (id == R.id.btn_remedy_type_2) {
//            if (mBtnRemedyType2.getText().equals(getString(R.string.remedy_type_and_volume_2_off))) {   //  닫힘
//                mBtnRemedyType2.setText(getString(R.string.remedy_type_and_volume_2_on));
//                mRemedyVolumeRay2.setVisibility(View.VISIBLE);
//            } else {                                                                                              //  열림
//                mBtnRemedyType2.setText(getString(R.string.remedy_type_and_volume_2_off));
//                mRemedyVolumeRay2.setVisibility(View.GONE);
//            }
//        } else if (id == R.id.btn_remedy_type_3) {
//            if (mBtnRemedyType3.getText().equals(getString(R.string.remedy_type_and_volume_3_off))) {   //  닫힘
//                mBtnRemedyType3.setText(getString(R.string.remedy_type_and_volume_3_on));
//                mRemedyVolumeRay3.setVisibility(View.VISIBLE);
//            } else {                                                                                              //  열림
//                mBtnRemedyType3.setText(getString(R.string.remedy_type_and_volume_3_off));
//                mRemedyVolumeRay3.setVisibility(View.GONE);
//            }
//        } else if (id == R.id.btn_sub_type_1) {
//            if (mBtnSubType1.getText().equals(getString(R.string.sub_type_1_off))) {                        //  닫힘
//                mBtnSubType1.setText(getString(R.string.sub_type_1_on));
//                mTepidWaterRay.setVisibility(View.VISIBLE);
//            } else {                                                                                              //  열림
//                mBtnSubType1.setText(getString(R.string.sub_type_1_off));
//                mTepidWaterRay.setVisibility(View.GONE);
//            }
//        } else if (id == R.id.btn_sub_type_2) {
//            if (mBtnSubType2.getText().equals(getString(R.string.sub_type_2_off))) {                        //  닫힘
//                mBtnSubType2.setText(getString(R.string.sub_type_2_on));
//                mSubLay2.setVisibility(View.VISIBLE);
//            } else {                                                                                              //  열림
//                mBtnSubType2.setText(getString(R.string.sub_type_2_off));
//                mSubLay2.setVisibility(View.GONE);
//            }
//        } else if (id == R.id.btn_water_care) {
//            AlertDialog.Builder ab = new AlertDialog.Builder(FeverResultActivity.this);
//            ab.setTitle(getString(R.string.how_appetite));
//            ab.setSingleChoiceItems(szAppetites, -1,
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            final int m_which = whichButton;
//
//                            AlertDialog.Builder ab = new AlertDialog.Builder(FeverResultActivity.this);
//                            ab.setTitle(getString(R.string.how_meal));
//                            ab.setSingleChoiceItems(szEatStyles, -1, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (which < 3) {
//                                                double basic_water = 0f;
//                                                double fever_water = 0f;
//                                                double appetite_water = 0f;
//                                                double sum_water = 0f;
//
//                                                try {
//                                                    if (Double.parseDouble(MainActivity.mLastWeight) <= 5.0d) {
//                                                        basic_water = 500d;
//                                                    } else if (Double.parseDouble(MainActivity.mLastWeight) <= 10.0d) {
//                                                        basic_water = 1000d;
//                                                    } else if (Double.parseDouble(MainActivity.mLastWeight) <= 15.0d) {
//                                                        basic_water = 1300d;
//                                                    } else if (Double.parseDouble(MainActivity.mLastWeight) <= 20.0d) {
//                                                        basic_water = 1500d;
//                                                    } else if (Double.parseDouble(MainActivity.mLastWeight) <= 25.0d) {
//                                                        basic_water = 1600d;
//                                                    } else {
//                                                        basic_water = 1700d;
//                                                    }
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                    Toast.makeText(FeverResultActivity.this, getString(R.string.check_baby_weight), Toast.LENGTH_SHORT).show();
//                                                    return;
//                                                }
//
//                                                double avgFever = getTempArray(new Date());
//
//                                                if (avgFever >= 39.4f) {
//                                                    fever_water = basic_water * 0.2f;
//                                                } else if (avgFever >= 37.5f) {
//                                                    fever_water = basic_water * ((2.0f - (39.4f - avgFever)) * 0.1f);
//                                                }
//
//                                                if (avgFever >= 38.5f) {
//                                                    fever_water = basic_water * 0.14f;
//                                                } else if (avgFever >= 38.0f) {
//                                                    fever_water = basic_water * 0.07f;
//                                                }
//
//                                                switch (which) {
//                                                    case 0:
//                                                        switch (m_which) {
//                                                            case 1:
//                                                                appetite_water = basic_water * 0.1f;
//                                                                break;
//                                                            case 2:
//                                                                appetite_water = basic_water * 0.2f;
//                                                                break;
//                                                            case 3:
//                                                                appetite_water = basic_water * 0.3f;
//                                                                break;
//                                                            case 4:
//                                                                appetite_water = basic_water * 0.5f;
//                                                                break;
//                                                        }
//                                                        break;
//                                                    case 1:
//                                                        switch (m_which) {
//                                                            case 1:
//                                                                appetite_water = basic_water * 0.075f;
//                                                                break;
//                                                            case 2:
//                                                                appetite_water = basic_water * 0.15f;
//                                                                break;
//                                                            case 3:
//                                                                appetite_water = basic_water * 0.225f;
//                                                                break;
//                                                            case 4:
//                                                                appetite_water = basic_water * 0.375f;
//                                                                break;
//                                                        }
//                                                        break;
//                                                    case 2:
//                                                        switch (m_which) {
//                                                            case 1:
//                                                                appetite_water = basic_water * 0.05f;
//                                                                break;
//                                                            case 2:
//                                                                appetite_water = basic_water * 0.1f;
//                                                                break;
//                                                            case 3:
//                                                                appetite_water = basic_water * 0.15f;
//                                                                break;
//                                                            case 4:
//                                                                appetite_water = basic_water * 0.25f;
//                                                                break;
//                                                        }
//                                                        break;
//
//                                                }
//
//                                                sum_water = fever_water + appetite_water;
//                                                String szWater = "" + sum_water;
//                                                szWater = szWater.substring(0, szWater.lastIndexOf("."));
//                                                if (basic_water * 0.3f < sum_water) {
//                                                    mDialog = new CustomAlertDialog(FeverResultActivity.this, CustomAlertDialog.TYPE_A);
//                                                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
//                                                    mDialog.setContent(getString(R.string.need_water).replace("[n]", szWater) + getString(R.string.more_water));
//                                                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
//                                                    mDialog.show();
//                                                } else {
//                                                    mDialog = new CustomAlertDialog(FeverResultActivity.this, CustomAlertDialog.TYPE_A);
//                                                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
//                                                    mDialog.setContent(getString(R.string.need_water).replace("[n]", szWater));
//                                                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
//                                                    mDialog.show();
//                                                }
//                                                dialog.dismiss();
//                                            }
//                                            dialog.dismiss();
//                                        }
//                                    }
//                            );
//                            ab.show();
//                            dialog.dismiss();
//                        }
//                    });
//            ab.show();
//        }
    }


    // 24시간 체온 평균 구하기
    public double getTempArray(Date curDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME_S);

            if(TemperMainActivity.mFeverItems.size() == 1){
                return Double.parseDouble(TemperMainActivity.mFeverItems.get(0).getmInputFever());
            }else {
                double sumTime = 0d;
                double sumTemp = 0d;

                for (int i = 0; i < TemperMainActivity.mFeverItems.size(); i++) {
                    double f_time = 0;
                    if (i == 0) {
                        f_time = Util.subDate(curDate, format.parse(TemperMainActivity.mFeverItems.get(i).getmInputDe()));
                    } else {
                        f_time = Util.subDate(format.parse(TemperMainActivity.mFeverItems.get(i - 1).getmInputDe()), format.parse(TemperMainActivity.mFeverItems.get(i).getmInputDe()));
                    }
                    sumTime += f_time;
                    BigDecimal preNum = new BigDecimal(f_time);
                    BigDecimal postNum;
                    postNum = new BigDecimal(TemperMainActivity.mFeverItems.get(i).getmInputFever());
                    sumTemp += preNum.multiply(postNum).doubleValue();
                }

                return sumTemp/sumTime;
            }
        }catch (Exception e){
            e.printStackTrace();
            return fFever;
        }
    }
}
