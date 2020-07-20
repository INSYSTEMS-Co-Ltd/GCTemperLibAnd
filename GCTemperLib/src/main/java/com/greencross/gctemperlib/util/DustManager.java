package com.greencross.gctemperlib.util;

import java.util.HashMap;
import java.util.Map;

public class DustManager {

    public static String mCityName = "";

    private static Map<String, String> CITY_MAP = null;

	static {
		CITY_MAP = new HashMap<String, String>();
		CITY_MAP.put("서울", "1");
		CITY_MAP.put("부산", "2");
		CITY_MAP.put("대구", "3");
		CITY_MAP.put("인천", "4");
		CITY_MAP.put("광주", "5");
		CITY_MAP.put("대전", "6");
		CITY_MAP.put("울산", "7");
		CITY_MAP.put("경기", "8");
		CITY_MAP.put("강원", "9");
		CITY_MAP.put("충북", "10");
		CITY_MAP.put("충남", "11");
		CITY_MAP.put("전북", "12");
		CITY_MAP.put("전남", "13");
		CITY_MAP.put("경북", "14");
		CITY_MAP.put("경남", "15");
		CITY_MAP.put("제주", "16");
	}
	
	public static String getCityNumber(String cityKrName) {
		return CITY_MAP.get(cityKrName);
	}
	
	public static String checkCity(String cityKrName) {
        if(cityKrName.contains("서울특별시")){
            return "서울";
        }else if(cityKrName.contains("경기도")){
			return "경기";
		}else if(cityKrName.contains("강원도")){
			return "강원";
		}else if(cityKrName.contains("충청북도")){
			return "충북";
		}else if(cityKrName.contains("충청남도")){
			return "충남";
		}else if(cityKrName.contains("전라북도")){
			return "전북";
		}else if(cityKrName.contains("전라남도")){
			return "전남";
		}else if(cityKrName.contains("경상북도")){
			return "경북";
		}else if(cityKrName.contains("경상남도")){
			return "경남";
		}else if(cityKrName.contains("제주")){
			return "제주";
		}else
			return cityKrName;
	}
}
