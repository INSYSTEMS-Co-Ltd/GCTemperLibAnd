package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 프로필보기 DB012
 * Input 값
 * SEQ: 대상 고객 회원일련번호
 * ME_SEQ: 조회 고객 회원 일련번호
 * PG_SIZE: 페이지사이즈 (페이지당 리스트 개수) (PG_SIZE 미 입력 및 9999값은 페이징 없이 전체 출력입니다.)
 * PG: 현재 페이지 (총 페이지:TPAGE까지 페이징 됩니다)
 * <p>
 * Output 값
 * MBER_SN: 대상 고객 회원일련번호
 * MBER_NM: 대상 고객명
 * MBER_SEX: 성별(1:남자,2:여자)
 * PROFILE_PIC: 프로필 사진
 * DISEASE_OPEN: 질환명 공개여부(Y/N)
 * DISEASE_NM: 질환명 (비공개시  비공개 로 표시 됨)
 * POINT_TOTAL_AMT: 총 누적 포인트
 * LV: 레벨 (1000포인트 당 1레벨 씩 획득 최초 1레벨)
 * TOT_PAGE: 총 페이지 수
 * TOT_CNT: 총 레코드 수
 * RESULT_CODE: 오류코드
 * DATA_LENGTH: DATA 배열의 원소 개수
 * DATA: 배열
 * CM_GUBUN: 커뮤니티 구분 1:커뮤니티 , 2: 공지사항
 * CM_SEQ: 커뮤니티 기본키
 * NICK: 닉네임(5글자이내)
 * PROFILE_PIC: 프로필 사진
 * CM_TITLE: 제목
 * CM_CONTENT: 글 내용
 * REGDATE: 등록일 EX)2016-05-20 오전 11:15:00
 * CM_TAG: 해쉬태그 값 (,로 구분 ) EX)#일상,#운동
 * HCNT: 해당 글 좋아요 수
 * MYHEART: 내가 좋아요 한 글 여부(Y/N)
 * RCNT: 해당 글 댓글 수
 */

public class Tr_DB012 extends BaseData {

	public static class RequestData {
		public String SEQ;
		public String ME_SEQ;
		public String PG_SIZE;
		public String PG;
	}

	public Tr_DB012() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;
			body.put("SEQ", data.SEQ);
			body.put("ME_SEQ", data.ME_SEQ);
			body.put("PG_SIZE", data.PG_SIZE);
			body.put("PG", data.PG);
			body.put("DOCNO", "DB012");

			return body;
		}

		return super.makeJson(obj);
	}

	/**************************************************************************************************/
	/***********************************************RECEIVE********************************************/
	/**************************************************************************************************/
//	@Expose
//	@SerializedName("DATA")
//	public List<CommunityListViewData> DATA;
	@Expose
	@SerializedName("DATA_LENGTH")
	public String DATA_LENGTH;
	@Expose
	@SerializedName("RESULT_CODE")
	public String RESULT_CODE;
	@Expose
	@SerializedName("TOT_CNT")
	public String TOT_CNT;
	@Expose
	@SerializedName("TOT_PAGE")
	public String TOT_PAGE;
	@Expose
	@SerializedName("LV")
	public String LV;
	@Expose
	@SerializedName("TOT_POINT")
	public String TOT_POINT;
	@Expose
	@SerializedName("DISEASE_NM")
	public String DISEASE_NM;
	@Expose
	@SerializedName("DISEASE_OPEN")
	public String DISEASE_OPEN;
	@Expose
	@SerializedName("PROFILE_PIC")
	public String PROFILE_PIC;
	@Expose
	@SerializedName("MBER_SEX")
	public String MBER_SEX;
	@Expose
	@SerializedName("MBER_NM")
	public String MBER_NM;
	@Expose
	@SerializedName("MBER_SN")
	public String MBER_SN;
	@Expose
	@SerializedName("DOCNO")
	public String DOCNO;
	@Expose
	@SerializedName("MBER_GRAD")
	public String MBER_GRAD;
	@Expose
	@SerializedName("CM_MEAL")
	public String CM_MEAL;
}
