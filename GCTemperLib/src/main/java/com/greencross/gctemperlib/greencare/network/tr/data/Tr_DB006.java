package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 좋아요 / 취소 DB006
 input 값
 SEQ : 회원일련번호(MBER_SN값)
 CM_SEQ : 커뮤니티 기본키

 output값
 OSEQ : 회원일련번호
 RESULT_CODE : 결과코드

 0000 : 입력성공
 1000 : 삭제성공
 6666 : 회원이 존재하지 않음
 9999 : 기타오류
 */

public class Tr_DB006 extends BaseData {

	public static class RequestData {
		public String SEQ;
		public String CM_SEQ;
	}

	public Tr_DB006() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;

			body.put("SEQ", data.SEQ);
			body.put("CM_SEQ", data.CM_SEQ);
			body.put("DOCNO", "DB006");

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
	@Expose
	@SerializedName("OSEQ")
	public String OSEQ;
	@Expose
	@SerializedName("DOCNO")
	public String DOCNO;
}
