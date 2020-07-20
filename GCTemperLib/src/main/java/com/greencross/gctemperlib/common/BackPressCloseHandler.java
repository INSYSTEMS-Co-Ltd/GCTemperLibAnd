package com.greencross.gctemperlib.common;

import android.app.Activity;
import android.widget.Toast;

import com.greencross.gctemperlib.R;

/**
 * Created by jihoon on 2016-03-21.
 * 앱 종료 감지 클래스
 * @since 0, 1
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    /**
     * 앱 종료 감지 클래스 생성자
     * @param context context
     */
    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    /**
     * 뒤로가기 클릭 이벤트
     */
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {

            activity.finish();
            toast.cancel();
        }
    }

    /**
     * 안내 토스트 문구
     */
    public void showGuide() {
        toast = Toast.makeText(activity, activity.getString(R.string.toast_app_exit), Toast.LENGTH_SHORT);
        toast.show();
    }
}
