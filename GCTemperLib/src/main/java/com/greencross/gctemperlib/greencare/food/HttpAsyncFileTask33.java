package com.greencross.gctemperlib.greencare.food;

import android.os.AsyncTask;
import android.util.Log;

import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.greencare.util.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 사용법
 * 녹십자 제공 소스
 * String param = "77777788889";
 String url = "http://m.shealthcare.co.kr/SK/SKUPLOAD/skfood_upload.ashx";
 String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/name/capture.jpg";

 HttpAsyncFileTask33 rssTask = new HttpAsyncFileTask33(this);
 rssTask.setParam(url, param, fileName);
 rssTask.execute();
 */
public class HttpAsyncFileTask33 extends AsyncTask<String, Void, String> {
    private static final String TAG = HttpAsyncFileTask33.class.getSimpleName();

    private HttpAsyncTaskInterface atv;
//	private String param = "";
	private static FileInputStream mFileInputStream = null;
	private static URL connectUrl = null;

	static String lineEnd = "\r\n";
	static String twoHyphens = "--";
	static String boundary = "*****";

	private static String baseUrl;
	private String fileName;

	private File file=null;
	private String fileNameKey="uploadedfile";
	private HashMap<String,String> params;

	public HttpAsyncFileTask33(String url, String param, HttpAsyncTaskInterface atv) {
		this.atv = atv;
        this.baseUrl = url;  //baseUrl;
        this.baseUrl += "?"+param;
	}

	public HttpAsyncFileTask33(String subUrl, HttpAsyncTaskInterface atv) {
		this.atv = atv;
		this.baseUrl = "http://m.shealthcare.co.kr"+subUrl;  //baseUrl;
	}

//	public void setParam(String param, String fileName) {
	public void setParam(String fileName) {
//		this.param = param;
		this.fileName = fileName;
//		this.baseUrl = "http://m.shealthcare.co.kr/SK/SKUPLOAD/skfood_upload.ashx";//page;
	}

	public void setParam(File file, String fileNameKey, HashMap<String,String> params) {
		this.fileNameKey = fileNameKey;
		this.file = file;
		this.params = params;
	}

	@Override
	protected void onPreExecute() {
		atv.onPreExecute();
	}

	@Override
	protected String doInBackground(String... urls) {
		return getJSONData(); 
	}

	@Override
	protected void onPostExecute(String data)  {
		atv.onFileUploaded(data);
	}

	public String getJSONData() {
		String result;
		if(!fileNameKey.equals("FILE")&&!fileNameKey.equals("img_file"))
			result = HttpFileUpload(fileName,fileNameKey);
		else
			result = HttpFileUpload(file,params,fileNameKey);

		return result;
	} 	

	private static String HttpFileUpload(String fileName, String fileNameKey) {
		String result = null;
		try{
			mFileInputStream = new FileInputStream(fileName);
//			urlString+="?sn="+param;
			connectUrl = new URL(baseUrl);
			GLog.i(TAG, "mFileInputStream is " + mFileInputStream);

			HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream( conn.getOutputStream()) ;
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			dos.writeBytes("Content-Disposition:form-data;name=\"" + fileNameKey + "\";filename=\"" + fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			
			int bytesAvailable = mFileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];
			int bytesRead = mFileInputStream.read( buffer , 0 , bufferSize);
            GLog.i(TAG, "image byte is " + bytesRead );

			while(bytesRead > 0 ){
				dos.write(buffer , 0 , bufferSize);
				bytesAvailable = mFileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = mFileInputStream.read(buffer,0,bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			int serverResponseCode = conn.getResponseCode();
		    String serverResponseMessage = conn.getResponseMessage();

            GLog.i(TAG, "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
		    if(serverResponseCode == 200){
		       
		    }

            GLog.i(TAG,  "File is written");
			mFileInputStream.close();
			dos.flush(); // 버퍼에 있는 값을 모두 밀어냄

			//웹서버에서 결과를 받아 EditText 컨트롤에 보여줌
			int ch;
			InputStream is = conn.getInputStream();
            GLog.i(TAG,   "is "+ is);
			StringBuffer b = new StringBuffer();
			while((ch = is.read()) != -1 ){
				b.append((char)ch);
			}

			result = b.toString();
            GLog.i(TAG,  "result = " + result);

			dos.close();
		}catch(Exception e){
            Logger.e(TAG,  "exception " + e.getMessage());
			result = null;
		}
		return result;
	}

	private static String HttpFileUpload(final File file, HashMap<String,String> params, String fileNameKey) {
		String boundary = "^-----^";
		String LINE_FEED = "\r\n";
		String charset = "UTF-8";
		OutputStream outputStream;
		PrintWriter writer;

		String result = null;
		try{

			URL url = new URL(baseUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(15000);

			outputStream = connection.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);


			for (Map.Entry<String,String> entry : params.entrySet()){
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append("Content-Disposition: form-data; name=\""+entry.getKey()+"\"").append(LINE_FEED);
				writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.append(entry.getValue()).append(LINE_FEED);
				writer.flush();
			}


			if(file!=null&&file.exists()) {
				/** 파일 데이터를 넣는 부분**/
				writer.append("--" + boundary).append(LINE_FEED);
				writer.append("Content-Disposition: form-data; name=\"" + fileNameKey + "\"; filename=\"" + file.getName() + "\"").append(LINE_FEED);
				writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
				writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
				writer.append(LINE_FEED);
				writer.flush();

				FileInputStream inputStream = new FileInputStream(file);
				byte[] buffer = new byte[(int) file.length()];
				int bytesRead = -1;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				outputStream.flush();
				inputStream.close();
				writer.append(LINE_FEED);
				writer.flush();
			}

			writer.append("--" + boundary + "--").append(LINE_FEED);
			writer.close();

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				result = response.toString();
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				result = response.toString();
			}

		} catch (ConnectException e) {
			Log.e(TAG, "ConnectException");
			e.printStackTrace();


		} catch (Exception e){
			e.printStackTrace();
		}

		return result;

	}
}