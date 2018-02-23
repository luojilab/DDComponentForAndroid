package com.luojilab.component.componentlib.service;


import java.util.List;

/**
 * <p><b>Package:</b> com.luojilab.component.json </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> JsonService </p>
 * <p><b>Description:</b> APIs provided by the JsonComponent.
 * <em>you can implement it via fast-json,gson,jackson etc.</em>
 * <p>
 * currently only basic functions contains!
 * </p>
 * Created by leobert on 2017/9/18.
 */

public interface JsonService {

    <T> T parseObject(String text, Class<T> clazz);

    <T> List<T> parseArray(String text, Class<T> clazz);

    String toJsonString(Object instance);

    class Factory {
        private static JsonService jsonService;

        public static JsonService getSingletonImpl() {
            if (jsonService == null) {
                synchronized (Factory.class) {
                    if (jsonService == null)
                        jsonService = new JsonServiceImpl();
                }
            }
            return jsonService;
        }
    }
}
