package com.greencross.gctemperlib.Alram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.base.BaseActivity;
import com.greencross.gctemperlib.common.MakeProgress;
import com.greencross.gctemperlib.greencare.alram_fragment.AlramMainFragment;

public class AlramMainActivity extends BaseActivity implements View.OnClickListener {

    private Intent mIntent;

    private View mActionbar;

    private TextView mActionbarTipBtn;

    private Toolbar toolbar;
    // 네비바
    public RelativeLayout mBgActionBar;
    private ImageButton mLefeBtn;
    private RelativeLayout mRightLayout;
    private FrameLayout mBgBabyFace;
    private ImageView mRightImg;
    private TextView mTitleTv;
    private String EVENT_POP = "";



    private MakeProgress mProgress			=	null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alram_main_activity);
        if (mProgress == null)
            mProgress = new MakeProgress(this);

        mIntent = getIntent();
        init();
        setEvent();
        mTitleTv.setText(getString(R.string.noti1));

        Intent intent = getIntent();
        EVENT_POP = intent.getStringExtra("EVENT_POP");
        if (EVENT_POP != null) {

            Bundle bundle = new Bundle();
            bundle.putString("EVENT_POP",EVENT_POP);
            replaceFragment(AlramMainFragment.newInstance(), true, false, bundle);
        } else {
            replaceFragment(AlramMainFragment.newInstance(), true, false, null);
        }
    }

    /**
     * 초기화
     */
    public void init() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_no_main);

        // start custom actionbar leftmargin remove
        View customView = getSupportActionBar().getCustomView();
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        // end custom actionbar leftmargin remove

        mBgActionBar = (RelativeLayout) getSupportActionBar().getCustomView().findViewById(R.id.bg_action_bar);
        mBgActionBar.setBackgroundColor(getResources().getColor(R.color.color_B8B8B8));
        mLefeBtn = (ImageButton) getSupportActionBar().getCustomView().findViewById(R.id.left_btn);
        mRightLayout = (RelativeLayout) getSupportActionBar().getCustomView().findViewById(R.id.right_layout);
        mBgBabyFace = (FrameLayout) getSupportActionBar().getCustomView().findViewById(R.id.bg_baby_face);
        mRightImg = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.photo_img);

        mRightLayout.setVisibility(View.GONE);


        mTitleTv = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.title_tv);

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//        mIndicator = (ImageView) findViewById(R.id.mIndicator);

    }




    /* 타이틀 변경 */
    public void setTitleTxt(String title) {
        mTitleTv.setText(title);
    }

    /**
     * 이벤트 연결
     */
    public void setEvent() {

        mRightLayout.setOnClickListener(this);
        mLefeBtn.setOnClickListener(this);
        mRightImg.setOnClickListener(this);


    }



    //    @Override
    public void onClick(View v) {
        String str = "";
        if (v.getId() == R.id.left_btn) {
            onBackPressed();
        }

        if (!str.equals("")) {
            mTitleTv.setText(str);
        }
    }
    public void replaceFragment(final Fragment fragment, final boolean isReplace, boolean isAnim, Bundle bundle) {

        android.util.Log.i("MotherMainA", "replaceFragment: " + fragment);

//        BaseFragment.newInstance(this);
//        if (isReplace)
//            removeAllFragment();

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (isAnim)
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left  , R.anim.slide_in_left, R.anim.slide_out_right);
        if (bundle != null)
            fragment.setArguments(bundle);

        transaction.replace(R.id.alram_main_content_layout, fragment, fragment.getClass().getSimpleName());
        if (!isFinishing()) {
            if (isReplace == false)
                transaction.addToBackStack(null);

            transaction.commit();
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isReplace == false)
                        transaction.addToBackStack(null);

                    transaction.commitAllowingStateLoss();
                }
            }, 100);
        }
    }

    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
