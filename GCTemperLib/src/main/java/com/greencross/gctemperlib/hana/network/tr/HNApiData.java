package com.greencross.gctemperlib.hana.network.tr;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.hana.util.JsonLogPrint;
import com.greencross.gctemperlib.util.GLog;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

public class HNApiData {
    private static final String TAG = HNApiData.class.getSimpleName();

    public Object getData(Context context, Class<? extends BaseData> cls, final Object obj) throws Exception {
        Object recv = null;
        BaseData baseData = createTrClass(cls, context);
        JSONObject body = baseData.makeJson(obj);
        String connUrl = baseData.getConnUrl();
        Log.i(TAG, "ApiData.url=" + connUrl);
//        ConnectionUtil connectionUtil = new ConnectionUtil(baseData.conn_url);
//            result = connectionUtil.doConnection(body, baseData.getClass().getSimpleName(), url, baseData);
        String result = connection(body, baseData, connUrl);

        if (TextUtils.isEmpty(result)) {
            Log.e(TAG, "getData.result=" + result);
            return result;
        } else {
            Log.i(TAG, "####################### API RESULT." + baseData.getClass().getSimpleName() + " #####################");
            JsonLogPrint.printJson(result);
            Log.i(TAG, "####################### API RESULT." + baseData.getClass().getSimpleName() + " #####################");
        }


        Gson gson = new Gson();
        if (result.startsWith("[")) {
            // json 배열 처리
            recv = baseData.gsonFromArrays(gson, result);
            return recv;
        } else {
            // json 단일 처리
            recv = gson.fromJson(result, baseData.getClass());
            return recv;
        }
    }

    private String connection(JSONObject body, BaseData tr, String connUrl) throws Exception {
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        String result = null;
//                String result = "";
        URL url = new URL(connUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setReadTimeout(5 * 1000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer "  +"APA91bGkmKwWBjCso94R3sM3CUEk79");
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

        Log.i(TAG, "###############  "+TAG+"." + tr.getClass().getSimpleName() + "  ###############");
        Log.i(TAG, "url=" + url);
        if (body != null)
            JsonLogPrint.printJson(body.toString());
        else
            Log.i(TAG, "Send Body is Null");
        Log.i(TAG, "###############  "+TAG+"." + tr.getClass().getSimpleName() + "  ###############");

        os = conn.getOutputStream();
//        os.write((tr.json_obj_name + "=").getBytes("UTF-8"));
//            os.write("json=".getBytes( "UTF-8"));           // json={key,value...} 형태로 파라메터 입력
//            os.write("&member_id=0&device_type=A&session_code=&store_id=1&app_ver=2.2".getBytes("EUC-KR"));
        if (body != null)
            os.write(body.toString().getBytes("UTF-8"));

        os.flush();
        os.close();

        Log.i(TAG, "conn.getResponseCode()=" + conn.getResponseCode());
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
        }
        return result;
    }


    private String decodeUniCode(String unicode) {
        try {
            StringBuffer str = new StringBuffer();

            char ch = 0;
            for (int i = unicode.indexOf("\\u"); i > -1; i = unicode.indexOf("\\u")) {
                ch = (char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16);
                str.append(unicode.substring(0, i));
                str.append(String.valueOf(ch));
                unicode = unicode.substring(i + 6);
            }
            str.append(unicode);

            return str.toString();
        } catch (Exception e) {
            return unicode;
        }

    }

    /**
     * HttpUrlConnection 에서 사용
     *
     * @return
     */
    public String getParams(Context context) {
        HashMap<String, String> body = new HashMap<String, String>();

        if (!body.containsKey("member_id"))
            body.put("member_id", CommonData.getInstance(context).getMemberId() + "");
        if (!body.containsKey("device_type"))
            body.put("device_type", "A");
        if (!body.containsKey("session_code"))
            body.put("session_code", CommonData.getInstance(context).getSessionCode());
        if (!body.containsKey("store_id"))
            body.put("store_id", NetworkConst.getInstance().getMarketId() + "");
        if (!body.containsKey("app_ver")) {    // app_ver 이 없다면
            if (!CommonData.getInstance(context).getAppVersion().equals("")) {    // app_ver 이 공백이 아니라면
                body.put("app_ver", CommonData.getInstance(context).getAppVersion());
            } else {                                                    // app_ver 이 공백이라면
                try {
                    PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
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
                e.printStackTrace();
            }
            result += "&" + key + "=" + value;
        }

        if (result.length() > 3) {
            result = result.replace("?&", "");
        }

        return result;
    }

    public interface IStep {
        void next(Object obj);
    }

    public interface IFailStep {
        void fail();
    }


//    public void getData(final Context context, final Class<? extends BaseData> cls, final Object obj, final ApiDataHN.IStep step) {
//        getData(context, cls, obj, step, null);
//    }

//    public void getData(final Context context, final Class<? extends BaseData> cls, final Object obj, final ApiDataHN.IStep step, final ApiDataHN.IFailStep failStep) {
//        BaseData tr = createTrClass(cls, context);
//        if (NetworkUtil.getConnectivityStatus(context) == false) {
//            CDialog.showDlg(context, "네트워크 연결 상태를 확인해주세요.");
//            return;
//        }
//
//        CConnAsyncTask.CConnectorListener queryListener = new CConnAsyncTask.CConnectorListener() {
//
//            @Override
//            public void run() throws Exception {
//
//                ApiDataHN data = new ApiDataHN();
//                return data.getData(context, tr, obj);
//            }
//
//            @Override
//            public void view(CConnAsyncTask.CQueryResult result) {
////				hideProgress();
//
//                if (result.result == CConnAsyncTask.CQueryResult.SUCCESS && result.data != null) {
//                    if (step != null) {
//                        step.next(result.data);
//                    }
//
//                } else {
//                    //mBaseActivity.hideProgressForce();
//                    if (failStep != null) {
//                        failStep.fail();
//                    } else {
//
//                        CDialog.showDlg(context, "데이터 수신에 실패 하였습니다.");
//                        Log.e(TAG, "CConnAsyncTask error=" + result.errorStr);
////						hideProgress();
//                    }
//                }
//            }
//        };
//
//        CConnAsyncTask asyncTask = new CConnAsyncTask();
//        asyncTask.execute(queryListener);
//    }


    private static BaseData createTrClass(Class<? extends BaseData> cls, Context context) {
        BaseData trClass = null;
        try {
            Constructor<? extends BaseData> co = cls.getConstructor();
            trClass = co.newInstance();
        } catch (Exception e) {
            try {
                Constructor<? extends BaseData> co = cls.getConstructor(Context.class);
                trClass = co.newInstance(context);
            } catch (Exception e2) {
                Log.e(TAG, "createTrClass", e2);
            }
        }

        return trClass;
    }
}
