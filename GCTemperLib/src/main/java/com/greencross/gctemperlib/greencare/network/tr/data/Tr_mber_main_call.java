package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 *
 * 메인 페이지 데이터(건강점수, 포인트)
 메인페이지에서 "오늘의건강점수"호출
 중요) 메인페이지 접근 시 마다 호출해서 데이터가 갱신되어함.

 <입력>
 input_de :오늘 날짜


 <결과>
 "day_health_amt": 오늘의 건강점수
 "user_point_amt": 내포인트
 */

public class Tr_mber_main_call extends BaseData {
    private final String TAG = Tr_mber_main_call.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String input_de;

    }


    public Tr_mber_main_call() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_main_call.RequestData) {
            JSONObject body = new JSONObject();
            Tr_mber_main_call.RequestData data = (Tr_mber_main_call.RequestData) obj;

            body.put("api_code", getApiCode("Tr_mber_main_call") ); //
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn); //  1000
            body.put("input_de",  data.input_de); //

            return body;
        }

        return super.makeJson(obj);
    }


    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code") // mber_main_call",
    public String api_code; //
    @SerializedName("insures_code") // 303",
    public String insures_code; //
    @SerializedName("mber_sn") // 1344",
    public String mber_sn; //
    @SerializedName("day_health_amt") // 48",
    public String day_health_amt; //
    @SerializedName("user_point_amt") // 3800",
    public String user_point_amt; //
    @SerializedName("health_view_de") // 2018032918",
    public String health_view_de; //
    @SerializedName("data_yn") // Y"
    public String data_yn; //
}
