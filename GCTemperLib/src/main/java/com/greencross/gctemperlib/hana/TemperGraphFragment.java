package com.greencross.gctemperlib.hana;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.FeverItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.hana.component.CDatePicker;
import com.greencross.gctemperlib.hana.component.CDialog;
import com.greencross.gctemperlib.hana.util.CDateUtil;
import com.greencross.gctemperlib.hana.util.StringUtil;
import com.greencross.gctemperlib.hana.chart.CustomCombinedChart;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_FeverList;
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

public class TemperGraphFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTxtGraphStartDate, mTxtGraphEndDate;

    private CustomCombinedChart mHistoryGraph;
    private LinearLayout mLogLayout;
    private String[] mArrXDate;
    private String[] mArrDateSet;

    private GregorianCalendar mCalendar;
    private ArrayList<FeverItem> mArrFeverList = new ArrayList<FeverItem>();

    private String mStrStartDate;
    private String mStrEndDate;
    private Date mStartDate;
    private Date mEndDate;
    private int cntDay = 0;

    private SimpleDateFormat mFormat;

    private boolean[] bSelFilter;
    private boolean[] bTumpSelFilter;

    private boolean mNoShowHelp = false;

    private String[] arrFilter;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temper_graph_frgment, container, false);
        if (getActivity() instanceof DummyActivity) {
            getActivity().setTitle(getString(R.string.temper_graph));
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtGraphStartDate = (TextView) view.findViewById(R.id.txt_graph_start_date);
        mTxtGraphEndDate = (TextView) view.findViewById(R.id.txt_graph_end_date);
        mLogLayout = (LinearLayout) view.findViewById(R.id.temper_log_layout);

        mHistoryGraph = (CustomCombinedChart) view.findViewById(R.id.history_graph);
        mHistoryGraph.getLegend().setEnabled(false);

        mTxtGraphStartDate.setOnClickListener(this);
        mTxtGraphEndDate.setOnClickListener(this);

        initView();
        initGraph();
        setDate();
        updateGraph();
        getData();
    }

    public void initView() {
        arrFilter = getResources().getStringArray(R.array.filter_list);
        bSelFilter = new boolean[arrFilter.length];

        mFormat = new SimpleDateFormat(CommonData.PATTERN_DATETIME_S);

        // 나갔다 들어왔을때, 처음 들어왔을때 체크 박스 초기화
        for (int i = 0; i < bSelFilter.length; i++) {
            bSelFilter[i] = true;
        }

        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE_DOT);
        mCalendar = new GregorianCalendar();
        mStartDate = new Date();
        mEndDate = new Date();
        mStrStartDate = format.format(mStartDate);
        mStrEndDate = format.format(mEndDate);
        mTxtGraphStartDate.setText(mStrStartDate);
        mTxtGraphEndDate.setText(mStrEndDate);
//        setDate();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txt_graph_start_date) {
            try {
                if (mStartDate == null) {
                    mStartDate = new Date();
                }
                mCalendar.setTime(mStartDate);
                int year = mCalendar.get(Calendar.YEAR);
                int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                GregorianCalendar calendar = new GregorianCalendar();
                calendar.get(Calendar.DAY_OF_MONTH);
                new CDatePicker(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);

                        Date startDate = mCalendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE_DOT);
                        String startDateStr = format.format(startDate);
                        String endDateStr = format.format(mEndDate);
                        if (startDateStr.compareTo(endDateStr) > 0) {  // 오늘 지남
                            Toast.makeText(getContext(), getString(R.string.over_date), Toast.LENGTH_LONG).show();
                            return;
                        }

                        mStartDate = startDate;
                        mStrStartDate = startDateStr;
                        mTxtGraphStartDate.setText(mStrStartDate);

                        getData();
                    }
                }, year, month, day, false).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.txt_graph_end_date) {
            try {
                if (mEndDate == null) {
                    mEndDate = new Date();
                }
                mCalendar.setTime(mEndDate);
                int year = mCalendar.get(Calendar.YEAR);
                int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                GregorianCalendar calendar = new GregorianCalendar();
                calendar.get(Calendar.DAY_OF_MONTH);
                new CDatePicker(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mCalendar.set(year, monthOfYear, dayOfMonth);

                        Date endDate = mCalendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE_DOT);
                        String startDateStr = format.format(mStartDate);
                        String endDateStr = format.format(endDate);
                        if (startDateStr.compareTo(endDateStr) > 0) {  // 오늘 지남
                            Toast.makeText(getContext(), getString(R.string.over_date2), Toast.LENGTH_LONG).show();
                            return;
                        }

                        mEndDate = endDate;
                        mStrEndDate = endDateStr;
                        mTxtGraphEndDate.setText(mStrEndDate);

                        getData();
                    }
                }, year, month, day, false).show();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    /**
     * 날자 계산 후 조회
     */
    protected void getData() {
        long startTime = getTimeInMillis(mTxtGraphStartDate.getText().toString());
        long endTime = getTimeInMillis(mTxtGraphEndDate.getText().toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");

        String startDate = sdf.format(startTime);
        String endDate = sdf.format(endTime);

        Tr_FeverList.RequestData requestData = new Tr_FeverList.RequestData();
        requestData.startdate = startDate;
        requestData.enddate = endDate;

        getData(Tr_FeverList.class, requestData, (isSuccess, message, data) -> {
            mArrFeverList.clear();

            if (data instanceof Tr_FeverList) {
                Tr_FeverList recv = (Tr_FeverList) data;
                if (recv.isSuccess(recv.resultcode)) {
                    Collections.sort(recv.datas, new TemperCompare());  // 날자역순으로 정렬(최종날자가

                    for (int i = 0; i < recv.datas.size(); i++) {
                        Tr_FeverList.Data feverData = (Tr_FeverList.Data) recv.datas.get(i);
                        FeverItem item = new FeverItem();
//                        item.setmFeverSn(feverData.idx);
                        item.setmInputDe(feverData.input_de);
                        item.setmInputFever(feverData.input_fever);

                        mArrFeverList.add(item);
                    }

                    makeLogItem(recv.datas);
                    updateGraph();
                } else {
                    CDialog.showDlg(getContext(), "알림", "데이터 수신 실패");
                }
            } else {
                CDialog.showDlg(getContext(), "알림", "데이터 수신 실패");
            }
        });
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
                    ((TextView) view.findViewById(R.id.temper_item_date_textview)).setText(CDateUtil.getFormat_yyyy_MM_dd_comma(yyyMMdd,"."));
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


    public void setDate() {
        cntDay = Util.sumDayCount(mStartDate, mEndDate);

//        if (mStrStartDate.equals(mStrEndDate)) {
//            mArrXDate = new String[1440];
//            for (int i = 0; i < mArrXDate.length; i++) {
//                int h = i / 60;
//                int m = i % 60;
//                mArrXDate[i] = String.format("%02d:%02d", h, m);
//            }
//        } else {
            mArrXDate = new String[cntDay * 24];
            mArrDateSet = new String[cntDay];

            SimpleDateFormat dateFormat = new SimpleDateFormat(CommonData.PATTERN_DATE_3);

            mCalendar.setTime(mStartDate);
            mArrDateSet[0] = dateFormat.format(mCalendar.getTime());
            for (int i = 1; i < cntDay; i++) {
                mCalendar.add(Calendar.DATE, 1);
                mArrDateSet[i] = dateFormat.format(mCalendar.getTime());
            }

            for (int i = 0; i < mArrXDate.length; i++) {
                int day = i / 24;
                int hour = i % 24;
                String amPm = CDateUtil.getAmPmString(hour);
                if (hour >= 13) {
                    hour -= 12;
                }
                mArrXDate[i] = mArrDateSet[day] + "\n"+amPm +" " + hour + "시";
            }
//        }
    }

    public void initGraph() {
        mHistoryGraph.setDescription("");
        mHistoryGraph.setBackgroundColor(Color.TRANSPARENT);
        mHistoryGraph.setDrawGridBackground(false);
        mHistoryGraph.setDrawBarShadow(false);


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
        mHistoryGraph.setExtraBottomOffset(10f);

        Legend l = mHistoryGraph.getLegend();
        l.setTextColor(Color.GRAY);
        l.setForm(Legend.LegendForm.CIRCLE);
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);  // XXX

        YAxis rightAxis = mHistoryGraph.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = mHistoryGraph.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#adadad"));
        leftAxis.setAxisLineColor(Color.parseColor("#808080"));
        leftAxis.setTextColor(Color.parseColor("#dddddd"));
        leftAxis.setTypeface(ResourcesCompat.getFont(getContext(), R.font.hanam));
        leftAxis.setAxisMaxValue(42.5f);
        leftAxis.setAxisMinValue(31.5f); // this replaces setStartAtZero(true)
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true);
        XAxis xAxis = mHistoryGraph.getXAxis();
        xAxis.setAxisLineColor(Color.parseColor("#808080"));
        xAxis.setGridColor(Color.parseColor("#adadad"));
        xAxis.setTextColor(Color.parseColor("#6972d1"));
        xAxis.setTypeface(ResourcesCompat.getFont(getContext(), R.font.hanal));
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public void updateGraph() {
        setDate();
        CombinedData data = new CombinedData(mArrXDate);
//        CombinedData data = new CombinedData();
        data.setData(parseScatterData());

        mHistoryGraph.setData(data);
        mHistoryGraph.invalidate();
    }

    /**
     * 차트 데이터 배열 만들기
     * @return
     */
    private ScatterData parseScatterData() {
        ScatterData data = new ScatterData();
        List<Entry> entries = new ArrayList<Entry>();

        SimpleDateFormat dateFormat = new SimpleDateFormat(CommonData.PATTERN_DATE_3);
        for (int i = 0; i < mArrFeverList.size(); i++) {
            try {
                if (TextUtils.isEmpty(mArrFeverList.get(i).getmInputFever()) == false) {
                    float fever = Float.parseFloat(mArrFeverList.get(i).getmInputFever());
                    Date date = mFormat.parse(mArrFeverList.get(i).getmInputDe());
                    Calendar cal = GregorianCalendar.getInstance();
                    cal.setTime(date);
//                    if (mStrStartDate.equals(mStrEndDate)) {
//                        entries.add(new Entry(fever, (cal.get(Calendar.HOUR_OF_DAY) * 60) + cal.get(Calendar.MINUTE)));
//                    } else {
                        for (int x = 0; x < mArrDateSet.length; x++) {
                            if (mArrDateSet[x].equals(dateFormat.format(date))) {
                                entries.add(new Entry(fever, ((x * 24) + cal.get(Calendar.HOUR_OF_DAY))));
                            }
                        }
//                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "parseScatterData="+entries.size());
        ScatterDataSet set = new ScatterDataSet(entries, null);

        set.setColor(Color.parseColor("#6972d1"));
        set.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set.setScatterShapeSize(7f);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        data.addDataSet(set);

        return data;
    }
}
