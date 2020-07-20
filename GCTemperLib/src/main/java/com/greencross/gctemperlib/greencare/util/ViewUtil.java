package com.greencross.gctemperlib.greencare.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.greencross.gctemperlib.greencare.base.value.Define;
import com.greencross.gctemperlib.R;

import java.io.File;

/**
 * Created by mrsohn on 2017. 3. 21..
 */

public class ViewUtil {

    private static final String TAG = ViewUtil.class.getSimpleName();


    /**
     * 이미지 URL에서 이미지를 bitmap로 가져온 후 ImageView 에 세팅
     *
     * @param idx
     * @param iv
     */
    public static void getIndexToImageData(final String idx, final ImageView iv, final ImageView dv, final int imgid, final TextView kaltv, final Context context) {
        if (TextUtils.isEmpty(idx)) {
            Logger.e(TAG, "getIndexToImageData idx is null");
            return;
        }

        // 로컬에서 받아오기
        Bitmap bitmap;
        try {
            String path = Define.getFoodPhotoPath(idx);
            Logger.i(TAG, "getIndexToImageData.path="+path);
            File imgFile = new File(path);
            if (imgFile.exists()) {
                kaltv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv.setImageBitmap(bitmap);
                dv.setVisibility(View.GONE);
                kaltv.setTypeface(null,Typeface.NORMAL);
            }
            else{
                if(kaltv.getText().toString().equals("0 kcal")){
                    kaltv.setTypeface(null,Typeface.NORMAL);
                }else{
                    kaltv.setTypeface(null,Typeface.BOLD);
                }
                kaltv.setTextColor(ContextCompat.getColor(context, R.color.color_FB8AD3));
                dv.setVisibility(View.VISIBLE);
                dv.setImageResource(imgid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 이미지 URL에서 이미지를 bitmap로 가져온 후 ImageView 에 세팅
     *
     * @param idx
     * @param iv
     */
    public static void getIndexToImageData(final String idx, final ImageView iv) {
        if (TextUtils.isEmpty(idx)) {
            Logger.e(TAG, "getIndexToImageData idx is null");
            return;
        }

        // 로컬에서 받아오기
        Bitmap bitmap;
        try {
            String path = Define.getFoodPhotoPath(idx);
            Logger.i(TAG, "getIndexToImageData.path="+path);
            File imgFile = new File(path);
            if (imgFile.exists()) {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCFontTypeface(Context context, AttributeSet attrs, View view) {
//        Typeface tf = ResourcesCompat.getFont(context, R.font.kelson_sans_regular);
//        setTypeface(tf, view);
    }

    public static void setTypeface(Typeface typeface, View view) {
        if (view instanceof TextView) {
            if (((TextView) view).getTypeface() == null)
                ((TextView) view).setTypeface(typeface);
        } else if (view instanceof EditText) {
            if (((EditText)view).getTypeface() == null)
                ((EditText)view).setTypeface(typeface);
        } else if (view instanceof Button) {
            if (((Button) view).getTypeface() == null)
                ((Button) view).setTypeface(typeface);
        } else if (view instanceof RadioButton) {
            if (((RadioButton) view).getTypeface() == null)
                ((RadioButton) view).setTypeface(typeface);
        } else if (view instanceof CheckBox) {
            if (((TextView) view).getTypeface() == null)
                ((TextView) view).setTypeface(typeface);
        } else if (view instanceof CheckedTextView) {
            if (((CheckedTextView) view).getTypeface() == null)
                ((CheckedTextView) view).setTypeface(typeface);
        } else if (view instanceof ToggleButton) {
            if (((ToggleButton) view).getTypeface() == null)
                ((ToggleButton) view).setTypeface(typeface);
        }
    }

    /**
     * kelson_sans_regular.otf
     *
     * @param context
     * @param view
     */
    public static void setTypefacekelson_sans_regular(Context context, View view) {
//        Typeface typeface = ResourcesCompat.getFont(context, R.font.kelson_sans_regular);
//        setTypeface(typeface, view);
    }

    /**
     * notosanskr_bold.otf
     *
     * @param context
     * @param view
     */
    public static void setTypefacenotosanskr_bold(Context context, View view) {
//        Typeface typeface = ResourcesCompat.getFont(context, R.font.kelson_sans_regular);
//        setTypeface(typeface, view);
    }

    /**
     * notosanskr_medium.otf
     *
     * @param context
     * @param view
     */
    public static void setTypefacenotosanskr_medium(Context context, View view) {
//        Typeface typeface = ResourcesCompat.getFont(context, R.font.notosanskr_medium);
//        setTypeface(typeface, view);
    }

    /**
     * notosanskr_regular.otf
     *
     * @param context
     * @param view
     */
    public static void setTypefacenotosanskr_regular(Context context, View view) {
//        Typeface typeface = ResourcesCompat.getFont(context, R.font.notosanskr_regular);
//        setTypeface(typeface, view);
    }
}
