package com.greencross.gctemperlib.collection;

/**
 * Created by jihoon on 2016-04-04.
 * 자녀 리스트 아이템
 * @since 0, 1
 */
public class ChildItem {

    private String mChlSn;
    private String mChldrnJoinSerial;
    private String mChldrnNm;
    private String mChldrnNcm;
    private String mChldrnLifyea;
    private String mChldrnAftLifyea;
    private String mChlExistYn;
    private String mChldrnSex;
    private String mChldrnHp;
    private String mChldrnCi;
    private String mChldrnHeight;
    private String mChldrnBdwgh;
    private String mChldrnHeadCm;
    private String mChldrnMainImage;
    private String mChldrnOrgImage;
    private String mSafeAreaX;
    private String mSafeAreaY;
    private String mSafeKm;
    private String mSafeAlarmAt;
    private String mSafeHedexpln;
    private String mSafeAdres;
    private String mHpUseTime;
    private String mHpUseAlarmAt;
    private String mHpUseEstbsTime;
    private String mSelect;
    private String mChldmKgper;

    public int born_to_day = 0;        // 태어난지 몇일인지

    public ChildItem(){

    }

    /**
     * 자녀 데이터
     * @param chl_sn                자녀키값(첫째,둘째, 무조건 자녀키값)
     * @param chldrn_joinserial     자녀증권번호
     * @param chldrn_nm             자녀명
     * @param chldrn_ncm            자녀 별칭
     * @param chldrn_lifyea         자녀 생년
     * @param chldrn_aft_lifyea     태아 출생예정일
     * @param chl_exist_yn         태아 여부
     * @param chldrn_sex            자녀 성별
     * @param chldrn_hp             자녀 휴대폰
     * @param chldrn_ci             자녀 ci
     * @param chldrn_height         키
     * @param chldrn_bdwgh          몸무게
     * @param chldrn_headcm         머리둘레
     * @param chldrn_main_image     자녀 메인 이미지 ( 썸네일 )
     * @param chldrn_org_image      자녀 메인 이미지 ( 원본 )
     * @param safe_area_x            안심지역 X 좌표 ( 경도 )
     * @param safe_area_y           안심지역 Y 좌표 ( 위도 )
     * @param safe_km               안심 반경
     * @param safe_alarm_at         안심 여부
     * @param safe_hedexpln         안심지역 별칭
     * @param safe_adres            안심지역 주소
     * @param hp_use_time           사용시간
     * @param hp_use_alarm_at       휴대폰 알림 여부
     * @param hp_use_estbs_time     휴대폰 사용 설정 시간
     * @param select                자녀 선택
     */
    public ChildItem(String chl_sn,
                     String chldrn_joinserial,
                     String chldrn_nm,
                     String chldrn_ncm,
                     String chldrn_lifyea,
                     String chldrn_aft_lifyea,
                     String chl_exist_yn,
                     String chldrn_sex,
                     String chldrn_hp,
                     String chldrn_ci,
                     String chldrn_height,
                     String chldrn_bdwgh,
                     String chldrn_headcm,
                     String chldrn_main_image,
                     String chldrn_org_image,
                     String safe_area_x,
                     String safe_area_y,
                     String safe_km,
                     String safe_alarm_at,
                     String safe_hedexpln,
                     String safe_adres,
                     String hp_use_time,
                     String hp_use_alarm_at,
                     String hp_use_estbs_time,
                     String select,
                     String chldm_kgper){

        this.mChlSn                 =   chl_sn;
        this.mChldrnJoinSerial      =   chldrn_joinserial;
        this.mChldrnNm              =   chldrn_nm;
        this.mChldrnNcm             =   chldrn_ncm;
        this.mChldrnLifyea          =   chldrn_lifyea;
        this.mChldrnAftLifyea        = chldrn_aft_lifyea;
        this.mChlExistYn            = chl_exist_yn;
        this.mChldrnSex             =   chldrn_sex;
        this.mChldrnHp              =   chldrn_hp;
        this.mChldrnCi              =   chldrn_ci;
        this.mChldrnHeight          =   chldrn_height;
        this.mChldrnBdwgh           =   chldrn_bdwgh;
        this.mChldrnHeadCm          =   chldrn_headcm;
        this.mChldrnMainImage       =  chldrn_main_image;
        this.mChldrnOrgImage        =   chldrn_org_image;
        this.mSafeAreaX             =   safe_area_x;
        this.mSafeAreaY             =   safe_area_y;
        this.mSafeKm                =   safe_km;
        this.mSafeAlarmAt           =   safe_alarm_at;
        this.mSafeHedexpln          =   safe_hedexpln;
        this.mSafeAdres             =   safe_adres;
        this.mHpUseTime             =   hp_use_time;
        this.mHpUseAlarmAt          =   hp_use_alarm_at;
        this.mHpUseEstbsTime        =   hp_use_estbs_time;
        this.mSelect                =   select;
        this.mChldmKgper           =   chldm_kgper;

    }

    public String getmChlSn() {
        return mChlSn;
    }

    public void setmChlSn(String mChlSn) {
        this.mChlSn = mChlSn;
    }

    public String getmChldrnJoinSerial() {
        return mChldrnJoinSerial;
    }

    public void setmChldrnJoinSerial(String mChldrnJoinSerial) {
        this.mChldrnJoinSerial = mChldrnJoinSerial;
    }

    public String getmChldrnNm() {
        return mChldrnNm;
    }

    public void setmChldrnNm(String mChldrnNm) {
        this.mChldrnNm = mChldrnNm;
    }

    public String getmChldrnNcm() {
        return mChldrnNcm;
    }

    public void setmChldrnNcm(String mChldrnNcm) {
        this.mChldrnNcm = mChldrnNcm;
    }

    public String getmChldrnLifyea() {
        return mChldrnLifyea;
    }

    public void setmChldrnLifyea(String mChldrnLifyea) {
        this.mChldrnLifyea = mChldrnLifyea;
    }

    public String getmChldrnAftLifyea() {
        return mChldrnAftLifyea;
    }

    public void setmChldrnAftLifyea(String mChldrnAftLifyea) {
        this.mChldrnAftLifyea = mChldrnAftLifyea;
    }

    public String getmChlExistYn() {
        return mChlExistYn;
    }

    public void setmChlExistYn(String mChlExistYn) {
        this.mChlExistYn = mChlExistYn;
    }

    public String getmChldrnSex() {
        return mChldrnSex;
    }

    public void setmChldrnSex(String mChldrnSex) {
        this.mChldrnSex = mChldrnSex;
    }

    public String getmChldrnHp() {
        return mChldrnHp;
    }

    public void setmChldrnHp(String mChldrnHp) {
        this.mChldrnHp = mChldrnHp;
    }

    public String getmChldrnCi() {
        return mChldrnCi;
    }

    public void setmChldrnCi(String mChldrnCi) {
        this.mChldrnCi = mChldrnCi;
    }

    public String getmChldrnHeight() {
        return mChldrnHeight;
    }

    public void setmChldrnHeight(String mChldrnHeight) {
        this.mChldrnHeight = mChldrnHeight;
    }

    public String getmChldrnBdwgh() {
        return mChldrnBdwgh;
    }

    public void setmChldrnBdwgh(String mChldrnBdwgh) {
        this.mChldrnBdwgh = mChldrnBdwgh;
    }

    public String getmChldrnHeadCm() {
        return mChldrnHeadCm;
    }

    public void setmChldrnHeadCm(String mChldrnHeadCm) {
        this.mChldrnHeadCm = mChldrnHeadCm;
    }

    public String getmChldrnMainImage() {
        return mChldrnMainImage;
    }

    public void setmChldrnMainImage(String mChldrnMainImage) {
        this.mChldrnMainImage = mChldrnMainImage;
    }

    public String getmChldrnOrgImage() {
        return mChldrnOrgImage;
    }

    public void setmChldrnOrgImage(String mChldrnOrgImage) {
        this.mChldrnOrgImage = mChldrnOrgImage;
    }

    public String getmSafeAreaX() {
        return mSafeAreaX;
    }

    public void setmSafeAreaX(String mSafeAreaX) {
        this.mSafeAreaX = mSafeAreaX;
    }

    public String getmSafeAreaY() {
        return mSafeAreaY;
    }

    public void setmSafeAreaY(String mSafeAreaY) {
        this.mSafeAreaY = mSafeAreaY;
    }

    public String getmSafeKm() {
        return mSafeKm;
    }

    public void setmSafeKm(String mSafeKm) {
        this.mSafeKm = mSafeKm;
    }

    public String getmSafeAlarmAt() {
        return mSafeAlarmAt;
    }

    public void setmSafeAlarmAt(String mSafeAlarmAt) {
        this.mSafeAlarmAt = mSafeAlarmAt;
    }

    public String getmSafeHedexpln() {
        return mSafeHedexpln;
    }

    public void setmSafeHedexpln(String mSafeHedexpln) {
        this.mSafeHedexpln = mSafeHedexpln;
    }

    public String getmSafeAdres() {
        return mSafeAdres;
    }

    public void setmSafeAdres(String mSafeAdres) {
        this.mSafeAdres = mSafeAdres;
    }

    public String getmHpUseTime() {
        return mHpUseTime;
    }

    public void setmHpUseTime(String mHpUseTime) {
        this.mHpUseTime = mHpUseTime;
    }

    public String getmHpUseAlarmAt() {
        return mHpUseAlarmAt;
    }

    public void setmHpUseAlarmAt(String mHpUseAlarmAt) {
        this.mHpUseAlarmAt = mHpUseAlarmAt;
    }

    public String getmHpUseEstbsTime() {
        return mHpUseEstbsTime;
    }

    public void setmHpUseEstbsTime(String mHpUseEstbsTime) {
        this.mHpUseEstbsTime = mHpUseEstbsTime;
    }

    public String getmSelect() {
        return mSelect;
    }

    public void setmSelect(String mSelect) {
        this.mSelect = mSelect;
    }


    public String getmChldmKgper() {
        return mChldmKgper;
    }

    public void setmChldmKgper(String mChldmKgper) {
        this.mChldmKgper = mChldmKgper;
    }
}
