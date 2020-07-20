package com.greencross.gctemperlib.greencare.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.greencare.base.value.Define;

/**
 * Created by MrsWin on 2017-02-16.
 */

public class CommonActionBar {
    private final String TAG = CommonActionBar.class.getSimpleName();

    public static String ACTION_BAR_TITLE = "action_bar_title";

    private AppCompatActivity mActivity;

    protected ImageButton mActionBackBtn, mActionSettingBtn, mActionDeviceBtn, mActionBluetoothBtn, mActionWriteBtn, mActionMediBtn, mActionWriteStepBtn, mActionBandStepBtn;
    protected Button mActionSaveBtn;

//    private ActionBar mActionBar;
    private RelativeLayout mActionBarLayout;
    private View mBlockView;
    private View mUnerlineView;
    private Toolbar mToolbar;
    private TextView mTitleTxtView;

    public CommonActionBar(AppCompatActivity activity, Toolbar toolbar) {
        mActivity = activity;
        createActionBar(toolbar);
    }

    private void createActionBar(Toolbar toolBar) {
        Log.i(TAG, "createActionBar");
//        mActionBar = mActivity.getSupportActionBar();
//
//        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
//        mActionBar.setDisplayShowCustomEnabled(true);
//        mActionBar.setDisplayHomeAsUpEnabled(false);         //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
//        mActionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
//        mActionBar.setDisplayShowHomeEnabled(false);         //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.

//        mActionBarLayout = toolBar.findViewById(R.id.action_bar_layout);
//
//        mBlockView = toolBar.findViewById(R.id.action_bar_block_layout);
//        mUnerlineView = toolBar.findViewById(R.id.action_bar_underline_layout);
//        mTitleTxtView = (TextView) toolBar.findViewById(R.id.action_bar_title_textview);
//        mActionBackBtn = (ImageButton) toolBar.findViewById(R.id.action_bar_back_button);
//        mActionDeviceBtn = (ImageButton) toolBar.findViewById(R.id.action_bar_device_btn);
//        mActionBluetoothBtn = (ImageButton) toolBar.findViewById(R.id.action_bar_bluetooth_btn);
//        mActionSettingBtn = (ImageButton) toolBar.findViewById(R.id.action_bar_setting_btn);
//        mActionWriteBtn = (ImageButton) toolBar.findViewById(R.id.action_bar_write_btn);
//        mActionWriteStepBtn = (ImageButton) toolBar.findViewById(R.id.action_bar_write_step_btn);
//        mActionBandStepBtn = (ImageButton) toolBar.findViewById(R.id.action_bar_band_step_btn);
//        mActionMediBtn = (ImageButton) toolBar.findViewById(R.id.action_bar_medi_btn);
//        mActionSaveBtn = (Button) toolBar.findViewById(R.id.action_bar_save_btn);
//
//        mActionBackBtn.setOnClickListener(actionBarClickListener);
//        mActionSettingBtn.setOnClickListener(actionBarClickListener);
//        mActionWriteBtn.setOnClickListener(actionBarClickListener);
//        mActionMediBtn.setOnClickListener(actionBarClickListener);
//        mActionSaveBtn.setOnClickListener(actionBarClickListener);
//        mActionWriteStepBtn.setOnClickListener(actionBarClickListener);
//        mActionBandStepBtn.setOnClickListener(actionBarClickListener);

//        mActionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
//        Toolbar parent = (Toolbar) toolBar.getParent();
//        parent.setContentInsetsAbsolute(0, 0);
//        parent.setAnimation(null);

    /**
     * 액션바 오렌지 바탕색 테마
     */
//    public void setOrangeTheme() {
//        mActionBarLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorMain));
//        mTitleTxtView.setTextColor(ContextCompat.getColor(mActivity, R.color.colorWhite));
//        mActionBackBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.btn_back));
    }

    /**
     * 액션바 화이트 배경 테마
     */
    public void setWhiteTheme() {
//        mActionBarLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorWhite));
//        mTitleTxtView.setTextColor(ContextCompat.getColor(mActivity, R.color.colorMain));
//        mActionBackBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.btn_back1));
//        mUnerlineView.setVisibility(View.VISIBLE);

    }

    View.OnClickListener actionBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
//            if (R.id.action_bar_back_button == vId) {
//                mActivity.onBackPressed();
//            }
        }
    };

    public void setVisibility(int visibility) {
        if (mToolbar != null) {
            mToolbar.setVisibility(visibility);

        }
    }


//    public void showActionBar(boolean isShow) {
//        if (mActionBar != null) {
//            if (isShow) {
//                mActionBar.show();
//            } else {
//                mActionBar.hide();
//            }
//        }
//    }

    /**
     * 액션바 타이틀 세팅
     *
     * @param title
     */
    public TextView setActionBarTitle(String title) {
//        View v = mActivity.getSupportActionBar().getCustomView();
//        TextView titleTxtView = (TextView) v.findViewById(R.id.action_bar_title_textview);

//        Fragment fragment = mActivity.getVisibleFragment();
//        if (fragment != null) {
//            if (fragment.getArguments() != null) {
//                // 플래그먼트 이동시 세팅해준 타이틀명
//                String bundleTitle = fragment.getArguments().getString(CommonActionBar.ACTION_BAR_TITLE);
//                mTitleTxtView.setText(bundleTitle == null ? title : bundleTitle);
//            } else {
//                mTitleTxtView.setText(title);
//            }
//        } else {
//            mTitleTxtView.setText(title);
//        }

        // 테스트일때 샘플코드로 이동
//        if (Logger.mUseLogSetting) {
//                mTitleTxtView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((BaseActivity) mActivity).replaceFragment(SampleFragment.newInstance());
//                }
//            });
//        }
        return mTitleTxtView;
    }




    public void setActionBarSettingBtn(boolean isShow, View.OnClickListener clickListener) {
        mActionSettingBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mActionSettingBtn.setOnClickListener(clickListener);
    }

    int mRequestCode = 1111;


    /**
     * 입력버튼
     *
     * @param isShow
     * @param clickListener
     */
    public void setActionBarStepWriteBtn(boolean isShow, View.OnClickListener clickListener) {
        mActionWriteStepBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mActionWriteStepBtn.setOnClickListener(clickListener);
    }

    public void setActionBarStepBandBtn(boolean isShow, View.OnClickListener clickListener) {
        mActionBandStepBtn.setVisibility(View.GONE);
        mActionBandStepBtn.setOnClickListener(clickListener);
    }

    public void setActionBarWriteBtn(boolean isShow, View.OnClickListener clickListener) {
        mActionWriteBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mActionWriteBtn.setOnClickListener(clickListener);
    }

    public void setActionBarMediBtn(boolean isShow, View.OnClickListener clickListener) {
        mActionMediBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mActionMediBtn.setOnClickListener(clickListener);
    }

    public Button setActionBarSaveBtn(View.OnClickListener clickListener) {
        mActionSaveBtn.setVisibility(View.VISIBLE);
        mActionSaveBtn.setOnClickListener(clickListener);
        return mActionSaveBtn;
    }

    public Button setActionBarSaveBtn(String label, View.OnClickListener clickListener) {
        mActionSaveBtn.setVisibility(View.VISIBLE);
        mActionSaveBtn.setOnClickListener(clickListener);
        mActionSaveBtn.setText(label);
        return mActionSaveBtn;
    }

    public void setActionBar(Define.ACTION_BAR type, String title, View.OnClickListener clickListener1, View.OnClickListener clickListener2) {
        setActionBarTitle(title);
        if (type == Define.ACTION_BAR.NO_RIGHT_MENU) {
            // 오른쪽 버튼 모두 안나오게
            setActionBarSettingBtn(false, null);
            setActionBarWriteBtn(false, null);
        } else if (type == Define.ACTION_BAR.RIGHT_MENU1) {
            // 오른쪽 버튼 하나만 나오게
            setActionBarSettingBtn(true, clickListener1);
            setActionBarWriteBtn(false, null);
        } else if (type == Define.ACTION_BAR.RIGHT_MENU2) {
            // 오른쪽 버튼 두개 나오게
            setActionBarSettingBtn(true, clickListener1);
            setActionBarWriteBtn(true, clickListener2);
        }
    }

    /**
     * 액션바 Progress Visible 일때 block 처리
     *
     * @param isVisible
     */
    public void showBlockLayout(boolean isVisible) {
        mBlockView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
