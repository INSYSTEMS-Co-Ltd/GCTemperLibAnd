package com.greencross.gctemperlib.collection;

public class Fever {
	String baby_id;	// 앱 내 아기 id
	String fever;	// 체온
	String date;	// 측정 날짜 - 시간
	String event_case;	// 이벤트 케이스
	
	public Fever(String baby_id, String fever, String date, String event_case){
		this.baby_id = baby_id;
		this.fever = fever;
		this.date = date;
		this.event_case = event_case;
	}
}
