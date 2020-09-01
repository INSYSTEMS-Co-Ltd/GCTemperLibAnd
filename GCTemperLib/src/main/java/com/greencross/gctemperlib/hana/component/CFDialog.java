package com.greencross.gctemperlib.hana.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.util.Logger;


/**
 * 공통 메시지 다이얼로그
 */
public class CFDialog extends Dialog {
    private static final String TAG = CFDialog.class.getSimpleName();
    private boolean mIsAutoDismiss = true;

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

    private View.OnClickListener mNoClickListener;
    private View.OnClickListener mOkClickListener;
    private static DismissListener mDismissListener;

    private static CFDialog instance;

    private static CFDialog getInstance(Context context) {
        instance = new CFDialog(context);
        initDialog(instance);
        return instance;
    }

    private static CFDialog getInstance(Activity activity) {
        instance = new CFDialog(activity);
        initDialog(instance);
        return instance;
    }

    private static void initDialog(CFDialog instance) {
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
            instance.viewTerm.setVisibility(View.GONE);
        }

        if (instance.mBtnLayout != null)
            instance.mBtnLayout.setVisibility(View.GONE);

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

        setContentView(R.layout.custom_dialog);
        setLayout();
        setClickListener();
    }

    public CFDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static CFDialog showDlg(Context context, String message) {
        final CFDialog dlg = getInstance(context);
        dlg.setMessage(message);
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        return dlg;
    }

    public static CFDialog LoginshowDlg(Context context, String title, String message) {
        CFDialog dlg = getInstance(context);
        dlg.setTitle(title);
        dlg.setMessage(message);

        return dlg;
    }

    public static CFDialog showDlg(Context context, String title, String message) {
        CFDialog dlg = getInstance(context);
        dlg.setTitle(title);
        dlg.setMessage(message);

        return dlg;
    }

    public static CFDialog showDlg(Context context, String message, View.OnClickListener okListener) {
        CFDialog dlg = getInstance(context);
        dlg.setMessage(message);
        dlg.setOkButton(okListener);

        return dlg;
    }

    public static CFDialog showDlg(Context context, String title, String message, View.OnClickListener okListener) {
        CFDialog dlg = getInstance(context);
        dlg.setTitle(title);
        dlg.setMessage(message);
        dlg.setOkButton(okListener);

        return dlg;
    }

    public static CFDialog showDlg(Activity activity, String message, final DismissListener dismissListener) {
        final CFDialog dlg = getInstance(activity);
        mDismissListener = dismissListener;
        dlg.setMessage(message);
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        return dlg;
    }

    public static CFDialog showDlg(Context context, String message, final DismissListener dismissListener) {
        final CFDialog dlg = getInstance(context);
        mDismissListener = dismissListener;
        dlg.setMessage(message);
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        return dlg;
    }

    public static CFDialog showDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CFDialog dlg = getInstance(context);
        dlg.setMessage(message);

        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static CFDialog CallshowDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CFDialog dlg = getInstance(context);
        dlg.setMessage(message);
        dlg.mOkButton.setText("고객센터\n연결하기");
        dlg.mNoButton.setText("확인");
        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static CFDialog UpdateshowDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CFDialog dlg = getInstance(context);
        dlg.setMessage(message);
        dlg.mOkButton.setText("확인");
        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }


    public static CFDialog showDlg(Context context, View view) {
        CFDialog dlg = getInstance(context);
        dlg.mContentLayout.removeAllViews();
        dlg.mContentLayout.addView(view);
        dlg.mOkButton.setVisibility(View.GONE);
        dlg.mBtnLayout.setVisibility(View.GONE);

        return dlg;
    }

    public static CFDialog showDlg(Context context, int layout) {
        View view = LayoutInflater.from(context).inflate(layout, null);
        CFDialog dlg = getInstance(context);
        dlg.mContentLayout.removeAllViews();
        dlg.mContentLayout.addView(view);
        dlg.mOkButton.setVisibility(View.GONE);
        dlg.mBtnLayout.setVisibility(View.GONE);

        return dlg;
    }

    public static CFDialog showDlg(Context context, View view, View.OnClickListener okListener, View.OnClickListener noListener) {
        CFDialog dlg = getInstance(context);
        dlg.setContentView(view);

        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static void showDlg(Context context, String title, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CFDialog dlg = getInstance(context);

        dlg.setTitle(title);
        dlg.setMessage(message);
        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);
    }

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
            mTitleView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            mTitleView.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
            mTitleLayout.setBackgroundColor(getContext().getResources().getColor(R.color.color_93286C));
            mTitleLayout.setGravity(Gravity.CENTER);
        }
    }

    public void setMessage(String message) {
        mMessageView.setText(message);
    }

    public void setClickListener() {   //final View.OnClickListener noClickListener, final View.OnClickListener okClickListener) {
        Logger.i("", "setClickListener=" + mNoButton);

        if (mNoClickListener == null) {
            mNoButton.setVisibility(View.GONE);
            viewTerm.setVisibility(View.GONE);
        }

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoClickListener != null) {
                    mNoClickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CFDialog.this.dismiss();
                    }
                } else {
                    CFDialog.this.dismiss();
                }
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOkClickListener != null) {
                    mOkClickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CFDialog.this.dismiss();
                    }
                } else {
                    CFDialog.this.dismiss();
                }
            }
        });
    }

    /**
     * 왼쪽 버튼 세팅
     */
    public void setNoButton(View.OnClickListener noClickListener) {
        mBtnLayout.setVisibility(View.VISIBLE);
        mNoButton.setVisibility(View.VISIBLE);
        viewTerm.setVisibility(View.VISIBLE);
        setAlertButtonClickListener(mNoButton, noClickListener);
    }

    public void setNoButton(String label, View.OnClickListener noClickListener) {
        mBtnLayout.setVisibility(View.VISIBLE);
        mNoButton.setVisibility(View.VISIBLE);
        viewTerm.setVisibility(View.VISIBLE);
        mNoButton.setText(label);
        setAlertButtonClickListener(mNoButton, noClickListener);
    }

    /**
     * 오른쪽 버튼 세팅
     *
     * @param okClickListener
     */
    public void setOkButton(View.OnClickListener okClickListener) {
        mBtnLayout.setVisibility(View.VISIBLE);
        String label = mOkButton.getText().toString();
        setOkButton(label, okClickListener);
    }

    public void setOkButton(String label, final View.OnClickListener okClickListener) {
        this.mOkClickListener = okClickListener;
        mBtnLayout.setVisibility(View.VISIBLE);
        mOkButton.setVisibility(View.VISIBLE);
        mOkButton.setText(label);
        setAlertButtonClickListener(mOkButton, okClickListener);
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
                        CFDialog.this.dismiss();
                    }
                } else {
                    CFDialog.this.dismiss();
                }
            }
        });
    }

    /*
     * Layout
     */
    public void setLayout() {
        mTitleLayout = (LinearLayout) findViewById(R.id.dialog_title_layout);
        mTitleView = (TextView) findViewById(R.id.dialog_title);
        mMessageView = (TextView) findViewById(R.id.dialog_content_tv);
        mNoButton = (Button) findViewById(R.id.dialog_btn_no);
        viewTerm = (View) findViewById(R.id.view_term);
        mOkButton = (Button) findViewById(R.id.dialog_btn_ok);
        mContentLayout = (LinearLayout) findViewById(R.id.dialog_content_layout);
        mBtnLayout = (LinearLayout) findViewById(R.id.dialog_btn_layout);
    }
}
