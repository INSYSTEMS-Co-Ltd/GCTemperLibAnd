package com.greencross.gctemperlib.greencare.alram_fragment;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_asstb_kbtg_alimi;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_asstb_kbtg_alimi_view;
import com.greencross.gctemperlib.Alram.AlramMainActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.ApplinkDialog;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.BaseFragment;
import com.greencross.gctemperlib.greencare.base.CommonActionBar;
import com.greencross.gctemperlib.greencare.base.IBaseFragment;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.util.DownloadUtil;
import com.greencross.gctemperlib.greencare.util.IntentUtil;
import com.greencross.gctemperlib.greencare.util.Logger;

import java.util.List;

public class AlramContentFragment extends BaseFragment implements IBaseFragment, View.OnClickListener {
    private final String TAG = AlramMainActivity.class.getSimpleName();

    private TextView noticonTitle, noticonContent;
    private WebView mNotiWebview;
    private ScrollView mContent_scrollview;
    private ImageButton mRight_pdf_down;

    private String HTML_YN="N";

    private String Idx;
    private String mPdf_url;
    private AlramMainActivity activity;



    public static BaseFragment newInstance() {
        AlramContentFragment fragment = new AlramContentFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifier_content_fragment, container, false);

        activity = (AlramMainActivity) getActivity();

        noticonTitle = view.findViewById(R.id.noticontent_title);
        noticonContent =  view.findViewById(R.id.noticontent_content);
        mNotiWebview = (WebView) view.findViewById(R.id.noti_webview);
        mContent_scrollview = view.findViewById(R.id.content_scrollview);
        mRight_pdf_down = view.findViewById(R.id.right_pdf_down);

        if(getArguments() != null) {
            Idx = getArguments().getString("IDX");
            NotiDetail(Idx);
        }


        return view;
    }

    //알리미 상세페이지
    public void NotiDetail(String idx) {
        Tr_asstb_kbtg_alimi_view.RequestData requestData = new Tr_asstb_kbtg_alimi_view.RequestData();
        CommonData login = CommonData.getInstance(getContext());
        requestData.KBTA_IDX = idx;
        requestData.mber_sn = login.getMberSn();

        getData(getContext(), Tr_asstb_kbtg_alimi_view.class, requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_asstb_kbtg_alimi_view) {
                    Tr_asstb_kbtg_alimi_view data = (Tr_asstb_kbtg_alimi_view) obj;
                    List<Tr_asstb_kbtg_alimi.chlmReadern> chlmReadern = data.dataList;
                    if(data.data_yn.equals("Y")){
                        HTML_YN = chlmReadern.get(0).html_yn;
                        mPdf_url = chlmReadern.get(0).kbt_pdf;
                        noticonTitle.setText(chlmReadern.get(0).kbt);
                        if(mPdf_url.equals("")){
                            mRight_pdf_down.setVisibility(View.GONE);
                        }else{
                            mRight_pdf_down.setVisibility(View.VISIBLE);
                        }

                        if(HTML_YN.equals("N")) {
                            mNotiWebview.setVisibility(View.GONE);
                            mContent_scrollview.setVisibility(View.VISIBLE);
                            noticonContent.setText(chlmReadern.get(0).kbc);
                        }
                        else {
                            mNotiWebview.setVisibility(View.VISIBLE);
                            mContent_scrollview.setVisibility(View.GONE);
                            mNotiWebview.setWebViewClient(new WebViewClientClass());


                            WebSettings settings = mNotiWebview.getSettings();
                            // 자바스크립트 허용
                            settings.setJavaScriptEnabled(true);
                            settings.setLoadWithOverviewMode(true);
                            settings.setUseWideViewPort(false);
                            settings.setSupportZoom(true);
                            settings.setBuiltInZoomControls(false);
                            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                            settings.setDomStorageEnabled(true);
                            settings.setDefaultTextEncodingName("utf-8");
                            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

                            mNotiWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                            mNotiWebview.setScrollbarFadingEnabled(true);

                            // 스크롤바 없애기
                            mNotiWebview.setHorizontalScrollBarEnabled(false);
                            mNotiWebview.setVerticalScrollBarEnabled(false);
                            mNotiWebview.setBackgroundColor(0);
                            mNotiWebview.setPadding(0, 0, 0, 0);

                            String source = chlmReadern.get(0).kbc;
                            if (Build.VERSION.SDK_INT >= 24) {
                                Spanned spannedFromHtml = Html.fromHtml(WebViewEscape(source),Html.FROM_HTML_MODE_LEGACY);
                                Logger.i(TAG,WebViewEscape(source));
                                mNotiWebview.loadDataWithBaseURL(null,spannedFromHtml.toString(), "text/html", "UTF-8",null);
                            } else {
                                Spanned spannedFromHtml =Html.fromHtml(WebViewEscape(source));
                                mNotiWebview.loadDataWithBaseURL(null,spannedFromHtml.toString(), "text/html", "UTF-8",null);
                            }

                        }
                    }else{
                        Logger.i(TAG,"KA002 : 알리미 글 존재 안함.");
                    }



                }
            }
        }, null);
    }

    private String getAppname(String PK){
        String name ="";
        try {
            name = (String) getContext().getPackageManager().getApplicationLabel(getContext().getPackageManager().getApplicationInfo(PK, PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return name;
    }


    private String WebViewEscape(String badString)
    {
        return badString.replace("&amp;quot;", "\"");

    }

    @Override
    public void loadActionbar(CommonActionBar actionBar) {
//        getToolBar().setVisibility(View.GONE);
        actionBar.setWhiteTheme();
        actionBar.setActionBarTitle( getString(R.string.text_weight_input));
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.share_btn).setOnClickListener(this);
        mRight_pdf_down.setOnClickListener(this);


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.share_btn) {//공유
            String imgUrl = "https://wkd.walkie.co.kr/HL_FV/info/image/01_16.png";

            View view = LayoutInflater.from(getContext()).inflate(R.layout.applink_dialog_layout, null);
            ApplinkDialog dlg = ApplinkDialog.showDlg(getContext(), view);
            dlg.setSharing(imgUrl, "7", Idx, "", "[현대해상 " + getAppname(getContext().getPackageName()) + "]", noticonTitle.getText().toString(), "자세히보기", "", false, "alrimi.png", "", "https://wkd.walkie.co.kr/HL_FV/info/start.html?pakagename=com.appmd.hi.gngcare&tabletname=&urlschema=appmd&service=7&param1=" + Idx + "&param2=");
        } else if (id == R.id.right_pdf_down) {
            CDialog.showDlg(getContext(), getString(R.string.pdf_down_text1), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = mPdf_url;
                    new DownloadUtil().startDownload(getActivity(), url, "Noti_" + Idx + ".pdf", "Noti_" + Idx, new DownloadUtil.IDownloadReceiver() {
                        @Override
                        public void success(String filePath) {
                            IntentUtil.sharePdfFile(getActivity(), filePath);
                        }

                        @Override
                        public void fail() {
                            Toast.makeText(getActivity(), "다운로드 취소 : ", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private class WebViewClientClass extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return true;
//        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.i(TAG, "onPageFinished.hideprogress");
            activity.hideProgress();
            // progress 가 해지 안 될 경우 처리
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null)
                        activity.hideProgress();
                }
            }, 1 * 500);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Logger.i(TAG, "onPageStarted.showProgress");
            activity.showProgress();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Logger.i(TAG, "onReceivedError.hideprogress");
            activity.hideProgress();
            // progress 가 해지 안 될 경우 처리
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null)
                        activity.hideProgress();
                }
            }, 1 * 500);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Logger.i(TAG, "onReceivedHttpError.hideprogress");
            activity.hideProgress();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Logger.i(TAG, "onReceivedSslError.hideprogress");
            activity.hideProgress();
        }
    }

    @Override
    public void onBackPressed() {
        if (mNotiWebview.canGoBack()) {
            mNotiWebview.goBack();
        } else {
            super.onBackPressed();
        }

    }
}
