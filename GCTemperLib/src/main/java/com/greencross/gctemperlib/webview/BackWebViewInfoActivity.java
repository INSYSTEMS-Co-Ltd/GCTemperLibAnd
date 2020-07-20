package com.greencross.gctemperlib.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.KakaoLinkUtil;
import com.greencross.gctemperlib.R;


/**
 * Created by jihoon on 2016-01-06.
 * 뒤로가기 웹뷰 클래스
 * @since 0, 1
 */
public class BackWebViewInfoActivity extends BackBaseActivity implements View.OnClickListener{

    private WebView mWebView;
    private Intent mIntent;
    private String		mUrl;
    private  Intent intent;


    private ImageView mBackImg;
    private LinearLayout mShareBtn,mPlusFriendAdd;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_webview_info_activity);


        mBackImg    = getBackImg();


        mIntent		=	getIntent();
        mUrl		=	mIntent.getStringExtra(CommonData.EXTRA_URL);
        mWebView	=	(WebView) findViewById(R.id.web_view);
        mShareBtn   =   (LinearLayout) findViewById(R.id.btn_share);
        mPlusFriendAdd  = (LinearLayout) findViewById(R.id.btn_friend_add);
        view = findViewById(R.id.root_view);

        mBackImg.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
        mPlusFriendAdd.setOnClickListener(this);

        //click 저장
        OnClickListener mClickListener = new OnClickListener(this,view, BackWebViewInfoActivity.this);

        //주요 서비스
        mShareBtn.setOnTouchListener(mClickListener);
        mPlusFriendAdd.setOnTouchListener(mClickListener);

        //코드 부여(주요 서비스)
        mShareBtn.setContentDescription(getString(R.string.ShareBtn9));
        mPlusFriendAdd.setContentDescription(getString(R.string.PlusFriendAdd));


        mWebView.setWebViewClient(new TermsWebViewClinet());
        mWebView.setWebChromeClient(new TermsWebViewChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.addJavascriptInterface(new AndroidBridge(), "Android");

        mWebView.setBackgroundColor(Color.TRANSPARENT);

        if ( Build.VERSION.SDK_INT >= 11 )
            mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        setTitle(mIntent.getStringExtra(CommonData.EXTRA_ACTIVITY_TITLE));

//        String postData = "member_id=" + CommonData.getInstance(BackWebViewInfoActivity.this).getMemberId() +"&store_id="+ NetworkConst.getInstance().getMarketId() +
//                "&device_type=A" +
//                "&app_ver=" + CommonData.getInstance(BackWebViewInfoActivity.this).getAppVer();
//        mWebView.postUrl(mUrl, EncodingUtils.getBytes(postData, "BASE64"));
        mWebView.loadUrl(mUrl);

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GLog.i("onStart() getClass().getSimpleName() = " + getClass().getSimpleName(), "dd");
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {

            mWebView.goBack();

            return true;

        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.common_left_btn) {
            if (mWebView.canGoBack()) {

                mWebView.goBack();

                return;

            } else {
                finish();
            }
        } else if (id == R.id.btn_share) {//                String imgUrl = "https://wkd.walkie.co.kr/HL_FV/info/image/01_info.png";
//
//                View view = LayoutInflater.from(BackWebViewInfoActivity.this).inflate(R.layout.applink_dialog_layout, null);
//                ApplinkDialog dlg = ApplinkDialog.showDlg(BackWebViewInfoActivity.this, view);
//                dlg.setSharing(imgUrl, "img", "", "","[현대해상 "+ KakaoLinkUtil.getAppname(BackWebViewInfoActivity.this.getPackageName(),BackWebViewInfoActivity.this)+"]","주요 서비스 소개","자세히보기","",false,"info.png","/HL_FV/INFO/info_share.asp","https://wkd.walkie.co.kr/HL_FV/INFO/info_share.asp");
        } else if (id == R.id.btn_friend_add) {
            KakaoLinkUtil.kakaoAddFriends(BackWebViewInfoActivity.this);
        }

    }



    private class AndroidBridge {
        @JavascriptInterface
        public void introduction(final String req){
            Handler handler = new Handler();
            handler.post( new Runnable() {
                @Override
                public void run() {
                    String result;

                    result = String.format("%s", req);
                    Log.i("infoWebview","result : "+result);
                    if(BackWebViewInfoActivity.this != null){
                        BackWebViewInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run(){
                                if(req.equals("mom")){

                                    if(CommonData.getInstance(BackWebViewInfoActivity.this).getIamChild().compareTo("Y") == 0) {
                                        mDialog = new CustomAlertDialog(BackWebViewInfoActivity.this, CustomAlertDialog.TYPE_A);
                                        mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                                        mDialog.setContent(getString(R.string.i_am_child));
                                        mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
                                        mDialog.show();

                                    }else{
//                                        if (CommonData.getInstance(BackWebViewInfoActivity.this).getHpMjYn().compareTo("Y") == 0 && CommonData.getInstance(BackWebViewInfoActivity.this).getHpMjYnJun()) {
//                                            intent = new Intent(BackWebViewInfoActivity.this, MotherHealthMainActivity.class);
//                                        } else {
//                                            intent = new Intent(BackWebViewInfoActivity.this, MotherHealthRegActivity.class);
//                                        }
//                                        finish();
//                                        startActivity(intent);

                                    }
//                                }else if(req.equals("mind") || req.equals("sound")){
//                                    startActivity(new Intent(BackWebViewInfoActivity.this, PsyMainActivity.class));
//                                    finish();
                                }else {
//                                    startActivity(new Intent(BackWebViewInfoActivity.this, GrowthMainActivity.class));
                                    finish();
                                }

                            }
                        });
                    }

                }
            });
        }
    }

    private class TermsWebViewChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            // TODO Auto-generated method stub
            return super.onJsAlert(view, url, message, result);
        }
    }

    private class TermsWebViewClinet extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//            handler.proceed(); // SSL 에러가 발생해도 계속 진행!
            GLog.i("onReceivedSslError()", "dd");
            mDialog = new CustomAlertDialog(BackWebViewInfoActivity.this, CustomAlertDialog.TYPE_B);
            mDialog.setTitle(getResources().getString(R.string.popup_dialog_a_type_title));
            mDialog.setContent(getResources().getString(R.string.popup_dialog_serucity_content));
            mDialog.setPositiveButton(getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    handler.proceed(); // SSL 에러가 발생해도 계속 진행!
                    dialog.dismiss();
                }
            });
            mDialog.setNegativeButton(null, new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    handler.cancel();    // 취소
                    dialog.dismiss();
                }
            });
            mDialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            if(request.startsWith(("tel:"))) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse(request));
                startActivity(intent1);
                return true;
            }
            view.loadUrl(request);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            showProgress();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            hideProgress();

//            if(titlePos.isEmpty()){
                setTitle(mWebView.getTitle());
                Log.i("BackWebview","webviewTitle:"+mWebView.getTitle());

                //getCommonActionBar().setActionBarTitle(titlePos);
//            }
            super.onPageFinished(view, url);
        }

    }
}
