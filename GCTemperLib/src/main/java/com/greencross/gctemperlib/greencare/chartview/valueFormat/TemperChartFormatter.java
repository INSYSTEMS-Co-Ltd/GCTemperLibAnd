package com.greencross.gctemperlib.greencare.chartview.valueFormat;


import android.text.TextUtils;

import com.greencross.gctemperlib.greencare.charting.components.AxisBase;
import com.greencross.gctemperlib.greencare.charting.formatter.IAxisValueFormatter;
import com.greencross.gctemperlib.greencare.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by philipp on 02/06/16.
 */
public class TemperChartFormatter implements IAxisValueFormatter {
//    private Tr_FeverList.Data[] mDatas;
    private List<String> mXlabels = new ArrayList<>();

    public TemperChartFormatter(List<String> xlabels ) {
        Collections.reverse(xlabels);
        mXlabels = xlabels;
//        if (datas != null)
//            mDatas = datas.toArray(new Tr_FeverList.Data[datas.size()]);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int idx = (int) value;
        if (idx > 0)
            if (idx < mXlabels.size()) {
                String date = mXlabels.get(idx);
                if (TextUtils.isEmpty(date) == false && date.length() >= 19) {
                    date = StringUtil.getIntString(date.substring(5));
                    date = date.substring(0,2)+"월"
                        + date.substring(2,4)+"일"
                        +"\n"+date.substring(4,6)+"시"
                        +date.substring(6,8)+"분";
                }
                return date;
            }
        return "";
    }
}
