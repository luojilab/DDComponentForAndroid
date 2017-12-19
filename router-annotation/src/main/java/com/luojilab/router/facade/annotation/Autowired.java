package com.luojilab.router.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Package:</b> com.luojilab.router.facade.annotation </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Autowired </p>
 * <p><b>Description:</b> used to auto-inject dependency </p>
 * Created by leobert on 2017/9/18.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface Autowired {
    /**
     * @return param's name or service name.
     */
    String name() default "";

    /**
     * <em>primitive java type check will be ignore</em>
     * check the result of DI, if inject failed, the value of
     * the field will be null, if required, burst crash
     *
     * @return true for required,false otherwise
     */
    boolean required() default false;

    /**
     * @return feild description
     */
    String desc() default "none desc.";
}
