package com.greencross.gctemperlib.hana.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.greencross.gctemperlib.charting.charts.CombinedChart;

public class CustomCombinedChart extends CombinedChart {
    public CustomCombinedChart(Context context) {
        super(context);
        init();
    }

    public CustomCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void init() {
        super.init();
        // 차트 X축 라벨 설정
        mXAxisRenderer = new CustomXAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer);
    }

}
