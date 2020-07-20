package com.greencross.gctemperlib.greencare.network.tr.data;

import android.content.Context;

import com.greencross.gctemperlib.greencare.database.DBHelper;
import com.greencross.gctemperlib.greencare.database.DBHelperLog;
import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 메뉴로그 기록

 api_code : api 코드명 string
 insures_code : 회사코드(108)
 mber_sn : 회원키값
 DATA_LENGTH : loop 돌아갈 카운트
 DATA : data
 L_CODE : 대분류
 M_CODE : 중분류
 S_CODE : 소분류
 R_TIME : 잔류시간
 R_COUNT : 카운트
 R_DATE : 로그날자
 
 결과코드
 0000	등록(수정)성공
 1111	대분류  값 존재하지 않음
 2222	중분류  값 존재하지 않음
 3333	소분류  값 존재하지 않음
 4444	회원 기본정보 없음
 5555	APP 로그등록일 존재하지 않음
 8888	폰 일련번호 존재하지 않음
 9999	기타오류
 */

public class Tr_asstb_menu_log_hist extends BaseData {
	private final String TAG = Tr_asstb_menu_log_hist.class.getSimpleName();


	public static class RequestData {
		public String mber_sn;
		public String DATA_LENGTH;
		public JSONArray DATA;
	}

	public Tr_asstb_menu_log_hist() throws JSONException {
		super.conn_url = BaseUrl.COMMON_URL;
	}

	public static JSONArray getArray(Context context) {
		DBHelper helper = new DBHelper(context);
		DBHelperLog db = helper.getLogDb();

		JSONArray array = new JSONArray();

		for (DBHelperLog.Data data : db.getlog()) {
			JSONObject obj = new JSONObject();
			try {
				if(data.s_cod.equals("")){
					obj.put("L_CODE",data.l_cod);
					obj.put("M_CODE",data.m_cod);
					obj.put("R_TIME",data.time);
					obj.put("R_COUNT",data.count);
					obj.put("R_DATE",data.regdate);

				}else if(data.m_cod.equals("") && data.s_cod.equals("")){
					obj.put("L_CODE",data.l_cod);
					obj.put("R_TIME",data.time);
					obj.put("R_COUNT",data.count);
					obj.put("R_DATE",data.regdate);
				}else{
					obj.put("L_CODE",data.l_cod);
					obj.put("M_CODE",data.m_cod);
					obj.put("S_CODE",data.s_cod);
					obj.put("R_TIME",data.time);
					obj.put("R_COUNT",data.count);
					obj.put("R_DATE",data.regdate);
				}


				array.put(obj);
			} catch (JSONException e) {
				Logger.e(e);
			}
		}
		return array;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = new JSONObject();//getBaseJsonObj("login_id");
			RequestData data = (RequestData) obj;

			body.put("api_code", "asstb_menu_log_hist");
			body.put("insures_code", INSURES_CODE);
			body.put("mber_sn", data.mber_sn);
			body.put("DATA_LENGTH",data.DATA_LENGTH);
			body.put("DATA",data.DATA);

			return body;
		}

		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

	@SerializedName("api_code")        //		api 코드명 string
	public String api_code;        //		api 코드명 string
	@SerializedName("insures_code")//		회원사 코드
	public String insures_code;    //		회원사 코드
	@SerializedName("mber_sn")
	public String mber_sn;
    @SerializedName("result_code")  // 저장 여부
    public String result_code;

}
