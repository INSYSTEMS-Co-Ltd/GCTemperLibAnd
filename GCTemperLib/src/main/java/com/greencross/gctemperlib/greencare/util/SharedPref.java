package com.greencross.gctemperlib.greencare.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private final String TAG = SharedPref.class.getSimpleName();
	
	private static SharedPref instance;
	private static Context mContext;
	
	public static final String PREF_NAME = "greencross";


    public static String MBER_SN = "mber_sn";         // 저장된 아이디

    public static String SAVED_LOGIN_ID = "saved_login_id";         // 저장된 아이디
//    public static String IS_SAVED_LOGIN_ID = "is_saved_login_id";   // 아이디 저장 여부
    public static String SAVED_LOGIN_PWD = "saved_login_pwd";       // 저장된 아이디
    public static String IS_LOGIN_SUCEESS = "is_login_suceess";     // 로그인 성공 여부
    public static String IS_AUTO_LOGIN = "is_auto_login";           // 자동 로그인 여부
    public static String MAIN_CARD_LIST = "main_card_list";         // 메인화면 카드 리스트

    public static String APP_CONNECT_TIME2 = "app_connect_time2";     // App 접속한 시간 기록
    public static String IS_GOOGLE_FITNESS_AUTH = "is_google_fitness_auth";     // 구글 피트니스 인증 여부

    public static String IS_SAVED_HEALTH_MESSAGE_DB = "is_saved_health_message_db"; // 3개월치 건강메시지

    public static String IS_SAVED_MEAL_DB = "is_saved_meal_db";           // 3개월치 식사 데이터
    public static String IS_SAVED_FOOD_DB = "is_saved_food_db"; // 3개월치 음식 데이터
    public static String LOAD_MAIN_DATA = "load_main_data";     // 3개월치 데이터

    public static String INTRO_FOOD_DB = "intro_food_db";     // 기본 음식 데이터

    public static String STEP_DATA_SOURCE_TYPE = "step_data_source_type_2"; // 걸음 데이터소스(구글, 밴드)
    public static String IS_REGIST_GOOGLE_FITNESS = "is_regist_google_fitness_2";     // 구글피트니스 저장 여부

    public static String SPLASH_PERMISSION_CHECK = "splash_permission_check";     // 기본 음식 데이터
    public static String SPLASH_BUILD_GOOGLE_ACCOUNT = "splash_build_google_account";     // 구글 인증 여부

    public static String PRESURE_LAST_SAVE_TIME = "presure_last_save_time";
    public static String PRESURE_AFTER_COUNT = "presure_after_count";   // 고혈압 전단계 1기 카운트
    public static String PRESURE_AFTER_TIME = "presure_after_time";     // 고혈압 전단계 1기 시간
    public static String PRESURE_ONESTEP_COUNT = "presure_onestep_count";   // 고혈압 1단계 카운트
    public static String PRESURE_ONESTEP_TIME = "presure_onestep_time";     // 고혈압 1단계 타임
    public static String PRESURE_TWOSTEP_COUNT = "presure_twostep_count";   // 고혈압 2단계 카운트
    public static String PRESURE_TWOSTEP_TIME = "presure_twostep_time";     // 고혈압 2단계 타임

    public static String HEALTH_MESSAGE_HEALTH = "health_message_health"; // 건강메시지
    public static String HEALTH_MESSAGE_SUGAR = "health_message_sugar"; // 건강메시지
    public static String HEALTH_MESSAGE_CONFIRIM = "health_message_confirm"; // 건강메시지

    public static String SUGAR_OVER_CHECK = "sugar_over_check";
    public static String SUAGR_OVER_TIME = "sugar_over_time";

    public static String STEP_TOTAL_COUNT = "step_total_count";               // 걸음수 총카운트
    public static String STEP_TOTAL_SAVEDATE = "step_total_savedate";          // 날짜를 초기화를 위한값
    public static String STEP_DISTANCE_TOTAL = "step_distance_total";
    public static String STEP_CALRORI_TOTAL = "step_calrori_total";

    public static String IS_DRUG_REQUEST = "is_drug_request"; // 검사 신청 여부

    public static String SAVE_LOAD_TODAY = "save_load_today";

    public static String HEALTH_INFO_NEW = "health_info_new"; //건강정보 서버 업데이트 날짜

    public static String FINGER_N_STEP = "FINGER_N_STEP"; // 핑거앤 대화 종류

    public static String LINK_SERVICE_DATA = "LINK_SERVICE_DATA"; //문자 sms
    public static String LINK_CONTENT_DATA1 = "LINK_CONTENT_DATA1"; //문자 sms

    public static String SEND_LOG_DATE = "SEND_LOG_DATE"; //클릭 로그 날짜

    public static String MOTHER_WEIGHT_TEMP = "MOTHER_WEIGHT_TEMP"; //엄마 체중 임시저장

    public static String EVENT_NOT_SEE = "EVENT_NOT_SEE"; //오늘 하루 안보기 이벤트 팝업


	public static SharedPref getInstance(Context context) {
        mContext = context;
		if (instance == null) {
			instance = new SharedPref();
		}
		return instance;
	}

//	public void initContext(Context context) {
//        mContext = context;
//    }

	// 값 불러오기
    public String getPreferences(String key){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
    
 // 값 불러오기
    public int getPreferences(String key, int defValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, defValue);
    }

    // 값 불러오기
    public float getPreferences(String key, float defValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getFloat(key, defValue);
    }
 // 값 불러오기
    public boolean getPreferences(String key, boolean defValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defValue);
    }
    
    // 값 저장하기
    public void savePreferences(String key, String val){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, val);
        editor.commit();
    }
    
 // 값 저장하기
    public void savePreferences(String key, int val){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    // 값 저장하기
    public void savePreferences(String key, float val){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, val);
        editor.commit();
    }

    // 값 저장하기
    public void savePreferences(String key, boolean val){
        if (mContext == null) {
            Logger.e(TAG, "mContext is null");
        } else {
            SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(key, val);
            editor.commit();
        }
    }
     
    // 값(Key Data) 삭제하기
    public void removePreferences(String key){
        if (mContext == null) {
            Logger.e(TAG, "mContext is null");
        } else {
            SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(key);
            editor.commit();
        }
    }
     
    // 값(ALL Data) 삭제하기
    public void removeAllPreferences(){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
