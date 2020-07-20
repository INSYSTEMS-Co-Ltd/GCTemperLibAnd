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
public class CPeriodDialog extends Dialog {
    private static final String TAG = CPeriodDialog.class.getSimpleName();
    private boolean mIsAutoDismiss = true;

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

    private static CPeriodDialog instance;

    private static CPeriodDialog getInstance(Context context) {
        instance = new CPeriodDialog(context);
        initDialog(instance);
        return instance;
    }

    private static CPeriodDialog getInstance(Activity activity) {
        instance = new CPeriodDialog(activity);
        initDialog(instance);
        return instance;
    }

    private static void initDialog(CPeriodDialog instance) {
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
            instance.mTitleView.setText("알림");

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

        setContentView(R.layout.popup_dialog_period_type);
        setLayout();
        setClickListener();
    }

    public CPeriodDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static CPeriodDialog showDlg(Context context, String message) {
        final CPeriodDialog dlg = getInstance(context);
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

    public static CPeriodDialog LoginshowDlg(Context context, String title, String message) {
        CPeriodDialog dlg = getInstance(context);
        dlg.setTitle(title);
        dlg.setMessage(message);

        return dlg;
    }

    public static CPeriodDialog showDlg(Context context, String title, String message) {
        CPeriodDialog dlg = getInstance(context);
        dlg.setTitle(title);
        dlg.setMessage(message);

        return dlg;
    }

    public static CPeriodDialog showDlg(Context context, String message, View.OnClickListener okListener) {
        CPeriodDialog dlg = getInstance(context);
        dlg.setMessage(message);
        dlg.setOkButton(okListener);

        return dlg;
    }

    public static CPeriodDialog showDlg(Context context, String title, String message, View.OnClickListener okListener) {
        CPeriodDialog dlg = getInstance(context);
        dlg.setTitle(title);
        dlg.setMessage(message);
        dlg.setOkButton(okListener);

        return dlg;
    }

    public static CPeriodDialog showDlg(Activity activity, String message, final CPeriodDialog.DismissListener dismissListener) {
        final CPeriodDialog dlg = getInstance(activity);
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

    public static CPeriodDialog showDlg(Context context, String message, final CPeriodDialog.DismissListener dismissListener) {
        final CPeriodDialog dlg = getInstance(context);
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

    public static CPeriodDialog showDlg(Context context, View view, final CPeriodDialog.DismissListener dismissListener) {
        CPeriodDialog dlg = getInstance(context);
        dlg.mContentLayout.removeAllViews();
        dlg.mContentLayout.addView(view);
        mDismissListener = dismissListener;
        dlg.mOkButton.setVisibility(View.VISIBLE);
        dlg.mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        return dlg;
    }

    public static CPeriodDialog showDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CPeriodDialog dlg = getInstance(context);
        dlg.setMessage(message);

        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static CPeriodDialog CallshowDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CPeriodDialog dlg = getInstance(context);
        dlg.setMessage(message);
        dlg.mOkButton.setText("연결하기");
        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static CPeriodDialog UpdateshowDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CPeriodDialog dlg = getInstance(context);
        dlg.setMessage(message);
        dlg.mOkButton.setText("확인");
        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }


    public static CPeriodDialog showDlg(Context context, View view) {
        CPeriodDialog dlg = getInstance(context);
        dlg.mContentLayout.removeAllViews();
        dlg.mContentLayout.addView(view);
        dlg.mOkButton.setVisibility(View.GONE);
        dlg.mBtnLayout.setVisibility(View.GONE);

        return dlg;
    }

    public static CPeriodDialog showDlg(Context context, int layout) {
        View view = LayoutInflater.from(context).inflate(layout, null);
        CPeriodDialog dlg = getInstance(context);
        dlg.mContentLayout.removeAllViews();
        dlg.mContentLayout.addView(view);
        dlg.mOkButton.setVisibility(View.GONE);
        dlg.mBtnLayout.setVisibility(View.GONE);

        return dlg;
    }

    public static CPeriodDialog showDlg(Context context, View view, View.OnClickListener okListener, View.OnClickListener noListener) {
        CPeriodDialog dlg = getInstance(context);
        dlg.setContentView(view);

        dlg.setOkButton(okListener);
        dlg.setNoButton(noListener);

        return dlg;
    }

    public static void showDlg(Context context, String title, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
        CPeriodDialog dlg = getInstance(context);

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
        } else {
            mTitleView.setText(title);
        }
    }

    public void setMessage(String message) {
        mMessageView.setText(message);
    }

    public void setClickListener() {   //final View.OnClickListener noClickListener, final View.OnClickListener okClickListener) {
        Logger.i("", "setClickListener=" + mNoButton);

        if (mNoClickListener == null) {
            mNoButton.setVisibility(View.GONE);
        }

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoClickListener != null) {
                    mNoClickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CPeriodDialog.this.dismiss();
                    }
                } else {
                    CPeriodDialog.this.dismiss();
                }
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOkClickListener != null) {
                    mOkClickListener.onClick(v);

                    if (mIsAutoDismiss) {
                        CPeriodDialog.this.dismiss();
                    }
                } else {
                    CPeriodDialog.this.dismiss();
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
        setAlertButtonClickListener(mNoButton, noClickListener);
    }

    public void setNoButton(String label, View.OnClickListener noClickListener) {
        mBtnLayout.setVisibility(View.VISIBLE);
        mNoButton.setVisibility(View.VISIBLE);
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
                        CPeriodDialog.this.dismiss();
                    }
                } else {
                    CPeriodDialog.this.dismiss();
                }
            }
        });
    }

    /*
     * Layout
     */
    public void setLayout() {
        mTitleView = (TextView) findViewById(R.id.dialog_title);
        mMessageView = (TextView) findViewById(R.id.dialog_content_tv);
        mNoButton = (Button) findViewById(R.id.dialog_btn_no);
        mOkButton = (Button) findViewById(R.id.confirm_btn);
        mContentLayout = (LinearLayout) findViewById(R.id.dialog_content);
        mBtnLayout = (LinearLayout) findViewById(R.id.dialog_a_type_button_layout);
    }
}