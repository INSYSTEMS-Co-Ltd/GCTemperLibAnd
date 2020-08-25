package com.greencross.gctemperlib.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.charting.components.YAxis.AxisDependency;
import com.greencross.gctemperlib.charting.data.BarLineScatterCandleBubbleData;
import com.greencross.gctemperlib.charting.interfaces.dataprovider.ChartInterface;
import com.greencross.gctemperlib.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}
