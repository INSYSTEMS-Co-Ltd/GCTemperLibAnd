package com.greencross.gctemperlib.push;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.TemperActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.util.GLog;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Iterator;
import java.util.List;

/**
 * Created by MobileDoctor on 2017-02-08.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    //hsh start

    int iStatePush=0;

    public static final int DIESEASE = 5;  //질병
    public static final int FEVER_MOVIE = 4; //
    //hsh end

    public static final int NOTICE = 3; //공지
    public static final int NEWS = 2; //뉴스
    public static final int FEVER = 1; //열지도

    public static final int DIET = 6; //다이어트 독려
    public static final int ALIMI = 7; // 알리

    public static final int DIESEASE_PROGRAM = 13; //유행성질환프로그램 신청
    public static final int COMMUNITY_COMMENT = 14; //커뮤니티 댓글
    public static final int COUMMUNITY_MENTION = 15; //커뮤니티 댓글언급
    public static final int COUMMUNITY_LIKE = 16; //커뮤니티 좋아요


    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String[] msg = remoteMessage.getData().get("message").split("\\|");
        FcmData data = new FcmData(msg[2], msg[1], msg[0]);

        System.out.println(remoteMessage.getData().toString());

        if(data.I_BUFFER2.length() > 0){
            switch (Integer.parseInt(data.I_BUFFER2)){
                case NOTICE :
                    //hsh start
                case FEVER_MOVIE:
                case DIESEASE:
                    //hsh end
                    if(CommonData.getInstance(this).getNoticePushAlarm())
                        showPushMessage(data);
                    break;
                case NEWS :
                    if(CommonData.getInstance(this).getNewsPushAlarm())
                        showPushMessage(data);
                    break;
                case FEVER :
                    if(CommonData.getInstance(this).getMapPushAlarm())
                        showPushMessage(data);
                    break;
                case DIET :
                    if(CommonData.getInstance(this).getDietPushAlarm())
                        showPushMessage(data);
                    break;
                case ALIMI :
                    if(CommonData.getInstance(this).getNotityPushAlarm())
                        showPushMessage(data);
                    break;
                case DIESEASE_PROGRAM :
                    if(CommonData.getInstance(this).getDisease_alert_yn())
                        showPushMessage(data);
                    break;
                case COMMUNITY_COMMENT :
                    if(CommonData.getInstance(this).getReplay_alert_yn())
                        showPushMessage(data);
                    break;
                case COUMMUNITY_MENTION :
                    if(CommonData.getInstance(this).getReplay_alert_yn())
                        showPushMessage(data);
                    break;
                case COUMMUNITY_LIKE :
                    if(CommonData.getInstance(this).getReplay_alert_yn())
                        showPushMessage(data);
                    break;
            }
        }else{
            if(CommonData.getInstance(this).getPushAlarm())
                showPushMessage(data);
        }
    }


    /**
     * 푸시 메시지를 전달 받으면 상태표시바에 표시함
     */
    private void showPushMessage(FcmData data) {

        NotificationCompat.Builder mNoti = null;


        NotificationManager mNM	= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent newIntent = new Intent(this, TemperActivity.class);

        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if(data.I_BUFFER2.length() > 0){
            switch (Integer.parseInt(data.I_BUFFER2)){
                case NOTICE :
                    iStatePush = NOTICE;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, NOTICE);
                    break;
                case NEWS :
                    iStatePush = NEWS;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, NEWS);
                    newIntent.putExtra(CommonData.EXTRA_INFO_SN, data.HIST_SN);
                    break;
                case FEVER :
                    iStatePush = FEVER;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, FEVER);
                    break;
                //hsh start
                case FEVER_MOVIE:
                    iStatePush = FEVER_MOVIE;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, FEVER_MOVIE);
                    newIntent.putExtra(CommonData.EXTRA_INFO_SN, data.HIST_SN);
                    break;
                case DIESEASE:
                    iStatePush = DIESEASE;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, DIESEASE);
                    break;
                case DIET:
                    iStatePush = DIET;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, DIET);
                    break;
                case ALIMI:
                    iStatePush = ALIMI;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, ALIMI);
                    break;
                case DIESEASE_PROGRAM:
                    iStatePush = DIESEASE_PROGRAM;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, DIESEASE_PROGRAM);
                    break;
                case COMMUNITY_COMMENT:
                    iStatePush = COMMUNITY_COMMENT;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, COMMUNITY_COMMENT);
                    break;
                case COUMMUNITY_MENTION:
                    iStatePush = COUMMUNITY_MENTION;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, COUMMUNITY_MENTION);
                    break;
                case COUMMUNITY_LIKE:
                    iStatePush = COUMMUNITY_LIKE;
                    newIntent.putExtra(CommonData.EXTRA_PUSH_TYPE, COUMMUNITY_LIKE);
                    break;

                //hsh end
            }
        }

        boolean b = getServiceTaskName(this);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, iStatePush, b?new Intent():newIntent, PendingIntent.FLAG_ONE_SHOT);


        try {   // 푸시 타입 저장

            NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(this);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(mCompatBuilder);
            style.setBigContentTitle(getString(R.string.app_name_hh_kr)); // app_name_kr
            style.bigText(data.alert);


            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            AudioManager audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
            GLog.i("getRingerMode() = " +audioManager.getRingerMode(), "dd");


            //ssshin add 2018.10.30 오레오 버전 이상 이슈처리(채널설정안하면 푸시 오지 않음.)
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("notificationChannel", "notificationChannel", importance);
                mNM.createNotificationChannel(notificationChannel);
            }
            switch(audioManager.getRingerMode()){
                case AudioManager.RINGER_MODE_VIBRATE:// 진동
                    if(CommonData.getInstance(this).getAlarmMode() == CommonData.PUSH_MODE_VIBRATE || CommonData.getInstance(this).getAlarmMode() == CommonData.PUSH_MODE_BELL_VIBRATE){ // 진동 설정이라면
                        mNoti = new NotificationCompat.Builder(this, "notificationChannel")
                                .setContentTitle( getString(R.string.app_name_hh_kr))
                                .setContentText(data.alert)
                                .setSmallIcon(R.drawable.hn_time_icon)
                                .setTicker(data.alert)
                                .setStyle(style)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                .setContentIntent(pendingIntent);
                    }else{
                        mNoti = new NotificationCompat.Builder(this, "notificationChannel")
                                .setContentTitle( getString(R.string.app_name_hh_kr) )
                                .setContentText(data.alert)
                                .setSmallIcon(R.drawable.hn_time_icon)
                                .setTicker(data.alert)
                                .setStyle(style)
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent);
                    }
                    break;
                case AudioManager.RINGER_MODE_NORMAL:// 소리
                    if(CommonData.getInstance(this).getAlarmMode() == CommonData.PUSH_MODE_VIBRATE){ // 진동 설정이라면
                        mNoti = new NotificationCompat.Builder(this, "notificationChannel")
                                .setContentTitle(getString(R.string.app_name_hh_kr))
                                .setContentText(data.alert)
                                .setSmallIcon(R.drawable.hn_time_icon)
                                .setTicker(data.alert)
                                .setStyle(style)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                .setContentIntent(pendingIntent);
                    }else if(CommonData.getInstance(this).getAlarmMode() == CommonData.PUSH_MODE_BELL){  // 벨소리 설정이라면
                        mNoti = new NotificationCompat.Builder(this, "notificationChannel")
                                .setContentTitle( getString(R.string.app_name_hh_kr))
                                .setContentText(data.alert)
                                .setSmallIcon(R.drawable.hn_time_icon)
                                .setTicker(data.alert)
                                .setStyle(style)
                                .setAutoCancel(true)
                                .setSound(soundUri).setLights(000000255,500,2000)
                                .setContentIntent(pendingIntent);
                    }else if(CommonData.getInstance(this).getAlarmMode() == CommonData.PUSH_MODE_BELL_VIBRATE) {  // 벨+진동 모드라면
                        mNoti = new NotificationCompat.Builder(this, "notificationChannel")
                                .setContentTitle( getString(R.string.app_name_hh_kr))
                                .setContentText(data.alert)
                                .setSmallIcon(R.drawable.hn_time_icon)
                                .setTicker(data.alert)
                                .setStyle(style)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                .setSound(soundUri).setLights(000000255,500,2000)
                                .setContentIntent(pendingIntent);
                    } else{  // 무음
                        mNoti = new NotificationCompat.Builder(this, "notificationChannel")
                                .setContentTitle(getString(R.string.app_name_hh_kr))
                                .setContentText(data.alert)
                                .setSmallIcon(R.drawable.hn_time_icon)
                                .setTicker(data.alert)
                                .setStyle(style)
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent);
                    }
                    break;
                case AudioManager.RINGER_MODE_SILENT:// 무음
                    mNoti = new NotificationCompat.Builder(this, "notificationChannel")
                            .setContentTitle(getString(R.string.app_name_hh_kr))
                            .setContentText(data.alert)
                            .setSmallIcon(R.drawable.hn_time_icon)
                            .setTicker(data.alert)
                            .setStyle(style)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);
                    break;
            }

            mNM.notify(iStatePush/*(int)System.currentTimeMillis()*/, mNoti.build());

        }catch(Exception e){
            GLog.e(e.toString());
        }
    }

    public class FcmData{
        public String alert;
        public String HIST_SN;
        public String I_BUFFER2;

        public FcmData(String alert, String HIST_SN, String I_BUFFER2){
            this.alert = alert;
            this.HIST_SN = HIST_SN;
            this.I_BUFFER2 = I_BUFFER2;
        }
    }

    private boolean getServiceTaskName(Context context) {

        boolean checked = false;
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info;
        GLog.i("myTaskName() = "+context.getPackageName(), "dd");
        info = am.getRunningTasks(1);
        for (Iterator iterator = info.iterator(); iterator.hasNext();) {
            ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) iterator.next();
            GLog.i("getServiceTaskName() = "+runningTaskInfo.topActivity.getClassName(), "dd");
            if (context.getPackageName().contains(runningTaskInfo.topActivity.getClassName())) {
                GLog.i("getServiceTaskName() = true", "dd");
                return true;
            }
        }
        return false;
    }
}