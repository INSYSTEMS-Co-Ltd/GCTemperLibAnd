package com.greencross.gctemperlib.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.greencross.gctemperlib.greencare.base.value.Define;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.util.cameraUtil.ProviderUtil;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.util.Logger;
//import com.kakao.kakaolink.v2.KakaoLinkResponse;
//import com.kakao.kakaolink.v2.KakaoLinkService;
//import com.kakao.message.template.ContentObject;
//import com.kakao.message.template.FeedTemplate;
//import com.kakao.message.template.LinkObject;
//import com.kakao.network.ErrorResult;
//import com.kakao.network.callback.ResponseCallback;
//import com.kakao.plusfriend.PlusFriendService;
//import com.kakao.util.exception.KakaoException;
//import com.kakao.util.helper.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KakaoLinkUtil {
    private final String TAG = KakaoLinkUtil.class.getSimpleName();

    String Url = "";

    public void sendKakaoMessage(Context context, String title, String imageUrl, String imgWidth, String imgHeight, String descrption, String linkService, String linkParam1, String linkParam2, String btn1title, String btn2title, Boolean applinkMode, String url) {
        createKakaoResponseCallBack(context);


//        FeedTemplate params;
//        String templateId;
//
//        if(!applinkMode) {
//            templateId="14728";
//
//            Map<String, String> templateArgs = new HashMap<String, String>();
//
//            templateArgs.put("imgUrl", imageUrl);
//            templateArgs.put("imgWidth", imgWidth);
//            templateArgs.put("imgHeight", imgHeight);
//            templateArgs.put("title", title);
//            templateArgs.put("description", descrption);
//            templateArgs.put("btTitle", btn1title);
//            templateArgs.put("params", url);
//
//
//
//            KakaoLinkService.getInstance().sendCustom(context, templateId, templateArgs, serverCallbackArgs, callback);
//
//        }else{
//            templateId="14729";
//
//            Map<String, String> templateArgs = new HashMap<String, String>();
//
//            templateArgs.put("imgUrl", imageUrl);
//            templateArgs.put("imgWidth", imgWidth);
//            templateArgs.put("imgHeight", imgHeight);
//            templateArgs.put("title", title);
//            templateArgs.put("description", descrption);
//            templateArgs.put("btTitle", btn1title);
//            templateArgs.put("params", "service=" + linkService + "&param1=" + linkParam1 + "&param2=" + linkParam2);
//
//            KakaoLinkService.getInstance().sendCustom(context, templateId, templateArgs, serverCallbackArgs, callback);
//
//        }

    }


    public void sendLBDMessage(Context context, String imageUrl) {

//        createKakaoResponseCallBack(context);
//        FeedTemplate params = FeedTemplate
//                .newBuilder(ContentObject.newBuilder("",
//                        imageUrl,
//                        //Url,
//                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
//                                .setMobileWebUrl("https://developers.kakao.com").build())
//                        .setDescrption("")
//                        .build())
//                .build();
//
//        KakaoLinkService.getInstance().sendDefault(context, params, serverCallbackArgs, callback);
    }

    private Map<String, String> serverCallbackArgs = getServerCallbackArgs();
//    private ResponseCallback<KakaoLinkResponse> callback;
    private void createKakaoResponseCallBack(final Context context) {
//        callback = new ResponseCallback<KakaoLinkResponse>() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Toast.makeText(context, errorResult.getErrorMessage(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onSuccess(KakaoLinkResponse result) {
////                Toast.makeText(context, "Successfully sent KakaoLink v2 message.", Toast.LENGTH_LONG).show();
//                android.util.Log.i(TAG,  "Successfully sent KakaoLink v2 message.");
//            }
//        };
    }

    private Map<String, String> getServerCallbackArgs() {
        Map<String, String> callbackParameters = new HashMap<>();
        callbackParameters.put("user_id", "1234");
        callbackParameters.put("title", "프로방스 자동차 여행 !@#$%");
        return callbackParameters;
    }

    /**
     * 문자메시지 보내기
     * @param context
     * @param smsText
     */
    public static void sendSMS(Context context, String smsText,String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(intent, 0);

        Log.i("linksms","list: "+resInfos);

        if(resInfos != null || resInfos.size() != 0) {

            for (ResolveInfo info : resInfos) {
                if (info.activityInfo.packageName.toLowerCase().contains("mms") ||
                        info.activityInfo.name.toLowerCase().contains("mms") ||
                        info.activityInfo.packageName.toLowerCase().contains("com.samsung.android.messaging") ||
                        info.activityInfo.name.toLowerCase().contains("com.samsung.android.messaging")) {


                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    intent.putExtra("sms_body", smsText + "\n\n\n" + url);
                    intent.putExtra(Intent.EXTRA_TEXT, smsText+url);

//                    intent.putExtra(Intent.EXTRA_STREAM,  Uri.parse(url));
                    intent.setPackage(info.activityInfo.packageName);
                    break;
                } else {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    intent.putExtra("sms_body", smsText +  "\n\n\n" + url);
                    intent.putExtra(Intent.EXTRA_TEXT, smsText+url);
//                    intent.setPackage(info.activityInfo.packageName);
                }
            }

            Intent openInChooser = Intent.createChooser(intent, "MMS:");
            context.startActivity(openInChooser);
        }
    }

    public static void sendImgSMS(Context context, String smsText,String smsSubText, String imgid,String url) {
        Uri uri = copyFiletoExternalStorage(context, imgid,"sms.png");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");



        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(intent, 0);

        Logger.i("linksms","list: "+resInfos);

        if(resInfos != null || resInfos.size() != 0) {

            for (ResolveInfo info : resInfos) {
                if (info.activityInfo.packageName.toLowerCase().contains("mms") ||
                        info.activityInfo.name.toLowerCase().contains("mms") ||
                        info.activityInfo.packageName.toLowerCase().contains("com.samsung.android.messaging") ||
                        info.activityInfo.name.toLowerCase().contains("com.samsung.android.messaging")) {

                    Logger.i("sms", "URL : " + uri);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra("sms_body", smsText + "\n" + smsSubText + "\n\n" + url);
                    intent.setPackage(info.activityInfo.packageName);
                    break;
                } else {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra("sms_body", smsText + "\n" + smsSubText + "\n\n" + url);
//                    intent.setPackage(info.activityInfo.packageName);
                }
            }


            Intent openInChooser = Intent.createChooser(intent, "MMS:");
            context.startActivity(openInChooser);
        }
    }





    /**
     * 카카오톡 일반 메시지 보내기
     * @param context
     * @param message
     */
    public static void sendText(Context context, String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.kakao.talk");
        context.startActivity(Intent.createChooser(sendIntent, "공유"));
    }

    /**
     * 카카오톡 일반 메시지 보내기
     */
    public static void sendImage(Context context, Uri uri) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("image/png");
        //Uri uri = Uri.parse("android.resource://com.kakao.sdk.link.sample/" + R.drawable.kakaolink_sample_icon);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setPackage("com.kakao.talk");
        context.startActivity(Intent.createChooser(sendIntent, "공유"));
    }

    public void sendMultipleImages(Context context, ArrayList<Uri> uris) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.setType("image/png");
//        ArrayList<Uri> files = new ArrayList<Uri>();
//        Uri uri1 = Uri.parse("android.resource://com.kakao.sdk.link.sample/" + R.drawable.kakaolink_sample_icon);
//        Uri uri2 = Uri.parse("android.resource://com.kakao.sdk.link.sample/" + R.drawable.kakaosdk_splash);
//        files.add(uri1);
//        files.add(uri2);
        sendIntent.setPackage("com.kakao.talk");
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(sendIntent, "공유"));
    }

    public Uri getShareUri(Context context, File file) {
        Uri contentUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider", file);
        return contentUri;
    }

    public String getKeyHash(final Context context) {
//        PackageInfo packageInfo = Utility.getPackageInfo(context, PackageManager.GET_SIGNATURES);
//        if (packageInfo == null)
//            return null;
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
//            } catch (NoSuchAlgorithmException e) {
//                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
        return null;
    }

    public static void ShareContent(Context context, String subject, String text){
        List targetedShareIntents = new ArrayList<>();

        //카카오톡
        Intent KakaotalkIntent = new Intent();
        KakaotalkIntent.setPackage("com.kakao.talk");
        if(KakaotalkIntent != null)
            targetedShareIntents.add(KakaotalkIntent);

//        String phoneNum = "000-0000-0000";
//        Uri uri = Uri.parse("smsto:" + phoneNum);
//        Intent SMS = new Intent(Intent.ACTION_SENDTO, uri);
//        SMS.putExtra("sms_body", text);
//        targetedShareIntents.add(SMS);

        Intent chooser = Intent.createChooser((Intent)targetedShareIntents.remove(0), "타이틀");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
        context.startActivity(chooser);

    }

    public Intent getShareIntent(final Context context, String name, String subject, String text) {
        boolean found = false;

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");

        //intent.setAction();

        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(intent, 0);

        if(resInfos == null || resInfos.size() == 0)
            return null;

        for(ResolveInfo info : resInfos) {
            if(info.activityInfo.packageName.toLowerCase().contains(name) || info.activityInfo.name.toLowerCase().contains(name)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setPackage(info.activityInfo.packageName);
                found = true;
                break;
            }
        }
        if(found)
            return intent;

        return null;
    }

    private static Uri copyFiletoExternalStorage(Context context, String resourceId, String resourceName){
        File mediaDirectory = new File(Define.IMAGE_SAVE_PATH);
        if (mediaDirectory.exists() == false)
            mediaDirectory.mkdirs();

        String pathSDCard = mediaDirectory + "/" + resourceName;

        try {

            InputStream in = context.getAssets().open(resourceId);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ProviderUtil.getOutputMediaFileUri(context, ProviderUtil.getOutputFile(resourceName));
        }else{
            return Uri.fromFile(ProviderUtil.getOutputFile(resourceName));
        }
    }

    /**
     * 카카오플러스친구 Dialog
     * @param context
     */
     public static void kakaoAddFriends(Context context){

         View view = LayoutInflater.from(context).inflate(R.layout.plus_friend_dialog_view,null);
         CDialog dlg = CDialog.showDlg(context, view);
         dlg.setTitle(context.getString(R.string.popup_dialog_a_type_title));
//         dlg.setOkButton(context.getString(R.string.popup_dialog_button_move), new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 try {
//                     PlusFriendService.getInstance().addFriend(context, "_zzzjj");
//                 } catch (KakaoException e) {
//                     // 에러 처리 (앱키 미설정 등등)
//                     Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                 }
//             }
//         });
//         dlg.setNoButton(context.getString(R.string.popup_dialog_button_cancel), new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//             }
//         });
     }

    /**
     * appName 가져오기
     * @param PK,context
     */
    public static String getAppname(String PK,Context context){
        String name ="";
        try {
            name = (String) context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(PK, PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return name;
    }

}
