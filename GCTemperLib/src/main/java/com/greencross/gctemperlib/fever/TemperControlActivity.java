package com.greencross.gctemperlib.fever;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.GCTemperLib;
import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.greencare.weight.WeightManageFragment;

public class TemperControlActivity extends BackBaseActivity {
    private Button mMapBtn, mEpidemicBtn;

    private LinearLayout mLinearTabMap, mLinearTabEpidemic;

    private LinearLayout mEpidemicRankLay;
    private TextView mTxtNullEpidemic;
    private LinearLayout[] mEpRankList;
    private TextView[] mTxtDiseNmList, mTxtDiseCntList;

    private  ImageButton mBtnAlarm, mBtnShare;


    private View view;

    private ImageView req_diease_btn;

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

        setContentView(R.layout.fever_control_activity);
        setTitle(getString(R.string.fever_map));


        findViewById(R.id.go_graph_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyActivity.startActivity(TemperControlActivity.this, WeightManageFragment.class, new Bundle());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
