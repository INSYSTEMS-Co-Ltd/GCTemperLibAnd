package com.greencross.gctemperlib.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.R;


/**
 * Created by jihoon on 2016-03-21.
 * 커스텀 팝업 다이얼로그
 * @since 0, 1
 */
public class ApplinkDialog extends Dialog {
    private static final String TAG = ApplinkDialog.class.getSimpleName();

    private LinearLayout mTitleLayout;
    private TextView mTitleView;
    private TextView mMessageView;
    private Button mNoButton;
    private View viewTerm;
    private Button mOkButton;
    private String mTitle;
    private String mMessage;
    private LinearLayout mContentLayout;
    private LinearLayout mBtnLayout;
    private TextView mServiceapp_name;

    private View.OnClickListener mNoClickListener;
    private View.OnClickListener mOkClickListener;
    private static DismissListener mDismissListener;

    private static ApplinkDialog instance;

    private LinearLayout mAppLayout1, mAppLayout2;

    private View mView_middle;

    private static View mView;
    private static Context mContext;

    private String mLinkImgUrl;
    private String mWidth;
    private String mHeight;
    private String mLinkService;
    private String mLinkParam1;
    private String mLinkParam2;
    private String mLinkPackageName;
    private String mLinkTitle;
    private String mLinkDiscription;
    private String mLinkBt1Title;
    private String mLinkBt2Title;
    private boolean mAppLinkMode = false;
    private String mLinkSmsimg;
    private String mLinkurl;
    private String mLinkSmsurl;


    private boolean mIsMission = false;

    private static ApplinkDialog getInstance(Context context) {
        instance = new ApplinkDialog(context);
        initDialog(instance);
        return instance;
    }

    private static ApplinkDialog getInstance(Activity activity) {
        instance = new ApplinkDialog(activity);
        initDialog(instance);
        return instance;
    }

    private static void initDialog(ApplinkDialog instance) {
        if (instance == null) {
            return;
        }

        if (mDismissListener != null)
            mDismissListener = null;

        if (instance.mTitleView != null)
            instance.mTitleView.setText(instance.getContext().getString(R.string.text_alert));

        instance.setOnDismissListener(null);

        if (instance.isShowing() == false)
            instance.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.applink_dialog);

        setLayout();
        //setClickListener();
    }


    public ApplinkDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static ApplinkDialog showDlg(Context context) {
        ApplinkDialog dlg = getInstance(context);
        dlg.setTitle("공유");
        //dlg.setGiftCode(code);

        return dlg;
    }

    public static ApplinkDialog showDlg(Context context, View view) {
        mView = view;
        mContext = context;
        final ApplinkDialog dlg = getInstance(context);
        dlg.mContentLayout.removeAllViews();
        dlg.mContentLayout.addView(view);
        dlg.viewTerm.setVisibility(View.GONE);
        dlg.mNoButton.setText(context.getText(R.string.popup_dialog_button_cancel));
        dlg.mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        dlg.mNoButton.setVisibility(View.VISIBLE);
        dlg.mBtnLayout.setVisibility(View.VISIBLE);

        return dlg;
    }

//    private View.OnClickListener mApplinkClicklistener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(v.getId() == R.id.kakaolink_layout){
////                final KakaoLinkUtil kakao = new KakaoLinkUtil();
//
//                //업로드
////                File imageFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/NewFolder/photo_2018-10-10_18-10-41.jpg");
////
////                KakaoLinkService.getInstance().uploadImage(getContext(), false, imageFile, new ResponseCallback<ImageUploadResponse>(){
////                    @Override
////                    public void onFailure(ErrorResult errorResult) {
////                        Logger.e("KakaoLinkError", errorResult.toString());
////                    }
////
////                    @Override
////                    public void onSuccess(ImageUploadResponse result) {
////                        Logger.d("KakaoLinkSuccess", result.getOriginal().getUrl());
////                        //Url = result.getOriginal().getUrl();
////                        mLinkImgUrl = result.getOriginal().getUrl();
////                        kakao.sendKakaoMessage(mContext, "타이틀", mLinkImgUrl, "내용");
////                    }
////                });
//
//                switch(mLinkService){
//                    case "img": // 웹링크 호출
//                        mWidth = "600";
//                        mHeight = "600";
//                        break;
//                    case "7": //알리미
//                        mAppLinkMode = true;
//                        mWidth = "600";
//                        mHeight = "600";
//                        break;
//                }
//
////                kakao.sendKakaoMessage(mContext, mLinkTitle, mLinkImgUrl, mWidth, mHeight, mLinkDiscription, mLinkService, mLinkParam1, mLinkParam2,mLinkBt1Title,mLinkBt2Title,mAppLinkMode,mLinkurl);
//
//                //kakao.sendKakaoMessage(mContext, "타이틀", "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png", "내용");
//                //kakao.sendKakaoMessage(mContext, "타이틀", "https://www.logaster.com/image/logo/1/netar-13816-240.png", "내용");
//                //kakao.sendKakaoMessage(mContext, "타이틀", "https://t1.daumcdn.net/cfile/tistory/2673A14753355A5F17.jpeg", "내용");
//                //kakao.sendKakaoMessage(mContext, "타이틀", "android.resource://" + getContext().getPackageName() + "/drawable/after_vi_001", "내용");
//                //kakao.sendKakaoMessage(mContext, "타이틀", Environment.getExternalStorageDirectory() + "/DCIM/Camera/NewFolder/nature-3583730_1280", "내용");
//                //kakao.sendKakaoMessage(mContext, "타이틀", "file:///android_asset/large_image.jpg", "내용");
//
//                ApplinkDialog.this.dismiss();
//            }
//            else if(v.getId() == R.id.message_layout){
//                KakaoLinkUtil.sendImgSMS(mContext, mLinkTitle, mLinkDiscription,mLinkSmsimg,mLinkSmsurl);
//                ApplinkDialog.this.dismiss();
//            }
//        }
//    };

    /**
     * 타이틀 세팅
     * 타이틀이 없으면 타이틀 영역을 Gone 처리
     * @param title
     */
    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            mTitleLayout.setVisibility(View.GONE);
        } else {
            mTitleLayout.setVisibility(View.VISIBLE);
            mTitleView.setText(title);
        }
    }


    public void setSharing(String imgUrl, String linkService, String linkParam1, String linkParam2, String Title,String description,String Bt1,String Bt2,boolean btn,String smsimg,String url,String smsurl){
        setTitle("공유하기");
        mLinkImgUrl = imgUrl;
        mLinkService = linkService;
        mLinkParam1 = linkParam1;
        mLinkParam2 = linkParam2;
        mLinkTitle = Title;
        mLinkDiscription = description;
        mLinkBt1Title = Bt1;
        mLinkBt2Title = Bt2;
        mLinkSmsimg = smsimg;
        mLinkurl = url;
        mLinkSmsurl = smsurl;

        mAppLayout1 = mView.findViewById(R.id.kakaolink_layout);
        mAppLayout2 = mView.findViewById(R.id.message_layout);


        mView_middle = mView.findViewById(R.id.applink_view);

        mAppLayout1.setVisibility(View.GONE);
        mAppLayout2.setVisibility(View.GONE);
        mView_middle.setVisibility(View.GONE);

//        mAppLayout1.setOnClickListener(mApplinkClicklistener);
//        mAppLayout2.setOnClickListener(mApplinkClicklistener);

        mAppLayout1.setVisibility(View.VISIBLE);
        mAppLayout2.setVisibility(View.VISIBLE);
        mView_middle.setVisibility(View.VISIBLE);
    }

    public void setShortcut(String webUrl, String packageName,int img,String app_name){
        setTitle("바로가기");

        mLinkImgUrl = webUrl;
        mLinkPackageName = packageName;

        mAppLayout1 = mView.findViewById(R.id.kakaolink_layout);
        mAppLayout2 = mView.findViewById(R.id.message_layout);


        mView_middle = mView.findViewById(R.id.applink_view);

        mAppLayout1.setVisibility(View.GONE);
        mAppLayout2.setVisibility(View.GONE);
        mView_middle.setVisibility(View.GONE);

        mServiceapp_name.setText(app_name);





        if(!packageName.isEmpty()) {
            mView_middle.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void dismiss() {
        if(mIsMission){
            mDialogResult.finish("finish");
        }
        super.dismiss();

        if (mDismissListener != null)
            mDismissListener.onDissmiss();

    }

    public interface DismissListener {
        void onDissmiss();
    }

    OnDialogResult mDialogResult;

    public void setDialogResult(OnDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnDialogResult {
        void finish(String result);
    }

    //    private void setAlertButtonClickListener(Button button, final View.OnClickListener clickListener) {
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (clickListener != null) {
//                    clickListener.onClick(v);
//
//                    if (mIsAutoDismiss) {
//                        HealthcounselGuideDialog.this.dismiss();
//                    }
//                } else {
//                    HealthcounselGuideDialog.this.dismiss();
//                }
//            }
//        });
//    }


    /*
     * Layout
     */
    public void setLayout() {


        mTitleLayout = findViewById(R.id.dialog_title_layout);
        mTitleView = (TextView) findViewById(R.id.dialog_title);

        mMessageView = (TextView) findViewById(R.id.dialog_content_tv);
        mNoButton = (Button) findViewById(R.id.dialog_btn_no);
        viewTerm = (View) findViewById(R.id.view_term);
        mOkButton = (Button) findViewById(R.id.dialog_btn_ok);
        mContentLayout = (LinearLayout) findViewById(R.id.dialog_content_layout);
        mBtnLayout = (LinearLayout) findViewById(R.id.dialog_btn_layout);
//        mPermission = (LinearLayout)findViewById(R.id.permission);
//        mFinish_btn = (Button)findViewById(R.id.finish_btn);
//        mConfirm_btn = (Button) findViewById(R.id.confirm_btn);
    }
}
