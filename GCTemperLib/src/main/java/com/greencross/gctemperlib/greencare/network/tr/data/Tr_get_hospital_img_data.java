package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * 병원방문 저장 데이터
 json={   "api_code") // get_hospital_img_data",   "insures_code") // 303",  "mber_sn") // 1344"  , "begin_day") // 20180401","end_day") // 20180403" }


 리턴되는 배열중에 최신것 하나만 사용되면 됨.
 */

public class Tr_get_hospital_img_data extends BaseData {
    private final String TAG = Tr_get_hospital_img_data.class.getSimpleName();

    public static class RequestData {
        public String mber_sn;
        public String begin_day;
        public String end_day;
    }

    public Tr_get_hospital_img_data() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        if (obj instanceof Tr_get_hospital_img_data.RequestData) {

            JSONObject body = new JSONObject();
            Tr_get_hospital_img_data.RequestData data = (Tr_get_hospital_img_data.RequestData) obj;
            body.put("api_code", getApiCode("Tr_get_hospital_img_data") ); //
            body.put("insures_code", INSURES_CODE);

            body.put("mber_sn", data.mber_sn);
            body.put("begin_day", data.begin_day);
            body.put("end_day", data.end_day);

            return body;
        }

        return super.makeJson(obj);
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code")
    public String api_code;
    @SerializedName("insures_code")
    public String insures_code;
    @SerializedName("mber_sn")
    public String mber_sn;
    @SerializedName("data_list")
    public List<DataList> data_list = new ArrayList<>();


    public static class DataList {
        @SerializedName("req_sn")
        public String req_sn;
        @SerializedName("idx") // 333",
        public String idx;
        @SerializedName("picture") // .PNG",
        public String picture;
        @SerializedName("hba_value") // ",
        public String hba_value;
        @SerializedName("visit_de") // 20180403",
        public String visit_de;
        @SerializedName("regdate") // 201804021103"
        public String regdate;
    }

}
