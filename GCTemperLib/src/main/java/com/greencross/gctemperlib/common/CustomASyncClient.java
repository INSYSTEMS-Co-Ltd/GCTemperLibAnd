package com.greencross.gctemperlib.common;

import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.SFSSLSocketFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.security.KeyStore;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;


public class CustomASyncClient {

	public static AsyncHttpClient client = new AsyncHttpClient();
	
	public CustomASyncClient(){
		try{
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            client.setSSLSocketFactory(sf);
            GLog.i("CustomASyncClient", "dd");
		}catch(Exception e){
			GLog.e(e.toString());
		}
	}
	
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
		client.post(url, params, responseHandler);
	}
	
	/**
	 * API 요청할 때 기본으로 사용되는 파라미터를 설정한다
	 * @param params
	 * @return
	 */
//	public static RequestParams setParams(RequestParams params){
//		params.put(CommonData.JSON_APP_CODE, CommonData.APP_CODE_ANDROID);
//		params.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
//		if(RCApplication.deviceToken != null) {
//			params.put(CommonData.JSON_TOKEN, RCApplication.deviceToken);
//		}
//		params.put(CommonData.JSON_PHONE_MODEL, Build.MODEL);
//		if(!CommonData.getInstance().getAppVersion().equals("")) {
//			params.put(CommonData.JSON_APP_VER, CommonData.getInstance().getAppVersion());
//		}
//
//		return params;
//	}
	
}
