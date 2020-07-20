package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 비밀번호 변경
 */
public class Tr_mber_pwd_edit_exe extends BaseData {
    private final String TAG = Tr_mber_pwd_edit_exe.class.getSimpleName();

    public static class RequestData {
        public String mber_sn;
        public String mber_id;
        public String bef_mber_pwd;
        public String aft_mber_pwd;
    }

    public Tr_mber_pwd_edit_exe() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_pwd_edit_exe.RequestData) {
            JSONObject body = new JSONObject();
            Tr_mber_pwd_edit_exe.RequestData data = (Tr_mber_pwd_edit_exe.RequestData) obj;

            body.put("api_code", getApiCode("Tr_mber_pwd_edit_exe") ); //
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn); //  1000
            body.put("mber_id", data.mber_id); //  1000
            body.put("bef_mber_pwd", data.bef_mber_pwd); //  1000
            body.put("aft_mber_pwd", data.aft_mber_pwd); //  1000
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
    @SerializedName("reg_yn")
    public String reg_yn; //

}
