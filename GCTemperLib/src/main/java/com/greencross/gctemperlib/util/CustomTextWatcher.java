package com.greencross.gctemperlib.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greencross.gctemperlib.R;

/**
 * Created by MobileDoctor on 2017-03-09.
 */

public class CustomTextWatcher implements TextWatcher {

    String mStrProv;
    Context mContext;
    EditText mEditText;
    TextView mTxtUnit;


    public CustomTextWatcher(Context context, EditText editText, TextView txtUnit){
        this.mContext = context;
        this.mEditText = editText;
        this.mTxtUnit = txtUnit;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mStrProv = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().lastIndexOf(".") > 0){
            if(s.toString().lastIndexOf(".") + 2 < s.toString().length()){
                mEditText.setText(mStrProv);
                mEditText.setSelection(start);
                Toast.makeText(mContext, mContext.getString(R.string.non_over_dot_2), Toast.LENGTH_SHORT).show();
            }
        }
        if(s.toString().length() > 0){
            if(mTxtUnit!= null)
                mTxtUnit.setVisibility(View.VISIBLE);

            //ssshin add 2018.10.30 처음 .(dot) 금지
            if(s.toString().startsWith(".")) {
                mEditText.setText("");
                mEditText.setSelection(start);
            }
        }else{
            if(mTxtUnit!= null)
                mTxtUnit.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
