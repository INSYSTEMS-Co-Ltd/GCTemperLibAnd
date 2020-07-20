package com.greencross.gctemperlib.greencare.weight;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.greencare.base.BaseFragment;
import com.greencross.gctemperlib.greencare.base.DummyActivity;
import com.greencross.gctemperlib.greencare.base.IBackPress;
import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.data.BarEntry;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.AxisValueFormatter2;
import com.greencross.gctemperlib.greencare.chartview.weight.WeightChartView;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_asstb_weight_hope_grp;
import com.greencross.gctemperlib.greencare.util.ChartTimeUtil;
import com.greencross.gctemperlib.greencare.util.DisplayUtil;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by insystemscompany on 2017. 2. 28..
 */

public class WeightBigDataChartFragment extends BaseFragment implements IBackPress {
    private final String TAG = WeightBigDataChartFragment.class.getSimpleName();
    public CommonData commonData;// = CommonData.getInstance();
    public ChartTimeUtil mTimeClass;

    private RelativeLayout mTitleLayout;

    protected WeightChartView mWeightChart;
//    protected FatChartView mFatChart;

    private TextView mCommentTv;
    private LinearLayout mHCallBtn;

    protected LinearLayout layout_weight_graph;              // 그래프 레이아웃
    protected View weight_bigdata_chart_layout;

    private ImageView mBigDataInfoIv;
    private LinearLayout mChartFrameLayout;
    private ScrollView mContentScrollView;

//    private DBHelperWeight.WeightStaticData mWeightStaticData;
    private AxisValueFormatter2 xFormatter;

    private View mVisibleView1;
    private View mVisibleView2;
//    private LinearLayout mChartFrameLayout;
    private ImageView mChartCloseBtn, mChartZoomBtn;

    private String mWeek;
    private String mWeight;

    private TextView Hcall_tv;

    public static Fragment newInstance() {
        WeightBigDataChartFragment fragment = new WeightBigDataChartFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bigdata_chart, container, false);
        commonData = CommonData.getInstance(getContext());
        if (getActivity() instanceof DummyActivity) {
            DummyActivity activity = (DummyActivity) getActivity();

            ImageView tipbtn = (ImageView) activity.findViewById(R.id.actionbar_tip_btn);
            tipbtn.setVisibility(View.GONE);
            tipbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.actionTipBtnClick(getContext(), StringUtil.getIntVal(SharedPref.getInstance(getContext()).getPreferences("PREF_KEY_WEEK_INPUT")));
                }
            });

            TextView titleTv = (TextView) activity.findViewById(R.id.common_title_tv);
            titleTv.setText(getString(R.string.mother_health_wt_prediction_result));

            mTitleLayout = activity.getmBgLayout();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        super.setBackPress(this);

        layout_weight_graph = (LinearLayout) view.findViewById(R.id.layout_weight_graph);
        weight_bigdata_chart_layout = (LinearLayout) view.findViewById(R.id.weight_bigdata_chart_layout);

        mCommentTv = view.findViewById(R.id.commentTxt);
        mBigDataInfoIv = view.findViewById(R.id.bigdata_info_iv);
        mHCallBtn = view.findViewById(R.id.Hcall_btn);

        Hcall_tv = view.findViewById(R.id.Hcall_tv);

        if(CommonData.getInstance(getContext()).getMberGrad().equals("10")) {
            Hcall_tv.setText("체중관리 상담 (무료)");
        } else {
            Hcall_tv.setText("체중관리 상담");
        }

        mTimeClass = new ChartTimeUtil(TypeDataSet.Period.PERIOD_PRAGNANT);
        mWeightChart = new WeightChartView(getContext(), view);
//        mFatChart = new FatChartView(getContext(), view);
        mWeightChart.setIsPragnant(false);

        mContentScrollView = view.findViewById(R.id.view_scrollview);
        mChartFrameLayout = view.findViewById(R.id.chart_frame_layout);

        TypeDataSet.Period periodType = mTimeClass.getPeriodType();
        mTimeClass.clearTime();         // 날자 초기화

        xFormatter = new AxisValueFormatter2(periodType);
        mWeightChart.setXValueFormat(xFormatter);
//        mFatChart.setXValueFormat(xFormatter);

        weight_bigdata_chart_layout.setVisibility(View.VISIBLE);

        // 차트 전체 화면 처리
        mVisibleView1 = view.findViewById(R.id.visible_layout_1);
        mVisibleView2 = view.findViewById(R.id.visible_layout_2);
//        mChartFrameLayout = view.findViewById(R.id.chart_frame_layout);
        mChartCloseBtn = view.findViewById(R.id.chart_close_btn);
        mChartZoomBtn = view.findViewById(R.id.landscape_btn);

        mChartCloseBtn.setOnClickListener(mClickListener);
        mChartZoomBtn.setOnClickListener(mClickListener);
        mHCallBtn.setOnClickListener(mClickListener);
        mBigDataInfoIv.setOnClickListener(mClickListener);
        weight_bigdata_chart_layout.findViewById(R.id.target_value_btn).setOnClickListener(mClickListener);

        Bundle bundle = getArguments();
        mWeek = bundle.getString(WeightBigDataInputFragment.INTENT_KEY_WEEK);
        mWeight = bundle.getString(WeightBigDataInputFragment.INTENT_KEY_WEIGHT);

        float xMin = StringUtil.getFloat(mWeek);
        float xMax = StringUtil.getFloat(mWeight);
        mWeightChart.setXvalMinMax(xMin, xMax, (int) (xMax-xMin));
        doWeightHopeGrp(mWeek, mWeight);
        setVisibleOrientationLayout();
    }

    /**
     * 빅데이터 40주 그래프
     */
    private void doWeightHopeGrp(String week, String weight) {
        Tr_asstb_weight_hope_grp.RequestData requestData = new Tr_asstb_weight_hope_grp.RequestData();

        CommonData login = CommonData.getInstance(getContext());
        requestData.mber_sn = login.getMberSn();
        requestData.input_week = week;
        requestData.input_kg = weight;
//        if (BuildConfig.DEBUG)
//            requestData.mber_sn = "103088";

        new ApiData().getData(getContext(), Tr_asstb_weight_hope_grp.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_asstb_weight_hope_grp) {
                    Tr_asstb_weight_hope_grp tr = (Tr_asstb_weight_hope_grp)obj;
                    if ("Y".equals(tr.data_yn)) {
                        // 신청완료
                        mWeightChart.setBigData(tr);    // bigData넣어주기

                        String message = "";
                        if (TextUtils.isEmpty(tr.db_msg_01) == false) {
                            message += tr.db_msg_01;
                        }

                        if (TextUtils.isEmpty(tr.db_msg_02) == false) {

                            if (TextUtils.isEmpty(tr.db_msg_01) == false) {
                                message += "--------------------------------------\n";
                            }
                            message += tr.db_msg_02;
                        }


                        if (TextUtils.isEmpty(tr.db_msg_03) == false) {
                            if (TextUtils.isEmpty(tr.db_msg_02) == false) {
                                message += "--------------------------------------\n";
                            }
                            message += tr.db_msg_03;
                        }
                        mCommentTv.setText(message);

                        List<BarEntry> weightYVals = new ArrayList<>();
                        for (int i = 0; i < 40; i++) {
                            weightYVals.add(new BarEntry(i, 0));
                        }

                        if (tr.grp_list.size() > 0) {
                            // X, Y 값 세팅 하기
                            Tr_asstb_weight_hope_grp.Grp_list grp = tr.grp_list.get(0);    // 0번째 데이터
                            float xWeekMin = StringUtil.getFloat(grp.m_week);
                            float yMinBmi = StringUtil.getFloat(grp.bmi_min);
                            float yMinWeight = StringUtil.getFloat(grp.weight);
                            float yMin = yMinBmi < yMinWeight ? yMinBmi : yMinWeight;

                            grp = tr.grp_list.get(tr.grp_list.size()-1);    // 0번째 데이터
                            float xWeekMax = StringUtil.getFloat(grp.m_week);

                            float yMaxBmi = StringUtil.getFloat(grp.bmi_max);
                            float yMaxWeight = StringUtil.getFloat(grp.weight);
                            float yMax = yMaxBmi < yMaxWeight ? yMaxWeight : yMaxBmi;

                            // X축 최소 최대 세팅
                            mWeightChart.setXvalMinMax(xWeekMin, xWeekMax, 20);
                            int yLabelCnt = (int)(yMax - yMin);
                            mWeightChart.setYvalMinMax(yMin-3, yMax+3, yLabelCnt+6); // 최소값 최대값이 없으면 임의로 넣어줌

                        } else {
                            mWeightChart.setXvalMinMax(14, 40, 20);
                            mWeightChart.setYvalMinMax(40, 70, 20); // 최소값 최대값이 없으면 임의로 넣어줌
                        }

                        mWeightChart.animateY();
                        mWeightChart.setData(weightYVals, mTimeClass);

                    }
                } else {
                    CDialog.showDlg(getContext(), "데이터 수신에 실패 하였습니다.");
                }
            }
        });
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();

            if(vId == R.id.landscape_btn){
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }else if(vId == R.id.chart_close_btn){
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }else if(vId == R.id.target_value_btn){
//                ((MotherHealthMainActivity)getContext()).actionBtnClick();
            } else if(vId == R.id.bigdata_info_iv) {
                bigDataInfoPopup();
            } else if(vId == R.id.Hcall_btn) {
                if ("10".equals(CommonData.getInstance(getContext()).getMberGrad())) {
                    CustomAlertDialog mDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getContext().getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getContext().getString(R.string.do_call_center));
                    mDialog.setNegativeButton(getContext().getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getContext().getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                        String tel = "tel:" + getContext().getString(R.string.call_center_number);
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(tel));
                        getContext().startActivity(intent);
                        dialog.dismiss();
                    });

                    mDialog.show();
                } else {
                    CustomAlertDialog mDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getContext().getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getContext().getString(R.string.call_center2));
                    mDialog.setNegativeButton(getContext().getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getContext().getString(R.string.do_call), (dialog, button) -> {
                        String tel = "tel:" + getContext().getString(R.string.call_center_number2);
//                        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                        Intent intentCall = new Intent(Intent.ACTION_DIAL);
                        intentCall.setData(Uri.parse(tel));
                        getContext().startActivity(intentCall);
                        dialog.dismiss();
                    });
                    mDialog.show();
                }
            }
        }
    };

    protected boolean isLandScape = false;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        GLog.i("onConfigurationChanged="+newConfig.orientation, "");
        switch (newConfig.orientation){
            case Configuration.ORIENTATION_LANDSCAPE: //가로 모드
                isLandScape = true;
                break;
            case Configuration.ORIENTATION_PORTRAIT: //세로 모드
                isLandScape = false;
                break;
        }

        setVisibleOrientationLayout();
    }

    /**
     * 임신중 체중안내
     * mber_mother_bdwgh_view 전문에서 문구
     */
    public void bigDataInfoPopup() {
        CustomAlertDialog mDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_A);
        mDialog.setTitle("");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.popup_bigdata_info, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mDialog.setContentView(view, params);
//        mDialog.setPositiveButton(getContext().getString(R.string.popup_dialog_button_confirm), null);

        view.findViewById(R.id.bigdata_popup_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    /**
     * 가로, 세로모드일때 불필요한 화면 Visible 처리
     */
    protected void setVisibleOrientationLayout() {
        mTitleLayout.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView1.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView2.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mChartCloseBtn.setVisibility(isLandScape ? View.VISIBLE : View.GONE);
        mChartZoomBtn.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mHCallBtn.setVisibility(isLandScape ? View.GONE : View.VISIBLE);

        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mChartFrameLayout.getLayoutParams();
        Log.i(TAG, "isLandScape="+isLandScape+", dm.widthPixels="+dm.widthPixels+", dm.heightPixels="+dm.heightPixels );

//        int height = (int) (dm.heightPixels - mDateLayout.getLayoutParams().height);//(dm.heightPixels *0.20)); // 15% 작게
        int landHeight = (int) (dm.heightPixels - dm.heightPixels * 0.08); // 가로모드 세로사이즈 30% 작게
//        int landHeight =  dm.heightPixels;
        int portHeight = DisplayUtil.getDpToPix(getContext(), 400);    // 세로모드일때 사이즈 400dp
        params.height = isLandScape ? landHeight : portHeight;

        mChartFrameLayout.setLayoutParams(params);

        mContentScrollView.setBackgroundColor(isLandScape ? ContextCompat.getColor(getContext(), R.color.colorWhite) : ContextCompat.getColor(getContext(), R.color.bg_gray));
        // 가로모드일때 스크롤뷰 막기
        mContentScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isLandScape;
            }
        });
        //가로모드 전환 시 스크롤 상단으로 위치
        mContentScrollView.smoothScrollTo(0,0);
    }

    @Override
    public void onBackPressed() {
        if (isLandScape) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.setBackPress(null);
            getActivity().onBackPressed();
        }
    }
}