package com.greencross.gctemperlib.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.greencross.gctemperlib.R;


public class CommonToolBar extends LinearLayout {
    private final String TAG = getClass().getSimpleName();
    public static String TOOL_BAR_TITLE = "action_bar_title";

    private LinearLayout mBackBtn;
    private ImageView mBackIcon;
    private TextView mTitleTv;
    private TextView mRightTv;
    private ImageView mRightIcon;


    public CommonToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.common_tool_bar, null);
        mBackBtn = view.findViewById(R.id.common_toolbar_back_btn);
        mBackIcon = view.findViewById(R.id.common_toolbar_back_icon);
        mTitleTv = view.findViewById(R.id.common_toolbar_title);
        mRightTv = view.findViewById(R.id.common_toolbar_right_btn);
        mRightIcon = view.findViewById(R.id.common_toolbar_right_icon);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).onBackPressed();
            }
        });

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.common_toolbar);
        String titleText = typedArray.getString(R.styleable.common_toolbar_title);
        String rightText = typedArray.getString(R.styleable.common_toolbar_right_text);
        Drawable leftIcon = typedArray.getDrawable(R.styleable.common_toolbar_left_icon);

        setTitle(titleText);
        setRightTv(rightText);
        setLeftIcon(leftIcon);

        typedArray.recycle();
    }

    public void setTitle(String title) {
        if (mTitleTv != null)
            mTitleTv.setText(title);
        else
            Log.e(TAG, "툴바 설정 안됨 레이아웃 확인 필요");
    }

    public void setTitle(int title) {
        if (mTitleTv != null)
            mTitleTv.setText(getContext().getString(title));
        else
            Log.e(TAG, "툴바 설정 안됨 레이아웃 확인 필요");
    }

    /**
     * 왼쪽 BackButton 설정
     */
    public void setLeftIcon(Drawable icon) {
        if (icon != null)
            mBackIcon.setImageDrawable(icon);
//        else
//            mBackIcon.setVisibility(View.GONE);
    }

    public ImageView getLeftICon() {
        return mBackIcon;
    }

    public void setLeftIcon(Drawable icon, OnClickListener  clickListener) {
        if (icon != null)
            setLeftIcon(icon);

        setLeftBtnOnclickListener(clickListener);
    }

    public void setLeftIcon(int icon) {
        setLeftIcon(icon, null);
    }


    public void setLeftIcon(int icon, OnClickListener  clickListener) {
        mBackIcon.setImageResource(icon);
        setLeftBtnOnclickListener(clickListener);
    }

    public void setLeftBtnOnclickListener(OnClickListener  clickListener) {
        mBackBtn.setOnClickListener(clickListener);
        mBackIcon.setOnClickListener(clickListener);
    }



    /**
     * 오른쪽 문구 및 클릭리스너 설정
     * @param clickListener
     */
    public void setRightTvOnclickListener(OnClickListener  clickListener) {
        mRightTv.setOnClickListener(clickListener);
    }

    public void setRightTv(String title) {
        mRightTv.setText(title);
    }

    public void setRightTv(int title) {
        mRightTv.setText(getContext().getString(title));
    }

    public View getBackBtn(){

        return mBackBtn;
    }

    public void setRightBtn(int icon, OnClickListener  clickListener) {
        mRightTv.setBackgroundResource(icon);
        mRightTv.setOnClickListener(clickListener);
    }

    public void setRightIcon(int icon, OnClickListener  clickListener) {
        mRightIcon.setVisibility(View.VISIBLE);
        mRightIcon.setImageResource(icon);
        mRightTv.setOnClickListener(clickListener);
        mRightIcon.setOnClickListener(clickListener);
    }



}
