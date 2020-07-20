package com.greencross.gctemperlib.greencare.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.greencross.gctemperlib.BuildConfig;
import com.greencross.gctemperlib.greencare.util.Logger;

import java.util.List;

/**
 * Created by mrsohn on 2017. 3. 20..
 */

public class DBHelper extends SQLiteOpenHelper {
    private final String TAG                    = DBHelper.class.getSimpleName();

    // + ------------------------------------------------------------
    // 15 : 7/12 - 음식테이블레 unit 추가. (14->15업그래이드시 오류발생)
    // 16 : 7/13 - 음식테이블 DB_VERSION이 다르면 테이블 Drop하고 새로 만들도록 수정.
    // + ------------------------------------------------------------
    private static int DB_VERSION               = 23;
    public boolean isNewFood                    = false;        //새로운 음식디비 있음.
    public static String DB_NAME                = "greencare_db";
    public static final String DB_PATH = "/data/data/" + BuildConfig.LIBRARY_PACKAGE_NAME + "/databases/";

    public String MAIN_ITEM_TABLE               = "_item_table";
    public String MAIN_ITEM_COLUMN_IDX          = "_idx";
    public String MAIN_ITEM_COLUMN_ITEM         = "_item";
    public String MAIN_ITEM_COLUMN_VISIBLE      = "_visible";

    private DBHelperSugar mSugarDb              = new DBHelperSugar(DBHelper.this);
    private DBHelperPresure mPresureDb          = new DBHelperPresure(DBHelper.this);
    private DBHelperStep mStepDb                = new DBHelperStep(DBHelper.this);
    private DBHelperStepRealtime mStepRtimeDb   = new DBHelperStepRealtime(DBHelper.this);
    private DBHelperWater mWaterDb              = new DBHelperWater(DBHelper.this);
    private DBHelperWeight mWeightDb            = new DBHelperWeight(DBHelper.this);
    private DBHelperBasic mBasicDb              = new DBHelperBasic(DBHelper.this);
    private DBHelperMessage mMessageDb          = new DBHelperMessage(DBHelper.this);
    private DBHelperPPG mPpgDb                  = new DBHelperPPG(DBHelper.this);

    private DBHelperFoodMain mFoodMainDb        = new DBHelperFoodMain(DBHelper.this);
    private DBHelperFoodDetail mFoodDetailDb    = new DBHelperFoodDetail(DBHelper.this);

    private DBHelperFoodCalorie mFoodCalorieDb  = new DBHelperFoodCalorie(DBHelper.this);
    private DBHelperFoodCalorieSearchHis mFoodCalorieSearchHisDb  = new DBHelperFoodCalorieSearchHis(DBHelper.this);

    private DBHelperLog mLogDb    = new DBHelperLog(DBHelper.this);

    //커뮤니티
    private DBHelperCommunitySearches mCommunitySearches  = new DBHelperCommunitySearches(DBHelper.this);
    private DBHelperCommunityNotice mCommunityNotice = new DBHelperCommunityNotice(DBHelper.this);

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        Logger.i(TAG, "DBHelper:onCreate");
        String sql = "CREATE TABLE if not exists "  + MAIN_ITEM_TABLE + " ("
                                                    + MAIN_ITEM_COLUMN_IDX + " INTEGER, "
                                                    + MAIN_ITEM_COLUMN_ITEM + " TEXT, "
                                                    + MAIN_ITEM_COLUMN_VISIBLE + " BOOLEAN);";
        db.execSQL(sql);
        Logger.i(TAG, "onCreate.sql=" + sql);
        db.execSQL(mSugarDb.createDb());
        db.execSQL(mPresureDb.createDb());
        db.execSQL(mStepDb.createDb());
        db.execSQL(mStepRtimeDb.createDb());
        db.execSQL(mWaterDb.createDb());
        db.execSQL(mWeightDb.createDb());
        db.execSQL(mBasicDb.createDb());
        db.execSQL(mMessageDb.createDb());
        db.execSQL(mPpgDb.createDb());

        db.execSQL(mFoodMainDb.createDb());
        db.execSQL(mFoodDetailDb.createDb());
        db.execSQL(mFoodCalorieDb.createDb());
        db.execSQL(mFoodCalorieSearchHisDb.createDb());
        db.execSQL(mLogDb.createDb());

        //커뮤니티
        db.execSQL(mCommunitySearches.createDb());
        db.execSQL(mCommunityNotice.createDb());
    }


    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Logger.i(TAG, "DBHelper:onUpgrade");

        if (oldVersion != newVersion) {
            Logger.i(TAG, "DBHelper:onUpgrade.oldVersion=" + oldVersion + ", newVersion=" + newVersion);

            isNewFood = true;
            // 지우고 테이블 다시 만듦.
//            db.execSQL(mFoodCalorieSearchHisDb.dropTable());
//            db.execSQL(mLogDb.dropTable());

            //커뮤니티
            db.execSQL(mCommunitySearches.dropTable());
            db.execSQL(mCommunityNotice.dropTable());
            onCreate(db);
        }
    }

    public void insert(int idx, String _item, boolean _visible) {
        // DB에 입력한 값으로 행 추가
        String sql = "INSERT INTO " + MAIN_ITEM_TABLE
                                    + " VALUES(" + idx + ", '"
                                    + _item + "', '"
                                    + _visible + "');";
        Logger.i(TAG, "insert.sql=" + sql);
        transactionExcute(sql);
    }

    public void updateIdx(int idx, String item) {
        String sql = "UPDATE "  + MAIN_ITEM_TABLE + " SET "
                                + MAIN_ITEM_COLUMN_IDX + "=" + idx
                                + " WHERE " + MAIN_ITEM_COLUMN_ITEM + "='" + item + "';";
        Logger.i(TAG, "updateIdx.sql=" + sql);
        transactionExcute(sql);
    }

    public void updateVisible(boolean _visible, String item) {
        String sql = "UPDATE "  + MAIN_ITEM_TABLE + " SET "
                                + MAIN_ITEM_COLUMN_VISIBLE + "='" + _visible
                                + "' WHERE " + MAIN_ITEM_COLUMN_ITEM + "='" + item + "';";
        Logger.i(TAG, "updateVisible.sql=" + sql);
        transactionExcute(sql);

    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "DELETE FROM " + DBHelperSugar.TB_DATA_SUGAR;
        Logger.i(TAG, "delete.sql=" + sql);
        db.execSQL(sql);
        Logger.i(TAG, sql);

        String sql2 = "DELETE FROM " + DBHelperWeight.TB_DATA_WEIGHT;
        Logger.i(TAG, "delete.sql=" + sql2);
        db.execSQL(sql2);
        Logger.i(TAG, sql2);

        String sql3 = "DELETE FROM " + DBHelperFoodDetail.TB_DATA_FOOD_DETAIL;
        Logger.i(TAG, "delete.sql=" + sql3);
        db.execSQL(sql3);
        Logger.i(TAG, sql3);

        String sql4 = "DELETE FROM " + DBHelperFoodMain.TB_DATA_FOOD_MAIN;
        Logger.i(TAG, "delete.sql=" + sql4);
        db.execSQL(sql4);
        Logger.i(TAG, sql4);

        String sql5 = "DELETE FROM " + DBHelperMessage.TB_DATA_MESSAGE;
        Logger.i(TAG, "delete.sql=" + sql5);
        db.execSQL(sql5);
        Logger.i(TAG, sql5);

        String sql6 = "DELETE FROM " + DBHelperPresure.TB_DATA_PRESSURE;
        Logger.i(TAG, "delete.sql=" + sql6);
        db.execSQL(sql6);
        Logger.i(TAG, sql6);

        String sql7 = "DELETE FROM " + DBHelperStep.TB_DATA_STEP;
        Logger.i(TAG, "delete.sql=" + sql7);
        db.execSQL(sql7);
        Logger.i(TAG, sql7);

        String sql8 = "DELETE FROM " + DBHelperWater.TB_DATA_WATER;
        Logger.i(TAG, "delete.sql=" + sql8);
        db.execSQL(sql8);
        Logger.i(TAG, sql8);

        String sql9 = "DELETE FROM " + DBHelperPPG.TB_DATA_PPG;
        Logger.i(TAG, "delete.sql=" + sql9);
        db.execSQL(sql9);
        Logger.i(TAG, sql9);

        String sql10 = "DELETE FROM " + DBHelperStepRealtime.TB_DATA_STEP_REALTIME;
        Logger.i(TAG, "delete.sql=" + sql10);
        db.execSQL(sql10);
        Logger.i(TAG, sql10);

        String sql11 = "DELETE FROM " + DBHelperFoodCalorieSearchHis.Field.TB_DATA_FOOD_CALORIE_SEARCH_HIS;
        Logger.i(TAG, "delete.sql=" + sql11);
        db.execSQL(sql11);
        Logger.i(TAG, sql11);

        String sql12 = "DELETE FROM " + DBHelperLog.Field.TB_LOG;
        Logger.i(TAG, "delete.sql=" + sql10);
        db.execSQL(sql12);
        Logger.i(TAG, sql12);

        String sql13 = "DELETE FROM " + DBHelperCommunitySearches.TB_DATA_COMM_SEARCHES;
        Logger.i(TAG, "delete.sql=" + sql13);
        db.execSQL(sql13);
        Logger.i(TAG, sql13);

        String sql14 = "DELETE FROM " + DBHelperCommunityNotice.TB_DATA_COMM_NOTICE;
        Logger.i(TAG, "delete.sql=" + sql14);
        db.execSQL(sql14);
        Logger.i(TAG, sql14);

        db.close();
    }


    public void delete(String _item) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        String sql = "DELETE FROM " + MAIN_ITEM_TABLE + " WHERE " + MAIN_ITEM_COLUMN_ITEM + "='" + _item + "';";
        Logger.i(TAG, "delete.sql=" + sql);
        db.execSQL(sql);
        Logger.i(TAG, sql);
        db.close();
    }



    public void transactionExcute(String sql) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.beginTransaction();
            db.execSQL(sql);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        db.close();
    }


    public boolean transactionExcuteB(String sql) {
        boolean isSuccess=false;

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.execSQL(sql);
            db.setTransactionSuccessful();
            isSuccess=true;
        } catch (SQLException e) {
            e.printStackTrace();
            isSuccess=false;
        } finally {
            db.endTransaction();
        }

        db.close();

        return isSuccess;
    }

    //sql 만큼 쿼리 수행
    public boolean transactionExcute(List<String> sql) {

        boolean isSuccess=false;

        if(sql==null||sql.size()<1)
            return isSuccess;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            for(int i=0;i<sql.size();i++) {
                db.execSQL(sql.get(i));
            }

            db.setTransactionSuccessful();
            isSuccess=true;
        } catch (SQLException e) {
            e.printStackTrace();
            isSuccess=false;
        } finally {
            db.endTransaction();
        }

        db.close();
        return isSuccess;
    }

    public DBHelperSugar getSugarDb() {
        return mSugarDb;
    }

    public DBHelperPresure getPresureDb() {
        return mPresureDb;
    }

    public DBHelperStep getStepDb() {
        return mStepDb;
    }

    public DBHelperStepRealtime getmStepRtimeDb() {
        return mStepRtimeDb;
    }

    public DBHelperPPG getPPGDb() {
        return mPpgDb;
    }

    public DBHelperWater getWaterDb() {
        return mWaterDb;
    }

    public DBHelperWeight getWeightDb() {
        return mWeightDb;
    }

    public DBHelperBasic getBasicDb() {
        return mBasicDb;
    }

    public DBHelperMessage getMessageDb() {
        return mMessageDb;
    }

    public DBHelperFoodMain getFoodMainDb() {
        return mFoodMainDb;
    }

    public DBHelperFoodDetail getFoodDetailDb() {
        return mFoodDetailDb;
    }

    public DBHelperFoodCalorie getFoodCalorieDb() {
        return mFoodCalorieDb;
    }

    public DBHelperFoodCalorieSearchHis getFoodCalorieSearchHisDb() {
        return mFoodCalorieSearchHisDb;
    }

    public DBHelperLog getLogDb() {
        return mLogDb;
    }

    //커뮤니티
    public DBHelperCommunitySearches getmCommunitySearches(){
        if (mCommunitySearches ==null)
            mCommunitySearches = new DBHelperCommunitySearches(DBHelper.this);
        return mCommunitySearches;
    }

    public DBHelperCommunityNotice getmCommunityNotice(){
        if (mCommunityNotice ==null)
            mCommunityNotice = new DBHelperCommunityNotice(DBHelper.this);
        return mCommunityNotice;
    }
}