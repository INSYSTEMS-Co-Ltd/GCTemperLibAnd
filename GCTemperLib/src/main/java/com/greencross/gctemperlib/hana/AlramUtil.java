package com.greencross.gctemperlib.hana;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlramUtil {
    private final static String TAG = AlramUtil.class.getSimpleName();


//    public static void setAlarmRepeat(Context context, int idx, String time_hhmm, long repeatTime) {
//        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        Intent alramIntent = new Intent(context, NotificationUtil.class);
//        alramIntent.putExtra("idx", idx);
//        PendingIntent alramSender = PendingIntent.getBroadcast(context, idx, alramIntent, 0);
//
//
//        Calendar cal = Calendar.getInstance();
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                //API 19 이상 API 23미만
//                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + repeatTime, alramSender);
//            } else {
//                //API 19미만
//                mAlarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + repeatTime, alramSender);
//            }
//        } else {
//            //API 23 이상
//            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + repeatTime, alramSender);
//        }
//    }

    /**
     * 일일 미션 알람 설정
     *
     * @param context
     */
    public static void setTemperAlramRepeat(Context context, Class<? extends BroadcastReceiver> receiver) {
        releaseAlarm(context, GCAlarmReceiver.ALRAM_REPEAT_1HOUR);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        Intent alramIntent = new Intent(context, GCAlarmService.class);
        Intent alramIntent = new Intent(context, receiver);
//        alramIntent.putExtra("idx",idx);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, GCAlarmReceiver.ALRAM_REPEAT_1HOUR, alramIntent, 0);

        long repeatTime = setTriggerTime();
//        long repeatTime = cal.getTimeInMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, repeatTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, repeatTime, pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, repeatTime, pendingIntent);
        }
    }

    /**
     * 1시간 반복 알람
     * @return
     */
    private static long setTriggerTime() {
//        long atime = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1); // 한시간뒤알람
        return cal.getTimeInMillis();
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        long btime = cal.getTimeInMillis();
//        long triggerTime = btime;
//        if (atime > btime)
//            triggerTime += 1000 * 60 * 60 * 24; // 하루 뒤 알람
//
//        return triggerTime;
    }

    /**
     * 알람 해제 메소드
     * @param context
     * @param requestCode
     */
    public static void releaseAlarm(Context context, int requestCode) {
        AlarmManager fiveToHourAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent fiveIntent = new Intent(context, GCAlarmReceiver.class);
        PendingIntent fiveSender = PendingIntent.getBroadcast(context, requestCode, fiveIntent, 0);

        fiveToHourAlarmManager.cancel(fiveSender);
        fiveSender.cancel();
    }


    //알람 해제 메소드
//    public static void releaseAlarm(Context context, int requestCode) {
//        AlarmManager fiveToHourAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        Intent fiveIntent = new Intent(context, NotificationUtil.class);
//
//        PendingIntent fiveSender = PendingIntent.getBroadcast(context, requestCode, fiveIntent, 0);
//
//        fiveToHourAlarmManager.cancel(fiveSender);
//        fiveSender.cancel();
//    }
//
//
//    //알람 해제 메소드
//    public static boolean checkAlram(Context context, int idx) {
//        boolean isAlram = false;
//
//        Intent intent = new Intent(context, NotificationUtil.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, idx, intent, PendingIntent.FLAG_NO_CREATE);
//
//        if (sender == null) {
//        } else {
//            isAlram = true;
//        }
//        return isAlram;
//    }
}

