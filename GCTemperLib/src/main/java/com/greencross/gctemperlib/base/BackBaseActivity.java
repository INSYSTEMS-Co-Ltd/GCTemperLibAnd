package com.greencross.gctemperlib.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.util.Util;


/**
 * Created by jihoon on 2016-03-28.
 * 뒤로가기 부모 클래스
 * @since 0, 1
 */
public class BackBaseActivity extends BaseActivity {
    protected final String TAG = getClass().getSimpleName();

    TextView titleTextView;
    ImageView backImg;
    Button mCompleteBtn;
    RelativeLayout mBgLayout;

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        intent = getIntent();
    }

    /**
     * 제목 설정
     * @param title 제목 문구
     */
    public void setTitle(CharSequence title) {
        if ( titleTextView != null )
            titleTextView.setText(title);
    }

    /**
     * 제목 폰트 사이즈
     * @param size 사이즈
     */
    public void setTitleSize(int size){
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    /**
     * 제목 문구 색상 변경
     * @param color 색상
     */
    public void setTitleColor(int color){
        titleTextView.setTextColor(color);
    }

    /**
     * 버튼 설정
     * @param title 버튼 문구
     */
    public void setCompleteTitle(CharSequence title){
        if(mCompleteBtn != null)
            mCompleteBtn.setText(title);
    }

    public void setCompleteTitleColor(int color){
        mCompleteBtn.setTextColor(color);
    }

    /**
     * 네비바 색상 설정
     * @param color
     */
    public void setBgColor(int color){
        mBgLayout.setBackgroundColor(color);
    }

    /**
     * 네비바 높이 설정
     * @param height
     */
    public void setBgHeight(int height){
        mBgLayout.getLayoutParams().height = height;
    }

    /**
     * 뒤로가기 이미지 반환
     * @return  backimg;
     */
    public ImageView getBackImg(){
        return backImg;
    }

    /**
     * 오른쪽 버튼
     * @return button
     */
    public Button getCompleteBtn(){
        return mCompleteBtn;
    }

    /**
     * 백그라운드 레이아웃 반환
     * @return mBgLayout
     */
    public RelativeLayout getmBgLayout(){
        return mBgLayout;
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
        titleTextView	=	(TextView)	findViewById(R.id.common_title_tv);
        backImg			=	(ImageView)	findViewById(R.id.common_left_btn);
        mCompleteBtn    =   (Button)    findViewById(R.id.common_right_btn);
        mBgLayout       =   (RelativeLayout) findViewById(R.id.common_bg_layout);


        backImg.setOnClickListener(v -> finish());

    }

//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        // TODO Auto-generated method stub
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }


    @Override
    public void finish() {
        Util.BackAnimationEnd(this);	// Activity 종료시 뒤로가기 animation
        super.finish();
    }

}

