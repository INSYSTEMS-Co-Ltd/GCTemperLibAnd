package com.greencross.gctemperlib.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.greencross.gctemperlib.R;


/**
 * Created by jihoon on 2016-03-21.
 * 프로그래스바 클래스
 * @since 0, 1
 */
public class MakeProgress extends ProgressDialog {

    public MakeProgress(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setCanceledOnTouchOutside(false);
        super.setContentView(R.layout.dialog_progress_layout_transparent);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}