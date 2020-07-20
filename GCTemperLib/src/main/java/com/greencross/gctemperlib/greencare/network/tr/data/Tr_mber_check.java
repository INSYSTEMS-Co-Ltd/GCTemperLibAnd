package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.DeviceUtil;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 FUNCTION NAME	Tr_mber_check	회원가입여부 확인

 Input
 "api_code": "mber_check",
 "app_code": "android",
 "insures_code": "303",
 "mber_nm": "홍태진",
 "mber_lifyea": "19851225",
 "mber_hp": "01085842254",
 "mber_nation": "1",
 "mber_sex": "1",
 "token": "APA91bHCUpphD7XglAYaBx6YXkUwMvVxydBB2pNMSg_z-N13kQ4_1TObbhHt-Aoju6_YqguAQHKQQ2IGxFOgQODYGhkSuBxY7QSQtvm3hm_05lyGl7tnEmTnyQEUBWiF8KRErkoQ3BN8",
 "phone_model": "SM-N910S"


 */

public class Tr_mber_check extends BaseData {
    private final String TAG = Tr_mber_check.class.getSimpleName();

    public static class RequestData {
//        public String api_code;
//        public String app_code;
//        public String insures_code;
        public String mber_nm;
        public String mber_lifyea;
        public String mber_hp;
//        public String mber_nation;
        public String mber_sex;
    }


    public Tr_mber_check() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_check.RequestData) {

            JSONObject body = getBaseJsonObj("mber_check");

            Tr_mber_check.RequestData data = (Tr_mber_check.RequestData) obj;
//            body.put("api_code",  data.api_code);
//            body.put("app_code",  data.app_code);
//            body.put("insures_code",  data.insures_code);
            body.put("mber_nm",  data.mber_nm);
            body.put("mber_lifyea",  data.mber_lifyea);
            body.put("mber_hp",  data.mber_hp);
            body.put("mber_nation",  "1");
            body.put("mber_sex",  data.mber_sex);
            body.put("token",  DEVICE_TOKEN);
            body.put("phone_model",  DeviceUtil.getPhoneModelName());

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
    @SerializedName("mber_no")
    public String mber_no; //
    @SerializedName("mber_id")
    public String mber_id; //
    @SerializedName("data_yn")
    public String data_yn; //

}
