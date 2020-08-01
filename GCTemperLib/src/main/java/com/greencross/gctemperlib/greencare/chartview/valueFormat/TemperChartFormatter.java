package com.greencross.gctemperlib.greencare.chartview.valueFormat;


import android.text.TextUtils;
import android.util.Log;

import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.components.AxisBase;
import com.greencross.gctemperlib.greencare.charting.formatter.IAxisValueFormatter;
import com.greencross.gctemperlib.greencare.network.tr.hnData.Tr_FeverList;
import com.greencross.gctemperlib.greencare.util.StringUtil;

import java.util.List;

/**
 * Created by philipp on 02/06/16.
 */
public class TemperChartFormatter implements IAxisValueFormatter {
    private Tr_FeverList.Data[] mDatas;

    public TemperChartFormatter(List<Tr_FeverList.Data> datas ) {
        if (datas != null)
            mDatas = datas.toArray(new Tr_FeverList.Data[datas.size()]);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int idx = (int) value;
        if (idx > 0)
            if (idx < mDatas.length) {
                String date = mDatas[idx].input_de;
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
