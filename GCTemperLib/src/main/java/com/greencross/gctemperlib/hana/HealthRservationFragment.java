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

public class HealthRservationFragment extends BaseFragment {

    public static Fragment newInstance() {
        HealthRservationFragment fragment = new HealthRservationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GCTemperLib gcHeatLib = new GCTemperLib(getContext());
        if (gcHeatLib.isAvailableGCToken() == false) {
            CDialog.showDlg(getContext(), "인증 후 이용 가능합니다.")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            getActivity().finish();
                        }
                    });
            return null;
        }

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
