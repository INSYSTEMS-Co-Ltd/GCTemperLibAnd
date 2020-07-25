package com.greencross.gctemperlib.greencare.network.tr;

/**
*/
public class BaseUrl {
	
	public static  boolean RELEASE_MODE = false;
	public static String COMMON_URL;

	static {
		if(RELEASE_MODE){
            COMMON_URL = "https://api.devgc.com/hana/Member/v1/Login";

		} else {
            COMMON_URL = "https://api.devgc.com/hana/Member/v1/Login";
		}
//		FOOD_IMAGE_URL ="http://wkd.walkie.co.kr/HS_HL/UPLOAD/SK_FOOD/";
		
	}
	

}