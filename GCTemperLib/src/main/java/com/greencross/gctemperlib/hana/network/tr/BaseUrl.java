package com.greencross.gctemperlib.hana.network.tr;

/**
*/
public class BaseUrl {
	
	public static  boolean RELEASE_MODE = false;
	public static String COMMON_URL;

	static {
		// https://api.devgc.com/hana 테스트 주소
		// https://wkd.walkie.co.kr/hana 운영서버 주소
		if(RELEASE_MODE){
            COMMON_URL = "https://api.devgc.com/hana";

		} else {
            COMMON_URL = "https://api.devgc.com/hana";
		}
//		FOOD_IMAGE_URL ="http://wkd.walkie.co.kr/HS_HL/UPLOAD/SK_FOOD/";
		
	}
	

}