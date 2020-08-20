package com.greencross.gctemperlib.hana.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();
    /**
     * @param name String
     * @return File
     */
    public static File getPublicDownloadFile(String name) {
        return new File(getExternalStoragePublicDirectory(), name);
    }

    /**
     * @return File
     */
    public static File getExternalStoragePublicDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }


    /**
     * 내부 저장소 이미지 파일에서 Bitmap로 변환
     *
     * @return
     */
    public static Bitmap getStorageFileToBitmap(Context context, String fileName) {
        String path = context.getFilesDir().getPath() + File.separator + fileName;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * bitmap를 ByteArrayInputStream로 변환
     * @param bitmap
     * @param format
     * @return
     */
    public static ByteArrayInputStream getBis(Bitmap bitmap, Bitmap.CompressFormat format) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        bitmap.compress(format, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

        return bs;
    }

    /**
     * @param file     File
     * @param fileName String
     * @return boolean
     */
    public static boolean copyFileToExternalStoragePublicDirectory(File file, String fileName) {
        return copyFile(file, getExternalStoragePublicDirectory() + File.separator + fileName);
    }

    /**
     * @param fileName String
     * @param text     String
     * @return boolean
     */
    public static boolean exportTextFileToExternalStoragePublicDirectory(String fileName, String text) {
        File targetFile = getPublicDownloadFile(fileName);

        FileWriter writer = null;
        boolean retVal = false;
        try {
            writer = new FileWriter(targetFile);
            writer.write(text);
            writer.flush();

            retVal = true;
        } catch (IOException e) {
            Logger.e(e);
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (Exception e) {
            }
        }

        return retVal;
    }


    /**
     * @param file             File
     * @param fileWithPathName String
     * @return boolean
     */
    public static boolean copyFile(File file, String fileWithPathName) {
        return copyFile(file, fileWithPathName, 1024);
    }


    /**
     * @param file             File
     * @param fileWithPathName String
     * @param bufsize          int
     * @return boolean
     */
    public static boolean copyFile(File file, String fileWithPathName, int bufsize) {
        boolean result;
        if (file != null && file.exists()) {
            FileInputStream fis = null;
            FileOutputStream newfos = null;

            try {
                fis = new FileInputStream(file);
                newfos = new FileOutputStream(fileWithPathName);

                int readcount = 0;
                byte[] buffer = new byte[bufsize];
                while ((readcount = fis.read(buffer, 0, bufsize)) != -1) {
                    newfos.write(buffer, 0, readcount);
                }

            } catch (Exception e) {
                Logger.e(TAG, "copyFile - " + e.getMessage());
            } finally {
                if (newfos != null)
                    try {
                        newfos.close();
                    } catch (Exception e) {
                    }

                if (fis != null)
                    try {
                        fis.close();
                    } catch (Exception e) {
                    }
            }

            result = true;
        } else {
            result = false;
        }

        return result;
    }


    /**
     * @param file             File
     * @param fileWithPathName fileWithPathName
     */
    public static boolean loadFileAndSavePng(File file, String fileWithPathName) {
        Bitmap bm = null;

        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            int targetHeight = 256;
            int targetWidth = 256;

            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.mark(bufferedInputStream.available());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bufferedInputStream, null, options);

            Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth - targetWidth);

            if (options.outHeight * options.outWidth >= targetHeight * targetWidth) {
                double sampleSize = scaleByHeight
                        ? options.outHeight / targetHeight
                        : options.outWidth / targetWidth;
                options.inSampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
            }

            try {
                bufferedInputStream.reset();
            } catch (IOException e) {
                if (fileInputStream != null) try {
                    fileInputStream.close();
                } catch (Exception esub) {
                }
                if (bufferedInputStream != null) try {
                    bufferedInputStream.close();
                } catch (Exception esub) {
                }

                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
            }

            // Do the actual decoding
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeStream(bufferedInputStream, null, options);
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            if (fileInputStream != null) try {
                fileInputStream.close();
            } catch (Exception e) {
            }
            if (bufferedInputStream != null) try {
                bufferedInputStream.close();
            } catch (Exception e) {
            }
        }

        if (bm == null)
            return false;

        File output = new File(fileWithPathName);

        ByteArrayOutputStream bos = null;
        FileOutputStream newfos = null;

        boolean retVal = false;

        try {
            bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] datas = bos.toByteArray();


            newfos = new FileOutputStream(output);

            for (int i = 0, iMax = datas.length; i < iMax; i += 1024) {
                int ends = Math.min(iMax - i, 1024);

                newfos.write(datas, i, ends);
            }

            retVal = true;
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            if (bos != null)
                try {
                    bos.close();
                } catch (Exception e) {
                }

            if (newfos != null)
                try {
                    newfos.close();
                } catch (Exception e) {
                }
        }

        return retVal;
    }

    public static String getUriToRealpath(Context context, Uri uri) {
        Logger.i(TAG, "getUriToRealpath.uri=" + uri);
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
//        try {
//            String[]  data = { MediaStore.Images.Media.DATA };
//            CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
//            Cursor cursor = loader.loadInBackground();
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            return uri.getPath();
//        }

    }

    /**
     * File Path 를 Uri 로 변환하기 ( Path -> Uri )
     */
    public static Uri getUriFromPath(Context context, String path) {
        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();
        Cursor c = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);
        c.moveToNext();
        int id = c.getInt(c.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;

    }


}
