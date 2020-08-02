package com.greencross.gctemperlib.hana.network.tr.hnData;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.greencross.gctemperlib.hana.network.tr.BaseData;
import com.greencross.gctemperlib.hana.network.tr.BaseUrl;

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

    @Override
    protected String getConnUrl() {
        return BaseUrl.COMMON_URL + "/Fever/v1/MapList";
    }

    public static class RequestData {
	}

	public Tr_MapList(Context context) {
        mContext = context;
//		super.conn_url = BaseUrl.COMMON_URL;
	}

//	@Override
//	public JSONObject makeJson(Object obj) throws JSONException {
//		if (obj instanceof RequestData) {
//			JSONObject body = new JSONObject();
//			RequestData data = (RequestData) obj;
//            String custNo = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_CUST_NO);          // 사용자 번호
//            String pushToken = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_PUSH_TOKEN);    // 푸시키
//
//			return body;
//		}
//		return super.makeJson(obj);
//	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("status")
    public String status;
    @SerializedName("docno")
    public String docno;
    @SerializedName("resultcode")
    public String resultcode;
    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<MapList> mapList = new ArrayList<>();
    public class MapList {
        @SerializedName("loc_nm_1")
        public String loc_nm_1;
        @SerializedName("loc_nm_2")
        public String loc_nm_2;
        @SerializedName("avg_fever")
        public String avg_fever;
        @SerializedName("loc_1")
        public String loc_1;
        @SerializedName("loc_2")
        public String loc_2;
    }
}
