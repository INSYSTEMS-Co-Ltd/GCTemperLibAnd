package com.greencross.gctemperlib.intro;

/**
 * 최초 로그인 시 3개월치 데이터 받아오기
 */

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.greencross.gctemperlib.greencare.database.DBHelper;
import com.greencross.gctemperlib.greencare.database.DBHelperFoodDetail;
import com.greencross.gctemperlib.greencare.database.DBHelperFoodMain;
import com.greencross.gctemperlib.greencare.database.DBHelperMessage;
import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_get_hedctdata;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_get_meal_input_data;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_get_meal_input_food_data;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_infra_message;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.greencare.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FirstDataGreenCare {
    private final String TAG = FirstDataGreenCare.class.getSimpleName();
    private Context mContext;
    /**
     * 앱 삭제시 녹십자에 저장된 3개월치 데이터 가져오기
     */
    private int m3MonthIdx = 2;
    public void doFirstData(Context context, final ApiData.IStep iStep) {
        mContext = context;
        Log.i(TAG, "doFirstData.m3MonthIdx="+m3MonthIdx);
        if (m3MonthIdx <= 4) {
            getFirstData(""+m3MonthIdx, new ApiData.IStep() {
                @Override
                public void next(Object obj) {
                    if (m3MonthIdx == 4) {
                        // 데이터가 6개 데이터 가져오기 되면 음식 데이터 가져오기(걸음,혈당,혈압,체중,물)

                        getFoodData(new ApiData.IStep() {
                            @Override
                            public void next(Object obj) {

                                iStep.next(null);

                                // TODO : XXX 건강메시지 제낌
//                                // 건강 메시지 가져오기
//                                getHealthMessageData(new ApiData.IStep() {
//                                    @Override
//                                    public void next(Object obj) {
//
//                                        boolean isConfirmMsg = SharedPref.getInstance(mContext).getPreferences(SharedPref.HEALTH_MESSAGE_CONFIRIM, false);
//                                        if(isConfirmMsg == false){
//                                            DBHelper helper = new DBHelper(mContext);
//                                            DBHelperMessage db = helper.getMessageDb();
//                                            List<MessageModel> messageList = db.getResultAll(helper, Tr_infra_message_write.INFRA_TY_ALL);
//                                            if(messageList.size() > 0) {
//                                                SharedPref.getInstance(mContext).savePreferences(SharedPref.HEALTH_MESSAGE_HEALTH, true);
//                                                SharedPref.getInstance(mContext).savePreferences(SharedPref.HEALTH_MESSAGE_SUGAR, true);
//                                            }
//                                        }
//                                        iStep.next(null);
//                                    }
//                                });
                            }
                        });

                    } else {
                        // 3개월 데이터 가져오기 (걸음, 혈당 혈압 체중)
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                m3MonthIdx++;
                                doFirstData(context, iStep);
                            }
                        }, 1);
                    }
                }
            });
        }
    }

    /**
     * 3개월치 데이터 가져오기
     * @param code
     * @param iStep
     */
    private void getFirstData(final String code, final ApiData.IStep iStep) {
        // 한번 저장 완료가 되면 호출 하지 않기 위한 값
        boolean isSaved = SharedPref.getInstance(mContext).getPreferences(SharedPref.SAVED_LOGIN_ID+code , false);
        Logger.i(TAG, "getFirstData.code="+code +", isSaved="+isSaved);
        if (isSaved) {
            iStep.next(null);
            return;
        }


        Calendar cal = Calendar.getInstance(Locale.KOREA);
        Date now = new Date();
        cal.setTime(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Tr_get_hedctdata.RequestData requestData = new Tr_get_hedctdata.RequestData();

        CommonData login = CommonData.getInstance(mContext);

        requestData.mber_sn = login.getMberSn();
        requestData.get_data_typ = code;
        requestData.end_day = CDateUtil.getToday_yyyy_MM_dd();    // 오늘 날자
        cal.add(Calendar.MONTH, -3);                        // 3개월 전 날자
        requestData.begin_day = sdf.format(cal.getTimeInMillis());   // 20170301


        new ApiData().getData(mContext, Tr_get_hedctdata.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_get_hedctdata) {
                   try {
                       DBHelper helper = new DBHelper(mContext);

                       // 1: 운동      2:혈당    3:혈압    4:체중    5:물data (제일 Low data), 6:심박수
                       Tr_get_hedctdata data = (Tr_get_hedctdata)obj;
                       if ("1".equals(data.get_data_typ)) {
                           helper.getStepDb().insert( data, true);
                       } else if ("2".equals(data.get_data_typ)) {
                           helper.getSugarDb().insert(data, true);
                       } else if ("3".equals(data.get_data_typ)) {
                           helper.getPresureDb().insert(helper, data, true);
                       } else if ("4".equals(data.get_data_typ)) {
                           helper.getWeightDb().insert(data, true);
                       }
//                       else if ("5".equals(data.get_data_typ)) {
//                           helper.getWaterDb().insert(data.data_list, true);
//                       }else if ("6".equals(data.get_data_typ)) {
//                           helper.getPPGDb().insert(data, true);
//                       }
                       SharedPref.getInstance(mContext).savePreferences(SharedPref.SAVED_LOGIN_ID+code , true);

                       iStep.next(obj);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                } else {

                }
            }
        });
    }

    private void getFoodData(final ApiData.IStep iStep) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        final String endDate = CDateUtil.getToday_yyyy_MM_dd();
        Calendar cal =  CDateUtil.getCalendar_yyyyMMdd(endDate);
        cal.add(Calendar.MONTH, -3);
        final String startDate = sdf.format(cal.getTimeInMillis());

        // 식사데이터 가져와서 sqlite 저장하기
        getFirstMealData(startDate, endDate, new ApiData.IStep() {
            @Override
            public void next(Object obj) {

                // 음식 데이터 가져와서 sqlite 저장하기
                getFirstFoodData(startDate, endDate, new ApiData.IStep() {
                    @Override
                    public void next(Object obj) {
                        iStep.next(obj);
                    }
                });
            }
        });
    }


    /**
     * 데이터 가져오기(식사)
     */
    private void getFirstMealData(final String beginDate, final String endDate, final ApiData.IStep iStep) {
        final boolean isSaved = SharedPref.getInstance(mContext).getPreferences(SharedPref.IS_SAVED_MEAL_DB, false);

        if (isSaved) {
            iStep.next(isSaved);
            return;
        }

        Tr_get_meal_input_data.RequestData requestData = new Tr_get_meal_input_data.RequestData();

        CommonData login = CommonData.getInstance(mContext);

        requestData.mber_sn = login.getMberSn();
        requestData.begin_day = beginDate;
        requestData.end_day = endDate;

        new ApiData().getData(mContext, Tr_get_meal_input_data.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_get_meal_input_data) {
                    Tr_get_meal_input_data data = (Tr_get_meal_input_data) obj;

                    DBHelper helper = new DBHelper(mContext);
                    DBHelperFoodMain db = helper.getFoodMainDb();
                    db.insert(data.data_list);

                    SharedPref.getInstance(mContext).savePreferences(SharedPref.IS_SAVED_MEAL_DB, true);

                    iStep.next(true);
                } else {
                    iStep.next(false);
                }
            }
        });
    }


    /**
     * 데이터 가져오기 (음식)
     */
    private void getFirstFoodData(String startDate, String endDate, final ApiData.IStep iStep) {
        boolean isSaved = SharedPref.getInstance(mContext).getPreferences(SharedPref.IS_SAVED_FOOD_DB, false);
        Logger.i(TAG, "getFirstFoodData, isSaved="+isSaved);
        if (isSaved) {
            iStep.next(isSaved);
            return;
        }

        final Tr_get_meal_input_food_data.RequestData requestData = new Tr_get_meal_input_food_data.RequestData();
        CommonData login = CommonData.getInstance(mContext);

        requestData.mber_sn = login.getMberSn();
        requestData.begin_day = startDate;
        requestData.end_day = endDate;

        new ApiData().getData(mContext, Tr_get_meal_input_food_data.class, requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_get_meal_input_food_data) {
                    Tr_get_meal_input_food_data data = (Tr_get_meal_input_food_data) obj;

                    DBHelper helper = new DBHelper(mContext);
                    DBHelperFoodDetail db = helper.getFoodDetailDb();
                    db.insert(data.data_list);

                    SharedPref.getInstance(mContext).savePreferences(SharedPref.IS_SAVED_FOOD_DB, true);

                    iStep.next(true);
                } else {
                    iStep.next(false);
                }
            }
        });
    }

    /**
     * 건강 메시지 가져와 Sqlite 저장하기
     */
    private int mHealthMsgCnt = 1;
    private void getHealthMessageData(final ApiData.IStep iStep) {

        final boolean isSaved = SharedPref.getInstance(mContext).getPreferences(SharedPref.IS_SAVED_HEALTH_MESSAGE_DB, false);

        if (isSaved) {
            iStep.next(isSaved);
            return;
        }

        Tr_infra_message.RequestData reqData = new Tr_infra_message.RequestData();
        CommonData login = CommonData.getInstance(mContext);

        reqData.mber_sn = login.getMberSn();
        reqData.pageNumber = ""+mHealthMsgCnt;

        new ApiData().getData(mContext, Tr_infra_message.class, reqData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_infra_message) {
                    Tr_infra_message recvData = (Tr_infra_message) obj;
                    int pageNum = StringUtil.getIntVal(recvData.pageNumber);
                    int pageMaxNum = StringUtil.getIntVal(recvData.maxpageNumber);

                    DBHelper helper = new DBHelper(mContext);
                    DBHelperMessage db = helper.getMessageDb();
                    db.insert(recvData.message_list, true);

                    if (pageNum > pageMaxNum) {
                        getHealthMessageData(iStep);
                    } else {
                        SharedPref.getInstance(mContext).savePreferences(SharedPref.IS_SAVED_HEALTH_MESSAGE_DB, true);
                        iStep.next(obj);
                    }

                    mHealthMsgCnt++;
                }
            }
        });
    }


}
