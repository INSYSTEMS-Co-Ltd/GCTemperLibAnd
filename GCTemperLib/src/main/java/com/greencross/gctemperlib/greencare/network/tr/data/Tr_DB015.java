package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * 태그목록 DB015
 * Input 값
 * DOCNO : 전문 키
 * <p>
 * Output 값
 * 배열의 길이 : DATA 배열의 원소 개수
 * 배열 : DATA
 * TAG_SEQ : 태그 일련번호
 * TAG_WORD : 태그명
 * REGDATE : 태그 등록일
 * RESULT_CODE : 결과코드
 * <p>
 * 0000 : 성공
 * 9999 : 기타오류
 */

public class Tr_DB015 extends BaseData {


	public static class RequestData {
		public String DOCNO;
	}

	public Tr_DB015() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;
			body.put("DOCNO", data.DOCNO);
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
	@SerializedName("DATA_LENGTH")
	public String DATA_LENGTH;
	@Expose
	@SerializedName("DOCNO")
	public String DOCNO;

	@Expose
	@SerializedName("DATA")
	public List<CommunityTagData> DATA;

	public static class CommunityTagData {
		@Expose
		@SerializedName("REGDATE")
		public String REGDATE;
		@Expose
		@SerializedName("TAG_WORD")
		public String TAG_WORD;
		@Expose
		@SerializedName("TAG_SEQ")
		public String TAG_SEQ;
	}


}
