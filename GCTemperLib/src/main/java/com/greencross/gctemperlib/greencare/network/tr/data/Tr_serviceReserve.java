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
 진료예약, 검진예약(고유키값) 나중에 회원쪽 cmpny_code HB으로 바꿔야 한다 20180313
 */

public class Tr_serviceReserve extends BaseData {
    private final String TAG = Tr_serviceReserve.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String pageNumber;

    }

    public Tr_serviceReserve() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_serviceReserve.RequestData) {
            JSONObject body = new JSONObject();
            Tr_serviceReserve.RequestData data = (Tr_serviceReserve.RequestData) obj;

            body.put("api_code", getApiCode("Tr_serviceReserve") );
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
    @SerializedName("serviceReserve")
    public List<Reserve> serviceReserveList = new ArrayList<>(); //

    public class Reserve {
        @SerializedName("seq")
        public String seq;
        @SerializedName("typ")
        public String typ;
        @SerializedName("hospitalName")
        public String hospitalName;
        @SerializedName("resrv_de") // 예약일
        public String resrv_de;
        @SerializedName("regdate") // 신청일
        public String regdate;
        @SerializedName("contents")
        public String contents;
        @SerializedName("resrv_step") //서비스 완료
        public String resrv_step;
    }

}
