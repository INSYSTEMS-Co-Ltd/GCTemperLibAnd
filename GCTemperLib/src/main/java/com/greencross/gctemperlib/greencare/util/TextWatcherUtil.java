package com.greencross.gctemperlib.greencare.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by cldlt on 2018-04-10.
 */

public class TextWatcherUtil {
    private final String TAG = TextWatcherUtil.class.getSimpleName();

    public void setFormatWatcher(final EditText tv) {
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                String str = s.toString();
//                if (count != after) {
//                    Logger.i(TAG, "onTextChanged.count="+count+", after="+after+", start="+start+", str="+str);
//                    String formatStr = StringUtil.getFormatPrice(str);
//                    tv.setText(formatStr);
//                    tv.setSelection(formatStr.length());
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (before != count) {
                    Logger.i(TAG, "onTextChanged.before="+before+", count="+count+", start="+start+", str="+str);
                    String formatStr = StringUtil.getFormatPrice(str);
                    tv.setText(formatStr);
                    tv.setSelection(formatStr.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private String beforeText = "";
    public void setTextWatcher(final EditText editText, final float maxVal, final int dotAfterCnt) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str = s.toString();
                Log.i(TAG, "onTextChanged.str="+str);
                if (beforeText.equals(str)) {
                    if (str.equals("") == false)
                        editText.setSelection(str.length());
                    return;
                }

                if (str.length() != 0) {
                    float val = StringUtil.getFloat(s.toString());

                    if (val == 0 || val > maxVal) {
                        str = str.substring(0, str.length()-1);
                        beforeText = str;
                        editText.setText(str);
                    }

                    String[] dotAfter = str.split("\\.");
                    if (dotAfter.length >= 2 && (dotAfter[1].length() > dotAfterCnt)) {
                        str = str.substring(0, str.length()-1);
                        beforeText = str;
                        editText.setText(str);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editText.addTextChangedListener(textWatcher);
    }
}
