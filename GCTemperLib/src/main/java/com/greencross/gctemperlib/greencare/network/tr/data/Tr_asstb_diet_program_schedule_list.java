package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
다이어트프로그램 스케쥴
 Input 값 api_code : api 코드명 insures_code : 회사코드(108) mber_sn : 기존회원키값
 Output 값 api_code : api 코드명 insures_code : 회원사 코드 sch_day : 스케줄 일자(30일동안) sch_user : 사용자 수 sch_per : 고객만족도 schedule_day : 1일, 2일,3일 schedule_subject : 제목
 data_yn : 저장완료
 */

public class Tr_asstb_diet_program_schedule_list extends BaseData {
    private final String TAG = Tr_asstb_diet_program_schedule_list.class.getSimpleName();

    public static class RequestData {
        public String mber_sn; // 1000
    }

    public Tr_asstb_diet_program_schedule_list() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof RequestData) {
            JSONObject body = new JSONObject();
            RequestData data = (RequestData) obj;
            body.put("api_code", getApiCode("Tr_asstb_diet_program_schedule_list")); // bdsg_info_input_data
            body.put("mber_sn", data.mber_sn); //  1000
            body.put("insures_code", INSURES_CODE);
            return body;
        }

        return super.makeJson(obj);
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/
    @SerializedName("api_code")
    public String api_code; //
    @SerializedName("insures_code")
    public String insures_code; //

    @SerializedName("data_yn")
    public String data_yn;

    @SerializedName("sch_per")
    public String sch_per = "0";
    @SerializedName("sch_user")
    public String sch_user = "0";
    @SerializedName("sch_day")
    public String sch_day = "0";

    @SerializedName("schedule_list")
    public List<Schedule_list> schedule_list;
    public static class Schedule_list {
        @SerializedName("schedule_subject")
        public String schedule_subject;
        @SerializedName("schedule_day")
        public String schedule_day;
    }
}
