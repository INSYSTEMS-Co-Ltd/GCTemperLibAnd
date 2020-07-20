
package com.greencross.gctemperlib.greencare.chartview.food;

import android.content.Context;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.charting.components.MarkerView;
import com.greencross.gctemperlib.greencare.charting.data.CEntry;
import com.greencross.gctemperlib.greencare.charting.highlight.Highlight;
import com.greencross.gctemperlib.greencare.charting.utils.MPPointF;

import java.text.DecimalFormat;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class RadarMarkerView extends MarkerView {

    private TextView tvContent;
    private DecimalFormat format = new DecimalFormat("##0");

    public RadarMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.marker_date_textview);
//        tvContent.setTypeface(ResourcesCompat.getFont(context, R.font.nanum_barun_gothic_light));
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(CEntry e, Highlight highlight) {
        tvContent.setText(format.format(e.getY()) + " %");

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() - 10);
    }
}
