package com.ljsw.component.json.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.mrzhang.componentservice.json.JsonService;

/**
 * <p><b>Package:</b> com.ljsw.component.json.serviceimpl </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> JsonServiceImpl </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/18.
 */

public class JsonServiceImpl implements JsonService {
    @Override
    public <T> T json2Object(String text, Class<T> clazz) {
        return JSON.parseObject(text,clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return JSON.toJSONString(instance);
    }
}
