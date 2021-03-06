package com.greencross.gctemperlib.hana.network.tr.hnData;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.greencross.gctemperlib.BuildConfig;
import com.greencross.gctemperlib.hana.network.tr.BaseData;
import com.greencross.gctemperlib.hana.network.tr.BaseUrl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 *
    회원체크

     @Request
     cust_id - 고객사 키
     phone_gubun - 폰 구분("A : 안드로이드, I : 아이폰")
     appver - 어플버젼정보(사용자)
     devicetoken - gsm 토큰 id
     os_ver - 폰 os버젼정보(사용자)
     p_model - 폰 모델 정보(사용자)
     pushk - 푸쉬종류 (아이폰에만 존재)("AT: 앱스토어 테스트, AR: 앱스토어 배포")

     @Response
     juminnum - 회원 대체키
     memname - 회원 명
     hpnum - 회원 핸드폰 번호
     ncrgd_yn - 측정독려알림수신 여부("Y: 동의, N: 미동의")
     ncrgd_de - 측정독려알림수신 업데이트 일자
     area_thmt_yn - 지역체온알림수신
     area_thmt_de - 지역체온알림수신 업데이트 일자
     heat_si - 관심지역2 loc_nm_2와 일치
     heat_do - 관심지역1 loc_nm_1와 일치
     new_appver - 최신 앱 버전(각 폰 OS에 맞는 앱 버전)
     new_link - 최신 앱 어플링크(각 폰 OS에 맞는 스토어 링크)
     status - HTTP응답코드
     indate - 서비스 시작일
     enddate - 서비스 종료일
     cmpny_ub_code - 유비케어 서비스 코드번호
     docno - 전문번호(HM001)
     resultcode - 결과코드(1000, 7777, 8888, 9999)
     message - 메시지(등록에 성공하였습니다.|사용중지된 회원입니다..|등록된 회원이 없습니다.|오류가 발생하였습니다.)

 */

public class Tr_Login extends BaseData {
	private final String TAG = getClass().getSimpleName();

    @Override
    protected String getConnUrl() {
        return BaseUrl.COMMON_URL + "/Member/v1/Login";
    }

	public static class RequestData {
		public String cust_id;	//":"1",
		public String devicetoken = "1111";	//":"1111",
	}

	public Tr_Login(Context context) {
        mContext = context;
//		super.conn_url = BaseUrl.COMMON_URL;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = new JSONObject();
			RequestData data = (RequestData) obj;
//            String refreshedToken = FirebaseInstanceId.getInstance().getToken();    // 토큰값.
//            String custNo = SharedPref.getInstance(mContext).getPreferences(SharedPref.PREF_CUST_NO);          // 사용자 번호
			body.put("cust_id", data.cust_id);
			body.put("phone_gubun", OS_GUBUN);
			body.put("appver", BuildConfig.VERSION_NAME);
			if (TextUtils.isEmpty(data.devicetoken) == false)
			    body.put("devicetoken", data.devicetoken);
			body.put("os_ver", ""+Build.VERSION.RELEASE);
			body.put("p_model", Build.MODEL);
			return body;
		}
		return super.makeJson(obj);
	}

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("resultcode")
    public String resultcode; // - 결과코드(1000, 7777, 8888, 9999)
    @SerializedName("message")
    public String message; // - 메시지(등록에 성공하였습니다.|사용중지된 회원입니다..|등록된 회원이 없습니다.|오류가 발생하였습니다.)


    @SerializedName("juminnum")
    public String juminnum; // - 회원 대체키
    @SerializedName("memname")
    public String memname; // - 회원 명
    @SerializedName("hpnum")
    public String hpnum; // - 회원 핸드폰 번호
    @SerializedName("ncrgd_yn")
    public String ncrgd_yn; // - 측정독려알림수신 여부("Y: 동의, N: 미동의")
    @SerializedName("ncrgd_de")
    public String ncrgd_de; // - 측정독려알림수신 업데이트 일자
    @SerializedName("area_thmt_yn")
    public String area_thmt_yn; // - 지역체온알림수신
    @SerializedName("area_thmt_de")
    public String area_thmt_de; // - 지역체온알림수신 업데이트 일자
    @SerializedName("heat_si")
    public String heat_si; // - 관심지역2 "loc_nm_2와" loc_nm_2와 일치
    @SerializedName("heat_do")
    public String heat_do; //- 관심지역1 oc_nm_1와" oc_nm_1와 일치
    @SerializedName("new_appver")
    public String new_appver; // - 최신 앱 버전(각 폰 OS에 맞는 앱 버전)
    @SerializedName("new_link")
    public String new_link; // - 최신 앱 어플링크(각 폰 OS에 맞는 스토어 링크)
    @SerializedName("status")
    public String status; // - HTTP응답코드
    @SerializedName("indate")
    public String indate ; //- 서비스 시작일
    @SerializedName("enddate")
    public String enddate ; //- 서비스 종료일
    @SerializedName("cmpny_ub_code")
    public String cmpny_ub_code; // - 유비케어 서비스 코드번호
    @SerializedName("docno")
    public String docno; // - 전문번호(HM001)
}
