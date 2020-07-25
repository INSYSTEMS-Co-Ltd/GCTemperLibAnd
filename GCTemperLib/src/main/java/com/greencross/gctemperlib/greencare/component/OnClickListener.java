package com.greencross.gctemperlib.greencare.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

//import com.greencross.gctemperlib.greencare.database.DBHelperLog;

import java.util.Timer;

/**
 * Created by mrsohn on2017. 3. 21..
 */
@SuppressLint("AppCompatCustomView")
public class OnClickListener implements View.OnTouchListener {
    private static final String TAG = OnClickListener.class.getSimpleName();
    private Timer mTimer;
    private Context mContext;


    public OnClickListener(View.OnClickListener clickListener, View v, Context context){
        if (clickListener != null)
            clickListener.onClick(v);

        mContext = context;
    }

    public void getTimer(Timer timer){
        mTimer = timer;
    }

    @Override
    public boolean onTouch(View v, MotionEvent Event) {
        if (Event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (mFragment instanceof PMainFragment) {
//                ((PMainFragment) mFragment).timerStop(mTimer);
//            }
            Log.i(TAG,"ACTION_DOWN");
        } else  if (Event.getAction() == MotionEvent.ACTION_MOVE) {
//            if (mFragment instanceof PMainFragment) {
//                ((PMainFragment) mFragment).setAutoSwiper();
//            }
        } else if (Event.getAction() == MotionEvent.ACTION_UP) {
//            if (mFragment instanceof PMainFragment) {
//                ((PMainFragment) mFragment).setAutoSwiper();
//            }

//            if (v.getContentDescription() != null && !v.getContentDescription().toString().equals("")) {
//                // _!: 카운트만 되는 버튼 끝에 붙임
//                if (v.getContentDescription().toString().contains("_!")) {
//                    String temp = v.getContentDescription().toString().replace("_!", "");
//                    String cod[] = temp.split("_");
//
//                    DBHelper helper = new DBHelper(mContext);
//                    DBHelperLog logdb = helper.getLogDb();
//
//                    if (cod.length == 1) {
//                        logdb.insert(cod[0], "", "", 0, 1);
//                        Log.i(TAG, "view.contentDescription : " + cod[0] + "count : 1");
//                    } else if (cod.length == 2) {
//                        logdb.insert(cod[0], cod[1], "", 0, 1);
//                        Log.i(TAG, "view.contentDescription : " + cod[0] + cod[1] + "count : 1");
//                    } else {
//                        logdb.insert(cod[0], cod[1], cod[2], 0, 1);
//                        Log.i(TAG, "view.contentDescription : " + cod[0] + cod[1] + cod[2] + "count : 1");
//                    }
//
//                }
//                Log.i(TAG,"ACTION_UP");
//            }
        }
        return false;
    }
}