package com.greencross.gctemperlib.hana.network.tr.hnData;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.greencross.gctemperlib.hana.network.tr.BaseData;
import com.greencross.gctemperlib.hana.network.tr.BaseUrl;
import com.greencross.gctemperlib.hana.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 체온관리

 @Request
 cust_id - 고객사 키
 fever - 체온
 area - 시/도
 est1 - 시/구
 est2 - 읍/면/동
 la - 위도
 lo - 경도
 Input_de - 입력일자
 is_wearable - 기기사용 여부("기본값 : 0, 0 : 직접 입력, 1: 파트론 웨어러블 기기 사용")

 @Response
 status - HTTP응답코드
 docno - 전문번호
 resultcode - 결과코드(1000, 8888, 9999)
 message - 메시지(저장에 성공하였습니다.|등록된 회원정보가 없습니다.|오류가 발생하였습니다.)

 */

public class Tr_Temperature extends BaseData {
	private final String TAG = getClass().getSimpleName();
    @Override
    protected String getConnUrl() {
        return BaseUrl.COMMON_URL + "/Fever/v1/Temperature";
    }

	public static class RequestData {
        public String fever;            // fever - 체온
        public String area;             // area - 시/도
        public String est1;             // est1 - 시/구
        public String est2;             // est2 - 읍/면/동
        public String la;               // la - 위도
        public String lo;               // lo - 경도
        public String Input_de ;        // Input_de - 입력일자
        public String is_wearable ;     // is_wearable - 기기사용 여부("기본값 : 0, 0 : 직접 입력, 1: 파트론 웨어러블 기기 사용")
	}

	public Tr_Temperature(Context context) {
        mContext = context;
//		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = new JSONObject();
			RequestData data = (RequestData) obj;
            String custNo = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_CUST_NO);          // 사용자 번호
            String pushToken = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_PUSH_TOKEN);    // 푸시키

            body.put("cust_id", custNo);
            body.put("fever", data.fever);
            body.put("area", data.area);
            body.put("est1", data.est1);
            body.put("est2", data.est2);
            body.put("la", data.la);
            body.put("lo", data.lo);
            body.put("Input_de ", data.Input_de);
            body.put("is_wearable", data.is_wearable);
			return body;
		}
		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("message")
    public String message;
    @SerializedName("resultcode")
    public String resultcode;
    @SerializedName("status")
    public String status;
    @SerializedName("docno")
    public String docno;

}
