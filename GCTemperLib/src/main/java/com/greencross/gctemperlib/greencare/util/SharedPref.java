package com.greencross.gctemperlib.greencare.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.greencross.gctemperlib.hana.GCAlramType;
import com.greencross.gctemperlib.hana.network.tr.hnData.Tr_Login;
import com.greencross.gctemperlib.util.SimpleCrypto;

public class SharedPref {
    private final String TAG = SharedPref.class.getSimpleName();

    private static SharedPref instance;
    private static Context mContext;

    public static final String PREF_NAME = "greencross";


    public static final String PREF_APP_TOKEN = "pref_app_token";                       // 라이브러리 인증키
    public static final String PREF_PUSH_TOKEN = "pref_push_token";                     // 푸시키
    public static final String TEMPER_INTRO_VIEW_SHOW = "TEMPER_INTRO_VIEW_SHOW";       // 지도화면 안내화면 표시여부
    public static final String LOCATION_PERMISSION_FIRST_SHOW = "LOCATION_PERMISSION_FIRST_SHOW";       // 체온관리 권한 설정 표시 여부 (최초1회만)


    /* 로그인 정보 */
    public static final String PREF_CUST_NO = "pref_cust_no";         // 사용자 번호
    private final String LOGIN_JUMINNUM = "juminnum";
    private final String LOGIN_MEMNAME = "memname";
    private final String LOGIN_HPNUM = "hpnum";
    private final String LOGIN_NCRGD_YN = "ncrgd_yn";
    private final String LOGIN_NCRGD_DE = "ncrgd_de";
    private final String LOGIN_AREA_THMT_YN = "area_thmt_yn";
    private final String LOGIN_AREA_THMT_DE = "area_thmt_de";
    private final String LOGIN_HEAT_SI = "heat_si";
    private final String LOGIN_HEAT_DO = "heat_do";
    private final String LOGIN_NEW_APPVER = "new_appver";
    private final String LOGIN_NEW_LINK = "new_link";
    private final String LOGIN_INDATE = "indate";
    private final String LOGIN_ENDDATE = "enddate";
    private final String LOGIN_CMPNY_UB_CODE = "cmpny_ub_code";
    private final String LOGIN_STATUS = "status";
    private final String LOGIN_DOCNO = "docno";
    private final String LOGIN_RESULTCODE = "resultcode";
    private final String LOGIN_MESSAGE = "message";

    private String CRYPTO_SEED_PASSWORD = "gchealthcare";   // 암호화 하기위한 값

    private Tr_Login mLogin;

    public static SharedPref getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new SharedPref();
        }
        return instance;
    }

    // 값 불러오기
    public String getPreferences(String key) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String val = pref.getString(key, "");
//        try {
////            String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
//            SimpleCrypto.decrypt(CRYPTO_SEED_PASSWORD, val);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return val;
    }

    // 값 불러오기
    public int getPreferences(String key, int defValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, defValue);
    }

    // 값 불러오기
    public float getPreferences(String key, float defValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getFloat(key, defValue);
    }

    // 값 불러오기
    public boolean getPreferences(String key, boolean defValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defValue);
    }

    // 값 저장하기
    public void savePreferences(String key, String val) {
//        try {
////            String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
//            SimpleCrypto.encrypt(CRYPTO_SEED_PASSWORD, val);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, val);
        editor.commit();
    }

    // 값 저장하기
    public void savePreferences(String key, int val) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    // 값 저장하기
    public void savePreferences(String key, float val) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, val);
        editor.commit();
    }

    // 값 저장하기
    public void savePreferences(String key, boolean val) {
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
    public void removePreferences(String key) {
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
    public void removeAllPreferences() {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }


    /**
     * 로그인 정보 저장하기
     * @param login
     */
    public void saveLoginInfo(Tr_Login login) {
        savePreferences(LOGIN_JUMINNUM, login.juminnum);
        savePreferences(LOGIN_MEMNAME, login.memname);
        savePreferences(LOGIN_HPNUM, login.hpnum);
        savePreferences(LOGIN_NCRGD_YN, login.ncrgd_yn);
        savePreferences(LOGIN_NCRGD_DE, login.ncrgd_de);
        savePreferences(LOGIN_AREA_THMT_YN, login.area_thmt_yn);
        savePreferences(LOGIN_AREA_THMT_DE, login.area_thmt_de);
        savePreferences(LOGIN_HEAT_SI, login.heat_si);
        savePreferences(LOGIN_HEAT_DO, login.heat_do);
        savePreferences(LOGIN_NEW_APPVER, login.new_appver);
        savePreferences(LOGIN_NEW_LINK, login.new_link);
        savePreferences(LOGIN_INDATE, login.indate);
        savePreferences(LOGIN_ENDDATE, login.enddate);
        savePreferences(LOGIN_CMPNY_UB_CODE, login.cmpny_ub_code);
        savePreferences(LOGIN_STATUS, login.status);
        savePreferences(LOGIN_DOCNO, login.docno);
        savePreferences(LOGIN_RESULTCODE, login.resultcode);
        savePreferences(LOGIN_MESSAGE, login.message);

        // 알람 설정 저장
        savePreferences(GCAlramType.GC_ALRAM_TYPE_독려.getAlramName(), "Y".equals(login.ncrgd_yn));
        savePreferences(GCAlramType.GC_ALRAM_TYPE_지역.getAlramName(), "Y".equals(login.area_thmt_yn));
        mLogin = null;
    }

    /**
     * 로그인 정보 가져오기
     * @return
     */
    public Tr_Login getLoginInfo() {
        if (mLogin == null) {
            mLogin = new Tr_Login(mContext);
        } else {
            return mLogin;
        }
        mLogin.juminnum = getPreferences(LOGIN_JUMINNUM);
        mLogin.memname = getPreferences(LOGIN_MEMNAME);
        mLogin.hpnum = getPreferences(LOGIN_HPNUM);
        mLogin.ncrgd_yn = getPreferences(LOGIN_NCRGD_YN);
        mLogin.ncrgd_de = getPreferences(LOGIN_NCRGD_DE);
        mLogin.area_thmt_yn = getPreferences(LOGIN_AREA_THMT_YN);
        mLogin.area_thmt_de = getPreferences(LOGIN_AREA_THMT_DE);
        mLogin.heat_si = getPreferences(LOGIN_HEAT_SI);
        mLogin.heat_do = getPreferences(LOGIN_HEAT_DO);
        mLogin.new_appver = getPreferences(LOGIN_NEW_APPVER);
        mLogin.new_link = getPreferences(LOGIN_NEW_LINK);
        mLogin.indate = getPreferences(LOGIN_INDATE);
        mLogin.enddate = getPreferences(LOGIN_ENDDATE);
        mLogin.cmpny_ub_code = getPreferences(LOGIN_CMPNY_UB_CODE);
        mLogin.status = getPreferences(LOGIN_STATUS);
        mLogin.docno = getPreferences(LOGIN_DOCNO);
        mLogin.resultcode = getPreferences(LOGIN_RESULTCODE);
        mLogin.message = getPreferences(LOGIN_MESSAGE);

        return mLogin;
    }


}
