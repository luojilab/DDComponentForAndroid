package com.mrzhang.componentservice.json;

/**
 * <p><b>Package:</b> com.mrzhang.componentservice.json </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> JsonService </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/18.
 */

public interface JsonService {

    <T> T json2Object(String text, Class<T> clazz);

    String object2Json(Object instance);

}
