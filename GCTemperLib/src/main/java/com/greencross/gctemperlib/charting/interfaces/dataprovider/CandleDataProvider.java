package com.greencross.gctemperlib.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.charting.data.CandleData;
import com.greencross.gctemperlib.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
