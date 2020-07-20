package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 FUNCTION NAME	login_add_aft	회원가입 추가정보

 Input
 변수명	FUNCTION NAME 	설명
 api_code	 	api 코드명 string
 insures_code	 	회원사 코드
 mber_sn           회원정보
 mber_height       키
 mber_bdwgh        체중
 sugar_typ         당뇨유형
 sugar_occur_de    당뇨발생일
 mber_bdwgh_goal   목표체중

 json={   "api_code": "login_add_aft",   "insures_code": "303",    "mber_sn": "1344"  ,"mber_height": "182","mber_bdwgh": "79"  , "sugar_typ": "1", "sugar_occur_de": "2015","mber_bdwgh_goal" : "" }
 */

public class Tr_login_add_aft extends BaseData {
	private final String TAG = Tr_login_add_aft.class.getSimpleName();


	public static class RequestData {
		public String mber_sn;
		public String mber_height;
		public String mber_bdwgh;
		public String sugar_typ;
		public String sugar_occur_de;
		public String mber_bdwgh_goal;
	}

	public Tr_login_add_aft() throws JSONException {
		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = new JSONObject();
			RequestData data = (RequestData) obj;

			body.put("api_code", "login_add_aft");
			body.put("insures_code", INSURES_CODE);
			body.put("mber_sn", data.mber_sn);
			body.put("mber_height", data.mber_height);
			body.put("mber_bdwgh", data.mber_bdwgh);
			body.put("sugar_typ", data.sugar_typ);
			body.put("sugar_occur_de", data.sugar_occur_de);
			body.put("mber_bdwgh_goal", data.mber_bdwgh_goal);

			return body;
		}

		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code")        //		api 코드명 string
	public String api_code;        //		api 코드명 string
	@SerializedName("insures_code")//		회원사 코드
	public String insures_code;    //		회원사 코드
	@SerializedName("mber_sn")    // 회원정보
	public String mber_sn;
	@SerializedName("point_alert_yn") // 최초 포인트 지급여부(Y 지급 , N 지급함)
	public String point_alert_yn;
	@SerializedName("point_alert_amt") // 지급 포인트
	public String point_alert_amt;
	@SerializedName("reg_yn") // 성공여부
	public String reg_yn;
}
