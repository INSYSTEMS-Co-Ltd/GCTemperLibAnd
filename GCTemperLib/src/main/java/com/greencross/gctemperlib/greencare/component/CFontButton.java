package com.greencross.gctemperlib.greencare.component;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by mrsohn on2017. 3. 21..
 */
@SuppressLint("AppCompatCustomView")
public class CFontButton extends Button {

    public CFontButton(Context context) {
        super(context);
    }

    public CFontButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CFontButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
//        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.kelson_sans_regular);
//        setTypeface(tf);
//        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
//                attrs,
//                R.font.TextViewWithFont,
//                0, 0);
//
//        String typeface = typedArray.getString(R.styleable.TextViewWithFont_font);
//        if (typeface != null) {
//            try {
////                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), typeface);
//                setTypeface(typeface);
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