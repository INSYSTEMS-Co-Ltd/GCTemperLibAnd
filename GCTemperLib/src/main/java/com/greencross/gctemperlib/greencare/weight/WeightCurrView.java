package com.greencross.gctemperlib.greencare.weight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.collection.ProgressItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.util.CustomSeekBar;
import com.greencross.gctemperlib.R;

import java.util.ArrayList;


/**
 * 현재체중
 */

public class WeightCurrView extends LinearLayout {
    private final String TAG = WeightCurrView.class.getSimpleName();

    private Context context;

    private CustomSeekBar seekbar;
    private float totalSpan = 100;
    private float span01 = 15;
    private float span02 = 30;
    private float span03 = 15;
    private float span04 = 15;
    private float span05;

    private LinearLayout after_birth_pop;
    private LinearLayout after_birth_pop_close;


    private ArrayList<ProgressItem> progressItemList;
    private ProgressItem mProgressItem;

    public WeightCurrView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public WeightCurrView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public WeightCurrView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.weight_curr_view, null);
        LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        seekbar = ((CustomSeekBar) view.findViewById(R.id.seekBar));

        addView(view, contentParams);

//        json={"api_code":"mber_mother_bdwgh_view","insures_code":"108","mber_sn": "21182"}

        after_birth_pop = (LinearLayout) view.findViewById(R.id.after_birth_pop);
        after_birth_pop.setVisibility(View.GONE);
        after_birth_pop_close = (LinearLayout) view.findViewById(R.id.after_birth_pop_close);


        after_birth_pop_close.setOnClickListener(mClickListener);



        // 엄마체중측정
        view.findViewById(R.id.mom_weight_measurement_btn).setOnClickListener(mClickListener);
        // 엄마체중예측
        view.findViewById(R.id.mom_weight_prediction_btn).setOnClickListener(mClickListener);
        // 상담연결하기
        view.findViewById(R.id.Hcall_btn).setOnClickListener(mClickListener);
        // 다이어트 프로그램 신청하기
        view.findViewById(R.id.req_diet_btn).setOnClickListener(mClickListener);

        //click 저장
        com.greencross.gctemperlib.greencare.component.OnClickListener ClickListener = new com.greencross.gctemperlib.greencare.component.OnClickListener(mClickListener, view, context);

        //엄마 건강
        view.findViewById(R.id.mom_weight_measurement_btn).setOnTouchListener(ClickListener);
        view.findViewById(R.id.mom_weight_prediction_btn).setOnTouchListener(ClickListener);
        view.findViewById(R.id.Hcall_btn).setOnTouchListener(ClickListener);
        view.findViewById(R.id.req_diet_btn).setOnTouchListener(ClickListener);

        //코드 부여(엄마 건강)
        view.findViewById(R.id.mom_weight_measurement_btn).setContentDescription(context.getString(R.string.mom_weight_measurement_btn));
        view.findViewById(R.id.mom_weight_prediction_btn).setContentDescription(context.getString(R.string.mom_weight_prediction_btn));
        view.findViewById(R.id.Hcall_btn).setContentDescription(context.getString(R.string.HCallBtn7));
        view.findViewById(R.id.req_diet_btn).setContentDescription(context.getString(R.string.req_diet_btn));

    }
    //임신여부
    CommonData common = CommonData.getInstance(getContext());
    String materPregency = common.getbirth_chl_yn(); //임신 중 N, 출산 후 Y


    OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if(vId == R.id.after_birth_pop_close){
                after_birth_pop.setVisibility(GONE);
//            }else if(vId == R.id.mom_weight_measurement_btn){
//                DummyActivity.startActivity(((Activity)context), MotherWeightInputFragment.class, new Bundle());
            }else if(vId == R.id.mom_weight_prediction_btn){
                DummyActivity.startActivityForResult(((Activity) getContext()), WeightBigDataInputFragment.REQ_WEIGHT_PREDICT, WeightBigDataInputFragment.class, new Bundle());
//            }else if(vId == R.id.req_diet_btn) {
//                reqDietProgram();
            }else if(vId == R.id.Hcall_btn){
                if("10".equals(CommonData.getInstance(getContext()).getMberGrad())) {
                    CustomAlertDialog mDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getContext().getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getContext().getString(R.string.do_call_center));
                    mDialog.setNegativeButton(getContext().getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getContext().getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                        String tel = "tel:" + getContext().getString(R.string.call_center_number);
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(tel));
                        getContext().startActivity(intent);
                        dialog.dismiss();
                    });

                    mDialog.show();
                }else{
                    CustomAlertDialog mDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getContext().getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getContext().getString(R.string.call_center2));
                    mDialog.setNegativeButton(getContext().getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getContext().getString(R.string.do_call), (dialog, button) -> {
                        String tel = "tel:" + getContext().getString(R.string.call_center_number2);
//                        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                        Intent intentCall = new Intent(Intent.ACTION_DIAL);
                        intentCall.setData(Uri.parse(tel));
                        getContext().startActivity(intentCall);
                        dialog.dismiss();
                    });
                    mDialog.show();
                }
            }
        }
    };

    /**
     * 임신중 체중안내
     * mber_mother_bdwgh_view 전문에서 문구
     */
    public static void bmiInfoPopup(Context context) {
        CustomAlertDialog mDialog = new CustomAlertDialog(context, CustomAlertDialog.TYPE_A);
        mDialog.setTitle("");
        View view = LayoutInflater.from(context).inflate(R.layout.popup_bmi, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mDialog.setContentView(view, params);

        TextView title = view.findViewById(R.id.bmi_title);

        TextView rateTv1  = view.findViewById(R.id.pregnant_rate_1);
        TextView rateTv2  = view.findViewById(R.id.pregnant_rate_2);
        TextView rateTv3  = view.findViewById(R.id.pregnant_rate_3);
        TextView rateTv4  = view.findViewById(R.id.pregnant_rate_4);

        rateTv1.setText(WeightManageFragment.bmi1);
        rateTv2.setText(WeightManageFragment.bmi2);
        rateTv3.setText(WeightManageFragment.bmi3);
        rateTv4.setText(WeightManageFragment.bmi4);

        title.setText(context.getString(R.string.임신기간체중안내팝업타이틀1,WeightManageFragment.bmi5));
//        mDialog.setContent("임신중 체중 안내 문구 넣어야 함");
        mDialog.setPositiveButton(context.getString(R.string.popup_dialog_button_confirm), null);
        mDialog.show();
    }


    /* 임신 중 */
    private void initDataToSeekbarCase01() {

    }

    public void initData(ArrayList<ProgressItem> progressItemList, float curBmi, float goalBmi) {
        curBmi = ((curBmi / 18) * 100);
        goalBmi = ((goalBmi / 18) * 100);


        seekbar.setMax(100);

        seekbar.initData(progressItemList, curBmi, goalBmi, after_birth_pop);
        seekbar.setEnabled(false);

        seekbar.invalidate();
    }

    public void initData(ArrayList<ProgressItem> progressItemList, int curWtInt, int goalWtInt, boolean flag) {

        seekbar.initData(progressItemList, curWtInt, goalWtInt, false, after_birth_pop);
        seekbar.setEnabled(false);

        seekbar.invalidate();
    }
}