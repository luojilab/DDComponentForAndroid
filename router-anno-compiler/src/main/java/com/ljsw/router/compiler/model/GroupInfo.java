package com.ljsw.router.compiler.model;

import javax.lang.model.type.TypeMirror;

/**
 * <p><b>Package:</b> com.ljsw.router.compiler.model </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> HostInfo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/25.
 */

public class GroupInfo {
    private String host;
    private String outPutPath;
    private String interfacePath;

    public GroupInfo(String host, String outPutPath, String interfacePath) {
        this.host = host;
        this.outPutPath = outPutPath;
        this.interfacePath = interfacePath;
    }

    public String getHost() {
        return host;
    }

    public String getOutPutPath() {
        return outPutPath;
    }

    public String getInterfacePath() {
        return interfacePath;
    }
}
