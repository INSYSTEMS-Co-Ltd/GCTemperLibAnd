package com.greencross.gctemperlib.greencare.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrsohn on 2017. 3. 20..
 */

public class DBHelperLog {
    private final String TAG = DBHelperLog.class.getSimpleName();

    private DBHelper mHelper;
    public DBHelperLog(DBHelper helper) {
        mHelper = helper;
    }

    // DB를 새로 생성할 때 호출되는 함수
    public String createDb() {
        // 새로운 테이블 생성
        Field tb = new Field();
        String sql = " CREATE TABLE if not exists " + tb.TB_LOG + " ("
                    + tb.L_COD + " TEXT NOT NULL, "
                    + tb.M_COD + " TEXT, "
                    + tb.S_COD   + " TEXT, "
                    + tb.TIME + " INTEGER, "
                    + tb.COUNT + " INTEGER, "
                    + tb.REGDATE + " TEXT); ";
        Logger.i(TAG, "onCreate.sql=" + sql);
        return sql;

    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    public String upgradeDb() {
        Field tb = new Field();
        return "DROP TABLE " + tb.TB_LOG + ";";
    }

    public String dropTable() {
        Field tb = new Field();
        String sql = "DROP TABLE IF EXISTS " + tb.TB_LOG + ";";
        Logger.i(TAG, "DropTable.sql=" + sql);
        return sql;
    }

    /**
     * 히스토리 삭제
     *
     */
    public void delete_log() {

        Field tb = new Field();
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String sql = "DELETE FROM " + tb.TB_LOG;
        Logger.i(TAG, "delete.sql=" + sql);
        db.execSQL(sql);
//        mHelper.transactionExcute(sb.toString());
    }


    //로그 히스토리 등록

    public void insert(String l_cod,String m_cod,String s_cod, int time,int count) {
        Field tb = new Field();

        String sql = "INSERT INTO " + tb.TB_LOG + "(l_cod,m_cod,s_cod,time,count,regdate) VALUES ('" + l_cod + "', '" + m_cod + "', '" + s_cod + "', " + time + ", " + count + ", '" + CDateUtil.getToday_yyyy_MM_dd()+"')";
        Logger.i(TAG, "insert.sql=" + sql);

        mHelper.transactionExcute(sql);

    }

    //로그 히스토리 조회

    public List<DBHelperLog.Data> getlog() {
        List<DBHelperLog.Data> dataList = new ArrayList<>();

        Field tb = new Field();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String sql = "SELECT l_cod,CASE(m_cod) WHEN '' THEN '' ELSE l_cod||m_cod END AS m_cod, CASE(s_cod) WHEN '' THEN '' ELSE l_cod||m_cod||s_cod END AS s_cod, CASE sum(time) WHEN 0 THEN 0 ELSE sum(time) END AS time, CASE sum(count) WHEN 0 THEN 0 ELSE sum(count) END AS count, regdate " +
                "FROM tb_log GROUP BY regdate,l_cod,m_cod,s_cod";

        Logger.i(TAG, sql);
        Cursor cursor = db.rawQuery(sql, null);

        Logger.i(TAG, "query =" + cursor.getCount());
        if (cursor.getCount() > 0) {
            try {
                int idx = 0;
                while (cursor.moveToNext()) {
                    DBHelperLog.Data data = new Data();
                    data.l_cod = cursor.getString(cursor.getColumnIndex(tb.L_COD));
                    data.m_cod = cursor.getString(cursor.getColumnIndex(tb.M_COD));
                    data.s_cod = cursor.getString(cursor.getColumnIndex(tb.S_COD));
                    data.time = cursor.getString(cursor.getColumnIndex(tb.TIME));
                    data.count = cursor.getString(cursor.getColumnIndex(tb.COUNT));
                    data.regdate = cursor.getString(cursor.getColumnIndex(tb.REGDATE));
                    dataList.add(data);

                    Logger.i(TAG, "idx="+idx+", data.time="+data.time+", count="+data.count + "l_cod="+data.l_cod);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        } else {
        }

        return dataList;
    }

    public static class Field {
        public static String TB_LOG = "tb_log";

        public static String L_COD = "l_cod";      // 대분류
        public static String M_COD = "m_cod";      // 중분류
        public static String S_COD = "s_cod";      // 소분류
        public static String TIME = "time";        // 대기시간
        public static String COUNT = "count";      // 클릭 수
        public static String REGDATE = "regdate";  // 등록날짜
    }


    public static class Data implements Parcelable {
        public String l_cod;
        public String m_cod;
        public String s_cod;
        public String time;
        public String count;
        public String regdate;


        public Data(Parcel in) {
            l_cod = in.readString();
            m_cod = in.readString();
            s_cod = in.readString();
            time = in.readString();
            count = in.readString();
            regdate = in.readString();
        }

        public Data() {}

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(l_cod);
            dest.writeString(m_cod);
            dest.writeString(s_cod);
            dest.writeString(time);
            dest.writeString(count);
            dest.writeString(regdate);
        }
    }
}
