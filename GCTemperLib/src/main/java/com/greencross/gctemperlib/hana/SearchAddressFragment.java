package com.greencross.gctemperlib.hana;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.BuildConfig;
import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.util.Util;


public class SearchAddressFragment extends BaseFragment {

    private TextView mTitleTextView;
    private ImageView mBackImg;
    private WebView mWebView;
    private Handler mHandler;

    public static Fragment newInstance() {
        SearchAddressFragment fragment = new SearchAddressFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_address, container, false);
        // WebView 초기화
        init_webView(view);

        // 핸들러를 통한 JavaScript 이벤트 반응
        mHandler = new Handler();
        if (getActivity() instanceof DummyActivity) {
            ((DummyActivity)getActivity()).setTitle(getString(R.string.search_address));
        }
        return view;
    }


    public void init_webView(View view) {
        // WebView 설정
        mWebView = (WebView) view.findViewById(R.id.web_view);
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
                WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
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
                Intent intent = getActivity().getIntent();

                intent.putExtra(CommonData.EXTRA_ADDRESS, arg1 + CommonData.STRING_SPACE + arg2);
                getActivity().setResult(Activity.RESULT_OK, intent);

               getActivity().finish();
            });
        }
    }

//    @Override
//    public void finish() {
//        // TODO Auto-generated method stub
//        super.finish();
//        Util.BackAnimationEnd(SearchAddressFragment.this);	// Activity 종료시 뒤로가기 animation
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.BackAnimationEnd(getActivity());	// Activity 종료시 뒤로가기 animation
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        // // super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
//        super.attachBaseContext(newBase);
//    }


}
