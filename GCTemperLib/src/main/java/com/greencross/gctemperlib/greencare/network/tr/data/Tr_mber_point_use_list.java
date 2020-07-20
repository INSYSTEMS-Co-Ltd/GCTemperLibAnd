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
 포이트 내역 리스트
 내정보에서 푸시여부설정

 //페이징 되어야 함.

 <요청값> start_de:시작일 end_de:종료일
 */

public class Tr_mber_point_use_list extends BaseData {
    private final String TAG = Tr_mber_point_use_list.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String start_de;
        public String end_de;
        public String pageNumber;

    }

    public Tr_mber_point_use_list() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_point_use_list.RequestData) {
            JSONObject body = new JSONObject();
            Tr_mber_point_use_list.RequestData data = (Tr_mber_point_use_list.RequestData) obj;

            body.put("api_code", getApiCode("Tr_mber_point_use_list") );
            body.put("insures_code", INSURES_CODE);

            body.put("mber_sn", data.mber_sn);
            body.put("start_de", data.start_de);
            body.put("end_de", data.end_de);
            body.put("pageNumber", data.pageNumber);

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
    @SerializedName("pageNumber")
    public String pageNumber; //
    @SerializedName("maxpageNumber")
    public String maxpageNumber; //
    @SerializedName("point_usr_amt")
    public String point_usr_amt = "0"; //
    @SerializedName("point_user_sum_amt")
    public String point_user_sum_amt = "0"; //

    @SerializedName("point_day_list")
    public List<pointDay> point_day_list = new ArrayList<>();

    public class pointDay {
        @SerializedName("point_code")
        public String point_code;
        @SerializedName("point_txt")
        public String point_txt;
        @SerializedName("accml_amt")
        public String accml_amt;
        @SerializedName("accml_sum_amt")
        public String accml_sum_amt;
        @SerializedName("remain_point_amt")
        public String remain_point_amt;
        @SerializedName("input_de")
        public String input_de;

    }
}
