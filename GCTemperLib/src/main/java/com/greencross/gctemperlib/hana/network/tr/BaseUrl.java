package com.greencross.gctemperlib.hana.network.tr;

/**
*/
public class BaseUrl {
	
	public static String COMMON_URL;
	public static void setCommonUrl(boolean isDebug) {
		if(isDebug){
			COMMON_URL = "https://api.devgc.com/hana";
		} else {
			COMMON_URL = "https://wkd.walkie.co.kr/hana";
		}
	}

	public static String HEALTH_BOX_URL = "https://mhanagchealthcare.eiparkclub.com/ExternalLogin/HanaCard?cno=";
	

}