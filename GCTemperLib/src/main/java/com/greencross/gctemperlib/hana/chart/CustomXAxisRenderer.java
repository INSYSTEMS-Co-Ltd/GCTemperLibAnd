package com.greencross.gctemperlib.hana.chart;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class CustomXAxisRenderer extends XAxisRenderer {
    private final String TAG = getClass().getSimpleName();

    public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }


    @Override
    protected void drawLabels(Canvas c, float pos, PointF anchor) {
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();

        // pre allocate to save performance (dont allocate in loop)
        float[] position = new float[]{
                0f, 0f
        };

        for (int i = mMinX; i <= mMaxX; i += mXAxis.mAxisLabelModulus) {
            position[0] = i;
            mTrans.pointValuesToPixel(position);
            if (mViewPortHandler.isInBoundsX(position[0])) {
                String label = mXAxis.getValues().get(i);
                String label2 = null;
                int newLineIdx = label.indexOf("\n");
                if (newLineIdx > 0) {
                    label2 = label.substring(newLineIdx);
                    label = label.substring(0, newLineIdx);
                }

                if (mXAxis.isAvoidFirstLastClippingEnabled()) {
                    // avoid clipping of the last
                    if (i == mXAxis.getValues().size() - 1 && mXAxis.getValues().size() > 1) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                        if (width > mViewPortHandler.offsetRight() * 2
                                && position[0] + width > mViewPortHandler.getChartWidth())
                            position[0] -= width / 2;

                        // avoid clipping of the first
                    } else if (i == 0) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                        position[0] += width / 2;
                    }
                }
                drawLabel(c, label, i, position[0], pos, anchor, labelRotationAngleDegrees);
                if (newLineIdx > 0) {
                    float height = Utils.calcTextHeight(mAxisLabelPaint, label);
                    drawLabel(c, label2, i, position[0], pos+height, anchor, labelRotationAngleDegrees);
                }
            }
        }
    }

    @Override
    protected void drawLabel(Canvas c, String label, int xIndex, float x, float y, PointF anchor, float angleDegrees) {
        String formattedLabel = mXAxis.getValueFormatter().getXValue(label, xIndex, mViewPortHandler);
//        Log.i(TAG, "drawLabel.label=" + label + ", x=" + x + ", y=" + y);
        Utils.drawText(c, formattedLabel, x, y, mAxisLabelPaint, anchor, angleDegrees);
    }



    @Override
    public void computeAxis(float xValMaximumLength, List<String> xValues) {

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());

        StringBuilder widthText = new StringBuilder();

        int xValChars = Math.round(xValMaximumLength);

        for (int i = 0; i < xValChars; i++) {
            widthText.append('h');
        }

        final FSize labelSize = Utils.calcTextSize(mAxisLabelPaint, widthText.toString());

        final float labelWidth = labelSize.width;
        final float labelHeight = Utils.calcTextHeight(mAxisLabelPaint, "Q") * 2;   // 두줄을 사용해야 해서 x2

        final FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(
                labelWidth,
                labelHeight,
                mXAxis.getLabelRotationAngle());

        StringBuilder space = new StringBuilder();
        int xValSpaceChars = mXAxis.getSpaceBetweenLabels();

        for (int i = 0; i < xValSpaceChars; i++) {
            space.append('h');
        }

        final FSize spaceSize = Utils.calcTextSize(mAxisLabelPaint, space.toString());

        mXAxis.mLabelWidth = Math.round(labelWidth + spaceSize.width);
        mXAxis.mLabelHeight = Math.round(labelHeight);
        mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width + spaceSize.width);
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);

        mXAxis.setValues(xValues);
    }
}
