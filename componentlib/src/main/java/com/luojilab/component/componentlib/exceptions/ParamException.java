package com.luojilab.component.componentlib.exceptions;

/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.exceptions </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> ParamException </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 12/01/2018.
 */

public final class ParamException extends UiRouterException {
    public ParamException(String paramName) {
        super(paramName + " is required param, but didn't contains in the bundle");
    }
}
