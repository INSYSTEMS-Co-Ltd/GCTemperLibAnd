package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
일일미션 상세
 json={   "api_code": "misson_health_day_amt_list", "insures_code": "303","mber_sn": "1344"   ,"input_de": "20180323" }

 //<요청>
 // input_de : 오늘 날짜


 //<결과>
 // accml_snd_amt : 달성포인트
 // accml_snd_day_amt : 금일지급포인트

 // {
 // health_nm : 활동
 // chk_cnt : 회수
 // sum_snd_amt_point : 포인트 (포인트가 0보다 크면 성공임, 색상을 파랑색으로)
 // }


 */

public class Tr_misson_health_day_amt_list extends BaseData {
    private final String TAG = Tr_misson_health_day_amt_list.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String input_de;

    }

    public Tr_misson_health_day_amt_list() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_misson_health_day_amt_list.RequestData) {
            JSONObject body = new JSONObject();
            Tr_misson_health_day_amt_list.RequestData data = (Tr_misson_health_day_amt_list.RequestData) obj;

            body.put("api_code", getApiCode("Tr_misson_health_day_amt_list") );
            body.put("insures_code", INSURES_CODE);

            body.put("mber_sn", data.mber_sn);
            body.put("input_de", data.input_de);

            return body;
        }

        return super.makeJson(obj);
    }

    public JSONArray getArray(Tr_get_hedctdata.DataList dataList) {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            obj.put("idx" , dataList.idx ); //170410173713859",
            array.put(obj);
        } catch (JSONException e) {
            Logger.e(e);
        }

        return array;
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/
    @SerializedName("api_code")
    public String api_code; //
    @SerializedName("accml_snd_amt")
    public String accml_snd_amt; //
    @SerializedName("accml_snd_day_amt")
    public String accml_snd_day_amt; //
    @SerializedName("misson_health_day")
    public List<HealthDay> misson_health_day_list = new ArrayList<>(); //

    public class HealthDay {
        @SerializedName("health_code")
        public String health_code;
        @SerializedName("health_nm")
        public String health_nm;
        @SerializedName("sum_work_cnt")
        public String sum_work_cnt;
        @SerializedName("chk_cnt")
        public String chk_cnt;
        @SerializedName("sum_snd_amt_point")
        public String sum_snd_amt_point;
    }
}
