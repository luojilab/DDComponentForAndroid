package com.ljsw.component.di;

import com.ljsw.component.di.serviceimpl.AutowiredServiceImpl;

/**
 * <p><b>Package:</b> com.ljsw.component.di </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> AutowiredService </p>
 * <p><b>Description:</b> Autowired Dependency inject </p>
 * Created by leobert on 2017/9/18.
 */

public interface AutowiredService {

    /**
     * Autowired core.
     * @param instance the instance who need autowired.
     */
    void autowire(Object instance);


    class Factory {
        private static Factory instance;

        public static Factory getInstance() {
            if (instance == null) {
                instance = new Factory();
            }
            return instance;
        }

        public AutowiredService create() {
            return new AutowiredServiceImpl();
        }
    }
}
