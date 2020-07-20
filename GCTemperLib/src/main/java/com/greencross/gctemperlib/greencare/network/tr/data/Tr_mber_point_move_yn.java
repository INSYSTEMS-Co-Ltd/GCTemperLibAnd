package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 *
 * 포인트 전환하기
 *
 내정보에서 푸시여부설정
 //페이징 되어야 함.

 <요청값> user_point_amt : 전환할 포인트값

 // 주의 : 보유할 포인트 보다 큰값을 보내지 않도록 할것!!! (두대의 스마트폰에서 동시에 할수는 보낼 수는 있지만, 그래도 보유포인트보다 큰폰인트가 전송안되어야함.)
 */

public class Tr_mber_point_move_yn extends BaseData {
    private final String TAG = Tr_mber_point_move_yn.class.getSimpleName();

    public static class RequestData {

        public String mber_sn;
        public String user_point_amt;

    }


    public Tr_mber_point_move_yn() {
        super.conn_url = BaseUrl.COMMON_URL;
    }

    @Override
    public JSONObject makeJson(Object obj) throws JSONException {
        Logger.i(TAG, "makeJson.obj=" + obj);
        if (obj instanceof Tr_mber_point_move_yn.RequestData) {
            JSONObject body = new JSONObject();
            Tr_mber_point_move_yn.RequestData data = (Tr_mber_point_move_yn.RequestData) obj;

            body.put("api_code", getApiCode("Tr_mber_point_move_yn") ); //
            body.put("insures_code", INSURES_CODE);
            body.put("mber_sn", data.mber_sn); //  1000
            body.put("user_point_amt",  data.user_point_amt); //

            return body;
        }

        return super.makeJson(obj);
    }


    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/

    @SerializedName("api_code") // mber_main_call",
    public String api_code; //
    @SerializedName("insures_code") // 303",
    public String insures_code; //
    @SerializedName("reg_yn") // 1344",
    public String reg_yn; //
}
