package com.greencross.gctemperlib.common;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by jihoon on 2016-03-21.
 * 네트워크리스너 인터페이스
 * @since 0, 1
 */
public abstract class CustomAsyncListener {

    public abstract void onNetworkError(final Context context, int type, int httpResultCode, CustomAlertDialog dialog);
    public abstract void onDataError(final Context context, int type, String resultData, CustomAlertDialog dialog);
    public abstract void onPost(final Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog);
    public void onStop(final Context context, int type) {

    }

}

