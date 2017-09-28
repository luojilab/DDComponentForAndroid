package com.ljsw.router.facade;

/**
 * <p><b>Package:</b> com.ljsw.router.facade </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Utils </p>
 * <p><b>Description:</b>utils </p>
 * Created by leobert on 2017/9/28.
 */

public final class Utils {

    public static void checkNull(Object object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " is null, not allowed");
        }
    }

    public static void checkNullOrEmpty(String str, String name) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(name + " is null or empty, not allowed");
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
