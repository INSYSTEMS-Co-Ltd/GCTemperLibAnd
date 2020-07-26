package com.greencross.gctemperlib.hana;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.GCTemperLib;
import com.greencross.gctemperlib.IGCResult;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.temper.TemperGraphFragment;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.greencare.util.TextWatcherUtil;
import com.greencross.gctemperlib.util.GpsInfo;

public class TemperControlFragment extends BaseFragment {

    private TextView mTemperNoticeTextview;
    private TextView mTemperTextview;

    public static Fragment newInstance() {
        TemperControlFragment fragment = new TemperControlFragment();
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

        View view = inflater.inflate(R.layout.fever_control_fragment, container, false);
        if (getActivity() instanceof DummyActivity) {
            getActivity().setTitle(getString(R.string.temper_control));
        }

        view.findViewById(R.id.go_graph_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyActivity.startActivity(TemperControlFragment.this, TemperGraphFragment.class, new Bundle());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTemperNoticeTextview = view.findViewById(R.id.temper_notice_message);
        mTemperTextview = view.findViewById(R.id.temper_textview);
        String temper = SharedPref.getInstance(getContext()).getPreferences(SharedPref.TEMPER) ;
        temper = TextUtils.isEmpty(temper) ? "0" : temper;
        mTemperTextview.setText(temper);
        getTemperMessage();

        view.findViewById(R.id.temper_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View temperInputAlertView = LayoutInflater.from(getContext()).inflate(R.layout.temper_input_alert, null, false);
                final EditText temperEt = temperInputAlertView.findViewById(R.id.temper_input_edittext);
                new TextWatcherUtil().setTextWatcher(temperEt, 50, 1);
                CDialog.showDlg(getContext(), temperInputAlertView)
                        .setOkButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String temper = temperEt.getText().toString();
                                mTemperTextview.setText(temper);
                                getTemperMessage();
                                SharedPref.getInstance(getContext()).savePreferences(SharedPref.TEMPER, temper);
                            }
                        })
                        .setNoButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                registTemper();
                            }
                        });
            }
        });

        // 체온 등록하기
        view.findViewById(R.id.temper_regist_done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsInfo gps = new GpsInfo(getContext());
                if (gps.isGetLocation()) {
                } else {
                    gps.showSettingsAlert();
                }
            }
        });
    }

    /**
     * 체온직접입력
     */
    private void registTemper() {
        String temper = mTemperTextview.getText().toString();
        final GCTemperLib gcLib = new GCTemperLib(getContext());

        showProgress();
        gcLib.registGCTemper(temper, new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                hideProgress();
                if (isSuccess) {
                    SharedPref.getInstance(getContext()).savePreferences(SharedPref.TEMPER, temper);
                    CDialog.showDlg(getActivity(), message);
                    getTemperMessage();
                } else {
                    CDialog.showDlg(getActivity(), message);
                }
            }
        });
    }


    private void getTemperMessage() {
        float temper = StringUtil.getFloat(mTemperTextview.getText().toString());
        Log.i(TAG, "getTemperMessage.temper="+temper);
        String result;
        if (temper == 0) {
            result = getString(R.string.temper_message1);
        } else if (temper < 36) {   // 36℃ 미만
            result =  getString(R.string.temper_message2);
        } else if (temper >= 36 && temper < 37.5) {   // 36℃ 이상 37.5℃ 미만
            result =  getString(R.string.temper_message3);
        } else if (temper >= 37.5 && temper < 38) {   // 37.5℃ 이상 38℃ 미만
            result =  getString(R.string.temper_message4);
        } else if (temper >= 38 && temper < 39) {   // 38℃ 이상 39℃ 미만
            result =  getString(R.string.temper_message5);
        } else if (temper >= 39 && temper < 40) {   //39℃ 이상 40℃ 미만
            result =  getString(R.string.temper_message6);
        } else if (temper >= 40) {  //40℃ 이상
            result =  getString(R.string.temper_message7);
        } else {
            result =  getString(R.string.temper_message1);
        }

        mTemperNoticeTextview.setText(result);
    }


}
