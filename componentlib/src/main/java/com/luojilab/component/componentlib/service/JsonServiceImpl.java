package com.luojilab.component.componentlib.service;

/**
 * Created by mrzhang on 2017/12/14.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * <p><b>Package:</b> com.luojilab.component.json.serviceimpl </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> JsonServiceImpl </p>
 * <p><b>Description:</b> implement of {@link JsonService}, in this,
 * base on fast-json
 * </p>
 * Created by leobert on 2017/9/18.
 */

class JsonServiceImpl implements JsonService {
    @Override
    public <T> T parseObject(String text, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(text, clazz);
    }

    @Override
    public <T> List<T> parseArray(String text, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(text, new TypeToken<List<T>>() {
        }.getType());
    }

    @Override
    public String toJsonString(Object instance) {
        Gson gson = new Gson();
        return gson.toJson(instance);
    }
}