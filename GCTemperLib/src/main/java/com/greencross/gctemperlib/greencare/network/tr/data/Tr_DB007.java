package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * 게시글 상세 리스트 DB007
 input 값
 SEQ : 회원일련번호(MBER_SN값)
 CM_SEQ : 커뮤니티 기본키

 output값
 CM_SEQ : 암환자 커뮤니티 기본키
 NICK : 닉네임(5글자이내)
 CM_TITLE : 제목
 CM_CONTENT : 글내용
 CM_GUBUN : 말머리 구분 1:암 진단 후, 2: 암치료방법, 3: 암 극복후기, 4:기타
 CM_IMG1 : 이미지1
 CM_IMG2 : 이미지2
 CM_IMG3 : 이미지3
 REGDATE : 등록일
 PROFILE_PIC : 작성자 프로필 사진
 CM_TAG : 해쉬태그 값 (,로 구분 ) EX)#일상,#운동
 HCNT : 글 당 추천 수
 HYN : 본인 추천여부(Y/N)
 RCNT : 글 당 댓글 수
 OSEQ : 글쓴이 회원일련번호
 MBER_GRAD : 정회원 10, 준회원 20
 RESULT_CODE : 결과코드
 AST_LENGTH : 배열의 원소 개수
 AST_MASS :
 CC_SEQ : 암환자 커뮤니티 댓글 기본키
 NICK : 닉네임(5글자이내)
 PROFILE_PIC : 프로필 사진
 SEQ : 회원일련번호
 CC_CONTENT : 댓글내용
 MBR_GRD : 정회원 10, 준회원 20
 REGDATE : 등록일


 0000 : 조회성공
 4444 : 등록된 글이 없습니다.
 6666 : 회원이 존재하지 않음
 9999 : 기타오류


 * 게시글에서 댓글을 계속해서 불러올때는 댓글리스트API를 사용합니다.
 */

public class Tr_DB007 extends BaseData {

	public static class RequestData {
		public String CM_SEQ; //게시글 일련번호
		public String SEQ; //회원일련번호
	}

	public Tr_DB007() {
		super.conn_url = "https://m.shealthcare.co.kr/HL_MOM_COMMUNITY/WS.ASMX/getJson";
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
			body.put("CM_SEQ", data.CM_SEQ);
			body.put("DOCNO", "DB007");

			return body;
		}

		return super.makeJson(obj);
	}

	/**************************************************************************************************/
	/***********************************************RECEIVE********************************************/
	/**************************************************************************************************/

	@Expose
	@SerializedName("DATA_LENGTH")
	public String DATA_LENGTH;
	@Expose
	@SerializedName("DATA")
	public List<DATA> DATA;
	@Expose
	@SerializedName("RESULT_CODE")
	public String RESULT_CODE;
	@Expose
	@SerializedName("OSEQ")
	public String OSEQ;
	@Expose
	@SerializedName("MBER_GRAD")
	public String MBER_GRAD;
	@Expose
	@SerializedName("HYN")
	public String HYN;
	@Expose
	@SerializedName("RCNT")
	public String RCNT;
	@Expose
	@SerializedName("HCNT")
	public String HCNT;
	@Expose
	@SerializedName("CM_TAG")
	public List<String> CM_TAG;
	@Expose
	@SerializedName("PROFILE_PIC")
	public String PROFILE_PIC;
	@Expose
	@SerializedName("REGDATE")
	public String REGDATE;
	@Expose
	@SerializedName("CM_IMG3")
	public String CM_IMG3;
	@Expose
	@SerializedName("CM_IMG2")
	public String CM_IMG2;
	@Expose
	@SerializedName("CM_IMG1")
	public String CM_IMG1;
	@Expose
	@SerializedName("CM_CONTENT")
	public String CM_CONTENT;
	@Expose
	@SerializedName("CM_GUBUN")
	public String CM_GUBUN;
	@Expose
	@SerializedName("CM_TITLE")
	public String CM_TITLE;
	@Expose
	@SerializedName("NICK")
	public String NICK;
	@Expose
	@SerializedName("DOCNO")
	public String DOCNO;
	@Expose
	@SerializedName("MYHEART") //ok
	public String MYHEART; //좋아요 클릭유무
	@Expose
	@SerializedName("CM_MEAL") //ok
	public String CM_MEAL; //좋아요 클릭유무

	public static class DATA {
		@Expose
		@SerializedName("REGDATE")
		private String REGDATE;
		@Expose
		@SerializedName("MBR_GRD")
		private String MBR_GRD;
		@Expose
		@SerializedName("CC_CONTENT")
		private String CC_CONTENT;
		@Expose
		@SerializedName("PROFILE_PIC")
		private String PROFILE_PIC;
		@Expose
		@SerializedName("SEQ")
		private String SEQ;
		@Expose
		@SerializedName("NICK")
		private String NICK;
		@Expose
		@SerializedName("CC_SEQ")
		private String CC_SEQ;
	}


}
