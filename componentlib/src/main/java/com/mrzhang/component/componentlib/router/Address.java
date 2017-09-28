package com.mrzhang.component.componentlib.router;

/**
 * <p><b>Package:</b> com.mrzhang.component.componentlib.router </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Address </p>
 * <p><b>Description:</b> contains the Uri info of the UiActivity,
 * works as the address of the activity，
 * helps to create a UiActivityUri
 * </p>
 * Created by leobert on 2017/9/28.
 */

public final class Address {
    private final String host;
    private final String path;

    public Address(String host, String path) {
        this.host = host;
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }
}
