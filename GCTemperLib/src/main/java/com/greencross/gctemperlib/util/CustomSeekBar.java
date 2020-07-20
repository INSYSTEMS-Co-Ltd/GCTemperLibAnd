package com.greencross.gctemperlib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.collection.ProgressItem;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.greencare.database.DBHelper;
import com.greencross.gctemperlib.greencare.database.DBHelperLog;
import com.greencross.gctemperlib.greencare.weight.WeightCurrView;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class CustomSeekBar extends SeekBar {

    private ArrayList<ProgressItem> mProgressItemsList;
    private float goalBmi;
    private int goalWt;
    private float curBmi;
    private int curInt;
    private int type = 0;
    private Bitmap bitmap2;
    private int xPosition = 0;
    private LinearLayout popup;

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initData(ArrayList<ProgressItem> progressItemsList, float curBmi, float goalBmi, LinearLayout popup) {
        this.goalBmi = goalBmi;
        this.mProgressItemsList = progressItemsList;
        this.curBmi = curBmi;
        this.type = 0;
        this.popup = popup;
    }

    public void initData(ArrayList<ProgressItem> progressItemsList, int curInt, int goalWt, boolean flag, LinearLayout popup) {
        this.mProgressItemsList = progressItemsList;
        this.goalWt = goalWt;
        this.curInt = curInt;
        this.type = 1;
        this.popup = popup;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        if(mProgressItemsList == null)
            return;

        if (mProgressItemsList.size() > 0) {

            if(type == 0) {

                int progressBarWidth = getWidth();
                int progressBarHeight = getHeight();
                int thumboffset = getThumbOffset();
                int lastProgressX = 0;
                int progressItemWidth, progressItemRight;

                int prevWidth = 0;

                int titleAreaWidth = progressBarWidth / (mProgressItemsList.size() - 1);

                int titleAreaWidthCnt = 0;
                for (int i = 0; i < mProgressItemsList.size(); i++) {
                    ProgressItem progressItem = mProgressItemsList.get(i);

                    Paint progressPaint = new Paint();
                    progressPaint.setColor(getResources().getColor(
                            progressItem.color));

                    Paint textPaint = new Paint();
                    //   textPaint.setARGB(255, 255, 255, 255);
                    textPaint.setTextAlign(Paint.Align.CENTER);
                    textPaint.setTextSize(dpToPx(12));
                    textPaint.setColor(getResources().getColor(R.color.color_646464));
                    textPaint.setTypeface(Typeface.DEFAULT_BOLD);

                    progressItemWidth = (int) (progressItem.progressItemPercentage
                            * progressBarWidth / 100);

                    progressItemRight = lastProgressX + progressItemWidth;

                    // for last item give right to progress item to the width
                    if (i == mProgressItemsList.size() - 1
                            && progressItemRight != progressBarWidth) {
                        progressItemRight = progressBarWidth;
                    }
//                    Rect progressRect = new Rect();
//
//                    progressRect.set(lastProgressX, thumboffset / 2,
//                            progressItemRight, progressBarHeight - thumboffset / 2);
//                    canvas.drawRect(progressRect, progressPaint);
                    if(i == 0) {
                        RectF progressRect = new RectF();

                        progressRect.set(lastProgressX, thumboffset - progressBarHeight /2,
                                progressItemRight, progressBarHeight - (thumboffset - progressBarHeight /2) - 10);

                        int cornersRadius = progressBarHeight /2;

                        canvas.drawRoundRect(progressRect, cornersRadius, cornersRadius, progressPaint);
                        canvas.drawRect(progressItemRight/2, thumboffset - progressBarHeight /2, progressItemRight, progressBarHeight - (thumboffset - progressBarHeight /2) - 10, progressPaint);
                    } else if(i == mProgressItemsList.size()-1){
                        RectF progressRect = new RectF();

                        progressRect.set(lastProgressX, thumboffset - progressBarHeight /2,
                                progressItemRight, progressBarHeight - (thumboffset - progressBarHeight /2) - 10);

                        int cornersRadius = progressBarHeight /2;

                        canvas.drawRoundRect(progressRect, cornersRadius, cornersRadius, progressPaint);
                        canvas.drawRect(lastProgressX, thumboffset - progressBarHeight /2, lastProgressX + cornersRadius /2, progressBarHeight - (thumboffset - progressBarHeight /2) - 10, progressPaint);

                    } else {
                        Rect progressRect = new Rect();

                        progressRect.set(lastProgressX, thumboffset - progressBarHeight /2,
                                progressItemRight, progressBarHeight - (thumboffset - progressBarHeight /2) - 10);
                        canvas.drawRect(progressRect, progressPaint);
                    }


                    lastProgressX = progressItemRight;

                    if (lastProgressX < 0)
                        lastProgressX = dpToPx(1);

                    if (lastProgressX >= progressBarWidth)
                        lastProgressX = progressBarWidth - dpToPx(1);
//                    canvas.drawText(progressItem.pointStr, lastProgressX, dpToPx(22), textPaint);

//                    Paint bottomPaint = new Paint();
//                    bottomPaint.setTextAlign(Paint.Align.CENTER);
//                    bottomPaint.setTextSize(dpToPx(12));
//                    bottomPaint.setColor(getResources().getColor(R.color.color_646464));
//                    bottomPaint.setTypeface(Typeface.DEFAULT_BOLD);
//
//                    if (lastProgressX != progressBarWidth)
//                        canvas.drawText(progressItem.bottomStr, lastProgressX, dpToPx(70), bottomPaint);

                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(getResources().getColor(progressItem.color));

//                    if (i != 1) {
//                        int titleAreaX = titleAreaWidth * titleAreaWidthCnt;
//                        canvas.drawRect(titleAreaX, dpToPx(77), titleAreaX + dpToPx(13), dpToPx(90), paint);
//                        Paint titlePaint = new Paint();
//                        titlePaint.setTextAlign(Paint.Align.LEFT);
//                        titlePaint.setTextSize(dpToPx(11));
//                        titlePaint.setColor(getResources().getColor(R.color.color_646464));
//                        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
//                        canvas.drawText(progressItem.descStr, titleAreaX + dpToPx(20), dpToPx(88), titlePaint);
//                        titleAreaWidthCnt++;
//                    } else {
                        Paint descPaint = new Paint();
                        //   textPaint.setARGB(255, 255, 255, 255);
                        descPaint.setTextAlign(Paint.Align.CENTER);
                        descPaint.setTextSize(dpToPx(11));
                        descPaint.setColor(getResources().getColor(R.color.colorWhite));
                        descPaint.setTypeface(Typeface.DEFAULT_BOLD);
                        int x = ((lastProgressX - prevWidth) / 2) + prevWidth;
                        if (lastProgressX != progressBarWidth)
                            canvas.drawText(progressItem.descStr, x, dpToPx(47), descPaint);
//                    }
                    prevWidth = lastProgressX;
                }

//                Paint descPaint = new Paint();
//                descPaint.setTextAlign(Paint.Align.CENTER);
//                descPaint.setTextSize(dpToPx(11));
//                descPaint.setColor(getResources().getColor(R.color.mdtp_white));
//                descPaint.setTypeface(Typeface.DEFAULT_BOLD);

//            /* kg */
//                Paint kgPaint = new Paint();
//                kgPaint.setTextAlign(Paint.Align.LEFT);
//                kgPaint.setTextSize(dpToPx(12));
//                kgPaint.setColor(getResources().getColor(R.color.color_646464));
//                kgPaint.setTypeface(Typeface.DEFAULT_BOLD);
//                canvas.drawText("kg", 0, dpToPx(70), kgPaint);
//
//
//             /* bmi */
//                Paint bmiPaint = new Paint();
//                bmiPaint.setTextAlign(Paint.Align.LEFT);
//                bmiPaint.setTextSize(35);
//                bmiPaint.setColor(getResources().getColor(R.color.color_646464));
//                bmiPaint.setTypeface(Typeface.DEFAULT_BOLD);
//                canvas.drawText("BMI", 0, dpToPx(22), bmiPaint);


//                Bitmap bitmap1 = BitmapFactory.decodeResource(
//                        getResources(),
//                        R.drawable.goal_thumb
//                );

                Bitmap bitmap1 = writeOnDrawable(R.drawable.goal_thumb,"목표체중",dpToPx(12),20);
                Log.d("tag2","bitmap1.getWidth()/2 : " + bitmap1.getWidth()/2);

                Log.d("tag2", "goalBmi : "+goalBmi);
                if(goalBmi < 0) {
                    goalBmi = dpToPx(1);
                    Log.d("tag2", "goalBmi_dpToPx(4) : "+goalBmi);
                }

                if((goalBmi * progressBarWidth / 100) >= progressBarWidth){
                    canvas.drawBitmap(
                            bitmap1,
                            progressBarWidth - bitmap1.getWidth(),
                            dpToPx(55),
                            null
                    );

                }else{

                    if((int) (goalBmi
                            * progressBarWidth / 100) - bitmap1.getWidth()/2 < -1){
                        canvas.drawBitmap(
                                bitmap1,
                                -1,
                                dpToPx(55),
                                null
                        );
                    }else if((int) (goalBmi
                            * progressBarWidth / 100) - bitmap1.getWidth()/2 > progressBarWidth - bitmap1.getWidth()) {
                        canvas.drawBitmap(
                                bitmap1,
                                progressBarWidth - bitmap1.getWidth(),
                                dpToPx(55),
                                null
                        );

                    } else {
                        canvas.drawBitmap(
                                bitmap1,
                                (int) (goalBmi
                                        * progressBarWidth / 100) - bitmap1.getWidth()/2,
                                dpToPx(55),
                                null
                        );
                    }


                }


//                Bitmap bitmap2 = BitmapFactory.decodeResource(
//                        getResources(),
//                        R.drawable.cur_thumb
//                );

                Bitmap bitmap2 = writeOnDrawable(R.drawable.cur_thumb1,"현재체중",dpToPx(12),0);

                Log.d("tag2", "curBMI :"+curBmi);

                if(curBmi < 0) {
                    curBmi = dpToPx(1);
                    Log.d("tag2", "curBMI_dpToPx(4) : "+curBmi);
                }

                if((curBmi * progressBarWidth / 100) >= progressBarWidth){
//                    xPosition = progressBarWidth - bitmap2.getWidth();
                    Log.d("tag2", String.format("curBMI2 : %d",progressBarWidth - bitmap1.getWidth()/2));
                    canvas.drawBitmap(
                            bitmap2,
                            progressBarWidth - bitmap2.getWidth(),
                            dpToPx(0),
                            null
                    );


                }else{
                    Log.d("tag2", String.format("curBMI3 : %d",(int) (curBmi
                            * progressBarWidth / 100) - bitmap1.getWidth()/2));

//                    xPosition = -1;
                    if((int) (curBmi
                            * progressBarWidth / 100) - bitmap2.getWidth()/2 < -1){
                        canvas.drawBitmap(
                                bitmap2,
                                -1,
                                dpToPx(0),
                                null
                        );
                    } else if((int) (curBmi
                            * progressBarWidth / 100) - bitmap2.getWidth()/2 > progressBarWidth - bitmap2.getWidth()) {
//                        xPosition = progressBarWidth - bitmap2.getWidth();
                        canvas.drawBitmap(
                                bitmap2,
                                progressBarWidth - bitmap2.getWidth(),
                                dpToPx(0),
                                null
                        );

                    } else {
//                        xPosition = (int) (curBmi
//                                * progressBarWidth / 100) - bitmap2.getWidth()/2;
                        canvas.drawBitmap(
                                bitmap2,
                                (int) (curBmi
                                        * progressBarWidth / 100) - bitmap2.getWidth()/2,
                                dpToPx(0),
                                null
                        );
                    }

                }





                super.onDraw(canvas);


            }else {

                int progressBarWidth = getWidth();
                int progressBarHeight = getHeight();
                int thumboffset = getThumbOffset();
                int lastProgressX = 0;
                int progressItemWidth, progressItemRight;

                int prevWidth = 0;

                int titleAreaWidth = progressBarWidth / (mProgressItemsList.size() - 1);

                int titleAreaWidthCnt = 0;
                for (int i = 0; i < mProgressItemsList.size(); i++) {
                    ProgressItem progressItem = mProgressItemsList.get(i);

                    Paint progressPaint = new Paint();
                    progressPaint.setColor(getResources().getColor(
                            progressItem.color));

                    Paint textPaint = new Paint();
                    //   textPaint.setARGB(255, 255, 255, 255);
                    textPaint.setTextAlign(Paint.Align.CENTER);
                    textPaint.setTextSize(dpToPx(12));
                    textPaint.setColor(getResources().getColor(R.color.color_646464));
                    textPaint.setTypeface(Typeface.DEFAULT_BOLD);

                    progressItemWidth = (int) (progressItem.progressItemPercentage
                            * progressBarWidth / 100);

                    progressItemRight = lastProgressX + progressItemWidth;

                    // for last item give right to progress item to the width
                    if (i == mProgressItemsList.size() - 1
                            && progressItemRight != progressBarWidth) {
                        progressItemRight = progressBarWidth;
                    }

                    if(i == 0) {
                        RectF progressRect = new RectF();

                        progressRect.set(lastProgressX, thumboffset - progressBarHeight /2,
                                progressItemRight, progressBarHeight - (thumboffset - progressBarHeight /2) - 10);

                        int cornersRadius = progressBarHeight /2;

                        canvas.drawRoundRect(progressRect, cornersRadius, cornersRadius, progressPaint);
                        canvas.drawRect(progressItemRight/2, thumboffset - progressBarHeight /2, progressItemRight, progressBarHeight - (thumboffset - progressBarHeight /2) - 10, progressPaint);
                    } else if(i == mProgressItemsList.size()-1){
                        RectF progressRect = new RectF();

                        progressRect.set(lastProgressX, thumboffset - progressBarHeight /2,
                                progressItemRight, progressBarHeight - (thumboffset - progressBarHeight /2) - 10);

                        int cornersRadius = progressBarHeight /2;

                        canvas.drawRoundRect(progressRect, cornersRadius, cornersRadius, progressPaint);
                        canvas.drawRect(lastProgressX, thumboffset - progressBarHeight /2, lastProgressX + cornersRadius /2, progressBarHeight - (thumboffset - progressBarHeight /2) - 10, progressPaint);

                    } else {
                        Rect progressRect = new Rect();

                        progressRect.set(lastProgressX, thumboffset - progressBarHeight /2,
                                progressItemRight, progressBarHeight - (thumboffset - progressBarHeight /2) - 10);
                        canvas.drawRect(progressRect, progressPaint);
                    }
                    lastProgressX = progressItemRight;

                    if (lastProgressX < 0)
                        lastProgressX = dpToPx(1);
                    if (lastProgressX >= progressBarWidth)
                        lastProgressX = progressBarWidth - dpToPx(1);
//                    canvas.drawText(progressItem.pointStr, lastProgressX, dpToPx(22), textPaint);

//                    Paint bottomPaint = new Paint();
//                    //   textPaint.setARGB(255, 255, 255, 255);
//                    bottomPaint.setTextAlign(Paint.Align.CENTER);
//                    bottomPaint.setTextSize(dpToPx(12));
//                    bottomPaint.setColor(getResources().getColor(R.color.color_646464));
//                    bottomPaint.setTypeface(Typeface.DEFAULT_BOLD);
//
//                    if (lastProgressX != progressBarWidth)
//                        canvas.drawText(progressItem.bottomStr, lastProgressX, dpToPx(70), bottomPaint);

                    Paint paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(getResources().getColor(progressItem.color));

                    Paint descPaint = new Paint();

                    descPaint.setTextAlign(Paint.Align.CENTER);
                    descPaint.setTextSize(dpToPx(11));
                    descPaint.setColor(getResources().getColor(R.color.colorWhite));
                    descPaint.setTypeface(Typeface.DEFAULT_BOLD);
                    int x = ((lastProgressX - prevWidth) / 2) + prevWidth;
                    canvas.drawText(progressItem.descStr, x, dpToPx(47), descPaint);

                    prevWidth = lastProgressX;
                }

//                Paint descPaint = new Paint();
//                descPaint.setTextAlign(Paint.Align.CENTER);
//                descPaint.setTextSize(dpToPx(11));
//                descPaint.setColor(getResources().getColor(R.color.mdtp_white));
//                descPaint.setTypeface(Typeface.DEFAULT_BOLD);

            /* kg */
//                Paint kgPaint = new Paint();
//                kgPaint.setTextAlign(Paint.Align.LEFT);
//                kgPaint.setTextSize(dpToPx(12));
//                kgPaint.setColor(getResources().getColor(R.color.color_646464));
//                kgPaint.setTypeface(Typeface.DEFAULT_BOLD);
//                canvas.drawText("kg", 0, dpToPx(70), kgPaint);


//                Bitmap bitmap2 = BitmapFactory.decodeResource(
//                        getResources(),
//                        R.drawable.cur_thumb
//                );

                bitmap2 = writeOnDrawable(R.drawable.cur_thumb,"현재체중",dpToPx(12),0);
                if(curInt < 0)
                    curInt = dpToPx(1);

                if((curInt * progressBarWidth / 100) >= progressBarWidth){
                    xPosition = progressBarWidth - bitmap2.getWidth();
                    canvas.drawBitmap(
                            bitmap2,
                            progressBarWidth - bitmap2.getWidth(),
                            dpToPx(0),
                            null
                    );
                }  else{

                    if((int) (curInt
                            * progressBarWidth / 100) - bitmap2.getWidth()/2 < -1){
                        xPosition = -1;
                        canvas.drawBitmap(
                                bitmap2,
                                -1,
                                dpToPx(0),
                                null
                        );

                    } else if((int) (curInt
                            * progressBarWidth / 100) - bitmap2.getWidth()/2 > progressBarWidth - bitmap2.getWidth()) {
                        xPosition = progressBarWidth - bitmap2.getWidth();
                        canvas.drawBitmap(
                                bitmap2,
                                progressBarWidth - bitmap2.getWidth(),
                                dpToPx(0),
                                null
                        );

                    } else {
                        xPosition = (int) (curInt
                                * progressBarWidth / 100) - bitmap2.getWidth()/2;
                        canvas.drawBitmap(
                                bitmap2,
                                (int) (curInt
                                        * progressBarWidth / 100) - bitmap2.getWidth()/2,
                                dpToPx(0),
                                null
                        );
                    }
                }



                super.onDraw(canvas);

            }

        }

    }

    public int dpToPx(float dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
        return px;
    }

    public Bitmap writeOnDrawable(int drawableId, String text, int TextSize, int y){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
//        BitmapFactory.decodeResource(getResources(),drawableId);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(getResources().getColor(R.color.colorWhite));
        paint.setTextSize(TextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        Canvas canvas = new Canvas(bm);
        Log.d("tag1","bm.getWidth()/2 : " + bm.getWidth()/2);
        Log.d("tag1","bm.getHeight()/2 : " + bm.getHeight());
        if(drawableId == R.drawable.cur_thumb1)
            canvas.drawText(text,bm.getWidth()/2 , bm.getHeight()/2 + y, paint);
        else if(drawableId == R.drawable.goal_thumb)
            canvas.drawText(text,bm.getWidth()/2 , bm.getHeight() - y, paint);
        else
            canvas.drawText(text,bm.getWidth()/3+10 , bm.getHeight()/2 + y, paint);

        return bm;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Check if the x and y position of the touch is inside the bitmap
                if (bitmap2 != null) {
                    if (x > xPosition && x < xPosition + bitmap2.getWidth() && y > dpToPx(0) && y < dpToPx(0) + bitmap2.getHeight()) {

                        if (getContext().getString(R.string.pregnant_weight_noti_btn) != null && !getContext().getString(R.string.pregnant_weight_noti_btn).equals("")) {
                            // _!: 카운트만 되는 버튼 끝에 붙임
                            if (getContext().getString(R.string.pregnant_weight_noti_btn).contains("_!")) {
                                String temp = getContext().getString(R.string.pregnant_weight_noti_btn).replace("_!", "");
                                String cod[] = temp.split("_");

                                DBHelper helper = new DBHelper(getContext());
                                DBHelperLog logdb = helper.getLogDb();

                                if (cod.length == 1) {
                                    logdb.insert(cod[0], "", "", 0, 1);
                                    Log.i("CustomSeekBar", "view.contentDescription : " + cod[0] + "count : 1");
                                } else if (cod.length == 2) {
                                    logdb.insert(cod[0], cod[1], "", 0, 1);
                                    Log.i("CustomSeekBar", "view.contentDescription : " + cod[0] + cod[1] + "count : 1");
                                } else {
                                    logdb.insert(cod[0], cod[1], cod[2], 0, 1);
                                    Log.i("CustomSeekBar", "view.contentDescription : " + cod[0] + cod[1] + cod[2] + "count : 1");
                                }

                            }
                            Log.i("CustomSeekBar", "ACTION_UP");
                        }

                        //Bitmap touched
                        if (CommonData.getInstance(getContext()).getbirth_chl_yn().equals("N"))
                            WeightCurrView.bmiInfoPopup(getContext());
//                    else
//                        popup.setVisibility(VISIBLE);
                    }
                    return true;
                }
        }
        return false;
    }
}
