package com.greencross.gctemperlib.common;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;


/**
 * Created by jihoon on 2016-03-21.
 * 공유 데이터 클래스
 * @since 0, 1
 */
public class CustomViewPager extends ViewPager {


    private boolean enabled;

    Float START_X;

    public CustomViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

//        this.enabled = true;

        postInitViewPager();

    }
    @Override

    public void setCurrentItem(int item, boolean smoothScroll) {

// TODO Auto-generated method stub

        super.setCurrentItem(item, smoothScroll);



    }

    private CViewpagerScrollDuration mScroller = null;



    /**

     * Override the Scroller instance with our own class so we can change the

     * duration

     */

    private void postInitViewPager() {

        try {

            Class<?> viewpager = ViewPager.class;

            Field scroller = viewpager.getDeclaredField("mScroller");

            scroller.setAccessible(true);

            Field interpolator = viewpager.getDeclaredField("sInterpolator");

            interpolator.setAccessible(true);



            mScroller = new CViewpagerScrollDuration(getContext(),

                    (Interpolator) interpolator.get(null));

            scroller.set(this, mScroller);

        } catch (Exception e) {

        }

    }



    /**

     * Set the factor by which the duration will change

     */

    public void setScrollDurationFactor(double scrollFactor) {

        mScroller.setScrollDurationFactor(scrollFactor);

    }





}