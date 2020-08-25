package com.greencross.gctemperlib.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.charting.data.BarData;
import com.greencross.gctemperlib.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isDrawHighlightArrowEnabled();
}
