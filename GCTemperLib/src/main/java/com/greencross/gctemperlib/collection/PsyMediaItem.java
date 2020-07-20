package com.greencross.gctemperlib.collection;

/**
 * Created by jihoon on 2018-04-01.
 * 음원 Item
 * @since 0, 1
 */
public class PsyMediaItem {

    private String mMlSn;
    private String mImg;
    private String mUrl;
    private String mTitle;
    private String mContent;
    private String mWr;
    private String mTotpg;

    /**
     * 음원
     * @param ml_sn    S/N
     * @param img 이미지
     * @param i_url   음원URL
     * @param tit   제목
     * @param con   내용
     * @param wr    이름
     * @param totpg
     */
    public PsyMediaItem(String ml_sn,
                        String img,
                        String i_url,
                        String tit,
                        String con,
                        String wr,
                        String totpg){
        this.mMlSn = ml_sn;
        this.mImg = img;
        this.mUrl = i_url;
        this.mTitle = tit;
        this.mContent = con;
        this.mWr = wr;
        this.mTotpg = totpg;
    }

    public String getmMlSn() {
        return mMlSn;
    }

    public void setmMlSn(String mMlSn) {
        this.mMlSn = mMlSn;
    }

    public String getmImg() {
        return mImg;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmWr() {
        return mWr;
    }

    public void setmWr(String mWr) {
        this.mWr = mWr;
    }

    public String getmTotpg() {
        return mTotpg;
    }

    public void setmTotpg(String mTotpg) {
        this.mTotpg = mTotpg;
    }

}
