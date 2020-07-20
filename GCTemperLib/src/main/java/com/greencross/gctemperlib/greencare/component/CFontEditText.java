package com.greencross.gctemperlib.greencare.component;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mrsohn on 2017. 3. 21..
 */
@SuppressLint("AppCompatCustomView")
public class CFontEditText extends EditText {

    public CFontEditText(Context context) {
        super(context);
    }

    public CFontEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CFontEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

//        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
//                attrs,
//                R.styleable.TextViewWithFont,
//                0, 0);
//
//        String typeface = typedArray.getString(R.styleable.TextViewWithFont_font);

//        Typeface typeface = getContext().getResources().getFont(R.font.kelson_sans_light);
//        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.kelson_sans_regular);
//        setTypeface(tf);
//        if (tf != null) {
//            try {
////                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), typeface);
//                setTypeface(tf);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.kelson_sans_regular));
//            setTypeface(tf);
//        }
    }
}