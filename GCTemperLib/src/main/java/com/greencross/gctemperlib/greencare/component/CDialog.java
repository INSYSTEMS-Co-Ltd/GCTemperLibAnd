package com.greencross.gctemperlib.greencare.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.util.Logger;


/**
 * 공통 메시지 다이얼로그
 */
public class CDialog extends Dialog {
    private static final String TAG = CDialog.class.getSimpleName();
    private boolean mIsAutoDismiss = true;

//    private LinearLayout mTitleLayout;
    private TextView mTitleView;
    private TextView mMessageView;
    private Button mNoButton;
    private View viewTerm;
    private Button mOkButton;
    private String mTitle;
    private String mMessage;
    //private LinearLayout mContentLayout;
    private LinearLayout mContentLayout;
    private LinearLayout mBtnLayout;

    private View.OnClickListener mNoClickListener;
    private View.OnClickListener mOkClickListener;
    private static DismissListener mDismissListener;

    private static CDialog instance;

    private static CDialog getInstance(Context context) {
        instance = new CDialog(context);
        initDialog(instance);
        return instance;
    }

    private static CDialog getInstance(Activity activity) {
        instance = new CDialog(activity);
        initDialog(instance);
        return instance;
    }

    private static void initDialog(CDialog instance) {
        if (instance == null) {
            return;
        }

        if (instance.mOkButton != null) {
            instance.mOkButton.setVisibility(View.GONE);
            instance.mOkButton.setOnClickListener(null);
        }

        if (instance.mNoButton != null) {
            instance.mNoButton.setVisibility(View.GONE);
            instance.mNoButton.setOnClickListener(null);
            //instance.viewTerm.setVisibility(View.GONE);
        }

        if (instance.mBtnLayout != null)
            instance.mBtnLayout.setVisibility(View.GONE);

        if (mDismissListener != null)
            mDismissListener = null;

        //if (instance.mTitleView != null)
            //instance.mTitleView.setText("알림");

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

        setContentView(R.layout.popup_dialog_e_type);

        setLayout();
        setClickListener();
    }

    public CDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static CDialog showDlg(Context context, String message,boolean canelable) {
        final CDialog dlg = getInstance(context);
        dlg.setTitle(message);
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        dlg.setCancelable(canelable);
        return dlg;
    }

    public static CDialog showDlg(Context context, String message) {
        final CDialog dlg = getInstance(context);
        dlg.setTitle(message);
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setBackgroundResource(R.drawable.btn_confirm);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        return dlg;
    }

    public static CDialog LoginshowDlg(Context context, String title, String message) {
        CDialog dlg = getInstance(context);
        //dlg.setTitle(title);
        dlg.setTitle(message);

        return dlg;
    }

    public static CDialog showDlg(Context context, int title, int message) {
        return showDlg(context, context.getString(title), context.getString(message));
    }
    public static CDialog showDlg(Context context, String title, String message) {
        final CDialog dlg = getInstance(context);
        dlg.setTitle(message);
        dlg.setMessage(message);
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setBackgroundResource(R.drawable.btn_confirm);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        return dlg;
    }

    public static CDialog showDlg(Context context, String message, View.OnClickListener okListener) {
        CDialog dlg = getInstance(context);
        dlg.setTitle(message);
        dlg.setOkButton(okListener);

        return dlg;
    }

    public static CDialog showDlg(Context context, String title, String message, View.OnClickListener okListener) {
        CDialog dlg = getInstance(context);
        //dlg.setTitle(title);
        dlg.setTitle(message);
        dlg.setOkButton(okListener);

        return dlg;
    }

    public static CDialog showDlg(Activity activity, String message, final CDialog.DismissListener dismissListener) {
        final CDialog dlg = getInstance(activity);
        mDismissListener = dismissListener;
        dlg.setTitle(message);
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setBackgroundResource(R.drawable.btn_confirm);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        return dlg;
    }

    public static CDialog showDlg(Context context, String message, final CDialog.DismissListener dismissListener) {
        final CDialog dlg = getInstance(context);
        mDismissListener = dismissListener;
        dlg.setTitle(message);
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setBackgroundResource(R.drawable.btn_confirm);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        return dlg;
    }

    public static CDialog showDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CDialog dlg = getInstance(context);
        dlg.setTitle(message);

        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static CDialog CallshowDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CDialog dlg = getInstance(context);
        dlg.setTitle(message);
        dlg.mOkButton.setText("연결하기");
        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static CDialog UpdateshowDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CDialog dlg = getInstance(context);
        dlg.setTitle(message);
        dlg.mOkButton.setText("확인");
        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }


    public static CDialog showDlg(Context context, View view) {
        CDialog dlg = getInstance(context);
        dlg.mContentLayout.removeAllViews();
        dlg.mContentLayout.addView(view);
        dlg.mOkButton.setVisibility(View.GONE);
        dlg.mBtnLayout.setVisibility(View.GONE);

        return dlg;
    }

    public static CDialog showDlg(Context context, int layout) {
        View view = LayoutInflater.from(context).inflate(layout, null);
        CDialog dlg = getInstance(context);
        dlg.mContentLayout.removeAllViews();
        dlg.mContentLayout.addView(view);
        dlg.mOkButton.setVisibility(View.GONE);
        dlg.mOkButton.setBackgroundResource(R.drawable.btn_confirm);
        dlg.mBtnLayout.setVisibility(View.GONE);

        return dlg;
    }

    public static CDialog showDlg(Context context, View view, View.OnClickListener okListener, View.OnClickListener noListener) {
        CDialog dlg = getInstance(context);
        dlg.setContentView(view);

        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static void showDlg(Context context, String title, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CDialog dlg = getInstance(context);

        dlg.setMessage(title);
        dlg.setTitle(message);
        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);
    }

    /**
     * 타이틀 세팅
     * 타이틀이 없으면 타이틀 영역을 Gone 처리
     * @param message
     */
    public void setMessage(String message) {
        mMessageView.setText(message);
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            mTitleView.setVisibility(View.GONE);
//            mTitleLayout.setVisibility(View.GONE);
        } else {
            mTitleView.setVisibility(View.VISIBLE);
            mTitleView.setText(title);
        }
    }

    public void setClickListener() {   //final View.OnClickListener noClickListener, final View.OnClickListener okClickListener) {
        Logger.i("", "setClickListener=" + mNoButton);

        if (mNoClickListener == null) {
            mNoButton.setVisibility(View.GONE);
            //viewTerm.setVisibility(View.GONE);
        }

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoClickListener != null) {
                    mNoClickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CDialog.this.dismiss();
                    }
                } else {
                    CDialog.this.dismiss();
                }
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOkClickListener != null) {
                    mOkClickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CDialog.this.dismiss();
                    }
                } else {
                    CDialog.this.dismiss();
                }
            }
        });
    }

    /**
     * 왼쪽 버튼 세팅
     */
    public CDialog setNoButton(View.OnClickListener noClickListener) {
        mBtnLayout.setVisibility(View.VISIBLE);
        mNoButton.setVisibility(View.VISIBLE);
        //viewTerm.setVisibility(View.VISIBLE);
        setAlertButtonClickListener(mNoButton, noClickListener);
        return instance;
    }

    public CDialog setNoButton(String label, View.OnClickListener noClickListener) {
        mBtnLayout.setVisibility(View.VISIBLE);
        mNoButton.setVisibility(View.VISIBLE);
       // viewTerm.setVisibility(View.VISIBLE);
        mNoButton.setText(label);
        setAlertButtonClickListener(mNoButton, noClickListener);
        return instance;
    }

    /**
     * 오른쪽 버튼 세팅
     *
     * @param okClickListener
     */
    public CDialog setOkButton(View.OnClickListener okClickListener) {
        mBtnLayout.setVisibility(View.VISIBLE);
        String label = mOkButton.getText().toString();
        setOkButton(label, okClickListener);
        return instance;
    }

    public CDialog setOkButton(String label, final View.OnClickListener okClickListener) {
        this.mOkClickListener = okClickListener;
        mBtnLayout.setVisibility(View.VISIBLE);
        mOkButton.setVisibility(View.VISIBLE);
        mOkButton.setText(label);
        setAlertButtonClickListener(mOkButton, okClickListener);
        return instance;
    }


    public void setOkButtonDissmissListenr(Button button, View.OnClickListener clickListener) {
        this.mOkClickListener = clickListener;
    }

    public void setNoButtonDissmissListenr(Button button, View.OnClickListener clickListener) {
        button.hasOnClickListeners();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mDismissListener != null)
            mDismissListener.onDissmiss();
    }

    public interface DismissListener {
        void onDissmiss();
    }

    private void setAlertButtonClickListener(Button button, final View.OnClickListener clickListener) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CDialog.this.dismiss();
                    }
                } else {
                    CDialog.this.dismiss();
                }
            }
        });
    }

    /*
     * Layout
     */
    public void setLayout() {
        mTitleView = (TextView) findViewById(R.id.dialog_title_textview);
        mMessageView = (TextView) findViewById(R.id.dialog_content);
        mNoButton = (Button) findViewById(R.id.cancel_btn);
        mOkButton = (Button) findViewById(R.id.confirm_btn);
        mContentLayout = (LinearLayout) findViewById(R.id.dialog_layout);
        mBtnLayout = (LinearLayout) findViewById(R.id.dialog_b_type_button_layout);

        //mContentLayout = (LinearLayout) findViewById(R.id.dialog_content_layout);
        //mTitleLayout = (LinearLayout)findViewById(R.id.dialog_title_layout);
        //mMessageView = (TextView) findViewById(R.id.dialog_content_tv);
        //mTitleView = (TextView) findViewById(R.id.dialog_title);
        //mOkButton = (Button) findViewById(R.id.dialog_btn_ok);
        //viewTerm = (View) findViewById(R.id.view_term);
        //mNoButton = (Button) findViewById(R.id.dialog_btn_no);
        //mBtnLayout = (LinearLayout) findViewById(R.id.dialog_btn_layout);
    }

    public void setBackgroundOkBtn(int background){

        if(mOkButton!=null)
            mOkButton.setBackgroundResource(background);


    }
    public void setBackgroundNoBtn(int background){

        if(mNoButton!=null)
            mNoButton.setBackgroundResource(background);

    }

    public void setTextColorNoBtn(int color){

        if(mNoButton!=null)
            mNoButton.setTextColor(color);

    }

    public void setTextColorOkBtn(int color){

        if(mOkButton!=null)
            mOkButton.setTextColor(color);

    }
}