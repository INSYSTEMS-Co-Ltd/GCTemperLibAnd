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
 */

public class Tr_hospitalList_gene_result extends BaseData {
    private final String TAG = Tr_hospitalList_gene_result.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;

    }

    public Tr_hospitalList_gene_result() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_hospitalList_gene_result.RequestData) {
            JSONObject body = new JSONObject();
            Tr_hospitalList_gene_result.RequestData data = (Tr_hospitalList_gene_result.RequestData) obj;

            body.put("api_code", getApiCode("Tr_hospitalList_gene_result") );
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
    public int maxpageNumber; //
    @SerializedName("gene_result")
    public List<Gene> gene_result_list = new ArrayList<>(); //

    class Gene {
        @SerializedName("SLC47A2") // 1",
        public String SLC47A2;
        @SerializedName("CYP2C9") // 2",
        public String CYP2C9;
        @SerializedName("TCF7L2") // 3",
        public String TCF7L2;
        @SerializedName("COQ2") // 1",
        public String COQ2;
        @SerializedName("CACNA1C") // 2",
        public String CACNA1C;
        @SerializedName("AGTR1") // 3",
        public String AGTR1;
        @SerializedName("ADRB1") // 1",
        public String ADRB1;
        @SerializedName("ACE") // 2",
        public String ACE;
        @SerializedName("NEDD4L") // 3",
        public String NEDD4L;
        @SerializedName("CYP2C19") // 1"
        public String CYP2C19;

    }

}
