package com.greencross.gctemperlib.hana;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.GCTemperLib;
import com.greencross.gctemperlib.IGCResult;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.hana.component.CDatePicker;
import com.greencross.gctemperlib.hana.component.CDialog;
import com.greencross.gctemperlib.util.CDateUtil;
import com.greencross.gctemperlib.util.PermissionUtil;
import com.greencross.gctemperlib.util.SharedPref;
import com.greencross.gctemperlib.util.StringUtil;
import com.greencross.gctemperlib.util.TextWatcherUtil;
import com.greencross.gctemperlib.util.cameraUtil.RuntimeUtil;
import com.greencross.gctemperlib.util.PermissionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;
import static com.greencross.gctemperlib.hana.network.tr.BaseUrl.HEALTH_BOX_URL;

public class TemperControlFragment extends BaseFragment {
    final static int REQUEST_CODE_BODY_TEMPERATURE = 10101;

    private ImageView mTemperNoticeIcon;
    private ImageView mTemperBubbleIv;
//    private TextView mTemperNoticeTitle;
//    private TextView mTemperNoticeMessage;

    private TextView mTemperTextview;
    private ScrollView  mScrollView;

    private boolean mIsWearable = false;    // 기기,수기 등록 여부

    private TextView mDateTv;
    private TextView mTtimeTv;

    // 스마일 아이콘
    private int[] mTemperIcon = new int[]{
            R.drawable.hn_temper_smile_1
            , R.drawable.hn_temper_smile_2
            , R.drawable.hn_temper_smile_3
            , R.drawable.hn_temper_smile_4
            , R.drawable.hn_temper_smile_5
            , R.drawable.hn_temper_smile_6
            , R.drawable.hn_temper_smile_7
    };

    private int[] mTemperBubbleImage = new int[]{
            R.drawable.hn_temper_bubble_1
            , R.drawable.hn_temper_bubble_2
            , R.drawable.hn_temper_bubble_3
            , R.drawable.hn_temper_bubble_4
            , R.drawable.hn_temper_bubble_5
            , R.drawable.hn_temper_bubble_6
            , R.drawable.hn_temper_bubble_7
    };

    private int[] mTemperTitles = new int[]{
            R.string.temper_title1
            , R.string.temper_title2
            , R.string.temper_title3
            , R.string.temper_title4
            , R.string.temper_title5
            , R.string.temper_title6
            , R.string.temper_title7
    };

    private int[] mTemperText = new int[]{
            R.string.temper_message1
            , R.string.temper_message2
            , R.string.temper_message3
            , R.string.temper_message4
            , R.string.temper_message5
            , R.string.temper_message6
            , R.string.temper_message7
    };

    public static Fragment newInstance() {
        TemperControlFragment fragment = new TemperControlFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mCalendar = new GregorianCalendar();

        View view = inflater.inflate(R.layout.temper_control_fragment, container, false);
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
        mScrollView = view.findViewById(R.id.temper_scrollview);
        mTemperNoticeIcon = view.findViewById(R.id.temper_notice_icon);
        mTemperBubbleIv = view.findViewById(R.id.temper_bubble_iv);
//        mTemperNoticeTitle = view.findViewById(R.id.temper_notice_title);
//        mTemperNoticeMessage = view.findViewById(R.id.temper_notice_message);

        mTemperTextview = view.findViewById(R.id.temper_textview);
//        String temper = SharedPref.getInstance(getContext()).getPreferences(SharedPref.TEMPER);
//        temper = TextUtils.isEmpty(temper) ? "0.0" : temper;
//        mTemperTextview.setText(temper);

        if(null != SharedPref.getInstance(getContext()).getPreferences(SharedPref.PREF_TEMPERATE) &&
                !"".equals(SharedPref.getInstance(getContext()).getPreferences(SharedPref.PREF_TEMPERATE))) {
            mTemperTextview.setText(String.format("%.1f", StringUtil.getFloatVal(SharedPref.getInstance(getContext()).getPreferences(SharedPref.PREF_TEMPERATE))));
            getTemperMessage();
            mIsWearable = true;
        }

        SharedPref.getInstance(getContext()).savePreferences(SharedPref.PREF_TEMPERATE, "");

        mDateTv = view.findViewById(R.id.date_textview);
        mTtimeTv = view.findViewById(R.id.time_textview);

        String today = CDateUtil.getToday_temper_graph();
        mDateTv.setText(today);
        mDateTv.setTag(CDateUtil.getToday_yyyyMMdd());
//        dateTv.setTag(cal.getTimeInMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat(CommonData.PATTERN_TIME_2);
        mTtimeTv.setText(timeFormat.format(new Date()));

        mDateTv.setOnClickListener(mClickListener);
        mTtimeTv.setOnClickListener(mClickListener);
        view.findViewById(R.id.temper_control_call_device_btn).setOnClickListener(mClickListener);

        getTemperMessage();
        view.findViewById(R.id.temper_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsWearable = false;
                final View temperInputAlertView = LayoutInflater.from(getContext()).inflate(R.layout.temper_input_alert, null, false);
                final EditText temperEt = temperInputAlertView.findViewById(R.id.temper_input_edittext);
                new TextWatcherUtil().setTextWatcher(temperEt, 42, 1);
                CDialog.showDlg(getContext(), temperInputAlertView)
                        .setOkButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String temper = temperEt.getText().toString();
                                if (TextUtils.isEmpty(temper))
                                    return;

                                if (StringUtil.getFloatVal(temper) < 30) {
                                    CDialog.showDlg(getContext(), "체온은 30℃ ~ 42℃까지 입력 가능합니다.");
                                    return;
                                }
                                mTemperTextview.setText(String.format("%.1f", StringUtil.getFloatVal(temper)));
                                getTemperMessage();
//                                SharedPref.getInstance(getContext()).savePreferences(SharedPref.TEMPER, temper);
                            }
                        })
                        .setNoButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });
            }
        });

        // 체온 등록하기
        view.findViewById(R.id.temper_regist_done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temper = mTemperTextview.getText().toString();
                if (StringUtil.getFloat(temper) <= 0.0f) {
//                    CDialog.showDlg(getContext(), getString(R.string.fever_health_no_alert_title), getString(R.string.temper_title1));
                    Toast.makeText(getContext(), getString(R.string.temper_title1), Toast.LENGTH_SHORT).show();
                    return;
                }

                GCTemperLib gcTemperLib = new GCTemperLib(getContext());

                String date = mDateTv.getTag().toString();
                String time = mTtimeTv.getText().toString();
//                    date = date.replaceAll("\\.", "-");

                String registTime = date + " " +time;

                String[] temp_time = time.split(":");
                if(temp_time.length >= 2) {
                    if (!DateTimeCheck("T", StringUtil.getIntVal(temp_time[0]), StringUtil.getIntVal(temp_time[1]), 0)) {
                        return;
                    }
                }

                gcTemperLib.registGCTemperServer(temper, registTime, mIsWearable, new IGCResult() {
                    @Override
                    public void onResult(boolean isSuccess, String message, Object data) {
                        if (isSuccess) {
                            DummyActivity.startActivity(TemperControlFragment.this, TemperGraphFragment.class, new Bundle());
                        } else {
                            CDialog.showDlg(getContext(), getString(R.string.fever_health_no_alert_title), message);
                        }
                    }
                });

//                GpsInfo gps = new GpsInfo(getContext());
//                if (gps.isGetLocation()) {
//                    GCTemperLib gcTemperLib = new GCTemperLib(getContext());
//
//                    String date = mDateTv.getTag().toString();
//                    String time = mTtimeTv.getText().toString();
////                    date = date.replaceAll("\\.", "-");
//
//                    String registTime = date + " " +time;
//
//                    String[] temp_time = time.split(":");
//                    if(temp_time.length >= 2) {
//                        if (!DateTimeCheck("T", StringUtil.getIntVal(temp_time[0]), StringUtil.getIntVal(temp_time[1]), 0)) {
//                            return;
//                        }
//                    }
//
//                    gcTemperLib.registGCTemperServer(temper, registTime , new IGCResult() {
//                        @Override
//                        public void onResult(boolean isSuccess, String message, Object data) {
//                            if (isSuccess) {
//                                DummyActivity.startActivity(TemperControlFragment.this, TemperGraphFragment.class, new Bundle());
//                            } else {
//                                CDialog.showDlg(getContext(), getString(R.string.fever_health_no_alert_title), message);
//                            }
//                        }
//                    });
//                } else {
//                    gps.showSettingsAlert();
//                }
            }
        });


        // 건강박스 구매하기
        view.findViewById(R.id.temper_health_store_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = SharedPref.getInstance(getContext()).getPreferences(SharedPref.PREF_CUST_ENCRYPT_NO);
                ExternalBrowser(HEALTH_BOX_URL+type);
            }
        });
    }

    /**
     * 상단 체온 메시지 띄우기
     */
    private void getTemperMessage() {
        float temper = StringUtil.getFloat(mTemperTextview.getText().toString());

        String result;
        int resultIdx = 0;
        if (temper == 0.0) {
            resultIdx = 0;
        } else if (temper < 36.0) {   // 36℃ 미만
            resultIdx = 1;
        } else if (temper >= 36.0 && temper < 37.5) {   // 36℃ 이상 37.5℃ 미만
            resultIdx = 2;
        } else if (temper >= 37.5 && temper < 38.0) {   // 37.5℃ 이상 38℃ 미만
            resultIdx = 3;
        } else if (temper >= 38.0 && temper < 39.0) {   // 38℃ 이상 39℃ 미만
            resultIdx = 4;
        } else if (temper >= 39.0 && temper < 40.0) {   //39℃ 이상 40℃ 미만
            resultIdx = 5;
        } else if (temper >= 40.0) {  //40℃ 이상
            resultIdx = 6;
        }

        Log.i(TAG, "getTemperMessage.temper=" + temper + ", " + (temper == 0.0));

        mTemperNoticeIcon.setImageResource(mTemperIcon[resultIdx]);
        mTemperBubbleIv.setImageResource(mTemperBubbleImage[resultIdx]);

        mScrollView.fullScroll(ScrollView.FOCUS_UP);    // 스크롤뷰 맨 위로


//        mTemperNoticeTitle.setText(mTemperTitles[resultIdx]);
//        mTemperNoticeMessage.setText(mTemperText[resultIdx]);
    }

    /**
     * 체온직접입력 Dialog
     */
//    private void registTemper(boolean isWearable) {
//        String temper = mTemperTextview.getText().toString();
//        float temperVal = StringUtil.getFloat(mTemperTextview.getText().toString());
//        if (temperVal == 0.0) {
//            CDialog.showDlg(getContext(), R.string.temper_title1, 0);
//            return;
//        }
//
//        showProgress();
//        final GCTemperLib gcLib = new GCTemperLib(getContext());
//        gcLib.registGCTemper(temper, new IGCResult() {
//            @Override
//            public void onResult(boolean isSuccess, String message, Object data) {
//                hideProgress();
//                if (isSuccess) {
////                    SharedPref.getInstance(getContext()).savePreferences(SharedPref.TEMPER, temper);
////                    CDialog.showDlg(getActivity(), message);
//                    getTemperMessage();
//                    // 체온측정(체온 그래프)로 이동 :
//                    DummyActivity.startActivity(TemperControlFragment.this, TemperGraphFragment.class, new Bundle());
//                } else {
//                    CDialog.showDlg(getActivity(), message);
//                }
//            }
//        });
//    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();

            if (vId == R.id.date_textview) {
                showCalendar((TextView) v);
            } else if (vId == R.id.time_textview) {
                showTimePicker((TextView) v);
            } else if (vId == R.id.temper_control_call_device_btn) {
                openPatron();
            }
        }
    };

    public void openPatron() {
        mIsWearable = true;
        String packageName = "com.partron.temperature310";
        String url = "intent://" +
                "insystems?"
                + "#Intent;" +
                "scheme=partron;" +
                "package=com.partron.temperature310;" +
                "end";
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            getActivity().startActivityForResult(intent, REQUEST_CODE_BODY_TEMPERATURE);
        } catch (Exception e) {
            CDialog.showDlg(getContext(), "사용하시는 스마트폰에\n전용 체온계 앱이 설치되어있지 않습니다.", "전용 체온계 앱을 설치하기 위해\n앱스토어로 이동합니다.")
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

    private int cal_year;
    private int cal_month;
    private int cal_day;
    private int cal_hour;
    private int cal_min;
    private void showCalendar(TextView tv) {
        GregorianCalendar calendar = new GregorianCalendar();
        String date = tv.getText().toString();
        date = StringUtil.getIntString(date);
        int year = StringUtil.getIntger(date.substring(0, 4));   //calendar.get(Calendar.YEAR);
        int month = StringUtil.getIntger(date.substring(4, 6)) - 1;  // calendar.get(Calendar.MONTH)-1;
        int day = StringUtil.getIntger(date.substring(6, 8));
        calendar.get(Calendar.DAY_OF_MONTH);

        new CDatePicker(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (DateTimeCheck("D", year, monthOfYear, dayOfMonth)) {
                    cal_year = year;
                    cal_month = monthOfYear;
                    cal_day = dayOfMonth;
                    mDateTvSet(tv, year, monthOfYear, dayOfMonth);

//                    getData();
                }
            }
        }, year, month, day, false).show();
    }

    private void mDateTvSet(TextView tv, int year, int monthOfYear, int dayOfMonth) {
        String msg = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
        String tagMsg = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth);
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear + 1);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        tv.setText(CDateUtil.getFormatYYYYMMDD(tagMsg, "."));
        tv.setTag(msg);
    }

    private boolean DateTimeCheck(String type, int pram1, int pram2, int pram3) {
        Calendar cal = Calendar.getInstance();

        if (type.equals("D")) {
            cal.set(Calendar.YEAR, pram1);
            cal.set(Calendar.MONTH, pram2);
            cal.set(Calendar.DAY_OF_MONTH, pram3);
            cal.set(Calendar.HOUR_OF_DAY, cal_hour);
            cal.set(Calendar.MINUTE, cal_min);

            if (cal.getTimeInMillis() > System.currentTimeMillis()) {
                CDialog.showDlg(getContext(), getString(R.string.message_nowtime_over), new CDialog.DismissListener() {
                    @Override
                    public void onDissmiss() {

                    }
                });
                return false;
            } else {
                return true;
            }
        } else {
            long date = StringUtil.getLongVal(mDateTv.getTag().toString().replaceAll("-",""));
            long today = StringUtil.getLongVal(CDateUtil.getToday_yyyy_MM_dd());

            long time = StringUtil.getLongVal(String.format("%02d%02d",pram1,pram2));
            long today_time = StringUtil.getLongVal(CDateUtil.getToday_HH_mm());
            if(date == today) {
                if(time > today_time) {
                    CDialog.showDlg(getContext(), getString(R.string.message_nowtime_over), new CDialog.DismissListener() {
                        @Override
                        public void onDissmiss() {

                        }
                    });
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }


    private String mCheckDate;
    private Date mCurDate;
    private GregorianCalendar mCalendar;
    private void showTimePicker(TextView tv) {
        try {
            if (mCurDate == null) {
                mCurDate = new Date();
            }
            mCalendar.setTime(mCurDate);
            int nHourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);
            int nMinute = mCalendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    if (DateTimeCheck("T", hourOfDay, minute, 0)) {
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        mCurDate = mCalendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_TIME_2);
                        tv.setText(format.format(mCurDate));

                        format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
                        mCheckDate = format.format(mCurDate);
                    }
                }
            }, nHourOfDay, nMinute, false);

            timePickerDialog.setCancelable(false);
            timePickerDialog.show();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 위치 권한 설정 하기
     * 최초 1회만 요청
     */
    private void permissionCheck() {
        // 위치 권한 설정 후 이용해 주세요.
        PermissionUtil.checkPermissions(getContext());
        int permissionState = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "권한설정 됨.", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(PermissionUtils.LOCATION_PERMS, CommonData.PERMISSION_REQUEST_GPS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = RuntimeUtil.verifyPermissions(grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_BODY_TEMPERATURE) {
            if(resultCode == RESULT_OK) {
                if (data != null) {
                    Log.d(TAG, "" + data.getStringExtra("bodyTemperature"));
                    float temper = StringUtil.getFloatVal(data.getStringExtra("bodyTemperature"));
                    mTemperTextview.setText(String.format("%.1f", temper));
                    getTemperMessage();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
