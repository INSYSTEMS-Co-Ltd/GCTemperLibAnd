package com.greencross.gctemperlib.common;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by jihoon on 2016-03-30
 * 공유
 * @since 0, 1
 */
public class CommonView {
    private static CommonView	_instance;
    private Context mContext;

    public CommonView(){}

    public CommonView(Context context)
    {
        this.mContext	=	context;
    }

    public static CommonView getInstance()
    {
        if (_instance == null)
        {
            synchronized (CommonView.class)
            {
                if(_instance == null)
                {
                    _instance = new CommonView();
                }
            }
        }
        return _instance;
    }

    public static CommonView getInstance(Context context)
    {
        if (_instance == null)
        {
            synchronized (CommonView.class)
            {
                if(_instance == null)
                {
                    _instance	=	new CommonView(context);
                }
            }
        }
        return _instance;
    }


    /**
     * 이미지버튼 활성화 설정
     * @param imagebtn  이미지버튼
     * @param hasfocus  bool ( true - 활성화, false - 비활성화 )
     */
    public void setClearImageBt(ImageButton imagebtn, boolean hasfocus){
        if (hasfocus)
        {
            imagebtn.setVisibility(View.VISIBLE);
        }
        else
        {
            imagebtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 입력창 초기화
     * @param editText  입력창
     */
    public void setClearEditText(EditText editText)
    {
        if (editText.isFocused()) editText.setText("");
    }

    /**
     * EditText 커서 위치 마지막으로 옮기기
     * @param editText 입력창
     * @param length    입력된 문자 개수
     */
    public void setSelectionCursor(EditText editText, int length){
        if(length > 0){
            editText.setSelection(length);
        }

    }

}
