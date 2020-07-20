package com.greencross.gctemperlib.greencare.network.tr.data;

import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * input값
 * img_file : 이미지
 * cmpny_code : 회사코드
 * mber_sn : 회원키
 * <p>
 * output값
 * file_name : 이미지명 - 원본이미지를 넣으면 tm_가 붙은 썸네일 이미지가 생성됩니다. EX)song.jpg(원본), tm_song.jpg(썸네일)
 * file_url : URL
 * data_yn : Y/N
 */

public class Tr_mber_profile_send extends BaseData {


	public static class RequestData {
	}

	public Tr_mber_profile_send() {
	}

	@Override
	public JSONObject makeJson(Object obj) throws JSONException {
		if (obj instanceof RequestData) {
			JSONObject body = getBaseJsonObj();

			RequestData data = (RequestData) obj;

			return body;
		}

		return super.makeJson(obj);
	}

	/**************************************************************************************************/
	/***********************************************RECEIVE********************************************/
	/**************************************************************************************************/

	@Expose
	@SerializedName("data_yn")
	public String data_yn;
	@Expose
	@SerializedName("file_url")
	public String file_url;
	@Expose
	@SerializedName("file_name")
	public String file_name;

}
