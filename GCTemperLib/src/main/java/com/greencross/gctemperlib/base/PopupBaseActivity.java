package com.greencross.gctemperlib.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.util.Util;


/**
 * Created by jihoon on 2016-04-08
 * 팝업 부모 클래스
 * @since 0, 1
 */
public class PopupBaseActivity extends BaseActivity {

    TextView titleTextView;
    ImageView closeImg;
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
     * 종료 이미지 변경
     * @param res   이미지 파일
     */
    public void setCloseImg(int res){
        closeImg.setImageResource(res);
    }

    /**
     * 종료 이미지 반환
     * @return closeImg
     */
    public ImageView getCloseImg(){
        return closeImg;
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
        titleTextView	=	(TextView)	findViewById(R.id.common_title_tv);
        mBgLayout       =   (RelativeLayout)    findViewById(R.id.common_bg_layout);
        closeImg = (ImageView) findViewById(R.id.close_img);

        closeImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        Util.PopupAnimationEnd(PopupBaseActivity.this);
    }

}
