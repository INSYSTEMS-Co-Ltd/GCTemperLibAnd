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
 건강 식단  리스트 (제목검색)
 */

public class Tr_content_special_bbslist_search_food extends BaseData {
    private final String TAG = Tr_content_special_bbslist_search_food.class.getSimpleName();

    public static class RequestData {

        public String bbs_title;
        public String pageNumber;

    }

    public Tr_content_special_bbslist_search_food() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_content_special_bbslist_search_food.RequestData) {
            JSONObject body = new JSONObject();
            Tr_content_special_bbslist_search_food.RequestData data = (Tr_content_special_bbslist_search_food.RequestData) obj;

            body.put("api_code", getApiCode("Tr_content_special_bbslist_search_food") );
            body.put("insures_code", INSURES_CODE);
            body.put("bbs_title", data.bbs_title);
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
    public String pageNumber; //
    @SerializedName("maxpageNumber")
    public String maxpageNumber; //

    @SerializedName("bbslist")
    public List<Bbs> bbslist = new ArrayList<>(); //

    public class Bbs {
        @SerializedName("cmpny_code")
        public String cmpny_code;
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
