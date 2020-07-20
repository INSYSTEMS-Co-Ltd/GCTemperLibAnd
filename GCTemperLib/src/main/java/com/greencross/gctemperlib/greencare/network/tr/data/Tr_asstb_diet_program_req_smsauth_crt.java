package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 다이어트 프로그램 sms 인증

 Input 값 api_code : api 코드명 insures_code : 회사코드(108) mber_sn : 기존회원키값, mber_hp : 휴대폰번호 auth_typ : 1. 다이어트 2. 유의질환
 Output 값 api_code : api 코드명 insures_code : 회원사 코드 reg_yn : 저장완료 sms_auth_num : 인증번호
 */

public class Tr_asstb_diet_program_req_smsauth_crt extends BaseData {
	private final String TAG = Tr_asstb_diet_program_req_smsauth_crt.class.getSimpleName();


	public static class RequestData {
		public String mber_sn;
		public String mber_hp;
		public String auth_typ;
	}

	public Tr_asstb_diet_program_req_smsauth_crt() throws JSONException {
		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = new JSONObject();//getBaseJsonObj("login_id");
			RequestData data = (RequestData) obj;

			body.put("api_code", "asstb_diet_program_req_smsauth_crt");
			body.put("insures_code", INSURES_CODE);
			body.put("mber_sn", data.mber_sn);
			body.put("mber_hp",data.mber_hp);
			body.put("auth_typ",data.auth_typ);

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
	@SerializedName("mber_sn")
	public String mber_sn;
	@SerializedName("sms_auth_num")
	public String sms_auth_num;
    @SerializedName("reg_yn")  // 저장 여부
    public String reg_yn;

}
