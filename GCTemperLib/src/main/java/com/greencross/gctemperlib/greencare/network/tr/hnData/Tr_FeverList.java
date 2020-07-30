package com.greencross.gctemperlib.greencare.network.tr.hnData;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 체온조회

 @Request
 cust_id - 고객사 키
 startdate - 조회 시작일
 enddate - 조회 종료일

 @Response
 Idx - 체온 일련번호
 Input_de - 입력일자
 Input_fever - 측정 체온 값
 is_wearable - 기기사용 여부("기본값 : 0, 0 : 직접 입력, 1: 파트론 웨어러블 기기 사용")
 status - HTTP응답코드
 docno - 전문번호(HJ003)
 resultcode - 결과코드(1000, 8888, 9999)
 message - 메시지(저장에 성공하였습니다.|등록된 회원정보가 없습니다.|오류가 발생하였습니다.)

 */

public class Tr_FeverList extends BaseData {
	private final String TAG = getClass().getSimpleName();

    @Override
    protected String getConnUrl() {
        return BaseUrl.COMMON_URL + "/Fever/v1/FeverList";
    }

	public static class RequestData {
        public String startdate;
        public String enddate;
	}

	public Tr_FeverList(Context context) {
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
            body.put("startdate", data.startdate);
            body.put("enddate", data.enddate);
			return body;
		}
		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/
    @SerializedName("Idx")
    public String Idx;
    @SerializedName("Input_de")
    public String Input_de;
    @SerializedName("Input_fever")
    public String Input_fever;
    @SerializedName("is_wearable")
    public String is_wearable;
    @SerializedName("status")
    public String status;
    @SerializedName("docno")
    public String docno;
    @SerializedName("resultcode")
    public String resultcode;
    @SerializedName("message")
    public String message;

}
