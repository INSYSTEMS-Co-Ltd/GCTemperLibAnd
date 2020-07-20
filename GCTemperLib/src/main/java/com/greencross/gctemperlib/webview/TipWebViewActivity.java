package com.greencross.gctemperlib.webview;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.greencross.gctemperlib.base.BackBaseActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.greencare.component.OnClickListener;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.BuildConfig;
import com.greencross.gctemperlib.R;

import java.util.HashMap;


/**
 * Created by jihoon on 2016-01-06.
 * 뒤로가기 웹뷰 클래스
 * @since 0, 1
 */
public class TipWebViewActivity extends BackBaseActivity implements View.OnClickListener{

    private WebView mWebView;
    private Intent mIntent;
    private String		mUrl_before;
    private String		mUrl_after;
    private String		mTitle;

    private ImageView mBackImg;
    private LinearLayout mMom_before;
    private LinearLayout mMom_after;
    private TextView mBefore;
    private TextView mAfter;

    private RadioButton mCategory1,mCategory2,mCategory3,mCategory4,mCategory5;

    private String Pregmentkey;
    private String PregmentValue;

    private int position;
    private int idx;
    private HashMap<String,String> mUrl = new HashMap<String,String>();

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_manage);

        View view1 = (View) findViewById(R.id.top);
        TextView title = (TextView) view1.findViewById(R.id.common_title_tv);

        mBackImg    = getBackImg();
        mBackImg.setOnClickListener(this);

        mIntent		=	getIntent();
        position		=	mIntent.getIntExtra(CommonData.EXTRA_URL_POSITION,0);
        mTitle      =   mIntent.getStringExtra("Title");
        Pregmentkey = mIntent.getStringExtra("Pregmentkey")==null?"":mIntent.getStringExtra("Pregmentkey");
        PregmentValue = mIntent.getStringExtra("PregmentValue")==null?"":mIntent.getStringExtra("PregmentValue");



        mBefore = (TextView) findViewById(R.id.btn_mom_before);
        mAfter = (TextView) findViewById(R.id.btn_mom_after);
        mWebView	=	(WebView) findViewById(R.id.mom_web_view);
        mCategory1 = (RadioButton) findViewById(R.id.category1);
        mCategory2 = (RadioButton) findViewById(R.id.category2);
        mCategory3 = (RadioButton) findViewById(R.id.category3);
        mCategory4 = (RadioButton) findViewById(R.id.category4);
        mCategory5 = (RadioButton) findViewById(R.id.category5);

        view = findViewById(R.id.root_view);

        String CommonUrl = "http://www.higngkids.co.kr/auth/HL_TIP_contents_view.asp?wkey=";

        mUrl.put("weight_before",CommonUrl+"LK0201001");
        mUrl.put("weight_after",CommonUrl+"LK0202001");
        mUrl.put("walk_before",CommonUrl+"LK0201002");
        mUrl.put("walk_after",CommonUrl+"LK0202002");
        mUrl.put("meal_before",CommonUrl+"LK0201003");
        mUrl.put("meal_after",CommonUrl+"LK0202003");
        mUrl.put("pressure_before",CommonUrl+"LK0201004");
        mUrl.put("pressure_after",CommonUrl+"LK0202004");
        mUrl.put("sugar_before",CommonUrl+"LK0201005");
        mUrl.put("sugar_after",CommonUrl+"LK0202005");


        if(CommonData.getInstance(this).getbirth_chl_yn().compareTo("Y") == 0){
            setCategory(position,1);
        }
        else{
            setCategory(position,0);
        }





        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true); // 줌 허용
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mWebView.getSettings().setTextZoom(100);

        // 웹뷰 디버깅 설정
        if(BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
            }
        }

        mWebView.setBackgroundColor(Color.TRANSPARENT);

        mWebView.setWebViewClient(new TermsWebViewClinet());
        mWebView.setWebChromeClient(new TermsWebViewChromeClient());

        mBefore.setOnClickListener(this);
        mAfter.setOnClickListener(this);
        mCategory1.setOnClickListener(this);
        mCategory2.setOnClickListener(this);
        mCategory3.setOnClickListener(this);
        mCategory4.setOnClickListener(this);
        mCategory5.setOnClickListener(this);
        title.setText(mTitle);


        //click 저장
        OnClickListener mClickListener = new OnClickListener(this, view, TipWebViewActivity.this);

        //엄마 건강
        mCategory1.setOnTouchListener(mClickListener);
        mCategory2.setOnTouchListener(mClickListener);
        mCategory3.setOnTouchListener(mClickListener);
        mCategory4.setOnTouchListener(mClickListener);
        mCategory5.setOnTouchListener(mClickListener);

        //코드 부여(엄마 건강)
        mCategory1.setContentDescription(getString(R.string.Category1));
        mCategory2.setContentDescription(getString(R.string.Category2));
        mCategory3.setContentDescription(getString(R.string.Category3));
        mCategory4.setContentDescription(getString(R.string.Category4));
        mCategory5.setContentDescription(getString(R.string.Category5));



    }

    public void setTab(int _tabNum){
        if(_tabNum == 0){         // 맵
            idx = _tabNum;

            mBefore.setSelected(true);
            mAfter.setSelected(false);

            mBefore.setBackgroundResource(R.drawable.underline_mother);
            mAfter.setBackgroundResource(R.color.color_FB8AD3);
            mWebView.loadUrl(mUrl_before+PregmentValue);

        }else{
            idx = _tabNum;

            mAfter.setSelected(true);
            mBefore.setSelected(false);

            mAfter.setBackgroundResource(R.drawable.underline_mother);
            mBefore.setBackgroundResource(R.color.color_FB8AD3);
            mWebView.loadUrl(mUrl_after);
        }
    }

    public void setCategory(int pos,int idx){
        switch (pos){
            case 0 :
                mUrl_before = mUrl.get("weight_before");
                mUrl_after = mUrl.get("weight_after");
                mCategory1.setSelected(true);
                mCategory2.setSelected(false);
                mCategory3.setSelected(false);
                mCategory4.setSelected(false);
                mCategory5.setSelected(false);
                setTab(idx);
                break;
            case 1:
                mUrl_before = mUrl.get("walk_before");
                mUrl_after = mUrl.get("walk_after");
                mCategory1.setSelected(false);
                mCategory2.setSelected(true);
                mCategory3.setSelected(false);
                mCategory4.setSelected(false);
                mCategory5.setSelected(false);
                setTab(idx);
                break;
            case 2:
                mUrl_before = mUrl.get("meal_before");
                mUrl_after = mUrl.get("meal_after");
                mCategory1.setSelected(false);
                mCategory2.setSelected(false);
                mCategory3.setSelected(true);
                mCategory4.setSelected(false);
                mCategory5.setSelected(false);
                setTab(idx);
                break;
            case 3:
                mUrl_before = mUrl.get("pressure_before");
                mUrl_after = mUrl.get("pressure_after");
                mCategory1.setSelected(false);
                mCategory2.setSelected(false);
                mCategory3.setSelected(false);
                mCategory4.setSelected(true);
                mCategory5.setSelected(false);
                setTab(idx);
                break;
            case 4:
                mUrl_before = mUrl.get("sugar_before");
                mUrl_after = mUrl.get("sugar_after");
                mCategory1.setSelected(false);
                mCategory2.setSelected(false);
                mCategory3.setSelected(false);
                mCategory4.setSelected(false);
                mCategory5.setSelected(true);
                setTab(idx);
                break;
        }
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

        try{
            Class.forName("android.webkit.WebView")
                    .getMethod("onResume",(Class[]) null)
                    .invoke(mWebView, (Object[]) null);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause",(Class[]) null)
                    .invoke(mWebView, (Object[]) null);
        } catch (Exception e){
            e.printStackTrace();
        }

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
        if (id == R.id.common_left_btn) {/*if ( mWebView.canGoBack()) {

                    mWebView.goBack();

                    return;

                }else{
                    finish();
                }*/
            finish();
        } else if (id == R.id.btn_mom_before) { // 그래프 보기
            setTab(0);
        } else if (id == R.id.btn_mom_after) {
            setTab(1);
        } else if (id == R.id.category1) {
            setCategory(0, idx);
        } else if (id == R.id.category2) {
            setCategory(1, idx);
        } else if (id == R.id.category3) {
            setCategory(2, idx);
        } else if (id == R.id.category4) {
            setCategory(3, idx);
        } else if (id == R.id.category5) {
            setCategory(4, idx);
        }
    }


    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;

    private class TermsWebViewChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            // TODO Auto-generated method stub
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(mCustomView);
            mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mCustomViewCallback.onCustomViewHidden();
            mCustomViewCallback = null;
        }

        @Override
        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (mCustomView != null)
            {
                onHideCustomView();
                return;
            }

            mCustomView = paramView;
            mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            mOriginalOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            mCustomViewCallback = paramCustomViewCallback;

            ((FrameLayout)getWindow().getDecorView()).addView(mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);

        }
    }

    private class TermsWebViewClinet extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//            handler.proceed(); // SSL 에러가 발생해도 계속 진행!
            GLog.i("onReceivedSslError()", "dd");
            mDialog = new CustomAlertDialog(TipWebViewActivity.this, CustomAlertDialog.TYPE_B);
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
//            if(!Pregmentkey.equals("") && !PregmentValue.equals("")){
//                String script ="javascript:call_tab('"+Pregmentkey+"','"+PregmentValue+"')";
//
//                mWebView.loadUrl(script);
//                Log.i("WebViewTip", "script="+script);
//            }
            super.onPageFinished(view, url);
        }

    }
}
