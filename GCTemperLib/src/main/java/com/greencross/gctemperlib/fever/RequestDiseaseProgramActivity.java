package com.greencross.gctemperlib.fever;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.MakeProgress;

/**
 * 다이어트프로그램 신청하기
 */
public class RequestDiseaseProgramActivity extends BackBaseActivity {
    private final String TAG = getClass().getSimpleName();

    private Intent mIntent;
    private View mActionbar;
    private MakeProgress mProgress = null;

    private TextView phone_tv, disease_program_tv1, disease_program_tv2, disease_program_tv3;
    private ImageView sms_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_disease_program);

        if (mProgress == null)
            mProgress = new MakeProgress(this);

        mIntent = getIntent();

        setTitle(getString(R.string.diseae_title));
        mActionbar = (View) findViewById(R.id.common_back_top);

        phone_tv = findViewById(R.id.phone_tv);
        sms_btn = findViewById(R.id.sms_btn);

        disease_program_tv1 = findViewById(R.id.disease_program_tv1);
        disease_program_tv2 = findViewById(R.id.disease_program_tv2);
        disease_program_tv3 = findViewById(R.id.disease_program_tv3);

//        CommonFunction.setTextByPartsStringColor(String.format(getString(R.string.diet_text5), StringUtil.getPhoneNumFormat(commonData.getPhoneNumber())),
//                String.format(getString(R.string.diet_text5), StringUtil.getPhoneNumFormat(commonData.getPhoneNumber())).indexOf("호는") + 2,
//                String.format(getString(R.string.diet_text5), StringUtil.getPhoneNumFormat(commonData.getPhoneNumber())).indexOf("입") - 1,
//                phone_tv, getResources().getColor(R.color.bg_yellow_light));

//        CommonFunction.setTextByPartsStringBold(getString(R.string.diseae_text1),
//                getString(R.string.diseae_text1).indexOf("유"),getString(R.string.diseae_text1).indexOf("램")+1,disease_program_tv1,false);
//
//        CommonFunction.setTextByPartsStringBold(getString(R.string.diseae_text2),
//                getString(R.string.diseae_text2).indexOf("!")+1,getString(R.string.diseae_text2).indexOf("결")+1,disease_program_tv2,false);
//
//        CommonFunction.setTextByPartsStringBold(getString(R.string.diseae_text3),
//                getString(R.string.diseae_text3).indexOf("1"),getString(R.string.diseae_text3).indexOf("공")+1,disease_program_tv3,false);



//        sms_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RequestDiseaseProgramActivity.this, RequestSmsProgramActivity.class);
//                intent.putExtra("SMS_TYPE","disease");
//                startActivity(intent);
//                finish();
//            }
//        });

    }

    @Override
    public void showProgress() {
//        super.showProgress();
        if (mProgress != null)
            mProgress.show();
    }

    @Override
    public void hideProgress() {
//        super.hideProgress();
        if (mProgress != null)
            mProgress.dismiss();
    }
}
