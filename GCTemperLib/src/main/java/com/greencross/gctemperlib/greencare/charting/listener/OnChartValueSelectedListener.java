package com.greencross.gctemperlib.greencare.charting.listener;

import com.greencross.gctemperlib.greencare.charting.highlight.Highlight;
import com.greencross.gctemperlib.greencare.charting.data.CEntry;

/**
 * Listener for callbacks when selecting values inside the chart by
 * touch-gesture.
 *
 * @author Philipp Jahoda
 */
public interface OnChartValueSelectedListener {

    /**
     * Called when a value has been selected inside the chart.
     *
     * @param e The selected CEntry
     * @param h The corresponding highlight object that contains information
     *          about the highlighted position such as dataSetIndex, ...
     */
    void onValueSelected(CEntry e, Highlight h);

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    void onNothingSelected();
}
