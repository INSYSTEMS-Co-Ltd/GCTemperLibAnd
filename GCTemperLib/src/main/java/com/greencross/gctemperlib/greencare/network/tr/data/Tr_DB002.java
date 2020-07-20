package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 게시글 입력/수정 DB002
 Input 값
 CM_SEQ: 커뮤니티 고유번호(게시물키,없으면 항목있고 NULL값)
 SEQ: 회원일련번호
 CM_TITLE: 글제목
 CM_GUBUN: 커뮤니티 구분 1:커뮤니티 , 2: 공지사항
 CM_CONTENT: 글내용
 FILE: 이미지
 PROFILE_PIC: 프로필 사진
 CM_TAG: 해쉬태그 값 (,로 구분 ) EX)#일상,#운동
 DEL_FILE: 삭제할 이미지 (,로구분) ex)CM_IMG1, CM_IMG2

 Output 값
 RESULT_CODE : 결과코드
 CM_SEQ : 커뮤니티 일련번호

 0000 : 입력(수정)성공
 4444 : 수정 시 수정글 존재하지 않음.
 5555 : 동일한게시물존재
 6666 : 필수 값 입력 없음.(커뮤니티 구분,제목,내용)
 8888 : 회원정보없음
 9999 : 기타오류

 // CM_SEQ 값이없으면 등록, 있으면 수정
 // 원본이미지를 넣으면 tm_가 붙은 썸네일 이미지가 생성됩니다. EX)song.jpg(원본), tm_song_jpg(썸네일)

 URL주소
 https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/comm_ins.aspx (테스트 할 수 있는 페이지)
 https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/HL_upload.ashx (처리페이지, 업로드를 받는 페이지)
 */

public class Tr_DB002 extends BaseData {

	public static class RequestData {
		public String CM_SEQ;
		public String SEQ;
		public String CM_TITLE;
		public String CM_GUBUN;
		public String CM_CONTENT;
		public String FILE;
		public String PROFILE_PIC;
		public String CM_TAG;
		public String DEL_FILE;
		public String CM_MEAL;
	}

	public Tr_DB002() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;

			body.put("CM_MEAL", data.CM_MEAL);
			body.put("CM_SEQ", data.CM_SEQ);
			body.put("SEQ", data.SEQ);
			body.put("CM_TITLE", data.CM_TITLE);
			body.put("CM_GUBUN", "1");
			body.put("CM_CONTENT", data.CM_CONTENT);
			body.put("FILE", data.FILE);
			body.put("PROFILE_PIC", data.PROFILE_PIC);
			body.put("CM_TAG", data.CM_TAG);
			body.put("DEL_FILE", data.DEL_FILE);
			body.put("DOCNO", "DB002");

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
	@SerializedName("DOCNO")
	public String DOCNO;
	@Expose
	@SerializedName("OCM_SEQ")
	public String OCM_SEQ;

	@Expose
	@SerializedName("CM_POINT")
	public String CM_POINT;
}
