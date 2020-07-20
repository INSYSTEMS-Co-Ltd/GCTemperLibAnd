package com.greencross.gctemperlib.common;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;

/**
 * Created by jihoon on 2016-03-21.
 * 커스텀 팝업 다이얼로그 인터페이스
 * @since 0, 1
 */
public class CustomAlertDialogInterface {

    public interface OnClickListener {
        public abstract void onClick(CustomAlertDialog dialog, Button button);
    }

    public interface OnImgClickListener {
        public abstract void onClick(CustomAlertDialog dialog, View view);
    }

    public interface OnCheckedChangeListener {

        public abstract void onCheckedChanged(CustomAlertDialog dialog, CheckBox checkbox, boolean ischeck);
    }

    public interface OnRatingBarChangeListener {

        public abstract void onRatingChanged(CustomAlertDialog dialog, RatingBar ratingbar, float size, boolean flag);
    }

}


