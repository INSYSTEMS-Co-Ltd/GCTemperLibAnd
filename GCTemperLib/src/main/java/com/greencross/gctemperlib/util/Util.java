package com.greencross.gctemperlib.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.greencross.gctemperlib.push.ShowAlertMsgActivity;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.greencare.util.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by jihoon on 2016-03-21.
 * 유틸 모음 클래스
 * @since 0, 1
 */
public abstract class Util {

    public static int mDisWitdh;
    private static Date mFacebookExpiresDate;

    // 마켓 스토어에 따라 변경해줘야 함.
    public final static int MARKET_ID = 1;

    /**
     * 이메일이 올바른지 확인
     * @param email
     * @return boolean
     */
    public static boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }
    /**
     *  SharedPreference
     * @param context
     * @return boolean
     */
    public static void setSharedPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("hyundai", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreference(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("hyundai", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }



//    private static final String Password_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{6,12}$"; // 숫자, 영문
    private static final String Password_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[^a-zA-Z0-9]+)(?=.*[0-9]+).{8,15}$"; // 숫자, 영문, 특수문자

//    private static final String Password_PATTERN = "^[a-zA-Z0-9!@.#$%^&*?_~]{8,16}$";
//    private static final String Password_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{7,16}$";
    private static final String NAME_PATTERN = "^[가-힣ㄱ-ㅎㅏ-ㅣ+]*$";
    public static final String PHONE_INVALID = "(01[016789])(\\d{3,4})(\\d{4})";

    /**
     * 최신 version 체크 ( 소수점 있는 버전명 전용 )
     * @param localVerName 설치된 버전
     * @param lastVerName 서버 최종 버전
     * @return 현재 설치된 버전이 최종 버전인지 체크한다. ( true - 업데이트 필요, false - 최신 버전 )
     */
    public static boolean isAppUpdate(String localVerName, String lastVerName)
    {
        try {
            int appVer = Integer.parseInt(localVerName.replace(".",""));
            int severVer = Integer.parseInt(lastVerName.replace(".",""));
            if(severVer > appVer)
                return true;
            else
                return false;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 패스워드가 올바른지 확인
     * @param passwd
     * @return boolean
     */
    public static boolean checkPasswd(String passwd) {
        Pattern pattern = Pattern.compile(Password_PATTERN);
        Matcher matcher = pattern.matcher(passwd);
        return matcher.matches();
    }

    /**
     * 문자열 -> 원화 형식으로 변경
     * @param comma 문자열
     * @return
     */
    public static String setComma(String comma){
        int result = Integer.parseInt(comma);
        return new DecimalFormat("#,###").format(result);
    }

    /**
     * 숫자를 2자리 수로 표현
     * @param num 숫자
     * @return
     */
    public static String getTwoDateFormat(int num){
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(num);
    };


    /**
     * 해당 날짜의 1주일 월요일 날짜 구하기
     * @param year 년도
     * @param month 월
     * @param day 일
     * @param type flase = yyyy-MM-dd , ture = yyyyMMdd
     */
    public static String getMonDayWeek(int year , int month , int day , boolean type) {
        SimpleDateFormat date;
        String result;

        if (type) {
             date = new SimpleDateFormat("yyyyMMdd");
        }else {
            date = new SimpleDateFormat("yyyy-MM-dd");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, month + 1);
        calendar.set(Calendar.DATE, day);
        calendar.add(Calendar.DATE, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));

        result = date.format(calendar.getTime());

        return result;
    }

    /**
     * 해당 날짜에 요일 구하기
     * @param context
     * @param date  날짜
     * @param date_format   데이트포멧타입
     * @return 요일
     */
    public static String getSunDayWeek(Context context, String date, String date_format) {

        Calendar calendar = Calendar.getInstance();
        Date weekkDate = getDateFormat(date, date_format);

        int year = weekkDate.getYear() + 1900;
        int month = weekkDate.getMonth() ;
        int day = weekkDate.getDate();

        GLog.i("!!!!!!  --> " + year + " !! " + month + " !! " + day, "dd");
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        String week = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = context.getString(R.string.sun);
                break;
            case 2:
                week = context.getString(R.string.mon);
                break;
            case 3:
                week = context.getString(R.string.tue);
                break;
            case 4:
                week = context.getString(R.string.wen);
                break;
            case 5:
                week = context.getString(R.string.thu);
                break;
            case 6:
                week = context.getString(R.string.fri);
                break;
            case 7:
                week = context.getString(R.string.sat);
                break;
        }


        return week;
    }


    /**
     * 해당 날짜의 1주일 일요일 날짜 구하기
     * @param year 년도
     * @param month 월
     * @param day 일
     * @param type flase = yyyy-MM-dd , ture = yyyyMMdd
     */
    public static String getDayOfTheWeek(int year , int month , int day , boolean type) {
        SimpleDateFormat date;
        String result;

        if (type) {
            date = new SimpleDateFormat("yyyyMMdd");
        }else {
            date = new SimpleDateFormat("yyyy-MM-dd");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, month + 1);
        calendar.set(Calendar.DATE, day);
        calendar.add(Calendar.DATE, Calendar.SUNDAY - calendar.get(Calendar.DAY_OF_WEEK));

        result = date.format(calendar.getTime());

        return result;
    }


    /**
     * 해당 요일의 월~일 구하기
     * @param date 날짜 ex:20160422
     * @return 결과값 ex:20160417 ,20160418 , 20160419 , 20160420 , 20160421 , 20160422 , 20160423
     */
    public static String[] getWeekMonSun(String date){

        Calendar cal = Calendar.getInstance();
        int yy =Integer.parseInt(date.substring(0, 4));
        int mm =Integer.parseInt(date.substring(4, 6))-1;
        int dd =Integer.parseInt(date.substring(6, 8));
        cal.set(yy, mm,dd);
//        }
        String[] arrYMD = new String[7];

        int inYear = cal.get(cal.YEAR);
        int inMonth = cal.get(cal.MONTH);
        int inDay = cal.get(cal.DAY_OF_MONTH);
        int yoil = cal.get(cal.DAY_OF_WEEK); //요일나오게하기(숫자로)
        if(yoil != 1){   //해당요일이 일요일이 아닌경우
            yoil = yoil-2;
        }else{           //해당요일이 일요일인경우
            yoil = 7;
        }
        inDay = inDay-yoil;
        for(int i = 0; i < 7;i++){
            cal.set(inYear, inMonth, inDay+i-1);  //
            String y = Integer.toString(cal.get(cal.YEAR));
            String m = Integer.toString(cal.get(cal.MONTH)+1);
            String d = Integer.toString(cal.get(cal.DAY_OF_MONTH));
            if(m.length() == 1) m = "0" + m;
            if(d.length() == 1) d = "0" + d;

            arrYMD[i] = y+m +d;
            GLog.i("arrYMD[" + i + "] = " + arrYMD[i], "dd");

        }

        return arrYMD;
    }


    /**
     * 두개의 날짜 비교
     * @param yourdate 비교하고싶은 날짜
     * @param dateformat 데이터포멧형식 ex:yyyyMMdd
     * @return 비교하고싶은 날짜와 현재날짜의 비교한 결과값
     */
    public static long getTwoDateCompare(String yourdate , String dateformat) {
        long diffDays = 0;
        long diff = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateformat);
            Date beginDate = formatter.parse(yourdate);
            Date today = new Date();
            //ssshin add 포멧을 맞춰서 처리
            String sToday = formatter.format(today);
            Date endDate = formatter.parse(sToday);


            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            diff = endDate.getTime() - beginDate.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);

        }catch (Exception e){
            GLog.e(e.toString());
        }

        return diffDays;
    }



    /**
     * 해당 날짜의 특수문자 삽입
     * @param date 받을 년도 ex: 20160413
     * @param type 0 = 2016/04/13 , 1 = 2016.04.13 , 2 = 2016년04월13일 , 3 = 2016년 04월 13일 , 4 = 2016-04-13
     */
    public static String getDateSpecialCharacter(Context context , String date , int type) {
        String resultDate = "";

        if (type == 0) {
            resultDate = date.substring(0, 4) + CommonData.STRING_SLASH + date.substring(4, 6) + CommonData.STRING_SLASH + date.substring(6, 8);
        } else if (type == 1){
            resultDate = date.substring(0, 4) + CommonData.STRING_DOT + date.substring(4, 6) + CommonData.STRING_DOT + date.substring(6, 8);
        }else if (type == 2){
            resultDate = date.substring(0, 4) + context.getString(R.string.year) + date.substring(4, 6) + context.getString(R.string.month) + date.substring(6, 8) + context.getString(R.string.day);
        }else if (type ==3){
            resultDate = date.substring(0, 4) + context.getString(R.string.year) + CommonData.STRING_SPACE + date.substring(4, 6) + context.getString(R.string.month) + CommonData.STRING_SPACE + date.substring(6, 8) + context.getString(R.string.day);
        }else {
            resultDate = date.substring(0, 4) + CommonData.STRING_HYPHEN + date.substring(4, 6) + CommonData.STRING_HYPHEN + date.substring(6, 8);
        }
        return resultDate;
    }


    /**
     * Dip -> Pixel 변환
     * @param dp  dip
     * @return int pixel
     */
//    public static int pixelFromDP( Context context, float dp)
//    {
//        int pixel;
//        pixel = (int) (dp * RCApplication.mScale + 0.5f);
////        GLog.i("mScale = " + CooingStarApplication.mScale);
////        GLog.i("pixel = " + pixel);
//        return pixel;
//    }

    /**
     * Pixel -> Dip 변환
     * @param context       context
     * @param pixelValue    pixel
     * @return
     */
//    public static int toDipFromPixel(Context context ,float pixelValue)
//    {
//        int dip= (int) (pixelValue/ RCApplication.mScale);
//        GLog.i("mScale = " + RCApplication.mScale, "dd");
//        GLog.i("dip = " + dip, "dd");
//        return dip;
//    }

    /**
     * 밀도 구하기
     * @param context   context
     * @return
     */
//    public static float getDensity(Context context){
//        return RCApplication.mScale;
//    }

    /**
     * 키보드 내리기
     * @param context
     * @param et
     */
    public static void hideKeyboard(final Context context, final EditText et){
        et.postDelayed(new Runnable() {                // 키보드 내리기
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
            }
        }, 100);
    }

    /**
     * 키보드 띄우기
     * @param context
     * @param et
     */
    public static void showKeyboard(final Context context, final EditText et){
        et.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(et, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }

    /**
     * 브로드캐스트 발송
     * @param context context
     * @return action 브로드캐스트 액션
     */
    public static void sendBroadCast(Context context, String action){
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);

    }

    /**
     * 알파 애니메이션 적용 ( 점점 보이는 애니메이션 )
     * @param view 적용될 뷰
     * @param delay 애니메이션 적용 시간
     * @param bool 뷰 사라지도록 설정 유무 ( true - 사라짐, false - 안사라짐 )
     * @param gone_delay 뷰 사라지는 시간
     */
    public static void setAlphaAni(final View view, int delay, boolean bool, int gone_delay){
        view.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(delay);
        view.setAnimation(animation);

        if(bool){	// 일정 시간이 지나면 사라지도록 설정했다면
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, gone_delay);
        }
    }


    /**
     * 두 날짜의 차이
     * @param nYear1
     * @param nMonth1
     * @param nDate1
     * @param nYear2
     * @param nMonth2
     * @param nDate2
     * @return
     */
    public static int GetDifferenceOfDate ( int nYear1, int nMonth1, int nDate1, int nYear2, int nMonth2, int nDate2 ){
        Calendar cal = Calendar.getInstance ( );
        int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;

        if ( nYear1 > nYear2 ){
            for ( int i = nYear2; i < nYear1; i++ ) {
                cal.set ( i, 12, 0 );
                nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
            }
            nTotalDate1 += nDiffOfYear;
        }
        else if ( nYear1 < nYear2 ){
            for ( int i = nYear1; i < nYear2; i++ ){
                cal.set ( i, 12, 0 );
                nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
            }
            nTotalDate2 += nDiffOfYear;
        }

        cal.set ( nYear1, nMonth1-1, nDate1 );
        nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
        nTotalDate1 += nDiffOfDay;

        cal.set ( nYear2, nMonth2-1, nDate2 );
        nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
        nTotalDate2 += nDiffOfDay;

        return nTotalDate1-nTotalDate2;
    }

    /**
     * yyyy-MM-dd 형식의 문자열을 Date 형태로 변환
     * @param date 날짜 문자열
     * @param date_format 패턴
     * @return
     */
    public static Date getDateFormat(String date, String date_format){
        SimpleDateFormat mFormat = new SimpleDateFormat(date_format);
        Date mDate = null;
        try {
            mDate = mFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mDate;
    }

    /**
     * 한국 현재시간 가져오기
     * @return 한국 현재시간을 문자열로 돌려준다.
     */
    public static String getKorDateFormat(){
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(tz);
        return df.format(date) ;

    }

    /** hsh
     * @return 현재시간을 불러온다
     */
    public static String getNowYYYYDateFormat(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(date) ;
    }
    /** hsh
     * @return 현재시간을 불러온다
     */
    public static String getNowDateFormat(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date) ;
    }

    /**
     * 팝업 Activity 활성화시 애니메이션
     * @param activity activity
     */
    public static void PopupAnimationStart(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.none);
    }

    /**
     * 팝업 Activity 종료시 애니메이션
     * @param activity activity
     */
    public static void PopupAnimationEnd(Activity activity){
        activity.overridePendingTransition(0, R.anim.slide_in_down);
    }

    /**
     * 뒤로가기 Activity 활성화시 애니메이션
     * @param activity activity
     */
    public static void BackAnimationStart(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 뒤로가기 Activity 종료시 애니메이션
     * @param activity activity
     */
    public static void BackAnimationEnd(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 이미지뷰 비트맵 자원해제
     * @param iv 이미지뷰
     */
    public static void recycleBitmap(ImageView iv) {
        Drawable d = iv.getDrawable();
        if (d instanceof BitmapDrawable) {
            GLog.i("d instanceof BitmapDrawable", "dd");
            Bitmap b = ((BitmapDrawable)d).getBitmap();
            b.recycle();

            d.setCallback(null);
        } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.

    }

    /**
     * 메모리 해제
     * @param root 상위 view
     */
    public static void recursiveRecycle(View root) {
        if (root == null)
            return;

         root.setBackgroundDrawable(null);		// Deprecated
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)root;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                recursiveRecycle(group.getChildAt(i));
            }

            if (!(root instanceof AdapterView)) {
                group.removeAllViews();
            }
        }

        if (root instanceof ImageView) {
            ((ImageView)root).setImageDrawable(null);
        }
        root = null;

        return;
    }

    /**
     * 해시태그 문자열 추출
     * @param inputStr  전체 문자열
     * @param separator 구분자 ( 문자열을 구분할 수 있는 문자 )
     */
    public static String getHashTag(String inputStr, String separator){

        StringBuffer sb = new StringBuffer();
        StringTokenizer token = new StringTokenizer(inputStr, separator);

        int strIndex = 0;
        while (token.hasMoreElements()) {
            String tokenStr =  (String) token.nextElement();

            int lastIndex = tokenStr.indexOf(" ");
            if(lastIndex != -1){    // 해시태그 문자열에 공백이 있는경우 공백 앞자리까지 해시태그
                tokenStr = tokenStr.substring(0, lastIndex);

                int blankIndex = tokenStr.indexOf(" ");
                if(blankIndex != -1){
                    sb.append(tokenStr.substring(0, blankIndex) + ",");
                }else{
                    sb.append(tokenStr + ",");
                }
            }else{  // 공백이 없는경우 문자열 전체를 해시태그
                sb.append(tokenStr +",");
            }

            strIndex++;
        }

        if(sb.length() > 0){
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();

    }

//    /**
//     * 특정 텍스트 Color Size 변경
//     * @param context 객체
//     * @param view	텍스트뷰
//     * @param fulltext 전체 문구
//     * @param subtext 변경할 문구
//     * @param color 변경할 색상
//     * @param size 폰트 사이즈
//     * @param styleType font style ( 0 - 기본, 1 - bold , 2 - italic )
//     */
//    public static void setTextViewCustom(Context context, TextView view, String fulltext, String subtext, int color, int size, int styleType, boolean isUnderLine) {
//        view.setText(fulltext, TextView.BufferType.SPANNABLE);
//        Spannable str = (Spannable) view.getText();
//        int i = fulltext.indexOf(subtext);
////        int j = fulltext.indexOf(subtext2);
//
//        GLog.i("color = " +color, "dd");
//        GLog.i("subtext = " +subtext, "dd");
//
//        if(color > 0) {
//            str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, color)), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   // 색상
//        }
//        if(size != 0) {  // 사이즈
//            str.setSpan(new AbsoluteSizeSpan(Util.pixelFromDP(context, size)), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////            if(!subtext2.equals("")){   // 두번째 문자가 있다면 적용
////                str.setSpan(new AbsoluteSizeSpan(BleUtil.pixelFromDP(context, size)), j, j + subtext2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////            }
//        }
//        str.setSpan(new StyleSpan(styleType), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);         // font Style
//        if(isUnderLine){
//            str.setSpan(new UnderlineSpan(), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);         // underLine
//        }
//
//    }

//    /**
//     * 텍스트하나에 특정문자 Color 변경
//     * @param cuttext       자를 text
//     * @param puttext       setText 할 곳
//     * @param tagname       시작하는 곳
//     * @param splitname     마지막 자를 곳
//     * @param firstcolor    태그 글짜 색깔
//     * @param secondcolor   일반 글짜 색깔
//     */
//    public static void setParticularColor (String cuttext , TextView puttext  , String tagname , String splitname , String firstcolor , String secondcolor) {
//
//        String[] tag = cuttext.split(splitname);
//        ArrayList<String> tagList = new ArrayList<String>();
//        for (int i = 0; i < tag.length; i++) {
//            boolean tagSharp = tag[i].startsWith(tagname);
//            String first = "<font color=" + firstcolor +">" + tag[i] + "</font>";
//            String second = "<font color=" + secondcolor + ">" + tag[i] + "</font>";
//            if (tagSharp) {
//                tagList.add(first);
//            } else {
//                tagList.add(second);
//            }
//            puttext.setText(Html.fromHtml(tagList.toString().replace(",", "").replace("]", "").replace("[", "")));
//        }
//
//    }

//    public static String checkLastDot(String str , String checkStr){
//        if(str.lastIndexOf(checkStr) == str.length()-1)
//            str = str.substring(0, str.length()-1);
//
//        return str;
//    }

//    /**
//     * 생후 계산
//     * @param
//     * @return
//     */
//    public static String getAfterBirth_New(Context context, int nYear1, int nMonth1, int nDate1, int nYear2, int nMonth2, int nDate2){
//
//        int afterMonth = (int) (((nYear1 - nYear2) * 12)+(nMonth1 - nMonth2)+((nDate1 - nDate2) / 30.4 ));
//        //Logger.i(BleUtil.class.getSimpleName(), "getAfterBirth.day="+days+", day="+days+", afterMonth="+afterMonth);
//        // 생후 XX개월
//        String str = context.getString(R.string.after_birth) +CommonData.STRING_SPACE +afterMonth +context.getString(R.string.dogmonth);
//
//
//        return str;
//
//    }

//    /**
//     * 5년 미만 생후 계산
//     * @param days 일수
//     * @return
//     */
//    public static String getAfterBirth(Context context, int days){
//
//        String str = "";
//
//        int year = 0;
//        int day = 0;
//
//        if(days >= CommonData.YEAR){
//            year = days/CommonData.YEAR;
//        }
//
//        day = days%CommonData.YEAR;
//
////        if(year > 0){
////            str = context.getString(R.string.after_birth) +CommonData.STRING_SPACE +year +context.getString(R.string.year) +CommonData.STRING_SPACE +day +context.getString(R.string.day);
////        }else{
////            str = context.getString(R.string.after_birth) +CommonData.STRING_SPACE +day +context.getString(R.string.day);
////        }
//
//        int afterMonth = (int) ( days/ 30.4 );
//        Logger.i(Util.class.getSimpleName(), "getAfterBirth.day="+days+", day="+days+", afterMonth="+afterMonth);
//        // 생후 XX개월
//        str = context.getString(R.string.after_birth) +CommonData.STRING_SPACE +afterMonth +context.getString(R.string.dogmonth);
//
//
//        return str;
//
//    }

//    /**
//     * 5년 미만 생후 계산
//     * @param days 일수
//     * @return
//     */
//    public static String getFetusBirth(Context context, int days){
//
//        String str = "";
//
//        int month = 0;
//        int day = 0;
//
//        if(days >= CommonData.MONTH){
//            month = days/CommonData.MONTH;
//        }
//
//        day = days%CommonData.MONTH;
//
//        // 0개월도 표시 해달라 요청 2018.04.29 소순상 (굿앤굿 3단계테스트 디펙트 238)
//        str = CommonData.STRING_SPACE +month +context.getString(R.string.dogmonth) +CommonData.STRING_SPACE +day +context.getString(R.string.day);
//
//
//        return str;
//
//    }

//    //ssshin add
//    /**
//     * 남은 개월수 계산
//     * @param days 일수
//     * @return
//     */
//    public static String getFetusBirthNew(Context context, int days){
//
//        String str = "";
//
//        if(days == 280){ //280일은 예정일로 표시
//            str = context.getString(R.string.mother_health_reg_week_cnt2);
//        }else{
//            //개월 수 계산 : (임신 일 수 / 28) 나머지 버림처리 + 1
//            int tempCal = (int)Math.floor(days / 28.0) + 1;
//            str = tempCal + context.getString(R.string.dogmonth);
//        }
//
//        return CommonData.STRING_SPACE + str + CommonData.STRING_SPACE;
//
//    }


    /**
     * xml 파싱
     * @param xml xml 정규식
     * @return    문자열
     * @throws Exception
     */
    public static String parseXml(String xml) throws Exception{

        String jsonStr = "";

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));

        int eventType = parser.getEventType();
        String mTag = "";

        while( eventType != XmlPullParser.END_DOCUMENT){		// 종료 도큐멘트가 아니라면
            switch(eventType){
                case XmlPullParser.START_DOCUMENT:
                case XmlPullParser.END_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    mTag = parser.getName();
//                    GLog.i("XmlPullParser.START_TAG = "+parser.getName().toString());
                    break;
                case XmlPullParser.END_TAG:
                    break;
                case XmlPullParser.TEXT:
//                    GLog.i("XmlPullParser.START_TAG = "+parser.getText());
                    GLog.i("mTag = " +mTag, "dd");
                    if(mTag.equals("string")){  // string tag 값 리턴
                        jsonStr = parser.getText();
                        return jsonStr;
                    }
                    break;
            }
            eventType = parser.next();
        }

        return jsonStr;
    }


    // 해열제 용량 단위 변환 Mg -> CC
    public static double converterMGtoCC(double mg, boolean isIbuPowder){
        try {
            if(isIbuPowder)
                return Double.parseDouble( String.format("%.1f", mg/12.8d));
            else
                return Double.parseDouble( String.format("%.1f", mg/32d));
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    // 아세트아미노팬 최대 허용량 계산
    public static float getMaxReducer_A(String babyWeight){
        try {
            return getVolume(babyWeight,2f,1);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }
    // 이부프로팬 최대 허용량 계산
    public static float getMaxReducer_I(String babyWeight){
        try {
            if(Float.parseFloat(babyWeight) <= 12.5)
                return getVolume(babyWeight,2f,2);
            else if(Float.parseFloat(babyWeight) <= 30)
                return 25f;
            else if(Float.parseFloat(babyWeight) <= 40)
                return 50f;
            else
                return 50f;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    // 0.5 단위 반올림 값
    public static float getVolume(String baby_weight,float multiply, int kind)
    {
        try {
            BigDecimal preNum = new BigDecimal(baby_weight);
            BigDecimal postNum = new BigDecimal(multiply);

            Double a = Math.floor(preNum.multiply(postNum).doubleValue() *2 + 0.5d);

            if(multiply < 1){	// 1보다 작으면 해열제 1회 용량
                if(a/2d >= 16d){	// 18cc 보다 크면 18을 리턴함.
                    return 16f;
                }else{			// 18cc 보다 작으면 계산식 대로 리턴
                    return (float) (a/2f);
                }
            }else if(kind == 1){	// 아니면 이부프로펜 하루 허용치
                if(a/2d >= 100d){
                    return 100f;
                }else{
                    return (float) (a/2f);
                }
            }else{
                if(a/2d >= 75d){
                    return 75f;
                }else{
                    return (float) (a/2f);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    // 위도 경도로 주소 찾기
    public static String FindAddress(Context context, double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    android.util.Log.i("gps", "FindAddress: " + currentLocationAddress);

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress).append("#");
                    bf.append(lat).append("#");
                    bf.append(lng);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bf.toString();
    }

    // 날짜 차 구하기
    public static float subDate(Date prevDate, Date backDate)
    {
        try {
            float subDate = 0f;
            float f_a = prevDate.getHours()+((float)prevDate.getMinutes() / 60f);
            float f_b = backDate.getHours()+((float)backDate.getMinutes() / 60f);
            subDate = f_a - f_b;
            if( subDate < 0)
                subDate = 24f + subDate;

            return subDate;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    // 아기 태어난 일자 게산
    public static int sumDayCount(Date startDate, Date endDate)
    {
        try {
            Calendar cal = Calendar.getInstance ( );
            cal.setTime ( endDate );// 종료일로 설정.

            Calendar cal2 = Calendar.getInstance ( );
            cal2.setTime ( startDate ); // 기준일로 설정.

            int count = 0;
            while ( !cal2.after ( cal ) )
            {
                count++;
                cal2.add ( Calendar.DATE, 1 ); // 다음날로 바뀜
            }
            return count;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return 0;
        }
    }

    public static void setRemedyAlarms(Context m_context, Date mCurDate, int chl_sn){
//        AlarmManager m_Manager = (AlarmManager)m_context.getSystemService(Context.ALARM_SERVICE);
//        GregorianCalendar mCalendar = new GregorianCalendar();
//        mCalendar.setTime(mCurDate);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            m_Manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context, m_context.getResources().getString(R.string.alret_reducer_2h).replace("[name]", MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn));
//        else
//            m_Manager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_reducer_2h).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn));
//
//        mCalendar.add(Calendar.MINUTE, 5);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            m_Manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_over_m).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn,  chl_sn+10000));
//        else
//            m_Manager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_over_m).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn+10000));
//
//        mCalendar.add(Calendar.MINUTE, 10);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            m_Manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_over_m).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn+20000));
//        else
//            m_Manager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_over_m).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn+20000));
    }

    public static void setFeverAlarms(Context m_context, Date mCurDate, int chl_sn){
//        AlarmManager m_Manager = (AlarmManager)m_context.getSystemService(Context.ALARM_SERVICE);
//        GregorianCalendar mCalendar = new GregorianCalendar();
//        mCalendar.setTime(mCurDate);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            m_Manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context, m_context.getResources().getString(R.string.alret_fever_2h).replace("[name]", MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn));
//        else
//            m_Manager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_fever_2h).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn));
//
//        mCalendar.add(Calendar.MINUTE, 5);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            m_Manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_over_m).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn+10000));
//        else
//            m_Manager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_over_m).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn+10000));
//
//        mCalendar.add(Calendar.MINUTE, 10);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            m_Manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_over_m).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn,  chl_sn+20000));
//        else
//            m_Manager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Util.setAlarm(m_context,m_context.getResources().getString(R.string.alret_over_m).replace("[name]",MainActivity.mChildMenuItem.get(MainActivity.mChildChoiceIndex).getmChldrnNm()), chl_sn, chl_sn+20000));
    }

    // 알람 세팅
    public static PendingIntent setAlarm(Context m_context, String msg, int chl_sn, int _id) {
        Intent i = new Intent(m_context.getApplicationContext(), ShowAlertMsgActivity.class);
        i.putExtra("content_msg", msg);
        i.putExtra("chl_sn", chl_sn);

        PendingIntent pi = PendingIntent.getActivity(m_context, _id, i, PendingIntent.FLAG_CANCEL_CURRENT);
        return pi;
    }

    // 알람 취소
    public static void cancelAlarm(Context m_context, int chl_sn){
        AlarmManager m_Manager = (AlarmManager)m_context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(m_context.getApplicationContext(), ShowAlertMsgActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(m_context, chl_sn, i, 0);
        m_Manager.cancel(pIntent);
        pIntent.cancel();
        pIntent = PendingIntent.getActivity(m_context, chl_sn+5000, i, 0);
        m_Manager.cancel(pIntent);
        pIntent.cancel();
        pIntent = PendingIntent.getActivity(m_context, chl_sn+10000, i, 0);
        m_Manager.cancel(pIntent);
        pIntent.cancel();
        pIntent = PendingIntent.getActivity(m_context, chl_sn+20000, i, 0);
        m_Manager.cancel(pIntent);
        pIntent.cancel();
    }
}
