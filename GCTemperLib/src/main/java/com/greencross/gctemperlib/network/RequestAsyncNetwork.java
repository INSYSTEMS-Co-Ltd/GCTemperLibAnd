package com.greencross.gctemperlib.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.util.JsonLogPrint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


/**
 * Created by jihoon on 2016-01-04.
 * 네트워크 요청 쓰레드 클래스
 * @since 0, 1
 */
public class RequestAsyncNetwork extends Thread {
    private Context mContext;
    private int			mType;
    private String      mUrl;
    private HttpUtil	mHttpUtil;
    private CustomAsyncListener mAsyncListener;
    private String		mParams;
    private ArrayList<NameValuePair> mNameValuePair;
    private HashMap<String, String> fileParams;
    private ArrayList<String> ArrParams;
    private Hashtable<String ,String> hashTable;

    private String		response	=	"";

    /**
     * HttpURLConnection 에서 사용
     * @param context
     * @param type
     * @param listener
     * @param params
     */
    public RequestAsyncNetwork(Context context , int type , CustomAsyncListener listener, HashMap<String, String> params)
    {
        this.mContext			= context;
        this.mType				= type;
        this.mAsyncListener 	= listener;

        this.mParams			= getParams(params);

        mHttpUtil				= new HttpUtil();
    }

    /**
     * HttpClient 에서 사용
     * @param context
     * @param type
     * @param listener
     * @param params
     */
    /*
    public RequestAsyncNetwork(Context context , int type , CustomAsyncListener listener, ArrayList<NameValuePair> params)
    {
        this.mContext			= context;
        this.mType				= type;
        this.mAsyncListener 	= listener;

        this.mNameValuePair		= getParams(params);

        mHttpUtil				= new HttpUtil();
    }
    */

    /**
     * 비동기 네트워크
     * @param context   컨텍스트
     * @param type  API 구분타입
     * @param url   서버 url
     * @param listener  리스너
     * @param params    파라미터
     */
    public RequestAsyncNetwork(Context context, int type, String url, CustomAsyncListener listener, ArrayList<NameValuePair> params){
        this.mContext			= context;
        this.mType           =  type;
        this.mUrl				= url;
        this.mAsyncListener 	= listener;

        this.mNameValuePair		= getParams(params);

        mHttpUtil				= new HttpUtil();
    }

    /**
     * 비동기 네트워크 ( 파일업로드 )
     * @param context   컨텍스트
     * @param type  API 구분타입
     * @param url   서버 url
     * @param listener  리스너
     * @param params    파라미터
     */
    public RequestAsyncNetwork(Context context, int type, String url, CustomAsyncListener listener, HashMap<String, String> params){
        this.mContext			= context;
        this.mType           =  type;
        this.mUrl				= url;
        this.mAsyncListener 	= listener;
        this.fileParams     =   params;

        mHttpUtil				= new HttpUtil();
    }

    public RequestAsyncNetwork(Context context, int type, String url, CustomAsyncListener listener, Hashtable<String, String> params){
        this.mContext			= context;
        this.mType           =  type;
        this.mUrl				= url;
        this.mAsyncListener 	= listener;
        this.hashTable     =   params;

        mHttpUtil				= new HttpUtil();
    }

    @SuppressLint("HandlerLeak")
//	private final Handler mNetworkHandler = new Handler()
    // UI 작업을 처리하기 위해서는 아래와 같이 UI쓰레드에 바인딩된 Handler를 만들어야 합니다.
    private final Handler mNetworkHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            CustomAlertDialog dialog = null;

            GLog.i("msg.what = " + msg.what, "dd");
            try {

                switch (msg.what) {

                    case -2: // 실행 종료 요청 (인터럽트)
                        mAsyncListener.onStop(mContext, mType);
                        break;

                    case 0: // 데이터 가져오기 성공

                        JSONObject resultObject = null;
                        JSONObject dataObject = null;
                        JSONObject alertObject = null;

                        int resultCode = -1;

                        try {

                            if (response.startsWith("<")) {   // api result 문자열 시작값이 "<" 로 시작하는 경우는 xml 폼
                                try {
                                    response = Util.parseXml(response);
//                                    GLog.i("response = " + response, "dd");
                                    JsonLogPrint.printJson(response);
                                } catch (Exception e) {
                                    GLog.e(e.toString());
                                }

                                resultObject = new JSONObject(response);

                                if (!resultObject.isNull(CommonData.JSON_REG_YN)) {
                                    String reg_yn = resultObject.getString(CommonData.JSON_REG_YN);
                                    if (reg_yn.equals(CommonData.YES)) {
                                        resultCode = 0;
                                    } else {
                                        resultCode = 1;
                                    }
                                } else {
                                    resultCode = 0;
                                }


                                dataObject = resultObject;
                            } else {
                                resultObject = new JSONObject(response);

                                resultCode = resultObject.getInt(CommonData.JSON_RESULT_CODE);
                                dataObject = resultObject.getJSONObject(CommonData.JSON_DATA);
                            }


                            dialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_A);
                            dialog.setTitle("error");
                            dialog.setContent("에러처리하세요");
                            dialog.setPositiveButton(mContext.getResources().getString(R.string.popup_dialog_button_confirm), null);

                            // result code 정의 작업


                            mAsyncListener.onPost(mContext, mType, resultCode, dataObject, dialog);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            dialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_A);
                            dialog.setTitle(mContext.getResources().getString(R.string.popup_dialog_data_error_title));
                            dialog.setContent(mContext.getResources().getString(R.string.popup_dialog_server_error_content));
                            dialog.setPositiveButton(mContext.getResources().getString(R.string.popup_dialog_button_confirm), null);

                            mAsyncListener.onDataError(mContext, mType, response, dialog);
                        }

                        break;

                    default: // 네트워크 오류

                        dialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_A);

                        // 와이파이 연결 오류
                        if (msg.what == -1) {
                            dialog.setTitle(mContext.getResources().getString(R.string.popup_dialog_netword_error_title));
                            dialog.setContent(mContext.getResources().getString(R.string.popup_dialog_netword_error_content));
                            dialog.setPositiveButton(mContext.getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                                @Override
                                public void onClick(CustomAlertDialog dialog, Button button) {
                                    // TODO Auto-generated method stub
                                    Util.sendBroadCast(mContext, CommonData.BROADCAST_ACTIVITY_FINISH);
                                    dialog.dismiss();
                                }
                            });
                        } else {

                            dialog.setTitle(mContext.getResources().getString(R.string.popup_dialog_netword_error_title));
                            dialog.setContent(mContext.getResources().getString(R.string.popup_dialog_netword_error_content));
                            dialog.setPositiveButton(mContext.getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                                @Override
                                public void onClick(CustomAlertDialog dialog, Button button) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });

                        }

                        mAsyncListener.onNetworkError(mContext, mType, mHttpUtil.responseResultCode, dialog);

                        break;
                }
            }catch(Exception e){
                GLog.e(e.toString());
            }

        }
    };

    /**
     * HttpUrlConnection 에서 사용
     * @param body  파라미터
     * @return
     */
    public String getParams(HashMap<String, String> body) {

        if ( body == null )
            body = new HashMap<String, String>();


        if ( !body.containsKey("member_id") )
            body.put("member_id", CommonData.getInstance(mContext).getMemberId()+"");

        if ( !body.containsKey("device_type") )
            body.put("device_type", "A");

        if ( !body.containsKey("session_code") )
            body.put("session_code", CommonData.getInstance(mContext).getSessionCode());

        if ( !body.containsKey("store_id") )
            body.put("store_id", NetworkConst.getInstance().getMarketId()+"");

        if ( !body.containsKey("app_ver")){	// app_ver 이 없다면
            if(!CommonData.getInstance(mContext).getAppVersion().equals("")){	// app_ver 이 공백이 아니라면
                body.put("app_ver", CommonData.getInstance(mContext).getAppVersion());
            }else{													// app_ver 이 공백이라면

                try {
                    PackageInfo pi = mContext.getPackageManager().getPackageInfo( mContext.getPackageName(), 0 );
                    body.put("app_ver", pi.versionName.toString());
                } catch (Exception e) {
                    GLog.e(e.toString());
                }

            }

        }

        String result = "?";

        Iterator<String> iterator = body.keySet().iterator();

        while (iterator.hasNext()) {

            String key = (String) iterator.next();

            GLog.v("Params Key : " + key + ", Value : " + body.get(key));

            String value = "";

            try {
//                value = URLEncoder.encode(body.get(key), "UTF-8");
                value = URLEncoder.encode(body.get(key), "euc-kr");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            result += "&" + key + "=" + value;

        }

        if ( result.length() > 3 ) {
            result = result.replace("?&", "");
        }

        return result;
    }

    /**
     * HttpClient 에서 사용
     * @param body  네트워크 파라미터
     * @return HttpClient request 에 필요한 param 이 누락되어 있으면 추가하여 돌려준다.
     */
    public ArrayList<NameValuePair> getParams(ArrayList<NameValuePair> body) {
        boolean isMember		= false;
        boolean isDeviceType	= false;
        boolean isSessionCode	= false;
        boolean isStoreId		= false;
        boolean isAppver		= false;

        if ( body == null || body.size() == 0 ){
            body = new ArrayList<NameValuePair>();
            body.add(new BasicNameValuePair("member_id", String.valueOf(CommonData.getInstance(mContext).getMemberId())));
        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("member_id")){
                if(i == (body.size()-1) && isMember == false){
                    body.add(new BasicNameValuePair("member_id", String.valueOf(CommonData.getInstance(mContext).getMemberId())));
                }
            }else{
                isMember = true;
            }
        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("device_type")){
                if(i == (body.size()-1) && isDeviceType == false){
                    body.add(new BasicNameValuePair("device_type", "A"));
                }
            }else{
                isDeviceType = true;
            }
        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("session_code")){
                if(i == (body.size()-1) && isSessionCode == false){
                    body.add(new BasicNameValuePair("session_code", CommonData.getInstance(mContext).getSessionCode()));
                }
            }else{
                isSessionCode = true;
            }
        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("store_id")){
                if(i == (body.size()-1) && isStoreId == false){
                    body.add(new BasicNameValuePair("store_id", String.valueOf(NetworkConst.getInstance().getMarketId())));
                }
            }else{
                isStoreId = true;
            }
        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("app_ver")){
                if(i == (body.size()-1) && isAppver == false){
                    if(!CommonData.getInstance(mContext).getAppVersion().equals("")){	// app_ver 이 공백이 아니라면
                        body.add(new BasicNameValuePair("app_ver", CommonData.getInstance(mContext).getAppVersion()));
                    }else{													// app_ver 이 공백이라면
                        try {
                            PackageInfo pi = mContext.getPackageManager().getPackageInfo( mContext.getPackageName(), 0 );
                            body.add(new BasicNameValuePair("app_ver", pi.versionName.toString()));
                        } catch (Exception e) {
                            GLog.e(e.toString());
                        }
                    }
                }
            }else{
                isAppver = true;
            }
        }

        return body;
    }

    @Override
    public void run()
    {
        super.run();

        boolean bNetworkRequest = false;

        Message msg = Message.obtain();

        try {

            bNetworkRequest = requestNetwork();

        }
        catch (Exception e) {
            e.getStackTrace();
            GLog.e("InterruptedException === " + e.getMessage());
        }

        if ( interrupted() ) {
            msg.what = -2;
        }
        else {

            GLog.i("bNetworkRequest = " + bNetworkRequest, "dd");

            if ( bNetworkRequest )  {

                // 네트워크 관련 오류가 있는지 확인
                if ( mHttpUtil.responseResultCode == HttpURLConnection.HTTP_OK ) {
                    GLog.i("msg.what = 0", "dd");
                    msg.what = 0;
                }
                else {
                    GLog.i("msg.what = " +mHttpUtil.responseResultCode, "dd");
                    msg.what = mHttpUtil.responseResultCode;
                }

            }
            else {
                GLog.i("msg.what = -1", "dd");
                msg.what = -1;
            }

        }

        mNetworkHandler.sendMessage(msg);


    }

    /**
     * 네트워크 url 세팅
     * @param url api url
     */
    private void setNetworkHttp(String url) {

//        if(mType == NetworkConst.NET_SET_UPLOAD){   // 식사등록 API ( 문자열 + 파일 업로드 )
//            response	=	mHttpUtil.HttpFileUploads(url, fileParams);
//        }else {
//            response = mHttpUtil.requestHttpClient(url, mNameValuePair);
//        }

        switch (mType){
            case NetworkConst.NET_CHLDRN_NOTE_UPLOAD:   // 육아 & 병력 노트 새로 글쓰기
                response	=	mHttpUtil.uploadFile(url, hashTable.get(CommonData.JSON_UPFILE), hashTable);
                break;
            default:    // 그외 네트워크
                response = mHttpUtil.requestHttpClient(url, mNameValuePair);
                break;
        }

        GLog.i("Thread type = " +mType +"response = " +response, "dd");
    }

    /**
     * 네트워크 체크
     * @return boolean 네트워크 상태 리턴
     */
    private boolean checkNetwork() {

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean isWifiAvail = ni.isAvailable();
        boolean isWifiConn = ni.isConnected();

        ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isMobileAvail = ni.isAvailable();
        boolean isMobileConn = ni.isConnected();


        String status = "\nWiFi Avail = " + isWifiAvail + ", Conn = "
                + isWifiConn + "\nMobile Avail = " + isMobileAvail
                + ", Conn = " + isMobileConn;

        GLog.e("Network Status : " + status);

        if (!isWifiConn && !isMobileConn) {
            return false;
        }

        return true;

    }

    /**
     * 네트워크 활성화 체크
     * @return  boolean ( true - 네트워크 사용중, false - 네트워크 미사용 )
     */
    private boolean isNetworkStat() {
        try{
            ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
            boolean blte_4g = false;
            if(lte_4g != null)
                blte_4g = lte_4g.isConnected();
            if( mobile != null ) {
                if (mobile.isConnected() || wifi.isConnected() || blte_4g)
                    return true;
            } else {
                if ( wifi.isConnected() || blte_4g )
                    return true;
            }
        }catch(Exception e){
            GLog.e(e.toString());
        }

        return false;
    }

    /**
     * 네트워크 상태 체크
     * @return  boolean ( true - 네트워크 사용중, false - 네트워크 미사용 )
     * @throws InterruptedException
     */
    private boolean requestNetwork() throws InterruptedException {

        this.response	=	"";

        // 네트워크 상태확인
//		if ( !checkNetwork() ){
        if ( !isNetworkStat()){
            return false;
        }

        /* 2015-10-23 api 호출시 네트워크 타입 제거
        this.setNetworkHttp(mType);
        */
        this.setNetworkHttp(mUrl);
        GLog.i("return = true ", "dd");
        return true;
    }
}
