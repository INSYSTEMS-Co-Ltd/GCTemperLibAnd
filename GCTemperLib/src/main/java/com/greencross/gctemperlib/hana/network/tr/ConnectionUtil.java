package com.greencross.gctemperlib.hana.network.tr;

import android.text.TextUtils;
import android.util.Log;

import com.greencross.gctemperlib.greencare.util.JsonLogPrint;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ConnectionUtil {
    private final String TAG = ConnectionUtil.class.getSimpleName();

    private String mUrl;
    private StringBuffer paramSb = new StringBuffer();

    public ConnectionUtil(String url) {
        mUrl = url;
    }

    public String doConnection(JSONObject body, String name, BaseData tr) throws Exception {
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            String result = "";
            URL url = new URL(mUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1 * 1000);
            conn.setReadTimeout(2 * 1000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
//            conn.setRequestProperty("Accept-Charset", "euc-kr");

            Log.i(TAG, "###############  ConnectionUtil." + name + "  ###############");
            Log.i(TAG, "url=" + url);
            JsonLogPrint.printJson(body.toString());
            Log.i(TAG, "###############  ConnectionUtil." + name + "  ###############");

            os = conn.getOutputStream();
            os.write((tr.json_obj_name + "=").getBytes("UTF-8"));
//            os.write("json=".getBytes( "UTF-8"));           // json={key,value...} 형태로 파라메터 입력
//            os.write("&member_id=0&device_type=A&session_code=&store_id=1&app_ver=2.2".getBytes("EUC-KR"));
            os.write(body.toString().getBytes("UTF-8"));
//            hdParams = "&" + hdParams;
//            os.write(hdParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br;
                is = new BufferedInputStream(conn.getInputStream());
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }

                br.close();

                result = sb.toString();
                // 불필요 부분 제거
                if (TextUtils.isEmpty(result) == false) {
                    int startSub = result.indexOf("{");
                    if (startSub != -1) {
                        result = result.substring(startSub).replace("</string>", "");
                    }
                    Log.i(TAG, "doConnection.result=" + result);
                }

                return result;
            } else {
                Log.e(TAG, "데이터 통신 실패");
                throw new Exception();
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.flush();
                os.close();
            }
        }
    }


    public String getParam() {
        String params = paramSb.toString();
        if ("".equals(params.trim()) == false)
            params = params.substring(0, params.length() - 1);

        return params;
    }

}
