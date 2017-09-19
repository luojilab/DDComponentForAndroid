package com.ljsw.component.json.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.mrzhang.componentservice.json.JsonService;

import java.util.List;

/**
 * <p><b>Package:</b> com.ljsw.component.json.serviceimpl </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> JsonServiceImpl </p>
 * <p><b>Description:</b> implement of {@link JsonService}, in this,
 * base on fast-json
 * </p>
 * Created by leobert on 2017/9/18.
 */

public class JsonServiceImpl implements JsonService {
    @Override
    public <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    @Override
    public <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    @Override
    public String toJsonString(Object instance) {
        return JSON.toJSONString(instance);
    }
}
