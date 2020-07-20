package com.greencross.gctemperlib.setting;

import android.content.Context;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.viewpagerindicator.CirclePageIndicator;

import com.greencross.gctemperlib.base.PopupBaseActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.R;

/**
 * Created by jihoon on 2016-04-11.
 * 서비스 소개 화면
 * @since 0, 1
 */
public class AboutServiceActivity extends PopupBaseActivity implements View.OnClickListener{

    public static final int ITEM_COUNT  = 6;

    private ViewPager mViewPager;
    private AboutServiceActivity.CustomAdapter mViewPagerAdapter;
//    private CirclePageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_service_activity);

        setBgColor(ContextCompat.getColor(AboutServiceActivity.this, R.color.color_100ffffff));

        init();
    }

    /**
     * 초기화
     */
    public void init(){

        mViewPager           =  (ViewPager) findViewById(R.id.login_viewpager);

        mViewPagerAdapter   =   new CustomAdapter(AboutServiceActivity.this);
//        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);


        mViewPager.setAdapter(mViewPagerAdapter);
//        mIndicator.setViewPager(mViewPager);
//
//
//        mIndicator.setFillColor(ContextCompat.getColor(AboutServiceActivity.this, R.color.bg_blue_light));  // viewpager 현재 페이지 아이콘
//        mIndicator.setPageColor(ContextCompat.getColor(AboutServiceActivity.this, R.color.color_100ffffff));  // viewpager 전체 페이지 아이콘


    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){
    }

    @Override
    public void onClick(View v) {
        GLog.i("onClick", "dd");
    }


    /**
     * ViewPager 어댑터
     */
    public class CustomAdapter extends PagerAdapter {

        LayoutInflater mInflater;
        Context mContext;

        public CustomAdapter(Context context){
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return ITEM_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = null;

            final ImageView mViewpagerImg;

            view = mInflater.inflate(R.layout.prologue_viewpager_item, null);

            mViewpagerImg =   (ImageView) view.findViewById(R.id.photo_img);

            switch(position){
                case 0: // 첫번째 프롤로그 화면
                    mViewpagerImg.setImageResource(R.drawable.intro_01);
                    break;
                case 1: // 두번째 프롤로그 화면
                    mViewpagerImg.setImageResource(R.drawable.intro_02);
                    break;
                case 2: // 세번째 프롤로그 화면
                    mViewpagerImg.setImageResource(R.drawable.intro_03);
                    break;
                case 3: // 세번째 프롤로그 화면
                    mViewpagerImg.setImageResource(R.drawable.intro_04);
                    break;
                case 4: // 네번째 프롤로그 화면
                    mViewpagerImg.setImageResource(R.drawable.intro_05);
                    break;
                case 5: // 다섯번째 프롤로그 화면
                    mViewpagerImg.setImageResource(R.drawable.intro_06);
                    break;
            }

            ((ViewPager) container).addView(view, 0);

            return view;
        }
    }
}