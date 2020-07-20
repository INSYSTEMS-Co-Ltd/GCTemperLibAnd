package com.greencross.gctemperlib.fever;

import com.greencross.gctemperlib.main.MainActivity;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.common.CommonData;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by MobileDoctor on 2017-03-21.
 */

public class FeverDiagnosis {

    static FeverDiagnosis m_sharedInstance;

    public static FeverDiagnosis shared()
    {
        if(m_sharedInstance == null){
            synchronized(FeverDiagnosis.class)
            {
                if(m_sharedInstance == null)
                    m_sharedInstance = new FeverDiagnosis();
            }
        }
        return m_sharedInstance;
    }

    public String getDiagnosis(double curFever, Date curDate){
        String strCode = "";

        int code_1 = 0;    // 응급판단
        int code_2 = 0;    // 해열제 정보 표시 유무
        int code_3 = 0;     // 해열제 교차복용 유무
        int code_4 = 0;     // 미온수 여부
        int code_5 = 0;     // 수분섭취 여부
        int code_6 = 0;     // 쿨패치 여부


        // 고온 응급
        if(curFever >= 40d)
            code_1 = 1;

        // 저온 응급
        if(curFever < 35.5d)
            code_1 = 2;

        // 24시간 응급
        if(curFever >= 38d){
            if(getTempArray(curDate))
                code_1 = 3;
        }

        // 신생아 응급
        if(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).born_to_day < 100 && curFever >= 38d ){
            code_1 = 4;
        }

        if(MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).born_to_day > 120){
            // 해열제 정보 표시 유무
            if(curFever >= 38)
                code_2 = 1;

            // 해열제 교차복용 유무
            if(LastRemedyCheck(curDate))
                code_3 = 1;
        }

        // 미온수 여부
        if(curFever >= 38.5d)
            code_4 = 1;

        // 수분섭취 여부
        if(curFever >= 38.5d)
            code_5 = 1;

        strCode = code_1+"_"+code_2+"_"+code_3+"_"+code_4+"_"+code_5+"_"+code_6;

        return strCode;
    }


    // 24시간 체온 평균 구하기
    public boolean getTempArray(Date curDate){
        boolean bool = false;
        try {
            SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME_S);

            if(TemperMainActivity.mFeverItems.size() < 4){
                return bool;
            }else {
                double sumTime = 0d;
                double sumTemp = 0d;

                for (int i = 0; i < TemperMainActivity.mFeverItems.size(); i++) {
                    double f_time = 0;
                    if (i == 0) {
                        f_time = Util.subDate(curDate, format.parse(TemperMainActivity.mFeverItems.get(i).getmInputDe()));
                    } else {
                        f_time = Util.subDate(format.parse(TemperMainActivity.mFeverItems.get(i - 1).getmInputDe()), format.parse(TemperMainActivity.mFeverItems.get(i).getmInputDe()));
                    }
                    sumTime += f_time;
                    BigDecimal preNum = new BigDecimal(f_time);
                    BigDecimal postNum;
                    postNum = new BigDecimal(TemperMainActivity.mFeverItems.get(i).getmInputFever());
                    sumTemp += preNum.multiply(postNum).doubleValue();
                }

                if(sumTemp/sumTime > 39.5d)
                    bool = true;

                return bool;
            }
        }catch (Exception e){
            e.printStackTrace();
            return bool;
        }
    }

    // 해열제 교차복용 정보 표시 유무
    public boolean LastRemedyCheck(Date curDate){

        boolean bool = false;
        try {
            GregorianCalendar mCalendar = new GregorianCalendar();
            mCalendar.setTime(curDate);
            mCalendar.add(Calendar.HOUR_OF_DAY, -4);
            Date checkDate = mCalendar.getTime();

            SimpleDateFormat format = new SimpleDateFormat(CommonData.PATTERN_DATETIME_S);

            for(int i = 0; i < TemperMainActivity.mRemedyItems.size(); i++){
                if(checkDate.compareTo(format.parse(TemperMainActivity.mRemedyItems.get(i).getmInputDe())) < 0){
                    bool = true;
                }
            }
            return bool;
        }catch (Exception e){
            e.printStackTrace();

            return bool;
        }
    }
}
