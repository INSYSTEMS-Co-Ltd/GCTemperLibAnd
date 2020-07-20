package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**

 게시글 리스트 DB001
 Input 값
 SEQ: 회원일련번호
 PG_SIZE: 페이지사이즈 (페이지당 리스트 개수)
 PG: 현재 페이지 (총 페이지:TPAGE까지 페이징 됩니다)
 CM_GUBUN: 커뮤니티 구분 1:커뮤니티 , 2: 공지사항
 CM_TAG: 해쉬태그 값으로 검색
 SWORD: 검색어 (제목 OR 내용 LIKE검색) (항목필수 아님,검색시 만 사용 하셔도 됩니다)


 Output값
 AST_LENGTH : 배열의 원소 개수
 ADDR_MASS : 배열
 TPAGE : 총 페이지수
 CM_SEQ : 커뮤니티 기본키(회원키)
 MBER_SN : 회원키
 NICK : 닉네임(5글자이내)
 PROFILE_PIC : 프로필 사진
 CM_TITLE : 제목
 CM_CONTENT : 글 내용
 REGDATE : 등록일
 CM_TAG : 해쉬태그 값 (,로 구분 ) EX)#일상,#운동
 HCNT : 글 당 하트 수
 MYHEART : 내가 좋아요 한 글 여부(Y/N)
 RCNT : 글 당 댓글 수
 MBER_GRAD : 정회원 10, 준회원 20
 */

public class Tr_DB001 extends BaseData {

	public static class RequestData {
		public String CMGUBUN; //1. 커뮤니티, 2. 공지사항
		public String PG_SIZE; //페이지사이즈 (페이지당 리스트 개수)
		public String PG; //현재 페이지 (총 페이지:TPAGE까지 페이징 됩니다)
		public String SEQ; //회원일련번호
		public String CM_TAG; //해쉬태그 값으로 검색
		public String SWORD; //검색어 (제목 OR 내용 LIKE검색) (항목필수 아님,검색시 만 사용 하셔도 됩니다)
	}

    public Tr_DB001() {
		super.conn_url="https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
		super.json_obj_name = "strJson";
	}
//	public Tr_DB001(Context context) {
//		mContext = context;
//	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();    // 토큰값.

			 body.put("SEQ", data.SEQ);
			 body.put("PG_SIZE", data.PG_SIZE); //한번에 요청하는 페이지 사이즈?
			 body.put("PG", data.PG); // 요청페이지
			 body.put("SWORD", data.SWORD);
			 body.put("CM_GUBUN", data.CMGUBUN);
			 body.put("CM_TAG",data.CM_TAG);
			 body.put("DOCNO", "DB001");

			return body;
		}

		return super.makeJson(obj);
	}

	/**************************************************************************************************/
	/***********************************************RECEIVE********************************************/
	/**************************************************************************************************/

	@SerializedName("DATA_LENGTH")
	public String DATA_LENGTH;
	@SerializedName("DOCNO")
	public String DOCNO;
//	@SerializedName("DATA")
//	public List<CommunityListViewData> DATA;




}
