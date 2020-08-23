package com.greencross.gctemperlib.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.greencross.gctemperlib.BuildConfig;
import com.greencross.gctemperlib.util.GLog;


/**
 * Created by jihoon on 2016-03-21.
 * 네트워크 공유 클래스
 *
 * @since 0, 1
 */
public class NetworkConst {
    private static NetworkConst _instance;

    private Context mContext;
    private PackageInfo pi;


    /**
     * 접속 도메인
     */
    private int market_id = 1;                        // 1 = GOOGLE_MARKET, 2 = TSTORE_MARKET, 3 = NAVER_MARKET


    public int getMarketId() {
        return market_id;
    }


    /**
     * handler return ID
     */
    public static final int NET_GET_EPIDEMIC = 79; // 유행주의보 데이터 가져오기

    public static final int NET_MBER_CHECK_AGREE_YN = 137;// 마케팅, 위치정보 동의 여부


    /**
     * 네트워크 구성 인스턴스 가져오기
     *
     * @return NetworkConst
     */
    public static NetworkConst getInstance() {
        if (_instance == null) {
            synchronized (NetworkConst.class) {
                if (_instance == null) {
                    _instance = new NetworkConst();
                }
            }

        }
        return _instance;
    }

    public void setContext(Context context) {
        this.mContext = context;
        try {
            pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            GLog.e(e.toString());
        }
    }

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
