package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * 개인정보수정하기
 json={   "api_code": "mber_edit_add_exe",   "insures_code": "303", "token": "deviceToken",  "mber_sn": "1406" ,"mber_sex": "2","mber_lifyea": "19750221","mber_height": "182","mber_bdwgh": "79" ,"mber_bdwgh_goal": "65"   ,"mber_actqy": "1",  "disease_nm": "1",  "medicine_yn": "Y",  "smkng_yn": "Y",  "sugar_typ": "1", "sugar_occur_de": "2018" }

 고정값 : ,"mber_actqy": "1", "disease_nm": "1", "medicine_yn": "Y", "smkng_yn": "Y"
 */

public class Tr_mber_edit_add_exe extends BaseData {
    private final String TAG = Tr_mber_edit_add_exe.class.getSimpleName();

    public static class RequestData {
        public String mber_sn; // 1000
        public String mber_sex; // 1
        public String mber_lifyea; // 19750221
        public String mber_height; // 182
        public String mber_bdwgh; // 79
        public String mber_bdwgh_goal; // 65

        public String sugar_typ;
        public String sugar_occur_de;

    }

    public Tr_mber_edit_add_exe() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_edit_add_exe.RequestData) {

            JSONObject body = getBaseJsonObj("mber_edit_add_exe");

            Tr_mber_edit_add_exe.RequestData data = (Tr_mber_edit_add_exe.RequestData) obj;
            body.put("mber_sn", data.mber_sn);
            body.put("mber_sex", data.mber_sex);
            body.put("mber_lifyea", data.mber_lifyea);
            body.put("mber_bdwgh_goal", data.mber_bdwgh_goal);

            body.put("mber_height", data.mber_height);
            body.put("mber_bdwgh", data.mber_bdwgh);
            body.put("sugar_typ", data.sugar_typ);
            body.put("sugar_occur_de", data.sugar_occur_de);

//고정값 : ,"mber_actqy": "1", "disease_nm": "1", "medicine_yn": "Y", "smkng_yn": "Y"
            body.put("mber_actqy", "1");
            body.put("disease_nm", "1");
            body.put("medicine_yn", "Y");
            body.put("smkng_yn", "Y");
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
