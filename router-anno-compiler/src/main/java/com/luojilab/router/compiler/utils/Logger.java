package com.luojilab.router.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * <p><b>Package:</b> com.luojilab.router.compiler.utils </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Logger </p>
 * <p><b>Description:</b> logger for apt </p>
 * Created by leobert on 2017/9/18.
 */

public class Logger {
    private Messager msg;

    public Logger(Messager messager) {
        msg = messager;
    }

    /**
     * Print info log.
     */
    public void info(CharSequence info) {
        if (isNotEmpty(info)) {
            msg.printMessage(Diagnostic.Kind.NOTE, Constants.PREFIX_OF_LOGGER + info);
        }
    }

    public void error(CharSequence error) {
        if (isNotEmpty(error)) {
            msg.printMessage(Diagnostic.Kind.ERROR, Constants.PREFIX_OF_LOGGER + "An exception is encountered, [" + error + "]");
        }
    }

    public void error(Throwable error) {
        if (null != error) {
            msg.printMessage(Diagnostic.Kind.ERROR, Constants.PREFIX_OF_LOGGER + "An exception is encountered, [" + error.getMessage() + "]" + "\n" + formatStackTrace(error.getStackTrace()));
        }
    }

    public void warning(CharSequence warning) {
        if (isNotEmpty(warning)) {
            msg.printMessage(Diagnostic.Kind.WARNING, Constants.PREFIX_OF_LOGGER + warning);
        }
    }

    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private  static  boolean isNotEmpty(final CharSequence cs) {
        boolean isEmpty =  cs == null || cs.length() == 0;
        return !isEmpty;
    }
}
