package com.greencross.gctemperlib.charting.interfaces.datasets;

import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;

import com.greencross.gctemperlib.charting.data.Entry;
import com.greencross.gctemperlib.charting.formatter.FillFormatter;
import com.greencross.gctemperlib.charting.interfaces.datasets.ILineRadarDataSet;

/**
 * Created by Philpp Jahoda on 21/10/15.
 */
public interface ILineDataSet extends ILineRadarDataSet<Entry> {

    /**
     * Returns the intensity of the cubic lines (the effect intensity).
     * Max = 1f = very cubic, Min = 0.05f = low cubic effect, Default: 0.2f
     *
     * @return
     */
    float getCubicIntensity();

    /**
     * Returns true if drawing cubic lines is enabled, false if not.
     *
     * @return
     */
    boolean isDrawCubicEnabled();

    /**
     * Returns true if drawing stepped lines is enabled, false if not.
     *
     * @return
     */
    boolean isDrawSteppedEnabled();

    /**
     * Returns the size of the drawn circles.
     */
    float getCircleRadius();

    /**
     * Returns the color at the given index of the DataSet's circle-color array.
     * Performs a IndexOutOfBounds check by modulus.
     *
     * @param index
     * @return
     */
    int getCircleColor(int index);

    /**
     * Returns true if drawing circles for this DataSet is enabled, false if not
     *
     * @return
     */
    boolean isDrawCirclesEnabled();

    /**
     * Returns the color of the inner circle (the circle-hole).
     *
     * @return
     */
    int getCircleHoleColor();

    /**
     * Returns true if drawing the circle-holes is enabled, false if not.
     *
     * @return
     */
    boolean isDrawCircleHoleEnabled();

    /**
     * Returns the DashPathEffect that is used for drawing the lines.
     *
     * @return
     */
    DashPathEffect getDashPathEffect();

    /**
     * Returns true if the dashed-line effect is enabled, false if not.
     * If the DashPathEffect object is null, also return false here.
     *
     * @return
     */
    boolean isDashedLineEnabled();

    /**
     * Returns the FillFormatter that is set for this DataSet.
     *
     * @return
     */
    FillFormatter getFillFormatter();
}