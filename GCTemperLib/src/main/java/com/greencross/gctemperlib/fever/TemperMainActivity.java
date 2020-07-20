package com.greencross.gctemperlib.fever;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.AllDataItem;
import com.greencross.gctemperlib.collection.FeverItem;
import com.greencross.gctemperlib.collection.FeverResultItem;
import com.greencross.gctemperlib.collection.RemedyItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.intro.IntroActivity;


import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MobileDoctor on 2017-02-28.
 */
public class TemperMainActivity extends AppCompatActivity {

    public static Activity FEVER_MAIN_ACTIVITY;

    public static final int THEME_ORANGE = 0;
    public static final int THEME_YELLOW = 1;

    public LayoutInflater mLayoutInflater;
    public Fragment mContentFragment;
    public FrameLayout mFrameContainer;

//    private Toolbar toolbar;

    private Intent intent = null;
    /* 2016-04-28 테스트 로그 확인용 주석
    public static TextView mDefaultTv;
    */

    // 네비바
//    public RelativeLayout mBgActionBar;
//    private ImageButton mLefeBtn;
//    private RelativeLayout mRightLayout;
//    private ImageView mRightImg;
//    private TextView mTitleTv;

    public static ArrayList<FeverItem> mFeverItems;
    public static ArrayList<FeverResultItem> mFeverResultItems;
    public static ArrayList<RemedyItem> mRemedyItems;
    public static ArrayList<AllDataItem> mAllDataItems;

    // 해열제 하루 허용치
    public static double max_reducer_1 = 0f;	// 아세트아미노펜
    public static double max_reducer_2 = 0f;	// 이부프로펜
    public static double cur_reducer_1 = 0f;	// 아세트아미노펜
    public static double cur_reducer_2 = 0f;	// 이부프로펜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fever_main_activity);

        FEVER_MAIN_ACTIVITY = TemperMainActivity.this;
        mLayoutInflater	=	(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        init();
        setEvent();

//        mTitleTv.setText(getString(R.string.title_fever));

        intent = getIntent();

        mFrameContainer.postDelayed(new Runnable() { // introbaseactivity 에서 db 쿼리 소요시간을 감안하여 살짝 딜레이
            @Override
            public void run() {
                switchContent(new FeverMainFragment());
            }
        }, CommonData.ANI_DELAY_500);

        // 자녀 데이터가 있는경우 UI 세팅
//        if(!MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn().equals("")) {
//
//            if(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlExistYn().equals(CommonData.NO)) {
////                mRightImg.setImageResource(R.drawable.main_fetus06b);
//            } else {
////                CustomImageLoader.displayImage(FeverMainActivity.this, MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnOrgImage(), mRightImg);
//            }
//
//
////            mRightLayout.setVisibility(View.VISIBLE);
//        }else{
////            mRightLayout.setVisibility(View.GONE);
//        }
    }

    /**
     * 초기화
     */
    public void init(){
//        toolbar = (Toolbar) findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);
//        actionBar = getSupportActionBar();
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.custom_actionbar_no_main);

        // start custom actionbar leftmargin remove
//        View customView = getSupportActionBar().getCustomView();
//        Toolbar parent =(Toolbar) customView.getParent();
//        parent.setContentInsetsAbsolute(0, 0);
        // end custom actionbar leftmargin remove

        mFrameContainer = (FrameLayout)findViewById(R.id.frame_container);

//        mBgActionBar = (RelativeLayout)getSupportActionBar().getCustomView().findViewById(R.id.bg_action_bar);
//        mLefeBtn    =   (ImageButton)   getSupportActionBar().getCustomView().findViewById(R.id.left_btn);
//        mRightLayout    =   (RelativeLayout)    getSupportActionBar().getCustomView().findViewById(R.id.right_layout);
//        mRightImg   =   (ImageView)  getSupportActionBar().getCustomView().findViewById(R.id.photo_img);
//
//        mTitleTv    =   (TextView)      getSupportActionBar().getCustomView().findViewById(R.id.title_tv);

        switchActionBarTheme(THEME_ORANGE);

        mFeverItems = new ArrayList<FeverItem>();
        mFeverResultItems = new ArrayList<FeverResultItem>();
        mRemedyItems = new ArrayList<RemedyItem>();
        mAllDataItems = new ArrayList<AllDataItem>();
    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){

//        mRightLayout.setOnClickListener(btnListener);
//        mLefeBtn.setOnClickListener(btnListener);
//        mRightImg.setOnClickListener(btnListener);

    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        GLog.i("onResume", "dd");

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GLog.i("onStop", "dd");

    }

    /**
     * 버튼 클릭 리스너
     */
    View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {

            Intent intent = null;
            String str = "";
            Fragment fragment = null;

            GLog.i("v.getId() = " +v.getId(), "dd");

            int id = v.getId();
            if (id == R.id.left_btn) {
                onBackPressed();
//            } else if (id == R.id.photo_img) {
//                intent = new Intent(TemperMainActivity.this, BabyInfoActivity.class);
//                startActivityForResult(intent, CommonData.REQUEST_CHILD_MANAGE);
//                Util.BackAnimationStart(TemperMainActivity.this);
            }

            if (!str.equals("")) {
//                mTitleTv.setText(str);
            }
        }
    };


    @Override
    public void onBackPressed() {
        if(mOnKeyBackPressedListener != null){
            mOnKeyBackPressedListener.onBack();
            switchActionBarTitle(getString(R.string.title_fever));
        }else{
            finish();
        }
    }

    @Override
    public void finish(){
        super.finish();
        Util.BackAnimationEnd(TemperMainActivity.this);
    }

    public interface onKeyBackPressedListener {
        void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }

    /**
     * Fragment 변경
     * @param fragment  변경할 fragment
     */
    public void switchContent(Fragment fragment){
        mContentFragment = fragment;

        if (fragment != null) {
            GLog.i("fragment != null", "dd");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        } else {
            // error in creating fragment
            GLog.e("Error in creating fragment");
        }
    }

    public void switchActionBarTheme(int theme){
        switch (theme){
            case THEME_ORANGE:
//                mBgActionBar.setBackgroundColor( getResources().getColor(R.color.h_orange));
                break;

            case THEME_YELLOW:
//                mBgActionBar.setBackgroundColor( getResources().getColor(R.color.bg_yellow_light));
                break;
        }
    }

    public void switchActionBarTitle(String title){

//        mTitleTv.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        GLog.i("onNewIntent", "dd");

        if ( CommonData.getInstance(this).getMemberId() == 0 ) {
            GLog.i("CommonData.getInstance(this).getMemberId() == 0", "dd");
            Intent introIntent = new Intent(getApplicationContext(), IntroActivity.class);
            startActivity(introIntent);
            finish();
        }

    }

    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub

            switch ( type ) {
                case NetworkConst.NET_JOIN_SNS:							// SNS 로그인
                case NetworkConst.NET_EMAIL_LOGIN:                                  // 일반계정 로그인
                    GLog.i("aaa", "dd");
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            try {

                            }catch(Exception e){
                                GLog.e(e.toString());
                            }
                    }
                    break;
            }

        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub

            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.

            dialog.show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        GLog.i("requestCode = " +requestCode, "dd");
        GLog.i("resultCode = " +resultCode, "dd");
        GLog.i("data = " + data, "dd");

        if(resultCode != Activity.RESULT_OK){
            return;
        }

        switch(requestCode){
            case CommonData.REQUEST_CHILD_MANAGE:   // 자녀관리
                GLog.i("REQUEST_CHILD_MANAGE", "dd");
                mFrameContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 자녀 데이터가 있는경우 UI 세팅
                        if(!MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlSn().equals("")) {

//                            if(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChlExistYn().equals(CommonData.NO)) {
//                                mRightImg.setImageResource(R.drawable.main_fetus06b);
//                            } else {
//                                CustomImageLoader.displayImage(FeverMainActivity.this, MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnOrgImage(), mRightImg);
//                            }



//                            mRightLayout.setVisibility(View.VISIBLE);
                        }else{
//                            mRightLayout.setVisibility(View.GONE);
                        }
                        setResult(RESULT_OK);
                        switchContent(new FeverMainFragment());
                        switchActionBarTheme(THEME_ORANGE);
                    }
                }, CommonData.ANI_DELAY_500);
                break;

        }

        if(mContentFragment != null) {
            GLog.i("mContentFragment != null", "dd");
            mContentFragment.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


}