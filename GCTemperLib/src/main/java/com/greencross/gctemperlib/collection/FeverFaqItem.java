package com.greencross.gctemperlib.collection;

/**
 * Created by jihoon on 2016-04-18.
 * 성장 FAQ 리스트 데이터
 * @since 0, 1
 */
public class FeverFaqItem {

    private String mFaqSn;
    private String mFaqQestn;
    private String mFaqAnswer;

    /**
     * 열나요 FAQ
     * @param faq_sn    faq 고유번호
     * @param faq_qestn 제목
     * @param faq_answer    내용
     */
    public FeverFaqItem(String faq_sn,
                        String faq_qestn,
                        String faq_answer){
        this.mFaqSn =   faq_sn;
        this.mFaqQestn= faq_qestn;
        this.mFaqAnswer=faq_answer;
    }

    public String getmFaqSn() {
        return mFaqSn;
    }

    public void setmFaqSn(String mFaqSn) {
        this.mFaqSn = mFaqSn;
    }

    public String getmFaqQestn() {
        return mFaqQestn;
    }

    public void setmFaqQestn(String mFaqQestn) {
        this.mFaqQestn = mFaqQestn;
    }

    public String getmFaqAnswer() {
        return mFaqAnswer;
    }

    public void setmFaqAnswer(String mFaqAnswer) {
        this.mFaqAnswer = mFaqAnswer;
    }
}
