package com.greencross.gctemperlib.hana;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.GCTemperLib;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.component.CDialog;

public class HealthCareServiceFragment extends BaseFragment {

    public static Fragment newInstance() {
        HealthCareServiceFragment fragment = new HealthCareServiceFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.health_care_service_fragment, container, false);
        if (getActivity() instanceof DummyActivity) {
            getActivity().setTitle(getString(R.string.title_health_care_service));
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.hearthcare_call_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "하나 고객전용 전화번호 요청 중", Toast.LENGTH_SHORT).show();
            }
        });
        
        view.findViewById(R.id.hearthcare_call_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "하나 고객전용 전화번호 요청 중", Toast.LENGTH_SHORT).show();
            }
        });
        
    }



}
