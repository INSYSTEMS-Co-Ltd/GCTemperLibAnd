package com.greencross.gctemperlib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.hana.HealthCareServiceFragment;
import com.greencross.gctemperlib.hana.HealthRservationFragment;
import com.greencross.gctemperlib.hana.SettingAddressFragment;
import com.greencross.gctemperlib.hana.TemperControlFragment;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.push.FirebaseMessagingService;
import com.greencross.gctemperlib.slideUtil.SlidingUpPanelLayout;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.GpsInfo;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.collection.EpidemicItem;
import com.greencross.gctemperlib.collection.LocationItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.MakeProgress;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.greencare.util.JsonLogPrint;
import com.greencross.gctemperlib.network.RequestApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class TemperActivity extends BackBaseActivity implements View.OnClickListener, OnMapReadyCallback {

    private final int REQUEST_SEARCH_ADDR = 787;

    private LinearLayout mLinearTabMap;

    private ImageButton mBtnAlarm;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

    private View mMarkerRootView;
    private TextView marker;
    private Intent mIntent;
    private int mFragmentNum = 0;

    private View view;

    private SlidingUpPanelLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GCTemperLib gcHeatLib = new GCTemperLib(this);
        if (gcHeatLib.isAvailableGCToken() == false) {
            CDialog.showDlg(this, "인증 후 이용 가능합니다.")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            finish();
                        }
                    });
            return;
        }

        setContentView(R.layout.temper_map_activity);

        setTitle(getString(R.string.temper_control));

        init();
        setEvent();
//        initRank();

        GregorianCalendar mCalendar = new GregorianCalendar();
        mCalendar.setTime(new Date());
        mCalendar.add(Calendar.DAY_OF_MONTH, -365);
        requestMapData(mCalendar.getTime(), new Date());

        mIntent = getIntent();

        if (mIntent != null) {
            mFragmentNum = mIntent.getIntExtra("tabNum", 0);
        }

        setSlideLayout();   // 하단 슬라이드 메뉴


        // 헬스케어란
        findViewById(R.id.map_mylocation_btn).setOnClickListener(view ->
                moveMyLocation()
        );

        // 열지도 알림
        findViewById(R.id.map_alram_btn).setOnClickListener(view ->
                DummyActivity.startActivity(TemperActivity.this, SettingAddressFragment.class, null)
        );
    }

    /**
     * 하단 슬라이드 메뉴 세팅
     */
    private void setSlideLayout() {
        // 체온관리
        findViewById(R.id.fever_map_menu_1).setOnClickListener(view ->
                DummyActivity.startActivity(TemperActivity.this, TemperControlFragment.class, null)
        );
        // 건강강검진예약
        findViewById(R.id.fever_map_menu_2).setOnClickListener(view ->
                DummyActivity.startActivity(TemperActivity.this, HealthRservationFragment.class, null)
        );

        // 건강상담
        findViewById(R.id.fever_map_menu_3).setOnClickListener(view -> {

            // TODO : DB 전송완료시 & DB전송이 완료되지 않은 경우 처리 해야 함

            CDialog.showDlg(TemperActivity.this, getString(R.string.fever_health_no_alert_title)).setTitle(R.string.fever_health_no_alert_message);

            CDialog dlg = CDialog.showDlg(TemperActivity.this, getString(R.string.fever_health_call_alert_message));
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


        });

        // 헬스케어란
        findViewById(R.id.fever_map_menu_4).setOnClickListener(view ->
                DummyActivity.startActivity(TemperActivity.this, HealthCareServiceFragment.class, null)
        );


        final View slideFullUpLayout = findViewById(R.id.slide_top_full_up_layout);
        final View slideFullDownLayout = findViewById(R.id.slide_top_full_down_layout);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                switch (newState) {
                    case COLLAPSED: // 완전 내려간 경우
                        slideFullUpLayout.setVisibility(View.GONE);
                        slideFullDownLayout.setVisibility(View.VISIBLE);
                        break;
                    case EXPANDED:  // 완전 펼쳐진 경우
                        slideFullUpLayout.setVisibility(View.VISIBLE);
                        slideFullDownLayout.setVisibility(View.GONE);
                        break;
                    case DRAGGING:

                        break;
                }
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }


    /**
     * 초기화
     */
    public void init() {
        mBtnAlarm = (ImageButton) findViewById(R.id.btn_alarm);
        mLinearTabMap = (LinearLayout) findViewById(R.id.linear_tab_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        view = findViewById(R.id.root_view);
    }

    /**
     * 이벤트 연결
     */
    public void setEvent() {
        mBtnAlarm.setOnClickListener(this);

        //hsh start
//        textView9.setOnClickListener(this);
        //hsh end


        //click 저장
        OnClickListener mClickListener = new OnClickListener(this, view, TemperActivity.this);

        //열지도
        mBtnAlarm.setOnTouchListener(mClickListener);

        mBtnAlarm.setContentDescription(getString(R.string.BtnAlarm));

    }

    public static ArrayList<EpidemicItem> mEpidemicList = new ArrayList<EpidemicItem>();        // 유행질병 카운트

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_alarm) {
//            intent = new Intent(TemperActivity.this, SettingAddressActivity.class);
//            startActivity(intent);
            DummyActivity.startActivityForResult(TemperActivity.this, REQUEST_SEARCH_ADDR, SettingAddressFragment.class, null);
        }
    }


    private Marker addMarker(LocationItem feverMapItem) {
        double fever = Double.parseDouble(feverMapItem.getAvg_fever());
        LatLng position = new LatLng(Double.parseDouble(feverMapItem.getLoc_1()), Double.parseDouble(feverMapItem.getLoc_2()));

        marker.setText(feverMapItem.getLoc_nm_2() + "\n" + "평균:" + feverMapItem.getAvg_fever());

        if (fever < 35.5d) {
            marker.setBackgroundResource(R.drawable.bg_marker_lv_1);
        } else if (fever < 37.5d) {
            marker.setBackgroundResource(R.drawable.bg_marker_lv_2);
        } else if (fever < 38d) {
            marker.setBackgroundResource(R.drawable.bg_marker_lv_3);
        } else if (fever < 39d) {
            marker.setBackgroundResource(R.drawable.bg_marker_lv_4);
        } else {
            marker.setBackgroundResource(R.drawable.bg_marker_lv_5);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("" + fever);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, mMarkerRootView)));

        return mMap.addMarker(markerOptions);
    }

    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    /**
     * 열지도 데이터 가져오기
     */
    public void requestMapData(Date startDate, Date ednDate) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE_F, CommonData.JSON_APINM_HJ002);
            params.add(new BasicNameValuePair(CommonData.JSON_STRJSON, object.toString()));
            RequestApi.requestApi(this, NetworkConst.NET_GET_MAP_DATA, NetworkConst.getInstance().getFeverDomain(), new CustomAsyncListener() {
                @Override
                public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
                    hideProgress();
                    dialog.show();
                }

                @Override
                public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
                    hideProgress();
                    dialog.show();
                }

                @Override
                public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                if (data_yn.equals(CommonData.YES)) {
                                    JSONArray mapArr = resultData.getJSONArray(CommonData.JSON_DATA_F);
                                    // 데이터가 있을 시
                                    if (mapArr.length() > 0) {
                                        for (int i = 0; i < mapArr.length(); i++) {
                                            try {
                                                JSONObject object = mapArr.getJSONObject(i);

                                                LocationItem item = new LocationItem();
                                                item.setLoc_nm_1(object.getString(CommonData.JSON_LOC_NM_1));
                                                item.setLoc_nm_2(object.getString(CommonData.JSON_LOC_NM_2));
                                                item.setAvg_fever(object.getString(CommonData.JSON_AVG_FEVER));
                                                item.setLoc_1(object.getString(CommonData.JSON_LOC_1));
                                                item.setLoc_2(object.getString(CommonData.JSON_LOC_2));

                                                if (Double.parseDouble(item.getAvg_fever()) > 0)
                                                    addMarker(item);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //hsh start
//                                        setTabShow();
                                        //hsh end
                                    }
                                }

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
                }
            }, params, new MakeProgress(this));
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    private Intent inTentGo = null;
    int typePush = FirebaseMessagingService.FEVER;
    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            switch (type) {

                case NetworkConst.NET_GET_EPIDEMIC:             // 유행 질병 데이터 가져오기
                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            try {
                                JsonLogPrint.printJson(resultData.toString());
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN_F);

                                if (data_yn.equals(CommonData.YES)) {
                                    JSONArray resultArr = resultData.getJSONArray(CommonData.JSON_DATA_F);
                                    ArrayList<EpidemicItem> epidemicItems = new ArrayList<>();

                                    for (int i = 0; i < resultArr.length(); i++) {
                                        JSONObject resultObject = resultArr.getJSONObject(i);

                                        EpidemicItem curItem = new EpidemicItem();
                                        curItem.setDzNum(resultObject.getInt(CommonData.JSON_DZNUM));
                                        curItem.setDzName(resultObject.getString(CommonData.JSON_DZNAME));
                                        curItem.setWeekago_1(resultObject.getInt(CommonData.JSON_WEEKAGO_1));
                                        curItem.setWeekago_2(resultObject.getInt(CommonData.JSON_WEEKAGO_2));
                                        epidemicItems.add(curItem);
                                    }

                                    epidemicItems.remove(epidemicItems.get(epidemicItems.size() - 2));
                                    epidemicItems.remove(epidemicItems.get(epidemicItems.size() - 2));

                                    for (int i = 0; i < epidemicItems.size(); i++) {
                                        epidemicItems.get(i).setRatio(getRatio(epidemicItems.get(i).getWeekago_1(), epidemicItems.get(epidemicItems.size() - 1).getWeekago_1(), 100));
                                    }

                                    epidemicItems.remove(epidemicItems.get(epidemicItems.size() - 1));

                                    Collections.sort(epidemicItems, (a, b) -> a.getRatio() > b.getRatio() ? -1 : a.getRatio() < b.getRatio() ? 1 : 0);

                                    //Collections.sort(epidemicItems, comRatio);
                                    //Collections.reverse(epidemicItems);

                                    mEpidemicList = epidemicItems;


                                    //mTxtEpidemic.setText(getString(R.string.epidemic_main) + " " + mEpidemicList.get(0).getDzName());
                                    //hsh start
                                    if (inTentGo != null && (typePush == FirebaseMessagingService.FEVER
                                            || typePush == FirebaseMessagingService.FEVER_MOVIE
                                            || typePush == FirebaseMessagingService.DIESEASE)) {
                                        startActivity(inTentGo);
                                        Util.BackAnimationStart(TemperActivity.this);
                                        inTentGo = null;
                                    }
                                    //hsh end
                                } else {
                                    //mTxtEpidemic.setText(getString(R.string.epidemic_main) + " " + getString(R.string.empty_epidemic_main));
                                }
                            } catch (Exception e) {
                                GLog.e(e.toString());
                                //mTxtEpidemic.setText(getString(R.string.epidemic_main) + " " + getString(R.string.empty_epidemic_main));
                            }
                    }
                    break;

                case NetworkConst.NET_MBER_CHECK_AGREE_YN:

                    switch (resultCode) {
                        case CommonData.API_SUCCESS:
                            GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");

                            try {
                                String data_yn = resultData.getString(CommonData.JSON_REG_YN);
                                if (data_yn.equals(CommonData.YES)) {
                                    CommonData.getInstance(TemperActivity.this).setMarketing_yn(resultData.getString(CommonData.JSON_MARKETING_YN));
                                    CommonData.getInstance(TemperActivity.this).setLocation_yn(resultData.getString(CommonData.JSON_LOCATION_YN));
                                    CommonData.getInstance(TemperActivity.this).setEvent_alert_yn(resultData.getString(CommonData.JSON_EVENT_ALERT_YN).equals(CommonData.YES) ? true : false);
                                    CommonData.getInstance(TemperActivity.this).setMapPushAlarm(resultData.getString(CommonData.JSON_HEAT_YN).equals(CommonData.YES) ? true : false);
                                    CommonData.getInstance(TemperActivity.this).setDietPushAlarm(resultData.getString(CommonData.JSON_DIET_YN).equals(CommonData.YES) ? true : false);
                                    CommonData.getInstance(TemperActivity.this).setDisease_alert_yn(resultData.getString(CommonData.JSON_DISEASE_ALERT_YN).equals(CommonData.YES) ? true : false);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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

    public double getRatio(int num_1, int num_2, int num_3) {

        BigDecimal bd_1 = null;
        BigDecimal bd_2 = null;
        BigDecimal bd_3 = new BigDecimal("" + num_3);

        if (num_1 > 0) {
            bd_1 = new BigDecimal("" + num_1);
            bd_2 = new BigDecimal("" + num_2);

            BigDecimal ratio = bd_1.divide(bd_2, 3, BigDecimal.ROUND_HALF_UP).multiply(bd_3);

            return ratio.doubleValue();
        } else {
            return 0d;
        }
    }

    //hsh start
//    private void setTabShow() {
//        Intent i = getIntent();
//        int push_type = i.getIntExtra(CommonData.EXTRA_PUSH_TYPE, 0);
//        int type = i.getIntExtra(CommonData.EXTRA_MAIN_TYPE, 0);
////        if ((push_type != 0 && push_type == FirebaseMessagingService.DIESEASE) || type != 0) {
////            setTab(1);
////        } else {
////            setTab(0);
////        }
//        getIntent().removeExtra(CommonData.EXTRA_PUSH_TYPE);
//        getIntent().removeExtra(CommonData.EXTRA_INFO_SN);
//    }
    //hsh end

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {

            }
        });
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(true);
//        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
//        mMap.getUiSettings().setMapToolbarEnabled(true);

        moveMyLocation();
        setCustomMarkerView();
    }

    /**
     * 내위치로 이동
     */
    private void moveMyLocation() {
        GpsInfo gps = new GpsInfo(this);
        if (gps.isGetLocation()) {
            LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

//            marker.setText(feverMapItem.getLoc_nm_2() + "\n" + "평균:" + feverMapItem.getAvg_fever());
//            if (fever < 35.5d) {
//                marker.setBackgroundResource(R.drawable.bg_marker_lv_1);
//            } else if (fever < 37.5d) {
//                marker.setBackgroundResource(R.drawable.bg_marker_lv_2);
//            } else if (fever < 38d) {
//                marker.setBackgroundResource(R.drawable.bg_marker_lv_3);
//            } else if (fever < 39d) {
//                marker.setBackgroundResource(R.drawable.bg_marker_lv_4);
//            } else {
//                marker.setBackgroundResource(R.drawable.bg_marker_lv_5);
//            }

//            MarkerOptions markerOptions = new MarkerOptions();
////            markerOptions.title("" + fever);
//            markerOptions.position(latLng);
//            Drawable drawable = getResources().getDrawable(R.drawable.draw_circle_707070);
//            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//
//            mMap.addMarker(markerOptions);
        }
    }

    private void setCustomMarkerView() {
        mMarkerRootView = LayoutInflater.from(this).inflate(R.layout.custom_marker, null);
        marker = (TextView) mMarkerRootView.findViewById(R.id.marker);
    }


    /**
     * 마케팅 정보 및 위치정보 동의
     */
    public void requestAgreeAlarmSetting(String YN) {
        GLog.i("requestAgreeAlarmSetting", "dd");
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject object = new JSONObject();
            object.put(CommonData.JSON_API_CODE, CommonData.METHOD_MBER_CHECK_AGREE_YN);
            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
            object.put(CommonData.JSON_MBER_SN, CommonData.getInstance(TemperActivity.this).getMberSn());

            object.put(CommonData.JSON_MARKETING_YN, "");
            object.put(CommonData.JSON_LOCATION_YN, YN);

            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

            RequestApi.requestApi(TemperActivity.this, NetworkConst.NET_MBER_CHECK_AGREE_YN, NetworkConst.getInstance().getDefDomain(), networkListener, params, new MakeProgress(this));
        } catch (Exception e) {
            GLog.i(e.toString(), "dd");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapFragment != null)
            mapFragment.onResume();

        if (CommonData.getInstance(TemperActivity.this).getLocation_yn().equals("N") || CommonData.getInstance(TemperActivity.this).getLocation_yn().equals("")) {
//            CustomAlertDialog mDialog2 = new CustomAlertDialog(FeverMapActivity.this, CustomAlertDialog.TYPE_F);
//            View view = LayoutInflater.from(FeverMapActivity.this).inflate(R.layout.popup_dialog_certiview, null);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            params.gravity = Gravity.CENTER;
//
//            TextView tv1 = view.findViewById(R.id.check_box1);
//            tv1.setText("위치정보 수집 및 이용 동의 (필수)");
//
//            TextView tv2 = view.findViewById(R.id.dialog_title);
//            TextView tv3 = view.findViewById(R.id.dialog_content);
//
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv2.getLayoutParams();
//            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            tv2.setLayoutParams(lp);
//
//            RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) tv3.getLayoutParams();
//            lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            tv3.setLayoutParams(lp2);
//            tv3.setGravity(Gravity.LEFT);
//
//
//            Button b1 = view.findViewById(R.id.confirm_btn);
//            b1.setBackground(getResources().getDrawable(R.drawable.btn_5_6bb0d7_right_bottom_round));
//
//            mDialog2.setContentView(view, params);
//            mDialog2.setTitle("위치정보 사용 동의");
//            mDialog2.setContent("[열지도]에서 현재 위치 정보를\n사용하고자 합니다. 동의하시겠습니까?\n동의하지 않을 경우 서비스 이용이 제한될 수 있습니다.");
//            mDialog2.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
//                requestAgreeAlarmSetting(dialog.isMotherChecked() ? CommonData.YES : CommonData.NO);
//                dialog.dismiss();
//            });
//            mDialog2.setNegativeButton(getString(R.string.popup_dialog_button_cancel), (dialog, button) -> {
//                finish();
//                dialog.dismiss();
//            });
//
//            mDialog2.setCheckboxButtonLv((dialog, button) -> {
//                dialog.setChangeCheckboxImg_motherBig();
//
//                if (dialog.isMotherChecked()) {
//                    b1.setEnabled(true);
//                } else {
//                    b1.setEnabled(false);
//                }
//            });
//            if (mDialog2.isMotherChecked()) {
//                b1.setEnabled(true);
//            } else {
//                b1.setEnabled(false);
//            }
//
//            mDialog2.setCancelable(false);
//            mDialog2.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapFragment != null)
            mapFragment.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mMap != null)
                mMap.clear();

            if (mapFragment != null)
                mapFragment.onDestroyView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    public void initRank() {
//        if (mEpidemicList.size() > 0) {
//            for (int i = 0; i < 10; i++) {
//                mTxtDiseNmList[i].setText(mEpidemicList.get(i).getDzName());
//                mTxtDiseCntList[i].setText("" + mEpidemicList.get(i).getRatio() + "%");
//            }
//        } else {
//            mEpidemicRankLay.setVisibility(View.GONE);
//            mTxtNullEpidemic.setVisibility(View.VISIBLE);
//        }
//    }

//    /**
//     * 유의질환 공유
//     */
//    public void requestSharedisease() {
//        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//
//        try {
//            JSONArray array = new JSONArray();
//            JSONObject object = new JSONObject();
//
//            object.put(CommonData.JSON_API_CODE, "asstb_mber_cntr_diss");
//            object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);          //  insures 코드
//            object.put(CommonData.JSON_CNTR_TYP, "15");
//            object.put(CommonData.JSON_MBER_SN, CommonData.getInstance(TemperActivity.this).getMberSn());             //  회원고유값
//            object.put("diss_view_de", CDateUtil.getToday_yyyy_MM_dd());
//
//
//            if (mEpidemicList.size() > 0 && mEpidemicList.size() > 9) {
//                object.put("data_length", "10");
//                for (int i = 0; i < 10; i++) {
//                    JSONObject epidObject = new JSONObject();
//                    epidObject.put("rank", String.valueOf(i + 1));
//                    epidObject.put("rank_nm", mEpidemicList.get(i).getDzName());
//                    epidObject.put("rank_per", "" + mEpidemicList.get(i).getRatio() + "%");
//
//                    array.put(epidObject);
//                }
//            } else if (mEpidemicList.size() > 0 && mEpidemicList.size() < 10) {
//                object.put("data_length", mEpidemicList.size());
//                for (int i = 0; i < mEpidemicList.size(); i++) {
//                    JSONObject epidObject = new JSONObject();
//                    epidObject.put("rank", String.valueOf(i + 1));
//                    epidObject.put("rank_nm", mEpidemicList.get(i).getDzName());
//                    epidObject.put("rank_per", "" + mEpidemicList.get(i).getRatio() + "%");
//
//                    array.put(epidObject);
//                }
//            } else {
//                object.put("data_length", "10");
//                for (int i = 0; i < 10; i++) {
//                    JSONObject epidObject = new JSONObject();
//                    epidObject.put("rank", String.valueOf(i + 1));
//                    epidObject.put("rank_nm", "");
//                    epidObject.put("rank_per", "0.0%");
//
//                    array.put(epidObject);
//                }
//            }
//
//
//            object.put("data", array);
//
//            params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));
//            RequestApi.requestApi(this, NetworkConst.NET_ASSTB_MBER_CNTR_DISS, NetworkConst.getInstance().getDefDomain(), new CustomAsyncListener() {
//                @Override
//                public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
//                    hideProgress();
//                    dialog.show();
//                }
//
//                @Override
//                public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
//                    hideProgress();
//                    dialog.show();
//                }
//
//                @Override
//                public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
//                    case CommonData.API_SUCCESS:
//                    GLog.i("NET_GET_APP_INFO API_SUCCESS", "dd");
//                    try {
//                        String data_yn = resultData.getString(CommonData.JSON_REG_YN);
//                        if (data_yn.equals(CommonData.YES)) {
//                            String imgUrl = "https://wkd.walkie.co.kr/HL_FV/info/image/01_15.png";
//                            String cntr_url = resultData.getString("cntr_url");
//
//                            if (cntr_url.contains("https://wkd.walkie.co.kr")) ;
//                            String param = cntr_url.replace("https://wkd.walkie.co.kr", "");
//
//                            View view = LayoutInflater.from(TemperActivity.this).inflate(R.layout.applink_dialog_layout, null);
//                            ApplinkDialog dlg = ApplinkDialog.showDlg(TemperActivity.this, view);
//                            dlg.setSharing(imgUrl, "img", "", "", "[현대해상 " + KakaoLinkUtil.getAppname(TemperActivity.this.getPackageName(), TemperActivity.this) + "]", "유의질환 발생 빈도", "자세히보기", "", false, "disease.png", param, cntr_url);
//
//                        } else {
//                        }
//
//                    } catch (Exception e) {
//                        GLog.e(e.toString());
//                    }
//
//                    break;
//                    case CommonData.API_ERROR_SYSTEM_ERROR:    // 시스템 오류
//                    GLog.i("NET_GET_APP_INFO API_ERROR_SYSTEM_ERROR", "dd");
//
//                    break;
//                    case CommonData.API_ERROR_INPUT_DATA_ERROR:    // 입력 데이터 오류
//                    GLog.i("NET_GET_APP_INFO API_ERROR_INPUT_DATA_ERROR", "dd");
//                    break;
//
//                    default:
//                    GLog.i("NET_GET_APP_INFO default", "dd");
//                    break;
//                }
//            }, params, new MakeProgress(this));
//        } catch (Exception e) {
//            GLog.i(e.toString(), "dd");
//        }
//    }

}
