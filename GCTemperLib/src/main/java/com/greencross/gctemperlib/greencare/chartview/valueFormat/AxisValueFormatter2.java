package com.greencross.gctemperlib.greencare.chartview.valueFormat;


import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.components.AxisBase;
import com.greencross.gctemperlib.greencare.charting.formatter.IAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class AxisValueFormatter2 implements IAxisValueFormatter {
    private int monthMax = 31;
    private final String[] mWeeks = new String[] {
            "", "일", "월", "화", "수", "목", "금", "토", ""
    };

    private TypeDataSet.Period mPeriod;

    public AxisValueFormatter2(TypeDataSet.Period period) {
        mPeriod = period;
    }

    public void setMonthMax(int monthMax) {
        this.monthMax = monthMax;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int idx = (int) value;

        if (mPeriod == TypeDataSet.Period.PERIOD_DAY) {
            String label = (idx < 0 || idx > 23) ? "" : (""+ idx);
            label = (idx % 2 == 0) ? "" : label;
            return label;
        } else if (mPeriod == TypeDataSet.Period.PERIOD_WEEK) {
            if (idx > 0 && mWeeks.length > idx) {
                return mWeeks[idx];
            } else {
                return "";
            }
        } else if (mPeriod == TypeDataSet.Period.PERIOD_MONTH) {
            String label = (idx == 0 || idx >= monthMax) ? "" : ""+ (idx);
            label = (idx % 2 == 0) ? "" : label;
            return label;
        } else if (mPeriod == TypeDataSet.Period.PERIOD_YEAR) {
            String label = (idx == 0 || idx >= 13) ? "" : (""+ idx);
            return label;
        } else if (mPeriod == TypeDataSet.Period.PERIOD_PRAGNANT) {
            String label = (""+ idx);
            return label;
        } else {
            return "!"+idx;
        }
    }
}
