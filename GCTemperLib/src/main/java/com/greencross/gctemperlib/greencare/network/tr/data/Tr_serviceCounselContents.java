package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 서비스 신청현황 내용보기(상담내역 상세보기)
 */

public class Tr_serviceCounselContents extends BaseData {
    private final String TAG = Tr_serviceCounselContents.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String pageNumber;
        public String seq;

    }

    public Tr_serviceCounselContents() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_serviceCounselContents.RequestData) {
            JSONObject body = new JSONObject();
            Tr_serviceCounselContents.RequestData data = (Tr_serviceCounselContents.RequestData) obj;

            body.put("api_code", getApiCode("Tr_serviceCounselContents") );
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn);
            body.put("seq", data.seq); //  1000

            return body;
        }

        return super.makeJson(obj);
    }

    public JSONArray getArray(Tr_get_hedctdata.DataList dataList) {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            obj.put("idx" , dataList.idx ); //170410173713859",
            array.put(obj);
        } catch (JSONException e) {
            Logger.e(e);
        }

        return array;
    }

    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/
    @SerializedName("pageNumber")
    public int pageNumber; //
    @SerializedName("maxpageNumber")
    public String maxpageNumber; //
    @SerializedName("serviceCounselContents")
    public List<ServiceCounselContents> serviceCounselContentsList = new ArrayList<>();//

    public class ServiceCounselContents {
        @SerializedName("title") // 헬스케어서비스 콜 센터에서 상담하신 내용입니다.",
        public String title;
        @SerializedName("time") // 201802091624319178",
        public String time;
        @SerializedName("type") // [일반상담]",
        public String type;
        @SerializedName("seq") // 2295236",
        public String seq;
        @SerializedName("question") // 농협 VIP 김영섭 IN) 문자를 받았는데 날짜만 있고 시간이 없어요.",
        public String question;
        @SerializedName("answer") // 분당차병원 오전 7시반~8시반사이에 내원해 주시면 됩니다.",
        public String answer;
        @SerializedName("answer_name") // 조민정",
        public String answer_name;
        @SerializedName("question_sex") // ",
        public String question_sex;
        @SerializedName("question_name") // 홍태진",
        public String question_name;
        @SerializedName("question_time") // 201802091624319178",
        public String question_time;
        @SerializedName("question_age") // 119"
        public String question_age;
    }

}
