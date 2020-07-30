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


    // 테스트 도메인
    private String REAL_DEF_DOMAIN = "https://wkd.walkie.co.kr/HL_FV/HL_Mobile_Call.asmx/mobile_Call";    //  api url

    // 열나요 도메인
    private String REAL_DOMAIN_FEVER = "https://api.devgc.com/hana/Fever/v1/MapList";
    //private String REAL_DOMAIN_FEVER_SERVER             = "http://ec2-52-78-56-157.ap-northeast-2.compute.amazonaws.com/api/feverAPI.php";
    //ssshin 2018.12.12 도메인 변경
    private String REAL_DOMAIN_FEVER_SERVER = "https://fevermanager.com/api/feverAPI.php";

    private int market_id = 1;                        // 1 = GOOGLE_MARKET, 2 = TSTORE_MARKET, 3 = NAVER_MARKET

    /**
     * API 도메인 주소
     *
     * @return
     */
    public String getDefDomain() {
        return REAL_DEF_DOMAIN;
    }

    /**
     * 열나요 API 도메인 주소
     *
     * @return
     */
    public String getFeverDomain() {
        return REAL_DOMAIN_FEVER;
    }

    /**
     * 열나요 서버 API 도메인 주소
     *
     * @return
     */
    public String getFeverServerDomain() {
        return REAL_DOMAIN_FEVER_SERVER;
    }


    public int getMarketId() {
        return market_id;
    }


    /**
     * API
     */
    private String DEF_GET_APP_INFO = "AppInfo/getVer";


    //웹뷰 url


    /**
     * handler return ID
     */
    public static final int NET_EMAIL_LOGIN = 2;
    public static final int NET_JOIN_SNS = 3;
    public static final int NET_GET_APP_INFO = 5;

    public static final int NET_CALL_REG_FX_ADD = 12;
    public static final int NET_CHLDRN_NOTE_UPLOAD = 53; // 육아 & 병력 기록하기
    public static final int NET_GROWTH_LAST_DATA = 64;    // 마지막 성장 데이터
    public static final int NET_DISEASE_INFO = 65;    // 유해질환 순위 및 위치정보

    public static final int NET_FEVER_LIST = 70; // 체온 리스트
    public static final int NET_FEVER_INPUT = 71; // 체온 입력수정삭제
    public static final int NET_REMEDY_LIST = 72; // 해열제 리스트
    public static final int NET_REMEDY_INPUT = 73; // 해열제 입력수정삭제
    public static final int NET_FEVER_RESULT_INPUT = 75; // 체온 입력 결과 입력수정삭제

    public static final int NET_ALL_LIST = 76; // 모든 정보 리스트

    public static final int NET_MEMO_INPUT = 77; // 메모류 입력
    public static final int NET_GET_EPIDEMIC = 79; // 유행주의보 데이터 가져오기
    public static final int NET_GET_MAP_DATA = 80; // 유행주의보 데이터 가져오기

    public static final int NET_GET_BBS_LIST = 82; // 게시판 리스트 가져오기
    public static final int NET_MBER_USER_HEAT_AREA_ADD = 83; // 유저 위치 정보 리스트 가져오기

    //hsh
    public static final int NET_GET_ALARM_POP = 86; // 메인화면 알람 팝업


    /* 아이 심리 */

    /* 엄마건강 */
    public static final int NET_MOTHER_REVC_GOAL = 101;

    public static final int NET_ASSTB_KBTG_ALIMI_VIEW_ON = 128;    // 마지막 성장 데이터

    public static final int NET_ASSTB_MBER_CNTR_BDHEAT = 131;    // 아이체온 현황 공유

    public static final int NET_MBER_CHECK_AGREE_YN = 137;// 마케팅, 위치정보 동의 여부
    public static final int NET_NOTICE_MAIN_POP_YN = 138;// 공지사항 팝업


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
