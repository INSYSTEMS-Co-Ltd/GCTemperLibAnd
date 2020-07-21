package com.greencross.gctemperlib.push;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.view.Window;
import android.view.WindowManager;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.TemperActivity;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.WakeLocker;


public class ShowAlertMsgActivity extends Activity {

	private int notiIndex = 1;

	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			if (CommonData.getInstance(ShowAlertMsgActivity.this).getPushAlarm()) {        // 푸쉬 켜짐
				requestWindowFeature(Window.FEATURE_NO_TITLE);
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
						WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

				WakeLocker.acquire(this);

				WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
				layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND |
						WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
						WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
						WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
				layoutParams.dimAmount = 0.7f;
				getWindow().setAttributes(layoutParams);

				WakeLocker.release();

				Intent i = getIntent();
				String msg = i.getStringExtra("content_msg");
				final int chl_sn = i.getIntExtra("chl_sn", 0);

				showPushMessage(msg, chl_sn);

				CustomAlertDialog mDialog = new CustomAlertDialog(this, CustomAlertDialog.TYPE_B);
				mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
				mDialog.setContent(msg);
				mDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), (dialog, button) -> {
                    dialog.dismiss();
                    finish();
                });
				mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                    Intent intent = new Intent(ShowAlertMsgActivity.this, TemperActivity.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("chl_sn", chl_sn);

                    startActivity(intent);
					dialog.dismiss();
					finish();
                });
				mDialog.show();
			} else {
				finish();
			}

		}catch (Exception e){
			e.printStackTrace();
			finish();
		}
	}

	/**
	 * 푸시 메시지를 전달 받으면 상태표시바에 표시함
	 */
	private void showPushMessage(String message , int chl_sn) {

		Notification mNoti = null;


		NotificationManager mNM	= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Intent newIntent = new Intent(this, TemperActivity.class);

		//newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        newIntent.putExtra(CommonData.JSON_PUSH_NOTICE_ID, Integer.parseInt(pid));
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		newIntent.putExtra("chl_sn", chl_sn);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, notiIndex, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);


		try {   // 푸시 타입 저장

			NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(this);
			NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(mCompatBuilder);
//            style.setSummaryText("and More +");
			style.setBigContentTitle(getString(R.string.app_name_kr));
			style.bigText(message);


			GLog.i("message = " + message, "dd");

			long[] vib = {500l, 100l, 500l, 100l};
			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			AudioManager audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
			GLog.i("getRingerMode() = " +audioManager.getRingerMode(), "dd");
			switch(audioManager.getRingerMode()){
				case AudioManager.RINGER_MODE_VIBRATE:// 진동
					if(CommonData.getInstance(ShowAlertMsgActivity.this).getAlarmMode() == CommonData.PUSH_MODE_VIBRATE ){ // 진동 설정이라면
						mNoti = new NotificationCompat.Builder(this)
								.setContentTitle( getString(R.string.app_name_kr) )
								.setContentText(message)
								.setSmallIcon(R.mipmap.noti_icon)
								.setTicker(message)
								.setStyle(style)
								.setAutoCancel(true)
								.setVibrate(vib)
								.setContentIntent(pendingIntent).getNotification();
					}else{
						mNoti = new NotificationCompat.Builder(this)
								.setContentTitle( getString(R.string.app_name_kr) )
								.setContentText(message)
								.setSmallIcon(R.mipmap.noti_icon)
								.setTicker(message)
								.setStyle(style)
								.setAutoCancel(true)
								.setContentIntent(pendingIntent).getNotification();
					}
					break;
				case AudioManager.RINGER_MODE_NORMAL:// 소리
					if(CommonData.getInstance(ShowAlertMsgActivity.this).getAlarmMode() == CommonData.PUSH_MODE_VIBRATE){ // 진동 설정이라면
						mNoti = new NotificationCompat.Builder(this)
								.setContentTitle( getString(R.string.app_name_kr) )
								.setContentText(message)
								.setSmallIcon(R.mipmap.noti_icon)
								.setTicker(message)
								.setStyle(style)
								.setAutoCancel(true)
								.setVibrate(vib)
								.setContentIntent(pendingIntent).getNotification();
					}else if(CommonData.getInstance(ShowAlertMsgActivity.this).getAlarmMode() == CommonData.PUSH_MODE_BELL){  // 벨소리 설정이라면
						mNoti = new NotificationCompat.Builder(this)
								.setContentTitle( getString(R.string.app_name_kr) )
								.setContentText(message)
								.setSmallIcon(R.mipmap.noti_icon)
								.setTicker(message)
								.setStyle(style)
								.setAutoCancel(true)
								.setSound(soundUri)
								.setContentIntent(pendingIntent).getNotification();
					}else if(CommonData.getInstance(ShowAlertMsgActivity.this).getAlarmMode() == CommonData.PUSH_MODE_BELL_VIBRATE) {  // 벨+진동 모드라면
						mNoti = new NotificationCompat.Builder(this)
								.setContentTitle( getString(R.string.app_name_kr) )
								.setContentText(message)
								.setSmallIcon(R.mipmap.noti_icon)
								.setTicker(message)
								.setStyle(style)
								.setAutoCancel(true)
								.setVibrate(vib)
								.setSound(soundUri)
								.setContentIntent(pendingIntent).getNotification();
					} else{  // 무음
						mNoti = new NotificationCompat.Builder(this)
								.setContentTitle(getString(R.string.app_name_kr))
								.setContentText(message)
								.setSmallIcon(R.mipmap.noti_icon)
								.setTicker(message)
								.setStyle(style)
								.setAutoCancel(true)
								.setContentIntent(pendingIntent).getNotification();
					}
					break;
				case AudioManager.RINGER_MODE_SILENT:// 무음
					mNoti = new NotificationCompat.Builder(this)
							.setContentTitle( getString(R.string.app_name_kr) )
							.setContentText(message)
							.setSmallIcon(R.mipmap.noti_icon)
							.setTicker(message)
							.setStyle(style)
							.setAutoCancel(true)
							.setContentIntent(pendingIntent).getNotification();
					break;
			}

			mNM.notify(notiIndex, mNoti);

		}catch(Exception e){
			GLog.e(e.toString());
		}
	}

	@Override protected void attachBaseContext(Context newBase) {
		// // super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
        super.attachBaseContext(newBase);
	}
}
