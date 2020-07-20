package com.greencross.gctemperlib.greencare.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.greencare.charting.components.YAxis;
import com.greencross.gctemperlib.greencare.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
