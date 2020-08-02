package com.greencross.gctemperlib.hana.network.tr.hnData;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.greencross.gctemperlib.hana.network.tr.BaseData;
import com.greencross.gctemperlib.hana.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
PUSH 알림 설정

 @Request
 cust_id - 고객사 키(단순 조회시 cust_id값만 입력)
 ncrgd_yn - 측정독려알림수신("Y : 동의 (기본값), N : 미동의")
 area_thmt_yn - 지역체온알림수신("Y: 동의, N : 미동의(기본값)")

 @Response
 ncrgd_yn_out - 측정독려알림수신여부(출력)
 ncrgd_de_out - 측정독려알림수신일(출력)
 area_thmt_yn_out - 지역체온알림수신여부(출력)
 area_thmt_de_out - 지역체온알림수신일(출력)
 status - HTTP응답코드
 docno - 전문번호(HM002)
 resultcode - 결과코드(1000, 8888, 9999)
 message - 메시지(등록에 성공하였습니다.|등록된 회원이 없습니다.|오류가 발생하였습니다.)
 */

public class Tr_Setup extends BaseData {
	private final String TAG = getClass().getSimpleName();
    @Override
    protected String getConnUrl() {
        return BaseUrl.COMMON_URL + "/Member/v1/Setup";
    }


	public static class RequestData {
		public String ncrgd_yn;
		public String area_thmt_yn;
	}

	public Tr_Setup(Context context) {
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
			body.put("ncrgd_yn", data.ncrgd_yn);
			body.put("area_thmt_yn", data.area_thmt_yn);
			return body;
		}
		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("resultcode")
    public String resultcode;
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
    @SerializedName("ncrgd_yn_out")
    public String ncrgd_yn_out;
    @SerializedName("ncrgd_de_out")
    public String ncrgd_de_out;
    @SerializedName("area_thmt_yn_out")
    public String area_thmt_yn_out;
    @SerializedName("area_thmt_de_out")
    public String area_thmt_de_out;
    @SerializedName("docno")
    public String docno;
}
