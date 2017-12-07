package com.mrzhang.component.componentlib.router;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p><b>Package:</b> com.mrzhang.component.componentlib.router </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Path </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/23.
 */

public final class UiActivityUri {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({Scheme.DUNQ,Scheme.DUNB})
    public @interface SchemeDef {

    }

    public static final class Scheme {
        public static final String DUNQ = "dunp:";

        public static final  String DUNB = "dunb:";
    }

    @SchemeDef
    private String scheme;

    private final String host;

    private final String path;

    private String queryString;

    public UiActivityUri(String host, String path) {
        this.host = host;
        this.path = path;
    }


    @SchemeDef
    public String getScheme() {
        return scheme;
    }

    public void setScheme(@SchemeDef String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryString() {
        return queryString;
    }



    //    public  String getUri() {
//
//    }

}
