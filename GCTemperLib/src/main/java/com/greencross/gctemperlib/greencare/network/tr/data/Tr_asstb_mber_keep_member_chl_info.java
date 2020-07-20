package com.greencross.gctemperlib.greencare.network.tr.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *
 미션  리스트  (전체,방문,혈당,건강,병원,설문 종류에따른 포인트 이력)
 json={"api_code":"asstb_mber_keep_member_chl_info"
 ,"insures_code":"108"
 ,"mber_sn":"115232"
 ,"mber_no":"9999999999970"
 }

 "api_code": "asstb_mber_keep_member_chl_info",
 "chldrn": [
 {
 "mber_no": "9999999999970",
 "full_join_sn": "223256",
 "full_chldrn_joinserial": "L0171074516300003",
 "full_chldrn_nm": "김시우",
 "full_chldrn_sex": "1",
 "full_chldrn_lifyea": "20171001",
 "full_chldrn_aft_lifyea": "20171001 ",
 "full_chl_exist_yn": "Y"
 },
 {
 "mber_no": "9999999999970",
 "full_join_sn": "224302",
 "full_chldrn_joinserial": "L0171077777700002",
 "full_chldrn_nm": "태아",
 "full_chldrn_sex": "2",
 "full_chldrn_lifyea": "00000000",
 "full_chldrn_aft_lifyea": "20190120 ",
 "full_chl_exist_yn": "N"
 }
 ],
 "api_code": "asstb_mber_keep_member_chl_info",
 "insures_code": "108",
 "data_yn": "Y"

 */

public class Tr_asstb_mber_keep_member_chl_info {
    private final String TAG = Tr_asstb_mber_keep_member_chl_info.class.getSimpleName();


    /**************************************************************************************************/
    /***********************************************RECEIVE********************************************/
    /**************************************************************************************************/
    @SerializedName("api_code")
    public String api_code; //
    @SerializedName("insures_code")
    public String insures_code; //
    @SerializedName("data_yn")
    public String data_yn; //
    @SerializedName("chldrn")
    public List<chldrn> chldrn_list = new ArrayList<>(); //

    public class chldrn {
        @SerializedName("mber_no") // 9999999999970",
        public String mber_no;
        @SerializedName("full_join_sn") // 223256",
        public String full_join_sn;
        @SerializedName("full_chldrn_joinserial") // L0171074516300003",
        public String full_chldrn_joinserial;
        @SerializedName("full_chldrn_nm") // 김시우",
        public String full_chldrn_nm;
        @SerializedName("full_chldrn_sex") // 1",
        public String full_chldrn_sex;
        @SerializedName("full_chldrn_lifyea") // 20171001",
        public String full_chldrn_lifyea;
        @SerializedName("full_chldrn_aft_lifyea") // 20171001",
        public String full_chldrn_aft_lifyea;
        @SerializedName("full_chl_exist_yn") // 0",
        public String full_chl_exist_yn;
    }

}
