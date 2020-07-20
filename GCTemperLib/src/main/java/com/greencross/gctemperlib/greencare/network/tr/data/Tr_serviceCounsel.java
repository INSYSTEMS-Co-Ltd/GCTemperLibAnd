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

public class Tr_serviceCounsel extends BaseData {
    private final String TAG = Tr_serviceCounsel.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String pageNumber;

    }

    public Tr_serviceCounsel() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_serviceCounsel.RequestData) {
            JSONObject body = new JSONObject();
            Tr_serviceCounsel.RequestData data = (Tr_serviceCounsel.RequestData) obj;

            body.put("api_code", getApiCode("Tr_serviceCounsel") );
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
    @SerializedName("serviceCounsel")
    public List<ServiceCounsel> serviceCounselList = new ArrayList<>(); //

    public class ServiceCounsel {
        @SerializedName("seq")
        public String seq;
        @SerializedName("title")
        public String title;
        @SerializedName("time")
        public String time;
        @SerializedName("type")
        public String type;
        @SerializedName("typ")
        public String typ;
    }

}
