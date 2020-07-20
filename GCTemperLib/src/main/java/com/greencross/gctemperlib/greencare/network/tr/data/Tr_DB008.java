package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 프로필수정 DB008
 Input 값
 SEQ: 회원일련번호(MBER_SN값)
 NICK: 닉네임(5글자이내)
 DISEASE_OPEN: 질환명 공개여부(Y/N)

 Output 값
 OSEQ: 회원일련번호
 ONICK: 닉네임(5글자이내)
 RESULT_CODE: 결과코드

 0000 : 닉네임 수정 성공
 1000 : 닉네임조회 성공
 4444 : 이미 등록된 닉네임
 5555 : 닉네임 수정 실패
 7777 : 사용중지 회원
 9999 : 기타오류

 NICK 이나 DISEASE_OPEN 중 한 항목만 입력하면 입력한 항목만 수정 되도록 수정했습니다.(둘다 NULL값이면 업데이트 되지않습니다.)
 */

public class Tr_DB008 extends BaseData {

	public static class RequestData {
		public String SEQ;
		public String NICK;
		public String DISEASE_OPEN;
	}

	public Tr_DB008() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;
			body.put("SEQ", data.SEQ);
			body.put("NICK", data.NICK);
			body.put("DISEASE_OPEN", data.DISEASE_OPEN);
			body.put("DOCNO", "DB008");

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
	@SerializedName("ODISEASE_OPEN")
	public String ODISEASE_OPEN;
	@Expose
	@SerializedName("ONICK")
	public String ONICK;
	@Expose
	@SerializedName("OSEQ")
	public String OSEQ;
	@Expose
	@SerializedName("DOCNO")
	public String DOCNO;
	@Expose
	@SerializedName("DISEASE_NM")
	public String DISEASE_NM;

}
