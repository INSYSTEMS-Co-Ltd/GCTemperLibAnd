package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 댓글 쓰기 DB004
 Input 값
 SEQ :회원일련번호(MBER_SN값)
 CM_SEQ :커뮤니티 기본키
 T_MBER_SN : 언급 회원 일련번호
 CC_CONTENT :댓글내용

 Output값
 OSEQ : 회원일련번호
 RESULT_CODE : 결과코드

 0000 : 입력성공
 4444 : 이미등록된 댓글
 6666 : 회원이 존재하지 않음
 7777 : 사용중지회원
 9999 : 기타오류
 */

public class Tr_DB004 extends BaseData {


	public static class RequestData {
		public String SEQ;
		public String CM_SEQ;
		public String T_MBER_SN;
		public String CC_CONTENT;
	}

	public Tr_DB004() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();    // 토큰값.

			body.put("SEQ", data.SEQ);
			body.put("T_MBER_SN", data.T_MBER_SN);
			body.put("CC_CONTENT", data.CC_CONTENT);
			body.put("CM_SEQ", data.CM_SEQ);
			body.put("DOCNO", "DB004");

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
