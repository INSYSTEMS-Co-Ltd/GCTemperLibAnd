package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 게시글삭제 DB011
 input값
 SEQ : 회원일련번호(MBER_SN값)
 CM_SEQ : 커뮤니티 기본키

 output값
 OSEQ : 회원일련번호
 RESULT_CODE : 결과코드

 0000 : 삭제성공
 4444 : 삭제 실패(해당글이 없음)
 6666 : 회원이 존재하지 않음
 7777 : 사용중지 회원
 9999 : 기타오류
 */

public class Tr_DB011 extends BaseData {

	public static class RequestData {
		public String SEQ;
		public String CM_SEQ;
	}

	public Tr_DB011() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;

			body.put("CM_SEQ", data.CM_SEQ);
			body.put("SEQ", data.SEQ);
			body.put("DOCNO", "DB011");

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
