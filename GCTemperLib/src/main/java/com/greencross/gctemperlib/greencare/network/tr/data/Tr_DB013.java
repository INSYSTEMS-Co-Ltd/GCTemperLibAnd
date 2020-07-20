package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 회원검색 DB013
 * Input 값
 * NICK : 닉네임(5글자이내) (LIKE 검색)
 * <p>
 * Output 값
 * DATA_LENGTH : DATA 배열의 원소 개수
 * DATA : 배열
 * MBER_SN : 검색 고객 회원일련번호
 * NICKNAME : 닉네임(5글자이내)
 * RESULT_CODE : 결과코드
 * <p>
 * 0000 : 성공
 * 9999 : 기타오류
 */

public class Tr_DB013 extends BaseData {


	public static class RequestData {
		public String NICK;
	}

	public Tr_DB013() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;

			body.put("NICK", data.NICK);
			body.put("DOCNO", "DB013");

			return body;
		}

		return super.makeJson(obj);
	}

	/**************************************************************************************************/
	/***********************************************RECEIVE********************************************/
	/**************************************************************************************************/


	@Expose
	@SerializedName("RESULT_CODE")
	public String RESULT_CODE;
//	@Expose
//	@SerializedName("DATA")
//	public List<CommunityUserData> DATA;
	@Expose
	@SerializedName("DATA_LENGTH")
	public String DATA_LENGTH;
	@Expose
	@SerializedName("DOCNO")
	public String DOCNO;


}
