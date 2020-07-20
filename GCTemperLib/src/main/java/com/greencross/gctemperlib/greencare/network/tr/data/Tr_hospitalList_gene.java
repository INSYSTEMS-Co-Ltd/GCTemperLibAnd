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
 유전자 병원 리스트
 */

public class Tr_hospitalList_gene extends BaseData {
    private final String TAG = Tr_hospitalList_gene.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String area_code;

    }

    public Tr_hospitalList_gene() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_hospitalList_gene.RequestData) {
            JSONObject body = new JSONObject();
            Tr_hospitalList_gene.RequestData data = (Tr_hospitalList_gene.RequestData) obj;

            body.put("api_code", getApiCode("Tr_hospitalList_gene") );
            body.put("area_code", data.area_code); // 지역코드
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);

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
    @SerializedName("maxpageNumber")
    public String maxpageNumber; //
    @SerializedName("hospitalList")
    public List<hospitalList> hospitalList = new ArrayList<>();//

    public class hospitalList {
        @SerializedName("hospital_areaCode") // 1",
        public String hospital_areaCode;
        @SerializedName("hospital_name") // 서울성모병원",
        public String hospital_name;
        @SerializedName("hospital_special") // "",
        public String hospital_special;
        @SerializedName("hospital_code") // 1115",
        public String hospital_code;
        @SerializedName("hpt_code") // SO04",
        public String hpt_code;
    }

}
