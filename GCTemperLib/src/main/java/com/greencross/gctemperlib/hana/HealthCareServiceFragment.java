package com.greencross.gctemperlib.hana;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.greencross.gctemperlib.TemperActivity;
import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Login;

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
                call();
            }
        });
        
        view.findViewById(R.id.hearthcare_call_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });
    }

    private void call() {
        Tr_Login login = SharedPref.getInstance(getContext()).getLoginInfo();
        if ("1000".equals(login.resultcode)) {
            CDialog dlg = CDialog.showDlg(getContext(), R.string.fever_health_call_alert_title, R.string.fever_health_call_alert_message);
            dlg.setOkButton(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tel = "tel:" + getString(R.string.health_call_center);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(tel));
                    startActivity(intent);
                }
            });
            dlg.setNoButton(getString(R.string.popup_dialog_button_cancel), null);
        } else {
            CDialog.showDlg(getContext(), R.string.fever_health_no_alert_title, R.string.fever_health_no_alert_message);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BackBaseActivity) {
            ((BackBaseActivity)getActivity()).reLogin();
        }
    }
}
