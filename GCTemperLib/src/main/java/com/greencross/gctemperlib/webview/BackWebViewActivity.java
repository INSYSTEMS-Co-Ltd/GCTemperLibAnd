package com.greencross.gctemperlib.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.R;


/**
 * Created by jihoon on 2016-01-06.
 * 뒤로가기 웹뷰 클래스
 * @since 0, 1
 */
public class BackWebViewActivity extends BackBaseActivity implements View.OnClickListener{

    private WebView mWebView;
    private Intent mIntent;
    private String		mUrl;


    private ImageView mBackImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_webview_activity);


        mBackImg    = getBackImg();
        mBackImg.setOnClickListener(this);

        mIntent		=	getIntent();
        mUrl		=	mIntent.getStringExtra(CommonData.EXTRA_URL);
        mWebView	=	(WebView) findViewById(R.id.web_view);


        mWebView.setWebViewClient(new TermsWebViewClinet());
        mWebView.setWebChromeClient(new TermsWebViewChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setTextZoom(100);

        mWebView.setBackgroundColor(Color.TRANSPARENT);

        if ( Build.VERSION.SDK_INT >= 11 )
            mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        setTitle(mIntent.getStringExtra(CommonData.EXTRA_ACTIVITY_TITLE));

//        String postData = "member_id=" + CommonData.getInstance().getMemberId() +"&store_id="+ NetworkConst.getInstance().getMarketId() +
//                "&device_type=A" +
//                "&app_ver=" + CommonData.getInstance().getAppVer();
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
        if ( mWebView.canGoBack()) {

            mWebView.goBack();

            return;

        }else{
            finish();
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
            mDialog = new CustomAlertDialog(BackWebViewActivity.this, CustomAlertDialog.TYPE_B);
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
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            showProgress();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            hideProgress();
            super.onPageFinished(view, url);
        }

    }
}
