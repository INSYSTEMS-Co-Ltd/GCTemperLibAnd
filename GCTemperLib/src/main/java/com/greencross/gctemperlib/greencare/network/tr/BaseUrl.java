package com.greencross.gctemperlib.greencare.network.tr;

/**
*/
public class BaseUrl {
	
	public static  boolean RELEASE_MODE = false;
//	public static  boolean RELEASE_MODE = true; 

	public static String COMMON_URL;
	public static String API_URL = "";
//	public static String FOOD_IMAGE_URL ="";

	static {
		if(RELEASE_MODE){
            COMMON_URL = "https://wkd.walkie.co.kr/HL_FV/HL_Mobile_Call.asmx/mobile_Call";

		} else {
            COMMON_URL = "https://wkd.walkie.co.kr/HL_FV/HL_Mobile_Call.asmx/mobile_Call";
		}
//		FOOD_IMAGE_URL ="http://wkd.walkie.co.kr/HS_HL/UPLOAD/SK_FOOD/";
		
	}
	

}