package com.greencross.gctemperlib.greencare.weight;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.greencare.base.BaseFragment;
import com.greencross.gctemperlib.greencare.base.DummyActivity;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.greencare.util.TextWatcherUtil;
import com.greencross.gctemperlib.R;

/**
 * 체중예측
 * Created by insystemscompany on 2017. 2. 28..
 */

public class WeightBigDataInputFragment extends BaseFragment {
    private static final String TAG = WeightBigDataInputFragment.class.getSimpleName();
    private final String PREF_KEY_WEEK_INPUT = "PREF_KEY_WEEK_INPUT";

    public static int REQ_WEIGHT_PREDICT = 454;
    public static String INTENT_KEY_WEEK = "intent_key_week";
    public static String INTENT_KEY_WEIGHT = "intent_key_weight";

    private EditText mInputWeek;
    private EditText mInputKg;
    private TextView mSaveButton;
    private android.widget.LinearLayout activitymain;
    private String recentWeight;

    public static Fragment newInstance() {
        WeightBigDataInputFragment fragment = new WeightBigDataInputFragment();
        return fragment;
    }

    private void setActionBar() {
        // CommonActionBar actionBar 는 안 씀 한화꺼
        if (getActivity() instanceof DummyActivity) {
            DummyActivity activity = (DummyActivity) getActivity();

            ImageView tipbtn = (ImageView) activity.findViewById(R.id.actionbar_tip_btn);
            tipbtn.setVisibility(View.GONE);
            tipbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.actionTipBtnClick(getContext(), StringUtil.getIntVal(mInputWeek.getText().toString()));
                }
            });

            TextView titleTv = (TextView) activity.findViewById(R.id.common_title_tv);
            titleTv.setText(getString(R.string.mother_health_wt_prediction));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bigdata_input, container, false);
        this.mSaveButton = (TextView) view.findViewById(R.id.show_result_button);
        this.mInputKg = (EditText) view.findViewById(R.id.mom_weight_kg);
        this.mInputWeek = (EditText) view.findViewById(R.id.mom_weight_week_et);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBar();

        recentWeight = CommonData.getInstance(getContext()).getMotherWeight();
        String period_week = CommonData.getInstance(getContext()).getMberPeriodWeek();
        mInputKg.setText(recentWeight);
//        mInputWeek.setText(StringUtil.getIntVal(period_week)< 14 ?"14":period_week);
        mInputWeek.setText(period_week);

        view.findViewById(R.id.bigdata_recent_tv).setVisibility(TextUtils.isEmpty(recentWeight) ? View.INVISIBLE : View.VISIBLE);

//        String savedWeek = SharedPref.getInstance(getContext()).getPreferences(PREF_KEY_WEEK_INPUT);
//        if (TextUtils.isEmpty(savedWeek) == false) {
//            mInputWeek.setText(savedWeek);
//        }

        new TextWatcherUtil().setTextWatcher(mInputWeek, 39, 0);
        new TextWatcherUtil().setTextWatcher(mInputKg, 130, 2);


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = mInputKg.getText().toString();
                String week = mInputWeek.getText().toString();
                SharedPref.getInstance(getContext()).savePreferences(PREF_KEY_WEEK_INPUT, week);

                if (validCheck(week, weight)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(INTENT_KEY_WEIGHT, weight);
                    bundle.putString(INTENT_KEY_WEEK, week);

                    if (getActivity() instanceof DummyActivity) {
                        DummyActivity activity = (DummyActivity)getActivity();
                        activity.replaceFragment(WeightBigDataChartFragment.newInstance(), false, true, bundle);
                    }
                }
            }
        });
    }

    private boolean validCheck(String weekVal, String weightVal) {

        if (TextUtils.isEmpty(weekVal)) {
            CDialog.showDlg(getContext(),"임신기간 입력해 주세요.");
            return false;
        }

        if (TextUtils.isEmpty(weightVal)) {
            CDialog.showDlg(getContext(),"몸무게를 입력해 주세요.");
            return false;
        }

        int week = StringUtil.getIntger(weekVal);
        float weight = StringUtil.getFloat(weightVal);
        float Curweight = StringUtil.getFloat(recentWeight);

        float lowWeight = Curweight-30;
        float overWeight = Curweight+30;

        if (week < 14 || 39 < week) {
            CustomAlertDialog mDialog =   new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_A);
            mDialog.setContent("14주 ~ 39주를 입력하세요.");
            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
            mDialog.show();
            return false;
        }


        if ( weight < Curweight-15 || Curweight+30 < weight) {
            CustomAlertDialog mDialog =   new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_A);
            mDialog.setContent("임신 전 체중보다 30kg 이상 증가 또는\n15kg 이상 감소한 체중 입력 시 예측이\n불가능합니다.\n\n임신 전 체중 : "+ Curweight +"kg");
            mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
            mDialog.show();
            return false;
        }

        return true;
//        doWeightHopeGrp();
    }

//    /**
//     * 빅데이터 40주 그래프
//     */
//    private void doWeightHopeGrp() {
//        Tr_asstb_weight_hope_grp.RequestData requestData = new Tr_asstb_weight_hope_grp.RequestData();
//
//        CommonData login = CommonData.getInstance(getContext());
//        requestData.mber_sn = login.getMberSn();
//        requestData.input_kg = mInputKg.getText().toString();
//        requestData.input_week = mInputWeek.getText().toString();
//
//        new ApiData().getData(getContext(), Tr_asstb_weight_hope_grp.class, requestData, new ApiData.IStep() {
//            @Override
//            public void next(Object obj) {
//                if (obj instanceof Tr_asstb_weight_hope_grp) {
//                    Tr_asstb_weight_hope_grp tr = (Tr_asstb_weight_hope_grp)obj;
//                    if ("Y".equals(tr.data_yn)) {
//
//                    } else {
//                        CDialog.showDlg(getContext(), "데이터 수신에 실패 하였습니다.");
//                    }
//                } else {
//                    CDialog.showDlg(getContext(), "데이터 수신에 실패 하였습니다.");
//                }
//            }
//        });
//    }
}