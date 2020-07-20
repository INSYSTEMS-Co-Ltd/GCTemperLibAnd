package com.greencross.gctemperlib.greencare.network.tr;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Log;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_hra_check_result_input;
import com.greencross.gctemperlib.greencare.util.JsonLogPrint;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.NetworkUtil;
import com.greencross.gctemperlib.util.GLog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

public class ApiData {
	private static final String TAG			= ApiData.class.getSimpleName();
	public static final int	TYPTE_NONE	= -1;

	private int				trMode		= -1;
	private String Url;

	/**
	 * 통신하여 json 데이터 Class<?> 세팅
	 * @param baseData
	 * @return
	 */

	public Object getData(Context context, BaseData baseData, Object obj) {
		JSONObject body = null;
//		IBaseData dataCls = null;
//		try {
//			Class<?> cl = Class.forName(cls.getName());
//			Constructor<?> co = cl.getConstructor();
//			dataCls = (BaseData) co.newInstance();
//
//			body = dataCls.makeJson(obj);
//
//		} catch (Exception e) {
//            try {
//                Class<?> cl = Class.forName(cls.getName());
//                Constructor<?> co = cl.getConstructor(Context.class);
//                dataCls = (BaseData) co.newInstance(context);
//
//                body = dataCls.makeJson(obj);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//                Log.e(TAG, "ApiData Class 생성 실패", e);
//            }
//		}

		try {
			body = baseData.makeJson(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Url = baseData.conn_url;
		Logger.d(TAG, "baseData.conn_url="+baseData.conn_url);

//        String url = BaseUrl.COMMON_URL;
//        if (Define.getInstance().getInformation() != null) {
//            // 로드벨런싱 후 Url
//            if (TextUtils.isEmpty(Define.getInstance().getInformation().apiURL))
//                url = Define.getInstance().getInformation().apiURL;
//        }

        Logger.i(TAG, "ApiData.url="+baseData.conn_url);
        ConnectionUtil connectionUtil = new ConnectionUtil(baseData.conn_url);


//		String result = null;
//		if (body != null) {
//			String hdParmas = getParams(context);	// 현대해상에서 사용하는 parameter 값
//            result = connectionUtil.doConnection(body, hdParmas,cls.getSimpleName());
//        }

		String result = null;
		if (body != null) {
//			if(Url.contains("https://app.fingern.co.kr:1334/gcross/chat/channel")){
			result = connectionUtil.doConnection(body, baseData.getClass().getSimpleName(), Url, baseData);
//			}else{
//				result = connectionUtil.doConnection(body, baseData.getClass().getSimpleName());
//			}

		}

//		if (TextUtils.isEmpty(result)) {
//			Logger.e(TAG, "getData.result="+result);
//		} else {
//			Logger.i(TAG, "####################### API RESULT."+cls.getSimpleName()+" #####################");
//			JsonLogPrint.printJson(result);
//			Logger.i(TAG, "####################### API RESULT."+cls.getSimpleName()+" #####################");
//		}

		if (TextUtils.isEmpty(result)) {
			Logger.e(TAG, "getData.result="+result);
		} else {
			Logger.i(TAG, "####################### API RESULT."+baseData.getClass().getSimpleName()+" #####################");
			JsonLogPrint.printJson(result);
			Logger.i(TAG, "####################### API RESULT."+baseData.getClass().getSimpleName()+" #####################");
		}

//		Gson gson = new Gson();
//		return gson.fromJson(result, dataCls.getClass());

		Gson gson = new Gson();
		if(result.startsWith("[")) {
			// json 배열 처리
			return baseData.gsonFromArrays(gson, result);
		}else{
			// json 단일 처리
			return gson.fromJson(result, baseData.getClass());
		}
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
	 * @return
	 */
	public String getParams(Context context) {
		HashMap<String, String> body = new HashMap<String, String>();

		if ( !body.containsKey("member_id") )
			body.put("member_id", CommonData.getInstance(context).getMemberId()+"");
		if ( !body.containsKey("device_type") )
			body.put("device_type", "A");
		if ( !body.containsKey("session_code") )
			body.put("session_code", CommonData.getInstance(context).getSessionCode());
		if ( !body.containsKey("store_id") )
			body.put("store_id", NetworkConst.getInstance().getMarketId()+"");
		if ( !body.containsKey("app_ver")){	// app_ver 이 없다면
			if(!CommonData.getInstance(context).getAppVersion().equals("")){	// app_ver 이 공백이 아니라면
				body.put("app_ver", CommonData.getInstance(context).getAppVersion());
			}else{													// app_ver 이 공백이라면
				try {
					PackageInfo pi = context.getPackageManager().getPackageInfo( context.getPackageName(), 0 );
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

		if ( result.length() > 3 ) {
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

	public interface IResult {
		void onSuccess(Object obj);
		void onFail(Object obj);
	}

	public void getData(final Context context, final Class<? extends BaseData> cls, final Object obj, final ApiData.IStep step) {
		getData(context, cls, obj, step, null);
	}

	public void getData(final Context context, final Class<? extends BaseData> cls, final Object obj, final ApiData.IStep step, final ApiData.IFailStep failStep) {
		BaseData tr = createTrClass(cls, context);
		if (NetworkUtil.getConnectivityStatus(context) == false) {
			CDialog.showDlg(context, "네트워크 연결 상태를 확인해주세요.");
			return;
		}
//        String url = "http://wkd.walkie.co.kr/SK/WebService/SK_Mobile_Call.asmx/SK_mobile_Call";
		String url = BaseUrl.COMMON_URL;

		Logger.i(TAG, "LoadBalance.cls=" + cls + ", url=" + url);
//		if (TextUtils.isEmpty(url) && (cls != Tr_get_infomation.class)) {
//			getInformation(context, cls, obj, step);
//			return;
//		}
		if(!cls.getName().equals(Tr_hra_check_result_input.class.getName())) {
//			if (isShowProgress)
//				showProgress();
		}

		CConnAsyncTask.CConnectorListener queryListener = new CConnAsyncTask.CConnectorListener() {

			@Override
			public Object run() throws Exception {

				ApiData data = new ApiData();
				return data.getData(context, tr, obj);
			}

			@Override
			public void view(CConnAsyncTask.CQueryResult result) {
//				hideProgress();

				if (result.result == CConnAsyncTask.CQueryResult.SUCCESS && result.data != null) {
					if (step != null) {
						step.next(result.data);
					}

				} else {
					//mBaseActivity.hideProgressForce();
					if (failStep != null) {
						failStep.fail();
					} else {

						CDialog.showDlg(context, "데이터 수신에 실패 하였습니다.");
						Log.e(TAG, "CConnAsyncTask error=" + result.errorStr);
//						hideProgress();
					}
				}
			}
		};

		CConnAsyncTask asyncTask = new CConnAsyncTask();
		asyncTask.execute(queryListener);
	}


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
