package com.greencross.gctemperlib.charting.interfaces.datasets;

import com.greencross.gctemperlib.charting.charts.ScatterChart;
import com.greencross.gctemperlib.charting.data.Entry;
import com.greencross.gctemperlib.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;

/**
 * Created by philipp on 21/10/15.
 */
public interface IScatterDataSet extends ILineScatterCandleRadarDataSet<Entry> {

    /**
     * Returns the currently set scatter shape size
     *
     * @return
     */
    float getScatterShapeSize();

    /**
     * Returns all the different scattershapes the chart uses
     *
     * @return
     */
    ScatterChart.ScatterShape getScatterShape();

    /**
     * Returns radius of the hole in the shape
     *
     * @return
     */
    float getScatterShapeHoleRadius();

    /**
     * Returns the color for the hole in the shape
     *
     * @return
     */
    int getScatterShapeHoleColor();
}
