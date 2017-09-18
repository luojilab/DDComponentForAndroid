package com.mrzhang.componentservice.di;

/**
 * <p><b>Package:</b> com.mrzhang.componentservice.di </p>
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
}
