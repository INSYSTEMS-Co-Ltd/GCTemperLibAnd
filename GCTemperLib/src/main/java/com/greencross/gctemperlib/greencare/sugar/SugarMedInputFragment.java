package com.greencross.gctemperlib.greencare.sugar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.greencross.gctemperlib.greencare.base.BaseFragment;
import com.greencross.gctemperlib.greencare.base.CommonActionBar;
import com.greencross.gctemperlib.greencare.bluetooth.manager.BluetoothManager;
import com.greencross.gctemperlib.greencare.bluetooth.manager.DeviceDataUtil;
import com.greencross.gctemperlib.greencare.bluetooth.model.BloodModel;
import com.greencross.gctemperlib.greencare.component.CDatePicker;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;
import com.greencross.gctemperlib.greencare.util.ViewUtil;
import com.greencross.gctemperlib.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by insystemscompany on 2017. 2. 28..
 */

public class SugarMedInputFragment extends BaseFragment {
    private static final String TAG = SugarMedInputFragment.class.getSimpleName();

    private TextView mDateTv, mTimeTv;
    private EditText mMedicenInputTv;
    private ImageButton mSaveBtn;

    public static Fragment newInstance() {
        SugarMedInputFragment fragment = new SugarMedInputFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sugar_med_input, container, false);
        return view;
    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
        super.loadActionbar(actionBar);
        actionBar.setActionBarTitle( getString(R.string.text_medi_info_input));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDateTv = (TextView) view.findViewById(R.id.sugar_input_date_textview);
        mTimeTv = (TextView) view.findViewById(R.id.sugar_input_time_textview);
        mMedicenInputTv = (EditText) view.findViewById(R.id.sugar_medicen_editext);
        mSaveBtn = (ImageButton)view.findViewById(R.id.sugar_med_input_save_btn);
        ViewUtil.setTypefacenotosanskr_bold(getContext(), mSaveBtn);

        String now_time = CDateUtil.getForamtyyyyMMddHHmm(new Date(System.currentTimeMillis()));
        java.util.Calendar cal = CDateUtil.getCalendar_yyyy_MM_dd_HH_mm(now_time);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        mDateTvSet(year, month, day);
        mTimeTvSet(hour, min);

        mSaveBtn.setEnabled(true);

        mDateTv.addTextChangedListener(watcher);
        mTimeTv.addTextChangedListener(watcher);

        mDateTv.setOnTouchListener(mTouchListener);
        mTimeTv.setOnTouchListener(mTouchListener);

        mSaveBtn.setOnClickListener(mOnClickListener);
    }

    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int vId = v.getId();
                if (vId == R.id.sugar_input_date_textview) {
                    showDatePicker(v);
                } else if (vId == R.id.sugar_input_time_textview) {
                    showTimePicker();
                }
            }
            return false;
        }
    };

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if (vId == R.id.sugar_med_input_save_btn) {

                final String regDate = mDateTv.getTag().toString();
                if (TextUtils.isEmpty(regDate)) {
                    CDialog.showDlg(getContext(), "날자를 입력해 주세요.");
                    return;
                }

                final String timeStr = mTimeTv.getTag().toString();
                if (TextUtils.isEmpty(timeStr)) {
                    CDialog.showDlg(getContext(), "시간을 입력해 주세요.");
                    return;
                }

                doInputSugar(regDate, timeStr);

               /* CDialog.showDlg(getContext(), "투약정보를 등록하시겠습니까?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, null);*/
            }
        }
    };

    private void doInputSugar(String regDate, String timeStr) {

        regDate += timeStr;


        String medicen = mMedicenInputTv.getText().toString();
        if (TextUtils.isEmpty(medicen)) {
            medicen = " ";

        }

        SparseArray<BloodModel> array = new SparseArray<>();
        BloodModel dataModel = new BloodModel();
        dataModel.setIdx(CDateUtil.getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
        dataModel.setMedicenName(medicen);
        dataModel.setBefore("1");
        dataModel.setRegtype(BloodModel.INPUT_TYPE_U);
        dataModel.setRegTime(regDate);

        array.append(0, dataModel);
        new DeviceDataUtil().uploadSugarData(SugarMedInputFragment.this, array, true, new BluetoothManager.IBluetoothResult() {
            @Override
            public void onResult(boolean isSuccess) {
                if (isSuccess) {
                    CDialog.showDlg(getContext(), "등록 되었습니다.", new CDialog.DismissListener() {
                        @Override
                        public void onDissmiss() {
                            mDateTv.setText("");
                            mDateTv.setTag("");
                            mTimeTv.setText("");
                            mTimeTv.setTag("");
                            mMedicenInputTv.setText("");
                            onBackPressed();
                        }
                    });
                } else {
                    CDialog.showDlg(getContext(), "등록에 실패 하였습니다.");
                }
            }
        });
    }

    private void showDatePicker(View v) {
        GregorianCalendar calendar = new GregorianCalendar();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String date = mDateTv.getTag().toString();
        if (TextUtils.isEmpty(date) == false) {
            year = StringUtil.getIntVal(date.substring(0 , 4));
            month = StringUtil.getIntVal(date.substring(4 , 6))-1;
            day = StringUtil.getIntVal(date.substring(6 , 8));
        }

        new CDatePicker(getContext(), dateSetListener, year, month, day, false).show();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            mDateTvSet(year, monthOfYear, dayOfMonth);
        }

    };

    private void mDateTvSet(int year, int monthOfYear, int dayOfMonth){
        String msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth);
        String tagMsg = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth);
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear + 1);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        mDateTv.setText(msg+" "+ CDateUtil.getDateToWeek(tagMsg)+"요일");
        mDateTv.setTag(tagMsg);
    }

    private void showTimePicker() {
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        Date now = new Date();
        cal.setTime(now);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String time = mTimeTv.getTag().toString();
        if (TextUtils.isEmpty(time) == false) {
            hour = StringUtil.getIntVal(time.substring(0, 2));
            minute = StringUtil.getIntVal(time.substring(2 , 4));

            Logger.i(TAG, "hour="+hour+", minute="+minute);
        }

        TimePickerDialog dialog = new TimePickerDialog(getContext(), listener, hour, minute, false);
        dialog.show();
    }

    /**
     * 시간 피커 완료
     */
    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            mTimeTvSet(hourOfDay, minute);
        }
    };

    private void mTimeTvSet(int hourOfDay, int minute){
        // 설정버튼 눌렀을 때
        String amPm = "오전";
        int hour = hourOfDay;
        if (hourOfDay > 11) {
            amPm = "오후";
            if (hourOfDay >= 13)
                hour -= 12;
        } else {
            hour = hour == 0 ? 12 : hour;
        }
        String tagMsg = String.format("%02d%02d", hourOfDay, minute);
        String timeStr = String.format("%02d:%02d", hour, minute);
        mTimeTv.setText(amPm + " " + timeStr);
        mTimeTv.setTag(tagMsg);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {

            if (isValidDate() && isValidTime()) {
                mSaveBtn.setEnabled(true);
            } else {
                mSaveBtn.setEnabled(false);
            }
        }
    };

    private boolean isValidDate() {
        String text = mDateTv.getText().toString();
        return TextUtils.isEmpty(text) == false;
    }

    private boolean isValidTime() {
        String text = mTimeTv.getText().toString();
        return TextUtils.isEmpty(text) == false;
    }
}