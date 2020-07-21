package com.greencross.gctemperlib.greencare.chartview.food;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.BaseFragment;

/**
 * Created by MrsWin on 2017-03-12.
 */

public class RadarChartFragment extends BaseFragment {

    public static Fragment newInstance() {
        RadarChartFragment fragment = new RadarChartFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_radarchart_noseekbar, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new RadarChartView(getContext(), view);
    }
}
