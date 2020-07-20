package com.greencross.gctemperlib;

public interface IGCResult {
    void onResult(boolean isSuccess, String message, Object data);
}
