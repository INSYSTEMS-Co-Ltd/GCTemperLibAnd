package com.greencross.gctemperlib.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.charting.components.YAxis;
import com.greencross.gctemperlib.charting.data.LineData;
import com.greencross.gctemperlib.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
