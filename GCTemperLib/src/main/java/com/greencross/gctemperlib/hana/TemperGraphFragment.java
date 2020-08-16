package com.greencross.gctemperlib.hana;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.collection.FeverItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.AxisYValueFormatter;
import com.greencross.gctemperlib.greencare.component.CDatePicker;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_FeverList;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.DisplayUtil;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * 체온 그래프
 */

public class TemperGraphFragment extends BaseFragment {
    private final String TAG = TemperGraphFragment.class.getSimpleName();

    private int mRequestCode = 1111;
    private Boolean isGraphActive = false;

    public CommonData commonData = CommonData.getInstance(getContext());

    //    public ChartTimeUtil mTimeClass;
    private TextView mDateTv;
    private TextView mTemperDayTv;

//    protected TemperChartView mTemperChart;
//    private TemperSwipeListView mSwipeListView;

    protected LinearLayout layout_temper_graph;              // 그래프 레이아웃

    protected TextView mXLabelTv;

    private ImageButton imgPre_btn;
    private ImageButton imgNext_btn;

    private AxisYValueFormatter yFormatter;

    private View mVisibleView1;
    private View mVisibleView3;
    private ScrollView mContentScrollView;
    private LinearLayout mChartFrameLayout;
    private ImageView mChartCloseBtn, mChartZoomBtn;

    private TextView mTxtGraphStartDate;
    private TextView mTxtGraphEndDate;

    private LinearLayout mLogLayout;
    private CombinedChart mHistoryGraph;


    public static Fragment newInstance() {
        TemperGraphFragment fragment = new TemperGraphFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temper_graph_fragment, container, false);
        if (getActivity() instanceof DummyActivity) {
            ((DummyActivity) getActivity()).setTitle(getString(R.string.temper_graph));
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHistoryGraph = (CombinedChart) view.findViewById(R.id.history_graph);

        mDateTv = (TextView) view.findViewById(R.id.period_date_textview);
        mTemperDayTv = (TextView) view.findViewById(R.id.textView18);
        layout_temper_graph = (LinearLayout) view.findViewById(R.id.layout_temper_graph);

//        mXLabelTv = view.findViewById(R.id.weight_chart_x_label_tv);

        imgPre_btn = (ImageButton) view.findViewById(R.id.pre_btn);
        imgNext_btn = (ImageButton) view.findViewById(R.id.next_btn);

        view.findViewById(R.id.pre_btn).setOnClickListener(mClickListener);
        view.findViewById(R.id.next_btn).setOnClickListener(mClickListener);

        mTxtGraphStartDate = view.findViewById(R.id.graph_pre_textview);
        mTxtGraphEndDate = view.findViewById(R.id.graph_next_textview);
        mTxtGraphStartDate.setOnClickListener(mClickListener);
        mTxtGraphEndDate.setOnClickListener(mClickListener);

        mLogLayout = (LinearLayout) view.findViewById(R.id.temper_log_layout);

        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String today = CDateUtil.getToday_temper_graph();
        mTxtGraphStartDate.setText(today);
        mTxtGraphEndDate.setText(today);

//        mTimeClass = new ChartTimeUtil();
//        mTemperChart = new TemperChartView(getContext(), view);
//
//        TypeDataSet.Period periodType = mTimeClass.getPeriodType();
//        mTimeClass.clearTime();         // 날자 초기화

//        xFormatter = new AxisValueFormatter2(periodType);
        yFormatter = new AxisYValueFormatter();

//        mTemperChart.setYValueFormat(yFormatter);
////        mTemperChart.setXValueFormat(xFormatter);
//        mTemperChart.getXAxis().setTextSize(DisplayUtil.getPxToDp(getContext(), 6));
//        mSwipeListView = new TemperSwipeListView(view, TemperGraphFragment.this);


//        setNextButtonVisible();
        // 차트 전체 화면 처리
        mContentScrollView = view.findViewById(R.id.view_scrollview);
        mChartFrameLayout = view.findViewById(R.id.chart_frame_layout);
        mChartCloseBtn = view.findViewById(R.id.chart_close_btn);
        mChartZoomBtn = view.findViewById(R.id.landscape_btn);

        mChartCloseBtn.setOnClickListener(mClickListener);
        mChartZoomBtn.setOnClickListener(mClickListener);

        // Y라벨 값 설정
        int yLabelCnt = (int) (43 - 31);
//        mTemperChart.setYvalMinMax(31, 43, yLabelCnt); // 최소값 최대값이 없으면 임의로 넣어줌
        setVisibleOrientationLayout();
        //click 저장
        OnClickListener ClickListener = new OnClickListener(mClickListener, view, getContext());

        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE_DOT);
        mCalendar = new GregorianCalendar();
        mStartDate = new Date();
        mEndDate = new Date();
        mStrStartDate = format.format(mStartDate);
        mStrEndDate = format.format(mEndDate);
        mTxtGraphStartDate.setText(mStrStartDate);
        mTxtGraphEndDate.setText(mStrEndDate);
//        initChart();
        initGraph();
    }

//    /**
//     * 차트 초기 로딩 화면 없애기
//     */
//    private void initChart() {
//        mTemperChart.setXvalMinMax(0, 0, 10);
//        List<BarEntry> temperYVals = new ArrayList<>();
//        temperYVals.add(new BarEntry(0, 0));
//
//        TemperChartFormatter formatter = new TemperChartFormatter(mXlabels);
//        mTemperChart.setXValueFormat(formatter);
//        mTemperChart.setData(temperYVals, mTimeClass);
//        mTemperChart.invalidate();
//    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();

//            if (vId == R.id.pre_btn) {
////                mTimeClass.calTime(-1);
//                getData();
//            } else if (vId == R.id.next_btn) {
//                // 초기값 일때 다음날 데이터는 없으므로 리턴
////                if (mTimeClass.getCalTime() == 0)
////                    return;
////                mTimeClass.calTime(1);
//                
//                
//                getData();
//            } else 
            if (vId == R.id.graph_pre_textview) {
                showCalendar((TextView) v);
            } else if (vId == R.id.graph_next_textview) {
                showCalendar((TextView) v);
//            } else if (vId == R.id.weight_modal_btn) {
//                new showModifiDlg();
            } else if (vId == R.id.landscape_btn) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else if (vId == R.id.chart_close_btn) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
//            setNextButtonVisible();
        }
    };


    private int cal_year;
    private int cal_month;
    private int cal_day;
    private int cal_hour;
    private int cal_min;

    private void showCalendar(TextView tv) {
        GregorianCalendar calendar = new GregorianCalendar();
        String date = tv.getText().toString();
        date = StringUtil.getIntString(date);
        int year = StringUtil.getIntger(date.substring(0, 4));   //calendar.get(Calendar.YEAR);
        int month = StringUtil.getIntger(date.substring(4, 6)) - 1;  // calendar.get(Calendar.MONTH)-1;
        int day = StringUtil.getIntger(date.substring(6, 8));
        calendar.get(Calendar.DAY_OF_MONTH);

        new CDatePicker(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (DateTimeCheck("D", year, monthOfYear, dayOfMonth)) {
                    cal_year = year;
                    cal_month = monthOfYear;
                    cal_day = dayOfMonth;
                    mDateTvSet(tv, year, monthOfYear, dayOfMonth);

                    getData();
                }
            }
        }, year, month, day, false).show();
    }


    public void initGraph() {
//        mHistoryGraph.setDescription("");
        mHistoryGraph.setDescription(null);
        mHistoryGraph.setBackgroundColor(Color.TRANSPARENT);
        mHistoryGraph.setDrawGridBackground(false);
        mHistoryGraph.setDrawBarShadow(false);

        /*// draw bars behind lines
        mHistoryGraph.setDrawOrder(new CombinedChart.DrawOrder[] {
                CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });*/

        // enable touch gestures
        mHistoryGraph.setTouchEnabled(true);
        // enable scaling and dragging
        mHistoryGraph.setScaleEnabled(true);
        mHistoryGraph.setDragEnabled(true);
        mHistoryGraph.setScaleYEnabled(false);

        mHistoryGraph.setDragDecelerationFrictionCoef(0.2f);

        // enable scaling and dragging
        mHistoryGraph.setHighlightPerDragEnabled(false);

        mHistoryGraph.setPinchZoom(false);

        Legend l = mHistoryGraph.getLegend();
        l.setTextColor(Color.GRAY);
        l.setForm(Legend.LegendForm.CIRCLE);
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);  // XXX

        YAxis rightAxis = mHistoryGraph.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = mHistoryGraph.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.GRAY);
        leftAxis.setAxisLineColor(Color.GRAY);
        leftAxis.setTextColor(Color.GRAY);
//        leftAxis.setAxisMaximum(42f);
//        leftAxis.setAxisMinimum(34f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaxValue(42);
        leftAxis.setAxisMinValue(34);
        leftAxis.setDrawZeroLine(false);

        XAxis xAxis = mHistoryGraph.getXAxis();
        xAxis.setAxisLineColor(Color.GRAY);
        xAxis.setGridColor(Color.GRAY);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    private String[] arrXDate;
    private int cntDay = 0;
    private Date mStartDate;
    private Date mEndDate;
    private String mStrStartDate;
    private String mStrEndDate;
    private String[] arrDateSet;
    private GregorianCalendar mCalendar;
    private SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME_S);
    private List<FeverItem> mArrFeverList = new ArrayList<>();

//    private List<AllDataItem> mAllDataItems = new ArrayList<>();

    public void updateGraph() {
        setDate();
//        CombinedData data = new CombinedData(arrXDate);
        CombinedData data = new CombinedData();
        data.setData(setScatterData());

        mHistoryGraph.setData(data);
        mHistoryGraph.invalidate();
    }

    public void setDate() {
        cntDay = Util.sumDayCount(mStartDate, mEndDate);

        if (mStrStartDate.equals(mStrEndDate)) {
            arrXDate = new String[1440];
            for (int i = 0; i < arrXDate.length; i++) {
                int h = i / 60;
                int m = i % 60;
                arrXDate[i] = String.format("%02d:%02d", h, m);
            }
        } else {
            arrXDate = new String[cntDay * 24];
            arrDateSet = new String[cntDay];

            SimpleDateFormat dateFormat = new SimpleDateFormat(CommonData.PATTERN_DATE_2);

            mCalendar.setTime(mStartDate);
            arrDateSet[0] = dateFormat.format(mCalendar.getTime());
            for (int i = 1; i < cntDay; i++) {
                mCalendar.add(Calendar.DATE, 1);
                arrDateSet[i] = dateFormat.format(mCalendar.getTime());
            }

            for (int i = 0; i < arrXDate.length; i++) {
                int day = i / 24;
                int hour = i % 24;
                arrXDate[i] = arrDateSet[day] + "\n" + hour + getString(R.string.hour_2);
            }
        }
    }


    private ScatterData setScatterData() {
        ScatterData d = new ScatterData();

        ArrayList<Entry> entries = new ArrayList<Entry>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(CommonData.PATTERN_DATE_2);

        for (int i = 0; i < mArrFeverList.size(); i++) {
            try {
                Date inputDe = format.parse(mArrFeverList.get(i).getmInputDe());
                int Y_Temper = Integer.parseInt(mArrFeverList.get(i).getmInputFever());
                float X_Time = (format.parse(mArrFeverList.get(i).getmInputDe()).getHours() * 60) + inputDe.getMinutes();

                Log.i(TAG, "ScatterData["+i+"]("+(mStrStartDate.equals(mStrEndDate))+")="+mStrStartDate+", "+mStrEndDate +", xTemper="+Y_Temper+", yTime="+X_Time);
                if (mStrStartDate.equals(mStrEndDate)) {
                    entries.add(new Entry(X_Time, Y_Temper));
                } else {
                    for (int x = 0; x < arrDateSet.length; x++) {
                        if (arrDateSet[x].equals(dateFormat.format(inputDe)))
                            entries.add(new Entry(((x * 24) + inputDe.getHours()), Y_Temper));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ScatterDataSet set = new ScatterDataSet(entries, null);
        set.setColor(getResources().getColor(R.color.h_orange));
        set.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set.setScatterShapeSize(20f);
        set.setLabel(null);
//        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//
        d.addDataSet(set);

        return d;
    }


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

    private void mDateTvSet(TextView tv, int year, int monthOfYear, int dayOfMonth) {
        String msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth);
        String tagMsg = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth);
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear + 1);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        tv.setText(CDateUtil.getFormatYYYYMMDD(tagMsg));
        tv.setTag(cal.getTimeInMillis());
    }

    private boolean DateTimeCheck(String type, int pram1, int pram2, int pram3) {
        Calendar cal = Calendar.getInstance();

        if (type.equals("D")) {
            cal.set(Calendar.YEAR, pram1);
            cal.set(Calendar.MONTH, pram2);
            cal.set(Calendar.DAY_OF_MONTH, pram3);
            cal.set(Calendar.HOUR_OF_DAY, cal_hour);
            cal.set(Calendar.MINUTE, cal_min);

            if (cal.getTimeInMillis() > System.currentTimeMillis()) {
                CDialog.showDlg(getContext(), getString(R.string.message_nowtime_over), new CDialog.DismissListener() {
                    @Override
                    public void onDissmiss() {

                    }
                });
                return false;
            } else {
                return true;
            }
        } else {
            cal.set(Calendar.YEAR, cal_year);
            cal.set(Calendar.MONTH, cal_month);
            cal.set(Calendar.DAY_OF_MONTH, cal_day);
            cal.set(Calendar.HOUR_OF_DAY, pram1);
            cal.set(Calendar.MINUTE, pram2);

            if (cal.getTimeInMillis() > System.currentTimeMillis()) {
                CDialog.showDlg(getContext(), getString(R.string.message_nowtime_over), new CDialog.DismissListener() {
                    @Override
                    public void onDissmiss() {

                    }
                });

                return false;
            } else {
                return true;
            }
        }
    }

//    private void setNextButtonVisible() {
//        // 초기값 일때 다음날 데이터는 없으므로 리턴
//        if (mTimeClass.getCalTime() == 0) {
//            imgNext_btn.setVisibility(View.INVISIBLE);
//        } else {
//            imgNext_btn.setVisibility(View.VISIBLE);
//        }
//
//        //임신여부
//        CommonData common = CommonData.getInstance(getContext());
//        String materPregency = common.getbirth_chl_yn(); //임신 중 N, 출산 후 Y
//    }

    /**
     * 날자 계산 후 조회
     */
    protected void getData() {
//        long startTime = mTimeClass.getStartTime();
//        long endTime = mTimeClass.getEndTime();
        long startTime = getTimeInMillis(mTxtGraphStartDate.getText().toString());
        long endTime = getTimeInMillis(mTxtGraphEndDate.getText().toString());

//        mTemperChart.getBarChart().setDrawMarkers(false); // 데이터 변경 될때 마커뷰 사라지게 하기

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");

        String startDate = sdf.format(startTime);
        String endDate = sdf.format(endTime);

//        if (mTimeClass.getPeriodType() == TypeDataSet.Period.PERIOD_DAY) {
//            mDateTv.setText(startDate);
//        } else if (mTimeClass.getPeriodType() == TypeDataSet.Period.PERIOD_YEAR) {
//            mDateTv.setText(yearSdf.format(startTime));
//        } else {
//            mDateTv.setText(startDate + " ~ " + endDate);
//        }

        Tr_FeverList.RequestData requestData = new Tr_FeverList.RequestData();
        requestData.startdate = startDate;
        requestData.enddate = endDate;

        getData(Tr_FeverList.class, requestData, (isSuccess, message, data) -> {
            if (data instanceof Tr_FeverList) {
                Tr_FeverList recv = (Tr_FeverList) data;
                if (recv.isSuccess(recv.resultcode)) {
                    mArrFeverList.clear();
                    for (int i = 0; i < recv.datas.size(); i++) {
                        Tr_FeverList.Data feverData = (Tr_FeverList.Data) recv.datas.get(i);
                        FeverItem item = new FeverItem();
                        item.setmFeverSn(feverData.idx);
                        item.setmInputDe(feverData.input_de);
                        item.setmInputFever(feverData.input_fever);

                        mArrFeverList.add(item);
                    }

                    updateGraph();
                    Collections.sort(recv.datas, new TemperCompare());  // 날자역순으로 정렬(최종날자가
                    makeLogItem(recv.datas);


//                    int maxX = makeLogItem(recv.datas);     //mTimeClass.getStartTimeCal().getActualMaximum(Calendar.DAY_OF_MONTH) + 1;
//                    mTemperChart.setXvalMinMax(0, recv.datas.size() + 1, recv.datas.size() + 3);
//                    List<BarEntry> temperYVals = new ArrayList<>();
//                    for (Tr_FeverList.Data temperData : recv.datas) {
//                        float idx = StringUtil.getFloatVal(temperData.idx);
//                        float input_fever = StringUtil.getFloatVal(temperData.input_fever);
//                        temperYVals.add(new BarEntry(temperData.chartXPositon, input_fever));
//                    }
//
//                    TemperChartFormatter formatter = new TemperChartFormatter(mXlabels);
//                    mTemperChart.setXValueFormat(formatter);
//                    mTemperChart.setData(temperYVals, mTimeClass);
//
//
//                    mTemperChart.invalidate();
                } else {
                    CDialog.showDlg(getContext(), "알림", "데이터 수신 실패");
                }
            } else {
                CDialog.showDlg(getContext(), "알림", "데이터 수신 실패");
            }
        });
    }

    /**
     * 하단 로그 화면 만들기
     *
     * @param datas
     */
    private List<String> mXlabels = new ArrayList<>();

    private int makeLogItem(List<Tr_FeverList.Data> datas) {
        mLogLayout.removeAllViews();
        int xCount = 0;
        String beforeInputDe = "";
        View beforeBottomLine = null;
        for (Tr_FeverList.Data temperData : datas) {
            if (TextUtils.isEmpty(temperData.input_de) == false) {
                String yyyMMdd = temperData.input_de.substring(0, 10);
                boolean isAddDateLayout = yyyMMdd.equals(beforeInputDe) == false;
                if (isAddDateLayout) {
                    xCount++;
                    beforeInputDe = yyyMMdd;
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.temper_log_frame_layout, null);
                    ((TextView) view.findViewById(R.id.temper_item_date_textview)).setText(yyyMMdd);
                    if (beforeBottomLine != null)
                        beforeBottomLine.setVisibility(View.GONE);
                    mLogLayout.addView(view);
                    mXlabels.add(temperData.input_de);
                }
                temperData.chartXPositon = xCount;
                View view = LayoutInflater.from(getContext()).inflate(R.layout.temper_log_item_layout, null);
                ImageButton ib = view.findViewById(R.id.temper_item_iv);
                boolean isWeareable = "1".equals(temperData.is_wearable);
                TextView itemTextView1 = view.findViewById(R.id.temper_item_text1);
                if (isWeareable) {
                    ib.setImageResource(R.drawable.hn_temper_log_icon2);
                    itemTextView1.setText("전용체온계");
                    itemTextView1.setTextColor(Color.parseColor("#6972d1"));
                }
                ((TextView) view.findViewById(R.id.temper_item_text2)).setText(temperData.input_de.substring(11, 16));
                ((TextView) view.findViewById(R.id.temper_item_text3)).setText(temperData.input_fever + " ℃");
                beforeBottomLine = view.findViewById(R.id.temper_item_bottom_line);
                mLogLayout.addView(view);
            }
        }

        return xCount;
    }

    class TemperCompare implements Comparator<Tr_FeverList.Data> {
        @Override
        public int compare(Tr_FeverList.Data data, Tr_FeverList.Data t1) {
            return t1.input_de.compareTo(data.input_de);
        }
    }

    private long getTimeInMillis(String text) {
        text = StringUtil.getIntString(text);
        int year = StringUtil.getIntger(text.substring(0, 4));
        int month = StringUtil.getIntger(text.substring(4, 6));
        int day = StringUtil.getIntger(text.substring(6, 8));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return cal.getTimeInMillis();
    }


//    /**
//     * y라벨 구하기
//     *
//     * @param temperYVals
//     */
//    private void setYMinMax(List<BarEntry> temperYVals) {
//        float yMin = Float.MAX_VALUE;
//        float yMax = Float.MIN_VALUE;
//        Log.i(TAG, "#######yLabelCnt##############");
//        for (BarEntry entry : temperYVals) {
//            float y = entry.getY();
//            if (y != 0 && y < yMin) {
//                yMin = y;
//            }
//
//            if (y != 0 && y > yMax) {
//                yMax = y;
//            }
//        }
//
//        int yLabelCnt = (int) (yMax - yMin);
//        mTemperChart.setYvalMinMax(yMin - 3, yMax + 3, yLabelCnt + 6); // 최소값 최대값이 없으면 임의로 넣어줌
////        Log.i(TAG, "yMin="+yMin+", yMax="+yMax+", yLabelCnt="+yLabelCnt);
//    }


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
//        mSwipeListView.getHistoryData();    // 히스토리 Refresh
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


    // 최고 32에서 13 빼고 시작한다
    private float totalSpan = 18; //(32 - 14)
    private float span01 = 4.5f;
    private float span02 = 4.5f;
    private float span03 = 2;
    private float span04 = 5;
    private float span05;


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

//    @Deprecated
//    public class QeuryVerifyDataTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgress();
//        }
//
//        protected Void doInBackground(Void... params) {
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            hideProgress();
//
//            Logger.i(TAG, "getContext is " + getContext());
//
//            if (getContext() == null) {
//                Logger.e(TAG, "getContext is Null");
//                return;
//            }
//
//            mXLabelTv.setText("(일)");
//            int maxX = mTimeClass.getStartTimeCal().getActualMaximum(Calendar.DAY_OF_MONTH) + 1;
//            mTemperChart.setXvalMinMax(0, maxX, maxX);
//
//            String startDay = CDateUtil.getFormattedString_yyyy(mTimeClass.getStartTime());
//            String endDay = CDateUtil.getFormattedString_MM(mTimeClass.getStartTime());
//            List<BarEntry> temperYVals = new ArrayList<>(); //weightDb.getResultMonth(startDay, endDay, true);
//            setYMinMax(temperYVals);
//            mTemperChart.setData(temperYVals, mTimeClass);
//            List<BarEntry> fatYVals = new ArrayList<>();//  weightDb.getResultMonth(startDay, endDay, false);
//            mTemperChart.animateY();
//            setNextButtonVisible();
//        }
//    }

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