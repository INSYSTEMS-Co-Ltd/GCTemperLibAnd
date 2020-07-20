package com.greencross.gctemperlib.greencare.chartview.weight;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.StringUtil;

/**
 * 임신 40주간 적정 체중 구하기
 */
public class Mother40WeekCalc {
    private final String TAG = getClass().getSimpleName();

    // 41주 차로 마지막 계산해 놓음 (마지막에 짤리는 모습 해결을 위해)
    // 저체중군 BMI<18.5
    private final float BMI_TYPE_1_LOW[] =  {0.13f * 14 ,0.53f * 12 ,0.32f * (14+1)};
    private final float BMI_TYPE_1_HIGH[] = {0.19f * 14 ,0.75f * 12 ,0.45f * (14+1)};
    // 정상체중군 18.5 <= 22.9f
    private final float BMI_TYPE_2_LOW[] =  {0.13f * 14 ,0.39f * 12 ,0.34f * (14+1)};
    private final float BMI_TYPE_2_HIGH[] = {0.19f * 14 ,0.53f * 12 ,0.49f * (14+1)};
    // 과체중군 23 <= 24.9ff
    private final float BMI_TYPE_3_LOW[] =  {0.13f * 14 ,0.39f * 12 ,0.34f * (14+1)};
    private final float BMI_TYPE_3_HIGH[] = {0.19f * 14 ,0.53f * 12 ,0.49f * (14+1)};
    // 비만군 25 <= 29.9f
    private final float BMI_TYPE_4_LOW[] =  {0.06f * 14 ,0.29f * 12 ,0.18f * (14+1)};
    private final float BMI_TYPE_4_HIGH[] = {0.13f * 14 ,0.49f * 12 ,0.26f * (14+1)};
    // 고도비만군 30 <f
    private final float BMI_TYPE_5_LOW[] =  {0.06f * 14 ,0.15f * 12 ,0.16f * (14+1)};
    private final float BMI_TYPE_5_HIGH[] = {0.13f * 14 ,0.30f * 12 ,0.26f * (14+1)};
    // 다태임신 쌍둥이f
    private final float BMI_TYPE_6_LOW[] =  {0.17f * 14 ,0.68f * 12 ,0.39f * (14+1)};
    private final float BMI_TYPE_6_HIGH[] = {0.23f * 14 ,0.83f * 12 ,0.49f * (14+1)};



    public float[] doBmiCalc(Context context) {
        CommonData login = CommonData.getInstance(context);
        if ("2".equals(login.getMberChlType())) {
            // 다태 임신
            // 고도비만
            Logger.i(TAG, "getWeight 다태임신");
            return cal40Weight(context, BMI_TYPE_6_LOW, BMI_TYPE_6_HIGH);
        }

        CommonData commonData = CommonData.getInstance(context);
        float bmi = StringUtil.getFloat(commonData.getBmi());// (fWeight / (rHeight * rHeight)); // 회원 BMI
        if (bmi < 18.5) {
            // 저체중
            Logger.i(TAG, "getWeight bmi=" + bmi);
            return cal40Weight(context, BMI_TYPE_1_LOW, BMI_TYPE_1_HIGH);
        } else if (bmi >= 18.5 && bmi < 23) {
            // 정상체중
            Logger.i(TAG, "getWeight bmi2=" + bmi);
            return cal40Weight(context, BMI_TYPE_2_LOW, BMI_TYPE_2_HIGH);
        } else if (bmi >= 23 && bmi < 25.0) {
            // 과체중
            Logger.i(TAG, "getWeight bmi3=" + bmi);
            return cal40Weight(context, BMI_TYPE_3_LOW, BMI_TYPE_3_HIGH);
        } else if (bmi >= 25.0 && bmi < 30) {
            // 비만
            Logger.i(TAG, "getWeight bmi4=" + bmi);
            return cal40Weight(context, BMI_TYPE_4_LOW, BMI_TYPE_4_HIGH);
        } else if (bmi >= 30) {
            // 고도비만
            Logger.i(TAG, "getWeight bmi5=" + bmi);
            return cal40Weight(context, BMI_TYPE_5_LOW, BMI_TYPE_5_HIGH);
        }

        return new float[16];
    }


    private float[] cal40Weight(Context context, float[] lowWeights, float[] highWeights) {
        float[] weight = new float[16];

        Paint paint = new Paint();
        Path path = new Path();
        paint.setColor(Color.parseColor("#ccffc0cb"));
//        path.setFillType(Path.FillType.EVEN_ODD);
        if (path.isEmpty() == false) {
            path.reset();
        }


        CommonData commonData = CommonData.getInstance(context);
        float befKg = StringUtil.getFloat(commonData.getBefKg());
        Logger.i(TAG, "bef_kg=" + befKg);

        weight[0] = 0;      // X축   0주
        weight[1] = befKg;  // Y축

        weight[2] = 15;         // X축   14주
        weight[3] = weight[1] + lowWeights[0];    // Y축

        weight[4] = 26;         // X축   26주
        weight[5] = weight[3] + lowWeights[1];    // Y축

        weight[6] = 40+1;         // X축   40 +1
        weight[7] = weight[5] + lowWeights[2];    // Y축


        weight[14] = 0;         // X축   0주
        weight[15] = befKg;    // Y축

        weight[12] = 15;         // X축  14주
        weight[13] = weight[15] + highWeights[0];    // Y축

        weight[10] = 26;         // X축  26주
        weight[11] = weight[13] + highWeights[1];    // Y축

        weight[8] = 40+1;         // X축   40주 +1
        weight[9] = weight[11] + highWeights[2];    // Y축


        Log.i(TAG, "40주_적정체중범위_LOW[0주]="+weight[1]);
        Log.i(TAG, "40주_적정체중범위_LOW[14주]="+weight[3]);
        Log.i(TAG, "40주_적정체중범위_LOW[26주]="+weight[5]);
        Log.i(TAG, "40주_적정체중범위_LOW[40주]="+weight[7]);
        Log.i(TAG, "40주_적정체중범위_HIGH[0주]="+weight[15]);
        Log.i(TAG, "40주_적정체중범위_HIGH[14주]="+weight[13]);
        Log.i(TAG, "40주_적정체중범위_HIGH[26주]="+weight[11]);
        Log.i(TAG, "40주_적정체중범위_HIGH[40주]="+weight[9]);


        return weight;
    }


}
