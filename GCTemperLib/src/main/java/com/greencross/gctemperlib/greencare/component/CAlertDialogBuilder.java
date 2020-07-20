package com.greencross.gctemperlib.greencare.component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 공통 메시지 다이얼로그
 */
public class CAlertDialogBuilder extends AlertDialog.Builder {
    private static final String TAG = CAlertDialogBuilder.class.getSimpleName();
    private boolean mIsAutoDismiss = true;

    private AlertDialog mAlertDialog;
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
//    private static DismissListener mDismissListener;


    public CAlertDialogBuilder(Context context) {
        super(context);
        setTitle("");
//        init();
    }

    public CAlertDialogBuilder(Context context, int themeResId) {
        super(context, themeResId);
        setTitle("알림");
//        init();
    }

    public View init() {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.alertdialog_layout, null);
//        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
//        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        lpWindow.dimAmount = 0.8f;
//
//        setLayout(view);
//        setView(view);

        return null;

//        mAlertDialog.getWindow().setAttributes(lpWindow);
    }

    @Override
    public AlertDialog create() {
        mAlertDialog = super.create();
        return mAlertDialog;
    }

    public void setLayout(View view) {
//        mNoButton = view.findViewById(R.id.dialog_btn_no);
//        viewTerm = view.findViewById(R.id.view_term);
//        mOkButton = view.findViewById(R.id.dialog_btn_ok);
//        mContentLayout = view.findViewById(R.id.dialog_content_layout);
//        mBtnLayout = view.findViewById(R.id.dialog_btn_layout);

        mBtnLayout.setVisibility(View.GONE);
        mOkButton.setVisibility(View.GONE);
        mNoButton.setVisibility(View.GONE);
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

    private void setAlertButtonClickListener(Button button, final View.OnClickListener clickListener) {
        if (mNoButton.getVisibility() == View.VISIBLE && mOkButton.getVisibility() == View.VISIBLE) {
            viewTerm.setVisibility(View.VISIBLE);
        } else {
            viewTerm.setVisibility(View.GONE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);

                    if (mAlertDialog != null && mIsAutoDismiss) {
                        mAlertDialog.dismiss();
                    }
                } else {
                    if (mAlertDialog != null)
                        mAlertDialog.dismiss();
                }
            }
        });
    }


    @Override
    public AlertDialog.Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        return super.setOnDismissListener(onDismissListener);
    }


//    /**
//     * 리스트 아이템 선택 Dialog
//     */
//    public static AlertDialog selectDialog(Context context, String title, DialogInterface.OnClickListener listener) {
//        final String[] mStrList = context.getResources().getStringArray(R.array.emails);
//        ListAdapter la = new ListAdapter(context, mStrList);
//
//        CAlertDialogBuilder builder = new CAlertDialogBuilder(context);
//        if (TextUtils.isEmpty(title) == false)
//            builder.setTitle(title);
//        builder.setSingleChoiceItems(la, -1, listener);
//
//        AlertDialog alert = builder.create();
//        alert.show();
//
//        return alert;
//    }

    public static class ListAdapter extends BaseAdapter {
        private Context mContext;
        private String[] mStrList;
        public  ListAdapter(Context context, String[] strList){
            mContext = context;
            mStrList = strList;
        }

        @Override
        public int getCount() {
            return mStrList.length;
        }

        @Override
        public String getItem(int position) {
            return mStrList[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new ListAdapter.ViewHolder();
//                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_list_item_textview, null);
//                holder.tv = view.findViewById(R.id.list_item_textview);
//
//                convertView = view;
                convertView.setTag(holder);
            } else {
                holder = (ListAdapter.ViewHolder)convertView.getTag();
            }

            holder.tv.setText(getItem(position));
            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
//        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        lpWindow.dimAmount = 0.8f;
//        getWindow().setAttributes(lpWindow);
//
//        setContentView(R.layout.custom_dialog);
//        setLayout();
//        setClickListener();
//    }
//
//    public CAlertDialogBuilder(Context context) {
//        // Dialog 배경을 투명 처리 해준다.
//        super(context, android.R.style.Theme_Translucent_NoTitleBar);
//    }
//
//    public static CAlertDialogBuilder showDlg(Context context, String message) {
//        final CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.setMessage(message);
//        mAlertDialog.mOkButton.setVisibility(View.VISIBLE);
//        mAlertDialog.mOkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder LoginshowDlg(Context context, String title, String message) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.setTitle(title);
//        mAlertDialog.setMessage(message);
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder showDlg(Context context, String title, String message) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.setTitle(title);
//        mAlertDialog.setMessage(message);
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder showDlg(Context context, String message, View.OnClickListener okListener) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.setMessage(message);
//        mAlertDialog.setOkButton(okListener);
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder showDlg(Context context, String title, String message, View.OnClickListener okListener) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.setTitle(title);
//        mAlertDialog.setMessage(message);
//        mAlertDialog.setOkButton(okListener);
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder showDlg(Activity activity, String message, final CAlertDialogBuilder.DismissListener dismissListener) {
//        final CAlertDialogBuilder mAlertDialog = getInstance(activity);
//        mDismissListener = dismissListener;
//        mAlertDialog.setMessage(message);
//        mAlertDialog.mOkButton.setVisibility(View.VISIBLE);
//        mAlertDialog.mOkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder showDlg(Context context, String message, final CAlertDialogBuilder.DismissListener dismissListener) {
//        final CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mDismissListener = dismissListener;
//        mAlertDialog.setMessage(message);
//        mAlertDialog.mOkButton.setVisibility(View.VISIBLE);
//        mAlertDialog.mOkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAlertDialog.dismiss();
//            }
//        });
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder showDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.setMessage(message);
//
//        mAlertDialog.setOkButton(okListener);
//        mAlertDialog.setNoButton(noListener);
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder UpdateshowDlg(Context context, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.setMessage(message);
//        mAlertDialog.mOkButton.setText("업데이트");
//        mAlertDialog.setOkButton(okListener);
//        mAlertDialog.setNoButton(noListener);
//
//        return mAlertDialog;
//    }
//
//
//    public static CAlertDialogBuilder showDlg(Context context, View view) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.mContentLayout.removeAllViews();
//        mAlertDialog.mContentLayout.addView(view);
//        mAlertDialog.mOkButton.setVisibility(View.GONE);
//        mAlertDialog.mBtnLayout.setVisibility(View.GONE);
//        mAlertDialog.setTitle("지역 선택");
//
//        return mAlertDialog;
//    }
//
//    public static CAlertDialogBuilder showDlg(Context context, View view, View.OnClickListener okListener, View.OnClickListener noListener) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//        mAlertDialog.setContentView(view);
//
//        mAlertDialog.setOkButton(okListener);
//        mAlertDialog.setNoButton(noListener);
//
//        return mAlertDialog;
//    }
//
//    public static void showDlg(Context context, String title, String message, View.OnClickListener okListener, View.OnClickListener noListener) {
//        CAlertDialogBuilder mAlertDialog = getInstance(context);
//
//        mAlertDialog.setTitle(title);
//        mAlertDialog.setMessage(message);
//        mAlertDialog.setOkButton(okListener);
//        mAlertDialog.setNoButton(noListener);
//    }
//
//    public void setTitle(String title) {
//        mTitleView.setText(title);
//    }
//
//    public void setMessage(String message) {
//        mMessageView.setText(message);
//    }
//
//    private void setClickListener() {   //final View.OnClickListener noClickListener, final View.OnClickListener okClickListener) {
//        Logger.i("", "setClickListener=" + mNoButton);
//
//        if (mNoClickListener == null) {
//            mNoButton.setVisibility(View.GONE);
//            viewTerm.setVisibility(View.GONE);
//        }
//
//        mNoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mNoClickListener != null) {
//                    mNoClickListener.onClick(v);
//
//                    if (mIsAutoDismiss) {
//                        CAlertDialogBuilder.this.dismiss();
//                    }
//                } else {
//                    CAlertDialogBuilder.this.dismiss();
//                }
//            }
//        });
//
//        mOkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOkClickListener != null) {
//                    mOkClickListener.onClick(v);
//
//                    if (mIsAutoDismiss) {
//                        CAlertDialogBuilder.this.dismiss();
//                    }
//                } else {
//                    CAlertDialogBuilder.this.dismiss();
//                }
//            }
//        });
//    }
//
//    /**
//     * 왼쪽 버튼 세팅
//     */
//    public void setNoButton(View.OnClickListener noClickListener) {
//        mBtnLayout.setVisibility(View.VISIBLE);
//        mNoButton.setVisibility(View.VISIBLE);
//        viewTerm.setVisibility(View.VISIBLE);
//        setAlertButtonClickListener(mNoButton, noClickListener);
//    }
//
//    public void setNoButton(String label, View.OnClickListener noClickListener) {
//        mBtnLayout.setVisibility(View.VISIBLE);
//        mNoButton.setVisibility(View.VISIBLE);
//        viewTerm.setVisibility(View.VISIBLE);
//        mNoButton.setText(label);
//        setAlertButtonClickListener(mNoButton, noClickListener);
//    }
//
//    /**
//     * 오른쪽 버튼 세팅
//     *
//     * @param okClickListener
//     */
//    public void setOkButton(View.OnClickListener okClickListener) {
//        mBtnLayout.setVisibility(View.VISIBLE);
//        String label = mOkButton.getText().toString();
//        setOkButton(label, okClickListener);
//    }
//
//    public void setOkButton(String label, final View.OnClickListener okClickListener) {
//        this.mOkClickListener = okClickListener;
//        mBtnLayout.setVisibility(View.VISIBLE);
//        mOkButton.setVisibility(View.VISIBLE);
//        mOkButton.setText(label);
//        setAlertButtonClickListener(mOkButton, okClickListener);
//    }
//
//    public void setOkButtonDissmissListenr(Button button, View.OnClickListener clickListener) {
//        this.mOkClickListener = clickListener;
//    }
//
//    public void setNoButtonDissmissListenr(Button button, View.OnClickListener clickListener) {
//        button.hasOnClickListeners();
//    }
//
//    public interface DismissListener {
//        void onDissmiss();
//    }
//
//    private void setAlertButtonClickListener(Button button, final View.OnClickListener clickListener) {
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (clickListener != null) {
//                    clickListener.onClick(v);
//
//                    if (mIsAutoDismiss) {
//                        CAlertDialogBuilder.this.dismiss();
//                    }
//                } else {
//                    CAlertDialogBuilder.this.dismiss();
//                }
//            }
//        });
//    }
//
//    /*
//     * Layout
//     */
//    private void setLayout() {
//        mTitleView = (TextView) findViewById(R.id.dialog_title);
//        mMessageView = (TextView) findViewById(R.id.dialog_content_tv);
//        mNoButton = (Button) findViewById(R.id.dialog_btn_no);
//        viewTerm = (View) findViewById(R.id.view_term);
//        mOkButton = (Button) findViewById(R.id.dialog_btn_ok);
//        mContentLayout = (LinearLayout) findViewById(R.id.dialog_content_layout);
//        mBtnLayout = (LinearLayout) findViewById(R.id.dialog_btn_layout);
//    }
}