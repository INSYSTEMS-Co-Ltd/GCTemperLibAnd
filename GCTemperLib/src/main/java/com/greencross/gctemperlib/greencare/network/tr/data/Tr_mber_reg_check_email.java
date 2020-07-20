package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 // 이메일 중복체크 (o)

 json={   "api_code": "mber_reg_check_email",   "insures_code": "303", "token": "deviceToken",  "mber_email": "tjhong@gchealthcare.com"}


 */

public class Tr_mber_reg_check_email extends BaseData {
    private final String TAG = Tr_mber_reg_check_email.class.getSimpleName();

    public static class RequestData {

        public String mber_email;

    }

    public Tr_mber_reg_check_email() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_reg_check_email.RequestData) {
            JSONObject body = new JSONObject();
            Tr_mber_reg_check_email.RequestData data = (Tr_mber_reg_check_email.RequestData) obj;

            body.put("api_code", getApiCode("Tr_mber_reg_check_email") ); //
            body.put("insures_code", INSURES_CODE);
            body.put("token", DEVICE_TOKEN);
            body.put("mber_email", data.mber_email);

            return body;
        }

        return super.makeJson(obj);
    }

    public JSONArray getArray(Tr_get_hedctdata.DataList dataList) {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            obj.put("idx" , dataList.idx );
            array.put(obj);
        } catch (JSONException e) {
            Logger.e(e);
        }

        return array;
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code")
    public String api_code; //
    @SerializedName("insures_code")
    public String insures_code; //
    @SerializedName("mber_email")
    public String mber_email; //
    @SerializedName("mber_email_yn")
    public String mber_email_yn; //

}
