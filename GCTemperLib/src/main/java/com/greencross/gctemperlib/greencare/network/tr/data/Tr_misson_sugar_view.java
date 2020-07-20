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
 혈당 미션상세 상세(한달)
 json={   "api_code") // misson_sugar_view", "insures_code") // 303","mber_sn") // 1344"   ,"input_de") // 20180323" }

 <결과>
 //mission_sugar_cnt 체크일수
 // 미션시작 = mission_start_de
 // 미션종료 = 30에서 day_diff를 뺀다. 예) 30-day_diff


 // -- 공표시방법 --
 // mission_sugar_at : 성공여부
 // mission_order_de 가 오늘날자와 동일하면 붉은색 테두리 있는공
 // mission_chk_yn 가 1이면 성공 주황색공
 // mission_order_de 가 오늘이전날짜이고 mission_chk_yn 가 0이면 회색공
 */

public class Tr_misson_sugar_view extends BaseData {
    private final String TAG = Tr_misson_sugar_view.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String input_de;

    }

    public Tr_misson_sugar_view() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_misson_sugar_view.RequestData) {
            JSONObject body = new JSONObject();
            Tr_misson_sugar_view.RequestData data = (Tr_misson_sugar_view.RequestData) obj;

            body.put("api_code", getApiCode("Tr_misson_sugar_view") );
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
    @SerializedName("mission_code") // 12",
    public String mission_code;
    @SerializedName("mission_nm") // 30일 혈당",
    public String mission_nm;
    @SerializedName("mission_app_title") // 꾸준히 혈당 체크",
    public String mission_app_title;
    @SerializedName("mission_start_de") // 20180322",
    public String mission_start_de;
    @SerializedName("mission_end_de") // 20180420",
    public String mission_end_de;
    @SerializedName("mission_sugar_cnt") // 11",
    public String mission_sugar_cnt;
    @SerializedName("mission_sugar_at") // Y",
    public String mission_sugar_at;
    @SerializedName("day_diff") // 2",
    public String day_diff;


    @SerializedName("misson_sugar_day_chk")
    public List<SugarDayChk> misson_sugar_day_chk_list = new ArrayList<>(); //

    public class SugarDayChk {
        @SerializedName("mission_order_de")
        public String mission_order_de;
        @SerializedName("mission_sugar_de")
        public String mission_sugar_de;
        @SerializedName("mission_chk_yn")
        public String mission_chk_yn;
    }
}
