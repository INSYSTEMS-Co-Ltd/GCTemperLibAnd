package com.greencross.gctemperlib.hana.util.cameraUtil;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.hana.chart.value.Define;
import com.greencross.gctemperlib.hana.util.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hyochan on 2016. 9. 30..
 */
public class ProviderUtil {
    private static final String TAG = ProviderUtil.class.getSimpleName();
    public static final String PROVIDER_AUTHORITIES = "com.appmd.hi.gngcare.provider";




//    private static File imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    private static File imageDirectory = new File(Define.IMAGE_SAVE_PATH);
    private static String imageFileName = "";
    private static String outputPath = "";

    public static File getOutputFile(Uri uri) {
        try {
            return getOutputFile(new File(uri.getPath()).getName());
        }catch (Exception e){
            return null;
        }
    }

    public static File getOutputFile(String imageFileName) {
        return new File(imageDirectory, imageFileName);
    }

    public static String getOutputFilePath(Uri uri) {
        return getOutputFile(uri).getAbsolutePath();
    }

    public static String getOutputFilePath(String imageFileName) {
        return getOutputFile(imageFileName).getAbsolutePath();
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        Log.d(TAG, "file:" + file);
//        Log.d(TAG, "BuildConfig.APPLICATION_ID:" + BuildConfig.APPLICATION_ID);
        return FileProvider.getUriForFile(context, context.getString(R.string.file_provider), file);
//        return Uri.fromFile(new File(Define.IMAGE_SAVE_PATH));

    }

//    public static Uri getOutputMediaFileUri(Context context) {
//        try {
//            return getOutputMediaFileUri(context, getOutputMediaFile());
//        } catch (IOException e) {
//            GLog.d(TAG, e.getLocalizedMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static File getOutputMediaFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = timeStamp;
//        File storageDir = new File(imageDirectory, "GreenCare");
//        File noMediaFile = new File(storageDir.getPath() + File.separator + MediaStore.MEDIA_IGNORE_FILENAME);
        File noMediaFile = new File(Define.IMAGE_SAVE_PATH);

        if (imageDirectory.exists() == false) {
            imageDirectory.mkdirs();
            Logger.i(TAG, "Create Storage Directory ::: "+imageDirectory.getPath());
        }

        if (noMediaFile.exists() == false) {
            noMediaFile.createNewFile();
            Logger.i(TAG, "Create Ignore file Create ::: "+ MediaStore.MEDIA_IGNORE_FILENAME);
        }

        File file = File.createTempFile(
                imageFileName,          /* prefix */
                ".jpg",             /* suffix */
                imageDirectory              /* directory */
        );

        outputPath = "file:" + file.getAbsolutePath();
        return file;
    }


    public static File getOutputMediaFile(String fileName) throws IOException {
        imageFileName = fileName;
        if (imageDirectory.exists() == false)
            imageDirectory.mkdirs();

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                imageDirectory      /* directory */
        );

        outputPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
