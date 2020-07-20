package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 <활동(걷기)>
 목표걸음수, 목표칼로리 받아오기

 {"api_code": "mvm_info_plan_data", "insures_code": "108",  "mber_sn": "21182" }

 <리턴값>
 goal_step  : 목표 걸음수
 goal_cal : 목표 칼로리
 comment1 : 걸음수 그래프 하단의 커멘트
 */

public class Tr_mvm_info_plan_data extends BaseData {
    private final String TAG = Tr_mvm_info_plan_data.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;

    }
    public Tr_mvm_info_plan_data() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mvm_info_plan_data.RequestData) {
            JSONObject body = new JSONObject();
            Tr_mvm_info_plan_data.RequestData data = (Tr_mvm_info_plan_data.RequestData) obj;

            body.put("api_code", getApiCode("Tr_mvm_info_plan_data") );
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);

            return body;
        }

        return super.makeJson(obj);
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/
    @SerializedName("goal_step")
    public String goal_step;//  : 목표 걸음수
    @SerializedName("goal_cal")
    public String goal_cal ;// 목표 칼로리
    @SerializedName("comment1")
    public String comment1 ;// 걸음수 그래프 하단의 커멘트

}
