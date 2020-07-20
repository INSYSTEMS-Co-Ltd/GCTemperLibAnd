package com.greencross.gctemperlib;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.greencross.gctemperlib.util.GLog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.common.CustomASyncClient;
import com.greencross.gctemperlib.common.NetworkConst;


/**
 */
public class RCApplication extends MultiDexApplication {

    private static volatile RCApplication instance = null;
    private static volatile Activity currentActivity = null;

    public static String deviceToken = "";
    public static float mScale;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GLog.i("onCreate", "dd");

        instance = this;
        String refreshedToken = "";

        try{
            FirebaseMessaging.getInstance().subscribeToTopic("gngcare");
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            System.out.println(refreshedToken);
            GLog.d(refreshedToken);
        }catch (Exception e){
            e.printStackTrace();
        }

        initImageLoader(getApplicationContext());                   // 이미지로더 Context 설정

        CommonData.getInstance(getApplicationContext());    // commondata setting
        NetworkConst.getInstance().setContext(getApplicationContext()); //  NetworkConst setting
        NetworkConst.getInstance().init();  // 앱 실행시 개발 or 리얼서버용 설정

//        Typekit.getInstance().addCustom1(Typekit.createFromAsset(this, "HIMaumR.ttf"));
//        Typekit.getInstance().addCustom2(Typekit.createFromAsset(this, "HIGothicB.ttf"));
//        Typekit.getInstance().addCustom3(Typekit.createFromAsset(this, "HIGothicM.ttf"));
//        Typekit.getInstance().addCustom4(Typekit.createFromAsset(this, "HIGothicL.ttf"));
//        Typekit.getInstance().addCustom5(Typekit.createFromAsset(this, "HIMaumL.ttf"));


        new CustomASyncClient();


        mScale = getResources().getDisplayMetrics().density;
        if(refreshedToken != null)
            deviceToken = refreshedToken;
    }


    /**
     * 이미지 로더 init
     * @param context
     */
    public void initImageLoader(Context context) {

        int memoryCacheSize;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory limit
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                        //.denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPoolSize(5)
                        //.imageDownloader(secureLoader)
                        //.enableLogging() // Not necessary in common
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }



    public static Activity getCurrentActivity() {
        GLog.d("++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        RCApplication.currentActivity = currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static RCApplication getCooingApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }
}
