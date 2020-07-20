package com.greencross.gctemperlib.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;



/**
 * Created by jihoon on 2016-03-21.
 * 공유 데이터 클래스
 * @since 0, 1
 */
public class CViewpagerScrollDuration extends Scroller {



    private double mScrollFactor = 1;



    public CViewpagerScrollDuration(Context context) {

        super(context);

    }



    public CViewpagerScrollDuration(Context context, Interpolator interpolator) {

        super(context, interpolator);

    }



    @SuppressLint("NewApi")

    public CViewpagerScrollDuration(Context context, Interpolator interpolator, boolean flywheel) {

        super(context, interpolator, flywheel);

    }



    /**

     * Set the factor by which the duration will change

     */

    public void setScrollDurationFactor(double scrollFactor) {

        mScrollFactor = scrollFactor;

    }



    @Override

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {

        duration =1500;

        super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));

    }



}