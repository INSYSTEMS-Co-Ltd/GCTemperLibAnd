package com.greencross.gctemperlib;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class TemperGcmVo {

    @SerializedName("message")
    private String message;
    @SerializedName("resultcode")
    private String resultcode;
    @SerializedName("docno")
    private String docno;
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private List<DataEntity> data;

    public TemperGcmVo() {

    }

    public String getMessage() {
        return message;
    }

    public String getResultcode() {
        return resultcode;
    }

    public String getDocno() {
        return docno;
    }

    public int getStatus() {
        return status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        @SerializedName("is_wearable")
        private String isWearable;
        @SerializedName("input_fever")
        private String inputFever;
        @SerializedName("input_de")
        private String inputDe;
        @SerializedName("idx")
        private String idx;

        public String getIsWearable() {
            return isWearable;
        }

        public String getInputFever() {
            return inputFever;
        }

        public String getInputDe() {
            return inputDe;
        }

        public String getIdx() {
            return idx;
        }
    }


}
