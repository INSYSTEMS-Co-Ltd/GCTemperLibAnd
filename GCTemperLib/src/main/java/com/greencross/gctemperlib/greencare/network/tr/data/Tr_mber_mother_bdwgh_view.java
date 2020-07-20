package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 FUNCTION NAME	mber_reg	회원가입

 "api_code":"mber_mother_bdwgh_view","insures_code":"108","mber_sn":"20914"


 * response
 "birth_chl_yn": "N",
 "input_de": "20190304",
 "now_weight": "85",
 "kg_kind": "초과",
 "mn_weight": "80",
 "mm_weight": "82",
 "comment1": "현재 적정체중 범위에서 3kg 초과 합니다. ",
 "comment2": "태아의 성장을 돕기 위해서 임신부의 신체가 자연스럽게 변화
 "bmi": "27.55",
 "bmi_kind": "비만군",
 "bmi_skg": "",
 "bmi_ekg": "",
 "mber_bdwgh_goal": "50",
 "mber_term_kg": "75",
 "api_code": "mber_mother_bdwgh_view",
 "insures_code": "108",
 "mber_sn": "20914",
 "data_yn": "Y"
 */

public class Tr_mber_mother_bdwgh_view extends BaseData {
    private final String TAG = Tr_mber_mother_bdwgh_view.class.getSimpleName();

    public static class RequestData {
        public String mber_sn; // 1000",
    }

    public Tr_mber_mother_bdwgh_view() throws JSONException {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_mother_bdwgh_view.RequestData) {
            Tr_mber_mother_bdwgh_view.RequestData data = (Tr_mber_mother_bdwgh_view.RequestData) obj;
            JSONObject body = new JSONObject();
            body.put("api_code", "mber_mother_bdwgh_view" ); // "mber_reg",
            body.put("insures_code", INSURES_CODE ); // "300",
            body.put("mber_sn", data.mber_sn); //9999999987,

            return body;
        }

        return super.makeJson(obj);
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("birth_chl_yn")
    public String birth_chl_yn;
    @SerializedName("input_de")
    public String input_de;
    @SerializedName("now_weight")
    public String now_weight;
    @SerializedName("kg_kind")
    public String kg_kind;
    @SerializedName("mn_weight")
    public String mn_weight;
    @SerializedName("mm_weight")
    public String mm_weight;
    @SerializedName("comment1")
    public String comment1;
    @SerializedName("comment1_kg")
    public String comment1_kg;
    @SerializedName("comment2")
    public String comment2;
    @SerializedName("bmi")
    public String bmi;
    @SerializedName("bmi_kind")
    public String bmi_kind;
    @SerializedName("bmi_skg")
    public String bmi_skg;
    @SerializedName("bmi_ekg")
    public String bmi_ekg;
    @SerializedName("mber_bdwgh_goal")
    public String mber_bdwgh_goal;
    @SerializedName("mber_term_kg")
    public String mber_term_kg;
    @SerializedName("api_code")
    public String api_code;
    @SerializedName("insures_code")
    public String insures_code;
    @SerializedName("mber_sn")
    public String mber_sn;
    @SerializedName("data_yn")
    public String data_yn;

    @SerializedName("mother_period_week")
    public String mother_period_week;
    @SerializedName("mother_period_day")
    public String mother_period_day;
    @SerializedName("mother_week")
    public String mother_week;
    @SerializedName("mother_14_week")
    public String mother_14_week;
    @SerializedName("mother_26_week")
    public String mother_26_week;
    @SerializedName("mother_40_week")
    public String mother_40_week;
    @SerializedName("mother_all_week")
    public String mother_all_week;


}
