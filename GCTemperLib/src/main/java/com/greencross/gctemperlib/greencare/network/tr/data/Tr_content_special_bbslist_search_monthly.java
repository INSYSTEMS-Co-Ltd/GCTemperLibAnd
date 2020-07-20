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
 건강정보 monthly health  11대 질병정보(검색)
 */

public class Tr_content_special_bbslist_search_monthly extends BaseData {
    private final String TAG = Tr_content_special_bbslist_search_monthly.class.getSimpleName();

    public static class RequestData {

        public String pageNumber;

    }

    public Tr_content_special_bbslist_search_monthly() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_content_special_bbslist_search_monthly.RequestData) {
            JSONObject body = new JSONObject();
            Tr_content_special_bbslist_search_monthly.RequestData data = (Tr_content_special_bbslist_search_monthly.RequestData) obj;

            body.put("api_code", getApiCode("Tr_content_special_bbslist_search_monthly") ); //
            body.put("insures_code", INSURES_CODE);
            body.put("pageNumber", data.pageNumber); //  1000

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
    public int maxpageNumber; //
    @SerializedName("bbslist")
    public List<Bbs> bbslist = new ArrayList<>(); //

    class Bbs {
        @SerializedName("info_day")
        public String info_day;
        @SerializedName("info_title_img")
        public String info_title_img;
        @SerializedName("info_title_url")
        public String info_title_url;
        @SerializedName("info_subject")
        public String info_subject;
    }

}
