package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 FUNCTION NAME	mber_reg	회원가입

 {
 "api_code": "mber_reg",
 "insures_code": "303",
 "mber_no": "9999999999990",
 "token": "APA91bHanHsaue_chJqab7tKn04XSjGrr4JvyvyrHoB2uZCx9eRY54aCrk14L0MfTx1DhSbgaUYaWGYfoBnPqO7aJSRA-xdU2gEgprAWRxraSI7cDLEVnAqyXnrZpYigAE9OmSNSPnkK9lI0zjNQZhQgwn3uDgpLRYh8mM9uHq1FLOfYhYNhA1E",
 "app_code": "iphone",
 "mber_id": "moondsun2",
 "mber_pwd": "0918",
 "mber_hp": "01039381112",
 "mber_nm": "세현아빠3",
 "mber_sex": "1",
 "mber_lifyea": "19851225",
 "mber_height": "182",
 "mber_bdwgh": "79",
 "mber_bdwgh_goal": "65",
 "pushk": "ET",
 "app_ver": "0.28",
 "phone_model": "SM890",
 "mber_actqy": "1",
 "disease_nm": "1,2,3,",
 "medicine_yn": "Y",
 "smkng_yn": "Y",
 "mber_zone ": "1",
 "sugar_typ": "1",
 "sugar_occur_de": "2015"
 }
 */

public class Tr_mber_reg extends BaseData {
    private final String TAG = Tr_mber_reg.class.getSimpleName();

    public static class RequestData {
        public String mber_id= ""; // tj222honwg@gchealthcare.2com2
        public String mber_pwd= ""; // tes222tpwd
        public String mber_hp= ""; // 010758421333
        public String mber_nm= ""; // 닉네임6
        public String mber_sex= ""; // 1
        public String mber_lifyea= ""; // 19750221
        public String mber_height= ""; // 182
        public String mber_bdwgh= ""; // 79
        public String mber_bdwgh_goal= ""; // 65
        public String pushk= ""; // ET
        public String app_ver= ""; // 0.28
        public String phone_model= ""; // SM890
        public String mber_actqy= ""; // 1
        public String disease_nm= ""; // 1,2,3,
        public String medicine_yn= ""; // Y
        public String smkng_yn= ""; // Y""
        public String mber_zone= ""; // 지역
        public String mber_email = ""; // 이메일
        public String mber_no = "";
    }

    public Tr_mber_reg() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_reg.RequestData) {
            Tr_mber_reg.RequestData data = (Tr_mber_reg.RequestData) obj;
            JSONObject body = new JSONObject();
            body.put("api_code", "mber_reg" ); // "mber_reg",
            body.put("insures_code", INSURES_CODE ); // "300",
            body.put("mber_no", data.mber_no); //9999999987,
            body.put("token", DEVICE_TOKEN ); // "deviceToken",
            body.put("app_code", APP_CODE ); // "iphone",
            body.put("mber_id", data.mber_id ); // "tj222honwg@gchealthcare.2com2",
            body.put("mber_pwd", data.mber_pwd ); // "tes222tpwd",
            body.put("mber_hp", data.mber_hp ); // "010758421333",
            body.put("mber_nm", data.mber_nm ); // "닉네임6",
            body.put("mber_sex", data.mber_sex ); // "1",
            body.put("mber_lifyea", data.mber_lifyea ); // "19750221",
            body.put("mber_height", data.mber_height ); // "182",
            body.put("mber_bdwgh", data.mber_bdwgh ); // "79",
            body.put("mber_bdwgh_goal", data.mber_bdwgh_goal ); // "65",
            body.put("pushk", "ET" ); // "ET",
            body.put("app_ver", data.app_ver ); // "0.28",
            body.put("phone_model", data.phone_model ); // "SM890",
            body.put("mber_actqy", "1" ); // "1",
            body.put("disease_nm", "1" ); // "1,2,3,",
            body.put("medicine_yn", "Y" ); // "Y",
            body.put("smkng_yn", "Y" ); // "Y"
            body.put("mber_zone", "1" );
            body.put("mber_email", data.mber_email );

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
