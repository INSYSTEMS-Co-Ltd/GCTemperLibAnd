package com.greencross.gctemperlib.greencare.chartview.valueFormat;


import com.greencross.gctemperlib.greencare.charting.data.CEntry;
import com.greencross.gctemperlib.greencare.charting.formatter.IValueFormatter;
import com.greencross.gctemperlib.greencare.charting.utils.ViewPortHandler;

/**
 * Created by mrsohn on 2017. 3. 1..
 */

public class BarDataFormatter implements IValueFormatter {

    @Override
    public String getFormattedValue(float value, CEntry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        int idx = (int) value;
        return idx == 0 ? "": String.format("%,d", idx);
    }
}
