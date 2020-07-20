package com.greencross.gctemperlib.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.util.Util;

public class MainPopActivity extends Activity implements View.OnClickListener {
    ImageButton btn_x/*, btn_no_again*/;
    CheckBox cb_no_again;

    TextView txt_title_alarm, txt_main, txt_no_again;

    FrameLayout fl_no_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_alarm_pop);

        initView();
        setData();
        setClick();
    }

    private void setData() {
        String sTitle="";
        String sTxt="";
        Intent intent = getIntent();
        if(intent != null) {
            sTitle = intent.getStringExtra("title");
            sTxt = intent.getStringExtra("txt");

            txt_title_alarm.setText(sTitle);

            sTxt = sTxt.replace("-","●");
            txt_main.setText(sTxt);
        }

    }

    private void setClick() {
        btn_x.setOnClickListener(this);
//        btn_no_again.setOnClickListener(this);
        txt_no_again.setOnClickListener(this);
        fl_no_again.setOnClickListener(this);
    }

    private void initView() {
        btn_x = (ImageButton)findViewById(R.id.btn_x);
//        btn_no_again = (ImageButton)findViewById(R.id.btn_no_again);

        cb_no_again = ( CheckBox ) findViewById( R.id.cb_no_again );
        cb_no_again.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked ) {
                    // perform logic
                }
            }
        });

        txt_title_alarm = (TextView) findViewById(R.id.txt_title_alarm);
        txt_main = (TextView) findViewById(R.id.txt_main);

        txt_no_again = (TextView) findViewById(R.id.txt_no_again);

        fl_no_again = (FrameLayout) findViewById(R.id.fl_no_again);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_x) {
            if (cb_no_again.isChecked()) {
                setNoViewPop();
            }
            finish();
            //            case R.id.btn_no_again:
        } else if (id == R.id.fl_no_again || id == R.id.txt_no_again) {
            if (cb_no_again.isChecked()) {
                cb_no_again.setChecked(false);
            } else {
                cb_no_again.setChecked(true);
            }
//                setNoViewPop();
//                finish();
        }
    }

    private void setNoViewPop() {
        String sNowTime = Util.getNowDateFormat();
        CommonData.getInstance(MainPopActivity.this).setAfterMainPopupShowCheck(sNowTime);
    }
}
