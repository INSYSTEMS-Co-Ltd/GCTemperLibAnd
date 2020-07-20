package com.greencross.gctemperlib.greencare.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.greencare.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
