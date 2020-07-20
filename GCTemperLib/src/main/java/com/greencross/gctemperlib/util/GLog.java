package com.greencross.gctemperlib.util;

import android.os.Debug;

import com.greencross.gctemperlib.common.NetworkConst;


/**
 * Created by jihoon on 2016-03-21.
 * 로그 추출 클래스
 */
public class GLog {

    private static final String 	APP_NAME = "GnGCare";			//최종 릴리즈시 false로
    private static final boolean 	DEBUG_WITH_DETAIL_INFO = true;		//호출 함수, 라인 정보
    private static final boolean	DEBUG_ONLY_CLASS_NAME = true;		//클래스 이름만 추가

    private static final int		STACK_NUMBUER = 3;

    public static void v(String message){
        android.util.Log.v(APP_NAME, message);
        if (NetworkConst.getInstance().isDebug() && message != null){
            String log = logMessage(message);
            android.util.Log.v(APP_NAME, log);
        }
    }

    public static void i(String message, String dd){
        android.util.Log.i(APP_NAME, message);
        if (NetworkConst.getInstance().isDebug() && message != null){
            String log = logMessage(message);
            android.util.Log.i(APP_NAME, log);
        }
    }

    public static void d(String message){
        android.util.Log.d(APP_NAME, message);
        if (NetworkConst.getInstance().isDebug() && message != null){
            String log = logMessage(message);
            android.util.Log.d(APP_NAME, log);
        }
    }

    public static void w(String message){
        if (NetworkConst.getInstance().isDebug() && message != null){
            String log = logMessage(message);
            android.util.Log.w(APP_NAME, log);
        }
    }

    public static void e(String message){
        android.util.Log.e(APP_NAME, message);
        if (NetworkConst.getInstance().isDebug() && message != null){
            String log = logMessage(message);
            android.util.Log.e(APP_NAME, log);
        }
    }
    /**
     * protocol의 로그 표시용.
     * 다른 곳에서는 사용 금지.
     * @param
     *   message   : 로그 정보
     * @return
     */
    public static void p(String message){
        boolean show = true;
        if(show){
            if (NetworkConst.getInstance().isDebug() && message != null){
                String log = logMessage(message);
                android.util.Log.d(APP_NAME, log);
            }
        }
    }

    public static String logMessage(String message){
        String log = null;
        if(DEBUG_WITH_DETAIL_INFO){
            log = getDebugMessage(message);
        }else if(DEBUG_ONLY_CLASS_NAME){
            log = getDebugMessageWithClassName(message);
        }else{
            log = message;
        }
        return log;
    }

    public static void debugNativeHeap(){
        if (NetworkConst.getInstance().isDebug()){
            String tag = "";
            String temp = new Throwable().getStackTrace()[STACK_NUMBUER].getClassName();
            if(temp != null){
                int lastDotPos = temp.lastIndexOf(".");
                tag = temp.substring(lastDotPos + 1);
            }
            String methodName = new Throwable().getStackTrace()[STACK_NUMBUER].getMethodName();
            int lineNumber = new Throwable().getStackTrace()[STACK_NUMBUER].getLineNumber();

            android.util.Log.i(APP_NAME, "[" + tag + "] " + methodName + "()" + "[" + lineNumber +"]" + " >> "
                    + "NativeHeapSize=" + Debug.getNativeHeapSize()
                    + " NativeHeapFreeSize=" + Debug.getNativeHeapFreeSize()
                    + " NativeHeapAllocatedSize()=" + Debug.getNativeHeapAllocatedSize());
        }
    }

    private static String getDebugMessageWithClassName(String message){
        String className = "";
        String temp = new Throwable().getStackTrace()[STACK_NUMBUER].getClassName();
        if(temp != null){
            int lastDotPos = temp.lastIndexOf(".");
            className = temp.substring(lastDotPos + 1);
        }

        String logText = "[" + className +"] " + message;

        return logText;
    }

    private static String getDebugMessage(String message){
        String tag = "";
        String temp = new Throwable().getStackTrace()[STACK_NUMBUER].getClassName();
        if(temp != null){
            int lastDotPos = temp.lastIndexOf(".");
            tag = temp.substring(lastDotPos + 1);
        }
        String methodName = new Throwable().getStackTrace()[STACK_NUMBUER].getMethodName();
        int lineNumber = new Throwable().getStackTrace()[STACK_NUMBUER].getLineNumber();

        String logText = "[" + tag +"] " + methodName+ "()" + "[" + lineNumber +"]" + " >> " + message;

        return logText;
    }

}
