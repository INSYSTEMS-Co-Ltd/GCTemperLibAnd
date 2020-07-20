package com.greencross.gctemperlib.collection;

/**
 * Created by jihoon on 2018-04-01.
 * 음원 Item
 * @since 0, 1
 */
public class PsyImgResultItem {

    private String mThumb;
    private String mNo;
    private String mSel;
    private String mContent;

    /**
     * 심리 결과
     * @param mThumb
     * @param mNo
     * @param mSel
     * @param mContent
     */
    public PsyImgResultItem(String mThumb,
                            String mNo,
                            String mSel,
                            String mContent){
        this.mThumb = mThumb;
        this.mNo = mNo;
        this.mSel = mSel;
        this.mContent = mContent;
    }


    public String getmThumb() {
        return mThumb;
    }

    public void setmThumb(String mThumb) {
        this.mThumb = mThumb;
    }

    public String getmNo() {
        return mNo;
    }

    public void setmNo(String mNo) {
        this.mNo = mNo;
    }

    public String getmSel() {
        return mSel;
    }

    public void setmSel(String mSel) {
        this.mSel = mSel;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }



}
