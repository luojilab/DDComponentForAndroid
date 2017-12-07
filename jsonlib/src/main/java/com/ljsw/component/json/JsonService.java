package com.ljsw.component.json;

import com.ljsw.component.json.serviceimpl.JsonServiceImpl;

import java.util.List;

/**
 * <p><b>Package:</b> com.ljsw.component.json </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> JsonService </p>
 * <p><b>Description:</b> APIs provided by the JsonComponent.
 * <em>you can implement it via fast-json,gson,jackson etc.</em>
 *
 * currently only basic functions contains!
 * </p>
 * Created by leobert on 2017/9/18.
 */

public interface JsonService {

    <T> T parseObject(String text, Class<T> clazz);

    <T> List<T> parseArray(String text, Class<T> clazz);

    String toJsonString(Object instance);

    class Factory {
        private static Factory instance;

        public static Factory getInstance() {
            if (instance == null) {
                instance = new Factory();
            }
            return instance;
        }

        public JsonService create() {
            return new JsonServiceImpl();
        }
    }

}
