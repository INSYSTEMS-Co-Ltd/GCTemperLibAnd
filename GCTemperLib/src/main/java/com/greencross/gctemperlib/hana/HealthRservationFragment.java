package com.greencross.gctemperlib.hana;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.GCTemperLib;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Login;

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


        TextView tv = view.findViewById(R.id.regist_code_tv);
        Tr_Login login = SharedPref.getInstance(getContext()).getLoginInfo();
        tv.setText(login.cmpny_ub_code);

        // 가입코드 복사하기
        view.findViewById(R.id.ever_health_code_copy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("everhealthcode", login.cmpny_ub_code);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getContext(), "복사 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 에버헬스바로가기
        view.findViewById(R.id.go_ever_health_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEverHeath();
            }
        });
    }

    public void openEverHeath() {
        String packageName = "com.ubicare.wellness";
        try {
            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } catch (Exception e) {
            CDialog.showDlg(getContext(), "사용하시는 스마트폰에 \n에버헬스 가족검진 앱이 설치되어있지 않습니다.\n" + "앱을 설치하기 위해 앱스토어로 이동합니다.")
                    .setOkButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = "market://details?id=" + packageName;
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            getContext().startActivity(i);
                        }
                    })
                    .setNoButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

        }
    }

}
