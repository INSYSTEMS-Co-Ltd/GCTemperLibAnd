package com.greencross.gctemperlib.hana.network.tr;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class BaseData implements Cloneable, IBaseData {
	protected Context mContext;


    public String APP_CODE = "android"+ Build.VERSION.RELEASE;
    public String INSURES_CODE = "108";
    public String OS_GUBUN = "A";

//    public BaseData(Context context) {
//    	mContext = context;
//	}

    protected String getApiCode(String tag) {
        return  tag.replace("Tr_", "");
    }

	protected String conn_url = BaseUrl.COMMON_URL;
	public String json_obj_name = "json";

	protected String getConnUrl() {
		return conn_url;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


	protected JSONObject getBaseJsonObj(String apiCode) throws JSONException {
		JSONObject body = new JSONObject();
		body.put("api_code", apiCode);
		body.put("app_code", APP_CODE);
		body.put("insures_code", INSURES_CODE);

		return body;
	}

	protected JSONObject getBaseJsonObj() throws JSONException{
		return getBaseJsonMediObj("");
	}

	protected JSONObject getBaseJsonMediObj(String apiCode) throws JSONException{
		JSONObject body = new JSONObject();
		return body;
	}

	public boolean isSuccess(String resultCode) {
		return "1000".equals(resultCode);
	}

	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		Class<?> gotClass = this.getClass();
		String className = gotClass.getName();
		Field[] fields = gotClass.getFields();
		int fieldLen = fields.length;
		Field field = null;
		String fieldName = null;
		String fieldValue = null;
		Class<?> fieldType = null;
		sBuilder.append("#ClassName=" + className + "\n");
		for (int i = 0; i < fieldLen; ++i) {
			field = fields[i];
			fieldName = field.getName();
			fieldType = field.getType();
			if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			try {
				if (fieldType.isArray()) {
					fieldValue = Arrays.toString((Object[]) field.get(BaseData.this));
				} else {
					fieldValue = field.get(BaseData.this).toString();
				}
			} catch (Exception e) {
			}
			sBuilder.append(fieldName + "=" + fieldValue + "\n");
		}
		return sBuilder.toString();
	}
	
	/**
	 * 이미지 데이터 불러올때 null 이거나 "" 이면 불러올 필요가 없으므로 null로 리턴 한다 
	 * @param url
	 * @param photo
	 * @return
	 */
	public String getPhoto(String url, String photo) {
		if (checkPhotoData(photo)) {
			return url+photo;
		} else {
			return null;
		}
	}
	
	public boolean checkPhotoData(String photo) {
		if (photo == null || "".equals(photo)) {
			return false;
		}
		return true;
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		return null;
	}


	/**
	 * 배열 데이터 처리
	 * @param gson
	 * @param json
	 * @return
	 */
	public List<? extends BaseData> gsonFromArrays(Gson gson, String json) {
//        Gson gson = new Gson();
		List<BaseData> list = gson.fromJson(json, new TypeToken<List<BaseData>>(){}.getType());
		return list;
	}

//	B representing the byte primitive type
//	S representing the short primitive type
//	I representing the int primitive type
//	J representing the long primitive type
//	F representing the float primitive type
//	D representing the double primitive type
//	C representing the char primitive type
//	Z representing the boolean primitive type
//	V representing void function return values
}
