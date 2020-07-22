package com.greencross.gctemperlib.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CommonView;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.network.RequestAsyncNetwork;
import com.greencross.gctemperlib.util.GLog;


import java.util.ArrayList;


/**
 * Created by jihoon on 2016-03-21.
 * activity 부모 클래스
 *
 * @since 0, 1
 */
public class BaseActivity extends AppCompatActivity {

    // 테스트
    public static ArrayList<Activity> actList = new ArrayList<Activity>();    // 엑티비티 리스트 저장

    public RequestAsyncNetwork mRequestAsyncNetwork = null; // 네트워크

    private boolean mButtonClickEnabled = true;  // 버튼 클릭 유무
    private Handler mButtonClickEnabledHandler = new Handler();

    boolean isShow = true;

    public LayoutInflater mLayoutInflater;
    public RelativeLayout mProgressLayout;

    public CommonData commonData;// = CommonData.getInstance();
    public CommonView commonView = CommonView.getInstance();
    public CustomAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        commonData = CommonData.getInstance(this);
        actList.add(this);

        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 브로드캐스트 등록
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(CommonData.BROADCAST_ACTIVITY_FINISH);
//        registerReceiver(mBroadcastReceiver, filter);
        GLog.i("방송 등록", "dd");
    }

    /**
     * 클릭 이벤트 중복 방지
     *
     * @param enabled true = 클릭 가능, false = 클릭 불가
     */
    public void setButtonClickEnabled(boolean enabled) {

        // 클릭을 활성화 할 때 0.5초의 delay가 있도록 한다. (연속클릭 방지 목적)
        if (enabled)
            mButtonClickEnabledHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    GLog.i("button click enable", "dd");
                    mButtonClickEnabled = true;
                }
            }, 500);
        else {
            GLog.i("button click disable", "dd");
            mButtonClickEnabled = false;
        }

    }

    /**
     * 클릭 이벤트 활성 여부 가져오기
     *
     * @return true = 클릭 가능, false = 클릭 불가
     */
    public boolean getButtonClickEnabled() {
        return mButtonClickEnabled;
    }

    public RelativeLayout getProgressLayout() {
        if (mProgressLayout != null) {
            return mProgressLayout;
        }
        return null;
    }

    @Override
    public void setContentView(int layoutResID) {
        //super.setContentView(layoutResID);

        View view = mLayoutInflater.inflate(layoutResID, null);
        View loadingView = mLayoutInflater.inflate(R.layout.dialog_progress_layout, null);
        ((ViewGroup) view).addView(loadingView);

        mProgressLayout = (RelativeLayout) loadingView.findViewById(R.id.progressbar_layout);
        // 레이아웃에 클릭 이벤트를 줘서 아래 버튼들이 눌리지 않도록 한다.
        mProgressLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                return;
            }
        });

        mProgressLayout.setVisibility(View.INVISIBLE);


        setContentView(view);
    }

    /**
     * 실행된 activity 모두 메모리 해제
     */
    public void activityClear() {
        for (int i = 0; i < actList.size() - 1; i++) {
            Activity activity = actList.get(i);

            if (activity != null)
                activity.finish();
        }
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
        hideProgress();
        GLog.i("onStop", "dd");

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    /**
     * 네트워크 대기중 로딩 animation 활성화
     */
    public void showProgress() {

//		if ( mProgress == null )
//			mProgress = new CFMakeProgress(this);
//
//		if ( mProgress.isShowing() )
//			return;
//
//		try {
//			if ( isShow )
//				mProgress.show();
//		}
//		catch ( Exception e ) {
//			e.getStackTrace();
//		}
        GLog.i("mProgressLayout = " + mProgressLayout, "dd");
        if (mProgressLayout != null)
            mProgressLayout.setVisibility(View.VISIBLE);

    }

    /**
     * 네트워크 완료후 로딩 animation 비활성화
     */
    public void hideProgress() {

//		try {
//			if ( mProgress == null )
//				return;
//
//			if ( mProgress.isShowing() && isShow )
//				mProgress.dismiss();
//		}
//		catch ( Exception e ) {
//			e.getStackTrace();
//		}
        if (mProgressLayout != null)
            mProgressLayout.setVisibility(View.GONE);

    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        if ( mBroadcastReceiver != null )
//            unregisterReceiver(mBroadcastReceiver);
//        if ( mProgressLayout != null)
//            mProgressLayout = null;
//
//        super.onDestroy();
//    }

    public void setContentView(int layoutResID, Context mContext) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
    }

//    /**
//     * 액티비티 종료 방송 리스너
//     */
//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            GLog.i(action, "dd");
//            if ( action.equals(CommonData.BROADCAST_ACTIVITY_FINISH)) {
//                GLog.i("receive Broadcast activity finish", "dd");
//                finish();
//            }
//
//        }
//    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    //    @Override protected void attachBaseContext(Context newBase) {
//        // // super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
//    }


    protected onKeyBackPressedListener onKeyBackPressedListener;
    protected onKeyBackCatchListener onKeyBackCatchListener;
    protected onFinishListener onFinishListener;

    public interface onKeyBackPressedListener {
        void onBack();
    }

    public interface onKeyBackCatchListener {
        void onCatch();
    }

    public interface onFinishListener {
        void catchFinish();
    }

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        onKeyBackPressedListener = listener;
    }

    public void setonKeyBackCatchListener(onKeyBackCatchListener listener) {
        onKeyBackCatchListener = listener;
    }

    public void setOnFinishListener(onFinishListener listener) {
        onFinishListener = listener;
    }

}
