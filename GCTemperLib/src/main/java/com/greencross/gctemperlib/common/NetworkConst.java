package com.greencross.gctemperlib.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.greencross.gctemperlib.util.GLog;


/**
 * Created by jihoon on 2016-03-21.
 * 네트워크 공유 클래스
 * @since 0, 1
 */
public class NetworkConst
{
    private static NetworkConst	_instance;

    private Context mContext;
    private PackageInfo pi;


    /**
     * 접속 도메인
     */

    // 개발 도메인
    private String TEST_DOMAIN						=	"";            // 이미지 url
    private String TEST_DEF_DOMAIN					=	"https://wkd.walkie.co.kr/HL_FV/HL_Mobile_Call.asmx/mobile_Call";    // api url

    // 테스트 도메인
    private String REAL_DOMAIN						=	"";    // 이미지 url
    private String REAL_DEF_DOMAIN					=	"https://wkd.walkie.co.kr/HL_FV/HL_Mobile_Call.asmx/mobile_Call";    //  api url

    // 열나요 도메인
    private String REAL_DOMAIN_FEVER                =   "https://wkd.walkie.co.kr/FEVER/ws.asmx/getJson";
    //private String REAL_DOMAIN_FEVER_SERVER             = "http://ec2-52-78-56-157.ap-northeast-2.compute.amazonaws.com/api/feverAPI.php";
    //ssshin 2018.12.12 도메인 변경
    private String REAL_DOMAIN_FEVER_SERVER             = "https://fevermanager.com/api/feverAPI.php";

    //아이 심리
    private String REAL_DOMAIN_PSY                   = "https://wkd.walkie.co.kr/HL_CHL/ws.asmx/getJson";


    private boolean isReal = true;					// true = 실서버 , false = 개발서버 //hsh
    private boolean isDebug = true;				// false = 로그 안나옴,true = 로그 나옴 //hsh

    private int market_id = 1;						// 1 = GOOGLE_MARKET, 2 = TSTORE_MARKET, 3 = NAVER_MARKET
    private int payment_id = 1;						// 0 = UPlus , 1 = INISIS

    /**
     * 앱 실행시 개발 or 리얼서버용 설정
     */
    public void init(){
        if(isReal){
            isReal = true;
            isDebug = false;
        }else{
            isReal = false;
            isDebug = true;
        }

    }

    /**
     * API 도메인 주소
     * @return
     */
    public String getDefDomain() {

        if ( isReal )
            return REAL_DEF_DOMAIN;
        else
            return TEST_DEF_DOMAIN;
    }

    /**
     * 열나요 API 도메인 주소
     * @return
     */
    public String getFeverDomain(){
        return REAL_DOMAIN_FEVER;
    }

    /**
     * 열나요 서버 API 도메인 주소
     * @return
     */
    public String getFeverServerDomain(){
        return REAL_DOMAIN_FEVER_SERVER;
    }

    /**
     * 심리 API 도메인 주소
     * @return
     */
    public String getPsyDomain(){
        return REAL_DOMAIN_PSY;
    }

    /**
     * 이미지 도메인 주소
     * @return
     */
    public String getImgDomain() {

        if ( isReal )
            return REAL_DOMAIN;
        else
            return TEST_DOMAIN;
    }


    public boolean isReal(){
        return isReal;
    }

    public int getMarketId(){
        return market_id;
    }

    public boolean isDebug(){
        return isDebug;
    }

    public int getPayment(){
        return payment_id;
    }


    /**
     * API
     */
    private String DEF_GET_APP_INFO							=   "AppInfo/getVer";

	
    //웹뷰 url



    /**
     * handler return ID
     */
    public static final int NET_EMAIL_LOGIN                     =   2;
    public static final int NET_JOIN_SNS    					=   3;
    public static final int NET_SET_JOIN_STEP                   =   4;
    public static final int NET_GET_APP_INFO	        		=   5;
    public static final int NET_SET_UPLOAD                      =   6;
    public static final int NET_SET_PHOTO                       =   7;
    public static final int NET_API_TEST                        =   8;
    public static final int NET_JOIN_EMAIL                      =   9;

    public static final int NET_GET_INFORMATION                 =   11;
    public static final int NET_CALL_REG_FX_ADD                 =   12;
    public static final int NET_LOGIN                             =   13;
    public static final int NET_MBER_USER_OUT                   =   14;// 회원 탈퇴
    public static final int NET_LOGIN_ID                        =   15;// 아이디 찾기
    public static final int NET_LOGIN_PWD                       =   16;// 비밀번호 찾기
    public static final int NET_MBER_CHECK                      =   17;// 회원 인증 및 계약자 여부
    public static final int NET_MBER_CHLDRN_INPUT               =   18;// 자녀 저장하기
    public static final int NET_MBER_REG_CHECK_ID               =   19;// 아이디 중복 체크 여부
    public static final int NET_MBER_REG_OK                     =   20;// 회원가입
    public static final int NET_CHLDRNCM                        =   22; // 자녀 애칭 변경
    public static final int NET_MBER_USER_PWD_EDIT              =   37; // 비밀번호 변경
    public static final int NET_RECORD_INPUT_OK                =   40; // 성장입력 DB저장
    public static final int NET_RECORD_INPUT                   =   41; // 성장입력
    public static final int NET_RECORD                         =   42; // 성장리스트
    public static final int NET_RECORD_DEL                     =   43; // 성장정보삭제
    public static final int NET_HEIGHT                         =   44; // 예측 키
    public static final int NET_CHLDRN_NOTE_UPLOAD              =   53; // 육아 & 병력 기록하기
    public static final int NET_CHLDRN_GROWTH_FAQ               =   54; // 성장 FAQ
    public static final int NET_RECORD_GRAPH                   =   55; // 성장리스트 그래프
    public static final int NET_CHLDRN_SET_FAQ                  =   63;    // 자주묻는질문
    public static final int NET_GROWTH_LAST_DATA                 =   64;    // 마지막 성장 데이터
    public static final int NET_DISEASE_INFO                 =   65;    // 유해질환 순위 및 위치정보

    public static final int NET_FEVER_LIST                         =   70; // 체온 리스트
    public static final int NET_FEVER_INPUT                         =   71; // 체온 입력수정삭제
    public static final int NET_REMEDY_LIST                         =   72; // 해열제 리스트
    public static final int NET_REMEDY_INPUT                         =   73; // 해열제 입력수정삭제
    public static final int NET_FEVER_RESULT_LIST                   =   74; // 체온 입력 결과 리스트
    public static final int NET_FEVER_RESULT_INPUT                   =   75; // 체온 입력 결과 입력수정삭제

    public static final int NET_ALL_LIST                                =   76; // 모든 정보 리스트

    public static final int NET_MEMO_INPUT                   =   77; // 메모류 입력
    public static final int NET_SAND_LOCATION                   =   78; // 위치정보 입력
    public static final int NET_GET_EPIDEMIC                   =   79; // 유행주의보 데이터 가져오기
    public static final int NET_GET_MAP_DATA                   =   80; // 유행주의보 데이터 가져오기
    public static final int NET_GET_DUST                   =   81; // 미세먼지 정보 가져오기

    public static final int NET_GET_BBS_LIST                   =   82; // 게시판 리스트 가져오기
    public static final int NET_MBER_USER_HEAT_AREA_ADD                   =   83; // 유저 위치 정보 리스트 가져오기
    public static final int NET_GET_YOUTUBE_LIST                   =   84; // 유튜브 리스트 가져오기
    public static final int NET_GET_BBS_VIEW                   =   85; // 푸시 게시물 링크 가져오기

    //hsh
    public static final int NET_GET_ALARM_POP                   =   86; // 메인화면 알람 팝업


    /* 아이 심리 */
    public static final int NET_PSY_MAIN                   =   87; // 아이 심리 메인
    public static final int NET_PSY_CHECK_M                  =   88; // 아이 심리 체크
    public static final int NET_PSY_CHECK_RESULT              =   89; // 아이 심리 결과 요청
    public static final int NET_PSY_CHECK_RESULT_CMT              =   90; // 아이 심리 결과
    public static final int NET_PSY_CHECK_RESULT_LIST              =   91; // 아이 심리 결과
    public static final int NET_PSY_MEDIA                   =   92; // 아이 심리 음원

    public static final int NET_EDU_VIDEO_LIST                   =   93; // 교육 비디오
    public static final int NET_EAT_PLAN_ROWS                   =   94;

    /* 엄마건강 */
    public static final int NET_MOTHER_WT_DATA                   =   100;
    public static final int NET_MOTHER_REVC_GOAL                =   101;
    public static final int NET_MOTHER_REVC_DATA                =   102;

    /* 정회원 여부 */
    public static final int NET_ASSTB_REG_CHK_OK                      =   120;// 준회원 등록
    public static final int NET_ASSTB_REG_ID_PWD_INPUT                     =   121;// 준회원가입
    public static final int NET_ASSTB_REG_EDIT_OK                  =   122;// 준회원가입
    public static final int NET_ASSTB_MBER_CHL_FIND_YN       =   123;// 정회원여부 확인
    public static final int NET_ASSTB_MBER_KEEP_MEMBER     =   124;// 정회원 바로 전환
    public static final int NET_ASSTB_MBER_KEEP_MEMBER_ID_CHANGE     =   129;// 아이디

    public static final int NET_ASSTB_MBER_KEEP_MEMBER_CHL_INFO     =   126;// 정회원 아이리스트
    public static final int NET_ASSTB_MBER_KEEP_MEMBER_CHL_INFO_MAPPING     =   127;// 정회원 아이매핑
    public static final int NET_ASSTB_MBER_KEEP_MEMBER_ID_CHANGE_CHL_INFO_MAPPING     =   130;// 아이디선택 후 정회원 아이매핑

    public static final int NET_ASSTB_KBTG_ALIMI_VIEW_ON                =   128;    // 마지막 성장 데이터

    public static final int NET_ASSTB_MBER_CNTR_HEIGHT_GROWTH                 =   129;    // 아이성장현황 공유
    public static final int NET_ASSTB_MBER_CNTR_HEIGHT_LAST                 =   130;    // 아이성장 예측키 공유
    public static final int NET_ASSTB_MBER_CNTR_BDHEAT                 =   131;    // 아이체온 현황 공유
    public static final int NET_ASSTB_MBER_CNTR_DISS                 =   132;    // 유의질환 공유
    public static final int NET_ASSTB_MBER_CNTR_TRL_RESULT                 =   133;    // 상황심리 공유
    public static final int NET_ASSTB_MBER_CNTR_TRL_RESULT_TWO                 =   134;    // 심리 공유

    public static final int NET_LOGIN_AGREEMENT_YN              =   135;// 동의하기
    public static final int NET_MBER_CHECK_HP_SEND_CRT              =   136;// 준회원 sms인증

    public static final int NET_MBER_CHECK_AGREE_YN              =   137;// 마케팅, 위치정보 동의 여부
    public static final int NET_NOTICE_MAIN_POP_YN              =   138;// 공지사항 팝업








    /**
     * 네트워크 구성 인스턴스 가져오기
     * @return NetworkConst
     */
    public static NetworkConst getInstance()
    {
        if (_instance == null)
        {
            synchronized (NetworkConst.class)
            {
                if(_instance == null)
                {
                    _instance = new NetworkConst();
                }
            }

        }
        return _instance;
    }

    public void setContext(Context context)
    {
        this.mContext = context;
        try {
            pi = mContext.getPackageManager().getPackageInfo( mContext.getPackageName(), 0 );
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            GLog.e(e.toString());
        }
    }

}
