package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 40주 누적그래프
 * '
 * Input 값
 * api_code : api 코드명 string
 * insures_code : 회사코드( 108)
 * mber_sn : 회원키값
 * mber_birth_due_de : 출산예정일
 * <p>
 * Output 값
 * api_code : api 코드명 string
 * insures_code : 회원사 코드
 * mother_period_start_de : 회원키값
 * mother_period_end_de :
 * m_week : 1~40주
 * m_week_weight : 해당 주에 측정 몸무게
 * m_reg_de : 측정 날짜.
 * last_m_week_weigh : 최근 체중
 * last_m_week : 최근 주차
 * last_m_reg_de : 최근 측정일
 */

public class Tr_asstb_forty_total_grp extends BaseData {
    private final String TAG = Tr_asstb_forty_total_grp.class.getSimpleName();

    public static class RequestData {
        public String mber_sn;
        public String mber_birth_due_de;
    }

    public Tr_asstb_forty_total_grp() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof RequestData) {
            JSONObject body = new JSONObject();

            RequestData data = (RequestData) obj;
            body.put("api_code", "asstb_forty_total_grp");
            body.put("insures_code", INSURES_CODE); // 300
            body.put("mber_sn", data.mber_sn); //  1000
            body.put("mber_birth_due_de", data.mber_birth_due_de); //
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

    @SerializedName("data_yn")
    public String data_yn;
    @SerializedName("last_m_reg_de")
    public String last_m_reg_de;
    @SerializedName("last_m_week")
    public String last_m_week;
    @SerializedName("last_m_week_weight")
    public String last_m_week_weight;
    @SerializedName("grp_list")
    public List<Grp_list> grp_list;
    @SerializedName("mother_period_end_de")
    public String mother_period_end_de;
    @SerializedName("mother_period_start_de")
    public String mother_period_start_de;


    public static class Grp_list {
        @SerializedName("m_reg_de")
        public String m_reg_de;
        @SerializedName("m_week_weight")
        public String m_week_weight;
        @SerializedName("m_week")
        public String m_week;
    }
}
