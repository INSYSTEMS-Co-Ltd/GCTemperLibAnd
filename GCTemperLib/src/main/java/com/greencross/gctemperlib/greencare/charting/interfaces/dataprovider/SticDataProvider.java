package com.greencross.gctemperlib.greencare.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.greencare.charting.data.SticData;

public interface SticDataProvider extends BarLineScatterCandleBubbleDataProvider {

    SticData getCandleData();
}
