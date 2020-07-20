
package com.greencross.gctemperlib.greencare.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.Log;

import com.greencross.gctemperlib.greencare.charting.animation.ChartAnimator;
import com.greencross.gctemperlib.greencare.charting.formatter.IValueFormatter;
import com.greencross.gctemperlib.greencare.charting.highlight.Highlight;
import com.greencross.gctemperlib.greencare.charting.highlight.Range;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_asstb_weight_hope_grp;
import com.greencross.gctemperlib.greencare.charting.buffer.BarBuffer;
import com.greencross.gctemperlib.greencare.charting.charts.WeightChart;
import com.greencross.gctemperlib.greencare.charting.data.BarData;
import com.greencross.gctemperlib.greencare.charting.data.BarEntry;
import com.greencross.gctemperlib.greencare.charting.data.CEntry;
import com.greencross.gctemperlib.greencare.charting.interfaces.datasets.IBarDataSet;
import com.greencross.gctemperlib.greencare.charting.utils.MPPointF;
import com.greencross.gctemperlib.greencare.charting.utils.Transformer;
import com.greencross.gctemperlib.greencare.charting.utils.Utils;
import com.greencross.gctemperlib.greencare.charting.utils.ViewPortHandler;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeightChartRenderer extends BarLineScatterCandleBubbleRenderer {
    private String TAG = WeightChartRenderer.class.getSimpleName();

    protected WeightChart mChart;
    /**
     * the rect object that is used for drawing the bars
     */
    protected RectF mBarRect = new RectF();

    protected BarBuffer[] mBarBuffers;

    protected Paint mShadowPaint;
    protected Paint mBarBorderPaint;

    /**
     * 빅데이터
     * @param bigData
     */
    float[] mBigDataBMIList;
    float[] mBigDataWeightList;
    public void setBigData(Tr_asstb_weight_hope_grp bigData) {
        int size = bigData.grp_list.size();
        mBigDataBMIList = new float[size * 2];

        float[] yMinMax = new float[2];

        List<Float> list = new ArrayList<>();
        List<Float> weightList = new ArrayList<>(); // 라인 그리기용 데이터
        int i = 0;
        for (Tr_asstb_weight_hope_grp.Grp_list data : bigData.grp_list) {
            // 체중범위 상단 라인
            list.add(StringUtil.getFloat(data.m_week));
            list.add(StringUtil.getFloat(data.bmi_max));

            // 몸무게 라인용 데이터
            if (StringUtil.getFloatVal(data.weight) != 0) {
                float weight = StringUtil.getFloat(data.weight);
                weightList.add(StringUtil.getFloat(data.m_week));
                weightList.add(weight);
            }

            i++;
        }

        List<Float> list2 = new ArrayList<>();
        // 체중범위 하단 라인
        for (Tr_asstb_weight_hope_grp.Grp_list data : bigData.grp_list) {
            float bmiMin = StringUtil.getFloat(data.bmi_min);
            list2.add(StringUtil.getFloat(data.bmi_min));
            list2.add(StringUtil.getFloat(data.m_week));
            i++;
        }

        Collections.reverse(list2);
        list.addAll(list2);

        mBigDataBMIList = toFloatArray(list); // 적정체중범위 그리기용

        int j = 0;
        for (float aa : mBigDataBMIList) {
            Log.i(TAG, "hopeList["+(j++)+"]="+aa);
        }

        mBigDataWeightList = toFloatArray(weightList); // 몸무게 라인 그리기용 데이터
    }

    public float[] toFloatArray(List<Float> alData) {
        if (alData == null)
            return null;

        if (alData.size() == 0)
            return new float[0];

        final int size = alData.size();
        float[] arData = new float[size];
        for (int i = 0; i < size; i++) {
            arData[i] = alData.get(i).floatValue();
        }

        return arData;
    }


    /**
     * 40주간 적정체중범위 데이터
     */
    float[] m40Datas;
    public void set40PathValue(float[] datas) {
        m40Datas = datas;
    }


    public WeightChartRenderer(WeightChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        initRender(chart, animator, viewPortHandler);
    }

    public void initRender(WeightChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        this.mChart = chart;

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.FILL);
        mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        // set alpha after color
        mHighlightPaint.setAlpha(120);

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);

        mBarBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarBorderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void initBuffers() {
        BarData barData = mChart.getBarData();
        mBarBuffers = new BarBuffer[barData.getDataSetCount()];

        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            mBarBuffers[i] = new BarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                    barData.getDataSetCount(), set.isStacked());
        }
    }

    @Override
    public void drawData(Canvas c) {
        BarData barData = mChart.getBarData();

        for (int i = 0; i < barData.getDataSetCount(); i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);

            if (set.isVisible()) {
                drawDataSet(c, set, i);
            }
        }

    }

    /**
     * 임신 40주간 적정 체중 범위 표시
     * @param c
     * @param trans
     *  차트 그리는 Path 순서
     *                       3
     *               2
     *       1               4
     * 0             5
     * 7     6
     */
    private void draw40WeightLevel(Canvas c, Transformer trans) {
        if (mChart.isPregnantTab() == false) {
            return;
        }

        if (m40Datas == null)
            return;

        float[] datas = m40Datas.clone();
        Paint paint = new Paint();
        Path path = new Path();
        paint.setColor(Color.parseColor("#ccffc0cb"));
//        path.setFillType(Path.FillType.EVEN_ODD);
        if (path.isEmpty() == false) {
            path.reset();
        }

        trans.pointValuesToPixel(datas);

        path.moveTo(datas[0], datas[1]);
        path.lineTo(datas[2], datas[3]);
        path.lineTo(datas[4], datas[5]);
        path.lineTo(datas[6], datas[7]);

        path.lineTo(datas[8], datas[9]);
        path.lineTo(datas[10], datas[11]);
        path.lineTo(datas[12], datas[13]);
        path.lineTo(datas[14], datas[15]);

        path.close();
        c.drawPath(path, paint);

        dotLinesVertical(c, trans);
    }


    /**
     * 세로라인그리기
     * @param c
     * @param trans
     */
    private void dotLinesVertical(Canvas c, Transformer trans) {
        Paint paint = new Paint();
        paint.setStrokeWidth(mChart.getAxisLeft().getAxisLineWidth()); //선의 굵기
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);

        float[] line14 = new float[]{15, 0, 15, c.getHeight()};
        trans.pointValuesToPixel(line14);
        c.drawLine(line14[0], line14[1], line14[2],line14[3] , paint);

        float[] line27 = new float[]{27, 0, 27, c.getHeight()};
        trans.pointValuesToPixel(line27);
        c.drawLine(line27[0], line27[1], line27[2],line27[3] , paint);
    }


    /**
     * 빅데이터 체중 라인 그리기
     * @param c
     * @param trans
     */
    private void drawBigDataChartLine(Canvas c, Transformer trans) {
        if (mBigDataWeightList == null)
            return;

        Paint paint = new Paint();
        Path path = new Path();
//        path.setFillType(Path.FillType.EVEN_ODD);
        if (path.isEmpty() == false) {
            path.reset();
        }


        // 빅데이터 그리기
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setColor(Color.parseColor("#00aa00"));


        float[] weightList = new float[mBigDataWeightList.length];
        System.arraycopy(mBigDataWeightList, 0, weightList, 0, weightList.length);
        trans.pointValuesToPixel(weightList);

//        path.moveTo(total40weekList[0], mTotal40weightList[0]);
//        Log.i(TAG, "draw40WeightLevel.moveto["+0+"]="+mTotal40weekList[0]+", mTotal40weightList["+0+"]="+mTotal40weightList[0]);
        for (int i = 0; i < weightList.length; i+=2) {
            float x = weightList[i];
            float y = weightList[i+1];
//            Log.i(TAG, "draw40WeightLevel["+i+"]="+total40weekList[i]+", mTotal40weightList["+i+"]="+total40weightList[i]);
            if (i == 0)
                path.moveTo(x, y);
            else
                path.lineTo(x, y);
        }
        paint.setAntiAlias(true);


        //그려진 선으로 shape을 만든다
        ShapeDrawable shape = new ShapeDrawable(new PathShape(path, 1, 1));
        shape.setBounds(0, 0, 1, 1);

        Paint spaint = shape.getPaint();
        spaint.setStyle(Paint.Style.STROKE);
        spaint.setColor(Color.parseColor("#00aa00"));
        spaint.setStrokeWidth(6f);
        spaint.setAntiAlias(true);
        shape.draw(c);

        drawBigDataCircleText(c, weightList);
    }

    /**
     * 차트 점, 값 그리기
     * @param c
     * @param weightList
     */
    private void drawBigDataCircleText(Canvas c, float[] weightList) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(mChart.getXAxis().getTextSize());

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.parseColor("#00aa00"));
        circlePaint.setStyle(Paint.Style.FILL);
        float circleRadius = 9f;
        float fontSize = mChart.getXAxis().getTextSize();

        // 첫번째 값
        int yPos = 1;
        int xPos = 0;
        String text = StringUtil.getNoneZeroString2(mBigDataWeightList[yPos]);
        float textXPos = weightList[xPos];
        c.drawText(text, textXPos, (weightList[yPos] - fontSize), textPaint);
        c.drawCircle(weightList[xPos]+8f, weightList[yPos], circleRadius, circlePaint);


        // 중간 값
        yPos = (mBigDataWeightList.length / 2) - ((int)mBigDataWeightList[xPos] % 2);   // 홀수, 짝수일 경우 보정값
        xPos = yPos-1;
        text = StringUtil.getNoneZeroString2(mBigDataWeightList[yPos]);
        textXPos = weightList[xPos] - ((fontSize * text.length())/4);
        Log.i(TAG, "middle.yPos"+yPos);
        if (yPos > 1) {
            c.drawText(text, textXPos, (weightList[yPos] - fontSize), textPaint);
            c.drawCircle(weightList[xPos], weightList[yPos], circleRadius, circlePaint);
        }

        // 마지막 값
        yPos = mBigDataWeightList.length-1;
        xPos = yPos-1;
        text = StringUtil.getNoneZeroString2(mBigDataWeightList[yPos]);
        textXPos = weightList[xPos] - ((text.length()*fontSize)/2) - 5f;
        c.drawText(text, textXPos, (weightList[yPos] - fontSize), textPaint);
        c.drawCircle(weightList[xPos]-8f, weightList[yPos], circleRadius, circlePaint);
    }

    /**
     * 빅데이터 적정 체중 범위 그리기
     * @param c
     * @param trans
     */
    private void drawBigDataChart(Canvas c, Transformer trans) {
        if (mBigDataBMIList == null)
            return;

        Paint paint = new Paint();
        Path path = new Path();

        if (path.isEmpty() == false) {
            path.reset();
        }

        float[] hopeList = new float[mBigDataBMIList.length];
        System.arraycopy(mBigDataBMIList, 0, hopeList, 0, hopeList.length);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#ccffc0cb"));
        trans.pointValuesToPixel(hopeList);

        for (int i = 0; i < hopeList.length; i+=2) {
            float x = hopeList[i];
            float y = hopeList[i+1];
//            Log.i(TAG, "draw40WeightLevel["+i+"]="+total40weekList[i]+", mTotal40weightList["+i+"]="+total40weightList[i]);
            if (i == 0)
                path.moveTo(x, y);
            else
                path.lineTo(x, y);
        }

        path.close();
        c.drawPath(path, paint);
    }

    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        drawBigDataChart(c, trans);         // 빅데이터 적정 체중 범위
        drawBigDataChartLine(c, trans);     // 빅데이터 그리기

        draw40WeightLevel(c, trans);    // 임신적정체중 그리기 시작

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // draw the bar shadow before the values
        // 바차트 그리기 시작
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        float beforeX = 0f;
        float beforeY = 0f;

        List<Float> xPos = new ArrayList<>();
        List<Float> yPos = new ArrayList<>();
        for (int j = 0; j < buffer.size(); j += 4) {

            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.*
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            }

            float circleSize = 10f;
            Paint paint = new Paint();
            paint.setColor(Color.rgb(225, 20, 127));
            float xVal=(buffer.buffer[j+2] + buffer.buffer[j])/2;
            float yVal = buffer.buffer[j+1];

            // 나누기 4 하는 이유는 원래 바차트에서 Rect 그리기 위한 값이였기 때문에 4포지션
            int realIdx = (j/4);
            BarEntry entry = dataSet.getEntryForIndex(realIdx);
            float realYVal = entry.getY();

            if (realYVal != 0) {
                c.drawCircle(xVal, yVal, circleSize, paint);
//                Logger.i("", "real Y value["+realIdx+"]="+realYVal+", xVal="+xVal+", yVal="+yVal);

                xPos.add(xVal);
                yPos.add(yVal);
            }
        }

        // 차트 연결 라인 그리기
        Paint linePaint = new Paint();
        linePaint.setColor(Color.rgb(225, 20, 127));
        linePaint.setStrokeWidth(2f);
        for (int i = 1; i <= xPos.size()-1 ; i++) {
            float xVal = xPos.get(i);
            float yVal = yPos.get(i);
            c.drawLine(xVal, yVal, xPos.get(i-1), yPos.get(i-1), linePaint);
        }
    }

    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {

        float left = x - barWidthHalf;
        float right = x + barWidthHalf;
        float top = y1;
        float bottom = y2;

        mBarRect.set(left, top, right, bottom);

        trans.rectToPixelPhase(mBarRect, mAnimator.getPhaseY());
    }

    @Override
    public void drawValues(Canvas c) {
        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            List<IBarDataSet> dataSets = mChart.getBarData().getDataSets();

            final float valueOffsetPlus = Utils.convertDpToPixel(4.5f);
            float posOffset = 0f;
            float negOffset = 0f;
            boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

            for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {

                IBarDataSet dataSet = dataSets.get(i);
                dataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, CEntry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                        if (value == 0f)
                            return "";
                        else
                            return "";
                    }
                });

                if (!shouldDrawValues(dataSet))
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());

                // calculate the correct offset depending on the draw position of
                // the value
                float valueTextHeight = Utils.calcTextHeight(mValuePaint, "0");
                posOffset = (drawValueAboveBar ? -valueOffsetPlus : valueTextHeight + valueOffsetPlus);
                negOffset = (drawValueAboveBar ? valueTextHeight + valueOffsetPlus : -valueOffsetPlus);

                if (isInverted) {
                    posOffset = -posOffset - valueTextHeight;
                    negOffset = -negOffset - valueTextHeight;
                }

                // get the buffer
                BarBuffer buffer = mBarBuffers[i];

                final float phaseY = mAnimator.getPhaseY();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);


                // if only single values are drawn (sum)
                if (!dataSet.isStacked()) {
                    int k = 0;
                    for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                        float x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2f;
                        if (!mViewPortHandler.isInBoundsRight(x))
                            break;

                        if (!mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])
                                || !mViewPortHandler.isInBoundsLeft(x))
                            continue;

                        BarEntry entry = dataSet.getEntryForIndex(j / 4);
                        float val = entry.getY();
                        if (dataSet.isDrawValuesEnabled()) {
                            int tmpHeight = 0;
                            if (k%2==0){
                                tmpHeight = 50;
                            }
                            // 그래프 상단 숫자 그리기
                            drawValue(c, dataSet.getValueFormatter(), val, entry, i, x,
                                    val >= 0 ?
                                            (buffer.buffer[j + 1] + posOffset)+tmpHeight :
                                            (buffer.buffer[j + 3] + negOffset)+tmpHeight,
                                    dataSet.getValueTextColor(j / 4));
                        }

                        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                            Drawable icon = entry.getIcon();

                            float px = x;
                            float py = val >= 0 ?
                                    (buffer.buffer[j + 1] + posOffset) :
                                    (buffer.buffer[j + 3] + negOffset);

                            px += iconsOffset.x;
                            py += iconsOffset.y;

                            Utils.drawImage(
                                    c,
                                    icon,
                                    (int)px,
                                    (int)py,
                                    icon.getIntrinsicWidth(),
                                    icon.getIntrinsicHeight());
                        }
                        k++;
                    }
                    // if we have stacks
                } else {
                    Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                    int bufferIndex = 0;
                    int index = 0;

                    while (index < dataSet.getEntryCount() * mAnimator.getPhaseX()) {
                        BarEntry entry = dataSet.getEntryForIndex(index);

                        float[] vals = entry.getYVals();
                        float x = (buffer.buffer[bufferIndex] + buffer.buffer[bufferIndex + 2]) / 2f;

                        int color = dataSet.getValueTextColor(index);

                        // we still draw stacked bars, but there is one
                        // non-stacked
                        // in between
                        if (vals == null) {

                            if (!mViewPortHandler.isInBoundsRight(x))
                                break;

                            if (!mViewPortHandler.isInBoundsY(buffer.buffer[bufferIndex + 1])
                                    || !mViewPortHandler.isInBoundsLeft(x))
                                continue;

                            if (dataSet.isDrawValuesEnabled()) {
                                drawValue(c, dataSet.getValueFormatter(), entry.getY(), entry, i, x, buffer.buffer[bufferIndex + 1] + (entry.getY() >= 0 ? posOffset : negOffset), color);
                                Logger.i("", "bufferIndex["+bufferIndex+"] getY="+entry.getY()+", getYVals="+entry.getYVals());
                            }

                            if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                Drawable icon = entry.getIcon();

                                float px = x;
                                float py = buffer.buffer[bufferIndex + 1] +
                                        (entry.getY() >= 0 ? posOffset : negOffset);

                                px += iconsOffset.x;
                                py += iconsOffset.y;

                                Utils.drawImage(
                                        c,
                                        icon,
                                        (int)px,
                                        (int)py,
                                        icon.getIntrinsicWidth(),
                                        icon.getIntrinsicHeight());
                            }

                            // draw stack values
                        } else {

                            float[] transformed = new float[vals.length * 2];
                            float posY = 0f;
                            float negY = -entry.getNegativeSum();

                            for (int k = 0, idx = 0; k < transformed.length; k += 2, idx++) {

                                float value = vals[idx];
                                float y;

                                if (value == 0.0f && (posY == 0.0f || negY == 0.0f)) {
                                    // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                                    y = value;
                                } else if (value >= 0.0f) {
                                    posY += value;
                                    y = posY;
                                } else {
                                    y = negY;
                                    negY -= value;
                                }
                                transformed[k + 1] = y * phaseY;
                            }

                            trans.pointValuesToPixel(transformed);

                            for (int k = 0; k < transformed.length; k += 2) {

                                final float val = vals[k / 2];
                                final boolean drawBelow =
                                        (val == 0.0f && negY == 0.0f && posY > 0.0f) ||
                                                val < 0.0f;
                                float y = transformed[k + 1]
                                        + (drawBelow ? negOffset : posOffset);

                                if (!mViewPortHandler.isInBoundsRight(x))
                                    break;

                                if (!mViewPortHandler.isInBoundsY(y)
                                        || !mViewPortHandler.isInBoundsLeft(x))
                                    continue;

                                if (dataSet.isDrawValuesEnabled()) {
                                    drawValue(c,
                                            dataSet.getValueFormatter(),
                                            vals[k / 2],
                                            entry,
                                            i,
                                            x,
                                            y,
                                            color);
                                }

                                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                                    Drawable icon = entry.getIcon();

                                    Utils.drawImage(
                                            c,
                                            icon,
                                            (int)(x + iconsOffset.x),
                                            (int)(y + iconsOffset.y),
                                            icon.getIntrinsicWidth(),
                                            icon.getIntrinsicHeight());
                                }
                            }
                        }

                        bufferIndex = vals == null ? bufferIndex + 4 : bufferIndex + 4 * vals.length;
                        index++;
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BarData barData = mChart.getBarData();

        for (Highlight high : indices) {

            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            BarEntry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

//            mHighlightPaint.setColor(set.getHighLightColor());
//            mHighlightPaint.setAlpha(set.getHighLightAlpha());

            mHighlightPaint.setColor(Color.TRANSPARENT);    // 차트 클릭시 하이라이트 색상 투명처리
            mHighlightPaint.setAlpha(Color.TRANSPARENT);    // 차트 클릭시 하이라이트 색상 투명처리

            boolean isStack = (high.getStackIndex() >= 0  && e.isStacked()) ? true : false;

            final float y1;
            final float y2;

            if (isStack) {

                if(mChart.isHighlightFullBarEnabled()) {

                    y1 = e.getPositiveSum();
                    y2 = -e.getNegativeSum();

                } else {

                    Range range = e.getRanges()[high.getStackIndex()];

                    y1 = range.from;
                    y2 = range.to;
                }

            } else {
                y1 = e.getY();
                y2 = 0.f;
            }

            prepareBarHighlight(e.getX(), y1, y2, barData.getBarWidth() / 2f, trans);

            setHighlightDrawPos(high, mBarRect);

            c.drawRect(mBarRect, mHighlightPaint);
        }
    }

    /**
     * Sets the drawing position of the highlight object based on the riven bar-rect.
     * @param high
     */
    protected void setHighlightDrawPos(Highlight high, RectF bar) {
        high.setDraw(bar.centerX(), bar.top);
    }

    @Override
    public void drawExtras(Canvas c) {
    }
}
