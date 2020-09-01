package com.greencross.gctemperlib.hana;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.hana.component.CDialog;
import com.greencross.gctemperlib.util.CDateUtil;
import com.greencross.gctemperlib.util.SharedPref;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Login;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HealthCareServiceFragment extends BaseFragment {

    private ImageView callBtn1;
    private ImageView callBtn2;

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
        callBtn1 = view.findViewById(R.id.hearthcare_call_btn1);
        callBtn2 = view.findViewById(R.id.hearthcare_call_btn2);

        callBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });

        callBtn2.setOnClickListener(new View.OnClickListener() {
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

    /**
     * 남은이용일수 구하기
     * @return
     */
    private void getRemainUseDate() {
        // 남은 이용일수 구하기
        Tr_Login login = SharedPref.getInstance(getContext()).getLoginInfo();
        if ("1000".equals(login.resultcode)) {
            // DB전송이 완료 된 경우
            long time = CDateUtil.getTime(CDateUtil.FORMAT_yyyy_MM_dd, login.enddate);

            Calendar c = Calendar.getInstance(); // 비교할 시간
            c.setTime(new Date(time));
            c.clear(Calendar.HOUR);
            c.clear(Calendar.MINUTE);
            c.clear(Calendar.SECOND);
            c.clear(Calendar.MILLISECOND); // 시간, 분, 초, 밀리초 초기화

            Calendar c2 = Calendar.getInstance(); // 현재 시간
            c2.clear(Calendar.HOUR);
            c2.clear(Calendar.MINUTE);
            c2.clear(Calendar.SECOND);
            c2.clear(Calendar.MILLISECOND); // 시간, 분, 초, 밀리초 초기화
            long dDayDiff = c.getTimeInMillis() - c2.getTimeInMillis();
            int day = (int)(Math.floor(TimeUnit.HOURS.convert(dDayDiff, TimeUnit.MILLISECONDS) / 24f)) +2;
            day = day < 0 ? 0 : day;

            if (callBtn1 != null && callBtn2 != null) {
                callBtn1.setVisibility(day <= 0 ? View.GONE : View.VISIBLE);
                callBtn2.setVisibility(day <= 0 ? View.GONE : View.VISIBLE);
            }
        } else {
            if (callBtn1 != null && callBtn2 != null) {
                callBtn1.setVisibility(View.VISIBLE);
                callBtn2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void reLoginComplete() {
        getRemainUseDate();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof BackBaseActivity) {
            ((BackBaseActivity)getActivity()).reLogin();
        }
    }


}
