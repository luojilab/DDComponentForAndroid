package com.luojilab.component.componentlib.log;

import com.luojilab.component.componentlib.log.impl.DefaultLogger;

/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.log </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> ILogger </p>
 * <p><b>Description:</b> logger </p>
 * Created by leobert on 11/01/2018.
 */

public interface ILogger {
    String defaultTag = "[DD-Compo]";

    ILogger logger = new DefaultLogger(defaultTag);

    void showLog(boolean isShowLog);

    void showStackTrace(boolean isShowStackTrace);

    void showMonitor(boolean isShowMonitor);

    void debug(String tag, String message);

    void info(String tag, String message);

    void warning(String tag, String message);

    void error(String tag, String message);

    void monitor(String message);

    boolean isMonitorMode();

    String getDefaultTag();

    void setDefaultTag(String defaultTag);

}
