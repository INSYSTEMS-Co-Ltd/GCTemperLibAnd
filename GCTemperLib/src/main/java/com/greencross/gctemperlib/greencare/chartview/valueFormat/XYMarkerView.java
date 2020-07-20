package com.greencross.gctemperlib.greencare.chartview.valueFormat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.components.MarkerView;
import com.greencross.gctemperlib.greencare.charting.data.CEntry;
import com.greencross.gctemperlib.greencare.charting.formatter.IAxisValueFormatter;
import com.greencross.gctemperlib.greencare.charting.highlight.Highlight;
import com.greencross.gctemperlib.greencare.charting.utils.MPPointF;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.ChartTimeUtil;
import com.greencross.gctemperlib.greencare.util.StringUtil;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class XYMarkerView extends MarkerView {
    private final String TAG = getClass().getSimpleName();

    private TextView mDateTv;
    private TextView mValueTv;
    private IAxisValueFormatter xAxisValueFormatter;
    private DecimalFormat format;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        mDateTv = (TextView) findViewById(R.id.marker_date_textview);
        mValueTv = (TextView) findViewById(R.id.marker_value_textview);
        format = new DecimalFormat("###.0");
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)

    @Override
    public void refreshContent(CEntry e, Highlight highlight) {
        ChartTimeUtil timeClass = getChartView().getTimeClass();
        long ll = timeClass.getStartTime();
        TypeDataSet.Period periodType = timeClass.getPeriodType();

//        일간 : 월일 시(터치한 그래프바의 전 1시간 표시)​ 예) 0월 00일​ 11시~12시​​
//        주간 : 월일 년도 표시​ 예) 0월 00일 0000년​
//        월간 : 월일 년도 표시​ 예) 0월 00일​ 0000년​
//        년간 : 월 년도 표시​ 예) 0월 0000년​
        mDateTv.setText("XXX x: " + xAxisValueFormatter.getFormattedValue(e.getX(), null) + ", y: " + format.format(e.getY()));

        Calendar disCal = (Calendar) timeClass.getStartTimeCal().clone();
        String dateStr = null;
        if (xAxisValueFormatter instanceof AxisValueFormatter) {
            AxisValueFormatter formatter = (AxisValueFormatter) xAxisValueFormatter;
           String xFormatStr = formatter.getFormattedValue(e.getX(), null);

            if (TypeDataSet.Period.PERIOD_DAY == periodType) {
                int hour = (int)e.getX();
                dateStr = disCal.get(Calendar.MONTH) +"월"+disCal.get(Calendar.DATE)+"일\n"
                        +hour+"시~"+(hour+1)+"시";
                Log.i(TAG, "refreshContent=" + CDateUtil.getFormattedString(ll, "yyyy-MM-dd") + ", periodType=" + periodType.name() + ", xFormatStr=" + dateStr);
            } else if (TypeDataSet.Period.PERIOD_WEEK == periodType || TypeDataSet.Period.PERIOD_MONTH == periodType) {
                disCal.add(Calendar.DATE, (int) e.getX()-1);
//                String day = formatter.mWeeks[(int) e.getX()];
                dateStr = disCal.get(Calendar.MONTH) + "월" + disCal.get(Calendar.DATE) + "일\n"
                        + disCal.get(Calendar.YEAR) + "년";
                Log.i(TAG, "refreshContent=" + CDateUtil.getFormattedString(ll, "yyyy-MM-dd") + ", periodType=" + periodType.name() + ", xFormatStr=" + dateStr);
            } else if (TypeDataSet.Period.PERIOD_YEAR == periodType) {
                disCal.add(Calendar.DATE, (int) e.getX()-1);
//                String day = formatter.mWeeks[(int) e.getX()];
                dateStr = ((int)e.getX())+ "주";
                Log.i(TAG, "refreshContent=" + CDateUtil.getFormattedString(ll, "yyyy-MM-dd") + ", periodType=" + periodType.name() + ", xFormatStr=" + dateStr);
//            } else {
//                GLog.i(TAG, "refreshContent="+ CDateUtil.getFormattedString(ll, "yyyy-MM-dd") +", periodType="+periodType.name()+", xFormatStr="+xFormatStr);
            }

            if (TextUtils.isEmpty(dateStr) == false)
                mDateTv.setText(dateStr);

            String noneZeroVal = StringUtil.getFormatPriceFloat(""+e.getY());
            mValueTv.setText( noneZeroVal + formatter.getUnitStr());
        }


        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
