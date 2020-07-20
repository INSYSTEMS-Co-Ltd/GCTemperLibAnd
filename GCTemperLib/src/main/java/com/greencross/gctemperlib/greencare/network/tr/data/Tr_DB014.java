package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 알림리스트 DB014
 * Input 값
 * SEQ : 회원일련번호
 * <p>
 * Output 값
 * DATA_LENGTH : DATA 배열의 원소 개수
 * DATA : 배열
 * CM_SEQ : 커뮤니티 일련번호
 * MSG : 알림 메시지 내용
 * RESULT_CODE : 결과코드
 * <p>
 * 0000 : 성공
 * 9999 : 기타오류
 */

public class Tr_DB014 extends BaseData {

	public static class RequestData {
		public String SEQ;
	}

	public Tr_DB014() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;

			body.put("SEQ", data.SEQ);
			body.put("DOCNO", "DB014");

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
//	public List<CommunityNoticeData> DATA;
	@Expose
	@SerializedName("DATA_LENGTH")
	public String DATA_LENGTH;
	@Expose
	@SerializedName("DOCNO")
	public String DOCNO;



}
