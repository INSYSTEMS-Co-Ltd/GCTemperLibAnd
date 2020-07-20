package com.greencross.gctemperlib.greencare.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by mrsohn on 2017. 3. 20..
 */

public class DBHelperCommunitySearches {
    private final String TAG = DBHelperCommunitySearches.class.getSimpleName();

    private DBHelper mHelper;
    public DBHelperCommunitySearches(DBHelper helper) {
        mHelper = helper;
    }


    public static final String TB_DATA_COMM_SEARCHES = "tb_data_comm_searches";
    public static String SEARCHES_IDX = "idx";                                 // 고유번호
    public static String SEARCHES_REGDATE = "regdate";                         // 등록일
    public static String SEARCHES_WORD = "word";               // 서버 등록 여부

    public static final String SQL_COMM_DELETE_AFTER_FIFTY="DELETE FROM "+TB_DATA_COMM_SEARCHES+" WHERE idx in (SELECT * FROM (SELECT idx FROM "+TB_DATA_COMM_SEARCHES+" ORDER BY idx DESC LIMIT 50, 100000))";
    public static final String SQL_COMM_INSERT_SEARCHEES="INSERT INTO "+TB_DATA_COMM_SEARCHES+" ("+SEARCHES_REGDATE+", "+SEARCHES_WORD+") VALUES ( datetime('now','localtime'), \'%s\')";
    public static final String SQL_COMM_SELECT_SEARCHES_ALL = "SELECT * FROM "+TB_DATA_COMM_SEARCHES+" ORDER BY idx DESC";
    public static final String SQL_COMM_DELETE_SEARCHES = "DELETE FROM "+TB_DATA_COMM_SEARCHES+" WHERE word=\'%s\'";
    public static final String SQL_COMM_DELETE_SEARCHES_ALL = "DELETE FROM "+TB_DATA_COMM_SEARCHES+"";

    // DB를 새로 생성할 때 호출되는 함수
    public String createDb() {
        // 새로운 테이블 생성
        String sql = "CREATE TABLE if not exists "+TB_DATA_COMM_SEARCHES+" ("
                            + SEARCHES_IDX+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + SEARCHES_REGDATE+" TEXT, "
                            + SEARCHES_WORD+" TEXT UNIQUE"
                            +"); ";
        Log.i(TAG, "onCreate.sql="+sql);
        return sql;
    }

    // 히스토리에서 선택된 DB로우를 삭제하는 함수
    public boolean deleteDb(String word){
        String sql = String.format(Locale.US,SQL_COMM_DELETE_SEARCHES,word);
        return mHelper.transactionExcuteB(sql);
    }

    public boolean deleteDbAll(){
        return mHelper.transactionExcuteB(SQL_COMM_DELETE_SEARCHES_ALL);
    }


    public void DeleteAfterFifty(){
        String sql ="DELETE FROM " + TB_DATA_COMM_SEARCHES + " WHERE idx in (SELECT * FROM (SELECT idx FROM "+TB_DATA_COMM_SEARCHES+" ORDER BY idx DESC LIMIT 50, 100000))" ;
        Log.i(TAG, "onDelete.sql = "+sql);
        mHelper.transactionExcute(sql);

    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    public String upgradeDb() {
        return "DROP TABLE "+TB_DATA_COMM_SEARCHES+";";
    }

    public boolean insert(String word) {
        boolean isSuccess=false;

        try{
            List<String> sql = new ArrayList<>();
            sql.add(String.format(Locale.US,SQL_COMM_DELETE_SEARCHES,word)); //기존에 동일한 키워드가 있으면 삭제
            sql.add(String.format(Locale.US, SQL_COMM_INSERT_SEARCHEES, word)); // 추가
            sql.add(SQL_COMM_DELETE_AFTER_FIFTY); // 50개 이상인 과거 키워드 삭제

            if(mHelper.transactionExcute(sql))
                isSuccess=true;

        }catch (Exception e){
            isSuccess=false;
        }

        return isSuccess;
    }

//    public List<CommunitySearchesData> getResultAll(DBHelper helper) {
//        // 읽기가 가능하게 DB 열기
//        SQLiteDatabase db = helper.getReadableDatabase();
//        List<CommunitySearchesData> data_list = new ArrayList<>();
//        Cursor cursor = db.rawQuery(SQL_COMM_SELECT_SEARCHES_ALL, null);
//        try {
//            cursor.moveToFirst();
//            do{
//                CommunitySearchesData data = new CommunitySearchesData();
//                data.idx = cursor.getInt(cursor.getColumnIndex(SEARCHES_IDX));
//                data.regdate = cursor.getString(cursor.getColumnIndex(SEARCHES_REGDATE));
//                data.word = cursor.getString(cursor.getColumnIndex(SEARCHES_WORD));
//                data_list.add(data);
//            }while(cursor.moveToNext());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            cursor.close();
//        }
//
//        return data_list;
//    }

    public String dropTable() {
        String sql = "DROP TABLE IF EXISTS " + TB_DATA_COMM_SEARCHES + ";";
        return sql;
    }
}