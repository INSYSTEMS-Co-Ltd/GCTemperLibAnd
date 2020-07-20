package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * FUNCTION NAME	bdwgh_info_input_data	체중 data 넣는 부분 자동, 수동 값을 넣는다.
 * 빅데이터 40주 그래프
 * <p>
 * <p>
 * Input 값
 * api_code : api 코드명 string
 * insures_code : 회사코드( 108)
 * mber_sn : 회원키값
 * input_week : 입력받은 주수(14주 이상 받아야만 한다)
 * input_kg : 현재 몸무게 입력값
 * <p>
 * Output 값
 * api_code : api 코드명 string
 * insures_code : 회원사 코드
 * mother_period_start_de : 회원키값
 * mother_period_end_de :
 * m_week : 1~40주
 * bmi_min : 그래프 최소값
 * weight : 예상 체중 몸무게 값
 * bmi_max : 그래프 최대값
 * db_bmi : bmi 값
 * db_msg_01 : 코멘트 첫번째 문구
 * db_msg_02 : 코멘트 두번째 문구
 * db_msg_03 : 코멘트 세번째 문구
 */

public class Tr_asstb_weight_hope_grp extends BaseData {
    private final String TAG = Tr_asstb_weight_hope_grp.class.getSimpleName();

    public static class RequestData {
        public String mber_sn;
        public String input_kg;
        public String input_week;
    }

    public Tr_asstb_weight_hope_grp() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof RequestData) {
            JSONObject body = new JSONObject();

            RequestData data = (RequestData) obj;
            body.put("api_code", "asstb_weight_hope_grp");
            body.put("insures_code", INSURES_CODE); // 300
            body.put("mber_sn", data.mber_sn); //  1000
            body.put("input_week", data.input_week); //
            body.put("input_kg", data.input_kg); //
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
    @SerializedName("db_msg_03")
    public String db_msg_03;
    @SerializedName("db_msg_02")
    public String db_msg_02;
    @SerializedName("db_msg_01")
    public String db_msg_01;
    @SerializedName("db_bmi")
    public String db_bmi;
    @SerializedName("grp_list")
    public List<Grp_list> grp_list;

    public static class Grp_list {
        @SerializedName("bmi_max")
        public String bmi_max;
        @SerializedName("weight")
        public String weight;
        @SerializedName("bmi_min")
        public String bmi_min;
        @SerializedName("m_week")
        public String m_week;
    }

}
