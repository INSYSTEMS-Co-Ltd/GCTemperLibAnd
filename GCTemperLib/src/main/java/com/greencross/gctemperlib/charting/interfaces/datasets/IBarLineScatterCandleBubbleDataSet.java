package com.greencross.gctemperlib.charting.interfaces.datasets;

import com.greencross.gctemperlib.charting.data.Entry;
import com.greencross.gctemperlib.charting.interfaces.datasets.IDataSet;

/**
 * Created by philipp on 21/10/15.
 */
public interface IBarLineScatterCandleBubbleDataSet<T extends Entry> extends IDataSet<T> {

    /**
     * Returns the color that is used for drawing the highlight indicators.
     *
     * @return
     */
    int getHighLightColor();
}
