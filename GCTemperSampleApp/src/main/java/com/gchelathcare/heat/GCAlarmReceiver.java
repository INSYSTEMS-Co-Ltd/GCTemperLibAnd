package com.gchelathcare.heat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.greencross.gctemperlib.DummyActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.hana.AlramUtil;
import com.greencross.gctemperlib.hana.GCAlramType;
import com.greencross.gctemperlib.hana.TemperControlFragment;

/**
 *
 *
 * {"data":{"msg":"message...","serviceCode":"GEMS","functionCode":"H002","title":"title..."},"to":"fBQoGNO-E9g:APA91bHzFgbWw5osPjee4hccqgOafQrqRbfC-HKd5UxDFIWcmF0j80AlRH10g_XEQL7aWM55nwtR3n12dwgLXMpv_9f8l5Md53rdEl7SUPfbmiGY05rL2HIEPRA4Qff3Q0Uu_p-LQeoe"}
 */
public class GCAlarmReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isAlramEanble = SharedPref.getInstance(context).getPreferences(GCAlramType.GC_ALRAM_TYPE_독려.getAlramName(), false);
        if (isAlramEanble) {
            AlramUtil.setTemperAlramRepeat(context, GCAlarmReceiver.class);
        }

        Log.i(TAG, "intent.getAction()="+intent.getAction());
        if (intent.getAction() != Intent.ACTION_BOOT_COMPLETED) {
            showNotification(context, "체온 측정 알림");
        }
    }

    /**
     * 푸시 메시지를 전달 받으면 상태표시바에 표시함
     */
    public static void showNotification(Context context, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = context.getPackageName();   // context.getString(context.getPackageName());
            String CHANNEL_NAME = context.getPackageName(); // context.getString(R.string.app_name);
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(false);
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(context, DummyActivity.class);
        notificationIntent.putExtra("ALRAM_MOVE_MENU", TemperControlFragment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, (int)(System.currentTimeMillis()/1000), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        String notiTitle = "하나카드";
        builder.setContentTitle(notiTitle)   // 상태바 드래그시 보이는 타이틀
                .setContentText(content)                                // 상태바 드래그시 보이는 서브타이틀
                .setTicker(content)                                     // 상태바 한줄 메시지
                .setSmallIcon(R.drawable.hn_temper_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.hn_temper_icon))
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        notificationManager.notify(0, builder.build());
    }
}