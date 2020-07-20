package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 댓글 리스트 DB003
 * <p>
 * Input 값
 * SEQ: 회원일련번호(MBER_SN값)
 * CM_SEQ: 커뮤니티 기본키
 * PG_SIZE: 페이지사이즈 (페이지당 리스트 개수)
 * PG: 현재 페이지 (총 페이지:TPAGE까지 페이징 됩니다)
 * <p>
 * Output 값
 * AST_LENGTH : 배열의 원소 개수
 * ADDR_MASS :	배열
 * TPAGE : 총 페이지수
 * CC_SEQ : 암환자 커뮤니티 댓글 기본키
 * CM_SEQ : 암환자 커뮤니티 기본키
 * OSEQ : 댓글 글쓴이 회원일련번호
 * NICK : 닉네임(5글자이내)
 * PROFILE_PIC : 작성자 프로필 사진
 * CM_CONTENT : 댓글내용
 * REGDATE : 등록일
 */

public class Tr_DB003 extends BaseData {

	public static class RequestData {
		public String SEQ;
		public String CM_SEQ;
		public String PG_SIZE;
		public String PG;
	}

	public Tr_DB003() {
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
			body.put("PG_SIZE", data.PG_SIZE); //한번에 요청하는 페이지 사이즈?
			body.put("PG", data.PG); // 요청페이지
			body.put("CM_SEQ", data.CM_SEQ);
			body.put("DOCNO", "DB003");

			return body;
		}

		return super.makeJson(obj);
	}

	/**************************************************************************************************/
	/***********************************************RECEIVE********************************************/
	/**************************************************************************************************/
//	@Expose
//	@SerializedName("DATA")
//	public List<CommunityCommentData> DATA;
	@Expose
	@SerializedName("DATA_LENGTH")
	public String DATA_LENGTH;
	@Expose
	@SerializedName("DOCNO")
	public String DOCNO;
}
