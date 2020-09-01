package com.greencross.gctemperlib.hana.network.tr.hnData;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.greencross.gctemperlib.hana.network.tr.BaseData;
import com.greencross.gctemperlib.hana.network.tr.BaseUrl;
import com.greencross.gctemperlib.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 *
관심지역 설정

 cust_id - 고객사 키(단순 조회시 cust_id값만 입력)
 heat_do - 관심지역1 LOC_NM_1와 매칭
 heat_si - 관심지역2 LOC_NM_2와 매칭

 @Response
 heat_do_out - 관심지역1 LOC_NM_1과 매칭 (출력)
 heat_si_out - 관심지역2 LOC_NM_2와 매칭 (출력)
 status - HTTP응답코드 docno - 전문번호(HM002)
 resultcode - 결과코드(1000, 8888, 9999) message - 메시지(등록에 성공하였습니다.|등록된 회원이 없습니다.|오류가 발생하였습니다.)
 */

public class Tr_AreaSetup extends BaseData {
	private final String TAG = getClass().getSimpleName();

	@Override
	protected String getConnUrl() {
		return BaseUrl.COMMON_URL + "/Member/v1/AreaSetup";
	}

	public static class RequestData {
		public String heat_do;
		public String heat_si;
	}

	public Tr_AreaSetup(Context context) {
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
			body.put("heat_do", data.heat_do);
			body.put("heat_si", data.heat_si);
			return body;
		}
		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("heat_do_out")
    public String heat_do_out;
    @SerializedName("heat_si_out")
    public String heat_si_out;
    @SerializedName("status")
    public String status;
    @SerializedName("resultcode")
    public String resultcode;
	@SerializedName("message")
	public String message;
}
