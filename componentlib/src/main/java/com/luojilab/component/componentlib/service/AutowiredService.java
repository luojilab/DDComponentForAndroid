package com.luojilab.component.componentlib.service;

import android.os.Bundle;

import com.luojilab.component.componentlib.exceptions.ParamException;

/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.di </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> AutowiredService </p>
 * <p><b>Description:</b> Autowired Dependency inject </p>
 * Created by leobert on 2017/9/18.
 */

public interface AutowiredService {
    boolean THROW_CONFIG = true;

    /**
     * Autowired core.
     *
     * @param instance the instance who need autowired.
     */
    void autowire(Object instance);

    void preCondition(Class targetActivityClz, Bundle params) throws ParamException;


    class Factory {
        private static AutowiredService autowiredServiceImpl;

        public static AutowiredService getSingletonImpl() {
            if (autowiredServiceImpl == null) {
                autowiredServiceImpl = new AutowiredServiceImpl();
            }
            return autowiredServiceImpl;
        }
    }
}
