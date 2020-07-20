package com.greencross.gctemperlib.collection;

/**
 * Created by MobileDoctor on 2017-03-21.
 */

public class RemedyItem {
    private String mRemedySn;
    private String mInputDe;
    private String mInputKind;      // 성분
    private String mInputType;      // 제형
    private String mInputVolume;

    public String getmRemedySn() {
        return mRemedySn;
    }

    public void setmRemedySn(String mRemedySn) {
        this.mRemedySn = mRemedySn;
    }

    public String getmInputDe() {
        return mInputDe;
    }

    public void setmInputDe(String mInputDe) {
        this.mInputDe = mInputDe;
    }

    public String getmInputKind() {
        return mInputKind;
    }

    public void setmInputKind(String mInputKind) {
        this.mInputKind = mInputKind;
    }

    public String getmInputType() {
        return mInputType;
    }

    public void setmInputType(String mInputType) {
        this.mInputType = mInputType;
    }

    public String getmInputVolume() {
        return mInputVolume;
    }

    public void setmInputVolume(String mInputVolume) {
        this.mInputVolume = mInputVolume;
    }
}
