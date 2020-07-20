package com.greencross.gctemperlib.greencare.charting.interfaces.dataprovider;

import com.greencross.gctemperlib.greencare.charting.data.PressureData;

public interface PresureDataProvider extends BarLineScatterCandleBubbleDataProvider {

    PressureData getPresureData();
}
