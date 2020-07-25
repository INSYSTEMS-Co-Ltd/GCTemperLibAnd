package com.greencross.gctemperlib.greencare.network.tr.hnData;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 No Environment
 @Request

 @Response
 loc_nm_1 - 주소 1
 loc_nm_2 - 주소 2
 avg_fever - 해당 지역 평균 체온
 loc_1 - 해당 지역의 시청(군청) 위도
 loc_2 - 해당 지역의 시청(군청) 경도
 status - HTTP응답코드
 docno - 전문번호(HJ001)
 resultcode - 결과코드(1000, 4444, 9999)
 message - 메시지(조회에 성공하였습니다.|이력이 없습니다.|오류가 발생하였습니다.)
 */

public class Tr_MapList extends BaseData {
	private final String TAG = getClass().getSimpleName();

	public static class RequestData {
		public String ncrgd_yn;
		public String area_thmt_yn;
	}

	public Tr_MapList(Context context) {
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

    List<MapList> mapList = new ArrayList<>();
    class MapList {
        @SerializedName("ncrgd_yn_out")
        public String ncrgd_yn_out;
        @SerializedName("ncrgd_de_out")
        public String ncrgd_de_out;
        @SerializedName("area_thmt_yn_ou")
        public String area_thmt_yn_out;
        @SerializedName("area_thmt_de_ou")
        public String area_thmt_de_out;
        @SerializedName("status")
        public String status;
        @SerializedName("docno")
        public String docno;
    }

}
