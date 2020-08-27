package com.m.k.mvp.utils;

import android.text.TextUtils;
import android.util.Log;


import com.m.k.mvp.BuildConfig;

import java.util.Collection;
import java.util.Locale;


/**
 * adb shell setprop log.tag.{@link #PRIVATE_TAG} LOG_LEVEL
 *  eg: adb shell setprop log.tag.Test d
 * Created by Cherry on 2018/06/07.
 * adb shell log.tag.Test V
 *
 *
 * eg: Logger.v("%s %s %d + %d  = %d,","TAG","Hello: ",5,6,11)
 * will print "TAG Hello: 5 + 6 = 11"
 */

/**
 * 1. 能够控制日志的输出与否
 * 2. 输出这行日志是从哪个类文件的什么方法里面的某一行输出的。
 * 3.不用手动打开日志开关。更具打包的类型自动设置是否输出日志。
 * 4.能够对上线的apk 把log 日志开关再次打开
 */

public class Logger {



    private static final String SIMPLE_PREFIX = "[(%s:%d)#%s()] ->  ";

    private static final String MORE_DETAIL_PREFIX = "at %s.%s(%s:%d)";


    private static final StringBuffer mMessageBuffer = new StringBuffer();
    private static String PRIVATE_TAG = "JianDao";
    private static String PUBLIC_TAG = "JianDao";
    private static boolean mDebugable = BuildConfig.DEBUG;
    boolean b = Log.isLoggable("Test", Log.INFO);
    // Log.isLoggable 接受两个参数，一个是 String 表示 tag, 一个是 int 表示日子级别（V,D,I,W,E）
    public  static boolean DEBUG_V = Log.isLoggable(PRIVATE_TAG, Log.VERBOSE);
    public  static boolean DEBUG_D = Log.isLoggable(PRIVATE_TAG, Log.DEBUG);

    private static boolean DEBUG_MORE_DETAIL_STACK_INFO = false;
    private static int  STACK_TRACE_INDEX = 5;
    private static final String SUFFIX = ".java";


    /**
     * 正式发布产品的时候应该关闭debug 模式
     *
     * @param debug 设置Log 模式，如果true 表示debug模式该模式可以从控制台查看相关log信息，否则不是
     */
    public static void setDebug(boolean debug) {
        mDebugable = debug;
    }



    public static boolean isDebug(){
        return mDebugable;
    }

    public static synchronized void v(String format, Object... args) {

        if(mDebugable){ // false
            log(Log.VERBOSE,buildPrivateMessage(null,format, args));
        }else{
            if(DEBUG_V){
                log(Log.VERBOSE,buildPrivateMessage(null,format, args));

            }
        }

    }

    /**
     * 级别最低，用于输出任意对调试或者开发过程中有帮助的信息,
     *
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter. If there are
     *               more arguments than required by {@code format},
     *               additional arguments are ignored.
     */

    public static synchronized void v(Throwable throwable, String format, Object... args) {

        if(mDebugable){ // false
            log(Log.VERBOSE,buildPrivateMessage(throwable,format, args));
        }else{
            if(DEBUG_V){
                log(Log.VERBOSE,buildPrivateMessage(throwable,format, args));
            }
        }


    }

    public static synchronized void d(String format, Object... args) {

        if(mDebugable){
            log(Log.DEBUG,buildPrivateMessage(null,format, args));
        }else{
            if(DEBUG_D){
                log(Log.DEBUG,buildPrivateMessage(null,format, args));
            }
        }

    }
    public static synchronized void d() {

        if(mDebugable){
            log(Log.DEBUG,buildPrivateMessage(null,null, null));
        }else{
            if(DEBUG_D){
                log(Log.DEBUG,buildPrivateMessage(null,null, null));
            }
        }

    }

    /**
     * 调试级别, 比VERBOSE高，用于输出调试信息
     *
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter. If there are
     *               more arguments than required by {@code format},
     *               additional arguments are ignored.
     */

    public static synchronized void d(Throwable throwable, String format, Object... args) {


        if(mDebugable){
            log(Log.DEBUG,buildPrivateMessage(throwable,format, args));
        }else{
            if(DEBUG_D){
                log(Log.DEBUG,buildPrivateMessage(throwable,format, args));
            }
        }
    }

    /**
     * 用于输出比较重要的信息，系统默认的级别，
     *
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter. If there are
     *               more arguments than required by {@code format},
     *               additional arguments are ignored.
     */
    public static void i(String format, Object... args) {
        Log.i(PUBLIC_TAG, buildPrivateMessage(null,format, args));
    }

    public static void i(Throwable throwable, String format, Object... args) {

        Log.i(PUBLIC_TAG, buildPrivateMessage(null,format, args), throwable);
    }

    public static void w(String format, Object... args) {
        Log.w(PUBLIC_TAG, buildPrivateMessage(null,format, args));

    }

    public static void w(Throwable throwable, String format, Object... args) {
        Log.w(PUBLIC_TAG, buildPrivateMessage(throwable,format, args), throwable);
    }

    public static void e(String format, Object... args) {
        Log.e(PUBLIC_TAG, buildPrivateMessage(null,format, args));
    }

    public static void e(Throwable throwable, String format, Object... args) {
        Log.e(PUBLIC_TAG, buildPrivateMessage(null,format, args), throwable);
    }



    public String formatJson(String json){
        return JsonFormat.format(json);
    }


    private static String getSimpleStackTrace(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX];
        return  String.format(Locale.getDefault(),SIMPLE_PREFIX,targetElement.getFileName(),targetElement.getLineNumber(),targetElement.getMethodName());

    }

    public static String getMoreDetailStackTrace() {
        StackTraceElement traceElements[] = Thread.currentThread().getStackTrace();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Math.min(traceElements.length, 100); i++) {
            StackTraceElement traceElement = traceElements[i];
            builder.append(String.format(Locale.getDefault(), MORE_DETAIL_PREFIX, traceElement.getClassName(),
                    traceElement.getMethodName(), traceElement.getFileName(), traceElement.getLineNumber()));
            builder.append("\n");
        }
        return builder.toString();
    }


    private static String buildPrivateMessage(Throwable throwable, String format, Object... args) {

        mMessageBuffer.delete(0, mMessageBuffer.length()); // 清空
        try {
            mMessageBuffer.append("{ ");
            // 栈帧
            mMessageBuffer.append(getSimpleStackTrace());
            if(!TextUtils.isEmpty(format)){
                mMessageBuffer.append((args == null) ? format : String.format(Locale.CHINA, format, args));
            }
        } catch (Exception e) {
            mMessageBuffer.append("buildPrivateMessage exception " + e.getMessage());
        }

        if(DEBUG_MORE_DETAIL_STACK_INFO){
            mMessageBuffer.append("\n");
            mMessageBuffer.append(getMoreDetailStackTrace());
            mMessageBuffer.append("\n");
        }
        if(throwable != null){

            mMessageBuffer.append(throwable.toString());
            mMessageBuffer.append("\n");
        }
        mMessageBuffer.append(" } \n");


        return mMessageBuffer.toString();
    }

    public static String isNull(Object o) {
        return o == null ? " Null" : "Not Null";
    }

    public static String toStringOb(Object o) {
        return o == null ? " Null" : o.toString();
    }

    public static int getCollectionSize(Collection collection){
        if(collection != null){
            return collection.size();
        }
        else{
            return 0;
        }
    }

    private static  boolean isEnableDebug(){
        if(!mDebugable){
            return false;
        }

        if(!DEBUG_V){
            return false;
        }

        return true;
    }

    private static void log(int level, String mes){

        if(mes.length() > 4000){
            for(int i = 0 ; i < mes.length(); i += 4000 ){ // 9000

                if( i + 4000 < mes.length()){
                    println(level,mes.substring(i,i + 4000));
                }else{
                    println(level,mes.substring(i,mes.length()));
                }

            }
        }else{
            println(level,mes);
        }
    }


    private static void println(int level,String mes){

        switch (level){
            case Log.ERROR:
                Log.e(PRIVATE_TAG,mes);
                break;
            case Log.WARN:
                Log.w(PRIVATE_TAG,mes);
                break;
            case Log.INFO:
                Log.i(PRIVATE_TAG,mes);
                break;
            case Log.DEBUG:
                Log.d(PRIVATE_TAG,mes);
                break;
            case Log.VERBOSE:
                Log.v(PRIVATE_TAG,mes);
                break;
        }
    }
}
