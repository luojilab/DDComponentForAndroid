package com.ljsw.router.compiler.model;

import javax.lang.model.type.TypeMirror;

/**
 * <p><b>Package:</b> com.ljsw.router.compiler.model </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> HostInfo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/25.
 */

public class HostInfo {
    private String host;
    private TypeMirror interfaceTypeMirror;

    public HostInfo(String host, TypeMirror interfaceTypeMirror) {
        this.host = host;
        this.interfaceTypeMirror = interfaceTypeMirror;
    }

    public String getHost() {
        return host;
    }


    public TypeMirror getInterfaceTypeMirror() {
        return interfaceTypeMirror;
    }
}
