package com.greencross.gctemperlib.hana;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.greencare.component.CDatePicker;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.PermissionUtil;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.greencare.util.cameraUtil.RuntimeUtil;
import com.greencross.gctemperlib.util.GpsInfo;
import com.greencross.gctemperlib.util.PermissionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TemperControlFragment extends BaseFragment {

    private ImageView mTemperNoticeIcon;
    private TextView mTemperNoticeTitle;
    private TextView mTemperNoticeMessage;

    private TextView mTemperTextview;

    private boolean mIsWearable = false;

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
                DummyActivity.startActivity(TemperControlFragment.this, TemperGraphFragment2.class, new Bundle());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTemperNoticeIcon = view.findViewById(R.id.temper_notice_icon);
        mTemperNoticeTitle = view.findViewById(R.id.temper_notice_title);
        mTemperNoticeMessage = view.findViewById(R.id.temper_notice_message);

        mTemperTextview = view.findViewById(R.id.temper_textview);
//        String temper = SharedPref.getInstance(getContext()).getPreferences(SharedPref.TEMPER);
//        temper = TextUtils.isEmpty(temper) ? "0.0" : temper;
//        mTemperTextview.setText(temper);

        TextView dateTv = view.findViewById(R.id.date_textview);
        TextView timeTv = view.findViewById(R.id.time_textview);

        String today = CDateUtil.getToday_temper_graph();
        dateTv.setText(today);
//        dateTv.setTag(cal.getTimeInMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat(CommonData.PATTERN_TIME_2);
        timeTv.setText(timeFormat.format(new Date()));

        dateTv.setOnClickListener(mClickListener);
        timeTv.setOnClickListener(mClickListener);
        view.findViewById(R.id.temper_control_call_device_btn).setOnClickListener(mClickListener);

        getTemperMessage();
//        view.findViewById(R.id.temper_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final View temperInputAlertView = LayoutInflater.from(getContext()).inflate(R.layout.temper_input_alert, null, false);
//                final EditText temperEt = temperInputAlertView.findViewById(R.id.temper_input_edittext);
//                new TextWatcherUtil().setTextWatcher(temperEt, 50, 1);
//                CDialog.showDlg(getContext(), temperInputAlertView)
//                        .setOkButton(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                String temper = temperEt.getText().toString();
//                                if (TextUtils.isEmpty(temper))
//                                    return;
//                                mTemperTextview.setText(temper);
//                                mIsWearable = false;
//                                getTemperMessage();
////                                SharedPref.getInstance(getContext()).savePreferences(SharedPref.TEMPER, temper);
//                            }
//                        })
//                        .setNoButton(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                            }
//                        });
//            }
//        });

        // 체온 등록하기
        view.findViewById(R.id.temper_regist_done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsInfo gps = new GpsInfo(getContext());
                if (gps.isGetLocation()) {
//                    registTemper(mIsWearable);
                } else {
                    gps.showSettingsAlert();
                }
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
        mTemperNoticeTitle.setText(mTemperTitles[resultIdx]);
        mTemperNoticeMessage.setText(mTemperText[resultIdx]);
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
        String packageName = "com.partron.temperature310";
        try {
            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } catch (Exception e) {
            CDialog.showDlg(getContext(), "사용하시는 스마트폰에 \n 파트론 앱이 설치되어있지 않습니다.", "앱을 설치하기 위해 앱스토어로 이동합니다.")
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
        String msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth);
        String tagMsg = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth);
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear + 1);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        tv.setText(CDateUtil.getFormatYYYYMMDD(tagMsg));
        tv.setTag(cal.getTimeInMillis());
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
            cal.set(Calendar.YEAR, cal_year);
            cal.set(Calendar.MONTH, cal_month);
            cal.set(Calendar.DAY_OF_MONTH, cal_day);
            cal.set(Calendar.HOUR_OF_DAY, pram1);
            cal.set(Calendar.MINUTE, pram2);

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
                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendar.set(Calendar.MINUTE, minute);
                    Date checkDate = new Date();
//                    if (mCalendar.getTime().compareTo(checkDate) >= 0) {    // 오늘 지남
//                        Toast.makeText(getContext(), getString(R.string.over_time), Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    mCurDate = mCalendar.getTime();
                    SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_TIME_2);
                    tv.setText(format.format(mCurDate));

                    format = new SimpleDateFormat(CommonData.PATTERN_DATETIME);
                    mCheckDate = format.format(mCurDate);
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
            Toast.makeText(getContext(), "권한설저 됨.", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(PermissionUtils.LOCATION_PERMS, CommonData.PERMISSION_REQUEST_GPS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGranted = RuntimeUtil.verifyPermissions(grantResults);
    }
}
