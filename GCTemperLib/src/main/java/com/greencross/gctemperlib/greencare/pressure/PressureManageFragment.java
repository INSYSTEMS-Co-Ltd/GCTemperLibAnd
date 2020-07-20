package com.greencross.gctemperlib.greencare.pressure;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.greencross.gctemperlib.greencare.base.value.TypeDataSet;
import com.greencross.gctemperlib.greencare.charting.data.PressureEntry;
import com.greencross.gctemperlib.greencare.chartview.presure.PresureChartView;
import com.greencross.gctemperlib.greencare.chartview.valueFormat.AxisValueFormatter3;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.greencare.base.BaseFragment;
import com.greencross.gctemperlib.greencare.base.DummyActivity;
import com.greencross.gctemperlib.greencare.database.DBHelper;
import com.greencross.gctemperlib.greencare.database.DBHelperMessage;
import com.greencross.gctemperlib.greencare.database.DBHelperPresure;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_infra_message_write;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.ChartTimeUtil;
import com.greencross.gctemperlib.greencare.util.DisplayUtil;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.webview.TipWebViewActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by insystemscompany on 2017. 2. 28..
 */

public class PressureManageFragment extends BaseFragment {
    private final String TAG = PressureManageFragment.class.getSimpleName();

    public ChartTimeUtil mTimeClass;
    private PresureChartView mChart;
    private TextView mDateTv;

    private LinearLayout layout_pressure_history;
    private LinearLayout layout_pressure_graph;
    private TextView btn_pressure_graph;
    private TextView btn_pressure_history;

    private TextView mStatTv;
    private TextView mesureResultTv;
    private TextView mesureResultTv2;
    private TextView mBottomSystolicTv;
    private TextView mBottomDiastolcTv;
    private TextView mBottomMinTv;
    private TextView mBottomMaxTv;
    private ImageButton imgPre_btn;
    private ImageButton imgNext_btn;

    private PressureSwipeListView mSwipeListView;
    private AxisValueFormatter3 xFormatter;

    private View mVisibleView1;
    private View mVisibleView2;
    private View mVisibleView3;
    private View mVisibleView4;
    private View mVisibleView5;
    private ScrollView mContentScrollView;
    private LinearLayout mChartFrameLayout;
    private ImageView mChartCloseBtn, mChartZoomBtn;;
    private LinearLayout mHCallBtn;

    private TextView Hcall_tv;
    private LinearLayout tip_lv;

    public static Fragment newInstance() {
        PressureManageFragment fragment = new PressureManageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pressure_manage, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDateTv = (TextView) view.findViewById(R.id.period_date_textview);

        layout_pressure_graph  = (LinearLayout) view.findViewById(R.id.layout_pressure_graph);
        layout_pressure_history  = (LinearLayout) view.findViewById(R.id.layout_pressure_history);
        btn_pressure_graph  = (TextView) view.findViewById(R.id.btn_pressure_graph);
        btn_pressure_history  = (TextView) view.findViewById(R.id.btn_pressure_history);

        mesureResultTv  = (TextView) view.findViewById(R.id.measure_result_textview);
        mesureResultTv2  = (TextView) view.findViewById(R.id.measure_result_textview2);


        RadioGroup periodRg = (RadioGroup) view.findViewById(R.id.period_radio_group);
        RadioButton radioBtnDay = (RadioButton) view.findViewById(R.id.period_radio_btn_day);
        RadioButton radioBtnWeek = (RadioButton) view.findViewById(R.id.period_radio_btn_week);
        RadioButton radioBtnMonth = (RadioButton) view.findViewById(R.id.period_radio_btn_month);

        mStatTv = (TextView) view.findViewById(R.id.textView21);
        mBottomSystolicTv = (TextView) view.findViewById(R.id.bottom_avg_min_textview);
        mBottomDiastolcTv = (TextView) view.findViewById(R.id.bottom_avg_max_textview);
        mBottomMinTv = (TextView) view.findViewById(R.id.bottom_min_textview);
        mBottomMaxTv = (TextView) view.findViewById(R.id.bottom_max_textview);

        imgPre_btn                  = (ImageButton) view.findViewById(R.id.pre_btn);
        imgNext_btn                 = (ImageButton) view.findViewById(R.id.next_btn);

        tip_lv = view.findViewById(R.id.tip_lv);

        view.findViewById(R.id.pre_btn).setOnClickListener(mClickListener);
        view.findViewById(R.id.next_btn).setOnClickListener(mClickListener);
        view.findViewById(R.id.target_value_btn).setOnClickListener(mClickListener);
        tip_lv.setOnClickListener(mClickListener);

        view.findViewById(R.id.btn_pressure_graph).setOnClickListener(mClickListener);
        view.findViewById(R.id.btn_pressure_history).setOnClickListener(mClickListener);
        periodRg.setOnCheckedChangeListener(mCheckedChangeListener);

        //탭초기 설정
        layout_pressure_history.setVisibility(View.GONE);
        layout_pressure_graph.setVisibility(View.VISIBLE);
        btn_pressure_history.setSelected(false);
        btn_pressure_graph.setSelected(true);

        mTimeClass = new ChartTimeUtil(radioBtnDay, radioBtnWeek, radioBtnMonth);
        mChart = new PresureChartView(getContext(), view);

        getData();

        mSwipeListView = new PressureSwipeListView(view, PressureManageFragment.this);

        /*mActonbarBtn.findViewById(R.id.action_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DummyActivity.startActivityForResult(PressureManageFragment.this, 1111, PressureInputFragment.class, new Bundle());
            }
        });*/

        setNextButtonVisible();
//        setGoneHcall_btn();

        // 상담연결하기
        mHCallBtn = view.findViewById(R.id.Hcall_btn);
        Hcall_tv = view.findViewById(R.id.Hcall_tv);
        mHCallBtn.setOnClickListener(mClickListener);

        if(CommonData.getInstance(getContext()).getMberGrad().equals("10")) {
            Hcall_tv.setText("혈압관리 상담 (무료)");
        } else {
            Hcall_tv.setText("혈압관리 상담");
        }


        // 차트 전체 화면 처리
        mVisibleView1 = view.findViewById(R.id.radioGroup2);
        mVisibleView2 = view.findViewById(R.id.period_radio_group);
        mVisibleView3 = view.findViewById(R.id.visible_layout_3);
        mVisibleView4 = view.findViewById(R.id.visible_layout_4);
        mVisibleView5 = view.findViewById(R.id.result_tip_layout);
        mContentScrollView = view.findViewById(R.id.view_scrollview);
        mChartFrameLayout = view.findViewById(R.id.chart_frame_layout);
        mChartCloseBtn = view.findViewById(R.id.chart_close_btn);
        mChartZoomBtn = view.findViewById(R.id.landscape_btn);

        mChartCloseBtn.setOnClickListener(mClickListener);
        mChartZoomBtn.setOnClickListener(mClickListener);

        setVisibleOrientationLayout();


        //click 저장
        OnClickListener ClickListener = new OnClickListener(mClickListener, view, getContext());

        //엄마 건강
        view.findViewById(R.id.btn_pressure_graph).setOnTouchListener(ClickListener);
        radioBtnDay.setOnTouchListener(ClickListener);
        radioBtnWeek.setOnTouchListener(ClickListener);
        radioBtnMonth.setOnTouchListener(ClickListener);
        view.findViewById(R.id.btn_pressure_history).setOnTouchListener(ClickListener);
        mHCallBtn.setOnTouchListener(ClickListener);

        //코드 부여(엄마 건강)
        view.findViewById(R.id.btn_pressure_graph).setContentDescription(getString(R.string.btn_pressure_graph));
        radioBtnDay.setContentDescription(getString(R.string.radioBtnpressureDay));
        radioBtnWeek.setContentDescription(getString(R.string.radioBtnpressureWeek));
        radioBtnMonth.setContentDescription(getString(R.string.radioBtnpressureMonth));
        view.findViewById(R.id.btn_pressure_history).setContentDescription(getString(R.string.btn_pressure_history));
        mHCallBtn.setContentDescription(getString(R.string.HCallBtn12));


    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();

            if (vId == R.id.pre_btn) {
                mTimeClass.calTime(-1);
                getData();
            } else if (vId == R.id.next_btn) {
                // 초기값 일때 다음날 데이터는 없으므로 리턴
                if (mTimeClass.getCalTime() == 0)
                    return;

                mTimeClass.calTime(1);
                getData();
            }else if (vId == R.id.btn_pressure_graph) {

                layout_pressure_history.setVisibility(View.GONE);
                layout_pressure_graph.setVisibility(View.VISIBLE);
//                Hcallbtn.setVisibility(View.VISIBLE);

                btn_pressure_history.setSelected(false);
                btn_pressure_graph.setSelected(true);

                getData();
            }else if (vId == R.id.btn_pressure_history) {

                layout_pressure_graph.setVisibility(View.GONE);
                layout_pressure_history.setVisibility(View.VISIBLE);
//                Hcallbtn.setVisibility(View.GONE);

                btn_pressure_history.setSelected(true);
                btn_pressure_graph.setSelected(false);

                mSwipeListView.getHistoryData();
            }else if (vId == R.id.target_value_btn) {
                DummyActivity.startActivityForResult(getActivity(), 1111, PressureInputMainFragment.class, new Bundle());
            }else if(vId == R.id.landscape_btn){
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }else if(vId == R.id.chart_close_btn){
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }else if(vId == R.id.Hcall_btn){
                if(CommonData.getInstance(getContext()).getMberGrad().equals("10")) {
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
                }else{
                    CustomAlertDialog mDialog = new CustomAlertDialog(getContext(), CustomAlertDialog.TYPE_B);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.call_center2));
                    mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), null);
                    mDialog.setPositiveButton(getString(R.string.do_call), (dialog, button) -> {
                        String tel = "tel:" + getString(R.string.call_center_number2);
//                        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                        Intent intentCall = new Intent(Intent.ACTION_DIAL);
                        intentCall.setData(Uri.parse(tel));
                        startActivity(intentCall);
                        dialog.dismiss();
                    });
                    mDialog.show();
                }
            } else if(vId == R.id.tip_lv){
                Intent intent = new Intent(getActivity(), TipWebViewActivity.class);
                intent.putExtra("Title", getString(R.string.psy_tip));
                intent.putExtra(CommonData.EXTRA_URL_POSITION, 3);
                startActivity(intent);
            }

            setNextButtonVisible();

        }
    };

    private void setNextButtonVisible(){
        // 초기값 일때 다음날 데이터는 없으므로 리턴
        if (mTimeClass.getCalTime() == 0) {
            imgNext_btn.setVisibility(View.INVISIBLE);
        }else{
            imgNext_btn.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 일간,주간,월간
     */
    public RadioGroup.OnCheckedChangeListener mCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            // 일간, 주간, 월간
            TypeDataSet.Period periodType = mTimeClass.getPeriodType();
            mTimeClass.clearTime();         // 날자 초기화

            if(periodType == TypeDataSet.Period.PERIOD_DAY){
                mStatTv.setText(getString(R.string.daily_statistics));
            }else if(periodType == TypeDataSet.Period.PERIOD_WEEK){
                mStatTv.setText(getString(R.string.weekly_statistics));
            }else if(periodType == TypeDataSet.Period.PERIOD_MONTH){
                mStatTv.setText(getString(R.string.monthly_statistics));
            }

            getData();   // 날자 세팅 후 조회
        }
    };

    /**
     * 날자 계산 후 조회
     */
    private void getData() {
        long startTime = mTimeClass.getStartTime();
        long endTime = mTimeClass.getEndTime();

        xFormatter = new AxisValueFormatter3(mTimeClass.getPeriodType());
        mChart.setXValueFormat(xFormatter);

        String format = "yyyy.MM.dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String startDate = sdf.format(startTime);
        String endDate = sdf.format(endTime);

        if (mTimeClass.getPeriodType() == TypeDataSet.Period.PERIOD_DAY) {
            mDateTv.setText(startDate);
        } else {
            mDateTv.setText(startDate +" ~ "+endDate);
        }

        format = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(format);
        startDate = sdf.format(startTime);
        endDate = sdf.format(endTime);
        getBottomDataLayout(startDate, endDate);
    }

    /**
     * 하단 데이터 세팅하기
     * @param startDate
     * @param endDate
     */
    private void getBottomDataLayout(String startDate, String endDate) {
        DBHelper helper = new DBHelper(getContext());
        DBHelperPresure db = helper.getPresureDb();
        DBHelperPresure.PressureData bottomData = db.getResultStatic(helper, startDate, endDate);

        mBottomSystolicTv.setText(""+bottomData.getSystolic());
        mBottomDiastolcTv.setText(""+bottomData.getDiastolc());
        mBottomMaxTv.setText(""+bottomData.getMaxsystolic());
        mBottomMinTv.setText(""+bottomData.getMaxdiastolc());

        // 건강메시지
        DBHelperMessage messageDb = helper.getMessageDb();
        DBHelperMessage.MessageData msgData = messageDb.getResultRecentMessage(helper, Tr_infra_message_write.INFRA_TY_HEALTH);
        if (msgData != null) {
            // 건강메시지
            if (TextUtils.isEmpty(msgData.getMessage()) == false) {
                String[] messages = msgData.getMessage().split("\n\n");
                mesureResultTv.setText(messages[0]);
                if (messages.length >= 2)
                    mesureResultTv2.setText(messages[1]);

            } else {
                mesureResultTv.setText("");
            }
        }

        new QeuryVerifyDataTask().execute();
    }

    public class QeuryVerifyDataTask extends AsyncTask<Void, Void, List<PressureEntry>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        protected List<PressureEntry> doInBackground(Void... params) {
            List<PressureEntry> yVals1 = null;
            DBHelper helper = new DBHelper(getContext());
            DBHelperPresure presureDb = helper.getPresureDb();
            TypeDataSet.Period period = mTimeClass.getPeriodType();

            mChart.setXvalMinMax(mTimeClass);

            if (period == TypeDataSet.Period.PERIOD_DAY) {
                String toDay = CDateUtil.getFormattedString_yyyy_MM_dd(mTimeClass.getStartTime());
                yVals1 = presureDb.getResultDay(helper, toDay);
//                mChart.setLabelCnt(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_WEEK));
            } else if (period == TypeDataSet.Period.PERIOD_WEEK) {
                String startDay = CDateUtil.getFormattedString_yyyy_MM_dd(mTimeClass.getStartTime());
                String endDay = CDateUtil.getFormattedString_yyyy_MM_dd(mTimeClass.getEndTime());

                yVals1 = presureDb.getResultWeek(helper, startDay, endDay);

//                mChart.setLabelCnt(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_WEEK));
            } else if (period == TypeDataSet.Period.PERIOD_MONTH) {
                String startDay = CDateUtil.getFormattedString_yyyy(mTimeClass.getStartTime());
                String endDay = CDateUtil.getFormattedString_MM(mTimeClass.getStartTime());
                // 이번달 최대 일수
                Calendar cal = Calendar.getInstance(); // CDateUtil.getCalendar_yyyyMMdd(startDay);
                cal.setTime(new Date(mTimeClass.getStartTime()));
                int dayCnt = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                yVals1 = presureDb.getResultMonth(helper, startDay, endDay, dayCnt);

                xFormatter.setMonthMax(dayCnt);

                Logger.i(TAG, "dayCnt="+dayCnt+", month="+(cal.get(Calendar.MONTH)+1) ) ;
                // sqlite 조회 하여 결과 가져오기
//                mChart.setLabelCnt((dayCnt/2));
            }
            return yVals1;
        }

        @Override
        protected void onPostExecute(List<PressureEntry> yVals1) {
            super.onPostExecute(yVals1);
            hideProgress();

            Logger.i(TAG, "yVals1.size="+yVals1.size());
            mChart.setData(yVals1, mTimeClass);
            mChart.invalidate();
            setNextButtonVisible();
        }
    }

    private boolean isLandScape = false;
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
     * 가로, 세로모드일때 불필요한 화면 Visible 처리
     */
    private void setVisibleOrientationLayout() {
        mVisibleView1.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView2.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView3.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView4.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mVisibleView5.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mHCallBtn.setVisibility(isLandScape ? View.GONE : View.VISIBLE);
        mChartCloseBtn.setVisibility(isLandScape ? View.VISIBLE : View.GONE);
        mChartZoomBtn.setVisibility(isLandScape ? View.GONE : View.VISIBLE);

        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mChartFrameLayout.getLayoutParams();
        android.util.Log.i(TAG, "isLandScape="+isLandScape+", dm.widthPixels="+dm.widthPixels+", dm.heightPixels="+dm.heightPixels );

//        int height = (int) (dm.heightPixels - mDateLayout.getLayoutParams().height);//(dm.heightPixels *0.20)); // 15% 작게
        int landHeight = (int) (dm.heightPixels - dm.heightPixels * 0.30); // 가로모드 세로사이즈 30% 작게
        int portHeight = DisplayUtil.getDpToPix(getContext(), 230);    // 세로모드일때 사이즈 230dp
        params.height = isLandScape ? landHeight : portHeight;

        mChartFrameLayout.setLayoutParams(params);
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
    public void onResume() {
        super.onResume();
        getData();
        mSwipeListView.getHistoryData();
    }
}