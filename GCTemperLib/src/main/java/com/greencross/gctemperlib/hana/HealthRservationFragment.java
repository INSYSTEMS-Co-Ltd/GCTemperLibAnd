package com.greencross.gctemperlib.hana;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.GCTemperLib;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.component.CDialog;

/**
 * 건강검진 예약
 */
public class HealthRservationFragment extends BaseFragment {

    public static Fragment newInstance() {
        HealthRservationFragment fragment = new HealthRservationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.health_reservation_fragment, container, false);
        if (getActivity() instanceof DummyActivity) {
            getActivity().setTitle(getString(R.string.title_health_reservation_fragment));
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }



}
