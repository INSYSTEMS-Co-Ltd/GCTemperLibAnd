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
걷기미션상세 상세(한달)
 json={   "api_code": "misson_walk_view", "insures_code": "303","mber_sn": "1344"   ,"input_de": "20180329" }

 // <요청>
 // input_de는 항상 오늘날짜가 들어가야함.

 // <결과>
 // mission_walk_cnt : 현재의 걸음수.
 // mission_walk_at : 성공여부.
 // day_diff : 미션종료 (30에서 day_diff를 빼야지 미션종료 "00일전"이 나옴)
 */

public class Tr_misson_walk_view extends BaseData {
    private final String TAG = Tr_misson_walk_view.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String input_de;

    }

    public Tr_misson_walk_view() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_misson_walk_view.RequestData) {
            JSONObject body = new JSONObject();
            Tr_misson_walk_view.RequestData data = (Tr_misson_walk_view.RequestData) obj;

            body.put("api_code", getApiCode("Tr_misson_walk_view") );
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
    @SerializedName("misson_walk_view")
    public List<WlakView> misson_walk_view_list = new ArrayList<>(); //

    public class WlakView {
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




        @SerializedName("mission_code") //11",
        public String mission_code;
        @SerializedName("mission_nm") //30일 걷기",
        public String mission_nm;
        @SerializedName("mission_app_title") //걷기 왕 미션",
        public String mission_app_title;
        @SerializedName("mission_start_de") //20180322",
        public String mission_start_de;
        @SerializedName("mission_end_de") //20180420",
        public String mission_end_de;
        @SerializedName("mission_walk_cnt") //38010",
        public String mission_walk_cnt;
        @SerializedName("mission_walk_at") //N",
        public String mission_walk_at;
        @SerializedName("day_diff") //8"
        public String day_diff;
    }
}
