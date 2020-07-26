package com.greencross.gctemperlib.greencare.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private final String TAG = SharedPref.class.getSimpleName();
	
	private static SharedPref instance;
	private static Context mContext;
	
	public static final String PREF_NAME = "greencross";


    public static final String PREF_APP_TOKEN = "pref_app_token";     // 라이브러리 인증키
    public static final String PREF_PUSH_TOKEN = "pref_push_token";   // 푸시키
    public static final String PREF_CUST_NO = "pref_cust_no";         // 사용자 번호
    public static final String PREF_GENDER = "pref_gender";     // 사용자 성별
    public static final String MBER_SN = "mber_sn";         // 저장된 아이디
    public static final String TEMPER = "temper";         // 최근 저장된 온도


    public static String HEALTH_MESSAGE_HEALTH = "health_message_health"; // 건강메시지
    public static String HEALTH_MESSAGE_SUGAR = "health_message_sugar"; // 건강메시지
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
