package com.ljsw.router.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Package:</b> com.ljsw.router.facade.annotation </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> RouteDef </p>
 * <p><b>Description:</b> used to decline a route node</p>
 * Created by leobert on 2017/9/18.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface RouteNode {
    /**
     * path of one route
     */
    String path();

    /**
     * refers to one group that contains a series of similar or related routers.
     * use common words.
     * e.g.
     * '/user/login' and '/user/register' can be merger into group 'user'
     *
     * we have departed the whole system to different component,thus it is useless
     * to define group for merge router
     *
     * it will be removed when route develop complete.
     *
     * emmm.... 暂时留存该设计
     */
    String group() default "default";


    /**
     * The priority of route.
     */
    int priority() default -1;
}
