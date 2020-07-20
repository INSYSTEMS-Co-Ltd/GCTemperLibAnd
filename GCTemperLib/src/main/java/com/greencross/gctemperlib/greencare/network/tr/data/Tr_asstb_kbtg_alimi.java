package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 FUNCTION NAME	water_goalqy	물 목표량 // 섭취량

 Input
 변수명	FUNCTION NAME 	설명
 api_code		api 코드명 string
 insures_code		회원사 코드
 mber_sn		회원 키값
 goal_water_goalqy		물 목표량
 goal_water_ntkqy		물 섭취량




 json = @"{   ""api_code"": ""mvm_goalqy"",  ""insures_code"": ""300"",  ""mber_sn"": ""1000"" , ""goal_water_goalqy"": ""4000""  , ""goal_water_ntkqy"": ""1000""    }";
 Output
 변수명		설명
 api_code		api 코드명 string
 insures_code		회원사 코드
 reg_yn		등록여부

 */

public class Tr_asstb_kbtg_alimi extends BaseData {
    private final String TAG = Tr_asstb_kbtg_alimi.class.getSimpleName();


    public static class RequestData {
        public String PAGE; // 페이지 번호
        public String PLN; // 페이지당 게시물 수
        public String mber_sn;

    }

    public Tr_asstb_kbtg_alimi() {
//		mContext = context;

        super.conn_url = BaseUrl.COMMON_URL;
//        super.conn_url = "https://wkd.walkie.co.kr/KBT/ws.asmx/getJson";
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        if (obj instanceof RequestData) {
//			JSONObject body = getBaseJsonObj("login");
            JSONObject body = new JSONObject();

            RequestData data = (RequestData) obj;

            body.put("api_code",getApiCode("Tr_asstb_kbtg_alimi")); //  "KA001",
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn ); // 1000",
            body.put("pushk", "0" ); // 1000",
            body.put("pageNumber", data.PAGE); //  "",
            body.put("pln", data.PLN); //  "",

            return body;
        }

        return super.makeJson(obj);
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code") // KA001",
    public String api_code;
    @SerializedName("insures_code") // KA001",
    public String insures_code;
    @SerializedName("chlmReadern") // 배열명
    public List<chlmReadern> dataList = new ArrayList<>();

    public class chlmReadern {
        @SerializedName("kbta_idx") // 알리미 게시물 일련번호
        public String kbta_idx;
        @SerializedName("kbt") // 알리미 제목
        public String kbt;
        @SerializedName("sub_tit") // 알리미 부제목
        public String sub_tit;
        @SerializedName("kbc") // 알리미 내용
        public String kbc;
        @SerializedName("kaimg") // 알리미 이미지 URL
        public String kaimg;
        @SerializedName("ka_timg") // 알리미 타이틀 이미지 URL
        public String ka_timg;
        @SerializedName("html_yn") // 알리미 이미지
        public String html_yn;
        @SerializedName("notice_typ") // 알리미 이미지
        public String notice_typ;
        @SerializedName("kbvd") //알리미 게재일
        public String kbvd;
        @SerializedName("kbt_pdf") //PDF 여부
        public String kbt_pdf;
    }

    @SerializedName("KBTP") // 알리미 총 페이지 수"2",
    public String KBTP;
    @SerializedName("DATA_LENGTH") // 데이터 길이"15",
    public String DATA_LENGTH;

    @SerializedName("data_yn") // "0000",
    public String data_yn;
}
