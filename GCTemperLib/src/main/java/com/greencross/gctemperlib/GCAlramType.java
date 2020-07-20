package com.greencross.gctemperlib;

public enum GCAlramType {

    GC_ALRAM_TYPE_독려("TYPE1", "측정 독려"),
    GC_ALRAM_TYPE_지역("TYPE2", "지역 체온");

    private String alramName;
    private String desc;
    GCAlramType(String alramName, String desc) {
        this.alramName = alramName;
        this.desc = desc;
    }

    public String getAlramName() {
        return alramName;
    }

    public String getDesc() {
        return desc;
    }
}
