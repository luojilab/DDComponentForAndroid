package com.mrzhang.component.componentlib.router;

import android.support.annotation.NonNull;

/**
 * <p><b>Package:</b> com.mrzhang.component.componentlib.router </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Path </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/23.
 */

public final class UiActivityUri {

    private final String host;

    private final String path;

    private String queryString;

    public UiActivityUri(@NonNull String host, @NonNull String path) {
        this.host = host;
        this.path = path;
    }

    public UiActivityUri(@NonNull Address address) {
        this(address.getHost(),address.getPath());
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
