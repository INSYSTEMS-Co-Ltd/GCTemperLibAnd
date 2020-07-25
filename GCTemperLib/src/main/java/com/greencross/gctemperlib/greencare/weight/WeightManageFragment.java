package com.greencross.gctemperlib.greencare.weight;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.greencross.gctemperlib.collection.ProgressItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.CustomEditConfirmDialog;
import com.greencross.gctemperlib.common.MakeProgress;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.components.YAxis;
import com.greencross.gctemperlib.greencare.charting.data.BarEntry;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.AxisValueFormatter2;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.AxisYValueFormatter;
import com.greencross.gctemperlib.greencare.chartview.weight.FatChartView;
import com.greencross.gctemperlib.greencare.chartview.weight.Mother40WeekCalc;
import com.greencross.gctemperlib.greencare.chartview.weight.WeightChartView;
import com.greencross.gctemperlib.greencare.component.CPDialog;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
//import com.greencross.gctemperlib.greencare.database.DBHelper;
//import com.greencross.gctemperlib.greencare.database.DBHelperWeight;
import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.ChartTimeUtil;
import com.greencross.gctemperlib.greencare.util.DisplayUtil;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.greencare.util.TextWatcherUtil;
import com.greencross.gctemperlib.network.RequestApi;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.webview.TipWebViewActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_mber_mother_bdwgh_view;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


/**
 * Created by insystemscompany on 2017. 2. 28..
 */

public class WeightManageFragment extends BaseFragment {
    private final String TAG = WeightManageFragment.class.getSimpleName();

    int mRequestCode = 1111;
    Boolean isGraphActive = false;

    public CommonData commonData = CommonData.getInstance(getContext());

    public ChartTimeUtil mTimeClass;
    private TextView mDateTv;
    private TextView mWeightTargetTv;
    private TextView mWeightTv;
    private TextView mWeightTargetWaitTv;
    private TextView mWeightDayTv;
    private TextView chartRule;

    private TextView mMother_week;
    private TextView mMother_period;

    protected WeightChartView mWeightChart;
    protected FatChartView mFatChart;

    private WeightSwipeListView mSwipeListView;

    protected WeightCurrView layout_curr_weight;              // 현재체중 레이아웃
    protected LinearLayout layout_weight_history;            // 타임라인 화면
    protected LinearLayout layout_weight_graph;              // 그래프 레이아웃
    protected LinearLayout weight_chart_date_layout;      // 차트 상단 날자 레이아웃
    protected View weight_graph_history_layout;
    protected TextView btn_curr_weight;
    protected TextView btn_weight_graph;
    protected TextView btn_weight_history;

    protected TextView mXLabelTv;

    private ImageButton imgPre_btn;
    private ImageButton imgNext_btn;
//    private ImageView Hcallbtn; //, Action_btn;

//    private DBHelperWeight.WeightStaticData mWeightStaticData;
    private AxisValueFormatter2 xFormatter;
    private AxisYValueFormatter yFormatter;

    private View mVisibleView1;
    private View mVisibleView2;
    private View mVisibleView3;
    private View mVisibleView4;
    private View mGraphHint;
    private ScrollView mContentScrollView;
    private LinearLayout mChartFrameLayout;
    private ImageView mChartCloseBtn, mChartZoomBtn;
    private LinearLayout mHCallBtn;

    private RadioButton radioBtnMonth;
    private RadioButton radioBtnYear;
    private RadioButton radioBtnDay;
    private RadioButton radioBtnWeek;

    public static String bmi1;
    public static String bmi2;
    public static String bmi3;
    public static String bmi4;
    public static String bmi5;

    private TextView Hcall_tv;


    public static Fragment newInstance() {
        WeightManageFragment fragment = new WeightManageFragment();
        return fragment;
    }

    /**
     * 목표체중 Dialog
     */
    public void showGoalDialog() {

        CustomEditConfirmDialog mDialog = new CustomEditConfirmDialog(getActivity());
        mDialog.setTitle(getString(R.string.mother_health_curr_wt_dialog_title));
        mDialog.setContent(getString(R.string.mother_health_curr_wt_dialog_content));

        CommonData commonData = CommonData.getInstance(getContext());
        mDialog.setEditStr(commonData.getMotherGoalWeight());
        new TextWatcherUtil().setTextWatcher(mDialog.getInputEditText(), 130, 2);

        mDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goalKg = mDialog.getEditStr();

                if (goalKg.endsWith(".")) {
                    goalKg = goalKg.replaceAll("\\.", "");
                    mDialog.setEditStr(goalKg);
                }

                if (TextUtils.isEmpty(goalKg) || "0".equals(goalKg)) {

                    Toast.makeText(getActivity(), "다시 기재해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    float tmp = Float.valueOf(goalKg); //임신 전 몸무게
                    if (tmp < 30 || 130 < tmp) {
                        Toast.makeText(getActivity(), "몸무게는 30~130kg까지 입력가능합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    requestRevcGoalWeight(goalKg);
                    //    Float goalKgFl = Float.valueOf(goalKg);

                    mDialog.dismiss();
                }
            }
        });
        mDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_weight_manage, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDateTv = (TextView) view.findViewById(R.id.period_date_textview);
        mWeightTargetTv = (TextView) view.findViewById(R.id.textView54);
        mWeightTv = (TextView) view.findViewById(R.id.textView52);
        mWeightTargetWaitTv = (TextView) view.findViewById(R.id.textView57);
        mWeightDayTv = (TextView) view.findViewById(R.id.textView18);
        chartRule = (TextView) view.findViewById(R.id.chart_rule);
        layout_curr_weight = (WeightCurrView) view.findViewById(R.id.curr_weight_view);
        initCurrWeightView(layout_curr_weight);
        layout_weight_graph = (LinearLayout) view.findViewById(R.id.layout_weight_graph);
        weight_chart_date_layout = view.findViewById(R.id.weight_chart_date_layout);
        layout_weight_history = (LinearLayout) view.findViewById(R.id.layout_weight_history);
        weight_graph_history_layout = (LinearLayout) view.findViewById(R.id.weight_graph_history_layout);
        btn_curr_weight = (TextView) view.findViewById(R.id.btn_curr_weight);
        btn_weight_graph = (TextView) view.findViewById(R.id.btn_weight_graph);
        btn_weight_history = (TextView) view.findViewById(R.id.btn_weight_history);

        mXLabelTv = view.findViewById(R.id.weight_chart_x_label_tv);


        imgPre_btn = (ImageButton) view.findViewById(R.id.pre_btn);
        imgNext_btn = (ImageButton) view.findViewById(R.id.next_btn);
        RadioGroup periodRg = (RadioGroup) view.findViewById(R.id.period_radio_group);
        radioBtnDay = (RadioButton) view.findViewById(R.id.period_radio_btn_day);
        radioBtnWeek = (RadioButton) view.findViewById(R.id.period_radio_btn_week);
        radioBtnMonth = (RadioButton) view.findViewById(R.id.period_radio_btn_month);
        radioBtnYear = (RadioButton) view.findViewById(R.id.period_radio_btn_year);

        view.findViewById(R.id.pre_btn).setOnClickListener(mClickListener);
        view.findViewById(R.id.next_btn).setOnClickListener(mClickListener);
        view.findViewById(R.id.btn_curr_weight).setOnClickListener(mClickListener);
        view.findViewById(R.id.btn_weight_graph).setOnClickListener(mClickListener);
        view.findViewById(R.id.btn_weight_history).setOnClickListener(mClickListener);
        view.findViewById(R.id.weight_modal_btn).setOnClickListener(mClickListener);
//        layout_curr_weight.findViewById(R.id.tip_btn).setOnClickListener(mClickListener);
        periodRg.setOnCheckedChangeListener(mCheckedChangeListener);


        getBottomDataLayout(); // 하단 데이터 셋팅

        mTimeClass = new ChartTimeUtil(radioBtnDay, radioBtnWeek, radioBtnMonth, radioBtnYear);
        mWeightChart = new WeightChartView(getContext(), view);
        mFatChart = new FatChartView(getContext(), view);

        TypeDataSet.Period periodType = mTimeClass.getPeriodType();
        mTimeClass.clearTime();         // 날자 초기화

        xFormatter = new AxisValueFormatter2(periodType);
        yFormatter = new AxisYValueFormatter(periodType);

        mWeightChart.setYValueFormat(yFormatter);
        mWeightChart.setXValueFormat(xFormatter);
        mFatChart.setXValueFormat(xFormatter);

        mSwipeListView = new WeightSwipeListView(view, WeightManageFragment.this);
        chartRule.setText("일간 : 시간별 최종데이터");

        setNextButtonVisible();

        layout_curr_weight.setVisibility(View.VISIBLE);
        layout_weight_graph.setVisibility(View.GONE);
        layout_weight_history.setVisibility(View.GONE);
        weight_graph_history_layout.setVisibility(View.GONE);

        btn_curr_weight.setSelected(true);
        btn_weight_graph.setSelected(false);
        btn_weight_history.setSelected(false);

//        btn_curr_weight.setBackgroundResource(R.drawable.underline_mother);
//        btn_weight_graph.setBackgroundResource(R.color.color_FB8AD3);
//        btn_weight_history.setBackgroundResource(R.color.color_FB8AD3);

        // 차트 전체 화면 처리
        mVisibleView1 = view.findViewById(R.id.period_radio_group);
        mVisibleView2 = view.findViewById(R.id.radioGroup2);
        mVisibleView3 = view.findViewById(R.id.period_radio_btn_day);
        mVisibleView4 = view.findViewById(R.id.visible_layout_4);
        mContentScrollView = view.findViewById(R.id.view_scrollview);
        mChartFrameLayout = view.findViewById(R.id.chart_frame_layout);
        mChartCloseBtn = view.findViewById(R.id.chart_close_btn);
        mChartZoomBtn = view.findViewById(R.id.landscape_btn);
        mGraphHint = view.findViewById(R.id.graph_hint);


        //임신중 상단 문구

        mMother_period = layout_curr_weight.findViewById(R.id.pregnant_date_Txt);
        mMother_week = layout_curr_weight.findViewById(R.id.pregnant_Txt);

        // 상담연결하기
        mHCallBtn = layout_curr_weight.findViewById(R.id.Hcall_btn);
        Hcall_tv = layout_curr_weight.findViewById(R.id.Hcall_tv);

        if (CommonData.getInstance(getContext()).getMberGrad().equals("10")) {
            Hcall_tv.setText("체중관리 상담 (무료)");
        } else {
            Hcall_tv.setText("체중관리 상담");
        }

        mChartCloseBtn.setOnClickListener(mClickListener);
        mChartZoomBtn.setOnClickListener(mClickListener);
        mHCallBtn.setOnClickListener(mClickListener);
        layout_curr_weight.findViewById(R.id.target_value_btn).setOnClickListener(mClickListener);
        weight_graph_history_layout.findViewById(R.id.target_value_btn).setOnClickListener(mClickListener);

        setVisibleOrientationLayout();

//        if (BuildConfig.DEBUG) {
//            doWeightTotalGrp();
//            doWeightHopeGrp("50", "20");
//        }


        //click 저장
        OnClickListener ClickListener = new OnClickListener(mClickListener, view, getContext());

        //엄마 건강
        view.findViewById(R.id.btn_curr_weight).setOnTouchListener(ClickListener);
        layout_curr_weight.findViewById(R.id.target_value_btn).setOnTouchListener(ClickListener);
        view.findViewById(R.id.btn_weight_graph).setOnTouchListener(ClickListener);
        view.findViewById(R.id.btn_weight_history).setOnTouchListener(ClickListener);
        radioBtnDay.setOnTouchListener(ClickListener);
        radioBtnWeek.setOnTouchListener(ClickListener);
        radioBtnMonth.setOnTouchListener(ClickListener);
        radioBtnYear.setOnTouchListener(ClickListener);
        mHCallBtn.setOnTouchListener(ClickListener);

        //코드 부여(엄마 건강)
        view.findViewById(R.id.btn_curr_weight).setContentDescription(getString(R.string.btn_curr_weight));
        layout_curr_weight.findViewById(R.id.target_value_btn).setContentDescription(getString(R.string.target_value_btn));
        view.findViewById(R.id.btn_weight_graph).setContentDescription(getString(R.string.btn_weight_graph));
        view.findViewById(R.id.btn_weight_history).setContentDescription(getString(R.string.btn_weight_history));
        mHCallBtn.setContentDescription(getString(R.string.HCallBtn7));

    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();

            if (vId == R.id.pre_btn) {
                mTimeClass.calTime(-1);
                getData();
            } else if (vId == R.id.next_btn) {
                // 초기값 일때 다음날 데이터는 없으므로 리턴
                if (mTimeClass.getCalTime() == 0)
                    return;

                mTimeClass.calTime(1);
                getData();
            } else if (vId == R.id.btn_curr_weight) {
                isGraphActive = false;
                layout_curr_weight.setVisibility(View.VISIBLE);
                layout_weight_graph.setVisibility(View.GONE);
                layout_weight_history.setVisibility(View.GONE);
                weight_graph_history_layout.setVisibility(View.GONE);
//                Hcallbtn.setVisibility(View.VISIBLE);
                btn_curr_weight.setSelected(true);
                btn_weight_graph.setSelected(false);
                btn_weight_history.setSelected(false);
                requestWeightData();

            } else if (vId == R.id.btn_weight_graph) {
                isGraphActive = true;
                layout_curr_weight.setVisibility(View.GONE);
                layout_weight_graph.setVisibility(View.VISIBLE);
                layout_weight_history.setVisibility(View.GONE);
//                Hcallbtn.setVisibility(View.GONE);
                weight_graph_history_layout.setVisibility(View.VISIBLE);
                btn_curr_weight.setSelected(false);
                btn_weight_graph.setSelected(true);
                btn_weight_history.setSelected(false);

                getData();
                getBottomDataLayout();
            } else if (vId == R.id.btn_weight_history) {
                isGraphActive = false;
                layout_curr_weight.setVisibility(View.GONE);
                layout_weight_graph.setVisibility(View.GONE);
                layout_weight_history.setVisibility(View.VISIBLE);
//                Hcallbtn.setVisibility(View.GONE);
                weight_graph_history_layout.setVisibility(View.VISIBLE);
                btn_curr_weight.setSelected(false);
                btn_weight_graph.setSelected(false);
                btn_weight_history.setSelected(true);

                mSwipeListView.getHistoryData();
            } else if (vId == R.id.weight_modal_btn) {
                new showModifiDlg();
            } else if (vId == R.id.landscape_btn) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else if (vId == R.id.chart_close_btn) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }else if(vId == R.id.target_value_btn){
//                ((MotherHealthMainActivity)getContext()).actionBtnClick();
            } else if (vId == R.id.Hcall_btn) {
                if (CommonData.getInstance(getContext()).getMberGrad().equals("10")) {
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
                } else {
                    CustomAlertDialog mDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.call_center2));
                    mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getString(R.string.do_call), (dialog, button) -> {
                        String tel = "tel:" + getString(R.string.call_center_number2);
//                        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                        Intent intentCall = new Intent(Intent.ACTION_DIAL);
                        intentCall.setData(Uri.parse(tel));
                        startActivity(intentCall);
                        dialog.dismiss();
                    });
                    mDialog.show();
                }
            } else if (vId == R.id.tip_lv) {
                Intent intent = new Intent(getActivity(), TipWebViewActivity.class);
                intent.putExtra("Title", getString(R.string.psy_tip));
                intent.putExtra(CommonData.EXTRA_URL_POSITION, 0);
                startActivity(intent);
            }
            setNextButtonVisible();
        }
    };

    private void setNextButtonVisible() {
        // 초기값 일때 다음날 데이터는 없으므로 리턴
        if (mTimeClass.getCalTime() == 0) {
            imgNext_btn.setVisibility(View.INVISIBLE);
        } else {
            imgNext_btn.setVisibility(View.VISIBLE);
        }

        //임신여부
        CommonData common = CommonData.getInstance(getContext());
        String materPregency = common.getbirth_chl_yn(); //임신 중 N, 출산 후 Y


        // 임신중인경우 차트 세팅
        if ("N".equals(materPregency)) {
            radioBtnYear.setText(R.string.임신기간40주);
            radioBtnMonth.setVisibility(View.GONE);

            radioBtnDay.setContentDescription(getString(R.string.radioBtnPregencyDay));
            radioBtnWeek.setContentDescription(getString(R.string.radioBtnPregencyWeek));
            radioBtnMonth.setContentDescription("");
            radioBtnYear.setContentDescription(getString(R.string.radioBtnPregencyYear));
        } else {
            radioBtnYear.setText(R.string.년간);
            radioBtnMonth.setVisibility(View.VISIBLE);

            radioBtnDay.setContentDescription(getString(R.string.radioBtnWeightDay));
            radioBtnWeek.setContentDescription(getString(R.string.radioBtnWeightWeek));
            radioBtnMonth.setContentDescription(getString(R.string.radioBtnWeightMonth));
            radioBtnYear.setContentDescription(getString(R.string.radioBtnWeightYear));
        }
    }

    /**
     * 일간,주간,월간,임신기간(40주)
     */
    public RadioGroup.OnCheckedChangeListener mCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            TypeDataSet.Period periodType = mTimeClass.getPeriodType();
            mTimeClass.clearTime();         // 날자 초기화


            if (periodType == TypeDataSet.Period.PERIOD_YEAR
                    && ("N".equals(commonData.getbirth_chl_yn()))) {
                // 년간 선택시 임신일 경우 X축 설정
                xFormatter = new AxisValueFormatter2(TypeDataSet.Period.PERIOD_PRAGNANT);
                yFormatter = new AxisYValueFormatter(TypeDataSet.Period.PERIOD_PRAGNANT);
            } else {
                xFormatter = new AxisValueFormatter2(periodType);
                yFormatter = new AxisYValueFormatter(periodType);
            }

            mWeightChart.setYValueFormat(yFormatter);
            mWeightChart.setXValueFormat(xFormatter);
            mFatChart.setXValueFormat(xFormatter);

            getData();   // 날자 세팅 후 조회
        }
    };

    /**
     * 날자 계산 후 조회
     */
    protected void getData() {
        long startTime = mTimeClass.getStartTime();
        long endTime = mTimeClass.getEndTime();

        mWeightChart.getBarChart().setDrawMarkers(false); // 데이터 변경 될때 마커뷰 사라지게 하기

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");

        String startDate = sdf.format(startTime);
        String endDate = sdf.format(endTime);

        if (mTimeClass.getPeriodType() == TypeDataSet.Period.PERIOD_DAY) {
            mDateTv.setText(startDate);
        } else if (mTimeClass.getPeriodType() == TypeDataSet.Period.PERIOD_YEAR) {
            mDateTv.setText(yearSdf.format(startTime));
        } else {
            mDateTv.setText(startDate + " ~ " + endDate);
        }

        new QeuryVerifyDataTask().execute();
    }

    public class QeuryVerifyDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgress();

            Logger.i(TAG, "getContext is " + getContext());

            if (getContext() == null) {
                Logger.e(TAG, "getContext is Null");
                return;
            }

//            DBHelper helper = new DBHelper(getContext());
//            DBHelperWeight weightDb = helper.getWeightDb();
//
//            mWeightChart.setIsPragnant(false);  // 임신여부
//            weight_chart_date_layout.setVisibility(View.VISIBLE);
//            TypeDataSet.Period period = mTimeClass.getPeriodType();
//            if (period == TypeDataSet.Period.PERIOD_DAY) {
//                mXLabelTv.setText("(시)");
//                mGraphHint.setVisibility(View.INVISIBLE);
//                mWeightChart.setXvalMinMax(-1, 24, 24);
//                mFatChart.setXvalMinMax(-1, 24, 24);
//
//                String toDay = CDateUtil.getFormattedString_yyyy_MM_dd(mTimeClass.getStartTime());
////                List<BarEntry> weightYVals = weightDb.getResultDay(toDay, true);
////                setYMinMax(weightYVals, false);
////                mWeightChart.setData(weightYVals, mTimeClass);
////                List<BarEntry> fatYVals = weightDb.getResultDay(toDay, false);
//            } else if (period == TypeDataSet.Period.PERIOD_WEEK) {
//                mXLabelTv.setText("");
//                mGraphHint.setVisibility(View.INVISIBLE);
//                mWeightChart.setXvalMinMax(0, 8, 9);
////                mFatChart.setXvalMinMax(0, 8, 9);
//
//                String startDay = CDateUtil.getFormattedString_yyyy_MM_dd(mTimeClass.getStartTime());
//                String endDay = CDateUtil.getFormattedString_yyyy_MM_dd(mTimeClass.getEndTime());
////                List<BarEntry> weightYVals = weightDb.getResultWeek(startDay, endDay, true);
////
////                setYMinMax(weightYVals, false);
////
////                mWeightChart.setData(weightYVals, mTimeClass);
////                List<BarEntry> fatYVals = weightDb.getResultWeek(startDay, endDay, false);
//            } else if (period == TypeDataSet.Period.PERIOD_MONTH) {
//                mXLabelTv.setText("(일)");
//                mGraphHint.setVisibility(View.INVISIBLE);
//                int maxX = mTimeClass.getStartTimeCal().getActualMaximum(Calendar.DAY_OF_MONTH) + 1;
//                xFormatter.setMonthMax(maxX);
//                mWeightChart.setXvalMinMax(0, maxX, maxX);
////                mFatChart.setXvalMinMax(0, maxX, maxX);
//
//                String startDay = CDateUtil.getFormattedString_yyyy(mTimeClass.getStartTime());
//                String endDay = CDateUtil.getFormattedString_MM(mTimeClass.getStartTime());
////                List<BarEntry> weightYVals = weightDb.getResultMonth(startDay, endDay, true);
////
////                setYMinMax(weightYVals, false);
////
////                mWeightChart.setData(weightYVals, mTimeClass);
////                List<BarEntry> fatYVals = weightDb.getResultMonth(startDay, endDay, false);
//            } else if (period == TypeDataSet.Period.PERIOD_YEAR) {
//                mXLabelTv.setText("(월)");
//                // 년간 차트 그리기
//                weight_chart_date_layout.setVisibility(View.VISIBLE);
//                mGraphHint.setVisibility(View.INVISIBLE);
//                String startDay = CDateUtil.getFormattedString_yyyy(mTimeClass.getStartTime());
////                    List<BarEntry> weightYVals = weightDb.getResultYear(startDay, true);
////
////                    setYMinMax(weightYVals, false);
////
////                    mWeightChart.setXvalMinMax(0, 12+1, 15);    // 끝에 짤리는것 때문에 13개월로 함(원래 12개월)
////                    mWeightChart.setData(weightYVals, mTimeClass);
//
//            }
//
//            mWeightChart.animateY();
////            mFatChart.animateY();
//            setNextButtonVisible();
        }
    }

    /**
     * y라벨 구하기
     *
     * @param weightYVals
     */
    private void setYMinMax(List<BarEntry> weightYVals, boolean is40Data) {
        float yMin = Float.MAX_VALUE;
        float yMax = Float.MIN_VALUE;
        Log.i(TAG, "#######yLabelCnt##############");
        for (BarEntry entry : weightYVals) {
            float y = entry.getY();
            if (y != 0 && y < yMin) {
                yMin = y;
            }

            if (y != 0 && y > yMax) {
                yMax = y;
            }
        }

        // y min값이 없는 경우
        if (yMin == Float.MAX_VALUE && is40Data == false) {
            yMin = StringUtil.getFloat(commonData.getMotherWeight()) - 3;
        }
        // y max값이 없는 경우
        if (yMax == Float.MIN_VALUE && is40Data == false) {
            yMax = yMin + 3;
        }

        int yLabelCnt = (int) (yMax - yMin);
        mWeightChart.setYvalMinMax(yMin - 3, yMax + 3, yLabelCnt + 6); // 최소값 최대값이 없으면 임의로 넣어줌
//        Log.i(TAG, "yMin="+yMin+", yMax="+yMax+", yLabelCnt="+yLabelCnt);
    }


    /**
     * 하단 데이터 세팅하기
     */
    private void getBottomDataLayout() {
//        try {
//            CommonData login = CommonData.getInstance(getContext());
//            mWeightTargetTv.setText(login.getMotherGoalWeight());
//
//            DBHelper helper = new DBHelper(getContext());
//            DBHelperWeight WeightDb = helper.getWeightDb();
//            DBHelperWeight.WeightStaticData bottomData = WeightDb.getResultStatic(helper);
//            mWeightStaticData = bottomData;
//
//            String dataWeight = "";
//            if (TextUtils.isEmpty(bottomData.getWeight())) {
//                dataWeight = "0";
//            } else {
//                dataWeight = bottomData.getWeight();
//            }
//
////            mWeightTv.setText(StringUtil.getNoneZeroString(StringUtil.getFloatVal(dataWeight)));
//            mWeightTv.setText(commonData.getMotherWeight());
//            float temp = StringUtil.getFloat(dataWeight) - StringUtil.getFloat(login.getMotherGoalWeight());
//            if (bottomData.getWeight().isEmpty()) {
//                mWeightTargetWaitTv.setText("--");
//            } else if (temp > 0) {
//                mWeightTargetWaitTv.setText("+" + String.format("%.1f", temp));
//            } else {
//                mWeightTargetWaitTv.setText(String.format("%.1f", temp));
//            }
//
//            if (bottomData.getWeight().isEmpty()) {
//                mWeightDayTv.setText(getString(R.string.recent_measurements));
//            } else {
//                String time = CDateUtil.getForamtyyyyMMddHHmm(new Date(System.currentTimeMillis()));
//                String timeStr = bottomData.getRegdate().substring(0, 4) + bottomData.getRegdate().substring(5, 7) + bottomData.getRegdate().substring(8, 10) + bottomData.getRegdate().substring(11, 13) + bottomData.getRegdate().substring(14, 16);
//                int dayTime = Integer.parseInt(time.substring(0, 8));
//                int dayTimeStr = Integer.parseInt(timeStr.substring(0, 8));
//
//                if (dayTime == dayTimeStr) {
//                    mWeightDayTv.setText(getString(R.string.today_measurements));
//                } else {
//                    if (dayTime > dayTimeStr) {
//
//                        mWeightDayTv.setText("최근 측정(" + getDateDiff("" + dayTimeStr) + "일전)");
//                    } else {
//                        mWeightDayTv.setText("");
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private long getDateDiff(String bDate) {

        int year = StringUtil.getIntVal(bDate.substring(0, 4));
        int month = StringUtil.getIntVal(bDate.substring(4, 6));
        int day = StringUtil.getIntVal(bDate.substring(6, 8));
        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH, day);
        thatDay.set(Calendar.MONTH, month - 1);
        thatDay.set(Calendar.YEAR, year);

        Calendar today = Calendar.getInstance();
        long diff = today.getTimeInMillis() - thatDay.getTimeInMillis(); //result in millis
        long days = diff / (24 * 60 * 60 * 1000);
        return days;
    }

    class showModifiDlg {
        private TextView mBmrTv;
        private TextView mBmiTv;
        private TextView mObesityTv;
        private TextView mFatTv;
        private TextView mBodyWaterTv;
        private TextView mMuscleTv;
        private Button mConfirmBtn;

        /**
         * 상세정보 Dialog 띄우기
         **/
        private showModifiDlg() {
//            View modifyView = LayoutInflater.from(getContext()).inflate(R.layout.activity_weight_modal, null);
//
//            mBmrTv = (TextView) modifyView.findViewById(R.id.weight_bmr_textxview);
//            mBmiTv = (TextView) modifyView.findViewById(R.id.weight_bmi_textxview);
//            mObesityTv = (TextView) modifyView.findViewById(R.id.weight_obesity_textxview);
//            mFatTv = (TextView) modifyView.findViewById(R.id.weight_fat_textxview);
//            mBodyWaterTv = (TextView) modifyView.findViewById(R.id.weight_bodywater_textxview);
//            mMuscleTv = (TextView) modifyView.findViewById(R.id.weight_muscle_textxview);
//
//            if (mWeightStaticData.getWeight().isEmpty()) {
//                mBmrTv.setText("0" + " kcal");
//                mBmiTv.setText("0" + " kg/m2");
//                mObesityTv.setText("0" + " %");
//                mFatTv.setText("0" + " %");
//                mBodyWaterTv.setText("0" + " %");
//                mMuscleTv.setText("0" + " %");
//            } else {
//                CommonData login = CommonData.getInstance(getContext());
////                mWeightStaticData.setWeight(login.getMotherWeight());
//
//                float rHeight = StringUtil.getFloat(login.getBefCm());
//                float rWeight = StringUtil.getFloat(mWeightStaticData.getWeight());
//                int rSex = Integer.parseInt(login.getGender());
//                int rAge = Integer.parseInt(login.getBirthDay().substring(0, 4));
//                String nowYear = CDateUtil.getFormattedString_yyyy(System.currentTimeMillis());
//
//                float Float_result = 0.0f;
//                if (rSex == 1) {
//                    Float_result = (float) (66.47f + (13.75f * rWeight) + (5.0f * rHeight) - (6.76f * ((StringUtil.getFloat(nowYear) - rAge) + 1)));
//                } else {
//                    Float_result = (float) (655.1f + (9.56f * rWeight) + (1.85f * rHeight) - (4.68f * ((StringUtil.getFloat(nowYear) - rAge) + 1)));
//                }
//
//                mBmrTv.setText(Integer.toString((int) Float_result) + " kcal");
//                mBmiTv.setText(String.format("%.1f", StringUtil.getFloatVal(mWeightStaticData.getWeight()) / ((StringUtil.getFloat(login.getBefCm()) / 100) * (StringUtil.getFloat(login.getBefCm()) / 100))) + " kg/m2");
//                mFatTv.setText(mWeightStaticData.getObesity() + " %");
//                mObesityTv.setText(mWeightStaticData.getFat() + " %");
//                mBodyWaterTv.setText(mWeightStaticData.getBodyWater() + " %");
//                mMuscleTv.setText(mWeightStaticData.getMuscle() + " %");
//            }
//
//            final CPDialog dlg = CPDialog.showDlg(getContext(), modifyView);
//            dlg.setTitle(getString(R.string.text_more_info));
//            mConfirmBtn = (Button) dlg.findViewById(R.id.right_confirm_btn_close);
//            mConfirmBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dlg.dismiss();
//                }
//            });
        }

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();  // 차트 데이터 Refresh
        getBottomDataLayout(); // 하단 데이터 Refresh
        mSwipeListView.getHistoryData();    // 히스토리 Refresh
        requestWeightData();
    }

    @Override
    public void onStop() {
        super.onStop();
        isGraphActive = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isGraphActive = false;
    }

    private TextView regDateTxt;
    private TextView curWeightTxt;
    private TextView commentTxt01;
    private TextView commentTxt01_1;
    private TextView commentTxt01_2;
    private TextView commentTxt01_3;
    private TextView commentTxt01_4;
    private TextView commentTxt02;
    private LinearLayout mom_set_lv;
    private LinearLayout tip_lv;


    public void initCurrWeightView(WeightCurrView view) {
        regDateTxt = (TextView) layout_curr_weight.findViewById(R.id.regDateTxt);
        curWeightTxt = (TextView) layout_curr_weight.findViewById(R.id.curWeightTxt);
        commentTxt01 = (TextView) layout_curr_weight.findViewById(R.id.commentTxt01);
        commentTxt01_1 = (TextView) layout_curr_weight.findViewById(R.id.commentTxt01_1);
        commentTxt01_2 = (TextView) layout_curr_weight.findViewById(R.id.commentTxt01_2);
        commentTxt01_3 = (TextView) layout_curr_weight.findViewById(R.id.commentTxt01_3);
        commentTxt01_4 = (TextView) layout_curr_weight.findViewById(R.id.commentTxt01_4);
        commentTxt02 = (TextView) layout_curr_weight.findViewById(R.id.commentTxt02);
        commentTxt02.setMovementMethod(ScrollingMovementMethod.getInstance());

        mom_set_lv = (LinearLayout) layout_curr_weight.findViewById(R.id.mom_set_lv);
        tip_lv = (LinearLayout) layout_curr_weight.findViewById(R.id.tip_lv);

        tip_lv.setOnClickListener(mClickListener);


    }

    public void requestWeightData() {
        Tr_mber_mother_bdwgh_view.RequestData requestData = new Tr_mber_mother_bdwgh_view.RequestData();
        CommonData login = CommonData.getInstance(getContext());
        requestData.mber_sn = login.getMberSn();

        getData(getContext(), Tr_mber_mother_bdwgh_view.class, requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_mber_mother_bdwgh_view) {
                    Tr_mber_mother_bdwgh_view data = (Tr_mber_mother_bdwgh_view) obj;
                    if (data.data_yn.equals("Y")) {

                        String yy = data.input_de.substring(0, 4);
                        String mm = data.input_de.substring(4, 6);
                        String dd = data.input_de.substring(6, 8);
                        regDateTxt.setText(yy + "." + mm + "." + dd);
                        commentTxt02.setText(data.comment2);
                        commonData.setbirth_chl_yn(data.birth_chl_yn); //임신여부 : N : 임신 중 , Y : 출산후

                        if (TextUtils.isEmpty(data.now_weight) == false && StringUtil.getIntVal(data.now_weight) > 0) {
                            curWeightTxt.setText(String.format("%.2f", StringUtil.getFloatVal(data.now_weight)) + "kg");
                            commonData.setMotherWeight(data.now_weight);
                        } else {
                            curWeightTxt.setText("__kg");
                        }

                        // 임신중 여부
                        if ("N".equals(commonData.getbirth_chl_yn())) {
                            //임신중
                            if (TextUtils.isEmpty(data.kg_kind) == false) {
                                commonData.setKg_Kind(data.kg_kind);
                            }
                        } else {
                            //출산후
                            if (TextUtils.isEmpty(data.bmi_kind) == false) {
                                commonData.setKg_Kind(data.bmi_kind + "군");
                            }
                        }

                        if (TextUtils.isEmpty(data.bmi) == false) {
                            commonData.setBmi(data.bmi);
                        }


//                        curWeightTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.color_E1147F));
                        Log.i(TAG, "colorChange: " + data.comment1);

                        initWeighCurrData(data);

                        //임신여부
                        CommonData common = CommonData.getInstance(getContext());
                        String materPregency = common.getbirth_chl_yn(); //임신 중 N, 출산 후 Y

                        layout_curr_weight.findViewById(R.id.target_value_btn).setVisibility("N".equals(materPregency) ? View.GONE : View.VISIBLE);
                        weight_graph_history_layout.findViewById(R.id.target_value_btn).setVisibility("N".equals(materPregency) ? View.GONE : View.VISIBLE);

                        if (commonData.getMberGrad().equals("10")) {
                            if ("N".equals(materPregency)) {
                                layout_curr_weight.findViewById(R.id.req_diet_btn).setVisibility(View.GONE);
                            } else {
                                // 출산일 후 1년 이내인지
                                if (StringUtil.getAfterBirth1Year(commonData.getLastChlBirth())) {
                                    layout_curr_weight.findViewById(R.id.req_diet_btn).setVisibility(View.VISIBLE);
                                } else {
                                    layout_curr_weight.findViewById(R.id.req_diet_btn).setVisibility(View.GONE);
                                }
                            }
                        } else {
                            layout_curr_weight.findViewById(R.id.req_diet_btn).setVisibility(View.GONE);
                        }

                        layout_curr_weight.findViewById(R.id.pregnant_lv).setVisibility("N".equals(materPregency) ? View.VISIBLE : View.GONE);
                        layout_curr_weight.findViewById(R.id.mom_weight_prediction_btn).setVisibility("N".equals(materPregency) ? View.VISIBLE : View.GONE);


                        mMother_period.setText("임신 " + data.mother_period_week + "주 " + data.mother_period_day + "일째 : ");
                        mMother_week.setText(data.mother_week);


                        commonData.setMberPeriodWeek(data.mother_period_week);


                        bmi1 = data.mother_14_week;
                        bmi2 = data.mother_26_week;
                        bmi3 = data.mother_40_week;
                        bmi4 = data.mother_all_week;
                        bmi5 = data.bmi_kind;


                    } else {
                        Toast.makeText(getActivity(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, null);
    }

    /**
     * 목표체중등록
     *
     * @param wt
     */
    public void requestRevcGoalWeight(String wt) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();

            object.put("api_code", "mvm_goalqy");
            object.put("insures_code", "108");
            object.put("mber_sn", CommonData.getInstance(getContext()).getMberSn());

            object.put("goal_mvm_calory", "");
            object.put("goal_mvm_stepcnt", "");
            object.put("goal_bdwgh", wt);

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));
            RequestApi.requestApi(getActivity(), NetworkConst.NET_MOTHER_REVC_GOAL, NetworkConst.getInstance().getDefDomain(), networkListener, params, new MakeProgress(getActivity()));

            CommonData.getInstance(getContext()).setMotherGoalWeight(wt);
        } catch (Exception e) {
            Log.i(e.toString(), "dd");
            e.printStackTrace();
        }
    }

    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {
        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            hideProgress();
            switch (type) {
                case NetworkConst.NET_MOTHER_REVC_GOAL:
                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            requestWeightData();
                            break;
                    }
                    break;
            }
        }

        @Override
        public void onNetworkError(Context context, int type,
                                   int httpResultCode, CustomAlertDialog dialog) {
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog
                dialog) {
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.

            dialog.show();

        }
    };

    public void setColorchange(String text) {
        if (text.contains("저체중군")) {
            commentTxt01.setText(text);
            commentTxt01.setTextColor(ContextCompat.getColor(getContext(), R.color.color_f3ab49));
        } else if (text.contains("정상체중군")) {
            commentTxt01.setText(text);
            commentTxt01.setTextColor(ContextCompat.getColor(getContext(), R.color.color_8dc349));

        } else if (text.contains("과체중군")) {
            commentTxt01.setText(text);
            commentTxt01.setTextColor(ContextCompat.getColor(getContext(), R.color.color_e65739));

        } else if (text.contains("비만군")) {
            commentTxt01.setText(text);
            commentTxt01.setTextColor(ContextCompat.getColor(getContext(), R.color.color_a49bc8));

        } else if (text.contains("고도비만")) {
            commentTxt01.setText(text);
            commentTxt01.setTextColor(ContextCompat.getColor(getContext(), R.color.color_717173));
        }
    }

    // 최고 32에서 13 빼고 시작한다
    private float totalSpan = 18; //(32 - 14)
    private float span01 = 4.5f;
    private float span02 = 4.5f;
    private float span03 = 2;
    private float span04 = 5;
    private float span05;

    public void initWeighCurrData(Tr_mber_mother_bdwgh_view resultData) {

        ArrayList<ProgressItem> progressItemList = new ArrayList<ProgressItem>();
        ProgressItem mProgressItem;

        if (resultData.birth_chl_yn.compareTo("Y") == 0) {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = (int) getResources().getDimension(R.dimen._14_dp);
            params.addRule(RelativeLayout.BELOW, R.id.seekbar_rl);
            mom_set_lv.setLayoutParams(params);

            final int mValue = 14;
            totalSpan = 18; //(32 - 14)
            span01 = 4.5f;
            span02 = 4.5f;
            span03 = 2;
            span04 = 5;
            //       span05 = 0;
            /*
            *
            * BMI = 체중(kg)/키(m)*키(m)
                예) 175센티에 80kg이라면
                   BMI = 80/(1.75 * 1.75)

            *
            * */

            float curHeight = StringUtil.getFloatVal(resultData.kg_kind) * 0.01f;
            float curWeight = StringUtil.getFloatVal(resultData.now_weight);
            float goalWeight = 0;

            float goalBmi = 0;
            float curBmi = (curWeight / (curHeight * curHeight)) - mValue;

            if (resultData.mber_bdwgh_goal.compareTo("") == 0 ||
                    resultData.mber_bdwgh_goal.compareTo("0") == 0) {
                setColorchange("[" + resultData.bmi_kind + "군]");
//                commentTxt01.setText(resultData.comment1);

                commentTxt01_1.setText("나의 목표체중 ");
                commentTxt01_2.setVisibility(View.VISIBLE);
                commentTxt01_3.setVisibility(View.VISIBLE);
                commentTxt01_2.setText("(임신 전 체중)  ");
                commentTxt01_3.setText(String.format("%.2f", StringUtil.getFloatVal(resultData.mber_term_kg)) + " kg");
                commentTxt01_4.setText(setTargetText(StringUtil.getFloatVal(resultData.mber_term_kg), curWeight));

                goalWeight = StringUtil.getFloatVal(resultData.mber_term_kg);
                goalBmi = (goalWeight / (curHeight * curHeight)) - mValue;

            } else {
                setColorchange("[" + resultData.bmi_kind + "군]");
//                commentTxt01.setText(resultData.comment1);


                commentTxt01_1.setText("나의 목표체중  ");
                commentTxt01_2.setVisibility(View.GONE);
                commentTxt01_3.setVisibility(View.VISIBLE);
                commentTxt01_3.setText(String.format("%.2f", StringUtil.getFloatVal(resultData.mber_bdwgh_goal)) + " kg");
                commentTxt01_4.setText(setTargetText(StringUtil.getFloatVal(resultData.mber_bdwgh_goal), curWeight));

                goalWeight = StringUtil.getFloatVal(resultData.mber_bdwgh_goal);
                goalBmi = (goalWeight / (curHeight * curHeight)) - mValue;
            }

            mProgressItem = new ProgressItem();

            mProgressItem.progressItemPercentage = ((span01 / totalSpan) * 100);
            Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
            mProgressItem.color = R.color.color_f3ab49;
            mProgressItem.pointStr = "18.5";
            mProgressItem.descStr = "저체중";
            mProgressItem.bottomStr = resultData.bmi_skg;

            progressItemList.add(mProgressItem);
            // blue span
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = (span02 / totalSpan) * 100;
            mProgressItem.color = R.color.color_8dc349;
            mProgressItem.pointStr = "23";
            mProgressItem.descStr = "정상체중";
            mProgressItem.bottomStr = resultData.bmi_ekg;
            progressItemList.add(mProgressItem);
            // green span
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = (span03 / totalSpan) * 100;
            mProgressItem.color = R.color.color_e65739;
            mProgressItem.pointStr = "25";
            mProgressItem.descStr = "과체중";
            mProgressItem.bottomStr = "";
            progressItemList.add(mProgressItem);
            // yellow span
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = (span04 / totalSpan) * 100;
            mProgressItem.color = R.color.color_a49bc8;
            mProgressItem.pointStr = "30";
            mProgressItem.descStr = "비만";
            mProgressItem.bottomStr = "";
            progressItemList.add(mProgressItem);
            // greyspan
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = (span05 / totalSpan) * 100;
            mProgressItem.color = R.color.color_717173;
            mProgressItem.pointStr = "";
            mProgressItem.descStr = "고도비만";
            mProgressItem.bottomStr = "";
            progressItemList.add(mProgressItem);

            layout_curr_weight.initData(progressItemList, curBmi, goalBmi);

        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = (int) getResources().getDimension(R.dimen.mom_set_lv);
            params.addRule(RelativeLayout.BELOW, R.id.seekbar_rl);
            mom_set_lv.setLayoutParams(params);
            /*
            *
            * BMI = 체중(kg)/키(m)*키(m)
                예) 175센티에 80kg이라면
                   BMI = 80/(1.75 * 1.75)
            *
            * */
            commentTxt01.setText(resultData.kg_kind.equals("부족") ? "적정체중 범위 " + resultData.comment1_kg + "kg 부족" :
                    resultData.kg_kind.equals("권장") ? "권장체중 범위" : "적정체중 범위 " + resultData.comment1_kg + "kg 초과");
            commentTxt01.setTextColor(getResources().getColor(R.color.color_FB8AD3));

            commentTxt01_1.setText("임신기간 체중 예측");
            commentTxt01_2.setVisibility(View.GONE);
            commentTxt01_3.setVisibility(View.GONE);
            commentTxt01_4.setText("빅데이터를 활용하여 예상 체중을 보여드립니다.");

            float mn_weight = StringUtil.getFloatVal(resultData.mn_weight);
            float mm_weight = StringUtil.getFloatVal(resultData.mm_weight);

            //ex mn_weight = 50;, mm_weight = 60; 경우
            float standard = (mm_weight - mn_weight) * 2; //20
            float max_weight = standard + (mm_weight - mn_weight) + standard; //50
            float curWeight = StringUtil.getFloatVal(resultData.now_weight); //

            totalSpan = max_weight; //50
            span01 = standard;      //20
            span02 = (mm_weight - mn_weight);   //20 + 10
            span03 = standard;                  //20 + 10 + 20

            mProgressItem = new ProgressItem();

            mProgressItem.progressItemPercentage = ((span01 / totalSpan) * 100);
            Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
            mProgressItem.color = R.color.color_f3ab49;
            mProgressItem.pointStr = "";
            mProgressItem.descStr = "부족";
            mProgressItem.bottomStr = resultData.mn_weight;

            progressItemList.add(mProgressItem);
            // blue span
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = (span02 / totalSpan) * 100;
            mProgressItem.color = R.color.color_8dc349;
            mProgressItem.pointStr = "";
            mProgressItem.descStr = "권장 체중";
            mProgressItem.bottomStr = resultData.mm_weight;

            progressItemList.add(mProgressItem);
            // green span
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = (span03 / totalSpan) * 100;
            mProgressItem.color = R.color.color_e65739;
            mProgressItem.pointStr = "";
            mProgressItem.descStr = "초과";
            mProgressItem.bottomStr = "";
            progressItemList.add(mProgressItem);

            float tmp = mn_weight - standard;
            int curWeightInt = (int) (((curWeight - tmp) / totalSpan) * 100);

            float goalWeightFloat = StringUtil.getFloat(resultData.mber_bdwgh_goal);

            int goalWeightInt = (int) (((goalWeightFloat - tmp) / totalSpan) * 100);

            layout_curr_weight.initData(progressItemList, curWeightInt, goalWeightInt, false);

        }

    }


    protected boolean isLandScape = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        GLog.i("onConfigurationChanged=" + newConfig.orientation, "");
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE: //가로 모드
                isLandScape = true;
                break;
            case Configuration.ORIENTATION_PORTRAIT: //세로 모드
                isLandScape = false;
                break;
        }

        setVisibleOrientationLayout();
    }

    /**
     * 출산 후 목표 체중 달성에 따른 문구
     */

    private String setTargetText(float target, float weight) {
        if (weight > target) {
            return String.format("현재 체중에서 %.2fkg 체중 감량이 필요합니다.", weight - target);
        } else if (weight == target) {
            return "축하합니다! 목표 체중을 달성했습니다. 새로운 목표를 설정해 체중관리에 도전하세요.";
        } else if (target > weight) {
            return String.format("현재 체중에서 %.2fkg 체중 증가가 필요합니다.", target - weight);
        }
        return "";
    }

    /**
     * 가로, 세로모드일때 불필요한 화면 Visible 처리
     */
    protected void setVisibleOrientationLayout() {
        mVisibleView1.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView2.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView3.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView4.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mHCallBtn.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mChartCloseBtn.setVisibility(isLandScape ? View.VISIBLE : View.GONE);
        mChartZoomBtn.setVisibility(isLandScape ? View.GONE : View.VISIBLE);

        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mChartFrameLayout.getLayoutParams();
        Log.i(TAG, "isLandScape=" + isLandScape + ", dm.widthPixels=" + dm.widthPixels + ", dm.heightPixels=" + dm.heightPixels);

//        int height = (int) (dm.heightPixels - mDateLayout.getLayoutParams().height);//(dm.heightPixels *0.20)); // 15% 작게
        int landHeight = (int) (dm.heightPixels - dm.heightPixels * 0.30); // 가로모드 세로사이즈 30% 작게
        int portHeight = DisplayUtil.getDpToPix(getContext(), mChartFrameLayout.getMeasuredHeight());    // 세로모드일때 사이즈 230dp
        params.height = isLandScape ? landHeight : LinearLayout.LayoutParams.MATCH_PARENT;

        mChartFrameLayout.setLayoutParams(params);
        // 가로모드일때 스크롤뷰 막기
        mContentScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isLandScape;
            }
        });
        //가로모드 전환 시 스크롤 상단으로 위치
        mContentScrollView.smoothScrollTo(0, 0);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.i(TAG, "onActivityResult.requestCode=" + requestCode);
////        if (resultCode == Activity.RESULT_OK && requestCode == WeightBigDataInputFragment.REQ_WEIGHT_PREDICT) {
////            if (BuildConfig.DEBUG) {
////                radioBtnYear.performClick();    // 차트 년간 버튼
////                btn_weight_graph.performClick();
////
////                String weight = data.getStringExtra(WeightBigDataInputFragment.INTENT_KEY_WEIGHT);
////                String week = data.getStringExtra(WeightBigDataInputFragment.INTENT_KEY_WEEK);
////                doWeightTotalGrp();
////                if (TextUtils.isEmpty(week) && TextUtils.isEmpty(week))
////                    return;
////                doWeightHopeGrp(weight, week);
////            }
////        }
//    }
}