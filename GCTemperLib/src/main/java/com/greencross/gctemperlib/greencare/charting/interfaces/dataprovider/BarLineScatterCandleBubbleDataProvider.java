package com.greencross.gctemperlib.greencare.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.greencare.charting.components.YAxis;
import com.greencross.gctemperlib.greencare.charting.data.BarLineScatterCandleBubbleData;
import com.greencross.gctemperlib.greencare.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    boolean isInverted(YAxis.AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
