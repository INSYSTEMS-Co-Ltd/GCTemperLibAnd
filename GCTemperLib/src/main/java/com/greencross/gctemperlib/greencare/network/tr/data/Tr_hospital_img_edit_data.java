package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * 병원방문  데이터 수정
 <요청값> req_sn: 받은값 hba_value: 받은값 visit_de : 받은값 등의 값을 던짐

 이미지도 변경이 필요하다면? 2.병원방문 이미지 저장 API를 호출하여 이미지도 업로드
 {   "api_code": "hospital_img_edit_data", "insures_code": "303",  "mber_sn": "1344"  , "hba_value": "180","visit_de": "20180422" ,"req_sn": "3"}
 */

public class Tr_hospital_img_edit_data extends BaseData {
    private final String TAG = Tr_hospital_img_edit_data.class.getSimpleName();

    public static class RequestData {
        public String mber_sn;
        public String hba_value;
        public String visit_de;
        public String req_sn;
    }

    public Tr_hospital_img_edit_data() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        if (obj instanceof Tr_hospital_img_edit_data.RequestData) {
            JSONObject body = new JSONObject();
            Tr_hospital_img_edit_data.RequestData data = (Tr_hospital_img_edit_data.RequestData) obj;
            body.put("api_code", getApiCode("Tr_hospital_img_edit_data") ); //
            body.put("insures_code", INSURES_CODE);

            body.put("mber_sn", data.mber_sn);
            body.put("hba_value", data.hba_value);
            body.put("visit_de", data.visit_de);
            body.put("req_sn", data.req_sn);

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
    @SerializedName("reg_yn")
    public String reg_yn;

}
