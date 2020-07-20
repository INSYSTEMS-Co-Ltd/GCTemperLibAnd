package com.greencross.gctemperlib.greencare.network.tr;

import android.text.TextUtils;
import android.util.Log;

import com.greencross.gctemperlib.greencare.base.value.Define;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_get_infomation;
import com.greencross.gctemperlib.greencare.util.JsonLogPrint;
import com.greencross.gctemperlib.greencare.util.Logger;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionUtil {
    private final String TAG = ConnectionUtil.class.getSimpleName();

    private String mUrl;
    private StringBuffer paramSb = new StringBuffer();

    public ConnectionUtil() {
    }

    public ConnectionUtil(String url) {
        mUrl = url;
    }


    public String getParam() {
        String params = paramSb.toString();
        if ("".equals(params.trim()) == false)
            params = params.substring(0, params.length() - 1);

        return params;
    }


    public String doConnection(JSONObject body, String hdParams, String name, BaseData tr) {
        URL mURL;
        HttpURLConnection conn = null;
        int mIntResponse = 0;
        String result = "";
        try {
            Tr_get_infomation info = Define.getInstance().getInformation();
            if (info != null && TextUtils.isEmpty(info.apiURL) == false) {
                mURL = new URL(info.apiURL);
            } else {
                mURL = new URL(BaseUrl.COMMON_URL);
            }
            if(name.equals(""))
                mURL = new URL(BaseUrl.COMMON_URL);
            else
                mURL = new URL(name);

            conn = (HttpURLConnection) mURL.openConnection();
            conn.setConnectTimeout(3 * 1000);
            conn.setReadTimeout(3 * 1000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
//            conn.setRequestProperty("Accept-Charset", "euc-kr");

            Log.i(TAG, "###############  ConnectionUtil."+name+"  ###############");
            Log.i(TAG, "url=" + mURL);
            JsonLogPrint.printJson(body.toString());
            Log.i(TAG, "###############  ConnectionUtil."+name+"  ###############");

            OutputStream os = conn.getOutputStream();
            os.write((tr.json_obj_name+"=").getBytes("UTF-8"));
//            os.write("json=".getBytes( "UTF-8"));           // json={key,value...} 형태로 파라메터 입력
//            os.write("&member_id=0&device_type=A&session_code=&store_id=1&app_ver=2.2".getBytes("EUC-KR"));
            os.write(body.toString().getBytes("UTF-8"));   // 현대해상 parameter
            hdParams = "&"+hdParams;
            os.write(hdParams.getBytes("UTF-8"));
            os.flush();
            os.close();
            mIntResponse = conn.getResponseCode();
        } catch (Exception e) {
            Log.d("hsh", "getJSONData ex" + e);
            mIntResponse = 1000;
        }

        if (mIntResponse == HttpURLConnection.HTTP_OK) {
            BufferedReader br;
            InputStream is = null;
            try {
                is = new BufferedInputStream(conn.getInputStream());
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line+ "\n");
                }

                br.close();

                result = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
        // 불필요 부분 제거
        if (TextUtils.isEmpty(result) == false) {
            int startSub = result.indexOf("{");
            if (startSub != -1) {
                result = result.substring(startSub).replace("</string>", "");
            }
            Logger.i(TAG, "doConnection.result="+result);
        }

        return result;
    }

}
