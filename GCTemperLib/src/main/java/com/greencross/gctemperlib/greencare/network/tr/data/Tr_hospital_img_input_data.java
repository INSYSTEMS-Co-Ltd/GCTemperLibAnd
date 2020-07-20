package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * 병원방문 저장 데이터
 <요청값> hba_value : 당화혈색소 수치 visit_de : 병원 방문일
 json={   "api_code": "hospital_img_input_data",   "insures_code": "303",  "mber_sn": "1344","idx": "777737788883"  , "hba_value": "80","visit_de": "20180413" ,"input_de": "20180403"}
 <결과값> req_sn : 이미지바이너리 파일 업로드시 사용되어야 함.

 */

public class Tr_hospital_img_input_data extends BaseData {
    private final String TAG = Tr_hospital_img_input_data.class.getSimpleName();

    public static class RequestData {
        public String mber_sn;
        public String idx;
        public String hba_value;
        public String visit_de;
        public String input_de;
    }

    public Tr_hospital_img_input_data() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        if (obj instanceof Tr_hospital_img_input_data.RequestData) {

            JSONObject body = new JSONObject();
            Tr_hospital_img_input_data.RequestData data = (Tr_hospital_img_input_data.RequestData) obj;
            body.put("api_code", getApiCode("Tr_hospital_img_input_data") ); //
            body.put("insures_code", INSURES_CODE);

            body.put("mber_sn", data.mber_sn);
            body.put("idx", data.idx);
            body.put("hba_value", data.hba_value);
            body.put("visit_de", data.visit_de);
            body.put("input_de", data.input_de);

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
    @SerializedName("req_sn")
    public String req_sn;
    @SerializedName("reg_yn")
    public String reg_yn;

}
