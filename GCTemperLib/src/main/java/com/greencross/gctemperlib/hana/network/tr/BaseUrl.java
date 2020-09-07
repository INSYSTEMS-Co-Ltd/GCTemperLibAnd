package com.greencross.gctemperlib.hana.network.tr;

/**
*/
public class BaseUrl {
	
	public static boolean DEBUG_MODE = false;
	public static String COMMON_URL;

	static {
		// https://api.devgc.com/hana 테스트 주소
		// https://wkd.walkie.co.kr/hana 운영서버 주소
		if(DEBUG_MODE){
            COMMON_URL = "https://api.devgc.com/hana";
		} else {
            COMMON_URL = "https://api.devgc.com/hana";
		}
	}

	public static String HEALTH_BOX_URL = "http://hanagchealthcare.eiparkclub.com";
	

}