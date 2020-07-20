package com.greencross.gctemperlib.fever;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.common.MakeProgress;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_disease_program_req_crt;

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




        // 신청
        findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDiseaeProgram();
            }
        });


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

    /**
     * 유행성 질환 정보 프로그램  신청하기
     */
    private void requestDiseaeProgram() {
        Tr_disease_program_req_crt.RequestData requestData = new Tr_disease_program_req_crt.RequestData();

        CommonData login = CommonData.getInstance(this);
        requestData.mber_sn = login.getMberSn();
        requestData.mber_hp = login.getPhoneNumber();


        new ApiData().getData(RequestDiseaseProgramActivity.this, Tr_disease_program_req_crt.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_disease_program_req_crt) {
                    Tr_disease_program_req_crt tr = (Tr_disease_program_req_crt)obj;
                    if ("Y".equals(tr.data_yn)) {
                        // 신청완료
                        popupCompleteProgram();
                    } else {
                        // 이미신청중
                        popupYetProgram();
                    }
                } else {
                    CDialog.showDlg(RequestDiseaseProgramActivity.this, "데이터 수신에 실패 하였습니다.");
                }
            }
        });
    }


    /**
     * 유행성 질환 정보 프로그램 이미 신청중 팝업
     */
    private void popupYetProgram() {
        mDialog = new CustomAlertDialog(RequestDiseaseProgramActivity.this, CustomAlertDialog.TYPE_A);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_request_diet_program_complete, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mDialog.setContentView(view,params);

        TextView t1 =  view.findViewById(R.id.dialog_content);
        TextView t2 = view.findViewById(R.id.dialog_content1);
        Button b1 = view.findViewById(R.id.confirm_btn);

        ViewCompat.setBackgroundTintList(
                b1,
                ColorStateList.valueOf(getResources().getColor(R.color.bg_yellow_light)));

        t1.setText("이미 유행질병 정보 프로그램을 이용중입니다.");
        t2.setText(getString(R.string.diet_diseae_no));

        mDialog.setPositiveButton(RequestDiseaseProgramActivity.this.getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
            @Override
            public void onClick(CustomAlertDialog dialog, Button button) {
                finish();
                dialog.dismiss();
            }
        });
        mDialog.show();
    }


    /**
     * 유행성 질환 정보 프로그램 이미 신청중 팝업
     */
    private void popupCompleteProgram() {
        mDialog = new CustomAlertDialog(RequestDiseaseProgramActivity.this, CustomAlertDialog.TYPE_A);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_request_diet_program_complete, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mDialog.setContentView(view,params);

        TextView t1 =  view.findViewById(R.id.dialog_content);
        TextView t2 = view.findViewById(R.id.dialog_content1);

        Button b1 = view.findViewById(R.id.confirm_btn);

        ViewCompat.setBackgroundTintList(
                b1,
                ColorStateList.valueOf(getResources().getColor(R.color.bg_yellow_light)));

        t1.setText("유행질병 정보 프로그램이 신청되었습니다.\n차주 화요일부터 10주 동안 콘텐츠를 제공해 드립니다.");
        t2.setText(getString(R.string.diet_diseae_no));

        mDialog.setPositiveButton(RequestDiseaseProgramActivity.this.getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
            @Override
            public void onClick(CustomAlertDialog dialog, Button button) {
                finish();
                dialog.dismiss();
            }
        });
        mDialog.show();
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
