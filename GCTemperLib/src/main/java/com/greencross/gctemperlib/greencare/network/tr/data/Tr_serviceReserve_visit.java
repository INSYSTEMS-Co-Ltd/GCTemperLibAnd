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
 서비스 신청현황 ( 상담내역 일반상담, 건강상담 )
 */

public class Tr_serviceReserve_visit extends BaseData {
    private final String TAG = Tr_serviceReserve_visit.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String pageNumber;

    }

    public Tr_serviceReserve_visit() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_serviceReserve_visit.RequestData) {
            JSONObject body = new JSONObject();
            Tr_serviceReserve_visit.RequestData data = (Tr_serviceReserve_visit.RequestData) obj;

            body.put("api_code", getApiCode("Tr_serviceReserve_visit") );
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);
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
    public String pageNumber; //
    @SerializedName("maxpageNumber")
    public String maxpageNumber; //
    @SerializedName("serviceReserve_visit")
    public List<serviceReserve_visit> serviceReserveVisitList = new ArrayList<>(); //

    public class serviceReserve_visit {
        @SerializedName("regdate") //신청일
        public String regdate;
        @SerializedName("visit_day") //방문일
        public String visit_day;
        @SerializedName("visit_count") //방문 수
        public String visit_count;
        @SerializedName("hospital") //병원 명
        public String hospital;
        @SerializedName("branch") //혈액종양내과
        public String branch;
        @SerializedName("jindan") //암/폐암
        public String jindan;
        @SerializedName("charge") //이름
        public String charge;
    }

}
