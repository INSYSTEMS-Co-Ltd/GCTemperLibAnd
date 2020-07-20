package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


public class Tr_hra_check_result_input extends BaseData {
private final String TAG = Tr_hra_check_result_input.class.getSimpleName();

public static class RequestData {
    public String insures_code; // 1000
    public String mber_sn; // 1011
    public String total_score; // 99
    public String moon_key; // 1 ~ 10

}


    public Tr_hra_check_result_input() {
        super.conn_url = BaseUrl.COMMON_URL;
    }
/*public Tr_hra_check_result_input() throws JSONException {
    super.conn_url = "http://m.shealthcare.co.kr/HWG/WebService/HWG_MOBILE_CALL.asmx/HWG_mobile_Call";
}*/

@Override
public JSONObject makeJson(Object obj) throws JSONException {
    if (obj instanceof RequestData) {

        JSONObject body = new JSONObject();

        RequestData data = (RequestData) obj;
        body.put("api_code", "hra_check_result_input");
        body.put("insures_code", INSURES_CODE);
        body.put("mber_sn", data.mber_sn ); // 1000",
        body.put("total_score", data.total_score); // 99
        body.put("moon_key", data.moon_key ); // 1~ 10

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
@SerializedName("d_level")
public String d_level; //
@SerializedName("comment")
public String comment; //
@SerializedName("reg_yn")
public String reg_yn; //

}
