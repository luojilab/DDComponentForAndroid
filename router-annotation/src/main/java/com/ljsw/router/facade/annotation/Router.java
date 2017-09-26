package com.ljsw.router.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Package:</b> com.ljsw.router.facade.annotation </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Routes </p>
 * <p><b>Description:</b> use to def the router which dispatches requests </p>
 * Created by leobert on 2017/9/19.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Router {
//    /**
//     * will removed, it is too heavy and confusing
//     * @return the interface class annotated with Router
//     */
//    @Deprecated
//    Class classPath();
    String host();
    String group() default "default";
}
