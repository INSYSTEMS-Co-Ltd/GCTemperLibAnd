package com.greencross.gctemperlib.greencare.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.greencare.charting.data.BarData;

public interface WeightBarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isHighlightFullBarEnabled();

    boolean isPregnantTab();   // 임산부 체중 임신기간여부

}
