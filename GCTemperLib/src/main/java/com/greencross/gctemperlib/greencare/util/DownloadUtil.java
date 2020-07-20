package com.greencross.gctemperlib.greencare.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by cldlt on 2018-04-10.
 */

public class DownloadUtil {
    private final String TAG = DownloadUtil.class.getSimpleName();

    private long mDownloadReference;
    private DownloadManager mDownloadManager;
    private BroadcastReceiver receiverDownloadComplete;    //다운로드 완료 체크
    private BroadcastReceiver receiverNotificationClicked;    //다운로드 시작 체크

    boolean mIsDownloading = true;
    public void startDownload(final Activity activity, String url, final String fileName, final String Title, final IDownloadReceiver iReceiver) {
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        }

        File files = new File(Environment.getExternalStorageDirectory().getPath()+activity.getFilesDir().getPath()+"/"+fileName);

        if(files.exists()==true){
            iReceiver.success(Environment.getExternalStorageDirectory().getPath()+activity.getFilesDir().getPath()+"/"+fileName);
            return;
        }

        Uri uri = Uri.parse(url);        //data는 파일을 떨궈 주는 uri
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(Title);    //다운로드 완료시 noti에 제목

        request.setVisibleInDownloadsUi(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        //모바일 네트워크와 와이파이일때 가능하도록
        request.setNotificationVisibility( DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //다운로드 완료시 noti에 보여주는것
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/temp", fileName);
        request.setDestinationInExternalPublicDir(activity.getFilesDir().getPath(), fileName);




        //다운로드 경로, 파일명을 적어준다
        mDownloadReference = mDownloadManager.enqueue(request);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);

        receiverNotificationClicked = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String extraId = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                long[] references = intent.getLongArrayExtra(extraId);
                for (long reference : references) {

                }
            }
        };

        activity.registerReceiver(receiverNotificationClicked, filter);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        receiverDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(mDownloadReference == reference){
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(reference);

                    while (mIsDownloading) {
                        Cursor cursor = mDownloadManager.query(query);
                        cursor.moveToFirst();

                        int fileIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);

                        String filePath = cursor.getString(fileIndex);
                        int status = cursor.getInt(columnIndex);
                        int reason = cursor.getInt(columnReason);

                        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            mIsDownloading = false;
                        }

                        final int dl_progress = (int) ((bytes_downloaded * 100L) / bytes_total);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                            mProgressBar.setProgress((int) dl_progress);
                                Log.i(TAG, "download.percent="+dl_progress);
                                if (dl_progress >= 100) {
                                    mIsDownloading = false;
                                }
                            }
                        });

                        cursor.close();

                        switch (status){
                            case DownloadManager.STATUS_SUCCESSFUL :
                                mIsDownloading = false;
//                            Toast.makeText(activity, "다운로드 완료.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Download STATUS_SUCCESSFUL "+reason);
                                Toast.makeText(context, "다운로드 성공 ", Toast.LENGTH_SHORT).show();
                                iReceiver.success(Environment.getExternalStorageDirectory().getPath()+activity.getFilesDir().getPath()+"/"+fileName);
                                break;
                            case DownloadManager.STATUS_PAUSED :
                                mIsDownloading = false;
//                            Toast.makeText(activity, "다운로드 중지 : " + reason, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Download STATUS_PAUSED "+reason);
                                iReceiver.fail();
                                break;
                            case DownloadManager.STATUS_FAILED :
                                mIsDownloading = false;
//                            Toast.makeText(activity, "다운로드 취소 : " + reason, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Download STATUS_FAILED "+reason);
                                iReceiver.fail();
                                break;
                        }
                    }

                }
            }
        };
        activity.registerReceiver(receiverDownloadComplete, intentFilter);
    }

    public interface IDownloadReceiver {
        void success(String filePath);
        void fail();
    }

}
