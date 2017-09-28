package com.ljsw.router.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Package:</b> com.ljsw.router.facade.annotation </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> UiRoutersHolder </p>
 * <p><b>Description:</b> use to tag the class to generate inner classes, referring
 * to Router and containing UiRouteNode</p>
 * Created by leobert on 2017/9/28.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface UiRoutersHolder {
}
