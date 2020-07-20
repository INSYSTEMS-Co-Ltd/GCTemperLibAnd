package com.greencross.gctemperlib.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.greencross.gctemperlib.greencare.util.SharedPref;

import java.util.ArrayList;


/**
 * Created by jihoon on 2016-03-21.
 * 공유 데이터 클래스
 *
 * @since 0, 1
 */
public class CommonData {

    private Context mContext;
    private static CommonData _instance;

    public static final String TEMP_PATH = android.os.Environment.getExternalStorageDirectory()
            + "/Android/data/com.appmd.hi.gngcare.common/tmp/";

    public static final String BROADCAST_ACTIVITY_FINISH = "com.appmd.hi.gngcare.activity.finish";
    public static final String BROADCAST_UPLOAD_MISSION = "BROADCAST_UPLOAD_MISSION";

    public static final String PERSONAL_TERMS_1_URL = "https://wkd.walkie.co.kr/FEVER/popup/pop_01.html";      // 개인정보 제공 동의
    public static final String PERSONAL_TERMS_2_URL = "https://wkd.walkie.co.kr/FEVER/popup/pop_02.html";      // 개인민감정보 제공 동의
    public static final String PERSONAL_TERMS_3_URL = "https://wkd.walkie.co.kr/FEVER/popup/pop_04.html";      // 개인정보 제3자 제공 동의
    public static final String LOCATION_TERMS_URL = "https://wkd.walkie.co.kr/FEVER/popup/pop_03.html";    // 위치정보 이용 동의

    public static final String PERSONAL_TERMS_JUN_1_URL = "https://wkd.walkie.co.kr/FEVER/popup/asso_pop_01.html";      // 준회원 개인정보 제공 동의
    public static final String PERSONAL_TERMS_JUN_2_URL = "https://wkd.walkie.co.kr/FEVER/popup/asso_pop_02.html";      // 준회원 개인정보 제3자 제공 동의
    public static final String PERSONAL_TERMS_JUN_3_URL = "https://wkd.walkie.co.kr/FEVER/popup/asso_pop_04.html";      // 준회원 개인민감정보 제공 동의
    public static final String LOCATION_TERMS_JUN_URL = "https://wkd.walkie.co.kr/FEVER/popup/asso_pop_03.html";    // 준회원 위치정보 이용 동의

    public static final String MOBILE_TERMS_URL = "https://wkd.walkie.co.kr/FEVER/popup/asso_pop_market.html";    //마케팅 이용 동의
    public static final String PERSONAL_TERMS_URL = "https://wkd.walkie.co.kr/FEVER/popup/pop_05.html";    //개인정보 처리 방침

    public static final String CHLDRN_INPUT_UPLOAD_URL = "https://wkd.walkie.co.kr/HL_FV/mobile_upload/HL_chldrn_input_upload.aspx";    // 자녀 관리 > 사진 업로드

    public static final String URL_GCBALANCECENTER = "http://www.gcbalancecenter.com/";    // 출처 : GC밸런스심리케어센터

    // 자녀 사진 업로드 hidden data
    public static final String CHLDRN_INPUT_UPLOAD_VIEWSTATE = "/wEPDwUKMTU3NTQwMjEyMGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFDEltYWdlQnV0dG9uMcmtD8l+QPmaQraJxlon6pwWAXDp24qhiSwPyYHt510d";
    public static final String CHLDRN_INPUT_UPLOAD_EVENTVALIDATION = "/wEdAAQ1Psvp8MhP8Qb1BoNYo24CPn9OJ+moTgcJaM4w8g/8abfdD2zHcsn5ImYdY7kWiwbpkAKvHlFmeWUpJ6tT4i4qo2Zgu61ffuWmiRsm9N11l9C+2j0hn/6bOaQo3d5OQBs=";

    // 일상기록 사진 업로드 hidden data
    public static final String CHLDRN_NOTE_UPLOAD_VIEWSTATE = "/wEPDwUKMTM2ODMxOTA2OGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFDEltYWdlQnV0dG9uMRVUYSqY+BBh12ChzYvoEIiZXVDunViBtSenJmijObhP";
    public static final String CHLDRN_NOTE_UPLOAD_EVENTVALIDATION = "/wEWCgLkzImcBgKNu5z0AwLgwJWSCALPh9yCCwKiwdKQDgKewZLDCQKZwfaKBQK1wfbXCQKY6fHPCQLSwpnTCIsvRoi9NE4oNAtmtPHie3a/PWTtrXZFCRfZgQ513+3v";


    public static final String VIEWSTATE = "__VIEWSTATE";
    public static final String EVENTVALIDATION = "__EVENTVALIDATION";
    public static final String DATA_YN_Y = "DATA_YN=Y";

    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String USER_AGENT = "User-Agent";
    public static final String IMAGEBUTTON_1_X = "ImageButton1.x";
    public static final String IMAGEBUTTON_1_Y = "ImageButton1.y";

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_EUC_KR = "euc-kr";

    // 육아노트
    public static final int INTRO_POST_DELAYED = 300;
    public static final int ANI_DELAY_100 = 100;
    public static final int ANI_DELAY_200 = 200;
    public static final int ANI_DELAY_500 = 500;
    public static final int ANI_DELAY_1000 = 1000;
    public static final int ANI_DELAY_1500 = 1500;
    public static final int ANI_DELAY_2000 = 2000;
    public static final int ANI_DELAY_2500 = 2500;
    public static final int ANI_DELAY_3000 = 3000;
    public static final int ANI_DELAY_60000 = 60000;
    public static final int TIME_OUT_DELAY = 15000;

    public static final int PERMISSION_REQUEST_SMS = 1; // SMS 권한 리퀘스트
    public static final int PERMISSION_REQUEST_STORAGE = 2; // 저장소 권한 리퀘스트
    public static final int PERMISSION_REQUEST_PHONE_STATE = 3; // 폰 권한 리퀘스트
    public static final int PERMISSION_REQUEST_GPS = 4;// GPS 권한, 열지도
    public static final int PERMISSION_REQUEST_NECESSARY_PERMS = 5;  // 폰, GPS 권한
    public static final int PERMISSION_REQUEST_GPS_DUST = 6;// GPS 권한, 미세먼지


    // 사진 등록시 처리
    public static final int NATIVE_GALLERY = 1;
    public static final int NATIVE_CAMERA = 2;
    public static final int NATIVE_CROP = 3;

    public static final int FILE_TYPE_IMAGE = 0;
    public static final int FILE_TYPE_VOICE = 1;

    // 푸시알림 모드
    public static final int PUSH_MODE_DEFAULT = 0;  //  무음
    public static final int PUSH_MODE_BELL = 1;  // 벨소리
    public static final int PUSH_MODE_VIBRATE = 2;  // 진동
    public static final int PUSH_MODE_BELL_VIBRATE = 3;  // 벨+진동

    // 로그인 타입
    public static final int LOGIN_TYPE_PARENTS = 1;   // 부모님
    public static final int LOGIN_TYPE_CHILD = 2;  // 자녀

    // 자주 묻는 질문 타입
    public static final int FREQUENTLY_TYPE_NORMAL = 0;  // 일반
    public static final int FREQUENTLY_TYPE_MEMBER_INFO = 1;  // 회원정보
    public static final int FREQUENTLY_TYPE_MAIN_VIEW = 2;  // 메인화면
    public static final int FREQUENTLY_TYPE_PARENTING_DIARY = 3;  // 육아 다이어리
    public static final int FREQUENTLY_TYPE_SAFETY = 4;  // 자녀 안전 지킴이
    public static final int FREQUENTLY_TYPE_PHONE_MANAGE = 5;  // 스마트폰 사용 관리
    public static final int FREQUENTLY_TYPE_TOTAL_HEALTHCARE = 6;  // 토탈 헬스케어

    // Wheel 팝업 타입
    public static final int WHEEL_TYPE_BIRTHDAY = 0;  // 인트로 생년월일 타입
    public static final int WHEEL_TYPE_HEIGHT = 1;  // 인트로 키 타입
    public static final int WHEEL_TYPE_WEIGHT = 2;  // 인트로 몸무게 타입
    public static final int WHEEL_TYPE_MYPAGE_BIRTHDAY = 3;  // 마이페이지에서 생년월일 타입
    public static final int WHEEL_TYPE_MYPAGE_HEIGHT = 4;  // 마이페이지에서 키 타입
    public static final int WHEEL_TYPE_ACTIVE_TIME = 5;  // 활동량 수동측정 시간 타입
    public static final int WHEEL_TYPE_UPDATE_FOOD = 6;  // 음식사진 등록 타입
    public static final int WHEEL_TYPE_WEIGHT_WEIGHT = 7;  // 웨이트 몸무게 저장
    public static final int WHEEL_TYPE_STEP_GOAL = 8;  // 걸음 목표 설정
    public static final int WHEEL_TYPE_MYPAGE_WEIGHT = 9;  // 마이페이지에서 몸무게 타입
    public static final int WHEEL_TYPE_TIME = 10; // 시간 ( 00시 00분 ~ 23시 59분 )
    public static final int WHEEL_TYPE_MYPAGE_HEAD = 11;  // 마이페이지에서 머리둘레 타입

    // Wheel 팝업 한화면에서 두개 쓸 때 분류 타입 (ex:육아다이어이 성장 정보입력)
    public static final String WHEEL_TYPE_BIRTHHEIGHT = "type_birthheight";  // 출생정보 키 타입
    public static final String WHEEL_TYPE_CHECKHEIGHT = "type_checkeight";  // 측정정보 키 타입
    public static final String WHEEL_TYPE_BIRTHWEIGHT = "type_birthweight";  // 출생정보 몸무게 타입
    public static final String WHEEL_TYPE_CHECKWEIGHT = "type_checkweight";  // 측정정보 몸무게 타입
    public static final String WHEEL_TYPE_BIRTHWHEAD = "type_birthhead";  // 출생정보 머리둘레 타입
    public static final String WHEEL_TYPE_CHECKHEAD = "type_checkhead";  // 측정정보 머리둘레 타입

    public static final String WHEEL_TYPE_MYHEIGHT = "type_myheight";  // 자식 키
    public static final String WHEEL_TYPE_MYWEIGHT = "type_myweight";  // 자식 몸무게
    public static final String WHEEL_TYPE_MOHEIGHT = "type_moheight";  // 엄마 키
    public static final String WHEEL_TYPE_MOWEIGHT = "type_moweight";  // 엄마 몸무게
    public static final String WHEEL_TYPE_FAHEIGHT = "type_faheight";  // 아빠 키
    public static final String WHEEL_TYPE_FAWEIGHT = "type_faweight";  // 아빠 몸무게


    // 생년월일
    public static final int BIRTHDAY_YEAR_START = 1940;
    public static final int BIRTHDAY_MONTH_START = 1;
    public static final int BIRTHDAY_MONTH_END = 12;
    public static final int BIRTHDAY_DAY_START = 1;
    public static final int BIRTHDAY_DAY_END = 31;

    // 체형
    public static final int HEIGHT_START = 20;
    public static final int HEIGHT_END = 220;

    // 몸무게
    public static final int WEIGHT_START = 1;
    public static final int WEIGHT_END = 200;

    // 머리둘레
    public static final int HEAD_START = 20;
    public static final int HEAD_END = 70;

    // 분
    public static final int MINUTE_START = 0;
    public static final int MINUTE_END = 59;

    // 태어난 일수 계산 데이터
    public static final int AFTER_BIRTH_4745 = 4745;   // 13년 일수
    public static final int AFTER_BIRTH_2555 = 2555;   // 7년 일수
    public static final int AFTER_BIRTH_1825 = 1825;   // 5년 일수
    public static final int AFTER_BIRTH_1095 = 1095;   // 3년 일수
    public static final int YEAR = 365;
    public static final int MONTH = 30; //개월
    public static final int ONE_HOUR_MINUTE = 60;
    public static final int AFTER_BIRTH_PERIOD = 280;   // 임신 40주
    public static final int AFTER_BIRTH_16 = 112;   // 임신 16주

    // arraylist page count
    public static final int PAGE_MAX_COUNT = 10; // api array result 개수

    // 육아노트 타입
    public static final int GRAPH_PAGENUMBER = 1;  // 그래프

    public static final String TWO_NUMBER_FORMAT = "%02d";
    public static final String LATLNG_FORMAT = "%.7f";

    // 접속일 UNIT TYPE
    public static final String UNIT_S = "s";    // 초
    public static final String UNIT_M = "m";    // 분
    public static final String UNIT_H = "h";    // 시
    public static final String UNIT_D = "d";    // 일
    public static final String UNIT_W = "w";    // 주
    public static final String UNIT_X = "x";    // 1개월전

    // 남성 여성 구분
    public static final String MALE = "1";   // 남자
    public static final String FEMALE = "2";   // 여자

    public static final String YES = "Y";
    public static final String NO = "N";

    // DateFormat 패턴
    public static final String PATTERN_YYMMDD = "yyMMdd";
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String PATTERN_YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_2 = "MM-dd";
    public static final String PATTERN_DATE_DOT = "yyyy.MM.dd";
    public static final String PATTERN_DATE_KR = "yyyy년 MM월 dd일";
    public static final String PATTERN_DATE_KR2 = "yyyy/MM/dd";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_DATETIME_S = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_TIME = "a hh:mm";
    public static final String PATTERN_TIME_2 = "HH:mm";

    // 문자
    public static final String STRING_COLON = ":";    // 콜론
    public static final String STRING_HYPHEN = "-";    // 하이픈
    public static final String STRING_SHARP = "#";    // 샵
    public static final String STRING_SPACE = " ";    // 공백
    public static final String STRING_DOT = ".";    // 점
    public static final String STRING_SLASH = "/";    // 슬러쉬
    public static final String STRING_REVERS_SLASH = "\\";  // 역슬러쉬
    public static final String STRING_PERCENT = "%";    // 퍼센트
    public static final String STRING_WAVE = "~";    // 물결

    // String 값 1 2 3 4 5 ...
    public static final String STRING_ZERO = "0";
    public static final String STRING_ONE = "1";
    public static final String STRING_TWO = "2";
    public static final String STRING_THR = "3";
    public static final String STRING_FOR = "4";
    public static final String STRING_FIV = "5";
    public static final String STRING_SIX = "6";
    public static final String STRING_TWOTEN = "20";
    public static final String STRING_FORONEFIV = "4.15";
    public static final String STRING_SIXTWO = "62";

    // 앱코드
    public static final String APP_CODE_ANDROID = "android";
    public static final String INSURE_CODE = "108";   // 현대해상 코드


    // intent putExtra 데이터

    public static final String EXTRA_TITLE_ARRAY = "EXTRA_TITLE_ARRAY";
    public static final String EXTRA_CONTENT_ARRAY = "EXTRA_CONTENT_ARRAY";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_CODE = "EXTRA_CODE";
    public static final String EXTRA_WHEEL_TYPE = "EXTRA_WHEEL_TYPE"; // 휠 타입
    public static final String EXTRA_BIRTHDAY = "EXTRA_BIRTHDAY";
    public static final String EXTRA_HEIGHT = "EXTRA_HEIGHT";
    public static final String EXTRA_WEIGHT = "EXTRA_WEIGHT";
    public static final String EXTRA_HEAD = "EXTRA_HEAD";
    public static final String EXTRA_HOUR = "EXTRA_HOUR";
    public static final String EXTRA_MINUTE = "EXTRA_MINUTE";
    public static final String EXTRA_AMPM = "EXTRA_AMPM";
    public static final String EXTRA_YEAR = "EXTRA_YEAR";
    public static final String EXTRA_MONTH = "EXTRA_MONTH";
    public static final String EXTRA_DAY = "EXTRA_DAY";
    public static final String EXTRA_PHOTO_INDEX = "EXTRA_PHOTO_INDEX";
    public static final String EXTRA_PHOTO_LIST = "EXTRA_PHOTO_LIST";
    public static final String EXTRA_PHOTO = "EXTRA_PHOTO";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_STEP_COUNT = "EXTRA_STEP_COUNT";
    public static final String EXTRA_HEIGHTTYPE = "EXTRA_HEIGHTTYPE";
    public static final String EXTRA_WEIGHTTYPE = "EXTRA_WEIGHTTYPE";
    public static final String EXTRA_HEADTYPE = "EXTRA_HEADTYPE";
    public static final String EXTRA_CHILD_INDEX = "EXTRA_CHILD_INDEX";
    public static final String EXTRA_NICK = "EXTRA_NICK";
    public static final String EXTRA_CHL_SN = "EXTRA_CHL_SN";
    public static final String EXTRA_HP_TIME = "EXTRA_HP_TIME";
    public static final String EXTRA_LC_X = "EXTRA_LC_X";   // 경도
    public static final String EXTRA_LC_Y = "EXTRA_LC_Y";   // 위도
    public static final String EXTRA_LOCATION = "EXTRA_LOCATION";   // 안전지역 별칭
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_URL_POSITION = "EXTRA_URL_POSITION";
    public static final String EXTRA_ACTIVITY_TITLE = "EXTRA_ACTIVITY_TITLE";
    public static final String EXTRA_CHILD_CARE_TYPE = "EXTRA_CHILD_CARE_TYPE";
    public static final String EXTRA_NOTE_EDIT = "EXTRA_NOTE_EDIT";    // 수정
    public static final String EXTRA_VIEW_TYPE = "EXTRA_VIEW_TYPE";      // 뷰타입 ( 1 - 육아노트 , 2 - 병력 노트 )
    public static final String EXTRA_NOTE_SN = "EXTRA_NOTE_SN";        // 리스트 고유번호
    public static final String EXTRA_UPLOAD_DATE = "EXTRA_UPLOAD_DATE";    // 데이터 기록 날짜 ( yyyyMMdd )
    public static final String EXTRA_UPLOAD_TIME = "EXTRA_UPLOAD_TIME";    // 데이터 기록 시간 ( HHmm )
    public static final String EXTRA_DIAGNOSIS = "EXTRA_DIAGNOSIS";      // 병력노트 진단명
    public static final String EXTRA_HOSPITAL = "EXTRA_HOSPITAL";       // 병력노트 병원명
    public static final String EXTRA_PHOTO_ARR = "EXTRA_PHOTO_ARR";      // 사진 리스트
    public static final String EXTRA_NOTE_TITLE_TEXT = "EXTRA_NOTE_TITLE_TEXT";    // 육아노트 제목
    public static final String EXTRA_NOTE_CONTENT_TEXT = "EXTRA_NOTE_CONTENT_TEXT";  // 육아노트 내용
    public static final String EXTRA_MEDICAL_TITLE_TEXT = "EXTRA_MEDICAL_TITLE_TEXT"; // 병력노트 내용
    public static final String EXTRA_LIST_INDEX = "EXTRA_LIST_INDEX";         // 리스트 인덱스
    public static final String EXTRA_COLOR_1 = "EXTRA_COLOR_1";
    public static final String EXTRA_AREA_1 = "EXTRA_AREA_1";
    public static final String EXTRA_AREA_TXT_1 = "EXTRA_AREA_TXT_1";
    public static final String EXTRA_COLOR_2 = "EXTRA_COLOR_2";
    public static final String EXTRA_AREA_2 = "EXTRA_AREA_2";
    public static final String EXTRA_AREA_TXT_2 = "EXTRA_AREA_TXT_2";
    public static final String EXTRA_COLOR_3 = "EXTRA_COLOR_3";
    public static final String EXTRA_AREA_3 = "EXTRA_AREA_3";
    public static final String EXTRA_AREA_TXT_3 = "EXTRA_AREA_TXT_3";
    public static final String EXTRA_COLOR_4 = "EXTRA_COLOR_4";
    public static final String EXTRA_AREA_4 = "EXTRA_AREA_4";
    public static final String EXTRA_AREA_TXT_4 = "EXTRA_AREA_TXT_4";
    public static final String EXTRA_MBERNO = "EXTRA_MBERNO";
    public static final String EXTRA_MBERNM = "EXTRA_MBERNM";
    public static final String EXTRA_MEMID = "EXTRA_MEMID";
    public static final String EXTRA_MEMID_SITE_BOOL = "EXTRA_MEMID_SITE_BOOL";
    public static final String EXTRA_MEMID_APP_BOOL = "EXTRA_MEMID_APP_BOOL";
    public static final String EXTRA_PHONENO = "EXTRA_PHONENO";
    public static final String EXTRA_MBERNATION = "EXTRA_MBERNATION";
    public static final String EXTRA_GENDER = "EXTRA_GENDER";
    public static final String EXTRA_JOB = "EXTRA_JOB";
    public static final String EXTRA_AFTERBIRTH = "EXTRA_AFTERBIRTH";
    public static final String EXTRA_GLAD = "EXTRA_GLAD";
    public static final String EXTRA_MBERSN = "EXTRA_MBERSN";
    public static final String EXTRA_MEMNAME = "EXTRA_MEMNAME";
    public static final String EXTRA_MARKETING = "EXTRA_MARKETING";
    public static final String EXTRA_JOINPATH = "EXTRA_JOINPATH";
    public static final String EXTRA_VIDEOID = "EXTRA_VIDEOID";
    public static final String EXTRA_VIMEOCAT = "EXTRA_VIMEOCAT";
    public static final String EXTRA_VIDEOTITLE = "EXTRA_VIDEOTITLE";

    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";

    public static final String EXTRA_INFO_SN = "EXTRA_INFO_SN";
    public static final String EXTRA_PUSH_TYPE = "EXTRA_PUSH_TYPE";

    public static final String EXTRA_MAIN_TYPE = "EXTRA_MAIN_TYPE";


    // 체중정보
    public static final int REQUEST_PUSH_ALARM = 107;    // 알림 설정
    public static final int REQUEST_LOGIN_ID = 108;    // 아이디 찾기
    public static final int REQUEST_CHILD_MANAGE = 109;    // 자녀관리
    public static final int REQUEST_CODE_INPUT = 110;    // 성장입력
    public static final int REQUEST_CODE_BABY_INFO = 111;    // 아이정보 변경
    public static final int REQUEST_CODE_GROWTH = 112;    // 성장 메인
    public static final int REQUEST_CODE_FEVER = 113;    // 체온 메인
    public static final int REQUEST_ADDRESS_SETTING = 114;    // 열지도 주소 설정
    public static final int REQUEST_ADDRESS_SEARCH = 115;    // 열지도 주소 설정

    // METHOD
    public static final String METHOD_GET_INFORMATION = "getinformation";
    public static final String METHOD_CALL_REG_FX_ADD = "call_reg_fx_add_crt";
    public static final String METHOD_LOGIN = "login";
    public static final String METHOD_MBER_USER_OUT = "mber_user_out";    // 회원 탈퇴
    public static final String METHOD_SET_GROWTH = "chldrn_growth_test";                   //1 성장테스트용(유영진대리)
    public static final String METHOD_SET_GROWTH_INPUT_OK = "chldrn_growth_input_ok_test";       //2 성장테스트용(유영진대리)
    public static final String METHOD_GET_GROWTH_LIST = "chldrn_growth_list";
    public static final String METHOD_GET_GROWTH_LIST_TEST = "chldrn_growth_list_test";
    public static final String METHOD_GET_GROWTH_MONTH_GRP = "chldrn_growth_list_month_grp";
    public static final String METHOD_GET_GROWTH_MONTH_GRP_TEST = "chldrn_growth_list_month_grp_test";
    public static final String METHOD_GET_GROWTH_DEL = "chldrn_growth_del";
    public static final String METHOD_GET_GROWTH_DEL_TEST = "chldrn_growth_del_test";
    public static final String METHOD_GET_HRA_NEXT = "chldrn_hra_next_cm_test";           //3 성장테스트용(유영진대리)
    public static final String METHOD_CHLDRNCM = "chldrncm"; // 자녀 애칭 변경
    public static final String METHOD_CHLDRN_SET_FAQ = "chldrn_set_faq";  // 자주 묻는 질문
    public static final String METHOD_CHLDRN_GROWTH_FAQ = "chldrn_growth_faq";    // 성장 FAQ
    public static final String METHOD_CHLDRN_GROWTH_LAST_CM_HEIGHT = "chldrn_growth_last_cm_height_test";    // 가장 최근 자녀 키 몸무게 데이터 조회   //4 성장테스트용(유영진대리)
    public static final String METHOD_CONTENT_SPECIAL_BBSLIST = "content_special_bbslist";           // 게시판 조회
    public static final String METHOD_CONTENT_MOVIE_BBSLIST = "content_movie_bbslist";           // 유튜브 조회
    public static final String METHOD_PUSH_HEALTH_VIEW = "push_health_view";           // 게시글 하나 조회
    public static final String METHOD_MBER_CHECK_AGREE_YN = "mber_check_agree_yn";           // 마케팅,위치정보 동의 여부
    public static final String METHOD_NOTICE_MAIN_POP_YN = "notice_main_pop_yn";           // 공지사항 팝업


    //hsh
    public static final String METHOD_ALARM_POP = "main_popup_notice";           // 메인 온도 팝업 조회
    public static final String JSON_POP_TITLE = "pop_subject";
    public static final String JSON_POP_TXT = "pop_cn";
    public static final String JSON_DATE_YN = "date_yn";

    // 회원
    public static final String METHOD_LOGIN_ID = "login_id"; // 아이디 찾기
    public static final String METHOD_LOGIN_PWD = "login_pwd";// 비밀번호 찾기
    public static final String METHOD_MBER_USER_PWD_EDIT = "mber_user_pwd_edit";   // 비밀번호 변경
    public static final String METHOD_MBER_CHECK = "mber_check_crt";// 회원 인증 및 계약자 여부
    public static final String METHOD_ASSTB_REG_CHK_OK = "asstb_reg_chk_ok_crt";// 회원 인증 및 계약자 여부
    public static final String METHOD_MBER_CHLDRN_INPUT = "mber_chldrn_input";// 자녀 저장하기
    public static final String METHOD_MBER_REG_CHECK_ID = "mber_reg_check_id";    // 아이디 중복 체크 여부
    public static final String METHOD_MBER_REG_OK = "mber_reg_ok_crt";  // 회원가입
    public static final String METHOD_ASSTB_REG_ID_PWD_INPUT = "asstb_reg_id_pwd_input";  // 준회원가입
    public static final String METHOD_ASSTB_REG_EDIT_OK = "asstb_reg_edit_ok";// 준회원 정보수정
    public static final String METHOD_ASSTB_MBER_CHL_FIND_YN = "asstb_mber_chl_find_yn"; //정회원 여부
    public static final String METHOD_ASSTB_MBER_KEEP_MEMBER = "asstb_mber_keep_member"; //정회원 바로 전환
    public static final String METHOD_ASSTB_MBER_KEEP_MEMBER_ID_CHANGE = "asstb_mber_keep_member_id_change"; //아이디 선택 후 정회원 바로 전환
    public static final String METHOD_ASSTB_MBER_KEEP_MEMBER_CHL_INFO = "asstb_mber_keep_member_chl_info"; //정회원 바로 전환
    public static final String METHOD_ASSTB_MBER_KEEP_MEMBER_CHL_INFO_MAPPING = "asstb_mber_keep_member_chl_info_mapping"; //정회원 아이 맵핑
    public static final String METHOD_ASSTB_MBER_KEEP_MEMBER_ID_CHANGE_CHL_INFO_MAPPING = "asstb_mber_keep_member_id_change_chl_info_mapping"; //아이 선택 후 정회원 아이 맵핑
    public static final String METHOD_ASSTB_KBTG_ALIMI_VIEW_ON = "asstb_kbtg_alimi_view_on";           // 최근 알림 1개
    public static final String METHOD_MBER_CHECK_HP_SEND_CRT = "mber_check_hp_send_crt";           // 준회원 sms인증


    public static final String METHOD_MBER_USER_HEAT_AREA_ADD = "mber_user_heat_area_add";  // 열지도 지역정보 넣기


    public static final int REQUEST_CODE_CROISE = 700;        // 크로이스 체온계로 데이터 받아오기


    /*
     * return Network result
     */

    public static final String JSON_JSON = "json";
    public static final String JSON_STRJSON = "strJson";
    public static final String JSON_RESULT_CODE = "result_code";
    public static final String JSON_DATA = "data";
    public static final String JSON_SESSION_CODE = "session_code";
    public static final String JSON_MEMBER_ID = "member_id";
    public static final String JSON_PHOTO = "photo";
    public static final String JSON_HEIGHT = "height";
    public static final String JSON_JOIN_TYPE = "join_type";
    public static final String JSON_JOIN_STEP = "join_step";
    public static final String JSON_TODAY_POPUP_EVENT = "today_popup_event";
    public static final String JSON_IS_POPUP_EVENT_READ = "is_popup_event_read";
    public static final String JSON_POPUP_EVENT_VIEW_ID = "pupop_event_view_id";
    public static final String JSON_POPUP_EVENT_READ_TIME = "pupop_event_read_time";


    public static final String JSON_API_RESULT_CODE = "RESULT_CODE";          // 결과코드


    // 현대해상
    public static final String JSON_API_CODE = "api_code";
    public static final String JSON_APP_CODE = "app_code";
    public static final String JSON_INSURES_CODE = "insures_code";
    public static final String JSON_CNTR_TYP = "cntr_typ"; //공유 번호
    public static final String JSON_CMPNY_NM = "cmpny_nm";
    public static final String JSON_CMPNY_ARS = "cmpny_ars";
    public static final String JSON_LOGINURL = "loginurl";
    public static final String JSON_APIURL = "apiurl";
    public static final String JSON_APPVERSION = "appVersion";
    public static final String JSON_UPDATEURL = "updateurl";
    public static final String JSON_CMPNY_FILE_COURS = "cmpny_file_cours";
    public static final String JSON_SERVICE_START = "service_start";
    public static final String JSON_FX_YN = "fx_yn";
    public static final String JSON_FX_MTH = "fx_mth";
    public static final String JSON_MBER_SN = "mber_sn";
    public static final String JSON_TOKEN = "token";
    public static final String JSON_PHONE_MODEL = "phone_model";
    public static final String JSON_APP_VER = "app_ver";
    public static final String JSON_MBER_AREA = "mber_area"; //거주지역
    public static final String JSON_MBER_COURS = "mber_cours"; //앱가입 경로
    public static final String JSON_MARKETING_YN = "marketing_yn"; //마케팅 동의
    public static final String JSON_MARKETING_YN_DE = "marketing_yn_de"; //마케팅 동의 날짜
    public static final String JSON_LOCATION_YN = "location_yn"; //위치정보
    public static final String JSON_DIET_PROGRAM_REQ_YN = "diet_program_req_yn"; //다이어트 프로그램 신청 여부


    public static final String JSON_PUSH_K = "pushk";
    public static final String JSON_MBER_NM = "mber_nm";
    public static final String JSON_MBER_LIFYEA = "mber_lifyea";
    public static final String JSON_MBER_HP = "mber_hp";
    public static final String JSON_MBER_NATION = "mber_nation";
    public static final String JSON_MBER_BRTHDY = "mber_brthdy";
    public static final String JSON_MBER_SEX = "mber_sex";
    public static final String JSON_MBER_JOB_YN = "mber_job_yn";
    public static final String JSON_MBER_AGREEMENT_YN = "mber_agreement_yn";
    public static final String JSON_CHIDRN_CNT = "chldrn_cnt";
    public static final String JSON_LOGIN_TYPE = "login_type";
    public static final String JSON_PUSH_YN = "push_yn";
    public static final String JSON_PUSH_MTH = "push_mth";
    public static final String JSON_AUTO_LOGIN = "auto_login";
    public static final String JSON_REMEMBER_ID = "remember_id";
    public static final String JSON_REG_YN = "reg_yn";
    public static final String JSON_GETINFORMATION = "getInformation";
    public static final String JSON_MBER_ID = "mber_id";
    public static final String JSON_MBER_PWD = "mber_pwd";
    public static final String JSON_MBER_BEFORE_PWD = "mber_before_pwd";
    public static final String JSON_MBER_NO = "mber_no";
    public static final String JSON_SEL_SITE_YN = "sel_site_yn";
    public static final String JSON_APP_SITE_YN = "app_site_yn";
    public static final String JSON_SEL_MEMID = "sel_memid";
    public static final String JSON_SITE_MEMID = "site_memid";
    public static final String JSON_APP_MEMID = "app_memid";
    public static final String JSON_MEMNAME = "memname";
    public static final String JSON_MEMBER_CERTIFI = "member_certifi";
    public static final String JSON_SEND_MAIL_YN = "send_mail_yn";
    public static final String JSON_CHL_SN = "chl_sn";
    public static final String JSON_REGTYPE = "regtype";
    public static final String JSON_BASIS_HEIGHT = "bsis_height";
    public static final String JSON_BASIS_BDWGH = "bsis_bdwgh";
    public static final String JSON_BASIS_HEADCM = "bsis_headcm";
    public static final String JSON_INPUT_HEIGHT = "input_height";
    public static final String JSON_INPUT_BDWGH = "input_bdwgh";
    public static final String JSON_INPUT_HEADCM = "input_headcm";
    public static final String JSON_INPUT_DE = "input_de";
    public static final String JSON_YYMM = "yymm";
    public static final String JSON_BSIS_YYMM = "bsis_yymm";
    public static final String JSON_BSIS_SEX = "bsis_sex";
    public static final String JSON_BSIS_HEIGHT = "bsis_height";
    public static final String JSON_BSIS_BDWGH = "bsis_bdwgh";
    public static final String JSON_INPUT_DAD_HEIGHT = "input_dad_height";
    public static final String JSON_INPUT_DAD_BDWGH = "input_dad_bdwgh";
    public static final String JSON_INPUT_MOM_HEIGHT = "input_mom_height";
    public static final String JSON_INPUT_MOM_BDWGH = "input_mon_bdwgh";
    public static final String JSON_MBER_FLAG = "mber_flag";
    public static final String JSON_MBER_MOTHER = "mber_mother";
    public static final String JSON_MBER_FATHER = "mber_father";
    public static final String JSON_MBER_LAST_CM = "mber_last_cm";
    public static final String JSON_MBER_START_PER = "mber_start_per";
    public static final String JSON_MBER_END_PER = "mber_end_per";
    public static final String JSON_PAGENUMBER = "pageNumber";
    public static final String JSON_MAXPAGENUMBER = "maxpageNumber";
    public static final String JSON_GROWTH_SN = "growth_sn";
    public static final String JSON_GROWTH_TYP = "growth_typ";
    public static final String JSON_CHIDRN_MONTH = "chldrn_month";
    public static final String JSON_CM_REUSLT = "cm_result";
    public static final String JSON_KG_REUSLT = "kg_result";
    public static final String JSON_HEAD_REUSLT = "head_result";
    public static final String JSON_MBER_ID_YN = "mber_id_yn";
    public static final String JSON_CHLDRN = "chldrn";
    public static final String JSON_MBER_CHLDRN_SN = "mber_chldrn_sn";
    public static final String JSON_CHLDRN_SN = "chldrn_sn";
    public static final String JSON_HEIGHTCM = "mber_cm";
    public static final String JSON_HEIGHTPER = "mber_cm_per";
    public static final String JSON_HEIGHTTXT = "mber_cm_txt";
    public static final String JSON_MBER_KG = "mber_kg";
    public static final String JSON_WEIGHTPER = "mber_kg_per";
    public static final String JSON_WEIGHTTXT = "mber_kg_txt";
    public static final String JSON_HEADCM = "mber_head";
    public static final String JSON_HEADPER = "mber_head_per";
    public static final String JSON_HEADTXT = "mber_head_txt";
    public static final String JSON_CHLDRN_JOINSERIAL = "chldrn_joinserial";
    public static final String JSON_CHLDRN_NM = "chldrn_nm";
    public static final String JSON_CHLDRN_NCM = "chldrn_ncm";
    public static final String JSON_CHLDRN_LIFYEA = "chldrn_lifyea";
    public static final String JSON_CHLDRN_AFT_LIFYEA = "chldrn_aft_lifyea";
    public static final String JSON_CHL_EXIST_YN = "chl_exist_yn";
    public static final String JSON_CHLDRN_SEX = "chldrn_sex";
    public static final String JSON_CHLDRN_HP = "chldrn_hp";
    public static final String JSON_CHLDRN_CI = "chldrn_ci";
    public static final String JSON_CHLDRN_HEIGHT = "chldrn_height";
    public static final String JSON_CHLDRN_BDWGH = "chldrn_bdwgh";
    public static final String JSON_CHLDRN_HEADCM = "chldrn_headcm";
    public static final String JSON_CHLDRN_MAIN_IMAGE = "chldrn_main_image";
    public static final String JSON_CHLDRN_ORG_IMAGE = "chldrn_org_image";
    public static final String JSON_SAFE_AREA_X = "safe_area_x";
    public static final String JSON_SAFE_AREA_Y = "safe_area_y";
    public static final String JSON_SAFE_KM = "safe_km";
    public static final String JSON_SAFE_ALARM_AT = "safe_alarm_at";
    public static final String JSON_HP_USE_TIME = "hp_use_time";
    public static final String JSON_HP_USE_ALARM_AT = "hp_use_alarm_at";
    public static final String JSON_HP_USE_ESTBS_TIME = "hp_use_estbs_time";
    public static final String JSON_CHLDRN_KGPER = "chldrn_kgper";
    public static final String JSON_SELECT = "select";
    public static final String JSON_SELECT_CHILD_SN = "select_child_sn";
    public static final String JSON_SAFE_HEDEXPLN = "safe_hedexpln";
    public static final String JSON_SAFE_ADRES = "safe_adres";
    public static final String JSON_UPFILE = "upfile";
    public static final String JSON_FAQ_SN = "faq_sn";
    public static final String JSON_FAQ_QESTN = "faq_qestn";
    public static final String JSON_FAQ_ANSWER = "faq_answer";
    public static final String JSON_FAQ_KWRD = "faq_kwrd";
    public static final String JSON_VALUE = "value";
    public static final String JSON_DATA_YN = "data_yn";
    public static final String JSON_MBER_AUTH_NUM = "mber_auth_num"; //인증번호
    public static final String JSON_SETGROUP = "set_group";
    public static final String JSON_SETQESTN = "set_qestn";
    public static final String JSON_SETANSWER = "set_answer";
    public static final String JSON_JUMINNUM = "juminnum";
    public static final String JSON_LAST_HRIGTH = "last_height";
    public static final String JSON_LAST_BDWGH = "last_bdwgh";
    public static final String JSON_HIPLANNER_HP = "fc_hp";
    public static final String JSON_THERMOMETERCHK = "thermometerchk"; // 체온계 여부
    public static final String JSON_MOTHER_WEIGHT = "mother_weight";  // 엄마 체중
    public static final String JSON_KG_KIND = "kg_kind";  // 체중 상태
    public static final String JSON_BMI = "bmi";  // BMI
    public static final String JSON_BMI_KIND = "bmi_kind";  // BMI 상태(제체중군~~)
    public static final String JSON_MAIN_CATEGORY = "main_category"; // 즐겨찾기
    public static final String JSON_WEIGHTNGCHK = "weighingchk"; //체중계 여부
    public static final String JSON_I_AM_CHILD = "i_am_child"; // 어린이 여부  부모여부(로그인자)
    public static final String JSON_MOTHER_GOAL_WEIGHT = "mother_goal_wei";   // 목표체중

    public static final String JSON_MOTHER_GOAL_CAL = "goal_mvm_calory";   // 목표칼로리
    public static final String JSON_MOTHER_GOAL_STEP = "goal_mvm_stepcnt";   // 목표스탭
    public static final String JSON_MBER_BIRTH_DUE_DE = "mber_birth_due_de";   // "20180511", 출산예정일
    public static final String JSON_MBER_CHL_BIRTH_DE = "mber_chl_birth_de";   // "20180511", 출산일
    public static final String JSON_BEF_CM = "bef_cm";   // 임신전 키
    public static final String JSON_BEF_KG = "bef_kg";   // 임신전 체중
    public static final String JSON_HP_MJ_YN = "hp_mj_yn";
    public static final String JSON_HP_MJ_YN_JUN = "hp_mj_yn_jun";
    public static final String JSON_ACTQY = "actqy";       // 활동 (가벼운활동: 1, 보통활동:2,힘든활동:3)
    public static final String JSON_BIRTH_CHL_YN = "birth_chl_yn";
    public static final String JSON_MBER_NICK = "nickname"; //커뮤니티 닉네임
    public static final String JSON_MOTHER_IS_PREGNANT = "mater_pregnant";   //임신여부 (Y/N) 로컬에서 판단한다.
    public static final String JSON_LAST_CHL_BIRTH = "last_chl_birth";   //막내 생일 로컬에서 판단한다.
    public static final String JSON_MBER_CHL_TYPE = "mber_chl_typ";   //1 : 단태임신, 2: 다태임신
    public static final String JSON_GET_DUST = "get_dust";
    public static final String JSON_DUST_SN = "dust_sn";
    public static final String JSON_DUSN_QY = "dusn_qy";
    public static final String JSON_LAST_HRIGTH_DE = "max_cm_de";
    public static final String JSON_LAST_BDWGH_DE = "max_kg_de";
    public static final String JSON_CONTENT_TYP = "content_typ";
    public static final String JSON_BBSLIST = "bbslist";
    public static final String JSON_INFO_DAY = "info_day";
    public static final String JSON_INFO_TITLE_URL = "info_title_url";
    public static final String JSON_INFO_SUBJECT = "info_subject";
    public static final String JSON_CONTENT_TYP_MOBILE_IMG = "content_typ_mobile_img";
    public static final String JSON_VIEW_DAY = "view_day";

    public static final String JSON_AREA_DO = "area_do";
    public static final String JSON_AREA_SI = "area_si";

    public static final String JSON_NEWS_YN = "news_yn";
    public static final String JSON_NOTICE_YN = "notice_yn";
    public static final String JSON_HEAT_YN = "heat_yn";
    public static final String JSON_DIET_YN = "diet_yn";
    public static final String JSON_NOTITY_YN = "notity_yn";

    public static final String JSON_PUSH_TYP = "push_typ";
    public static final String JSON_INFO_SN = "info_sn";
    public static final String JSON_HEALTH_LIST = "health_list";
    public static final String JSON_MBER_GRAD = "mber_grad";

    public static final String JSON_MOTHER_PERIOD_WEEK = "mother_period_week";   //임신 주차
    public static final String JSON_DISEASE_ALERT_YN = "disease_alert_yn";
    public static final String JSON_EVENT_ALERT_YN = "event_alert_yn";
    public static final String JSON_REPLAY_ALERT_YN = "replay_alert_yn";
    public static final String JSON_MAIN_INTRO_YN = "main_intro_yn";


    // JSON_ARRAY
    public static final String JSON_GROWTH_LIST = "growth_list";
    public static final String JSON_GROWTH_MONTH_LIST = "growth_month_list";
    public static final String JSON_NOTICE = "notice";
    public static final String JSON_SET_LIST = "setlist";
    public static final String JSON_DiSEASE_RANK = "disease_rank";

    //아이심리
    public static final String JSON_CHL_SN_F = "CHL_SN";
    public static final String JSON_ML_MCODE_F = "ML_MCODE";
    public static final String JSON_PGSIZE_F = "PGSIZE";
    public static final String JSON_NOWPG_F = "NOWPG";
    public static final String JSON_M_CODE_F = "M_CODE"; //문항 코드
    public static final String JSON_MJ_SEQ_F = "MJ_SEQ";
    public static final String JSON_START_DATE_F = "START_DATE";
    public static final String JSON_END_DATE_F = "END_DATE";
    public static final String JSON_L_CODE_F = "L_CODE";

    // 현대해상 열나요
    public static final String JSON_API_CODE_F = "API_CODE";
    public static final String JSON_DOCNO_F = "DOCNO";
    public static final String JSON_REG_YN_F = "REG_YN";
    public static final String JSON_COUNT_F = "COUNT";
    public static final String JSON_DATA_F = "DATA";
    public static final String JSON_FEVER_SN_F = "FEVER_SN";
    public static final String JSON_INPUT_DE_F = "INPUT_DE";
    public static final String JSON_IS_WEARABLE_F = "IS_WEARABLE";
    public static final String JSON_INPUT_FEVER_F = "INPUT_FEVER";
    public static final String JSON_START_DE_F = "START_DE";
    public static final String JSON_SEL_DE_F = "SEL_DE";
    public static final String JSON_END_DE_F = "END_DE";
    public static final String JSON_P_YN = "P_YN";

    public static final String JSON_FEVER_F = "FEVER";
    public static final String JSON_DISEASE_F = "DISEASE";
    public static final String JSON_TYPE_F = "TYPE";
    public static final String JSON_INPUT_F = "INPUT";
    public static final String JSON_UPDATE_F = "UPDATE";
    public static final String JSON_DELETE_F = "DELETE";

    public static final String JSON_REMEDY_SN_F = "REMEDY_SN";
    public static final String JSON_INPUT_KIND_F = "INPUT_KIND";
    public static final String JSON_INPUT_TYPE_F = "INPUT_TYPE";
    public static final String JSON_INPUT_VOLUME_F = "INPUT_VOLUME";

    public static final String JSON_FEVER_RE_SN_F = "FEVER_RE_SN";
    public static final String JSON_INPUT_CODE_F = "INPUT_CODE";

    public static final String JSON_SYM_SN_F = "SYM_SN";
    public static final String JSON_DISE_SN_F = "DISE_SN";
    public static final String JSON_VAC_SN_F = "VAC_SN";
    public static final String JSON_MEMO_SN_F = "MEMO_SN";
    public static final String JSON_INPUT_NUM_F = "INPUT_NUM";
    public static final String JSON_INPUT_MEMO_F = "INPUT_MEMO";

    public static final String JSON_FILTER_1_F = "FILTER_1";
    public static final String JSON_FILTER_2_F = "FILTER_2";
    public static final String JSON_FILTER_3_F = "FILTER_3";
    public static final String JSON_FILTER_4_F = "FILTER_4";
    public static final String JSON_FILTER_5_F = "FILTER_5";
    public static final String JSON_FILTER_6_F = "FILTER_6";
    public static final String JSON_FILTER_7_F = "FILTER_7";

    public static final String JSON_DATA_SN_F = "DATA_SN";
    public static final String JSON_FILTER_F = "FILTER";

    public static final String JSON_MBER_SN_F = "MBER_SN";
    public static final String JSON_LOC_SN_F = "LOC_SN";
    public static final String JSON_LATITUDE_F = "LATITUDE";
    public static final String JSON_LONGITUDE_F = "LONGITUDE";
    public static final String JSON_ADDRESS_F = "ADDRESS";

    public static final String JSON_DZNUM = "DZNUM";
    public static final String JSON_DZNAME = "DZNAME";
    public static final String JSON_WEEKAGO_1 = "WEEKAGO_1";
    public static final String JSON_WEEKAGO_2 = "WEEKAGO_2";

    public static final String JSON_LOC_NM_1 = "LOC_NM_1";
    public static final String JSON_LOC_NM_2 = "LOC_NM_2";
    public static final String JSON_AVG_FEVER = "AVG_FEVER";
    public static final String JSON_LOC_1 = "LOC_1";
    public static final String JSON_LOC_2 = "LOC_2";

    public static final String METHOD_DISEASE_HX001 = "HX001";    // 유해질환 순위 및 위치정보

    public static final String JSON_APINM_HF001 = "HF001";
    public static final String JSON_APINM_HF002 = "HF002";
    public static final String JSON_APINM_HF003 = "HF003";
    public static final String JSON_APINM_HF004 = "HF004";

    public static final String JSON_APINM_HR001 = "HR001";
    public static final String JSON_APINM_HR002 = "HR002";
    public static final String JSON_APINM_HR003 = "HR003";
    public static final String JSON_APINM_HR004 = "HR004";

    //  public static final String JSON_APINM_HP001                   =   "HP001";
    //  public static final String JSON_APINM_HP002                   =   "HP002";

    public static final String JSON_APINM_HS001 = "HS001";
    public static final String JSON_APINM_HS002 = "HS002";

    public static final String JSON_APINM_HD001 = "HD001";
    public static final String JSON_APINM_HD002 = "HD002";

    public static final String JSON_APINM_HV001 = "HV001";
    public static final String JSON_APINM_HV002 = "HV002";

    public static final String JSON_APINM_HM001 = "HM001";
    public static final String JSON_APINM_HM002 = "HM002";

    public static final String JSON_APINM_HA001 = "HA001";

    public static final String JSON_APINM_HL001 = "HL001";
    public static final String JSON_APINM_HL002 = "HL002";

    public static final String JSON_APINM_HJ002 = "HJ002";

    public static final String JSON_APINM_HB002 = "HB002";

    /* 심리 음원 */
    public static final String JSON_APINM_HP001 = "HP001";

    /* 심리 메인 */
    public static final String JSON_APINM_HP002 = "HP002";

    /* 심리 결과 요청 */
    public static final String JSON_APINM_HP003 = "HP003";

    /* 심리 결과 (코멘트 포함)*/
    public static final String JSON_APINM_HP004 = "HP004";

    /* 심리 결과 리스트 */
    public static final String JSON_APINM_HP006 = "HP006";


    /* 심리 체크 문항 */
    public static final String JSON_APINM_HP007 = "HP007";

    /* 심리 결과 - 상황심리 (코멘트 포함)*/
    public static final String JSON_APINM_HP008 = "HP008";


    public static final String JSON_INPUT_KIND_0 = "0";
    public static final String JSON_INPUT_KIND_1 = "1";
    public static final String JSON_INPUT_KIND_2 = "2";
    public static final String JSON_INPUT_KIND_ALL = "3";

    public static final String JSON_HELP_FEVER = "json_help_fever";
    public static final String JSON_HELP_GROWTH = "json_help_growth";

    public static final String JSON_ADDRESS_DO = "address_do";
    public static final String JSON_ADDRESS_GU = "address_gu";

    public static final String JSON_AFT_BABY_VIEW = "aft_baby_view";
    public static final String JSON_MOTHER_BIG_VIEW = "mother_big_view";

    public static final String EXTRA_RESULT_CODE = "result_code";
    public static final String EXTRA_FEVER = "fever";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_SN = "sn";
    public static final String EXTRA_KIND = "kind";
    public static final String EXTRA_VOLUME = "volume";
    public static final String EXTRA_NUM = "num";
    public static final String EXTRA_MEMO = "memo";
    public static final String EXTRA_MEMO_TYPE = "memo_type";
    public static final String EXTRA_IS_EDIT = "is_edit";
    public static final String EXTRA_IS_TIMELIEN = "is_timelien";

    public static final String PACKAGE_CROISE = "tomato.temperature";
    public static final String PACKAGE_CROISE_MAIN = "tomato.temperature.MainActivity";
    public static final String SEND = "send";
    public static final String REPLY = "reply";
    public static final String FEVER_CHECK = "fever_check";
    public static final String MARKET_URL = "market://details?id=";

    //hsh
    public static final String JSON_MAIN_POP_LIST = "main_pop_list";
    public static final String JSON_MAIN_POP_DATE = "aft_main_pop_date";

    // API result Code
    public static final int API_SUCCESS = 0;        // 성공

    public static final int API_ERROR_ = 0;
    public static final int API_ERROR_SYSTEM_ERROR = 2;  // 시스템 오류로 인하여 중단
    public static final int API_ERROR_INPUT_DATA_ERROR = 3;  // 입력 데이터가 부족함

    public static final int API_ERROR_LOGIN_DROP_MEMBER = 1002;   // 로그인 실패 - 탈퇴한 회원
    public static final int API_ERROR_LOGIN_WRITE_INFO = 1003;   // 로그인 실패 - 정보기록 실패
    public static final int API_ERROR_LOGIN_SESSION = 1004;   // 로그인 실패 - 세션 생성 오류
    public static final int API_ERROR_LOGIN_TOKEN = 1005;      // 로그인 실패 - 세션 토큰 오류
    public static final int API_ERROR_LOGIN_INVALID_ID = 1006;   // 로그인 실패 - sns id 와 세션 토큰으로 받은 아이디가 다른 경우

    /*
    common get/set data
     */


    public CommonData(Context context) {
        this.mContext = context;
    }

    /**
     * CommonData 인스턴스 리턴
     *
     * @param context context
     * @return CommonData
     */
    public static CommonData getInstance(Context context) {
        if (_instance == null) {
            synchronized (CommonData.class) {
                if (_instance == null) {
                    _instance = new CommonData(context);
                }
            }
        }
        return _instance;
    }

    /**
     * preferences 삭제
     */
    public void deletePreferences() {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.clear();
        editor.commit();
    }

    /**
     * preference 권한 얻기
     *
     * @return
     */
    private SharedPreferences getSharedPreference()								// 데이터 가져오기
    {
        return mContext.getSharedPreferences("GnGCare", Activity.MODE_PRIVATE);
    }
    private SharedPreferences getSharedPreference(Context context)                                // 데이터 가져오기
    {
        return context.getSharedPreferences("GnGCare", Activity.MODE_PRIVATE);
    }

    /**
     * String 타입의 preferencedata 를 저장한다.
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * int 타입의 preferencedata 를 저장한다.
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Long 타입의 preferencedata 를 저장한다.
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * boolean 타입의 preferencedata 를 저장한다.
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * float 타입의 preferencedata 를 저장한다
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, float value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    private void setSharedPreferenceArrayString(String key, ArrayList<String> stringList) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key, TextUtils.join("‚‗‚", stringList)).apply();
        editor.commit();
    }

    // 이메일 저장
    public void setMberId(String email) {
        setSharedPreferenceData(JSON_MBER_ID, email);
    }

    public String getMberId() {
        return getSharedPreference().getString(JSON_MBER_ID, "");
    }


    // 비밀번호 저장하기
    public void setMberPwd(String password) {
        /* 암호화 주석
        String seed = GetDevicesUUID();
        GLog.i("setPassword() getDevicesUUid = " + seed);
        try {
            setSharedPreferenceData(JSON_MBER_PWD, SimpleCrypto.encrypt(seed, password));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        setSharedPreferenceData(JSON_MBER_PWD, password);
    }

    // 비밀번호 가져오기
    public String getMberPwd() {
        /* 복호화 주석
        String password = getSharedPreference().getString(JSON_MBER_PWD, "");

        if ( password.equals("") )
            return "";

        String seed = GetDevicesUUID();
        GLog.i("getPassword()  getDevicesUUid = " + seed);
        try {
            return SimpleCrypto.decrypt(seed, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        */
        return getSharedPreference().getString(JSON_MBER_PWD, "pwd1234");
    }

    // 로그인 유지 세션코드
    public void setSessionCode(String sessionCode) {
        setSharedPreferenceData(JSON_SESSION_CODE, sessionCode);
    }

    public String getSessionCode() {
        return getSharedPreference().getString(JSON_SESSION_CODE, "");
    }

    // 회원 고유번호
    public void setMemberId(int memberId) {
        setSharedPreferenceData(JSON_MEMBER_ID, memberId);
    }

    public int getMemberId() {
        return getSharedPreference().getInt(JSON_MEMBER_ID, 0);
    }

    // 회원인증 유무
    public void setMemberCertifi(boolean bool) {
        setSharedPreferenceData(JSON_MEMBER_CERTIFI, bool);
    }

    public boolean getMemberCertifi() {
        return getSharedPreference().getBoolean(JSON_MEMBER_CERTIFI, false);
    }

    // 회원가입 후 코드번호
    public void setMberNo(String no) {
        setSharedPreferenceData(JSON_MBER_NO, no);
    }

    public String getMberNo() {
        return getSharedPreference().getString(JSON_MBER_NO, "");
    }

    // start 로그인시 데이터
    // 부모 회원키값
    public void setMberSn(String sn) {
        setSharedPreferenceData(JSON_MBER_SN, sn);
    }

    public String getMberSn() {
        return getSharedPreference().getString(JSON_MBER_SN, "");
    } // 로그인 인증 될때까지 홍태진 차장님 고유번호 기본값으로 설정

    // 자녀 회원키값
    public void setChlSn(String sn) {
        setSharedPreferenceData(JSON_CHL_SN, sn);
    }

    public String getChlSn() {
        return getSharedPreference().getString(JSON_CHL_SN, "");
    }

    // 내 이름
    public void setMberNm(String nick) {
        setSharedPreferenceData(JSON_MBER_NM, nick);
    }

    public String getMberNm() {
        return getSharedPreference().getString(JSON_MBER_NM, "");
    }

    // 회원 전화번호
    public void setPhoneNumber(String phone) {
        setSharedPreferenceData(JSON_MBER_HP, phone);
    }

    public String getPhoneNumber() {
        return getSharedPreference().getString(JSON_MBER_HP, "");
    }

    // 생년월일
    public void setBirthDay(String birth) {
        setSharedPreferenceData(JSON_MBER_BRTHDY, birth);
    }

    public String getBirthDay() {
        return getSharedPreference().getString(JSON_MBER_BRTHDY, "");
    }

    // 내국인 &외국인
    public void setMberNation(String nation) {
        setSharedPreferenceData(JSON_MBER_NATION, nation);
    }

    public String getMberNation() {
        return getSharedPreference().getString(JSON_MBER_NATION, "");
    }

    // 회원 성별
    public void setGender(String gender) {
        setSharedPreferenceData(JSON_MBER_SEX, gender);
    }

    public String getGender() {
        return getSharedPreference().getString(JSON_MBER_SEX, "1");
    }

    // 회원 직업
    public void setMberJob(String job) {
        setSharedPreferenceData(JSON_MBER_JOB_YN, job);
    }

    public String getMberJob() {
        return getSharedPreference().getString(JSON_MBER_JOB_YN, "Y");
    }

    // 동의여부
    public void setMberAgreementYn(String job) {
        setSharedPreferenceData(JSON_MBER_AGREEMENT_YN, job);
    }

    public String getMberAgreementYn() {
        return getSharedPreference().getString(JSON_MBER_AGREEMENT_YN, "Y");
    }


    // 정회원 여부
    public void setMberGrad(String grad) {
        setSharedPreferenceData(JSON_MBER_GRAD, grad);
    }

    public String getMberGrad() {
        return getSharedPreference().getString(JSON_MBER_GRAD, "");
    }

    // 계약자 or 피보험자
    public void setLoginType(int type) {
        setSharedPreferenceData(JSON_LOGIN_TYPE, type);
    }

    public int getLoginType() {
        return getSharedPreference().getInt(JSON_LOGIN_TYPE, 0);
    }

    // 자녀수
    public void setChildCnt(int count) {
        setSharedPreferenceData(JSON_CHIDRN_CNT, count);
    }

    public int getChildCnt() {
        return getSharedPreference().getInt(JSON_CHIDRN_CNT, 0);
    }

    // 고유키값
    public void setJuminNum(String num) {
        setSharedPreferenceData(JSON_JUMINNUM, num);
    }

    public String getJuminNum() {
        return getSharedPreference().getString(JSON_JUMINNUM, "");
    }

    // 회원 사진
    public void setPhoto(String url) {
        setSharedPreferenceData(JSON_PHOTO, url);
    }

    public String getPhoto() {
        return getSharedPreference().getString(JSON_PHOTO, "");
    }

    // 자동로그인
    public void setAutoLogin(boolean bool) {
        setSharedPreferenceData(JSON_AUTO_LOGIN, bool);
    }

    public boolean getAutoLogin() {
        return getSharedPreference().getBoolean(JSON_AUTO_LOGIN, true);
    }

    // 아이디 저장
    public void setRememberId(boolean bool) {
        setSharedPreferenceData(JSON_REMEMBER_ID, bool);
    }

    public boolean getRememberId() {
        return getSharedPreference().getBoolean(JSON_REMEMBER_ID, true);
    }

    // 자녀정보
    public void setChldrn(String chldrn) {
        setSharedPreferenceData(JSON_CHLDRN, chldrn);
    }

    public String getChldrn() {
        return getSharedPreference().getString(JSON_CHLDRN, "");
    }

    // 자녀선택 했는지 유무
    public void setSelect(boolean bool) {
        setSharedPreferenceData(JSON_SELECT, bool);
    }

    public boolean getSelect() {
        return getSharedPreference().getBoolean(JSON_SELECT, false);
    }

    // 자녀선택 자녀키
    public void setSelectChildSn(String sn) {
        setSharedPreferenceData(JSON_SELECT_CHILD_SN, sn);
    }

    public String getSelectChildSn() {
        return getSharedPreference().getString(JSON_SELECT_CHILD_SN, "");
    }

    // 하이플래너 전화번호
    public void setHiPlannerHp(String sn) {
        setSharedPreferenceData(JSON_HIPLANNER_HP, sn);
    }

    public String getHiPlannerHp() {
        return getSharedPreference().getString(JSON_HIPLANNER_HP, "");
    }

    // 체중계 여부
    public void setTempDivices(String sn) {
        setSharedPreferenceData(JSON_THERMOMETERCHK, sn);
    }

    public String getTempDivices() {
        return getSharedPreference().getString(JSON_THERMOMETERCHK, "");
    }

    // 엄마체중
    public void setMotherWeight(String sn) {
        setSharedPreferenceData(JSON_MOTHER_WEIGHT, sn);
    }

    public String getMotherWeight() {
        return getSharedPreference().getString(JSON_MOTHER_WEIGHT, "");
    }

    // BMI
    public void setBmi(String bmi) {
        setSharedPreferenceData(JSON_BMI, bmi);
    }

    public String getBmi() {
        return getSharedPreference().getString(JSON_BMI, "");
    }

    // BMI_KIND 저제충군 ~
    public void setBmi_Kind(String bmi_kind) {
        setSharedPreferenceData(JSON_BMI_KIND, bmi_kind);
    }

    public String getBmi_Kind() {
        return getSharedPreference().getString(JSON_BMI_KIND, "");
    }

    // 체중 상태
    public void setKg_Kind(String sn) {
        setSharedPreferenceData(JSON_KG_KIND, sn);
    }

    public String getKg_Kind() {
        return getSharedPreference().getString(JSON_KG_KIND, "");
    }

    // 즐겨찾기
    public void setMain_Category(String sn) {
        setSharedPreferenceData(JSON_MAIN_CATEGORY, sn);
    }

    public String getMain_Category() {
        return getSharedPreference().getString(JSON_MAIN_CATEGORY, "");
    }

    // 체중 상태
    public void setMberKg(String sn) {
        setSharedPreferenceData(JSON_MBER_KG, sn);
    }

    public String getMberKg() {
        return getSharedPreference().getString(JSON_MBER_KG, "");
    }

    // 체중계 여부
    public void setWeighingchk(String sn) {
        setSharedPreferenceData(JSON_WEIGHTNGCHK, sn);
    }

    public String getWeighingchk() {
        return getSharedPreference().getString(JSON_WEIGHTNGCHK, "");
    }

    // 엄마목표체중
    public void setMotherGoalWeight(String sn) {
        setSharedPreferenceData(JSON_MOTHER_GOAL_WEIGHT, sn);
    }

    public String getMotherGoalWeight() {
        return getSharedPreference().getString(JSON_MOTHER_GOAL_WEIGHT, "");
    }

    // 엄마 목표칼로리
    public void setMotherGoalCal(String sn) {
        setSharedPreferenceData(JSON_MOTHER_GOAL_CAL, sn);
    }

    public String getMotherGoalCal() {
        return getSharedPreference().getString(JSON_MOTHER_GOAL_CAL, "");
    }

    // 엄마 목표스탭
    public void setMotherGoalStep(String sn) {
        setSharedPreferenceData(JSON_MOTHER_GOAL_STEP, sn);
    }

    public String getMotherGoalStep() {
        return getSharedPreference().getString(JSON_MOTHER_GOAL_STEP, "");
    }

    // 출산예정일
    public void setMberBirthDueDe(String sn) {
        setSharedPreferenceData(JSON_MBER_BIRTH_DUE_DE, sn);
    }

    public String getMberBirthDueDe() {
        return getSharedPreference().getString(JSON_MBER_BIRTH_DUE_DE, "");
    }

    // 출산일
    public void setMberChlBirthDe(String sn) {
        setSharedPreferenceData(JSON_MBER_CHL_BIRTH_DE, sn);
    }

    public String getMbeChlBirthDe() {
        return getSharedPreference().getString(JSON_MBER_CHL_BIRTH_DE, "");
    }

    // 어린이 여부
    public void setIamChild(String sn) {
        setSharedPreferenceData(JSON_I_AM_CHILD, sn);
    }

    public String getIamChild() {
        return getSharedPreference().getString(JSON_I_AM_CHILD, "");
    }

    // 임신전 키
    public void setBefCm(String sn) {
        setSharedPreferenceData(JSON_BEF_CM, sn);
    }

    public String getBefCm() {
        return getSharedPreference().getString(JSON_BEF_CM, "");
    }

    // 임신전 체중
    public void setBefKg(String sn) {
        setSharedPreferenceData(JSON_BEF_KG, sn);
    }

    public String getBefKg() {
        return getSharedPreference().getString(JSON_BEF_KG, "");
    }


    // 엄마 데이터 저장 여부
    public void setHpMjYn(String sn) {
        setSharedPreferenceData(JSON_HP_MJ_YN, sn);
    }

    public String getHpMjYn() {
        return getSharedPreference().getString(JSON_HP_MJ_YN, "");
    }

    // 엄마 데이터 저장 여부
    public void setHpMjYnJun(Boolean sn) {
        setSharedPreferenceData(JSON_HP_MJ_YN_JUN, sn);
    }

    public Boolean getHpMjYnJun() {
        return getSharedPreference().getBoolean(JSON_HP_MJ_YN_JUN, true);
    }

    // 임신중 여부
    public void setbirth_chl_yn(String sn) {
        setSharedPreferenceData(JSON_BIRTH_CHL_YN, sn);
    }

    public String getbirth_chl_yn() {
        return getSharedPreference().getString(JSON_BIRTH_CHL_YN, "");
    }

    // 커뮤니티 닉네임
    public void setMberNick(String sn) {
        setSharedPreferenceData(JSON_MBER_NICK, sn);
    }

    public String getMberNick() {
        return getSharedPreference().getString(JSON_MBER_NICK, "");
    }


    // 마케팅동의
    public void setMarketing_yn(String sn) {
        setSharedPreferenceData(JSON_MARKETING_YN, sn);
    }

    public String getMarketing_yn() {
        return getSharedPreference().getString(JSON_MARKETING_YN, "");
    }

    // 마케팅 동의날짜
    public void setMarketing_yn_de(String sn) {
        setSharedPreferenceData(JSON_MARKETING_YN_DE, sn);
    }

    public String getMarketing_yn_de() {
        return getSharedPreference().getString(JSON_MARKETING_YN_DE, "");
    }


    // 활동
    public void setActqy(String sn) {
        setSharedPreferenceData(JSON_ACTQY, sn);
    }

    public String getActqy() {
        return getSharedPreference().getString(JSON_ACTQY, "");
    }

    // end 로그인시 데이터

    // 회원가입 스탭 ( 0, 1, 2, 3, 4 )
    public void setJoinStep(int step) {
        setSharedPreferenceData(JSON_JOIN_STEP, step);
    }

    public int getJoinStep() {
        return getSharedPreference().getInt(JSON_JOIN_STEP, 0);
    }

    // 로그인 타입
    public void setJoinType(String joinType) {
        setSharedPreferenceData(JSON_JOIN_TYPE, joinType);
    }

    public String getJoinType() {
        return getSharedPreference().getString(JSON_JOIN_TYPE, "");
    }

    // 키
    public void setHeight(float height) {
        setSharedPreferenceData(JSON_HEIGHT, height);
    }

    public float getHeight() {
        return getSharedPreference().getFloat(JSON_HEIGHT, 0f);
    }

    // 팝업 이벤트 ID
    public void setPopupEventId(int id) {
        setSharedPreferenceData(JSON_POPUP_EVENT_VIEW_ID, id);
    }

    public int getPopupEventId() {
        return getSharedPreference().getInt(JSON_POPUP_EVENT_VIEW_ID, -1);
    }

    // 오늘 팝업 봤는지 유무 ( 하루에 한번만 보인다. )
    public void setPopupEventTodayRead(boolean read) {
        setSharedPreferenceData(JSON_TODAY_POPUP_EVENT, read);
    }

    public boolean getPopupEventTodayRead() {
        return getSharedPreference().getBoolean(JSON_TODAY_POPUP_EVENT, false);
    }

    // 팝업 이벤트 봤는지 유무 ( 로그인 할때마다 1번만 보인다 )
    public void setPopupEventRead(boolean read) {
        setSharedPreferenceData(JSON_IS_POPUP_EVENT_READ, read);
    }

    public boolean getPopupEventRead() {
        return getSharedPreference().getBoolean(JSON_IS_POPUP_EVENT_READ, false);
    }

    // 팝업이벤트 그만보기 체크시간
    public void setPopupEventReadTime(String readTime) {
        setSharedPreferenceData(JSON_POPUP_EVENT_READ_TIME, readTime);
    }

    public String getPopupEventReadTime() {
        return getSharedPreference().getString(JSON_POPUP_EVENT_READ_TIME, "");
    }

    // 회원사명
    public void setCmpnyNm(String name) {
        setSharedPreferenceData(JSON_CMPNY_NM, name);
    }

    public String getCmpnyNm() {
        return getSharedPreference().getString(JSON_CMPNY_NM, "");
    }

    //콜센터 번호
    public void setCmpnyArs(String num) {
        setSharedPreferenceData(JSON_CMPNY_ARS, num);
    }

    public String getCmpnyArs() {
        return getSharedPreference().getString(JSON_CMPNY_ARS, "");
    }

    // 인증 URL
    public void setLoginUrl(String url) {
        setSharedPreferenceData(JSON_LOGINURL, url);
    }

    public String getLoginUrl() {
        return getSharedPreference().getString(JSON_LOGINURL, "");
    }

    // api URL
    public void setApiUrl(String url) {
        setSharedPreferenceData(JSON_APIURL, url);
    }

    public String getApiUrl() {
        return getSharedPreference().getString(JSON_APIURL, "");
    }

    // 최신버전
    public void setAppVersion(String ver) {
        setSharedPreferenceData(JSON_APPVERSION, ver);
    }

    public String getAppVersion() {
        return getSharedPreference().getString(JSON_APPVERSION, "");
    }

    // update URL
    public void setUpdateUrl(String url) {
        setSharedPreferenceData(JSON_UPDATEURL, url);
    }

    public String getUpdateUrl() {
        return getSharedPreference().getString(JSON_UPDATEURL, "");
    }

    // 파일업로드 호출링크
    public void setCmpnyFileCoursUrl(String url) {
        setSharedPreferenceData(JSON_CMPNY_FILE_COURS, url);
    }

    public String getCmpnyFileCoursUrl() {
        return getSharedPreference().getString(JSON_CMPNY_FILE_COURS, "");
    }

    // 사용가이드 확인 유무
    public void setGuideCheck(boolean bool) {
        setSharedPreferenceData(JSON_SERVICE_START, bool);
    }

    public boolean getGuideCheck() {
        return getSharedPreference().getBoolean(JSON_SERVICE_START, false);
    }

    // 체온 알림 설정 유무
    public void setPushAlarm(boolean bool) {
        setSharedPreferenceData(JSON_FX_YN, bool);
    }

    public boolean getPushAlarm() {
        return getSharedPreference().getBoolean(JSON_FX_YN, false);
    }

    // 일반공지 알림 설정 유무
    public void setNoticePushAlarm(boolean bool) {
        setSharedPreferenceData(JSON_NOTICE_YN, bool);
    }

    public boolean getNoticePushAlarm() {
        return getSharedPreference().getBoolean(JSON_NOTICE_YN, false);
    }

    // 건강뉴스 알림 설정 유무
    public void setNewsPushAlarm(boolean bool) {
        setSharedPreferenceData(JSON_NEWS_YN, bool);
    }

    public boolean getNewsPushAlarm() {
        return getSharedPreference().getBoolean(JSON_NEWS_YN, false);
    }

    // 열지도 알림 설정 유무
    public void setMapPushAlarm(boolean bool) {
        setSharedPreferenceData(JSON_HEAT_YN, bool);
    }

    public boolean getMapPushAlarm() {
        return getSharedPreference().getBoolean(JSON_HEAT_YN, false);
    }

    // 다이어트 프로그램 독려 알림 설정 유무
    public void setDietPushAlarm(boolean bool) {
        setSharedPreferenceData(JSON_DIET_YN, bool);
    }

    public boolean getDietPushAlarm() {
        return getSharedPreference().getBoolean(JSON_DIET_YN, false);
    }

    // 알림게시판 설정 유무
    public void setNotityPushAlarm(boolean bool) {
        setSharedPreferenceData(JSON_NOTITY_YN, bool);
    }

    public boolean getNotityPushAlarm() {
        return getSharedPreference().getBoolean(JSON_NOTITY_YN, false);
    }

    // 유행성질환 알림여부
    public void setDisease_alert_yn(boolean bool) {
        setSharedPreferenceData(JSON_DISEASE_ALERT_YN, bool);
    }

    public boolean getDisease_alert_yn() {
        return getSharedPreference().getBoolean(JSON_DISEASE_ALERT_YN, false);
    }

    // 메인 인트로 띄움여부
    public void setMainItro_yn(boolean bool) {
        setSharedPreferenceData(JSON_MAIN_INTRO_YN, bool);
    }

    public boolean getMainItro_yn() {
        return getSharedPreference().getBoolean(JSON_MAIN_INTRO_YN, false);
    }

    // 이벤트 알림여부
    public void setEvent_alert_yn(boolean bool) {
        setSharedPreferenceData(JSON_EVENT_ALERT_YN, bool);
    }

    public boolean getEvent_alert_yn() {
        return getSharedPreference().getBoolean(JSON_EVENT_ALERT_YN, false);
    }

    // 게시글, 댓글 알림 여부
    public void setReplay_alert_yn(boolean bool) {
        setSharedPreferenceData(JSON_REPLAY_ALERT_YN, bool);
    }

    public boolean getReplay_alert_yn() {
        return getSharedPreference().getBoolean(JSON_REPLAY_ALERT_YN, false);
    }

    // 위치정보 동의 여부
    public void setLocation_yn(String sn) {
        setSharedPreferenceData(JSON_LOCATION_YN, sn);
    }

    public String getLocation_yn() {
        return getSharedPreference().getString(JSON_LOCATION_YN, "");
    }

    // 다이어트 신청 여부
    public void setDiet_program_req_yn(String sn) {
        setSharedPreferenceData(JSON_DIET_PROGRAM_REQ_YN, sn);
    }

    public String getDiet_program_req_yn() {
        return getSharedPreference().getString(JSON_DIET_PROGRAM_REQ_YN, "");
    }

    // 알림 벨소리
    public void setAlarmMode(int mode) {
        setSharedPreferenceData(JSON_FX_MTH, mode);
    }

    public int getAlarmMode() {
        return getSharedPreference().getInt(JSON_FX_MTH, CommonData.PUSH_MODE_DEFAULT);
    }

    // 열나요 타임라인 도움말 최초 보기
    public void setFeverTimeLineHelp(boolean bool) {
        setSharedPreferenceData(JSON_HELP_FEVER, bool);
    }

    public boolean getFeverTimeLineHelp() {
        return getSharedPreference().getBoolean(JSON_HELP_FEVER, false);
    }

    // 성장 타임라인 도움말 최초 보기
    public void setGrowthTimeLineHelp(boolean bool) {
        setSharedPreferenceData(JSON_HELP_GROWTH, bool);
    }

    public boolean getGrowthTimeLineHelp() {
        return getSharedPreference().getBoolean(JSON_HELP_GROWTH, false);
    }

    // 게시판 URL 로 조회 여부 확인
    public void setBBSUrlShowCheck(String url, boolean is_show) {
        setSharedPreferenceData(url, is_show);
    }

    public boolean getBBSUrlShowCheck(String url) {
        return getSharedPreference().getBoolean(url, false);
    }

    // 열지도 주소 저장
    public void setAddressDo(String address) {
        setSharedPreferenceData(JSON_ADDRESS_DO, address);
    }

    public String getAddressDo() {
        return getSharedPreference().getString(JSON_ADDRESS_DO, "");
    }

    public void setAddressGu(String address) {
        setSharedPreferenceData(JSON_ADDRESS_GU, address);
    }

    public String getAddressGu() {
        return getSharedPreference().getString(JSON_ADDRESS_GU, "");
    }

    // 태아 출산 여부 팝업 보여주기 여부
    public void setAfterBabyPopupShowCheck(boolean is_show) {
        setSharedPreferenceData(JSON_AFT_BABY_VIEW, is_show);
    }

    public boolean getAfterBabyPopupShowCheck() {
        return getSharedPreference().getBoolean(JSON_AFT_BABY_VIEW, false);
    }

    // 예측 체중 여부 팝업 보여주기 여부
    public void setMotherBigPopupShowCheck(boolean is_show) {
        setSharedPreferenceData(JSON_MOTHER_BIG_VIEW, is_show);
    }

    public boolean getMotherBigPopupShowCheck() {
        return getSharedPreference().getBoolean(JSON_MOTHER_BIG_VIEW, false);
    }

    // 메인 온초 측정 안내 팝업 보여주기 여부
    public void setAfterMainPopupShowCheck(String date) {
        setSharedPreferenceData(JSON_MAIN_POP_DATE, date);
    }

    public String getAfterMainPopupShowCheck() {
        return getSharedPreference().getString(JSON_MAIN_POP_DATE, "");
    }


    //    // 임신여부
    public void setMotherIsPregnancy(String yn) {
        setSharedPreferenceData(JSON_MOTHER_IS_PREGNANT, yn);
    }

    public String getMotherIsPregnancy() {
        return getSharedPreference().getString(JSON_MOTHER_IS_PREGNANT, "");
    }

    // 막내 생일
    public void setLastChlBirth(String yn) {
        setSharedPreferenceData(JSON_LAST_CHL_BIRTH, yn);
    }

    public String getLastChlBirth() {
        return getSharedPreference().getString(JSON_LAST_CHL_BIRTH, "0");
    }

    // 단태임신, 다태임신 여부
    public void setMberChlType(String yn) {
        setSharedPreferenceData(JSON_MBER_CHL_TYPE, yn);
    }

    public String getMberChlType() {
        return getSharedPreference().getString(JSON_MBER_CHL_TYPE, "");
    }


    // 임신주차
    public void setMberPeriodWeek(String yn) {
        setSharedPreferenceData(JSON_MOTHER_PERIOD_WEEK, yn);
    }

    public String getMberPeriodWeek() {
        return getSharedPreference().getString(JSON_MOTHER_PERIOD_WEEK, "");
    }


    // 링크 여부
    public void setLink(String yn) {
        setSharedPreferenceData(SharedPref.LINK_SERVICE_DATA, yn);
    }

    public String getLink() {
        return getSharedPreference().getString(SharedPref.LINK_SERVICE_DATA, "");
    }

    public void setLink1(String yn) {
        setSharedPreferenceData(SharedPref.LINK_CONTENT_DATA1, yn);
    }

    public String getLink1() {
        return getSharedPreference().getString(SharedPref.LINK_CONTENT_DATA1, "");
    }

    //이벤트 팝업 오늘하루 안보기
    public void setEventNotSee(String yn) {
        setSharedPreferenceData(SharedPref.EVENT_NOT_SEE, yn);
    }

    public String getEventNotSee() {
        return getSharedPreference().getString(SharedPref.EVENT_NOT_SEE, "");
    }
}