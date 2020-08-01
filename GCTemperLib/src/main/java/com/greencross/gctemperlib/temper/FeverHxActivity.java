package com.greencross.gctemperlib.temper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.adapter.FeverHistoryListAdapter;
import com.greencross.gctemperlib.collection.AllDataItem;
import com.greencross.gctemperlib.collection.FeverItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.RequestApi;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class FeverHxActivity extends BackBaseActivity implements View.OnClickListener {

    TextView mTxtHistoryDate, mTxtGraphStartDate, mTxtGraphEndDate;
    Button mFeverGraphBtn, mFeverTimelineBtn;
    LinearLayout mLinearTabGraph, mLinearTabTimeline, mGraphDateLay, mMainLay;
    RecyclerView mRecyTimeline;
    FrameLayout mHelpLay;
    ImageButton mBtnHelpClose, mBtnCheckHelp;

    CombinedChart mHistoryGraph;
    String[] arrXDate;
    String[] arrDateSet;

    FeverHistoryListAdapter mAdapter;

    private Date mCurDate;
    GregorianCalendar mCalendar;
    ArrayList<FeverItem> mArrFeverList;

    private String mStrStartDate;
    private String mStrEndDate;
    private Date mStartDate;
    private Date mEndDate;
    int cntDay = 0;

    ArrayList<AllDataItem> mAllDataItems;
    SimpleDateFormat format;

    int tabNum = 0;

    boolean[] bSelFilter;
    boolean[] bTumpSelFilter;

    boolean mNoShowHelp = false;

    String[] arrFilter;

    private View view;

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        int tmpNum = intent.getIntExtra(CommonData.EXTRA_IS_TIMELIEN, 0);
        if(tmpNum != 0)
            tabNum = tmpNum;

        setTab(tabNum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fever_hx_activity);

        setTitle(getString(R.string.fever_hx_title));

        init();
        setEvent();
        initView();

        initGraph();
        initTimeLine();
    }

    /**
     * 초기화
     */
    public void init(){
        mMainLay = (LinearLayout)findViewById(R.id.main_lay);
        mHelpLay = (FrameLayout)findViewById(R.id.help_lay);

        mGraphDateLay = (LinearLayout)findViewById(R.id.graph_date_lay);
        mTxtGraphStartDate = (TextView)findViewById(R.id.txt_graph_start_date);
        mTxtGraphEndDate = (TextView)findViewById(R.id.txt_graph_end_date);

        mTxtHistoryDate = (TextView)findViewById(R.id.txt_history_date);
        mFeverGraphBtn = (Button)findViewById(R.id.fever_graph_btn);
        mFeverTimelineBtn = (Button)findViewById(R.id.fever_timeline_btn);

        mLinearTabGraph = (LinearLayout)findViewById(R.id.linear_tab_graph);
        mLinearTabTimeline = (LinearLayout)findViewById(R.id.linear_tab_timeline);
        mRecyTimeline = (RecyclerView)findViewById(R.id.recy_timeline);

        mHistoryGraph = (CombinedChart)findViewById(R.id.history_graph);

        mBtnHelpClose = (ImageButton)findViewById(R.id.btn_help_close);
        mBtnCheckHelp = (ImageButton)findViewById(R.id.btn_check_help);

        view = findViewById(R.id.root_view);
    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){
        mTxtHistoryDate.setOnClickListener(this);
        mFeverGraphBtn.setOnClickListener(this);
        mFeverTimelineBtn.setOnClickListener(this);
        mTxtGraphStartDate.setOnClickListener(this);
        mTxtGraphEndDate.setOnClickListener(this);
        mBtnHelpClose.setOnClickListener(this);
        mBtnCheckHelp.setOnClickListener(this);


        //click 저장
        OnClickListener mClickListener = new OnClickListener(this,view, FeverHxActivity.this);

        //아이 체온
        mFeverGraphBtn.setOnTouchListener(mClickListener);
        mFeverTimelineBtn.setOnTouchListener(mClickListener);

        //코드 부여(아이 체온)
        mFeverGraphBtn.setContentDescription(getString(R.string.FeverGraphBtn));
        mFeverTimelineBtn.setContentDescription(getString(R.string.FeverTimelineBtn));
    }

    public void initView(){
        arrFilter = getResources().getStringArray(R.array.filter_list);
        bSelFilter = new boolean[arrFilter.length];

        format = new SimpleDateFormat(CommonData.PATTERN_DATETIME_S);

        // 나갔다 들어왔을때, 처음 들어왔을때 체크 박스 초기화
        for (int i = 0; i < bSelFilter.length; i++){
            bSelFilter[i] = true;
        }

        mArrFeverList = new ArrayList<FeverItem>();
        mAllDataItems = new ArrayList<AllDataItem>();
        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE_KR);
        mCalendar = new GregorianCalendar();
        mCurDate = new Date();
        mStartDate = new Date();
        mEndDate = new Date();
        mStrStartDate = format.format(mStartDate);
        mStrEndDate = format.format(mEndDate);
        mTxtGraphStartDate.setText(mStrStartDate);
        mTxtGraphEndDate.setText(mStrEndDate);
        mTxtHistoryDate.setText(R.string.filter_all);
        //setDate();
    }

    public void initTimeLine(){
        mAdapter = new FeverHistoryListAdapter(FeverHxActivity.this, mAllDataItems, R.layout.fever_hx_list_item);

        LinearLayoutManager layoutManager = new LinearLayoutManager(FeverHxActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyTimeline.setLayoutManager(layoutManager);
        mRecyTimeline.setItemAnimator(new DefaultItemAnimator());
        mRecyTimeline.setHasFixedSize(true);
//        ((FeverHistoryListAdapter) mAdapter).setMode(Attributes.Mode.Single);
        mRecyTimeline.setAdapter(mAdapter);
    }

    public void setTab(int _tabNum){
        tabNum = _tabNum;
//        if(_tabNum == 0){         // 그래프 보기
//            mGraphDateLay.setVisibility(View.VISIBLE);
//            mTxtHistoryDate.setVisibility(View.INVISIBLE);
//            mFeverGraphBtn.setTextColor(getResources().getColor(R.color.h_orange));
//            mFeverTimelineBtn.setTextColor(Color.WHITE);
//            mFeverGraphBtn.setBackgroundResource(R.drawable.underline_fever);
//            mFeverTimelineBtn.setBackgroundColor(getResources().getColor(R.color.bg_yellow_light));
//            requestFeverRecordApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());
//        }else{
//            if(!CommonData.getInstance(this).getFeverTimeLineHelp()){
//                mHelpLay.setVisibility(View.VISIBLE);
//            }else{
//                mHelpLay.setVisibility(View.GONE);
//            }
//
//            mGraphDateLay.setVisibility(View.INVISIBLE);
//            mTxtHistoryDate.setVisibility(View.VISIBLE);
//            mFeverTimelineBtn.setTextColor(getResources().getColor(R.color.h_orange));
//            mFeverGraphBtn.setTextColor(Color.WHITE);
//            mFeverTimelineBtn.setBackgroundResource(R.drawable.underline_fever);
//            mFeverGraphBtn.setBackgroundColor(getResources().getColor(R.color.bg_yellow_light));
//            requestAllListApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), true,true,true,true,true,false,true);
//        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        int id = v.getId();
        if (id == R.id.fever_graph_btn) { // 그래프 보기
            setTab(0);
        } else if (id == R.id.fever_timeline_btn) {
            setTab(1);
//        } else if (id == R.id.txt_history_date) {
//            bTumpSelFilter = new boolean[bSelFilter.length];
//            for (int i = 0; i < bSelFilter.length; i++) {
//                if (bSelFilter[i])
//                    bTumpSelFilter[i] = true;
//                else
//                    bTumpSelFilter[i] = false;
//            }
//
//            AlertDialog.Builder ab = new AlertDialog.Builder(FeverHxActivity.this);
//            ab.setMultiChoiceItems(arrFilter, bSelFilter, (dialog, which, isChecked) -> {
//
//            });
//
//            ab.setPositiveButton(R.string.popup_dialog_button_confirm, (dialog, which) -> {
//                String str = "";
//                int cnt = 0;
//                for (int i = 0; i < bSelFilter.length; i++) {
//                    if (bSelFilter[i]) {
//                        cnt++;
//                    }
//                }
//
//                // 전체 선택
//
//                if (cnt == 6) {
//                    mTxtHistoryDate.setText(R.string.filter_all);
//                    requestAllListApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), true, true, true, true, true, false, true);
//                } else if (cnt == 0) {
//                    mDialog = new CustomAlertDialog(FeverHxActivity.this, CustomAlertDialog.TYPE_A);
//                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
//                    mDialog.setContent(getString(R.string.no_filter));
//                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog1, button) -> dialog1.dismiss());
//                    mDialog.show();
//                    bSelFilter = bTumpSelFilter;
//                    return;
//                } else {
//                    requestAllListApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn(), bSelFilter[0], bSelFilter[1], bSelFilter[2], bSelFilter[3], bSelFilter[4], false, bSelFilter[5]);
//                    for (int i = 0; i < bSelFilter.length; i++) {
//                        if (bSelFilter[i])
//                            str += arrFilter[i] + ", ";
//                    }
//
//                    if (str.length() > 0) {
//                        str = str.substring(0, str.length() - 2);
//                        mTxtHistoryDate.setText(str);
//                    }
//                }
//            });
//
//            ab.setNegativeButton(R.string.popup_dialog_button_cancel, (dialog, which) -> dialog.dismiss());
//            ab.show();
        } else if (id == R.id.txt_graph_start_date) {
//            try {
//                if (mStartDate == null) {
//                    mStartDate = new Date();
//                }
//                mCalendar.setTime(mStartDate);
//                int nNowYear = mCalendar.get(Calendar.YEAR);
//                int nNowMonth = mCalendar.get(Calendar.MONTH);
//                int nNowDay = mCalendar.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog alert = new DatePickerDialog(FeverHxActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
//                    mCalendar.set(year, monthOfYear, dayOfMonth);
//
//                    if (mCalendar.getTime().compareTo(mEndDate) >= 0) {    // 오늘 지남
//                        Toast.makeText(FeverHxActivity.this, getString(R.string.over_date), Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                    mStartDate = mCalendar.getTime();
//                    SimpleDateFormat format1 = new SimpleDateFormat(CommonData.PATTERN_DATE_KR);
//                    mStrStartDate = format1.format(mStartDate);
//                    mTxtGraphStartDate.setText(mStrStartDate);
//
//                    // 그래프 데이터 가져와서 뿌려야 함.
//                    requestFeverRecordApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());
//                }, nNowYear, nNowMonth, nNowDay);
//
//                alert.setCancelable(false);
//
//                alert.show();
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//        } else if (id == R.id.txt_graph_end_date) {
//            try {
//                if (mEndDate == null) {
//                    mEndDate = new Date();
//                }
//                mCalendar.setTime(mEndDate);
//                int nNowYear = mCalendar.get(Calendar.YEAR);
//                int nNowMonth = mCalendar.get(Calendar.MONTH);
//                int nNowDay = mCalendar.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog alert = new DatePickerDialog(FeverHxActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
//                    mCalendar.set(year, monthOfYear, dayOfMonth);
//
//                    if (mStartDate.compareTo(mCalendar.getTime()) >= 0) {  // 오늘 지남
//                        Toast.makeText(FeverHxActivity.this, getString(R.string.over_date), Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                    mEndDate = mCalendar.getTime();
//                    SimpleDateFormat format12 = new SimpleDateFormat(CommonData.PATTERN_DATE_KR);
//                    mStrStartDate = format12.format(mEndDate);
//                    mTxtGraphEndDate.setText(mStrEndDate);
//
//                    // 그래프 데이터 가져와서 뿌려야 함.
//                    requestFeverRecordApi(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn());
//                }, nNowYear, nNowMonth, nNowDay);
//
//                alert.setCancelable(false);
//
//                alert.show();
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
        } else if (id == R.id.btn_check_help) {//                if(mNoShowHelp){    // 참 -  즉 체크 돼있는 상태에서 누를 경우
//                    mBtnCheckHelp.setImageResource(R.drawable.btn_close_yellow1);
//                    mNoShowHelp = false;
//                }else{
//                    mBtnCheckHelp.setImageResource(R.drawable.btn_close_yellow2);
//                    mNoShowHelp = true;
//                }
            CommonData.getInstance(this).setFeverTimeLineHelp(true);
            mHelpLay.setVisibility(View.GONE);
        } else if (id == R.id.btn_help_close) {//                CommonData.getInstance(this).setFeverTimeLineHelp(mNoShowHelp);
            mHelpLay.setVisibility(View.GONE);
        }
    }


    public void setDate(){
        cntDay = Util.sumDayCount(mStartDate, mEndDate);

        if( mStrStartDate.equals(mStrEndDate) ){
            arrXDate = new String[1440];
            for(int i = 0; i < arrXDate.length; i++){
                int h = i / 60;
                int m = i % 60;
                arrXDate[i] = String.format("%02d:%02d", h, m);
            }
        }else{
            arrXDate = new String[cntDay*24];
            arrDateSet = new String[cntDay];

            SimpleDateFormat dateFormat = new SimpleDateFormat(CommonData.PATTERN_DATE_2);

            mCalendar.setTime(mStartDate);
            arrDateSet[0] = dateFormat.format(mCalendar.getTime());
            for(int i = 1; i < cntDay; i++){
                mCalendar.add(Calendar.DATE, 1);
                arrDateSet[i] = dateFormat.format(mCalendar.getTime());
            }

            for(int i = 0; i < arrXDate.length; i++){
                int day = i / 24;
                int hour = i % 24;
                arrXDate[i] = arrDateSet[day]+"\n"+hour+getString(R.string.hour_2);
            }
        }
    }

    public void initGraph(){
        Description desc = new Description();
        mHistoryGraph.setDescription(desc);
        mHistoryGraph.setBackgroundColor(Color.TRANSPARENT);
        mHistoryGraph.setDrawGridBackground(false);
        mHistoryGraph.setDrawBarShadow(false);

        /*// draw bars behind lines
        mHistoryGraph.setDrawOrder(new CombinedChart.DrawOrder[] {
                CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });*/

        // enable touch gestures
        mHistoryGraph.setTouchEnabled(true);
        // enable scaling and dragging
        mHistoryGraph.setScaleEnabled(true);
        mHistoryGraph.setDragEnabled(true);
        mHistoryGraph.setScaleYEnabled(false);

        mHistoryGraph.setDragDecelerationFrictionCoef(0.2f);

        // enable scaling and dragging
        mHistoryGraph.setHighlightPerDragEnabled(false);

        mHistoryGraph.setPinchZoom(false);

        Legend l = mHistoryGraph.getLegend();
        l.setTextColor(Color.GRAY);
        l.setForm(Legend.LegendForm.CIRCLE);
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);  // XXX

        YAxis rightAxis = mHistoryGraph.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = mHistoryGraph.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.GRAY);
        leftAxis.setAxisLineColor(Color.GRAY);
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setAxisMaxValue(42f);
        leftAxis.setAxisMinValue(34f); // this replaces setStartAtZero(true)
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true);

        XAxis xAxis = mHistoryGraph.getXAxis();
        xAxis.setAxisLineColor(Color.GRAY);
        xAxis.setGridColor(Color.GRAY);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public void updateGraph(){

        setDate();

//        CombinedData data = new CombinedData(arrXDate);
        CombinedData data = new CombinedData();
        data.setData(ScatterData());

        mHistoryGraph.setData(data);
        mHistoryGraph.invalidate();
    }

    private ScatterData ScatterData() {

        ScatterData d = new ScatterData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        SimpleDateFormat dateFormat = new SimpleDateFormat(CommonData.PATTERN_DATE_2);

        for(int i = 0; i < mArrFeverList.size(); i++){
            try {
                if( mStrStartDate.equals(mStrEndDate) ){
                    entries.add(new Entry(Float.parseFloat(mArrFeverList.get(i).getmInputFever()), (format.parse(mArrFeverList.get(i).getmInputDe()).getHours() * 60) + format.parse(mArrFeverList.get(i).getmInputDe()).getMinutes()));
                }else{
                    for(int x = 0; x < arrDateSet.length; x++){
                        if( arrDateSet[x].equals( dateFormat.format(format.parse(mArrFeverList.get(i).getmInputDe()))))
                            entries.add(new Entry(Float.parseFloat(mArrFeverList.get(i).getmInputFever()), ((x*24) + format.parse(mArrFeverList.get(i).getmInputDe()).getHours() )));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ScatterDataSet set = new ScatterDataSet(entries, getString(R.string.baby_temp));

        set.setColor(getResources().getColor(R.color.h_orange));
        set.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set.setScatterShapeSize(7f);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }

    /**
     * 전체 데이터 가져오기
     */
    public void requestAllListApi(String chl_sn, boolean filter_1, boolean filter_2, boolean filter_3, boolean filter_4, boolean filter_5, boolean filter_6, boolean filter_7) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HA001);    //  api 코드명
            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_FILTER_1_F, String.valueOf(filter_1 ? 0:1));           //  체온 여부
            object.put(CommonData.JSON_FILTER_2_F, String.valueOf(filter_2 ? 0:1));           //  해열제 여부
            object.put(CommonData.JSON_FILTER_3_F, String.valueOf(filter_3 ? 0:1));           //  결과 레포트 여부
            object.put(CommonData.JSON_FILTER_4_F, String.valueOf(filter_4 ? 0:1));           //  증상 여부
            object.put(CommonData.JSON_FILTER_5_F, String.valueOf(filter_5 ? 0:1));           //  진단 여부
            object.put(CommonData.JSON_FILTER_6_F, String.valueOf(filter_6 ? 0:1));           //  예방접종 여부
            object.put(CommonData.JSON_FILTER_7_F, String.valueOf(filter_7 ? 0:1));           //  메모 여부

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(FeverHxActivity.this, NetworkConst.NET_ALL_LIST, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    /**
     * 체온 리스트 가져오기
     */
    public void requestFeverRecordApi(String chl_sn) {
//        GLog.i("requestAppInfo");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // {   "api_code": "chldrn_growth_list",   "insures_code": "106", "mber_sn": "10035"  ,"chl_sn": "1000" ,"pageNumber": "1" , "growth_typ": "1"}
        try {
            SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATE);

            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HF003);    //  api 코드명
            object.put(CommonData.JSON_CHL_SN_F, chl_sn);               //  자녀키값
            object.put(CommonData.JSON_START_DE_F, format.format(mStartDate));
            object.put(CommonData.JSON_END_DE_F, format.format(mEndDate));

            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));

            RequestApi.requestApi(this, NetworkConst.NET_FEVER_LIST, NetworkConst.getInstance().getFeverDomain(), networkListener, params, getProgressLayout());
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }


    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub

            switch ( type ) {
                case NetworkConst.NET_ALL_LIST:        // 모든 정보 리스트 가져오기

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                mAllDataItems.clear();
                                if (data_yn.equals(CommonData.YES)) {

                                    JSONArray allArr = resultData.getJSONArray(CommonData.JSON_DATA_F);

                                    String tumpDate = allArr.getJSONObject(0).getString(CommonData.JSON_INPUT_DE_F).substring(0, 10);

                                    AllDataItem firstDate = new AllDataItem();
                                    firstDate.setmIsDate(true);
                                    firstDate.setmInputDe(tumpDate);
                                    mAllDataItems.add(firstDate);

                                    for(int i = 0; i < allArr.length(); i++){

                                        if(tumpDate.equals(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_DE_F).substring(0, 10))){
                                            AllDataItem item = new AllDataItem();
                                            item.setmDataSn(allArr.getJSONObject(i).getString(CommonData.JSON_DATA_SN_F));
                                            item.setmFilter(allArr.getJSONObject(i).getString(CommonData.JSON_FILTER_F));
                                            item.setmInputDe(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_DE_F));

                                            item.setmFever(allArr.getJSONObject(i).getString(CommonData.JSON_FEVER_F));

                                            item.setmInputKind(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_KIND_F));
                                            item.setmInputType(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_TYPE_F));
                                            item.setmInputVolume(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_VOLUME_F));

                                            item.setmInputCode(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_CODE_F));

                                            item.setmInputNum(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_NUM_F));
                                            item.setmInputMemo(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_MEMO_F));

                                            mAllDataItems.add(item);
                                        }else{
                                            AllDataItem dateLay = new AllDataItem();
                                            dateLay.setmIsDate(true);
                                            dateLay.setmInputDe(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_DE_F).substring(0, 10));
                                            mAllDataItems.add(dateLay);
                                            tumpDate = allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_DE_F).substring(0, 10);

                                            AllDataItem item = new AllDataItem();
                                            item.setmDataSn(allArr.getJSONObject(i).getString(CommonData.JSON_DATA_SN_F));
                                            item.setmFilter(allArr.getJSONObject(i).getString(CommonData.JSON_FILTER_F));
                                            item.setmInputDe(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_DE_F));

                                            item.setmFever(allArr.getJSONObject(i).getString(CommonData.JSON_FEVER_F));

                                            item.setmInputKind(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_KIND_F));
                                            item.setmInputType(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_TYPE_F));
                                            item.setmInputVolume(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_VOLUME_F));

                                            item.setmInputCode(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_CODE_F));

                                            item.setmInputNum(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_NUM_F));
                                            item.setmInputMemo(allArr.getJSONObject(i).getString(CommonData.JSON_INPUT_MEMO_F));

                                            mAllDataItems.add(item);
                                        }

                                    }
                                }

                                mAdapter.notifyDataSetChanged();

                                mLinearTabGraph.setVisibility(View.GONE);
                                mLinearTabTimeline.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                GLog.e(e.toString());
                            }

                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");

                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            break;
                    }
                    break;

                case NetworkConst.NET_FEVER_LIST:
                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                mArrFeverList.clear();
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                if (data_yn.equals(CommonData.YES)) {
                                    JSONArray feverArr = resultData.getJSONArray(CommonData.JSON_DATA_F);
                                    // 데이터가 있을 시
                                    if (feverArr.length() > 0) {
                                        for(int i = 0; i < feverArr.length(); i++){
                                            JSONObject object = feverArr.getJSONObject(i);

                                            FeverItem item = new FeverItem();
                                            item.setmFeverSn(object.getString(CommonData.JSON_FEVER_SN_F));
                                            item.setmInputDe(object.getString(CommonData.JSON_INPUT_DE_F));
                                            item.setmInputFever(object.getString(CommonData.JSON_INPUT_FEVER_F));

                                            mArrFeverList.add(item);
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                GLog.e(e.toString());
                                mArrFeverList.clear();
                            }

                            break;
                        case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");
                            mArrFeverList.clear();
                            break;
                        case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
                            GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
                            mArrFeverList.clear();
                            break;

                        default:
                            GLog.i("NET_GET_APP_INFO default", "dd");
                            mArrFeverList.clear();
                            break;
                    }

                    updateGraph();

                    mLinearTabGraph.setVisibility(View.VISIBLE);
                    mLinearTabTimeline.setVisibility(View.GONE);

                    break;
            }
            hideProgress();
        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            hideProgress();
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            hideProgress();
            dialog.show();

        }
    };

}
