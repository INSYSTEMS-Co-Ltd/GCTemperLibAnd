package com.greencross.gctemperlib.greencare.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.greencare.charting.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isHighlightFullBarEnabled();
}
