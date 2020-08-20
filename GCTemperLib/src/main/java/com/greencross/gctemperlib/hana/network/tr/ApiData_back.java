package com.greencross.gctemperlib.hana.network.tr;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.hana.util.JsonLogPrint;
import com.greencross.gctemperlib.hana.util.Logger;
import com.greencross.gctemperlib.network.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ApiData_back {
	private final String TAG			= ApiData_back.class.getSimpleName();
	public static final int	TYPTE_NONE	= -1;

	private int				trMode		= -1;
	private IStep mStep;
	private IBaseData mClass;
	ArrayList<NameValuePair> params = new ArrayList<>();

	/**
	 * 통신하여 json 데이터 Class<?> 세팅
	 * @param cls
	 * @return
	 */

	public Object getData(Context context, Class<?> cls, Object obj) {
		return getData(context, cls, obj, null);
	}

	public Object getData(final Context context, final Class<?> cls, final Object obj, final ApiData_back.IStep step) {
		mStep = step;
		JSONObject body = null;
		IBaseData dataCls = null;
		try {
			Class<?> cl = Class.forName(cls.getName());
			Constructor<?> co = cl.getConstructor();
			dataCls = (BaseData) co.newInstance();
			mClass = dataCls;

			body = dataCls.makeJson(obj);
			CommonData commonData = CommonData.getInstance(context);
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(CommonData.JSON_JSON, body.toString()));
			params.add(new BasicNameValuePair(CommonData.JSON_MEMBER_ID, ""+commonData.getMemberId()));
			params.add(new BasicNameValuePair(CommonData.JSON_SESSION_CODE, ""+commonData.getSessionCode()));
			params.add(new BasicNameValuePair(CommonData.JSON_APP_VER, CommonData.getInstance(context).getAppVersion()));
			params.add(new BasicNameValuePair("device_type", "A"));
			params.add(new BasicNameValuePair("store_id", NetworkConst.getInstance().getMarketId()+""));

//			requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, ArrayList<NameValuePair> params, progress){
//			RequestApi.requestApi(context, NetworkConst.NET_REMEDY_INPUT, NetworkConst.getInstance().getFeverDomain(), networkListener, params);

			new Thread() {
				public void run() {

					HttpUtil mHttpUtil = new HttpUtil();
					String result = mHttpUtil.requestHttpClient(BaseUrl.COMMON_URL, params);

					if (TextUtils.isEmpty(result) == false) {
//						Logger.i(TAG, "getData.result="+result);
//						result = result.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns=\"http://tempuri.org/\">", "")
//								.replace("</string>","");
						int startSub = result.indexOf("{");

						result = result.substring(startSub).replace("</string>","");
						Logger.i(TAG, "doConnection.result="+result);

						try {
							Gson gson = new Gson();
							Object obj = gson.fromJson(result, mClass.getClass());

							mHandler.obtainMessage(0, obj);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}
			}.start();




//			this.mContext			= context;
//			this.mType           =  type;
//			this.mUrl				= url;
//			this.mAsyncListener 	= listener;
//
//			this.mNameValuePair		= getParams(params);



//			new NetWorkAsync().doInBackground();


//			GLog.i("NameValuePair.requestApi=", "");
//			for (NameValuePair pair : params) {
//				GLog.i("NameValuePair.getName="+pair.getName()+", getValue="+pair.getValue(), "");
//			}
////			if(progress != null) {
////				GLog.i("progress = not null ", "dd");
////				progress.setVisibility(View.VISIBLE);   // api 호출중에 프로그래스바 활성화
////			}
////			RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params);
////			requestAsyncNetwork.start();

			return obj;
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return null;


//		JSONObject body = null;
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
//				step.next(body);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//                GLog.e(TAG, "ApiData Class 생성 실패", e);
//            }
//		}
//
//        String url = BaseUrl.COMMON_URL;
//        if (Define.getInstance().getInformation() != null) {
//            // 로드벨런싱 후 Url
//            if (TextUtils.isEmpty(Define.getInstance().getInformation().apiURL))
//                url = Define.getInstance().getInformation().apiURL;
//        }
//
//        Logger.i(TAG, "ApiData.url="+url);
//        ConnectionUtil connectionUtil = new ConnectionUtil(url);
//
//
//		String result = null;
//		if (body != null) {
//            result = connectionUtil.doConnection(body, cls.getSimpleName());
//        }
//
//		if (TextUtils.isEmpty(result)) {
//			Logger.e(TAG, "getData.result="+result);
//		} else {
//			Logger.i(TAG, "####################### API RESULT."+cls.getSimpleName()+" #####################");
//			JsonLogPrint.printJson(result);
//			Logger.i(TAG, "####################### API RESULT."+cls.getSimpleName()+" #####################");
//		}
//
//		Gson gson = new Gson();
//		return gson.fromJson(result, dataCls.getClass());
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mStep != null) {
				mStep.next(msg.arg1);
			}
		}
	};

	class NetWorkAsync extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... voids) {
			HttpUtil mHttpUtil = new HttpUtil();

			String result = mHttpUtil.requestHttpClient(BaseUrl.COMMON_URL, params);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

				Gson gson = new Gson();
				Object obj = gson.fromJson(result, mClass.getClass());

				if (mStep != null) {
					mStep.next(obj);
				}
		}
	}


	CustomAsyncListener networkListener = new CustomAsyncListener() {
		@Override
		public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {

		}

		@Override
		public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {

		}

		@Override
		public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
			String result = resultData.toString();

			if (TextUtils.isEmpty(result)) {
				Logger.e(TAG, "getData.result=" + result);
			} else {
				Logger.i(TAG, "####################### API RESULT." + mClass.getClass().getSimpleName() + " #####################");
				JsonLogPrint.printJson(resultData.toString());
				Logger.i(TAG, "####################### API RESULT." + mClass.getClass().getSimpleName() + " #####################");

				Gson gson = new Gson();
				Object obj = gson.fromJson(result, mClass.getClass());
				mStep.next(obj);
			}

		}
	};

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

	public interface IStep {
		void next(Object obj);
	}

    public interface IFailStep {
        void fail();
    }
}
