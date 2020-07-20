package com.greencross.gctemperlib.collection;

/**
 * Created by MobileDoctor on 2016-05-11.
 */
public class MemoItem {
    public int memo_id = 0; // 메모 id
    public String baby_id;
    public int type;        // 1: 증상, 2 : 항생제, 3: 병원진단, 4: 오늘 일, 5: 예방접종
    public int kind;
    public String memo;
    public String date;

    public MemoItem(String baby_id, int type, int kind, String memo, String date){
        this.baby_id = baby_id;
        this.type = type;
        this.kind = kind;
        this.memo = memo;
        this.date = date;
    }
}
