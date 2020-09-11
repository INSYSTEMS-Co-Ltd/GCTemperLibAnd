package com.greencross.gctemperlib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.util.CDateUtil;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Login;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_MapList;
import com.greencross.gctemperlib.util.SharedPref;
import com.greencross.gctemperlib.hana.HealthCareServiceFragment;
import com.greencross.gctemperlib.hana.HealthRservationFragment;
import com.greencross.gctemperlib.hana.SettingAddressFragment;
import com.greencross.gctemperlib.hana.TemperControlFragment;
import com.greencross.gctemperlib.hana.component.CDialog;
import com.greencross.gctemperlib.hana.component.OnClickListener;
import com.greencross.gctemperlib.push.FirebaseMessagingService;
import com.greencross.gctemperlib.hana.slideUtil.SlidingUpPanelLayout;
import com.greencross.gctemperlib.util.GpsInfo;
import com.greencross.gctemperlib.collection.LocationItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class TemperActivity extends BackBaseActivity implements View.OnClickListener, OnMapReadyCallback {

    private final int REQUEST_SEARCH_ADDR = 787;

    private LinearLayout mLinearTabMap;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

    private View mMarkerRootView;
    private TextView marker;
    private Intent mIntent;
    private View mInfoLayout;

    private View view;

    private SlidingUpPanelLayout mLayout;

    private ImageButton mMyLocationBtn;

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

        setSlideLayout();   // 하단 슬라이드 메뉴

        // 헬스케어란

        mMyLocationBtn = findViewById(R.id.map_mylocation_btn);
        mMyLocationBtn.setOnClickListener((View.OnClickListener) view -> {
            requestPermissionLocation(new IGCResult() {
                @Override
                public void onResult(boolean isSuccess, String message, Object data) {
                    if (isSuccess) {
                        GpsInfo gps = new GpsInfo(TemperActivity.this);
                        if (gps.isGetLocation()) {
                            moveMyLocation();
                        } else {
                            gps.showSettingsAlert();
                        }
                    }
                }
            });
        });

        // 열지도 알림
        findViewById(R.id.map_alram_btn).setOnClickListener(view ->
                DummyActivity.startActivity(TemperActivity.this, SettingAddressFragment.class, null)
        );

        findViewById(R.id.slide_contents_view).setOnClickListener(null);
    }

    /**
     * 하단 슬라이드 메뉴 세팅
     */
    private void setSlideLayout() {
        // 체온관리
        findViewById(R.id.fever_map_menu_1).setOnClickListener(this);
        findViewById(R.id.fever_map_menu_1_iv).setOnClickListener(this);
        // 건강강검진예약
        findViewById(R.id.fever_map_menu_2).setOnClickListener(this);
        findViewById(R.id.fever_map_menu_2_iv).setOnClickListener(this);
        // 건강상담
        findViewById(R.id.fever_map_menu_3).setOnClickListener(this);
        findViewById(R.id.fever_map_menu_3_iv).setOnClickListener(this);
        // 헬스케어 서비스란
        findViewById(R.id.fever_map_menu_4).setOnClickListener(this);
        findViewById(R.id.fever_map_menu_4_iv).setOnClickListener(this);


        final View slideFullUpLayout = findViewById(R.id.slide_top_full_up_layout);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);


        final LinearLayout alphaLayoutUp = findViewById(R.id.slide_up_alpha);
        final LinearLayout alphaLayoutBottom = findViewById(R.id.slide_bottom_alpha);
        final FrameLayout slideBottomHandle = findViewById(R.id.slide_bottom_handle);
        alphaLayoutBottom.getBackground().setAlpha(0);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
//                Log.i(TAG, "onPanelSlide2, offset " +  slideOffset);
                float upAlpha = 1 - slideOffset;
                float bottomAlpha = slideOffset;

                Log.i(TAG, "upAlpha="+upAlpha);
                Log.i(TAG, "bottomAlpha="+bottomAlpha);

                alphaLayoutUp.setAlpha(upAlpha);
                alphaLayoutBottom.setAlpha(bottomAlpha);
                slideBottomHandle.setAlpha(upAlpha);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                switch (newState) {
                    case COLLAPSED: // 완전 내려간 경우
//                        slideFullUpLayout.setVisibility(View.GONE);
//                        slideFullDownLayout.setVisibility(View.VISIBLE);
                        break;
                    case EXPANDED:  // 완전 펼쳐진 경우
//                        slideFullUpLayout.setVisibility(View.VISIBLE);
//                        slideFullDownLayout.setVisibility(View.GONE);
                        getRemainUseDate();
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

    private float getHex(int i) {
//        i = (int) (Math.round(i * 100) / 100.0d);
        int rounded = (int) Math.round(i * 255);
        String hex = Integer.toHexString(rounded).toUpperCase();

//        if (hex.length() == 1)
//            hex = "0" + hex;
//        int percent = (int) (i * 100);
        return rounded;
    }


    /**
     * 초기화
     */
    public void init() {
        boolean isShowIntro = SharedPref.getInstance(this).getPreferences(SharedPref.TEMPER_INTRO_VIEW_SHOW, true);
        if (isShowIntro) {
            mInfoLayout = findViewById(R.id.temper_info_fragment);
            mInfoLayout.findViewById(R.id.temper_info_gone_btn).setOnClickListener(this);
            mInfoLayout.findViewById(R.id.temper_info_start_btn).setOnClickListener(this);
            mInfoLayout.setVisibility(View.VISIBLE);
        } else {
            if(null != SharedPref.getInstance(TemperActivity.this).getPreferences(SharedPref.PREF_TEMPERATE) &&
                    !"".equals(SharedPref.getInstance(TemperActivity.this).getPreferences(SharedPref.PREF_TEMPERATE))) {
                slideMenu1();
            }
        }

        mLinearTabMap = (LinearLayout) findViewById(R.id.linear_tab_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        view = findViewById(R.id.root_view);
    }

    /**
     * 이벤트 연결
     */
    public void setEvent() {
        //click 저장
        OnClickListener mClickListener = new OnClickListener(this, view, TemperActivity.this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.temper_info_gone_btn) {
            SharedPref.getInstance(this).savePreferences(SharedPref.TEMPER_INTRO_VIEW_SHOW, false);
            mInfoLayout.setVisibility(View.GONE);
            firstLocationPermission();
        } else if (id == R.id.temper_info_start_btn) {
            mInfoLayout.setVisibility(View.GONE);
            firstLocationPermission();
        } else if (id == R.id.fever_map_menu_1 || id == R.id.fever_map_menu_1_iv) { // 체온관리
            slideMenu1();
        } else if (id == R.id.fever_map_menu_2 || id == R.id.fever_map_menu_2_iv) {   // 건강강검진예약
            slideMenu2();
        } else if (id == R.id.fever_map_menu_3 || id == R.id.fever_map_menu_3_iv) { // 건강상담
            slideMenu3();
        } else if (id == R.id.fever_map_menu_4 || id == R.id.fever_map_menu_4_iv) { // 헬스케어 서비스란
            DummyActivity.startActivity(TemperActivity.this, HealthCareServiceFragment.class, null);
        }
    }

    /**
     * 슬라이드메뉴 체온관리
     */
    private void slideMenu1() {
        requestPermissionLocation(new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                if (isSuccess) {
                    DummyActivity.startActivity(TemperActivity.this, TemperControlFragment.class, null);
//                        GpsInfo gps = new GpsInfo(TemperActivity.this);
//                        if (gps.isGetLocation()) {
//                            moveMyLocation();
//                        } else {
//                            gps.showSettingsAlert();
//                        }
                } else {
                    CDialog.showDlg(TemperActivity.this, getString(R.string.fever_health_no_alert_title), "권한 설정 후 이용 가능합니다.");
                }
            }
        });
    }

    /**
     * 슬라이드메뉴 검강검진 예약
     */
    private void slideMenu2() {
        Tr_Login login = SharedPref.getInstance(TemperActivity.this).getLoginInfo();
        if ("1000".equals(login.resultcode)) {
            // DB 전송이 완료된 경우
            DummyActivity.startActivity(TemperActivity.this, HealthRservationFragment.class, null);
        } else {
            CDialog.showDlg(TemperActivity.this, R.string.fever_health_no_alert_title, R.string.fever_health_no_alert_message);
        }
    }

    private void slideMenu3() {
        Tr_Login login = SharedPref.getInstance(TemperActivity.this).getLoginInfo();
        if ("1000".equals(login.resultcode)) {
            // DB 전송이 완료된 경우
            CDialog dlg = CDialog.showDlg(TemperActivity.this, R.string.fever_health_call_alert_title, R.string.fever_health_call_alert_message);
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
            CDialog.showDlg(TemperActivity.this, R.string.fever_health_no_alert_title, R.string.fever_health_no_alert_message);
        }
    }

    /**
     * 열지도 마커 그리기
     * @param feverMapItem
     * @return
     */
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

    /**
     * View를 Bitmap으로 변환
     * @param context
     * @param view
     * @return
     */
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
        Tr_MapList.RequestData requestData = new Tr_MapList.RequestData();
        getData(Tr_MapList.class, requestData, new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                if (data instanceof Tr_MapList) {
                    Tr_MapList recv = (Tr_MapList) data;

                    if (recv.isSuccess(recv.resultcode)) {
                        for (Tr_MapList.MapList mapList : recv.mapList) {
                            LocationItem item = new LocationItem();
                            item.setLoc_nm_1(mapList.loc_nm_1);
                            item.setLoc_nm_2(mapList.loc_nm_2);
                            item.setAvg_fever(mapList.avg_fever);
                            item.setLoc_1(mapList.loc_1);
                            item.setLoc_2(mapList.loc_2);

                            if (Double.parseDouble(item.getAvg_fever()) > 0)
                                addMarker(item);
                        }
                    } else {
                        CDialog.showDlg(TemperActivity.this, recv.message);
                    }
                } else {
                    CDialog.showDlg(TemperActivity.this, "데이터 수신에 실패 하였습니다.");
                }
            }
        });
    }

    private Intent inTentGo = null;
    private int typePush = FirebaseMessagingService.FEVER;

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
        mMap.setMyLocationEnabled(isLocationPermission());
        removeMyLocationButton();

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                GpsInfo gps = new GpsInfo(TemperActivity.this);
                if (gps.isGetLocation()) {
                    moveMyLocation();
                } else {
                    gps.showSettingsAlert();
                }
                return false;
            }
        });
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(true);
//        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
//        mMap.getUiSettings().setMapToolbarEnabled(true);

        setCustomMarkerView();

        GpsInfo gps = new GpsInfo(TemperActivity.this);
        if (gps.isGetLocation()) {
            if (isLocationPermission()) {
                moveMyLocation();
            } else {
                LatLng latLng = new LatLng(37.575784, 126.976789);  // 위치가 안켜진 경우 광화문
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        } else {
            LatLng latLng = new LatLng(37.575784, 126.976789);  // 위치가 안켜진 경우 광화문
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        }
    }

    /**
     * 구글맵 내위치 버튼 없애기
     * 하드코딩 처리
     */
    private void removeMyLocationButton() {
        RelativeLayout mapLayout = findViewById(R.id.map_layout);
        FrameLayout mapView = (FrameLayout) mapLayout.getChildAt(0);
        for (int i = 0; i < mapView.getChildCount(); i++) {
//            Log.i(TAG, "mapLayout1["+i+"]::"+mapView.getChildAt(i));
            View childView = mapView.getChildAt(i);
            if (childView instanceof FrameLayout) {
                FrameLayout frameLayout = (FrameLayout) childView;
//                Log.i(TAG, " mapLayout2["+i+"]::"+childView);
//                FrameLayout linearLayout = (FrameLayout) childView;
                for (int j = 0; j < frameLayout.getChildCount(); j++) {
//                    Log.i(TAG, "  mapLayout3["+j+"]::"+frameLayout.getChildAt(j));
                    if (frameLayout.getChildAt(j) instanceof RelativeLayout) {
                        RelativeLayout child2Layout = (RelativeLayout) frameLayout.getChildAt(j);
                        for (int k = 0; k < child2Layout.getChildCount(); k++) {
//                            Log.i(TAG, "      mapLayout4["+k+"]::"+child2Layout.getChildAt(k));
                            if (child2Layout.getChildAt(k) instanceof ImageView) {
                                // 내위치 버튼 없애기
                                ImageView iv = (ImageView) child2Layout.getChildAt(k);
//                                iv.setImageResource(0);
//                                Log.i(TAG, "      mapLayout.iv["+j+"]["+k+"]::"+iv.getX()+", "+iv.getY()+", iv="+iv);
                                if (j == 2 && k == 0) {
                                    iv.setImageResource(0); // 구글 맵 내 위치 버튼 없애기
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 내위치로 이동
     */
    private void moveMyLocation() {
        requestPermissionLocation(new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                if (isSuccess) {
                    if (mMap != null) {
                        mMap.setMyLocationEnabled(isLocationPermission());
                        removeMyLocationButton();
                    }
                    GpsInfo gps = new GpsInfo(TemperActivity.this);
                    if (gps.isGetLocation()) {
                        LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                    } else {

                    }
                }
            }
        });

//        GpsInfo gps = new GpsInfo(this);
//        if (gps.isGetLocation()) {
//            LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

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
//        }
    }

    private void setCustomMarkerView() {
        mMarkerRootView = LayoutInflater.from(this).inflate(R.layout.custom_marker, null);
        marker = (TextView) mMarkerRootView.findViewById(R.id.marker);
    }


    /**
     * 남은이용일수 구하기
     * @return
     */
    private void getRemainUseDate() {
        TextView remainTextview = findViewById(R.id.fever_map_remain_day);
        // 남은 이용일수 구하기
        Tr_Login login = SharedPref.getInstance(TemperActivity.this).getLoginInfo();
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
            if (day <= 0) {
                // 남은 이용일수가 0일인 경우 건강상담표시 하지 않음
                findViewById(R.id.fever_map_menu_3_line).setVisibility(View.GONE);
                findViewById(R.id.fever_map_menu_3).setVisibility(View.GONE);
            } else {
                // 남은이용일수rk 있는 경우 남은 일자 표시
                remainTextview.setText("남은 이용일 수 "+day+"일");
                // 남은 이용일수가 0일인 경우 건강상담표시 하지 않음
                findViewById(R.id.fever_map_menu_3_line).setVisibility(View.VISIBLE);
                findViewById(R.id.fever_map_menu_3).setVisibility(View.VISIBLE);
            }
        } else {
            // DB전송이 완료되지 않은 경우, 남은이용일수 표시 하지 않음
            remainTextview.setVisibility(View.INVISIBLE);
        }
    }

    private void firstLocationPermission() {
        requestPermissionLocation(new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                if(null != SharedPref.getInstance(TemperActivity.this).getPreferences(SharedPref.PREF_TEMPERATE) &&
                        !"".equals(SharedPref.getInstance(TemperActivity.this).getPreferences(SharedPref.PREF_TEMPERATE))) {
                    slideMenu1();
                }
            }
        });
//        // 최초1회 위치 권한 팝업 띄우기
//        boolean isFirstShow = SharedPref.getInstance(this).getPreferences(SharedPref.LOCATION_PERMISSION_FIRST_SHOW, true);
//        if (isFirstShow == true) {
//            requestPermissionLocation(new IGCResult() {
//                @Override
//                public void onResult(boolean isSuccess, String message, Object data) {
//                    if(null != SharedPref.getInstance(TemperActivity.this).getPreferences(SharedPref.PREF_TEMPERATE) &&
//                            !"".equals(SharedPref.getInstance(TemperActivity.this).getPreferences(SharedPref.PREF_TEMPERATE))) {
//                        slideMenu1();
//                    }
//                }
//            });
//            SharedPref.getInstance(this).savePreferences(SharedPref.LOCATION_PERMISSION_FIRST_SHOW, false);
//        } else {
//            if(null != SharedPref.getInstance(TemperActivity.this).getPreferences(SharedPref.PREF_TEMPERATE) &&
//                    !"".equals(SharedPref.getInstance(TemperActivity.this).getPreferences(SharedPref.PREF_TEMPERATE))) {
//                slideMenu1();
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reLogin();

        if (mapFragment != null)
            mapFragment.onResume();
    }

    /**
     * 재 로그인 완료
     */
    @Override
    protected void reLoginComplete() {
        super.reLoginComplete();

        getRemainUseDate(); // 남은이용일수 구하기
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
//            e.printStackTrace();
        }
    }
}
