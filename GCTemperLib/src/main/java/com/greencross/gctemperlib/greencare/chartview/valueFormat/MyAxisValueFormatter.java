package com.greencross.gctemperlib.greencare.chartview.valueFormat;


import com.greencross.gctemperlib.greencare.charting.components.AxisBase;
import com.greencross.gctemperlib.greencare.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;

    public MyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " $";
    }
}
