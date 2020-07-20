package com.greencross.gctemperlib.greencare.database.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.greencross.gctemperlib.greencare.database.DBHelper;
import com.greencross.gctemperlib.greencare.util.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Created by mrsohn on 2017. 4. 19..
 */

public class DBBackupManager {

    private static final String TAG = DBBackupManager.class.getSimpleName();

    public void importDB(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();


            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName()+ File.separator
                        + "//databases//" + DBHelper.DB_NAME;

                String backupDBPath = DBHelper.DB_NAME; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Logger.i(TAG, "DBPath="+currentDBPath);
                Logger.i(TAG, "backupDB path="+backupDB.getParent());
                Logger.i(TAG, "currentDB path="+currentDB.getParent());

                Toast.makeText(context, "Import Successful!",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "sdcard is not used", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Import Failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // DB가 있나 체크하기
    public static boolean isCheckDB(Context context){
//        String filePath = "/data/data/" + context.getPackageName() + "/databases/" + DB_NAME;
        File file = new File(DBHelper.DB_PATH);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * sqlite 식사 정보 들어있는 db 최초 집어 넣기
     * @param context
     */
    public void importDBAssets(Context context) {
        Log.d(TAG, "sqlite importDBAssets="+DBHelper.DB_PATH);
        File dbPath = new File(DBHelper.DB_PATH);
        File dbFile = new File(DBHelper.DB_PATH + DBHelper.DB_NAME);
        Logger.i(TAG, "sqlite.exists()="+dbFile.exists());
        if (dbFile.exists()) {
            // 파일이 있는 경우 종료
            Log.e(TAG, "sqlite 디비 파일 존재 함. " + dbFile.getPath());
            return;
        }

        if (dbPath.exists()) {
            dbPath.mkdirs();
        }

        try {
//            String currentDBPath = "/data/data/" + context.getPackageName()+ "/databases/";// + DBHelper.DB_NAME;
            AssetManager am = context.getAssets();

            InputStream is = am.open(DBHelper.DB_NAME);
            BufferedInputStream bis = new BufferedInputStream(is);

            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            File dir = new File(DBHelper.DB_PATH);
            if (dir.exists() == false)
                dir.mkdirs();


            try {
                dbFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fos = new FileOutputStream(dbFile);
            bos = new BufferedOutputStream(fos);

            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();

            fos.close();
            bos.close();
            is.close();
            bis.close();
            Log.d(TAG, "sqlite 디비 저장 성공:"+dbFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportDB(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            Logger.i(TAG, "sd.canWrite()="+sd.canWrite());
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName()
                        + "//databases//"+ File.separator + DBHelper.DB_NAME;
                String backupDBPath = DBHelper.DB_NAME; // From SD directory.
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Logger.i(TAG, "backupDB path="+backupDB.getParent());
                Logger.i(TAG, "currentDB path="+currentDB.getParent());

                Toast.makeText(context, "Backup Successful!",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Backup Failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}