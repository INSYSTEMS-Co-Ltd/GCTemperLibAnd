package com.greencross.gctemperlib.greencare.network.tr.data;

import android.content.Context;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 FUNCTION NAME	login	로그인

 Input
 변수명	FUNCTION NAME 	설명
 api_code		api 코드명 string
 app_code		iOS, android
 insures_code		회원사 코드
 token		디바이스 토큰
 mber_id		회원아이디
 mber_pwd		회원 비밀번호
 app_code		os 버전
 phone_model		폰 모델명(SM-N9105
 pushk
 app_ver		앱 버전



 json = @"{   ""api_code"": ""login"",  ""insures_code"": ""300"", ""token"": ""APA91bHanHsaue_chJqab7tKn04XSjGrr4JvyvyrHoB2uZCx9eRY54aCrk14L0MfTx1DhSbgaUYaWGYfoBnPqO7aJSRA-xdU2gEgprAWRxraSI7cDLEVnAqyXnrZpYigAE9OmSNSPnkK9lI0zjNQZhQgwn3uDgpLRYh8mM9uHq1FLOfYhYNhA1E"", ""mber_id"": ""tjhong@naver.com"" , ""mber_pwd"": ""999999a"", ""app_code"": ""android19"",""phone_model"": ""SM-N910S"",""pushk"": """", ""app_ver"": ""1.20"" }";

 Output
 변수명		설명
 api_code		api 코드명 string
 insures_code		회원사 코드
 mber_sn		회원 키값
 log_yn		Y
 tot_basic_goal
 day_basic_goal
 default_basic_goal
 mber_hp_newyn
 mber_sex		성별 1(남) 2,(여)
 mber_height		회원 키
 mber_bdwgh		회원체중
 mber_actqy		활동량 1,2,3

 */

public class Tr_login extends BaseData {
	private final String TAG = Tr_login.class.getSimpleName();


	public static class RequestData {
        public String mber_id; // tjhong@naver.com
        public String mber_pwd; // 999999a
        public String phone_model; // SM-N910S
        public String pushk; //
        public String app_ver; // 1.20
        public String mber_lifyea;

	}

    public Tr_login(){}

	public Tr_login(Context context) {
		mContext = context;

		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj("login");

			RequestData data = (RequestData) obj;

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();    // 토큰값.

			body.put("api_code","login"); //  "login",
			body.put("insures_code", INSURES_CODE); //  "300",
			body.put("token", refreshedToken);
			body.put("mber_id",data.mber_id); //  "tjhong@naver.com",
			body.put("mber_pwd",data.mber_pwd); //  "999999a",
			body.put("app_code", APP_CODE); //  "android19",
			body.put("phone_model",data.phone_model); //  "SM-N910S",
			body.put("pushk", ""); //  "",
			body.put("app_ver",data.app_ver); //  "1.20"

			return body;
		}

		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

//        @SerializedName("api_code") // login",
//        public String api_code;
//        @SerializedName("insures_code") // 303",
//        public String insures_code;
//        @SerializedName("mber_sn") // 1406",
//        public String mber_sn;
//        @SerializedName("tot_basic_goal") // 0",
//        public String tot_basic_goal;
//        @SerializedName("day_basic_goal") // 0",
//        public String day_basic_goal;
//        @SerializedName("default_basic_goal") // ",
//        public String default_basic_goal;
//        @SerializedName("log_yn") // Y",
//        public String log_yn;
//        @SerializedName("mber_hp_newyn") // Y",
//        public String mber_hp_newyn;
//        @SerializedName("mber_sex") // 2",
//        public String mber_sex;
//        @SerializedName("mber_lifyea") // 19750221",
//        public String mber_lifyea;
//        @SerializedName("mber_nm") // 세현아빠6",
//        public String mber_nm;
//        @SerializedName("mber_hp") // 01039381115",
//        public String mber_hp;
//        @SerializedName("mber_height") // 182",
//        public String mber_height;
//        @SerializedName("mber_bdwgh") // 38",
//        public String mber_bdwgh;
//        @SerializedName("mber_bdwgh_app") // 88.10",
//        public String mber_bdwgh_app;
//        @SerializedName("mber_bdwgh_goal") // 65",
//        public String mber_bdwgh_goal;
//        @SerializedName("mber_actqy") // 1",
//        public String mber_actqy;
//        @SerializedName("disease_nm") // 1",
//        public String disease_nm;
//        @SerializedName("medicine_yn") // Y",
//        public String medicine_yn;
//        @SerializedName("smkng_yn") // Y",
//        public String smkng_yn;
//        @SerializedName("mber_zone") // 1",
//        public String mber_zone;
//        @SerializedName("goal_mvm_calory") // 1000",
//        public String goal_mvm_calory;
//        @SerializedName("goal_mvm_stepcnt") // 1300",
//        public String goal_mvm_stepcnt;
//        @SerializedName("goal_water_ntkqy") // 0",
//        public String goal_water_ntkqy;
//        @SerializedName("sugar_typ") // 1",
//        public String sugar_typ;
//        @SerializedName("sugar_occur_de") // 2018",
//        public String sugar_occur_de;
//        @SerializedName("day_health_amt") // 35",
//        public String day_health_amt;
//        @SerializedName("user_point_amt") // 3",
//        public String user_point_amt;
//        @SerializedName("sugar_alert_yn") // Y",
//        public String sugar_alert_yn;
//        @SerializedName("health_alert_yn") // Y",
//        public String health_alert_yn;
//        @SerializedName("mission_alert_yn") // Y",
//        public String mission_alert_yn;
//        @SerializedName("add_reg_yn") // N"w
//        public String add_reg_yn;
//
//
//        // 로그인시 아이디 저장하기 위한 변수
//        public String mber_id;

}
