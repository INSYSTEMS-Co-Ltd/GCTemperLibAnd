package com.greencross.gctemperlib.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by jihoon on 2016-03-21.
 * 공유 데이터 클래스
 *
 * @since 0, 1
 */
public class CommonData {

    private Context mContext;
    private static CommonData _instance;


    // 일상기록 사진 업로드 hidden data
    public static final String CHLDRN_NOTE_UPLOAD_VIEWSTATE = "/wEPDwUKMTM2ODMxOTA2OGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFDEltYWdlQnV0dG9uMRVUYSqY+BBh12ChzYvoEIiZXVDunViBtSenJmijObhP";
    public static final String CHLDRN_NOTE_UPLOAD_EVENTVALIDATION = "/wEWCgLkzImcBgKNu5z0AwLgwJWSCALPh9yCCwKiwdKQDgKewZLDCQKZwfaKBQK1wfbXCQKY6fHPCQLSwpnTCIsvRoi9NE4oNAtmtPHie3a/PWTtrXZFCRfZgQ513+3v";

    // 육아노트
    public static final int TIME_OUT_DELAY = 15000;

    public static final int PERMISSION_REQUEST_GPS = 4;// GPS 권한, 열지도


    // 푸시알림 모드
    public static final int PUSH_MODE_DEFAULT = 0;  //  무음
    public static final int PUSH_MODE_BELL = 1;  // 벨소리
    public static final int PUSH_MODE_VIBRATE = 2;  // 진동
    public static final int PUSH_MODE_BELL_VIBRATE = 3;  // 벨+진동

    // 로그인 타입
    public static final int LOGIN_TYPE_PARENTS = 1;   // 부모님
    public static final int LOGIN_TYPE_CHILD = 2;  // 자녀


    public static final String YES = "Y";
    public static final String NO = "N";

    // DateFormat 패턴
    public static final String PATTERN_YYMMDD = "yyMMdd";
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String PATTERN_YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_2 = "MM-dd";
    public static final String PATTERN_DATE_3 = "M/d";
    public static final String PATTERN_DATE_DOT = "yyyy.MM.dd";
    public static final String PATTERN_DATE_DOT2 = "yyyy.MM.dd.";
    public static final String PATTERN_DATE_KR = "yyyy년 MM월 dd일";
    public static final String PATTERN_DATE_KR2 = "yyyy/MM/dd";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_DATETIME_S = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_TIME = "a hh:mm";
    public static final String PATTERN_TIME_2 = "HH:mm";

    // 문자
    public static final String STRING_HYPHEN = "-";    // 하이픈
    public static final String STRING_SPACE = " ";    // 공백
    public static final String STRING_DOT = ".";    // 점
    public static final String STRING_SLASH = "/";    // 슬러쉬


    // intent putExtra 데이터

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_ACTIVITY_TITLE = "EXTRA_ACTIVITY_TITLE";

    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";

    public static final String EXTRA_INFO_SN = "EXTRA_INFO_SN";
    public static final String EXTRA_PUSH_TYPE = "EXTRA_PUSH_TYPE";

    // 체중정보
    public static final int REQUEST_ADDRESS_SEARCH = 115;    // 열지도 주소 설정

    public static final String JSON_JSON = "json";
    public static final String JSON_SESSION_CODE = "session_code";
    public static final String JSON_MEMBER_ID = "member_id";
    public static final String JSON_HEIGHT = "height";
    public static final String JSON_APPVERSION = "appVersion";
    public static final String JSON_FX_YN = "fx_yn";
    public static final String JSON_FX_MTH = "fx_mth";
    public static final String JSON_MBER_SN = "mber_sn";
    public static final String JSON_APP_VER = "app_ver";
    public static final String JSON_MARKETING_YN = "marketing_yn"; //마케팅 동의
    public static final String JSON_MARKETING_YN_DE = "marketing_yn_de"; //마케팅 동의 날짜
    public static final String JSON_LOCATION_YN = "location_yn"; //위치정보
    public static final String JSON_DIET_PROGRAM_REQ_YN = "diet_program_req_yn"; //다이어트 프로그램 신청 여부


    public static final String JSON_MBER_NM = "mber_nm";
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
    public static final String JSON_MBER_PWD = "mber_pwd";
    public static final String JSON_CHLDRN = "chldrn";
    public static final String JSON_CHLDRN_LIFYEA = "chldrn_lifyea";
    public static final String JSON_CHL_EXIST_YN = "chl_exist_yn";
    public static final String JSON_DATA_YN = "data_yn";
    public static final String JSON_JUMINNUM = "juminnum";
    public static final String JSON_HIPLANNER_HP = "fc_hp";
    public static final String JSON_THERMOMETERCHK = "thermometerchk"; // 체온계 여부
    public static final String JSON_MOTHER_WEIGHT = "mother_weight";  // 엄마 체중
    public static final String JSON_KG_KIND = "kg_kind";  // 체중 상태
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

    public static final String JSON_AREA_DO = "area_do";
    public static final String JSON_AREA_SI = "area_si";

    public static final String JSON_NEWS_YN = "news_yn";
    public static final String JSON_NOTICE_YN = "notice_yn";
    public static final String JSON_HEAT_YN = "heat_yn";
    public static final String JSON_DIET_YN = "diet_yn";
    public static final String JSON_NOTITY_YN = "notity_yn";

    public static final String JSON_MBER_GRAD = "mber_grad";

    public static final String JSON_DISEASE_ALERT_YN = "disease_alert_yn";
    public static final String JSON_EVENT_ALERT_YN = "event_alert_yn";
    public static final String JSON_REPLAY_ALERT_YN = "replay_alert_yn";

    // 현대해상 열나요
    public static final String JSON_REG_YN_F = "REG_YN";
    public static final String JSON_DATA_F = "DATA";

    public static final String JSON_DZNUM = "DZNUM";
    public static final String JSON_DZNAME = "DZNAME";
    public static final String JSON_WEEKAGO_1 = "WEEKAGO_1";
    public static final String JSON_WEEKAGO_2 = "WEEKAGO_2";


    public static final String JSON_HELP_FEVER = "json_help_fever";

    public static final String JSON_ADDRESS_DO = "address_do";
    public static final String JSON_ADDRESS_GU = "address_gu";


    // API result Code
    public static final int API_SUCCESS = 0;        // 성공

    public static final int API_ERROR_SYSTEM_ERROR = 2;  // 시스템 오류로 인하여 중단
    public static final int API_ERROR_INPUT_DATA_ERROR = 3;  // 입력 데이터가 부족함

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
    private SharedPreferences getSharedPreference() { // 데이터 가져오기
        return mContext.getSharedPreferences("GnGCare", Activity.MODE_PRIVATE);
    }

    private SharedPreferences getSharedPreference(Context context) { // 데이터 가져오기
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

    public String getSessionCode() {
        return getSharedPreference().getString(JSON_SESSION_CODE, "");
    }

    public int getMemberId() {
        return getSharedPreference().getInt(JSON_MEMBER_ID, 0);
    }


    // start 로그인시 데이터
    // 부모 회원키값
    public void setMberSn(String sn) {
        setSharedPreferenceData(JSON_MBER_SN, sn);
    }


    // 내 이름
    public void setMberNm(String nick) {
        setSharedPreferenceData(JSON_MBER_NM, nick);
    }

    // 회원 전화번호
    public void setPhoneNumber(String phone) {
        setSharedPreferenceData(JSON_MBER_HP, phone);
    }

    // 생년월일
    public void setBirthDay(String birth) {
        setSharedPreferenceData(JSON_MBER_BRTHDY, birth);
    }

    // 내국인 &외국인
    public void setMberNation(String nation) {
        setSharedPreferenceData(JSON_MBER_NATION, nation);
    }

    // 회원 성별
    public void setGender(String gender) {
        setSharedPreferenceData(JSON_MBER_SEX, gender);
    }

    // 회원 직업
    public void setMberJob(String job) {
        setSharedPreferenceData(JSON_MBER_JOB_YN, job);
    }

    // 동의여부
    public void setMberAgreementYn(String job) {
        setSharedPreferenceData(JSON_MBER_AGREEMENT_YN, job);
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

    // 고유키값
    public void setJuminNum(String num) {
        setSharedPreferenceData(JSON_JUMINNUM, num);
    }

    // 자동로그인
    public void setAutoLogin(boolean bool) {
        setSharedPreferenceData(JSON_AUTO_LOGIN, bool);
    }


    // 아이디 저장
    public void setRememberId(boolean bool) {
        setSharedPreferenceData(JSON_REMEMBER_ID, bool);
    }

    // 자녀정보
    public void setChldrn(String chldrn) {
        setSharedPreferenceData(JSON_CHLDRN, chldrn);
    }

    public String getChldrn() {
        return getSharedPreference().getString(JSON_CHLDRN, "");
    }

    // 하이플래너 전화번호
    public void setHiPlannerHp(String sn) {
        setSharedPreferenceData(JSON_HIPLANNER_HP, sn);
    }

    // 체중계 여부
    public void setTempDivices(String sn) {
        setSharedPreferenceData(JSON_THERMOMETERCHK, sn);
    }

    // 엄마체중
    public void setMotherWeight(String sn) {
        setSharedPreferenceData(JSON_MOTHER_WEIGHT, sn);
    }

    // 체중 상태
    public void setKg_Kind(String sn) {
        setSharedPreferenceData(JSON_KG_KIND, sn);
    }

    // 즐겨찾기
    public void setMain_Category(String sn) {
        setSharedPreferenceData(JSON_MAIN_CATEGORY, sn);
    }

    // 체중계 여부
    public void setWeighingchk(String sn) {
        setSharedPreferenceData(JSON_WEIGHTNGCHK, sn);
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

    // 엄마 목표스탭
    public void setMotherGoalStep(String sn) {
        setSharedPreferenceData(JSON_MOTHER_GOAL_STEP, sn);
    }

    // 출산예정일
    public void setMberBirthDueDe(String sn) {
        setSharedPreferenceData(JSON_MBER_BIRTH_DUE_DE, sn);
    }

    // 출산일
    public void setMberChlBirthDe(String sn) {
        setSharedPreferenceData(JSON_MBER_CHL_BIRTH_DE, sn);
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

    // 엄마 데이터 저장 여부
    public void setHpMjYnJun(Boolean sn) {
        setSharedPreferenceData(JSON_HP_MJ_YN_JUN, sn);
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

    // 활동
    public void setActqy(String sn) {
        setSharedPreferenceData(JSON_ACTQY, sn);
    }

    // 키
    public void setHeight(float height) {
        setSharedPreferenceData(JSON_HEIGHT, height);
    }

    public float getHeight() {
        return getSharedPreference().getFloat(JSON_HEIGHT, 0f);
    }


    // 최신버전
    public void setAppVersion(String ver) {
        setSharedPreferenceData(JSON_APPVERSION, ver);
    }

    public String getAppVersion() {
        return getSharedPreference().getString(JSON_APPVERSION, "");
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

    //    // 임신여부
    public void setMotherIsPregnancy(String yn) {
        setSharedPreferenceData(JSON_MOTHER_IS_PREGNANT, yn);
    }

    // 막내 생일
    public void setLastChlBirth(String yn) {
        setSharedPreferenceData(JSON_LAST_CHL_BIRTH, yn);
    }

    // 단태임신, 다태임신 여부
    public void setMberChlType(String yn) {
        setSharedPreferenceData(JSON_MBER_CHL_TYPE, yn);
    }

}