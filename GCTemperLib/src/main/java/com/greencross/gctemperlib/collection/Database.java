package com.greencross.gctemperlib.collection;

import java.util.ArrayList;

public class Database {
	String Aid;							// 안드로이드 디바이스별 고유 키값

	String loc_1;						// 위도
	String loc_2;						// 경도
	String add_1;
	String add_2;
	String add_3;
	String add_4;

	ArrayList<MemoItem> MemoList;		// 메모 목록 - 유일하게 기존 MemoItem 객체를 공통으로 사용
	ArrayList<Fever> FeverList;		// 체온 목록

	public Database(String _Aid, String loc_1, String loc_2, String address_1, String address_2, String address_3, String address_4){
		this.Aid = _Aid;
		this.loc_1 = loc_1;
		this.loc_2 = loc_2;
		this.add_1 = address_1;
		this.add_2 = address_2;
		this.add_3 = address_3;
		this.add_4 = address_4;
	}

	public Database(String _Aid, ArrayList<Fever> feverList, ArrayList<MemoItem> memoList){
		this.Aid = _Aid;
		this.FeverList = feverList;
		this.MemoList = memoList;
	}
}
