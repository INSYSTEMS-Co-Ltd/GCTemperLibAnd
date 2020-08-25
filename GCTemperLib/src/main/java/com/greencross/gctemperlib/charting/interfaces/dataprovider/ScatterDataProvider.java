package com.greencross.gctemperlib.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.charting.data.ScatterData;
import com.greencross.gctemperlib.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
