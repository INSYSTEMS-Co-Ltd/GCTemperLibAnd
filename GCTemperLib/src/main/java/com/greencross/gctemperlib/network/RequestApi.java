package com.greencross.gctemperlib.network;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Hashtable;

import cz.msebera.android.httpclient.NameValuePair;

import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.MakeProgress;
import com.greencross.gctemperlib.util.GLog;


/**
 * Created by jihoon on 2016-01-05.
 * api 요청 클래스
 * @since 0, 1
 */
public class RequestApi {

    /**
     * 모든 API 요청 ( Activity 호출 )
     * @param context   컨텍스트
     * @param networkType   api 요청 타입 ( int )
     * @param url   api 주소
     * @param networkListener   네트워크 응답 리스너
     * @param params    요청 파라미터
     */
    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, ArrayList<NameValuePair> params, RelativeLayout progress){
        if(progress != null) {
            GLog.i("progress = not null ", "dd");
            progress.setVisibility(View.VISIBLE);   // api 호출중에 프로그래스바 활성화
        }
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params);
        requestAsyncNetwork.start();

    }

    /**
     * 모든 API 요청 ( Fragment 호출 )
     * @param context   컨텍스트
     * @param networkType   api 요청 타입 ( int )
     * @param url   api 주소
     * @param networkListener   네트워크 응답 리스너
     * @param params    요청 파라미터
     */
    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, ArrayList<NameValuePair> params, MakeProgress progress){
        if(progress != null) {
       //     progress.show();   // api 호출중에 프로그래스바 활성화
        }
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params);
        requestAsyncNetwork.start();
    }

    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, ArrayList<NameValuePair> params, MakeProgress progress, boolean visible){
        if(progress != null) {
            progress.show();   // api 호출중에 프로그래스바 활성화
        }
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params);
        requestAsyncNetwork.start();
    }

    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, Hashtable<String, String> table, RelativeLayout progress){
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, table);
        requestAsyncNetwork.start();
        if(progress != null)
            progress.setVisibility(View.VISIBLE);   // api 호출중에 프로그래스바 활성화
    }
}
