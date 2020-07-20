package com.greencross.gctemperlib.network;

import android.os.AsyncTask;

import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.collection.Database;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataUploadAsyncTask extends AsyncTask<Database, Void, Void> {

	@Override
	protected Void doInBackground(Database... params) {
		// TODO Auto-generated method stub
		try {
			Gson gson = new Gson();

			String result = gson.toJson(params[0]);
			String param = "func=feverUpdate&data="+result;

			URL url = new URL(NetworkConst.getInstance().getFeverServerDomain());
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream os = conn.getOutputStream();
			os.write(param.getBytes("utf-8"));
			os.flush();
			os.close();

			new BufferedReader( new InputStreamReader( conn.getInputStream(), "UTF-8" ));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;

	}
}
