package com.greencross.gctemperlib.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.R;


/**
 * Created by jihoon on 2016-03-21.
 * 커스텀 팝업 다이얼로그
 *
 * @since 0, 1
 */
public class CustomEditConfirmDialog extends Dialog {

    private int id = -1;
    private int memberId = -1;
    private String mToast;


    TextView mContentTv;
    Button button;
    Button negaButton;
    CheckBox checkbox;
    Context mContext;
    EditText mContextEt;
    boolean isChecked = false;
    RelativeLayout mLayout;
    EditText dialogEdit;

    private boolean mOutTouchCancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        // TODO Auto-generated method stub
        mOutTouchCancel = cancel;
    }


    /**
     * 다이얼로그 외 영억 터치시 다이얼로그를 닫는다
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        int action = event.getAction();

        switch (action) {

            case MotionEvent.ACTION_UP:

                float touchX = event.getX();
                float touchY = event.getY();

                float layoutX = mLayout.getLeft();
                float layoutY = mLayout.getTop();

                float layoutWidth = mLayout.getWidth();
                float layoutHeight = mLayout.getHeight();

                if (!(touchX >= layoutX && touchX <= layoutX + layoutWidth
                        && touchY >= layoutY && touchY <= layoutY + layoutHeight)
                        ) {
                    if (mOutTouchCancel) {
                        try {
                            // popup type TYPE_A or TYPE_NEW_1 은 onclick 처리

                            GLog.v("close window");
                            dismiss();

                        } catch (Exception e) {
                            GLog.e(e.toString());
                            dismiss();
                        }
                    }
                }

                break;

        }

        return super.onTouchEvent(event);


    }

    public void setEditStr(String val){
        if (TextUtils.isEmpty(val) == false) {
            dialogEdit.setText(val);
            dialogEdit.setSelection(val.length());
        }
    }

    public EditText getInputEditText() {
        return dialogEdit;
    }

    public String getEditStr(){
        return dialogEdit.getText().toString().trim();
    }

    /**
     * 뒤로가기 단, 버튼이 1개인 다이얼로그는 button click으로 인지한다.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 커스텀 팝업 다이얼로그
     *
     * @param context context
     */
    public CustomEditConfirmDialog(Context context) {
        super(context, R.style.popup_custom_theme);
        mContext = context;
        //    setSelectView(type);

        setContentView(R.layout.popup_dialog_d_type);
        GLog.i("onCreate()", "dd");

        WindowManager.LayoutParams lpWindow = getWindow().getAttributes();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        mLayout = (RelativeLayout) getWindow().findViewById(R.id.dialog_layout);

        dialogEdit = (EditText) findViewById(R.id.dialog_edit);
        dialogEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String tmp = dialogEdit.getText().toString();
                if (dialogEdit.getText().toString().length() != 0 && dialogEdit.getText().toString().contains(".")) {
                    String st = tmp.substring(tmp.indexOf("."), tmp.length());
                    if (st.length() > 3) {
                        String hTmp = tmp.substring(0, tmp.indexOf("."));
                        String tTmp = st;
                        dialogEdit.setText(hTmp + tTmp.substring(0, 3));
                    }
                }
            }
        });

        button = (Button) findViewById(R.id.confirm_btn);
        negaButton = (Button) findViewById(R.id.cancel_btn);
        setCanceledOnTouchOutside(true);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setToastMsg(String msg) {
        this.mToast = msg;
    }

    public int getMemberId() {
        return this.memberId;
    }

    /**
     * 버틴 클릭 기본 설정
     */
    public View.OnClickListener clickListener = v -> {
        // TODO Auto-generated method stub
        dismiss();
    };


    /*
     * (non-Javadoc)
     * @see android.app.Dialog#setTitle(java.lang.CharSequence)
     */
    @Override
    public void setTitle(CharSequence title) {
        // TODO Auto-generated method stub
        TextView titleTextView = null;
        titleTextView = (TextView) findViewById(R.id.dialog_title);

        if (titleTextView != null) {
            titleTextView.setText(title);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.app.Dialog#setTitle(int)
     */
    @Override
    public void setTitle(int titleId) {
        // TODO Auto-generated method stu
        this.setTitle(titleId);
        String title = getContext().getResources().getString(titleId);
        setTitle(title);
    }

    /**
     * 팝업 내용
     *
     * @param content 내용
     */
    public void setContent(CharSequence content) {
        // TODO Auto-generated method stub
        mContentTv = null;
        mContentTv = (TextView) findViewById(R.id.dialog_content);

        if (mContentTv != null) {
            mContentTv.setText(content);
            setToastMsg(String.valueOf(content));
        }
    }


    /**
     * 확인 버튼 설정 ( 버튼이 2개일 경우 오른쪽 )
     *
     * @param listener button click event
     */
    public void setPositiveButton(View.OnClickListener listener) {
        button.setOnClickListener(listener);
    }

    /**
     * 취소 버튼 설정 ( 버튼이 2개일 경우 왼쪽 )
     *
     * @param listener button click event
     */
    public void setNegativeButton(View.OnClickListener listener) {
        negaButton.setOnClickListener(listener);
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


}
