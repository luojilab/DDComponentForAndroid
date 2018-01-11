package com.luojilab.component.componentlib.log.impl;

import android.util.Log;

import com.luojilab.component.componentlib.log.ILogger;

/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.log.impl </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> DefaultLogger </p>
 * <p><b>Description:</b> DefaultLogger </p>
 * Created by leobert on 11/01/2018.
 */

public class DefaultLogger implements ILogger {

    private static boolean isShowLog = false;
    private static boolean isShowStackTrace = false;
    private static boolean isMonitorMode = false;

    private String defaultTag = "[DD-Compo]";

    public void setDefaultTag(String defaultTag) {
        this.defaultTag = defaultTag;
    }

    @Override
    public void showLog(boolean showLog) {
        isShowLog = showLog;
    }

    @Override
    public void showStackTrace(boolean showStackTrace) {
        isShowStackTrace = showStackTrace;
    }

    @Override
    public void showMonitor(boolean showMonitor) {
        isMonitorMode = showMonitor;
    }

    public DefaultLogger() {
    }

    public DefaultLogger(String defaultTag) {
        this.defaultTag = defaultTag;
    }

    @Override
    public void debug(String tag, String message) {
        if (isShowLog) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d( getDefaultTag() + tag, message + getExtInfo(stackTraceElement));
        }
    }

    @Override
    public void info(String tag, String message) {
        if (isShowLog) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i( getDefaultTag() + tag, message + getExtInfo(stackTraceElement));
        }
    }

    @Override
    public void warning(String tag, String message) {
        if (isShowLog) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w( getDefaultTag() + tag, message + getExtInfo(stackTraceElement));
        }
    }

    @Override
    public void error(String tag, String message) {
        if (isShowLog) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e( getDefaultTag() + tag, message + getExtInfo(stackTraceElement));
        }
    }

    @Override
    public void monitor(String message) {
        if (isShowLog && isMonitorMode()) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(defaultTag + "::monitor", message + getExtInfo(stackTraceElement));
        }
    }

    @Override
    public boolean isMonitorMode() {
        return isMonitorMode;
    }

    @Override
    public String getDefaultTag() {
        return defaultTag;
    }

    public static String getExtInfo(StackTraceElement stackTraceElement) {

        String separator = " & ";
        StringBuilder sb = new StringBuilder("[");

        if (isShowStackTrace) {
            String threadName = Thread.currentThread().getName();
            String fileName = stackTraceElement.getFileName();
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            long threadID = Thread.currentThread().getId();
            int lineNumber = stackTraceElement.getLineNumber();

            sb.append("ThreadId=").append(threadID).append(separator);
            sb.append("ThreadName=").append(threadName).append(separator);
            sb.append("FileName=").append(fileName).append(separator);
            sb.append("ClassName=").append(className).append(separator);
            sb.append("MethodName=").append(methodName).append(separator);
            sb.append("LineNumber=").append(lineNumber);
        }

        sb.append(" ] ");
        return sb.toString();
    }
}

