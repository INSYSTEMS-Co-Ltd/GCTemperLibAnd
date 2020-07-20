package com.greencross.gctemperlib.intro;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.greencross.gctemperlib.RCApplication;
import com.greencross.gctemperlib.base.IntroBaseActivity;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.util.GLog;
import com.greencross.gctemperlib.util.PermissionUtils;
import com.greencross.gctemperlib.util.Util;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomAlertDialog;
import com.greencross.gctemperlib.common.CustomAlertDialogInterface;
import com.greencross.gctemperlib.common.CustomAsyncListener;
import com.greencross.gctemperlib.common.NetworkConst;
import com.greencross.gctemperlib.network.RequestApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


/**
 * Created by jihoon on 2016-03-21.
 * 로고 클래스
 * @since 0, 1
 */
public class IntroActivity extends IntroBaseActivity {

	public static final int DIALOG_UPDATE		= 0;
	public static final int DIALOG_DATA_ERROR	= 1;

	private boolean mFlag = true;											// 자동 로그인 플래그

	private PackageManager pm;
	private PackageInfo pi	= null;

	CustomAlertDialog mDialog;

	ImageView introimg;

	private Handler mIntroHandler = new Handler();

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro_activity);

		init();

		/*try {
			checkAcvity();
		}catch (Exception e){

		}*/

		Uri linkValueUrl = getIntent().getData();
		Bundle extras = getIntent().getExtras();
		if(linkValueUrl != null) {   // 카카오링크 or Web으로 부터 실행
			CommonData.getInstance(IntroActivity.this).setLink(linkValueUrl.getQueryParameter("service"));
			CommonData.getInstance(IntroActivity.this).setLink1(linkValueUrl.getQueryParameter("param1"));
			Log.i("LoginActivity", "Check LinkData - service :" + CommonData.getInstance(IntroActivity.this).getLink());
			Log.i("LoginActivity", "Check LinkData1 - service :" + CommonData.getInstance(IntroActivity.this).getLink1());
		}else{
			Log.i("LoginActivity", "No Data");
			CommonData.getInstance(IntroActivity.this).setLink("");
			CommonData.getInstance(IntroActivity.this).setLink1("");
		}


		introimg.postDelayed(() -> {
            if (!PermissionUtils.canAccessLocation(IntroActivity.this) ) {  // 폰 정보, 위취 권한이 없다면
				View view = LayoutInflater.from(IntroActivity.this).inflate(R.layout.splash_permission_dialog_view,null);
				CDialog dlg = CDialog.showDlg(IntroActivity.this, view);
				dlg.setTitle(getString(R.string.splash_permission_dlg_title));
				dlg.setOkButton(getString(R.string.popup_dialog_button_confirm), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ActivityCompat.requestPermissions(IntroActivity.this, PermissionUtils.NECESSARY_PERMS1, CommonData.PERMISSION_REQUEST_NECESSARY_PERMS);
					}
				});
				dlg.setNoButton(getString(R.string.popup_dialog_button_quit), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

            } else {
                requestGetInfo();
            }
        }, CommonData.ANI_DELAY_2000);
	}

	private void checkAcvity() {
		ActivityManager manager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
		List<ActivityManager.RunningTaskInfo> tasks =  manager.getRunningTasks(Integer.MAX_VALUE);

		for (ActivityManager.RunningTaskInfo taskInfo : tasks) {
			/*if(taskInfo.baseActivity.getClassName().equals(<your package name>.<your class name>) && (taskInfo.numActivities > 1)){
				finish();
			}*/
			GLog.i("acvity name = " + taskInfo.baseActivity.getClassName(), "dd");

		}

		List<ActivityManager.AppTask> tasks2 = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			tasks2 = manager.getAppTasks();
			if(tasks2!=null) {
				GLog.i("acvity name2 = " + tasks2.get(0).getClass().getName(), "dd");
				tasks2.get(0).finishAndRemoveTask();
			}
		}


	}

	/**
	 * 초기화
	 */
	public void init(){

		introimg	=	(ImageView)	findViewById(R.id.intro_image);


		pm = getPackageManager();

		try {
			pi			= getPackageManager().getPackageInfo( getPackageName(), 0);
			CommonData.getInstance(IntroActivity.this).setAppVersion(pi.versionName.toString());
			GLog.i("app_ver  = " + pi.versionName.toString(), "dd");
			GLog.i("pi.packagename = " + pi.packageName, "dd");
		}
		catch(Exception e){
			GLog.e(e.toString());
		}
	}

	/**
	 * 자동로그인
	 */
	public void autoLoginCheckToView() {

		GLog.i("autoLoginCheckToView()", "dd");
		ArrayList<NameValuePair> params;

		String mberSn = commonData.getMberSn();	// 부모 회원 고유키
		String mberNo = commonData.getMberNo();	// 부모 가입키

		Intent intent = null;

		GLog.i("mberSn = " + mberSn, "dd");

//		if(!mberSn.equals("")){	// 부모 고유 키값이 존재하다면 부모 로그인
//			if(commonData.getAutoLogin()){	// 자동로그인 체크라면 자동로그인
//				if(commonData.getMberPwd().equals("")){
//					intent = new Intent(IntroActivity.this, LoginActivity.class);
//					startActivity(intent);
//					finish();
//				}else {
//					requestLogin();
//				}
//			}else{	// 자동로그인이 아닌경우 로그인 페이지로 이동
//				commonData.setMberPwd("");
//				intent = new Intent(IntroActivity.this, LoginActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		}else{	// 자녀도 부모도 아닌 경우
//			if(commonData.getMemberCertifi()) {// 회원가입 X, 회원인증 O 인 경우 가입화면으로
//				intent = new Intent(IntroActivity.this, JoinActivity.class);
//				startActivity(intent);
//				finish();
//			}else{// 회원가입 X, 회원인증 X 인 경우
//				if(CommonData.getInstance(IntroActivity.this).getGuideCheck()){	// 사용가이드 확인했다면 인증화면으로 이동
//					GLog.i("사용가이드 확인함", "dd");
//					intent = new Intent(IntroActivity.this, LoginActivity.class);
//
//				}else {	// 사용 가이드 확인 안했다면
//					GLog.i("사용가이드 확인 안함", "dd");
//					intent = new Intent(IntroActivity.this, PrologueActivity.class);
//				}
//				startActivity(intent);
//				finish();
//			}
//		}
	}

	/**
	 * 앱 최신버전 가져오기
	 */
	public void requestGetInfo(){
		GLog.i("requestGetInfo", "dd");
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		try {
			JSONObject object = new JSONObject();
			object.put(CommonData.JSON_API_CODE, CommonData.METHOD_GET_INFORMATION);
			object.put(CommonData.JSON_APP_CODE, CommonData.APP_CODE_ANDROID + Build.VERSION.RELEASE);
			object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);

			params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

			RequestApi.requestApi(IntroActivity.this, NetworkConst.NET_GET_INFORMATION, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgressLayout());
		}catch(Exception e){
			GLog.i(e.toString(), "dd");
		}

	}

	/**
	 * 로그인
	 */
	public void requestLogin(){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		try {
			JSONObject object = new JSONObject();
			object.put(CommonData.JSON_API_CODE, CommonData.METHOD_LOGIN);
			object.put(CommonData.JSON_APP_CODE, CommonData.APP_CODE_ANDROID + Build.VERSION.RELEASE);
			object.put(CommonData.JSON_INSURES_CODE, CommonData.INSURE_CODE);
			object.put(CommonData.JSON_MBER_ID, commonData.getMberId());
			object.put(CommonData.JSON_MBER_PWD, commonData.getMberPwd());

//			object.put(CommonData.JSON_MBER_SN, commonData.getMberSn());
			GLog.i("deviceToken = " + RCApplication.deviceToken, "dd");
			if(RCApplication.deviceToken != null) {
				object.put(CommonData.JSON_TOKEN, RCApplication.deviceToken);
			}
			object.put(CommonData.JSON_PHONE_MODEL, Build.MODEL);
			//TODO 테스트 시 버전 99
            object.put(CommonData.JSON_APP_VER, commonData.getAppVersion());
//			object.put(CommonData.JSON_APP_VER, "99");

			params.add(new BasicNameValuePair(CommonData.JSON_JSON, object.toString()));

			RequestApi.requestApi(IntroActivity.this, NetworkConst.NET_LOGIN, NetworkConst.getInstance().getDefDomain(), networkListener, params, getProgressLayout());
		}catch(Exception e){
			GLog.i(e.toString(), "dd");
		}
	}

	/**
	 * 프롤로그 화면이동 런어블
	 */
	private Runnable mIntroRunnable = () -> {
//        GLog.i("mIntroRunnable start", "dd");
//        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
//        startActivity(intent);
//        IntroActivity.this.finish();
    };

	/**
	 * 팝업 다이얼로그 클릭 리스너
	 */
	public CustomAlertDialogInterface.OnClickListener dialogClickListener = (dialog, button) -> {
        dialog.dismiss();
        Intent 	intent = null;
        ApplicationInfo appInfo = null;
        switch ( dialog.getId() ) {
            case DIALOG_UPDATE:													// 업데이트 button click
                Uri uri 	= Uri.parse( "market://details?id=" +pi.packageName);   // 패키지명
                GLog.i("uri = " + uri, "dd");
                intent	= new Intent( Intent.ACTION_VIEW, uri );

                startActivity( intent );
                finish();
                break;

            case DIALOG_DATA_ERROR:
                finish();
                break;
        }
    };

	/**
	 * 네트워크 리스너
	 */
	public CustomAsyncListener networkListener = new CustomAsyncListener() {

		@Override
		public void onPost(Context context, int type, int resultCode, JSONObject resultData, CustomAlertDialog dialog) {
			// TODO Auto-generated method stub

			switch ( type ) {
				case NetworkConst.NET_GET_INFORMATION:									// 앱 정보 가져오기

					switch ( resultCode ) {
						case CommonData.API_SUCCESS:
							boolean isAppUpdate = false;


							try {

								String data_yn = resultData.getString(CommonData.JSON_DATA_YN);

								if(data_yn.equals(CommonData.YES)) {

									JSONArray jsonArr = resultData.getJSONArray(CommonData.JSON_GETINFORMATION);
									if (jsonArr.length() > 0) {
										GLog.i("jsonArr = " + jsonArr.length(), "dd");
										JSONObject object = jsonArr.getJSONObject(0);

										String app_version = object.getString(CommonData.JSON_APPVERSION);

										GLog.i("app_ver  = " + pi.versionName.toString()+ "서버 "+app_version, "dd");
										isAppUpdate = Util.isAppUpdate(pi.versionName.toString(), app_version);

										commonData.setAppVersion(app_version);
										commonData.setCmpnyNm(object.getString(CommonData.JSON_CMPNY_NM));
										commonData.setCmpnyArs(object.getString(CommonData.JSON_CMPNY_ARS));
										commonData.setLoginUrl(object.getString(CommonData.JSON_LOGINURL));
										commonData.setApiUrl(object.getString(CommonData.JSON_APIURL));
										commonData.setUpdateUrl(object.getString(CommonData.JSON_UPDATEURL));
										commonData.setCmpnyFileCoursUrl(object.getString(CommonData.JSON_CMPNY_FILE_COURS));


									}
								}else{
									mDialog = new CustomAlertDialog(IntroActivity.this, CustomAlertDialog.TYPE_A);
									mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
									mDialog.setContent(getString(R.string.popup_dialog_data_error_content));
									mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

										@Override
										public void onClick(CustomAlertDialog dialog, Button button) {
											dialog.dismiss();
											finish();
										}
									});
									mDialog.show();

								}


							} catch ( Exception e ) {
								e.printStackTrace();
							}


							if ( pi != null && isAppUpdate ) {	// 자동업데이트가 필요하다면

								CustomAlertDialog updateDialog = new CustomAlertDialog(IntroActivity.this, CustomAlertDialog.TYPE_B);
								updateDialog.setTitle(getString(R.string.popup_dialog_update_title));
								updateDialog.setContent(getString(R.string.popup_dialog_update_content));
								updateDialog.setNegativeButton(getString(R.string.popup_dialog_button_cancel), (dialog1, button) -> {
									autoLoginCheckToView();
									dialog1.dismiss();
								});
								updateDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);
								updateDialog.setId(DIALOG_UPDATE);
								updateDialog.show();
								return;
							}

							autoLoginCheckToView();

							break;
						case CommonData.API_ERROR_SYSTEM_ERROR:	// 시스템 오류
							mDialog = new CustomAlertDialog(IntroActivity.this, CustomAlertDialog.TYPE_A);
							mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
							mDialog.setContent(getString(R.string.popup_dialog_system_error_content));
							mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

								@Override
								public void onClick(CustomAlertDialog dialog, Button button) {
									dialog.dismiss();
									finish();
								}
							});
							mDialog.show();

							break;
						case CommonData.API_ERROR_INPUT_DATA_ERROR:	// 입력 데이터 오류
							mDialog = new CustomAlertDialog(IntroActivity.this, CustomAlertDialog.TYPE_A);
							mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
							mDialog.setContent(getString(R.string.popup_dialog_input_error_content));
							mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

								@Override
								public void onClick(CustomAlertDialog dialog, Button button) {
									dialog.dismiss();
									finish();
								}
							});
							mDialog.show();
							break;

						default:
							if ( mFlag )
								autoLoginCheckToView();

							break;
					}
					break;
				case NetworkConst.NET_JOIN_SNS:							// SNS 로그인
				case NetworkConst.NET_EMAIL_LOGIN:                                  // 일반계정 로그인
				case NetworkConst.NET_LOGIN:	// 로그인
					switch ( resultCode ) {
						case CommonData.API_SUCCESS:
							GLog.i("NET_LOGIN", "dd");

							try {
								String data_yn = resultData.getString(CommonData.JSON_DATA_YN);

								if(data_yn.equals(CommonData.YES)){	// 로그인 성공
									GLog.i("NET_LOGIN SUCCESS", "dd");
//									if(!commonData.getMberId().equals("")){	// 아이디가 있다면 부모
//										commonData.setLoginType(CommonData.LOGIN_TYPE_PARENTS);
//									}else{	// 없다면 자녀
//										commonData.setLoginType(CommonData.LOGIN_TYPE_CHILD);
//									}


									commonData.setLoginType(CommonData.LOGIN_TYPE_PARENTS);

									loginSuccess(IntroActivity.this, resultData,false);

								}else{	// 로그인 실패
									GLog.i("NET_LOGIN FAIL", "dd");
									mDialog	=	new CustomAlertDialog(IntroActivity.this, CustomAlertDialog.TYPE_A);
									mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
									mDialog.setContent(getString(R.string.popup_dialog_login_error));
									mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog1, button) -> {
                                        // 로그인 실패 시 내용을 전부 지운다.
                                        CommonData.getInstance(IntroActivity.this).deletePreferences();
                                        CommonData.getInstance(IntroActivity.this).setGuideCheck(true);

                                        mIntroHandler.postDelayed(mIntroRunnable, CommonData.INTRO_POST_DELAYED);
                                        dialog1.dismiss();
                                    });
									mDialog.show();
								}
							}catch(Exception e){
								GLog.e(e.toString());
								GLog.i("NET_LOGIN Exception", "dd");
								mDialog	=	new CustomAlertDialog(IntroActivity.this, CustomAlertDialog.TYPE_A);
								mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
								mDialog.setContent(getString(R.string.popup_dialog_login_error));
								mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog12, button) -> {
                                    // 로그인 실패 시 내용을 전부 지운다.
                                    CommonData.getInstance(IntroActivity.this).deletePreferences();
                                    CommonData.getInstance(IntroActivity.this).setGuideCheck(true);

                                    mIntroHandler.postDelayed(mIntroRunnable, CommonData.INTRO_POST_DELAYED);
                                    dialog12.dismiss();
                                });
								mDialog.show();
							}

							break;

						default:
							GLog.i("NET_LOGIN default", "dd");
							// 로그인 실패 시 내용을 전부 지운다.
							mDialog	=	new CustomAlertDialog(IntroActivity.this, CustomAlertDialog.TYPE_A);
							mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
							mDialog.setContent(getString(R.string.popup_dialog_login_error));
							mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog13, button) -> {
                                // 로그인 실패 시 내용을 전부 지운다.
                                CommonData.getInstance(IntroActivity.this).deletePreferences();
                                CommonData.getInstance(IntroActivity.this).setGuideCheck(true);

                                mIntroHandler.postDelayed(mIntroRunnable, CommonData.INTRO_POST_DELAYED);
                                dialog13.dismiss();
                            });
							mDialog.show();
							break;
					}
					break;
			}
			hideProgress();
		}

		@Override
		public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
			// TODO Auto-generated method stub
			hideProgress();
			dialog.show();
		}

		@Override
		public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
			// TODO Auto-generated method stub
			// 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
			hideProgress();
			dialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);
			dialog.setId(DIALOG_DATA_ERROR);
			dialog.show();

		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		switch(requestCode) {
			case CommonData.PERMISSION_REQUEST_NECESSARY_PERMS:
				if (PermissionUtils.canAccessLocation(this)) {	// 폰, GPS 권한을 획득했다면
					requestGetInfo();
				}
				else {
					GLog.i("권한 획득 거부 or 취소", "dd");
					mDialog = new CustomAlertDialog(this, CustomAlertDialog.TYPE_A);
					mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
					mDialog.setContent(getString(R.string.popup_dialog_permission_content));
					mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), (dialog, button) -> {
                        mDialog.dismiss();
                        finish();
                    });
					mDialog.show();
				}
				break;
			default:
				GLog.i("onRequestPermissionsResult default", "dd");
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				break;
		}
	}

}
