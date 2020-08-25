package com.greencross.gctemperlib.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.charting.data.BubbleData;
import com.greencross.gctemperlib.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
