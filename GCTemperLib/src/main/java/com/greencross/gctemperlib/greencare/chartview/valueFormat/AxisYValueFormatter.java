package com.greencross.gctemperlib.greencare.chartview.valueFormat;


import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.components.AxisBase;
import com.greencross.gctemperlib.greencare.charting.formatter.IAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class AxisYValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int idx = (int) value;
        if (idx > 31 && idx < 43)
            return ""+idx;
        else
            return "";
    }
}
