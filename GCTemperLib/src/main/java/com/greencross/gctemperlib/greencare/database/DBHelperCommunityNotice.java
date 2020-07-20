package com.greencross.gctemperlib.greencare.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class DBHelperCommunityNotice {
    private final String TAG = DBHelperCommunityNotice.class.getSimpleName();

    private DBHelper mHelper;
    public DBHelperCommunityNotice(DBHelper helper) {
        mHelper = helper;
    }

    public static final String TB_DATA_COMM_NOTICE = "tb_data_comm_notice";
    public static String NOTICE_IDX = "idx";
    public static String NOTICE_REGDATE = "regdate";
    public static String NOTICE_MSG = "msg";
    public static String NOTICE_CMSEQ = "cm_seq";
    public static String NOTICE_ISNEW = "isnew";
    public static String NOTICE_ALMSEQ = "alm_seq";

    public static String NOTICE_PROFILEPIC = "profile_pic";
    public static String NOTICE_MBERGRAD = "mber_grad";
    public static String NOTICE_CMGUBUN = "cm_gubun";
    public static String NOTICE_MBERSN = "mber_sn";
    public static String NOTICE_NICK = "nick";



    public static final String SQL_COMM_DELETE_AFTER_FIFTY="DELETE FROM "+TB_DATA_COMM_NOTICE+" WHERE idx in (SELECT * FROM (SELECT idx FROM "+TB_DATA_COMM_NOTICE+" ORDER BY idx DESC LIMIT 50, 100000))";
    public static final String SQL_COMM_INSERT_NOTICE="INSERT INTO "+TB_DATA_COMM_NOTICE+" ("+NOTICE_REGDATE+", "+NOTICE_MSG+","+NOTICE_CMSEQ+","+NOTICE_ALMSEQ+","+NOTICE_PROFILEPIC+","+NOTICE_MBERGRAD+","+NOTICE_CMGUBUN+","+NOTICE_MBERSN+","+NOTICE_NICK+"   ) VALUES (  \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\')";
    public static final String SQL_COMM_SELECT_NOTICE_ALL = "SELECT * FROM "+TB_DATA_COMM_NOTICE+" ORDER BY cast(ALM_SEQ as integer) DESC";
    public static final String SQL_COMM_SELECT_NOTICE_ISNEW = "SELECT idx FROM "+TB_DATA_COMM_NOTICE+" WHERE isnew='true'";
    public static final String SQL_COMM_SELECT_NOTICE_ALMSEQ = "SELECT max(cast(alm_seq as integer)) as MAX_INDEX FROM "+TB_DATA_COMM_NOTICE+" ";
    public static final String SQL_COMM_DELETE_NOTICE_ALL = "DELETE FROM "+TB_DATA_COMM_NOTICE+"";
    public static final String SQL_COMM_UPDATE_NOTICE_ISNEW = "UPDATE "+TB_DATA_COMM_NOTICE+" SET isnew='false' where isnew='true'";
    public static final String SQL_COMM_UPDATE_NOTICE_ALMSEQ="UPDATE "+TB_DATA_COMM_NOTICE+" SET regdate=\'%s\',msg=\'%s\',cm_seq=\'%s\', profile_pic=\'%s\',mber_grad=\'%s\',cm_gubun=\'%s\',mber_sn=\'%s\',nick=\'%s\' WHERE alm_seq=\'%s\'";
    // DB를 새로 생성할 때 호출되는 함수
    public String createDb() {
        // 새로운 테이블 생성
        String sql = "CREATE TABLE if not exists "+TB_DATA_COMM_NOTICE+" ("
                            + NOTICE_IDX+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + NOTICE_REGDATE+" TEXT, "
                            + NOTICE_MSG+" TEXT, "
                            + NOTICE_CMSEQ+" TEXT, "
                            + NOTICE_ALMSEQ+" TEXT UNIQUE, "
                            + NOTICE_PROFILEPIC+" TEXT, "
                            + NOTICE_MBERGRAD+" TEXT, "
                            + NOTICE_CMGUBUN+" TEXT, "
                            + NOTICE_MBERSN+" TEXT, "
                            + NOTICE_NICK+" TEXT, "
                            + NOTICE_ISNEW+" TEXT DEFAULT true "
                            +"); ";
        Log.i(TAG, "onCreate.sql="+sql);
        return sql;
    }

//    // 히스토리에서 선택된 DB로우를 삭제하는 함수
//    public boolean deleteDb(String word){
//        String sql = String.format(Locale.US,SQL_COMM_DELETE_NOTICE,word);
//        return mHelper.transactionExcuteB(sql);
//    }

    public boolean deleteDbAll(){
        return mHelper.transactionExcuteB(SQL_COMM_DELETE_NOTICE_ALL);
    }


    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    public String upgradeDb() {
        return "DROP TABLE "+TB_DATA_COMM_NOTICE+";";
    }

    public boolean insert(String REGDATE,String MSG, String CMSEQ, String ALMSEQ,String PROFILE_PIC, String MBER_GRAD,String CM_GUBUN,String MBER_SN,String NICK) {
        boolean isSuccess=false;

        try{
            List<String> sql = new ArrayList<>();
            sql.add(String.format(Locale.US, SQL_COMM_INSERT_NOTICE, REGDATE,MSG,CMSEQ,ALMSEQ,PROFILE_PIC,MBER_GRAD,CM_GUBUN,MBER_SN,NICK)); // 추가
            sql.add(SQL_COMM_DELETE_AFTER_FIFTY); // 50개 이상인 과거 키워드 삭제

            if(mHelper.transactionExcute(sql))
                isSuccess=true;

        }catch (Exception e){
            isSuccess=false;
        }

        return isSuccess;
    }

//    public List<CommunityNoticeData> getResultAll(DBHelper helper) {
//        // 읽기가 가능하게 DB 열기
//        SQLiteDatabase db = helper.getReadableDatabase();
//        List<CommunityNoticeData> data_list = new ArrayList<>();
//        Cursor cursor = db.rawQuery(SQL_COMM_SELECT_NOTICE_ALL, null);
//        try {
//            cursor.moveToFirst();
//            do{
//                CommunityNoticeData data = new CommunityNoticeData();
//                data.IDX = cursor.getInt(cursor.getColumnIndex(NOTICE_IDX));
//                data.REGDATE = cursor.getString(cursor.getColumnIndex(NOTICE_REGDATE));
//                data.MSG = cursor.getString(cursor.getColumnIndex(NOTICE_MSG));
//                data.CM_SEQ = cursor.getString(cursor.getColumnIndex(NOTICE_CMSEQ));
//                data.ALM_SEQ = cursor.getString(cursor.getColumnIndex(NOTICE_ALMSEQ));
//
//                data.PROFILE_PIC = cursor.getString(cursor.getColumnIndex(NOTICE_PROFILEPIC));
//                data.MBER_GRAD = cursor.getString(cursor.getColumnIndex(NOTICE_MBERGRAD));
//                data.CM_GUBUN = cursor.getString(cursor.getColumnIndex(NOTICE_CMGUBUN));
//                data.MBER_SN = cursor.getString(cursor.getColumnIndex(NOTICE_MBERSN));
//                data.NICK = cursor.getString(cursor.getColumnIndex(NOTICE_NICK));
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
        String sql = "DROP TABLE IF EXISTS " + TB_DATA_COMM_NOTICE + ";";
        return sql;
    }

    public String getMaxALMSEQ(DBHelper helper){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        String ALM_SEQ="0";
        try{
            cursor = db.rawQuery(SQL_COMM_SELECT_NOTICE_ALMSEQ,null);

            cursor.moveToFirst();
            ALM_SEQ = cursor.getString(cursor.getColumnIndex("MAX_INDEX"));

            if(ALM_SEQ==null)
                ALM_SEQ="0";


        }catch (Exception e){
            e.printStackTrace();
            ALM_SEQ="0";
        }finally{
            cursor.close();
        }

        return ALM_SEQ;
    }

    public boolean getisNew(DBHelper helper) {
        boolean isNew = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(SQL_COMM_SELECT_NOTICE_ISNEW,null);
            cursor.moveToFirst();

            if(cursor.getCount()>0){
                isNew=true;
            }else{
                isNew=false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            cursor.close();
        }

        return isNew;
    }

    public boolean updateIsNew() {
        return mHelper.transactionExcuteB(SQL_COMM_UPDATE_NOTICE_ISNEW);
    }

    public void update(String regdate, String msg, String cm_seq, String alm_seq,String PROFILE_PIC, String MBER_GRAD,String CM_GUBUN,String MBER_SN,String NICK) {
        mHelper.transactionExcuteB(String.format(Locale.US,SQL_COMM_UPDATE_NOTICE_ALMSEQ,regdate,msg,cm_seq,PROFILE_PIC,MBER_GRAD,CM_GUBUN,MBER_SN,NICK,alm_seq));
    }
}