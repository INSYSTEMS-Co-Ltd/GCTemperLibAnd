package com.greencross.gctemperlib.network;

import android.os.Build;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.tsccm.ThreadSafeClientConnManager;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.params.HttpProtocolParams;
import cz.msebera.android.httpclient.protocol.HTTP;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.SFSSLSocketFactory;


/**
 * Created by jihoon on 2016-01-04.
 * 네트워크 클래스
 * @since 0, 1
 */
public class HttpUtil {

    public int responseResultCode = -1;

    public String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    public String uploadFile(String page, String fileName, Hashtable<String, String> params) {
        URL mConnectUrl;
        String result = null;
        FileInputStream fileInputStream = null;

        HttpURLConnection httpUrlConnection = null;

        try{
            File file = new File(fileName);
            if(! file.exists()){
//                Util.showMessage("file not exist ");
                return null;
            }

            fileInputStream = new FileInputStream(file);

//            connectURL = new URL(page);
            //			connectURL = new URL("https","www.walkie.co.kr", 449, "/upload/kyobo/picture_upload.aspx");
//            Util.showMessage("connectURL "+connectURL);

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "----WebKitFormBoundaryTGNE4W5X9ZSBF0rC";

            //			trustAllHosts();

            /*

            httpsurlconnection = (HttpsURLConnection)connectURL.openConnection();

            httpsurlconnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null);  // No validation for now
            httpsurlconnection.setSSLSocketFactory(context.getSocketFactory());

            */

            mConnectUrl			=	new URL(page);

            if (mConnectUrl.getProtocol().toLowerCase().equals("https")) {

                trustAllHosts();
                HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) mConnectUrl.openConnection();
                httpsUrlConnection.setHostnameVerifier(DO_NOT_VERIFY);
                httpUrlConnection = httpsUrlConnection;

            } else {
                httpUrlConnection = (HttpURLConnection) mConnectUrl.openConnection();
            }

            // Connect to host
            //			httpsurlconnection.connect();
            httpUrlConnection.setInstanceFollowRedirects(true);

            httpUrlConnection.setRequestProperty( "Connection", "close" );
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setAllowUserInteraction(false);

            String origin = "";
            final String header = "https://";
            if(page.startsWith(header)) {
                int index = page.indexOf('/', header.length());
                if(index != -1) {
                    origin = page.substring(0, index);
                }
            }
//            Util.showMessage("origin "+origin);
            String ext = getExtension(fileName);
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());

            httpUrlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            httpUrlConnection.setRequestProperty("Accept-Charset", "windows-949,utf-8;q=0.7,*;q=0.3");
            httpUrlConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            httpUrlConnection.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
            httpUrlConnection.setRequestProperty("Cache-Control", "max-age=0");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Accept-Charset", "euc-kr");
            httpUrlConnection.setRequestProperty("Origin", origin);
            httpUrlConnection.setRequestProperty("Referer", page);
            httpUrlConnection.setRequestProperty("User-Agent", "Android Browser");

            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);

            DataOutputStream dos = new DataOutputStream(httpUrlConnection.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"__VIEWSTATE\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(CommonData.CHLDRN_NOTE_UPLOAD_VIEWSTATE);

            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"__EVENTVALIDATION\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(CommonData.CHLDRN_NOTE_UPLOAD_EVENTVALIDATION);

            dos.writeBytes(lineEnd);

            Set<Map.Entry<String, String>> set = params.entrySet();
            Iterator<Map.Entry<String, String>> itr = set.iterator();
            while(itr.hasNext()) {
                Map.Entry<String, String> item = itr.next();
//                Util.showMessage("item.getKey() "+item.getKey()+" item.getValue() "+item.getValue());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + item.getKey() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(item.getValue());
                dos.writeBytes(lineEnd);
            }

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"ImageButton1.x\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(27));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"ImageButton1.y\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(22));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: " + mime + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"upfile\";filename=\"" + fileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = fileInputStream.available();
//            Util.showMessage("bytesAvailable "+bytesAvailable);
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[ ] buffer = new byte[bufferSize];

            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//            Util.showMessage("bytesRead "+bytesRead);
            while (bytesRead > 0){

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            fileInputStream.close();
            dos.flush();

//            Util.showMessage("httpUrlConnection.getResponseCode() "+httpUrlConnection.getResponseCode());

            if(httpUrlConnection.getResponseCode() > 200){
                InputStream err = httpUrlConnection.getErrorStream();
                List<Byte> Array = new ArrayList<Byte>();

                int ch;
                while((ch = err.read()) != -1 ) {
                    Array.add((byte)ch);
                }
                byte[] bb = new byte[Array.size()];
                for(int i=0;i<bb.length;i++){
                    bb[i] = Array.get(i);
                }

                Array.clear();
//                Util.showMessage("error "+ new String(bb,"UTF-8"));
                dos.close();

            }else{

                InputStream is = httpUrlConnection.getInputStream();

                int ch;
                StringBuffer b = new StringBuffer();
                while((ch = is.read()) != -1 ) {
                    b.append( (char)ch );
                }
                result = b.toString();
                dos.close();
            }

        }catch (Exception e){
//            Util.showMessage("e " + e.toString());
            GLog.e(e.toString());
            e.printStackTrace();
//            hideDialog();
//            showErrorDialog("사진 선택 오류입니다.\n다시 시도해 주세요.");
        }finally{
            if( httpUrlConnection != null){
                httpUrlConnection.disconnect();
            }
        }

        return result;
    }

    /**
     * API 호출
     * @param addr	(도메인)
     * @param param	(Post 파라미터)
     * @return
     */
    public String requestHttpClient(String addr, ArrayList<NameValuePair> param){
        StringBuilder strBuilder = new StringBuilder();

        HttpClient httpClient = getHttpClient();
        httpClient.getParams().setParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setParameter("http.connection.timeout", CommonData.TIME_OUT_DELAY);
        httpClient.getParams().setParameter("http.socket.timeout", CommonData.TIME_OUT_DELAY);
        httpClient.getParams();
        InputStream is = null;

        try{
            StringBuilder logParam = new StringBuilder();
            for(NameValuePair nvp:param){
                logParam.append("&" +nvp.getName() +"=" +nvp.getValue());
            }
            GLog.i("url = " + addr + "?" + logParam, "dd");
        }
        catch(Exception e){
            GLog.e(e.toString());
        }

        try {

//			URL url = new URL(addr);
            URI uri = new URI(addr);

            HttpPost httpPost = new HttpPost(uri);
            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(param, "UTF-8");
//            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(param, "euc-kr");

            httpPost.setEntity(entityRequest);
            // User_agent 사용자 단말 정보 전달
            httpPost.setHeader("USER_AGENT", "Android " + Build.VERSION.RELEASE.toString() +"; "+Build.MODEL.toString());
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entityResponse = response.getEntity();
            is = entityResponse.getContent();

            responseResultCode = response.getStatusLine().getStatusCode();
            GLog.i("responseResultCode = " +responseResultCode, "dd");

            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while((line = bufferReader.readLine()) != null)
            {
                strBuilder.append(line);
                if( line == null) break;
            }
            bufferReader.close();

        }
        catch (Exception e) {
            GLog.e(e.toString());
            return "";
        }
        finally {

            httpClient.getConnectionManager().shutdown();
        }

        return strBuilder.toString();
    }

    private HttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            GLog.e(e.toString());
            return new DefaultHttpClient();
        }
    }

    private void trustAllHosts()
    {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {}

            @Override
            public void checkServerTrusted( java.security.cert.X509Certificate[] chain, String authType)
                    throws java.security.cert.CertificateException {}
        } };

        try
        {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
    {
        @Override
        public boolean verify(String arg0, SSLSession arg1)
        {
            return true;
        }
    };

    private String getExtension(String uri) {
        if (uri == null) {
            return "";
        }

        int dot = uri.lastIndexOf('.');
        if (dot > 0 && dot < uri.length() - 1)
            return uri.substring(dot + 1);
        else
            return "";
    }


}
