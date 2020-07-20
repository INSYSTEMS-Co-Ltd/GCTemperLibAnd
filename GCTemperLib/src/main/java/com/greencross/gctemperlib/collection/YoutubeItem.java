package com.greencross.gctemperlib.collection;

/**
 * Created by MobileDoctor on 2017-08-28.
 */

public class YoutubeItem {
    private String info_subject;
    private String info_title_url;
    private String view_day;

    public YoutubeItem(String info_subject, String info_title_url, String view_day) {
        this.info_subject = info_subject;
        this.info_title_url = info_title_url;
        this.view_day = view_day;
    }

    public String getInfo_subject() {

        return info_subject;
    }

    public void setInfo_subject(String info_subject) {
        this.info_subject = info_subject;
    }

    public String getInfo_title_url() {
        return info_title_url;
    }

    public void setInfo_title_url(String info_title_url) {
        this.info_title_url = info_title_url;
    }

    public String getView_day() {
        return view_day;
    }

    public void setView_day(String view_day) {
        this.view_day = view_day;
    }
}
