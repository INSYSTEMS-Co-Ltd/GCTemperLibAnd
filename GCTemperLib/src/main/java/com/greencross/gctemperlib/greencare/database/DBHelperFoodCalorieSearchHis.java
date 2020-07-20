package com.greencross.gctemperlib.greencare.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.greencross.gctemperlib.greencare.database.util.ChoSearchQuery;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_get_meal_input_food_data;
import com.greencross.gctemperlib.greencare.util.Logger;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrsohn on 2017. 3. 20..
 */

public class DBHelperFoodCalorieSearchHis {
    private final String TAG = DBHelperFoodCalorieSearchHis.class.getSimpleName();

    private DBHelper mHelper;
    public DBHelperFoodCalorieSearchHis(DBHelper helper) {
        mHelper = helper;
    }

    // DB를 새로 생성할 때 호출되는 함수
    public String createDb() {
        // 새로운 테이블 생성
        Field tb = new Field();
        String sql = " CREATE TABLE if not exists " + tb.TB_DATA_FOOD_CALORIE_SEARCH_HIS + " ("
                    + tb.FOOD_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + tb.FOOD_CODE + " INTEGER NOT NULL, "
                    + tb.FOOD_KIND + " VARCHAR(30) NULL, "
                    + tb.FOOD_NAME   + " VARCHAR(150) NULL, "
                    + tb.FOOD_GRAM + " VARCHAR(30) NULL, "
                    + tb.FOOD_UNIT + " VARCHAR(30) NULL, "
                    + tb.FOOD_CALORIE + " VARCHAR(30) NULL, "
                    + tb.FOOD_CARBOHYDRATE + " VARCHAR(30) NULL, "
                    + tb.FOOD_PROTEIN + " VARCHAR(30) NULL, "
                    + tb.FOOD_FAT + " VARCHAR(30) NULL, "
                    + tb.FOOD_SUGARS + " VARCHAR(30) NULL, "
                    + tb.FOOD_SALT + " VARCHAR(30) NULL, "
                    + tb.FOOD_CHOLESTEROL + " VARCHAR(30) NULL, "
                    + tb.FOOD_SATURATED + " VARCHAR(30) NULL, "
                    + tb.FOOD_TRANSQUANTI + " VARCHAR(30) NULL); ";
        Logger.i(TAG, "onCreate.sql=" + sql);
        return sql;

    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    public String upgradeDb() {
        Field tb = new Field();
        return "DROP TABLE " + tb.TB_DATA_FOOD_CALORIE_SEARCH_HIS + ";";
    }

    public String dropTable() {
        Field tb = new Field();
        String sql = "DROP TABLE IF EXISTS " + tb.TB_DATA_FOOD_CALORIE_SEARCH_HIS + ";";
        Logger.i(TAG, "DropTable.sql=" + sql);
        return sql;
    }

    public void initFoodCalorieDb(BufferedReader reader) {
        Field tb = new Field();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            String tableName =tb.FOOD_CODE+","
                            + tb.FOOD_KIND+","
                            + tb.FOOD_NAME+","
                            + tb.FOOD_GRAM+","
                            + tb.FOOD_UNIT+","
                            + tb.FOOD_CALORIE+","
                            + tb.FOOD_CARBOHYDRATE+","
                            + tb.FOOD_PROTEIN+","
                            + tb.FOOD_FAT+","
                            + tb.FOOD_SUGARS+","
                            + tb.FOOD_SALT+","
                            + tb.FOOD_CHOLESTEROL+","
                            + tb.FOOD_SATURATED+","
                            + tb.FOOD_TRANSQUANTI;
            String str1 = "INSERT INTO " + tb.TB_DATA_FOOD_CALORIE_SEARCH_HIS +" ("+tableName+") VALUES(";
            String str2 = ");";

            int idx = 0;
            String line = "";
            while ((line = reader.readLine()) != null) {
                try {
                    StringBuilder sb = new StringBuilder(str1);
                    String[] str = line.split(",");
                    if (str != null && str.length >= 14) {
                        sb.append("'"+str[0] + "','");
                        sb.append(str[1] + "','");
                        sb.append(str[2] + "','");
                        sb.append(str[3] + "','");
                        sb.append(str[4] + "','");
                        sb.append(str[5] + "','");
                        sb.append(str[6] + "','");
                        sb.append(str[7] + "','");
                        sb.append(str[8] + "','");
                        sb.append(str[9] + "','");
                        sb.append(str[10] + "','");
                        sb.append(str[11] + "','");
                        sb.append(str[12] + "','");
                        sb.append(str[13] + "'");
                        sb.append(str2);
                        String sql = sb.toString();
                        mHelper.transactionExcute(sql);
                        Logger.i(TAG, "food_code["+(idx++)+"]="+line);
                    } else {
                        Logger.e(TAG,"food_code No lenth 12 ["+(idx++)+"]="+ line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    public void insert(DBHelperFoodCalorie.Data datas) {
        // DB에 입력한 값으로 행 추가
        delete(datas.food_name);
        StringBuffer sb = new StringBuffer();
        Field tb = new Field();
        String sql = "INSERT INTO " + tb.TB_DATA_FOOD_CALORIE_SEARCH_HIS + ""
                + " VALUES";
        sb.append(sql);

            String values = "("+null+", '"
                    + datas.food_code + "', '" // 4",
                    + datas.food_kind + "', '" // 4",
                    + datas.food_name + "', '" // 400",
                    + datas.food_gram + "', '" // 530",
                    + datas.food_unit + "', '" // 3",
                    + datas.food_calorie + "', '" // 310",
                    + datas.food_carbohydrate + "', '" // 141",
                    + datas.food_protein + "', '" // 52",
                    + datas.food_fat + "', '" // 3",
                    + datas.food_sugars + "', '" // 131",
                    + datas.food_salt + "', '" // D",
                    + datas.food_cholesterol + "', '" // D",
                    + datas.food_saturated + "', '" // D",
                    + datas.food_transquanti + " ') "; // 201703301420"


            sb.append(values);
        Logger.i(TAG, "FoodHistoryinsert.sql=" + sb.toString());
        mHelper.transactionExcute(sb.toString());
    }

    /**
     * 히스토리 삭제
     * @param idx
     */
    public void delete(String idx) {
        if (TextUtils.isEmpty(idx))
            return;

        Field tb = new Field();
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String sql = "DELETE FROM " + tb.TB_DATA_FOOD_CALORIE_SEARCH_HIS
                + " Where name='" + idx + "'";
        Logger.i(TAG, "delete.sql=" + sql);
        db.execSQL(sql);
//        mHelper.transactionExcute(sb.toString());
    }

    public Cursor getResult(String searchStr) {
        Field tb = new Field();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "Select "+ tb.FOOD_CODE +" AS _id, "
                + tb.FOOD_CODE +","
                + tb.FOOD_KIND+","
                + tb.FOOD_NAME+","
                + tb.FOOD_GRAM+","
                + tb.FOOD_UNIT+","
                + tb.FOOD_CALORIE+","
                + tb.FOOD_CARBOHYDRATE+","
                + tb.FOOD_PROTEIN+","
                + tb.FOOD_FAT+","
                + tb.FOOD_SUGARS+","
                + tb.FOOD_SALT+","
                + tb.FOOD_CHOLESTEROL+","
                + tb.FOOD_SATURATED+","
                + tb.FOOD_TRANSQUANTI
                + " FROM " + tb.TB_DATA_FOOD_CALORIE_SEARCH_HIS;
        if (TextUtils.isEmpty(searchStr) == false) {
            sql += " WHERE ";
            sql += ChoSearchQuery.makeQuery(searchStr);
        }
        sql += " Order by "+tb.FOOD_IDX +" DESC;";

        Logger.i(TAG, sql);

        Cursor cursor = db.rawQuery(sql, null);
        Logger.i(TAG, "query =" + cursor.getCount());

        return cursor;
    }

    public List<Data> getResult(List<Tr_get_meal_input_food_data.ReceiveDatas> foodList) {
        List<Data> dataList = new ArrayList<>();

        if (foodList.size() <= 0)
            return dataList;

        Field tb = new Field();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "Select "
                        + tb.FOOD_CODE +","
                        + tb.FOOD_KIND+","
                        + tb.FOOD_NAME+","
                        + tb.FOOD_GRAM+","
                        + tb.FOOD_UNIT+","
                        + tb.FOOD_CALORIE+","
                        + tb.FOOD_CARBOHYDRATE+","
                        + tb.FOOD_PROTEIN+","
                        + tb.FOOD_FAT+","
                        + tb.FOOD_SUGARS+","
                        + tb.FOOD_SALT+","
                        + tb.FOOD_CHOLESTEROL+","
                        + tb.FOOD_SATURATED+","
                        + tb.FOOD_TRANSQUANTI+","
                        + tb.FOOD_CODE +" AS _id "
                + " FROM " + tb.TB_DATA_FOOD_CALORIE_SEARCH_HIS;
        if (foodList != null && foodList.size() > 0) {
            sql += " WHERE ";
            int cnt = 0;
            for (Tr_get_meal_input_food_data.ReceiveDatas data : foodList) {
                sql += tb.FOOD_CODE +"=";
                sql += "'"+data.foodcode+"'";
                if (cnt != foodList.size()-1) {
                    sql += " OR ";
                }
                cnt++;
            }
        }
        sql += " Order by "+tb.FOOD_NAME +" ASC LIMIT 200;";

        Logger.i(TAG, sql);
        Cursor cursor = db.rawQuery(sql, null);

        Logger.i(TAG, "query =" + cursor.getCount());
        if (cursor.getCount() > 0) {
            try {
                int idx = 0;
                while (cursor.moveToNext()) {
                    DBHelperFoodCalorieSearchHis.Data data = new Data();
                    data.food_code = cursor.getString(cursor.getColumnIndex(tb.FOOD_CODE));
                    data.food_kind = cursor.getString(cursor.getColumnIndex(tb.FOOD_KIND));
                    data.food_name = cursor.getString(cursor.getColumnIndex(tb.FOOD_NAME));
                    data.food_gram = cursor.getString(cursor.getColumnIndex(tb.FOOD_GRAM));
                    data.food_unit = cursor.getString(cursor.getColumnIndex(tb.FOOD_UNIT));
                    data.food_calorie = cursor.getString(cursor.getColumnIndex(tb.FOOD_CALORIE));
                    data.food_carbohydrate  = cursor.getString(cursor.getColumnIndex(tb.FOOD_CARBOHYDRATE));
                    data.food_protein = cursor.getString(cursor.getColumnIndex(tb.FOOD_PROTEIN));
                    data.food_sugars = cursor.getString(cursor.getColumnIndex(tb.FOOD_SUGARS));
                    data.food_salt = cursor.getString(cursor.getColumnIndex(tb.FOOD_SALT));
                    data.food_cholesterol = cursor.getString(cursor.getColumnIndex(tb.FOOD_CHOLESTEROL));
                    data.food_saturated = cursor.getString(cursor.getColumnIndex(tb.FOOD_SATURATED));
                    data.forpeople = cursor.getString(cursor.getColumnIndex(tb.FORPEOPLE));
                    data.food_transquanti = cursor.getString(cursor.getColumnIndex(tb.FOOD_TRANSQUANTI));
                    // 전문등록때 사용되는 idx
                    data.food_idx = foodList.get(idx++).idx;
                    dataList.add(data);

                    Logger.i(TAG, "idx="+idx+", data.food_code="+data.food_code+", food_name="+data.food_name);
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
        public static String TB_DATA_FOOD_CALORIE_SEARCH_HIS = "tb_data_food_calorie_search_his";

        public static String FOOD_IDX = "idx";                // 고유번호
        public static String FOOD_CODE = "code";                // 고유번호
        public static String FOOD_KIND = "kind";                // 식품군
        public static String FOOD_NAME = "name";                // 식품군
        public static String FOOD_GRAM = "gram";                // 1회제공량
        public static String FOOD_UNIT = "unit";                // 제공량 단위
        public static String FOOD_CALORIE = "calorie";          // 열량
        public static String FOOD_CARBOHYDRATE = "carbohydrate";// 탄수화물
        public static String FOOD_PROTEIN = "protein";          // 단백질
        public static String FOOD_FAT = "fat";                  // 지방
        public static String FOOD_SUGARS = "sugars";            // 당류
        public static String FOOD_SALT = "salt";                // 나트륨
        public static String FOOD_CHOLESTEROL = "cholesterol";  // 콜레스테롤
        public static String FOOD_SATURATED = "saturated";      // 포화지방산
        public static String FOOD_TRANSQUANTI = "ctransquantic";// 트랜스지방산
        public static String FORPEOPLE = "forpeople";           // 인분
    }


    public static class Data implements Parcelable {
        public String food_idx;

        public String idx;
        public String food_code;
        public String food_kind;
        public String food_name;
        public String food_gram;
        public String food_unit;
        public String food_calorie;
        public String food_carbohydrate;
        public String food_protein;
        public String food_fat;
        public String food_sugars;
        public String food_salt;
        public String food_cholesterol;
        public String food_saturated;
        public String food_transquanti;
        public String food_ctransquantic;

        public String forpeople; // db 저장을 위한 값


        public Data(Parcel in) {
            idx = in.readString();
            food_idx = in.readString();
            food_code = in.readString();
            food_kind = in.readString();
            food_name = in.readString();
            food_gram = in.readString();
            food_unit = in.readString();
            food_calorie = in.readString();
            food_carbohydrate = in.readString();
            food_protein = in.readString();
            food_fat = in.readString();
            food_sugars = in.readString();
            food_salt = in.readString();
            food_cholesterol = in.readString();
            food_saturated = in.readString();
            food_transquanti = in.readString();
            forpeople = in.readString();
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
            dest.writeString(food_idx);
            dest.writeString(idx);

            dest.writeString(food_code);
            dest.writeString(food_kind);
            dest.writeString(food_name);
            dest.writeString(food_gram);
            dest.writeString(food_unit);
            dest.writeString(food_calorie);
            dest.writeString(food_carbohydrate);
            dest.writeString(food_protein);
            dest.writeString(food_fat);
            dest.writeString(food_sugars);
            dest.writeString(food_salt);
            dest.writeString(food_cholesterol);
            dest.writeString(food_saturated);
            dest.writeString(food_transquanti);
            dest.writeString(forpeople);
        }
    }
}