package com.greencross.gctemperlib.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import com.greencross.gctemperlib.R;


/**
 * Created by jihoon on 2016-03-21.
 * 이미지 로딩 속도 개선을 위한 클래스
 * 서버로부터 받는 이미지는 이 클래스에서 처리한다.
 * @since 0, 1
 */
public class CustomImageLoader {
    public static DisplayImageOptions options;
    public static DisplayImageOptions options_main_new;
    public static DisplayImageOptions optionsFood;
    public static DisplayImageOptions optionsOrign;

    /**
     * 프로파일에 사용할 이미지 다운로드 생성자
     */
    public static void setOptionsProfile()
    {
//        options = new DisplayImageOptions.Builder()
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .cacheOnDisc(true)
//                .cacheInMemory(true)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
////                .showStubImage(R.drawable.img_profile)
//                .showImageOnLoading(R.drawable.main19)
//                .build();
    }

    /**
     * 프로파일에 사용할 이미지 다운로드 생성자
     */
    public static void setOptionsProfileMain()
    {
//        options = new DisplayImageOptions.Builder()
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .cacheOnDisc(true)
//                .cacheInMemory(true)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
////                .showStubImage(R.drawable.img_profile)
//                .showImageOnLoading(R.drawable.main19)
//                .build();
    }


    /**
     * 프로파일에 사용할 이미지 다운로드 생성자
     */
    public static void setOptionsProfileMain_new(int height)
    {
//        options_main_new = new DisplayImageOptions.Builder()
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .cacheOnDisc(true)
//                .cacheInMemory(true)
//                .displayer(new RoundedBitmapDisplayer(height/2))
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
////                .showStubImage(R.drawable.img_profile)
//                .showImageOnLoading(R.drawable.main19)
//                .build();
    }

    /**
     * 음식사진에 사용할 이미지 다운로드 생성자
     */
    public static void setOptionsFood()
    {
        optionsFood = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .showStubImage(R.drawable.img_profile)
                .showImageOnLoading(R.drawable.background_c1bfb6)
                .build();
    }

    /**
     * 음식사진에 사용할 이미지 다운로드 생성자
     */
    public static void setOptionsDefault()
    {
        optionsFood = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .showStubImage(R.drawable.img_profile)
                .showImageOnLoading(R.drawable.baby_photo_bg)
                .build();
    }

    /**
     * 미사용
     */
    public static void setOptionsOrign()
    {
        optionsOrign = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .build();
    }

    public static void clearCache()
    {
        ImageLoader.getInstance().clearDiscCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

    /**
     * 이미지 가져오기 ( 프로필 사진에서 사용 )
     * @param context
     * @param imageUrl 이미지 URL
     * @param imageView 이미지 View
     */
    public static void displayImage(Context context, String imageUrl, ImageView imageView)
    {
        if ( options == null )
            setOptionsProfile();

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(imageUrl, imageView, options);
    }

    /**
     * 이미지 가져오기 ( 메인화면 프로필 사진에서 사용 )
     * @param context
     * @param imageUrl 이미지 URL
     * @param imageView 이미지 View
     */
    public static void displayImageMain(Context context, String imageUrl, ImageView imageView)
    {
        if ( options == null )
            setOptionsProfileMain();

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(imageUrl, imageView, options);
    }


    public static void displayImageMainNew(Context context, String imageUrl, ImageView imageView, int height)
    {
        if ( options_main_new == null )
            setOptionsProfileMain_new(height);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(imageUrl, imageView, options_main_new);
    }

    /**
     * 이미지 가져오기 ( 먹은이야기 음식사진에서 사용 )
     * @param context
     * @param imageUrl 이미지 URL
     * @param imageView 이미지 View
     */
    public static void displayImageFood(Context context, String imageUrl, ImageView imageView){
        if ( optionsFood == null )
            setOptionsFood();

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(imageUrl, imageView, optionsFood);
    }

    /**
     * 이미지 가져오기 ( 로딩 흰색배경 )
     * @param context
     * @param imageUrl 이미지 URL
     * @param imageView 이미지 View
     */
    public static void displayImageDefault(Context context, String imageUrl, ImageView imageView){
        if ( optionsFood == null )
            setOptionsDefault();

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(imageUrl, imageView, optionsFood);
    }

    /**
     * 이미지 가져오기
     * @param context
     * @param imageUrl 이미지 URL
     * @param imageView 이미지 View
     * @param listener 이미지 callback
     */
    public static void displayImage(Context context, String imageUrl, ImageView imageView, ImageLoadingListener listener)
    {
        if ( options == null )
            setOptionsProfile();

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(imageUrl, imageView, options, listener);

    }

    /**
     * 이미지 가져오기 ( 알파값 허용 )
     * @param context
     * @param imageUrl 이미지 URL
     * @param imageView 이미지 View
     * @param listener 이미지 callback
     */
    public static void displayImageOrign(Context context, String imageUrl, ImageView imageView, ImageLoadingListener listener)
    {
        if ( optionsOrign == null )
            setOptionsOrign();

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(imageUrl, imageView, listener);
        //imageLoader.displayImage(imageUrl, imageView,  optionsOrign);
    }

    public static void loadImage(Context context, String imageUrl, ImageLoadingListener listener)
    {
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.loadImage(imageUrl, listener);
    }

}
