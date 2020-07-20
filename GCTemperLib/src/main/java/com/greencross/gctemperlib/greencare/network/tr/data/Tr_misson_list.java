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
 미션  리스트  (전체,방문,혈당,건강,병원,설문 종류에따른 포인트 이력)
 json={   "api_code": "misson_list", "insures_code": "303","mber_sn": "1344" ,"misson_typ": "0" ,"app_code" : "ios", "pageNumber": "1" }

 // <<요청>> // misson_typ : 0 고정
 // 페이징 개발 안해도되 됨. pageNumber:1로 고정값.
 // 항상 4건이 리스트업됨. 만약 3건만 있다면, 로그인하면 해당계정으로 4건을 내려보내줌



 // <<러턴값>>
 // (공통)
 // mission_code : 100-일일, 11-30일걷기, 12-30일혈당, 13-병원방문
 // mission_view_point : 포인트

 // (일일)
 // mission_health_point_amt 현재점수

 // (30일걷기)
 // mission_day_diff 몇일째
 // mission_walk_cnt 누적걸음수

 // (30일혈당)
 // mission_day_diff 몇일째
 // mission_sugar_cnt 측정일수

 // (병원방문)
 // mission_hosvisit_at : N -참여가능, Y 이면 참여 불가능

 */

public class Tr_misson_list extends BaseData {
    private final String TAG = Tr_misson_list.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String misson_typ;
        public String pageNumber;

    }

    public Tr_misson_list() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_misson_list.RequestData) {
            JSONObject body = new JSONObject();
            Tr_misson_list.RequestData data = (Tr_misson_list.RequestData) obj;

            body.put("api_code", getApiCode("Tr_misson_list") );
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);
            body.put("misson_typ", data.misson_typ);
            body.put("pageNumber", data.pageNumber); //  1000

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
    @SerializedName("pageNumber")
    public int pageNumber; //
    @SerializedName("maxpageNumber")
    public int maxpageNumber; //
    @SerializedName("misson_day_list")
    public List<Mission> misson_day_list = new ArrayList<>(); //

    public class Mission {
        @SerializedName("mission_code") // 100",
        public String mission_code;
        @SerializedName("mission_nm") // 일일",
        public String mission_nm;
        @SerializedName("mission_view_point") // 33P",
        public String mission_view_point;
        @SerializedName("mission_start_de") // 20180322",
        public String mission_start_de;
        @SerializedName("mission_end_de") // 20180926",
        public String mission_end_de;
        @SerializedName("mission_app_title") // 병원방문",
        public String mission_app_title;
        @SerializedName("mission_day_diff") // 15",
        public String mission_day_diff;
        @SerializedName("mission_health_point_amt") // 0",
        public String mission_health_point_amt;
        @SerializedName("mission_walk_cnt") // 0",
        public String mission_walk_cnt;
        @SerializedName("mission_walk_at") // N",
        public String mission_walk_at;
        @SerializedName("mission_sugar_cnt") // 0",
        public String mission_sugar_cnt;
        @SerializedName("mission_sugar_at") // N",
        public String mission_sugar_at;
        @SerializedName("mission_hosvisit_cnt") // 0",
        public String mission_hosvisit_cnt;
        @SerializedName("mission_hosvisit_at") // N"
        public String mission_hosvisit_at;
    }

}
