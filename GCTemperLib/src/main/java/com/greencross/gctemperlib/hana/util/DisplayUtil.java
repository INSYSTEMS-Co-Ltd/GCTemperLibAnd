package com.greencross.gctemperlib.hana.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * 디스플레이 유틸
 */
public class DisplayUtil {
    /**
     * @param context Context
     * @param dip     float
     * @return 폰트 크기
     */
    public static int ftDipToPx(Context context, float dip) {
        float val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return Math.round(val);
    }

    /**
     * @param context Context
     * @param sp      float
     * @return 폰트 크기
     */
    public static int ftSpToPx(Context context, float sp) {
        float val = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return Math.round(val);
    }

    /**
     * @param con Context
     * @return Density
     */
    public static float getDensity(Context con) {
        return con.getResources().getDisplayMetrics().density;
    }

    /**
     * @param con Context
     * @param px  int
     * @return px에 대한 dp값
     */
    public static int getPxToDp(Context con, float px) {
        return (int) (px / getDensity(con));
    }

    /**
     * @param con Context
     * @param dp  float
     * @return dp에 대한 px값
     */
    public static int getDpToPix(Context con, float dp) {
        return (int) (dp * getDensity(con) + 0.5);
    }

    /**
     * @param context Context
     * @return Screen Width
     */
    public static int getScreenWidth(Context context) {
        Display dis = ((WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point point = new Point();
            dis.getSize(point);

            return point.x;
        } else {
            return dis.getWidth();
        }
    }

    /**
     *
     * @param context
     * @param dp
     * @return
     *
     * float density = context.getResources().getDisplayMetrics().density;
     * float px = dp * density;
     * float dp = px / density;
     */
    public static float convertDpToPixel(Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }



    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */

    public static float convertPixelsToDp(Context context, float px){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }




    /**
     * @param context Context
     * @return Screen Height
     */
    public static int getScreenHeight(Context context) {
        Display dis = ((WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point point = new Point();
            dis.getSize(point);

            return point.y;
        } else {
            return dis.getHeight();
        }
    }

    public static int getSysStatebarSize(View view) {
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);

        int stateBarHeight = rect.top;

        return stateBarHeight;
    }
}
