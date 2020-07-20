package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 FUNCTION NAME	mber_alert_yn	알람설정

 Input
 변수명	FUNCTION NAME 	설명

 "api_code": "mber_alert_yn",   "insures_code": "303", "mber_sn": "1344" ,"sugar_alert_yn": "N"  ,"health_alert_yn": "N" ,"mission_alert_yn": "Y"


 json = @"{   ""api_code"") // "mber_user_call"",   ""insures_code"") // "300"", ""mber_sn"") // "1000""  }";
 Output
 변수명		설명
 "api_code": "mber_alert_yn",
 "insures_code": "303",
 "mber_sn": "1344",
 "sugar_alert_yn": "N",
 "health_alert_yn": "N",
 "mission_alert_yn": "Y",
 "data_yn": "Y"

 */

public class Tr_mber_alert_yn extends BaseData {
	private final String TAG = Tr_mber_alert_yn.class.getSimpleName();


	public static class RequestData {
        public String mber_sn; // 1000""
        public String sugar_alert_yn;
        public String health_alert_yn;
        public String mission_alert_yn;
	}

	public Tr_mber_alert_yn() throws JSONException {
		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
            JSONObject body = new JSONObject();
            Tr_mber_alert_yn.RequestData data = (Tr_mber_alert_yn.RequestData) obj;

            body.put("api_code", getApiCode("Tr_mber_alert_yn") );
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);
            body.put("sugar_alert_yn", data.sugar_alert_yn);
            body.put("health_alert_yn", data.health_alert_yn);
            body.put("mission_alert_yn", data.mission_alert_yn);

			return body;
		}

		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

        @SerializedName("api_code") // mber_user_call",
        public String api_code; // mber_user_call",
        @SerializedName("insures_code") // 300",
        public String insures_code; // 300",
    	@SerializedName("mber_sn") // ,
        public String mber_sn;
        @SerializedName("sugar_alert_yn") // Y,
        public String sugar_alert_yn;
        @SerializedName("health_alert_yn") // Y,
        public String health_alert_yn;
        @SerializedName("mission_alert_yn") // Y,
        public String mission_alert_yn;
        @SerializedName("data_yn") // Y"
        public String data_yn;

//    @SerializedName("api_code") // mber_user_call",
//    public String api_code; // mber_user_call",
//    @SerializedName("insures_code") // 300",
//    public String insures_code; // 300",
//    @SerializedName("mber_nm") // jstjtskgxj",
//    public String mber_nm; // jstjtskgxj",
//    @SerializedName("mber_hp") // 01033333333",
//    public String mber_hp; // 01033333333",
//    @SerializedName("mber_id") // cc@cc.com",
//    public String mber_id; // cc@cc.com",
//    @SerializedName("mber_lifyea") // 970821",
//    public String mber_lifyea; // 970821",
//    @SerializedName("mber_sex") // 2",
//    public String mber_sex; // 2",
//    @SerializedName("mber_height") // 170",
//    public String mber_height; // 170",
//    @SerializedName("mber_bdwgh") // 55.55",
//    public String mber_bdwgh; // 55.55",
//    @SerializedName("mber_bdwgh_goal") // 44.44",
//    public String mber_bdwgh_goal; // 44.44",
//    @SerializedName("mber_actqy") // 2",
//    public String mber_actqy; // 2",
//    @SerializedName("disease_nm") // 2,4,",
//    public String disease_nm; // 2,4,",
//    @SerializedName("medicine_yn") // Y",
//    public String medicine_yn; // Y",
//    @SerializedName("smkng_yn") // N",
//    public String smkng_yn; // N",
//    @SerializedName("mber_zone") // Y"
//    public String mber_zone; // Y"
//    @SerializedName("data_yn") // Y"
//    public String data_yn; // Y"

}
