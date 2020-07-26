package com.greencross.gctemperlib.greencare.temper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.data.BarEntry;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.AxisValueFormatter2;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.AxisYValueFormatter;
import com.greencross.gctemperlib.greencare.chartview.temper.TemperChartView;
import com.greencross.gctemperlib.greencare.component.CDatePicker;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.greencare.network.tr.hnData.Tr_FeverList;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.ChartTimeUtil;
import com.greencross.gctemperlib.greencare.util.DisplayUtil;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * 체온 그래프
 */

public class TemperGraphFragment extends BaseFragment {
    private final String TAG = TemperGraphFragment.class.getSimpleName();

    int mRequestCode = 1111;
    Boolean isGraphActive = false;

    public CommonData commonData = CommonData.getInstance(getContext());

    public ChartTimeUtil mTimeClass;
    private TextView mDateTv;
    private TextView mTemperTargetTv;
    private TextView mTemperTv;
    private TextView mTemperTargetWaitTv;
    private TextView mTemperDayTv;
    private TextView chartRule;

    private TextView mMother_week;

    protected TemperChartView mTemperChart;

    private TemperSwipeListView mSwipeListView;

    protected LinearLayout layout_weight_graph;              // 그래프 레이아웃
    protected LinearLayout weight_chart_date_layout;      // 차트 상단 날자 레이아웃

    protected TextView mXLabelTv;

    private ImageButton imgPre_btn;
    private ImageButton imgNext_btn;
//    private ImageView Hcallbtn; //, Action_btn;

    //    private DBHelperTemper.TemperStaticData mTemperStaticData;
    private AxisValueFormatter2 xFormatter;
    private AxisYValueFormatter yFormatter;

    private View mVisibleView1;
    private View mVisibleView3;
    private View mVisibleView4;
    private ScrollView mContentScrollView;
    private LinearLayout mChartFrameLayout;
    private ImageView mChartCloseBtn, mChartZoomBtn;


    public static Fragment newInstance() {
        TemperGraphFragment fragment = new TemperGraphFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temper_graph_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof DummyActivity) {
            ((DummyActivity) getActivity()).setTitle(getString(R.string.temper_graph));
        }

        mDateTv = (TextView) view.findViewById(R.id.period_date_textview);
        mTemperTargetTv = (TextView) view.findViewById(R.id.textView54);
        mTemperTv = (TextView) view.findViewById(R.id.textView52);
        mTemperTargetWaitTv = (TextView) view.findViewById(R.id.textView57);
        mTemperDayTv = (TextView) view.findViewById(R.id.textView18);
        chartRule = (TextView) view.findViewById(R.id.chart_rule);
        layout_weight_graph = (LinearLayout) view.findViewById(R.id.layout_weight_graph);
        weight_chart_date_layout = view.findViewById(R.id.weight_chart_date_layout);

        mXLabelTv = view.findViewById(R.id.weight_chart_x_label_tv);

        imgPre_btn = (ImageButton) view.findViewById(R.id.pre_btn);
        imgNext_btn = (ImageButton) view.findViewById(R.id.next_btn);

        view.findViewById(R.id.pre_btn).setOnClickListener(mClickListener);
        view.findViewById(R.id.next_btn).setOnClickListener(mClickListener);
        view.findViewById(R.id.weight_modal_btn).setOnClickListener(mClickListener);
        TextView preCalendarTv = view.findViewById(R.id.graph_pre_textview);
        TextView nextCalendarTv = view.findViewById(R.id.graph_next_textview);
        preCalendarTv.setOnClickListener(mClickListener);
        nextCalendarTv.setOnClickListener(mClickListener);

        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String today = CDateUtil.getToday_yyyy_MM_dd();
        preCalendarTv.setText(today);
        nextCalendarTv.setText(today);

        mTimeClass = new ChartTimeUtil();
        mTemperChart = new TemperChartView(getContext(), view);

        TypeDataSet.Period periodType = mTimeClass.getPeriodType();
        mTimeClass.clearTime();         // 날자 초기화

        xFormatter = new AxisValueFormatter2(periodType);
        yFormatter = new AxisYValueFormatter(periodType);

        mTemperChart.setYValueFormat(yFormatter);
        mTemperChart.setXValueFormat(xFormatter);

        mSwipeListView = new TemperSwipeListView(view, TemperGraphFragment.this);
        chartRule.setText("일간 : 시간별 최종데이터");

        setNextButtonVisible();

        // 차트 전체 화면 처리
        mVisibleView4 = view.findViewById(R.id.visible_layout_4);
        mContentScrollView = view.findViewById(R.id.view_scrollview);
        mChartFrameLayout = view.findViewById(R.id.chart_frame_layout);
        mChartCloseBtn = view.findViewById(R.id.chart_close_btn);
        mChartZoomBtn = view.findViewById(R.id.landscape_btn);

        mChartCloseBtn.setOnClickListener(mClickListener);
        mChartZoomBtn.setOnClickListener(mClickListener);

        setVisibleOrientationLayout();

        //click 저장
        OnClickListener ClickListener = new OnClickListener(mClickListener, view, getContext());
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
            } else if (vId == R.id.graph_pre_textview) {
                showCalendar((TextView) v);
            } else if (vId == R.id.graph_next_textview) {
                showCalendar((TextView) v);
            } else if (vId == R.id.weight_modal_btn) {
                new showModifiDlg();
            } else if (vId == R.id.landscape_btn) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else if (vId == R.id.chart_close_btn) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }else if(vId == R.id.target_value_btn){
//                ((MotherHealthMainActivity)getContext()).actionBtnClick();
            }
            setNextButtonVisible();
        }
    };

    private void showCalendar(TextView tv) {
        GregorianCalendar calendar = new GregorianCalendar();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new CDatePicker(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (DateTimeCheck("D", year, monthOfYear, dayOfMonth)) {
                    cal_year = year;
                    cal_month = monthOfYear;
                    cal_day = dayOfMonth;
                    mDateTvSet(tv, year, monthOfYear, dayOfMonth);
                }
            }
        }, year, month, day, false).show();
    }

    private int cal_year;
    private int cal_month;
    private int cal_day;
    private int cal_hour;
    private int cal_min;
//    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            if (DateTimeCheck("D", year, monthOfYear, dayOfMonth)) {
//                cal_year = year;
//                cal_month = monthOfYear;
//                cal_day = dayOfMonth;
//                mDateTvSet(year, monthOfYear, dayOfMonth);
//            }
//        }
//    };

    private void mDateTvSet(TextView tv, int year, int monthOfYear, int dayOfMonth){
        String msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth);
        String tagMsg = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth);
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear + 1);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        tv.setText(CDateUtil.getFormatYYYYMMDD(tagMsg));
//        tv.setText(msg+" "+ CDateUtil.getDateToWeek(tagMsg)+"요일");
        tv.setTag(tagMsg);
    }

    private boolean DateTimeCheck(String type, int pram1, int pram2, int pram3){
        Calendar cal = Calendar.getInstance();

        if(type.equals("D")){
            cal.set(Calendar.YEAR, pram1);
            cal.set(Calendar.MONTH, pram2);
            cal.set(Calendar.DAY_OF_MONTH, pram3);
            cal.set(Calendar.HOUR_OF_DAY, cal_hour);
            cal.set(Calendar.MINUTE, cal_min);

            if(cal.getTimeInMillis() > System.currentTimeMillis()){
                CDialog.showDlg(getContext(), getString(R.string.message_nowtime_over), new CDialog.DismissListener() {
                    @Override
                    public void onDissmiss() {

                    }
                });
                return false;
            }else{
                return true;
            }
        }else{
            cal.set(Calendar.YEAR, cal_year);
            cal.set(Calendar.MONTH, cal_month);
            cal.set(Calendar.DAY_OF_MONTH, cal_day);
            cal.set(Calendar.HOUR_OF_DAY, pram1);
            cal.set(Calendar.MINUTE, pram2);

            if(cal.getTimeInMillis() > System.currentTimeMillis()){
                CDialog.showDlg(getContext(), getString(R.string.message_nowtime_over), new CDialog.DismissListener() {
                    @Override
                    public void onDissmiss() {

                    }
                });

                return false;
            }else{
                return true;
            }
        }
    }

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

            mTemperChart.setYValueFormat(yFormatter);
            mTemperChart.setXValueFormat(xFormatter);

            getData();   // 날자 세팅 후 조회
        }
    };

    /**
     * 날자 계산 후 조회
     */
    protected void getData() {
        long startTime = mTimeClass.getStartTime();
        long endTime = mTimeClass.getEndTime();

        mTemperChart.getBarChart().setDrawMarkers(false); // 데이터 변경 될때 마커뷰 사라지게 하기

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

            mXLabelTv.setText("(일)");
            int maxX = mTimeClass.getStartTimeCal().getActualMaximum(Calendar.DAY_OF_MONTH) + 1;
            xFormatter.setMonthMax(maxX);
            mTemperChart.setXvalMinMax(0, maxX, maxX);

            String startDay = CDateUtil.getFormattedString_yyyy(mTimeClass.getStartTime());
            String endDay = CDateUtil.getFormattedString_MM(mTimeClass.getStartTime());
            List<BarEntry> weightYVals = new ArrayList<>(); //weightDb.getResultMonth(startDay, endDay, true);
            setYMinMax(weightYVals, false);
            mTemperChart.setData(weightYVals, mTimeClass);
            List<BarEntry> fatYVals = new ArrayList<>();//  weightDb.getResultMonth(startDay, endDay, false);
            mTemperChart.animateY();
            setNextButtonVisible();
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

//        // y min값이 없는 경우
//        if (yMin == Float.MAX_VALUE && is40Data == false) {
//            yMin = StringUtil.getFloat(commonData.getMotherTemper()) - 3;
//        }
//        // y max값이 없는 경우
//        if (yMax == Float.MIN_VALUE && is40Data == false) {
//            yMax = yMin + 3;
//        }

        int yLabelCnt = (int) (yMax - yMin);
        mTemperChart.setYvalMinMax(yMin - 3, yMax + 3, yLabelCnt + 6); // 최소값 최대값이 없으면 임의로 넣어줌
//        Log.i(TAG, "yMin="+yMin+", yMax="+yMax+", yLabelCnt="+yLabelCnt);
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
        mSwipeListView.getHistoryData();    // 히스토리 Refresh
//        requestTemperData();
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
    private TextView curTemperTxt;
    private TextView commentTxt01;
    private TextView commentTxt02;
    private LinearLayout mom_set_lv;


    public void requestTemperData() {
        Tr_FeverList.RequestData requestData = new Tr_FeverList.RequestData();
        CommonData login = CommonData.getInstance(getContext());

        getData(getContext(), Tr_FeverList.class, requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_FeverList) {
                    Tr_FeverList data = (Tr_FeverList) obj;


                }
            }
        }, null);
    }

//    /**
//     * 목표체중등록
//     *
//     * @param wt
//     */
//    public void requestRevcGoalTemper(String wt) {
//        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//        try {
//            JSONObject object = new JSONObject();
//
//            object.put("api_code", "mvm_goalqy");
//            object.put("insures_code", "108");
//            object.put("mber_sn", CommonData.getInstance(getContext()).getMberSn());
//
//            object.put("goal_mvm_calory", "");
//            object.put("goal_mvm_stepcnt", "");
//            object.put("goal_bdwgh", wt);
//
//            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));
//            RequestApi.requestApi(getActivity(), NetworkConst.NET_MOTHER_REVC_GOAL, NetworkConst.getInstance().getDefDomain(), networkListener, params, new MakeProgress(getActivity()));
//
//            CommonData.getInstance(getContext()).setMotherGoalTemper(wt);
//        } catch (Exception e) {
//            Log.i(e.toString(), "dd");
//            e.printStackTrace();
//        }
//    }

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
                            requestTemperData();
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

    public void initWeighCurrData(Tr_FeverList resultData) {

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
//        mVisibleView1.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
//        mVisibleView3.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
//        mVisibleView4.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
//        mChartCloseBtn.setVisibility(isLandScape ? View.VISIBLE : View.GONE);
//        mChartZoomBtn.setVisibility(isLandScape ? View.GONE : View.VISIBLE);

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
////        if (resultCode == Activity.RESULT_OK && requestCode == TemperBigDataInputFragment.REQ_WEIGHT_PREDICT) {
////            if (BuildConfig.DEBUG) {
////                radioBtnYear.performClick();    // 차트 년간 버튼
////                btn_weight_graph.performClick();
////
////                String weight = data.getStringExtra(TemperBigDataInputFragment.INTENT_KEY_WEIGHT);
////                String week = data.getStringExtra(TemperBigDataInputFragment.INTENT_KEY_WEEK);
////                doTemperTotalGrp();
////                if (TextUtils.isEmpty(week) && TextUtils.isEmpty(week))
////                    return;
////                doTemperHopeGrp(weight, week);
////            }
////        }
//    }
}