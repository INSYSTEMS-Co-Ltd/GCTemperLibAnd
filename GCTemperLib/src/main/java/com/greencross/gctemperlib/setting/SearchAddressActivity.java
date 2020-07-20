package com.greencross.gctemperlib.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.BuildConfig;
import com.greencross.gctemperlib.R;


public class SearchAddressActivity extends AppCompatActivity {

    TextView mTitleTextView;
    ImageView mBackImg;
    private WebView mWebView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        mHandler = new Handler();
    }

    public void init_webView() {
        mTitleTextView =	(TextView)	findViewById(R.id.common_title_tv);
        mBackImg =	(ImageView)	findViewById(R.id.common_left_btn);
        mBackImg.setOnClickListener(v -> finish());
        mTitleTextView.setText(getString(R.string.search_address));
        // WebView 설정
        mWebView = (WebView) findViewById(R.id.web_view);
        // JavaScript 허용
        mWebView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebView.getSettings().setSupportMultipleWindows(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        mWebView.addJavascriptInterface(new AndroidBridge(), "GngCare");
        // web client 를 chrome 으로 설정
        mWebView.setWebChromeClient(new WebChromeClient());
        // 웹뷰 디버깅 설정
        if(BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
            }
        }

        // webview url load
        mWebView.loadUrl("https://wkd.walkie.co.kr/HL_FV/INFO/getAddress.html");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2) {
            mHandler.post(() -> {
                //Toast.makeText(SearchAddressActivity.this, String.format("(%s) %s %s %s", arg1, arg2, arg3, arg4), Toast.LENGTH_LONG).show();
                Intent intent = getIntent();

                intent.putExtra(CommonData.EXTRA_ADDRESS, arg1+CommonData.STRING_SPACE+arg2);
                setResult(RESULT_OK, intent);

                finish();
            });
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        Util.BackAnimationEnd(SearchAddressActivity.this);	// Activity 종료시 뒤로가기 animation
    }

    @Override protected void attachBaseContext(Context newBase) {
        // // super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
        super.attachBaseContext(newBase);
    }
}
