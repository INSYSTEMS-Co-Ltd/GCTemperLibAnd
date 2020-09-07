package com.gchelathcare.heat;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.multidex.BuildConfig;

import com.greencross.gctemperlib.hana.GCAlramType;
import com.greencross.gctemperlib.GCTemperLib;
import com.greencross.gctemperlib.IGCResult;
import com.greencross.gctemperlib.hana.component.CDialog;

public class Main1QActivity extends Activity {

    private LinearLayout mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1q_main);

        final GCTemperLib gcLib = new GCTemperLib(this);

        Uri linkValueUrl = getIntent().getData();

        if (linkValueUrl != null) {   // Web으로 부터 실행
            Log.i("Main1QActivity", "Check LinkData - bt : " + linkValueUrl.getQueryParameter("bt"));
            gcLib.registGCTemper(linkValueUrl.getQueryParameter("bt"), new IGCResult() {
                @Override
                public void onResult(boolean isSuccess, String message, Object data) {
                    if(isSuccess) {
                        gcLib.startGCMainActivity();
                    }
                }
            });
        }

        mProgress = findViewById(R.id.progress_layout);

        ((TextView) findViewById(R.id.lib_token_edittext)).setText(BuildConfig.GC_TOKEN);

        // 녹십자 라이브러리 사용을 위한 토큰 등록
        findViewById(R.id.gc_token_regist_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regsitGCToken();
            }
        });

        // 고객번호 등록
        findViewById(R.id.cust_no_regist_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regsitCustNo();
            }
        });

        // Push키 등록
        findViewById(R.id.push_token_regist_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regsitPushToken();
            }
        });

        // 체온 등록
        findViewById(R.id.temper_regist_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regsitTemper();
            }
        });

        // 녹십자 체온관리 실행
        findViewById(R.id.gc_excute_gc_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gcLib.startGCMainActivity();
            }
        });

        Switch switch1 = findViewById(R.id.alram_type_1);
        Switch switch2 = findViewById(R.id.alram_type_2);

        switch1.setChecked(gcLib.getSettingAlramService(GCAlramType.GC_ALRAM_TYPE_독려));
        switch2.setChecked(gcLib.getSettingAlramService(GCAlramType.GC_ALRAM_TYPE_지역));
        // 측정 독려 알림 수신 설정 변경
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                settingAlramService(GCAlramType.GC_ALRAM_TYPE_독려, checked);
            }
        });

        // 지역 체온 알림 수신 설정 변경
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                settingAlramService(GCAlramType.GC_ALRAM_TYPE_지역, checked);
            }
        });

        findViewById(R.id.gc_lib_reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CDialog.showDlg(Main1QActivity.this, "초기화 하시겠습니까?")
                        .setOkButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gcLib.resetGCData();
                            }
                        })
                        .setNoButton(getResources().getString(R.string.comm_cancel), null);

            }
        });



        /**
         * 운영모드, 테스트 모드 설정
         */
        RadioButton debugRadioButton = findViewById(R.id.radio_test_mode);
        RadioButton realRadioButton = findViewById(R.id.radio_real_mode);
        if (gcLib.getDebugMode()) {
            debugRadioButton.setChecked(true);
        } else {
            realRadioButton.setChecked(true);
        }
        ((RadioGroup)findViewById(R.id.rg_test_mode)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio_test_mode) {
                    gcLib.setDebugMode(true);
                } else {
                    gcLib.setDebugMode(false);
                }
            }
        });
    }

    /**
     * 최초 녹십자 라이브러리 사용을 위한 토큰 등록
     */
    private void regsitGCToken() {
        final GCTemperLib gcLib = new GCTemperLib(this);
        boolean isAvailable = gcLib.registGCToken(BuildConfig.GC_TOKEN);
        String message = String.format("인증 %1$s 하였습니다.", isAvailable ? "성공" : "실패");
        CDialog.showDlg(this, message);
    }

    /**
     * 녹십자 라이브러리 고객번호 등록
     */
    public void regsitCustNo() {
        final GCTemperLib gcLib = new GCTemperLib(this);
        TextView custNoTv = findViewById(R.id.cust_no_edittext);
        String customerNo = custNoTv.getText().toString();
        String gender = null;
        showProgress();
        gcLib.registCustomerNo(customerNo, new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                hideProgress();
                if (isSuccess) {
                    CDialog.showDlg(Main1QActivity.this, message);
                } else {
                    CDialog.showDlg(Main1QActivity.this, message);
                }
            }
        });
    }

    /**
     * 녹십자 라이브러리 Push 토큰 등록
     */
    public void regsitPushToken() {
        final GCTemperLib gcLib = new GCTemperLib(this);
        TextView tokenTv = findViewById(R.id.push_token_edittext);
        String pushToken = tokenTv.getText().toString();
        showProgress();
        gcLib.registPushToken(pushToken, new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                hideProgress();
                if (isSuccess) {
                    CDialog.showDlg(Main1QActivity.this, message);
                } else {
                    CDialog.showDlg(Main1QActivity.this, message);
                }
            }
        });
    }

    /**
     * 녹십자 라이브러리 체온등록
     */
    public void regsitTemper() {
        final GCTemperLib gcLib = new GCTemperLib(this);
        TextView temperEditText = findViewById(R.id.temper_edittext);
        String temper = temperEditText.getText().toString();

        showProgress();
        gcLib.registGCTemper(temper, new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                hideProgress();
                if (isSuccess) {
                    CDialog.showDlg(Main1QActivity.this, message);
                } else {
                    CDialog.showDlg(Main1QActivity.this, message);
                }
            }
        });
    }


    /**
     * 녹십자 라이브러리 Push 알람설정
     * @param type
     * @param isEnable
     */
    private void settingAlramService(GCAlramType type, boolean isEnable) {
        final GCTemperLib gcLib = new GCTemperLib(this);

        showProgress();
        gcLib.settingAlramService(type, isEnable, new IGCResult() {
            @Override
            public void onResult(boolean isSuccess, String message, Object data) {
                hideProgress();
                if (isSuccess) {
                    CDialog.showDlg(Main1QActivity.this, message);
                } else {
                    CDialog.showDlg(Main1QActivity.this, message);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri linkValueUrl = intent.getData();

        if (linkValueUrl != null) {   // Web으로 부터 실행
            Log.i("Main1QActivity", "Check LinkData - bt : " + linkValueUrl.getQueryParameter("bt"));
            final GCTemperLib gcLib = new GCTemperLib(this);
            gcLib.registGCTemper(linkValueUrl.getQueryParameter("bt"), new IGCResult() {
                @Override
                public void onResult(boolean isSuccess, String message, Object data) {
                    if(isSuccess) {
                        gcLib.startGCMainActivity();
                    }
                }
            });

        }
    }

    private void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (mProgress.getVisibility() == View.VISIBLE)
            hideProgress();
        else
            super.onBackPressed();
    }

}
