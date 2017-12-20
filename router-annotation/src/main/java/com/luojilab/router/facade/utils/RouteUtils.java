package com.luojilab.router.facade.utils;

/**
 * Created by mrzhang on 2017/12/18.
 */

public class RouteUtils {

    private static final String ROUTERIMPL_OUTPUT_PKG = "com.luojilab.gen.router";
    private static final String DOT = ".";
    private static final String UIROUTER = "UiRouter";

    private static final String ROUTERTABLE = "RouterTable";

    public static String fiestCharUpperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    public static String genHostUIRouterClass(String host) {
        String claName = ROUTERIMPL_OUTPUT_PKG + DOT + fiestCharUpperCase(host) + UIROUTER;
        return new String(claName);
    }


    public static String genRouterTable(String host) {
        String claName = "./UIRouterTable/" + fiestCharUpperCase(host) + ROUTERTABLE + ".txt";
        return new String(claName);
    }


}
