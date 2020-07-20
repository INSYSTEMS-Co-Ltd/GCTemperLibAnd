package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 미션 이력 리스트
 json={   "api_code": "misson_month_hist_list", "insures_code": "303","mber_sn": "1344"   ,"pageNumber": "1" }

 // 패이징 구형되어야함.


 //<결과>
 // mission_succ_view : 목표
 // mission_code : 100-일일, 11-30일걷기, 12-30일혈당, 13-병원방문
 // 나의걸음수 : mission_walk_cnt (mission_code 가 11일때)
 // 나의 체크일수 : mission_sugar_cnt (mission_code 가 12일때)
 // "_at" 으로 끝나는 항목은 "성공여부"
 // 성공여부 : mission_walk_at (mission_code 가 11일때), mission_sugar_at (mission_code 가 12일때)

 */

public class Tr_misson_month_hist_list extends BaseData {
	private final String TAG = Tr_misson_month_hist_list.class.getSimpleName();


	public static class RequestData {
		public String mber_sn;
		public String pageNumber;

	}

	public Tr_misson_month_hist_list() throws JSONException {
		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = new JSONObject();//getBaseJsonObj("login_id");

			RequestData data = (RequestData) obj;
            body.put("api_code", getApiCode("Tr_misson_month_hist_list") );
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);
            body.put("pageNumber", data.pageNumber);

			return body;
		}

		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code")
    public String api_code;
    @SerializedName("insures_code")
    public String insures_code;
    @SerializedName("pageNumber")
    public String pageNumber;
    @SerializedName("maxpageNumber")
    public String maxpageNumber;
    @SerializedName("misson_month_list")
    public List<MissionMonth> misson_month_list = new ArrayList<>();

    public class MissionMonth {
        @SerializedName("mission_code") // 13",
        public String mission_code;
        @SerializedName("mission_nm") // 병원방문미션",
        public String mission_nm;
        @SerializedName("mission_app_title") // 병원방문",
        public String mission_app_title;
        @SerializedName("mission_succ_view") // ",
        public String mission_succ_view;
        @SerializedName("mission_start_de") // 20180322",
        public String mission_start_de;
        @SerializedName("mission_end_de") // 20180926",
        public String mission_end_de;
        @SerializedName("mission_walk_cnt") // 0",
        public String mission_walk_cnt;
        @SerializedName("mission_walk_at") // N",
        public String mission_walk_at;
        @SerializedName("mission_walk_point") // 0",
        public String mission_walk_point;
        @SerializedName("mission_sugar_cnt") // 0",
        public String mission_sugar_cnt;
        @SerializedName("mission_sugar_at") // N",
        public String mission_sugar_at;
        @SerializedName("mission_sugar_point") // 0"
        public String mission_sugar_point;
    }
}
