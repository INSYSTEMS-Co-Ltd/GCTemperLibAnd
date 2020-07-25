package com.greencross.gctemperlib.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.common.MakeProgress;
//import com.greencross.gctemperlib.greencare.database.DBHelperLog;
//import com.greencross.gctemperlib.greencare.database.util.DBBackupManager;
import com.greencross.gctemperlib.greencare.util.JsonLogPrint;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;
//import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


/**
 * Created by jihoon on 2016-03-21.
 * 로그인 부모 클래스
 * @since 0, 1
 */
public class IntroBaseActivity extends BaseActivity {
    private final String TAG = IntroBaseActivity.class.getSimpleName();

    private MakeProgress mProgress;

    public static final String ROOT_DIR = "/data/data/com.appmd.hi.gngcare/databases/";  //로컬db 저장
    public static final String DATABASE_NAME = "emomcare_v2.db";      //로컬db명
    public static final String TABLE_NAME = "eMomCsv";    // 테이블 명

    public static final int TYPE_QUERY_MALE_HEIGHT_3  =   0;
    public static final int TYPE_QUERY_MALE_HEIGHT_50  =   1;
    public static final int TYPE_QUERY_MALE_HEIGHT_90  =   2;
    public static final int TYPE_QUERY_MALE_WEIGHT_3  =   3;
    public static final int TYPE_QUERY_MALE_WEIGHT_50  =   4;
    public static final int TYPE_QUERY_MALE_WEIGHT_90  =   5;
    public static final int TYPE_QUERY_MALE_HEAD_3  =   6;
    public static final int TYPE_QUERY_MALE_HEAD_50  =   7;
    public static final int TYPE_QUERY_MALE_HEAD_90  =   8;

    public static final int TYPE_QUERY_FEMALE_HEIGHT_3  =   9;
    public static final int TYPE_QUERY_FEMALE_HEIGHT_50  =   10;
    public static final int TYPE_QUERY_FEMALE_HEIGHT_90  =   11;
    public static final int TYPE_QUERY_FEMALE_WEIGHT_3  =   12;
    public static final int TYPE_QUERY_FEMALE_WEIGHT_50  =   13;
    public static final int TYPE_QUERY_FEMALE_WEIGHT_90  =   14;
    public static final int TYPE_QUERY_FEMALE_HEAD_3  =   15;
    public static final int TYPE_QUERY_FEMALE_HEAD_50  =   16;
    public static final int TYPE_QUERY_FEMALE_HEAD_90  =   17;

    public static final String QUERY_MALE_HEIGHT_3  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 2 and percent = 3";
    public static final String QUERY_MALE_HEIGHT_50  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 2 and percent = 50";
    public static final String QUERY_MALE_HEIGHT_90  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 2 and percent = 90";
    public static final String QUERY_MALE_WEIGHT_3  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 1 and percent = 3";
    public static final String QUERY_MALE_WEIGHT_50  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 1 and percent = 50";
    public static final String QUERY_MALE_WEIGHT_90  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 1 and percent = 90";
    public static final String QUERY_MALE_HEAD_3  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 5 and percent = 3";
    public static final String QUERY_MALE_HEAD_50  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 5 and percent = 50";
    public static final String QUERY_MALE_HEAD_90  =   "select value from " + TABLE_NAME +" where gender_code = 1 and gubun_code = 5 and percent = 90";

    public static final String QUERY_FEMALE_HEIGHT_3  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 2 and percent = 3";
    public static final String QUERY_FEMALE_HEIGHT_50  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 2 and percent = 50";
    public static final String QUERY_FEMALE_HEIGHT_90  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 2 and percent = 90";
    public static final String QUERY_FEMALE_WEIGHT_3  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 1 and percent = 3";
    public static final String QUERY_FEMALE_WEIGHT_50  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 1 and percent = 50";
    public static final String QUERY_FEMALE_WEIGHT_90  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 1 and percent = 90";
    public static final String QUERY_FEMALE_HEAD_3  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 5 and percent = 3";
    public static final String QUERY_FEMALE_HEAD_50  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 5 and percent = 50";
    public static final String QUERY_FEMALE_HEAD_90  =   "select value from " + TABLE_NAME +" where gender_code = 2 and gubun_code = 5 and percent = 90";


    private SQLiteDatabase db; // adapter 속성 정의
    private Cursor cursor;
    ProductDBHelper mHelper; // adapter 생성 정의

    private RelativeLayout mMainLayout;
    private ImageView mBackImg;
    private TextView mTitleTextView;
//    CommonData commonData = CommonData.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Logger.initLogger(this);
//        new DBBackupManager().importDBAssets(IntroBaseActivity.this);


    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
        mTitleTextView	=	(TextView)	findViewById(R.id.common_title_tv);

    }

    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }

    public void setTitle(int resId) {
        mTitleTextView.setText(resId);
    }

    public ImageView getBackImg(){
        return mBackImg;
    }


    /**
     * 로그인 성공 후 처리
     * @param data
     * switchMember 정회원 전환 시 : true 아니면 false
     */
    public void loginSuccess(Context context, JSONObject data, Boolean switchMember) {
        introshowProgress();

//        {   "api_code": "mber_check",   "insures_code": "106", "token": "deviceToken",  "app_code": "android19" ,  "mber_nm": "테스트" ,"mber_lifyea": "20140101","mber_hp": "01075841333", "mber_nation": "1"  ,  "mber_sex": "3"   }

        String data_yn = "";
        try {
            JsonLogPrint.printJson(data.toString());

            data_yn = data.getString(CommonData.JSON_DATA_YN);

            if(data_yn.equals(CommonData.YES)) { // 로그인 성공이라면
                commonData.setMberNm(data.getString(CommonData.JSON_MBER_NM));
                commonData.setPhoneNumber(data.getString(CommonData.JSON_MBER_HP));
                commonData.setBirthDay(data.getString(CommonData.JSON_MBER_BRTHDY));
                commonData.setGender(data.getString(CommonData.JSON_MBER_SEX));
                commonData.setMberNation(data.getString(CommonData.JSON_MBER_NATION));

                //푸시
                commonData.setPushAlarm(data.getString(CommonData.JSON_PUSH_YN).equals(CommonData.YES) ? true : false);
                commonData.setNewsPushAlarm(data.getString(CommonData.JSON_NEWS_YN).equals(CommonData.YES) ? true : false);
                commonData.setMapPushAlarm(data.getString(CommonData.JSON_HEAT_YN).equals(CommonData.YES) ? true : false);
                commonData.setNoticePushAlarm(data.getString(CommonData.JSON_NOTICE_YN).equals(CommonData.YES) ? true : false);
                commonData.setDietPushAlarm(data.getString(CommonData.JSON_DIET_YN).equals(CommonData.YES) ? true : false);
                commonData.setNotityPushAlarm(data.getString(CommonData.JSON_NOTITY_YN).equals(CommonData.YES) ? true : false);

                commonData.setAddressDo(data.getString(CommonData.JSON_AREA_DO));
                commonData.setAddressGu(data.getString(CommonData.JSON_AREA_SI));
                commonData.setAlarmMode(data.getInt(CommonData.JSON_PUSH_MTH));
//                commonData.setMberNation(data.getString(CommonData.JSON_MBER_NATION));
//            commonData.setLoginType(data.getInt(CommonData.JSON_CHIDRN_CNT) > 0 ? CommonData.LOGIN_TYPE_PARENTS : CommonData.LOGIN_TYPE_CHILD);
                commonData.setChildCnt(data.getInt(CommonData.JSON_CHIDRN_CNT));
                commonData.setJuminNum(data.getString(CommonData.JSON_JUMINNUM));
                commonData.setHpMjYn(data.getString(CommonData.JSON_HP_MJ_YN));
                commonData.setHiPlannerHp(data.getString(CommonData.JSON_HIPLANNER_HP));
                commonData.setTempDivices(data.getString(CommonData.JSON_THERMOMETERCHK));
                commonData.setMotherWeight(data.getString(CommonData.JSON_MOTHER_WEIGHT));
                commonData.setKg_Kind(data.getString(CommonData.JSON_KG_KIND)); // 엄마체중 상태
                commonData.setWeighingchk(data.getString(CommonData.JSON_WEIGHTNGCHK)); // 체중계여부
                commonData.setIamChild(data.getString(CommonData.JSON_I_AM_CHILD));
                commonData.setMotherWeight(data.getString(CommonData.JSON_MOTHER_WEIGHT));
                commonData.setMotherGoalWeight(data.getString(CommonData.JSON_MOTHER_GOAL_WEIGHT));
                commonData.setMotherGoalCal(data.getString(CommonData.JSON_MOTHER_GOAL_CAL));   //목표칼로리
                commonData.setMotherGoalStep(data.getString(CommonData.JSON_MOTHER_GOAL_STEP)); //목표체중
                commonData.setMberBirthDueDe(data.getString(CommonData.JSON_MBER_BIRTH_DUE_DE));
                commonData.setMberChlBirthDe(data.getString(CommonData.JSON_MBER_CHL_BIRTH_DE));
                commonData.setBefCm(data.getString(CommonData.JSON_BEF_CM));
                commonData.setBefKg(data.getString(CommonData.JSON_BEF_KG));
                commonData.setHpMjYn(data.getString(CommonData.JSON_HP_MJ_YN));
                commonData.setActqy(data.getString(CommonData.JSON_ACTQY));
                commonData.setKg_Kind(data.getString(CommonData.JSON_KG_KIND));
                commonData.setMberJob(data.getString(CommonData.JSON_MBER_JOB_YN));

                commonData.setMberAgreementYn(data.getString(CommonData.JSON_MBER_AGREEMENT_YN)); //동의 여부

                commonData.setMberGrad(data.getString(CommonData.JSON_MBER_GRAD)); // 정회원 여부 : 10:정회원, 20: 준회원

                if(!data.isNull(CommonData.JSON_MBER_SN)){  // 회원고유키값이 있다면 저장 ( 모든 api 호출시 사용 )
                    commonData.setMberSn(data.getString(CommonData.JSON_MBER_SN));
                }

                if(data.getJSONArray(CommonData.JSON_CHLDRN).length() > 0) {    // 자녀정보가 있을 경우
                    commonData.setChldrn(data.getJSONArray(CommonData.JSON_CHLDRN).toString()); // 자녀정보
                }




                try {
                    PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
                    CommonData.getInstance(this).setAppVersion(pi.versionName.toString());
                    GLog.i("app_ver  = " + pi.versionName.toString(), "dd");
                } catch (Exception e) {
                    GLog.e(e.toString());
                }



                // + -----------------
                // 임신여부를 판단.
                // + -----------------
                String isPregnancy = "N";
                try{
                    if(!commonData.getChldrn().equals("")) {
                        JSONArray childArr = new JSONArray(CommonData.getInstance(this).getChldrn());
                        if (childArr.length() > 0) {
                            for (int i = 0; i < childArr.length(); i++) {
                                JSONObject childData = childArr.getJSONObject(i);
                                String exitYN = childData.getString(CommonData.JSON_CHL_EXIST_YN);
                                if ("N".equals(exitYN)){
                                    isPregnancy = "Y";
                                    break;
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    GLog.e(e.toString());
                }
                commonData.setMotherIsPregnancy(isPregnancy);


                //막내 생일
                String LastChild = "0";
                int year=0;
                int month=0;
                int day=0;
                if(!commonData.getChldrn().equals("")) {
                    JSONArray childArr = new JSONArray(CommonData.getInstance(this).getChldrn());
                    if (childArr.length() > 0) {
                        for (int i = 0; i < childArr.length(); i++) {

                            JSONObject childData = childArr.getJSONObject(i);

                            String chldrn_lifyea = childData.getString(CommonData.JSON_CHLDRN_LIFYEA);
                            if (!chldrn_lifyea.equals("000000")
                                    && chldrn_lifyea.length() > 0) {
                                if (i == 0) {
                                    final Date mDate = Util.getDateFormat(chldrn_lifyea, CommonData.PATTERN_YYMMDD);
                                    year = mDate.getYear() + 1900;
                                    month = mDate.getMonth() + 1;
                                    day = mDate.getDate();
                                    LastChild = String.format("%04d%02d%02d",year,month,day);
                                } else {
                                    final Date temp_mDate = Util.getDateFormat(chldrn_lifyea, CommonData.PATTERN_YYMMDD);
                                    year = temp_mDate.getYear() + 1900;
                                    month = temp_mDate.getMonth() + 1;
                                    day = temp_mDate.getDate();
                                    String temp = String.format("%04d%02d%02d",year,month,day);
                                    if (StringUtil.getIntger(temp) >= StringUtil.getIntger(LastChild)) {
                                        final Date mDate = Util.getDateFormat(chldrn_lifyea, CommonData.PATTERN_YYMMDD);
                                        year = mDate.getYear() + 1900;
                                        month = mDate.getMonth() + 1;
                                        day = mDate.getDate();
                                        LastChild = String.format("%04d%02d%02d",year,month,day);
                                    }
                                }
                            }
                        }
                    }
                }
                commonData.setLastChlBirth(LastChild);



                // + -----------------
                // 단태임신, 다태임신 여부를 판단. 1:단태, 2:다태
                // + -----------------
                if(!data.isNull(CommonData.JSON_MBER_CHL_TYPE)){
                    commonData.setMberChlType(data.getString(CommonData.JSON_MBER_CHL_TYPE));
                }

                //엄마 임신여부
                commonData.setbirth_chl_yn(data.getString(CommonData.JSON_BIRTH_CHL_YN));

                //닉네임
                commonData.setMberNick(data.getString(CommonData.JSON_MBER_NICK));
                commonData.setMarketing_yn(data.getString(CommonData.JSON_MARKETING_YN));
                commonData.setMarketing_yn_de(data.getString(CommonData.JSON_MARKETING_YN_DE));

                commonData.setDisease_alert_yn(data.getString(CommonData.JSON_DISEASE_ALERT_YN).equals(CommonData.YES) ? true : false);
                commonData.setEvent_alert_yn(data.getString(CommonData.JSON_EVENT_ALERT_YN).equals(CommonData.YES) ? true : false);
                commonData.setReplay_alert_yn(data.getString(CommonData.JSON_REPLAY_ALERT_YN).equals(CommonData.YES) ? true : false);
                commonData.setLocation_yn(data.getString(CommonData.JSON_LOCATION_YN));
                //다이어트 신청여부
                commonData.setDiet_program_req_yn(data.getString(CommonData.JSON_DIET_PROGRAM_REQ_YN));


                // 그래프 퍼센트데이터 db 갱싱 과정
                if(!isCheckDB()) {  // assets db 를 폰에 저장 안했다면
                    setDB();        // assets db -> phone 으로 저장

                    graphDataSet(); // 그래프 데이터 세팅
                    GLog.i("db empty", "dd");
                }else{
                    GLog.i("db result", "dd");
//                    if(GrowthMainActivity.mMaleHeight_3.size() < 1){  // 그래프 데이터가 없다면
//                        graphDataSet(); // 그래프 데이터 세팅
//                    }
                }

//                sendLog(context,switchMember);


            }else{

                if(switchMember){
                    mDialog = new CustomAlertDialog(IntroBaseActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.popup_dialog_switch_member_error));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                        @Override
                        public void onClick(CustomAlertDialog dialog, Button button) {
                            dialog.dismiss();
                            // 로그인 실패 시 내용을 전부 지운다.
                            commonData.setMberPwd(null);
                            commonData.setMain_Category("");
                            commonData.setAutoLogin(false);
                            commonData.setRememberId(false);
                            commonData.setMberSn("");

                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_bef_cm", "");
                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_bef_kg", "");
                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_mber_kg","");
                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_mber_term_kg", "");
                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_mber_chl_birth_de", "");
                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_mber_milk_yn", "");
                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_mber_birth_due_de", "");
                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_mber_chl_typ", "");
                            Util.setSharedPreference(IntroBaseActivity.this, "MonJin_actqy", "");


                            commonData.setLoginType(CommonData.LOGIN_TYPE_PARENTS);
                            mDialog.dismiss();
                            finish();

//                            Intent intent2 = new Intent(IntroBaseActivity.this, LoginActivity.class);
//                            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent2);
                        }
                    });
                    mDialog.show();

                }else {
                    mDialog = new CustomAlertDialog(IntroBaseActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.popup_dialog_login_error));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                        @Override
                        public void onClick(CustomAlertDialog dialog, Button button) {
                            dialog.dismiss();
                        }
                    });
                    mDialog.show();
                }

            }

        } catch (JSONException e) { GLog.e(e.toString());}
    }



    /**
     * 그래프 퍼센트 데이터 세팅
     */
    public void graphDataSet(){
//        GrowthMainActivity.mMaleHeight_3.clear(); GrowthMainActivity.mMaleHeight_50.clear(); GrowthMainActivity.mMaleHeight_90.clear();
//        GrowthMainActivity.mMaleWeight_3.clear(); GrowthMainActivity.mMaleWeight_50.clear(); GrowthMainActivity.mMaleWeight_90.clear();
//        GrowthMainActivity.mMaleHead_3.clear(); GrowthMainActivity.mMaleHead_50.clear(); GrowthMainActivity.mMaleHead_90.clear();
//        GrowthMainActivity.mFeMaleHeight_3.clear(); GrowthMainActivity.mFeMaleHeight_50.clear(); GrowthMainActivity.mFeMaleHeight_90.clear();
//        GrowthMainActivity.mFeMaleWeight_3.clear(); GrowthMainActivity.mFeMaleWeight_50.clear(); GrowthMainActivity.mFeMaleWeight_90.clear();
//        GrowthMainActivity.mFeMaleHead_3.clear(); GrowthMainActivity.mFeMaleHead_50.clear(); GrowthMainActivity.mFeMaleHead_90.clear();

        mHelper=new ProductDBHelper(this);
        db=mHelper.getWritableDatabase();

        new RequestDb().execute(QUERY_MALE_HEIGHT_3, QUERY_MALE_HEIGHT_50, QUERY_MALE_HEIGHT_90,
                QUERY_MALE_WEIGHT_3, QUERY_MALE_WEIGHT_50, QUERY_MALE_WEIGHT_90,
                QUERY_MALE_HEAD_3, QUERY_MALE_HEAD_50, QUERY_MALE_HEAD_90,
                QUERY_FEMALE_HEIGHT_3, QUERY_FEMALE_HEIGHT_50, QUERY_FEMALE_HEIGHT_90,
                QUERY_FEMALE_WEIGHT_3, QUERY_FEMALE_WEIGHT_50, QUERY_FEMALE_WEIGHT_90,
                QUERY_FEMALE_HEAD_3, QUERY_FEMALE_HEAD_50, QUERY_FEMALE_HEAD_90);
    }

    /**
     * 디비 있는지 체크
     * @return bool ( true - db 있음, false - db 없음 )
     */
    public boolean isCheckDB(){
        String filePath = ROOT_DIR + DATABASE_NAME;
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;

    }

    public void setDB() {         //setDB에 Context가 없다.
        File folder = new File(ROOT_DIR);
        if(folder.exists()) {
        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = getResources().getAssets();       //ctx가 없다.
        File outfile = new File(ROOT_DIR+DATABASE_NAME);
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open(DATABASE_NAME, AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } else {}
        } catch (IOException e) {}
    }

    /**
     * db 쿼리 후 데이터 추출
     * @param query 쿼리문
     * @param type  쿼리타입 ( 0 ~ 17 );
     * @return  쿼리 결과 문자열
     */
    public void requestDb(String query, int type){

        cursor=db.rawQuery(query,null); //쿼리문

        switch(type){
//            case TYPE_QUERY_MALE_HEIGHT_3:  //  남자 키 3%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleHeight_3.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_MALE_HEIGHT_50: //  남자 키 50%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleHeight_50.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_MALE_HEIGHT_90: //  남자 키 90%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleHeight_90.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_MALE_WEIGHT_3:  //  남자 몸무게 3%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleWeight_3.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_MALE_WEIGHT_50: //  남자 몸무게 50%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleWeight_50.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_MALE_WEIGHT_90: //  남자 몸무게 90%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleWeight_90.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_MALE_HEAD_3:    //  남자 머리둘레 3%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleHead_3.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_MALE_HEAD_50:   //  남자 머리둘레 50%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleHead_50.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_MALE_HEAD_90:   //  남자 머리둘레 90%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mMaleHead_90.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_HEIGHT_3:  //  여자 키 3%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleHeight_3.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_HEIGHT_50: //  여자 키 50%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleHeight_50.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_HEIGHT_90: //  여자 키 90%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleHeight_90.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_WEIGHT_3:  //  여자 몸무게 3%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleWeight_3.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_WEIGHT_50: //  여자 몸무게 50%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleWeight_50.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_WEIGHT_90: //  여자 몸무게 90%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleWeight_90.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_HEAD_3:    //  여자 머리둘레 3%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleHead_3.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_HEAD_50:   //  여자 머리둘레 50%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleHead_50.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
//            case TYPE_QUERY_FEMALE_HEAD_90:   //  여자 머리둘레 90%
//                if(cursor.moveToFirst()){
//                    do{
//                        GrowthGraphItem item = new GrowthGraphItem(cursor.getString(cursor.getColumnIndex(CommonData.JSON_VALUE)));
//                        GrowthMainActivity.mFeMaleHead_90.add(item);
//                    }while (cursor.moveToNext());
//                }
//                break;
        }

    }


    /**
     * SNS 회원가입 후 처리
     * @param data 로그인 정보 데이터
     * @param dialog 화면에 띄울 다이얼로그
     */
    public void JoinTologinSuccess(final JSONObject data, CustomAlertDialog dialog) {

        // 프로필 입력 단계로 이동하겠냐고 물어봄
        /*
        dialog.setPositiveButton(null, new CustomAlertDialogInterface.OnClickListener() {

            @Override
            public void onClick(CustomAlertDialog dialog, Button button) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                loginSuccess(data);
            }
        });

        dialog.show();
        */
        loginSuccess(null, data,false);

    }

    /**
     * 로그인 후 앱 화면 이동처리
     */
    protected void moveActivity() {
//
//        Intent i = getIntent();
//
//
//        int chl_sn = i.getIntExtra("chl_sn", 0);
//
//        Intent intent = null;
//
//        if(commonData.getMberGrad().equals("10")){
//            if(commonData.getMberAgreementYn().equals("N")){
////                intent = new Intent(IntroBaseActivity.this, AgreeConfirmActivity.class);
//            } else {
//                intent = new Intent(IntroBaseActivity.this, MainActivity.class);
//            }
//        } else {
//            intent = new Intent(IntroBaseActivity.this, MainActivity.class);
//        }
//
//
//
//        if(chl_sn != 0)
//            intent.putExtra("chl_sn", chl_sn);
//
//        int push_type = i.getIntExtra(CommonData.EXTRA_PUSH_TYPE, 0);
//        int kakao = StringUtil.getIntVal(CommonData.getInstance(this).getLink());
//        String kakao_link = CommonData.getInstance(this).getLink1();
////        if(i.getData() != null) {
////            kakao = StringUtil.getIntVal(i.getData().getQueryParameter("service"));
////        }
//        Log.i(TAG, "Push Data Check - service : " + kakao);
//        if(push_type > 0)
//            intent.putExtra(CommonData.EXTRA_PUSH_TYPE, push_type);
//        else if(kakao > 0) {
//            intent.putExtra(CommonData.EXTRA_PUSH_TYPE, kakao);
//            intent.putExtra("EVENT_POP", kakao_link);
//        }
//
//        if(push_type == FirebaseMessagingService.NEWS
//                //hsh start
//                ||push_type == FirebaseMessagingService.FEVER_MOVIE
//            //hsh end
//        ){
//            String info_sn = i.getStringExtra(CommonData.EXTRA_INFO_SN);
//            intent.putExtra(CommonData.EXTRA_INFO_SN, info_sn);
//        }
//
//        getIntent().removeExtra(CommonData.EXTRA_PUSH_TYPE);
//        getIntent().removeExtra(CommonData.EXTRA_INFO_SN);
//
//        startActivity(intent);
//        activityClear();
//        finish();


    /*
        switch(commonData.getJoinStep()){
            case CommonData.JOIN_STEP_0:    // 이용약관 동의안함

                break;
            case CommonData.JOIN_STEP_1:    // 기본정보 미입력
//                intent = new Intent(IntroBaseActivity.this, JoinBasicInfoActivity.class);
                startActivity(intent);
                activityClear();
                finish();
                break;
            case CommonData.JOIN_STEP_2:    // 비만도 미입력
//                intent = new Intent(IntroBaseActivity.this, JoinResultActivity.class);
                startActivity(intent);
                activityClear();
                finish();
                break;
            case CommonData.JOIN_STEP_3:    // 활동량 미입력
//                intent = new Intent(IntroBaseActivity.this, JoinActiveInfoActivity.class);
                startActivity(intent);
                activityClear();
                finish();
                break;
            case CommonData.JOIN_STEP_4:    // 가입 완료
                intent = new Intent(IntroBaseActivity.this, GrowthMainActivity.class);
                startActivity(intent);
                activityClear();
                finish();
                break;
        }
    */
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        GLog.i("onDestroy()", "dd");
        if(cursor != null)
            cursor.close();

        if(db != null)
            db.close();

        if(mHelper != null)
            mHelper.close();

        introBasehideProgress();

    }

    class ProductDBHelper extends SQLiteOpenHelper {  //새로 생성한 adapter 속성은 SQLiteOpenHelper이다.
        public ProductDBHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);    // db명과 버전만 정의 한다.
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
        }
    }

    /**
     * db 쿼리 asynctask 클래스
     */
    private class RequestDb extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            GLog.i(Util.getKorDateFormat(), "dd");
            StringBuilder result = new StringBuilder();

            for(int i=0; i<params.length; i++){
                GLog.i("params[" +i +"] = " +params[i], "dd");
                requestDb(params[i], i);
            }
            result.append("ok");

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            GLog.i("result = " +result, "dd");
            GLog.i(Util.getKorDateFormat(), "dd");
        }
    }

    /**
     * 프로그래스 활성화
     */
    public void introshowProgress() {

        if ( mProgress == null )
            mProgress = new MakeProgress(this);

        mProgress.show();
    }

    /**
     * 프로그래스 비활성화
     */
    public void introBasehideProgress() {

        if ( mProgress != null && mProgress.isShowing() )
            mProgress.dismiss();
    }

    /**
     * 클릭로그
     */
//    private void sendLog(Context context, Boolean switchMember){
//        String sendFlag = SharedPref.getInstance(context).getPreferences(SharedPref.SEND_LOG_DATE);
//
//        if(sendFlag == null ||sendFlag.equals("")){
//            sendFlag = CDateUtil.getToday_yyyy_MM_dd();
//            SharedPref.getInstance(context).savePreferences(SharedPref.SEND_LOG_DATE,sendFlag);
//        }
//
//        if(StringUtil.getLong(sendFlag) < StringUtil.getLong(CDateUtil.getToday_yyyy_MM_dd())){
//            Tr_asstb_menu_log_hist.RequestData reqData = new Tr_asstb_menu_log_hist.RequestData();
//            reqData.DATA = Tr_asstb_menu_log_hist.getArray(context);
//            reqData.DATA_LENGTH = String.valueOf(reqData.DATA.length());
//            reqData.mber_sn = CommonData.getInstance(this).getMberSn();
//
//            if(reqData.DATA.length() > 0) {
//
//                new ApiData().getData(context, Tr_asstb_menu_log_hist.class, reqData, new ApiData.IStep() {
//                    @Override
//                    public void next(Object obj) {
//
//                        if (obj instanceof Tr_asstb_menu_log_hist) {
//                            Tr_asstb_menu_log_hist data = (Tr_asstb_menu_log_hist) obj;
//                            if (data.result_code.equals("0000")) {
//                                DBHelper helper = new DBHelper(context);
//                                DBHelperLog db = helper.getLogDb();
//                                db.delete_log();
//
//                                SharedPref.getInstance(IntroBaseActivity.this).savePreferences(SharedPref.SEND_LOG_DATE, CDateUtil.getToday_yyyy_MM_dd());
//
//                                FirstData(context, switchMember);
//
//                            } else {
//                                FirstData(context, switchMember);
//                            }
//                        }
//                    }
//                });
//            } else {
//                FirstData(context, switchMember);
//            }
//        }else {
//            FirstData(context,switchMember);
//        }
//    }

    private void FirstData (Context context, Boolean switchMember){
        // 걸음수 등록하기 20190215
        moveActivity();
//        new IntroStepUpload(IntroBaseActivity.this, new BluetoothManager.IBluetoothResult() {
//            @Override
//            public void onResult(boolean isSuccess) {
//                if (TextUtils.isEmpty(commonData.getMberSn())) {
//                    if(switchMember){ ////정회원 전환 시
//                        activityClear();
//                        finish();
//                    }else{
//                        moveActivity();
//                    }
//
//                } else {
//                    new FirstDataGreenCare().doFirstData(context, new ApiData.IStep() {
//                        @Override
//                        public void next(Object obj) {
//                            if(switchMember){ //정회원 전환 시
//                                activityClear();
//                                finish();
//                            }else{
//                                moveActivity();
//                            }
//                        }
//                    });
//                }
//            }
//        }).start();
    }
}
