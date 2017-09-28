package com.ljsw.router.compiler.model;

import static com.ljsw.router.facade.Utils.checkNullOrEmpty;
import static com.ljsw.router.facade.Utils.isNullOrEmpty;

/**
 * <p><b>Package:</b> com.ljsw.router.compiler.model </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> GroupInfo </p>
 * <p><b>Description:</b> bean to hold info of {@link com.ljsw.router.facade.annotation.Router} </p>
 * Created by leobert on 2017/9/25.
 */

public class GroupInfo {
    private String host;
    private String outPutPath;
    private String interfacePath;
    private String group;
    private String alias;

    public GroupInfo(String host, String group, String alias, String outPutPath, String interfacePath) {
        checkNullOrEmpty(host, "host");
        checkNullOrEmpty(group, "group");
        this.host = host;
        this.group = group;
        this.outPutPath = outPutPath;
        this.interfacePath = interfacePath;
        if (isNullOrEmpty(alias)) {
            this.alias = host + "_" + group;
        } else {
            this.alias = alias;
        }
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

    public String getGroup() {
        return group;
    }

    public String getAlias() {
        return alias;
    }
}
