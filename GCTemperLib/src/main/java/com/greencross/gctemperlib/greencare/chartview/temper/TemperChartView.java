
package com.greencross.gctemperlib.greencare.chartview.temper;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greencross.gctemperlib.greencare.chartview.valueFormat.BarDataFormatter;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.XYMarkerView;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.charts.TemperChart;
import com.greencross.gctemperlib.greencare.charting.components.Legend;
import com.greencross.gctemperlib.greencare.charting.components.XAxis;
import com.greencross.gctemperlib.greencare.charting.components.YAxis;
import com.greencross.gctemperlib.greencare.charting.data.BarData;
import com.greencross.gctemperlib.greencare.charting.data.BarDataSet;
import com.greencross.gctemperlib.greencare.charting.data.BarEntry;
import com.greencross.gctemperlib.greencare.charting.formatter.IAxisValueFormatter;
import com.greencross.gctemperlib.greencare.charting.interfaces.datasets.IBarDataSet;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.AxisValueFormatter;
import com.greencross.gctemperlib.greencare.util.ChartTimeUtil;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.greencare.charting.components.YAxis.AxisDependency;

import java.util.ArrayList;
import java.util.List;


public class TemperChartView {

    protected TemperChart mChart;
    private Context mContext;

//    protected Typeface mTfRegular;
//    protected Typeface mTfLight;
    public TemperChartView(Context context, View v) {
        mContext = context;

        mChart = (TemperChart) v.findViewById(R.id.chart1);
        mChart.setTouchEnabled(true);       // 클릭시 값 표시 해주려면 true

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);
        mChart.getDefaultValueFormatter();

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setScaleEnabled(false);

        mChart.setDrawGridBackground(false);

        AxisValueFormatter xAxisFormatter = new AxisValueFormatter(TypeDataSet.Period.PERIOD_DAY);
        xAxisFormatter.setUnitStr("Kg");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(15);
        xAxis.setValueFormatter(xAxisFormatter);

//        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        // 그래프 왼쪽 수치 몸무게 값 범위 지정
        leftAxis.setAxisMinimum(40);
//        leftAxis.setAxisMaximum(65);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        // 하단 설명 문구 (bottom label)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        // 차트 클릭시 나오는 마커
        XYMarkerView mv = new XYMarkerView(mContext, xAxisFormatter);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        mv.setEnabled(true);

        mChart.setExtraTopOffset(30);   // 차트 상단 여백
//        mChart.setDrawValueAboveBar(false); // 그래프 상단 값 표시

        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChart.setDrawMarkers(true);  // 마커 표시 활성화 시키기
            }
        });
    }

    public void setXValueFormat(IAxisValueFormatter f) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(f);
        xAxis.setTextColor(Color.parseColor("#6972d1"));
    }

    public void setYValueFormat(IAxisValueFormatter f) {
        YAxis YAxis = mChart.getAxis(AxisDependency.LEFT);
        YAxis.setValueFormatter(f);
        YAxis.setTextColor(Color.parseColor("#dddddd"));
    }

    public TemperChart getBarChart() {
        return mChart;
    }

    public XAxis getXAxis() {
        return mChart.getXAxis();
    }

    public YAxis getYAxisLeft() {
        return mChart.getAxisLeft();
    }

    /**
     * X축 최대 값을 설정 한다
     * @param max
     */
    public void setXvalMinMax(float min, float max, int labelCnt) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMinimum(min);
        xAxis.setAxisMaximum(max);
        xAxis.setLabelCount(labelCnt);
    }

    public void setYvalMinMax(float min, float max, int labelCnt) {
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMinimum(min);
        yAxis.setAxisMaximum(max);
        yAxis.setLabelCount(labelCnt);
    }

    public void invalidate() {
        if (mChart != null)
            mChart.invalidate();
    }

    public void animateXY() {
        mChart.animateXY(500, 500);
    }
    public void animateY() {
        mChart.animateY(500);
    }


    public void setData(List<BarEntry> yVals1, ChartTimeUtil timeClass) {
        mChart.setTimeClass(timeClass);
        BarDataSet set1;
        if (yVals1.size() == 0) {
            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setAxisMinimum(32);
            leftAxis.setAxisMaximum(42);
        }


        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);

            // 목표체중 라인 값 설정해 주기
//            CommonData login = CommonData.getInstance();
//            if (login != null) {
//                float goalWeight = StringUtil.getFloatVal(login.getMotherGoalWeight());
//                float maxYVal = mChart.getAxisLeft().mAxisMaximum;
//
//                YAxis leftAxis = mChart.getAxisLeft();
//                if (maxYVal < goalWeight) {
//                    leftAxis.setAxisMaximum(goalWeight + 10);
//                } else {
//                    leftAxis.setAxisMaximum(maxYVal + 10);
//                }
//            }
            CommonData commonData = CommonData.getInstance(mContext);
            float befKg = StringUtil.getFloat(commonData.getBefKg());
            YAxis leftAxis = mChart.getAxisLeft();
//            leftAxis.setAxisMinimum(set1.getYMin() == 0 ? 40 : set1.getYMin());
//            leftAxis.setAxisMaximum(set1.getYMax() > befKg ? set1.getYMax()+10 : befKg + 20 );

//            Log.i(getClass().getSimpleName(), "set1.getYMin()="+set1.getYMin()+", set1.getYMax()="+set1.getYMax());

            mChart.getData().notifyDataChanged();

            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "bottom label");

            set1.setDrawIcons(false);

            // 그래프 색상 설정
            set1.setColor(ContextCompat.getColor(mContext, R.color.color_E1147F));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            // 그래프 두께 및 상단 값 세팅
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.BLACK);
//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            data.setValueFormatter(new BarDataFormatter());

            // Y축 라벨 세팅
//            float yMin = data.getYMin() == 0 ? 40 : data.getYMin();
//            setYvalMinMax(yMin, data.getYMax(), (int) (data.getYMax() - data.getYMin() / 2));

            mChart.setData(data, mChart.getTimeClass());
        }
    }

//    public void setTestData(int count, float range) {
//
//        float start = 0f;
//
//        List<BarEntry> yVals1 = new ArrayList<>();
//        for (int i = (int) start; i < start + count; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult);
//
//            if (Math.random() * 100 < 25) {
//                yVals1.add(new BarEntry(i, val, ContextCompat.getDrawable(mContext, android.R.drawable.btn_star)));
//            } else {
//                yVals1.add(new BarEntry(i, val));
//            }
//        }
//
//        BarDataSet set1;
//
//        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            set1.setDrawValues(false);  // 그래프 상단 숫자(값) 표시 여부
//            set1.setHighLightColor(Color.TRANSPARENT);  // 차트 클릭시 하이라이트 색상
//            set1.setHighLightAlpha(Color.TRANSPARENT);
//
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//        } else {
//            set1 = new BarDataSet(yVals1, "bottom label");
//            set1.setDrawValues(false);  // 그래프 상단 숫자(값) 표시 여부
//            set1.setDrawIcons(false);
//
//            // 그래프 색상 설정
//            set1.setColor(ContextCompat.getColor(mContext, R.color.color_E1147F));
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            // 그래프 두께 및 상단 값 세팅
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
//            data.setValueTextColor(Color.RED);
//            set1.setHighLightColor(Color.TRANSPARENT);  // 차트 클릭시 하이라이트 색상
//            set1.setHighLightAlpha(Color.TRANSPARENT);
////            data.setValueTypeface(mTfLight);
//            data.setBarWidth(0.9f);
//            data.setValueFormatter(new BarDataFormatter());
//
//            mChart.setData(data, mChart.getTimeClass());
//        }
//    }

}
