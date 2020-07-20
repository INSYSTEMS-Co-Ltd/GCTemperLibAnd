
package com.greencross.gctemperlib.greencare.charting.renderer;


import com.greencross.gctemperlib.greencare.charting.utils.ViewPortHandler;
import com.greencross.gctemperlib.greencare.util.ChartTimeUtil;

/**
 * Abstract baseclass of all Renderers.
 * 
 * @author Philipp Jahoda
 */
public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    public void setTimeClass(ChartTimeUtil timeClass) {
    }
}
