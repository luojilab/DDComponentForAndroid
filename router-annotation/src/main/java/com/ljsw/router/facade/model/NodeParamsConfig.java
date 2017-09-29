package com.ljsw.router.facade.model;

import java.util.HashMap;

/**
 * <p><b>Package:</b> com.ljsw.router.facade.model </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> NodeParamsConfig </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/29.
 */

public class NodeParamsConfig {
    private HashMap<String,Integer> paramsType = new HashMap<>();

    public void add(String name,Integer type) {
        paramsType.put(name, type);
    }

    public Integer getType(String name) {
        return paramsType.get(name);
    }

    public boolean contains(String name) {
        return paramsType.containsKey(name);
    }
}
