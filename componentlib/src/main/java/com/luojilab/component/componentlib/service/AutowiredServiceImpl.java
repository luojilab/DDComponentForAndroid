package com.luojilab.component.componentlib.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.luojilab.component.componentlib.exceptions.ParamException;
import com.luojilab.component.componentlib.log.ILogger;
import com.luojilab.component.componentlib.router.ISyringe;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.di.serviceimpl </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> DepInjectServiceImpl </p>
 * <p><b>Description:</b> implement of {@link AutowiredService},used to fetch
 * data from bundles in the intent</p>
 * Created by leobert on 2017/9/18.
 */

public class AutowiredServiceImpl implements AutowiredService {

    private LruCache<String, ISyringe> classCache = new LruCache<>(50);
    private List<String> blackList = new ArrayList<>();

    //attention! make sure this keeps same with the one in AutowiredProcessor
    private static final String SUFFIX_AUTOWIRED = "$$Router$$Autowired";

    @Override
    public void autowire(Object instance) {
        String className = instance.getClass().getName();
        try {
            if (!blackList.contains(className)) {
                ISyringe autowiredHelper = classCache.get(className);
                if (null == autowiredHelper) {  // No cache.
                    autowiredHelper = (ISyringe) Class.forName(instance.getClass().getName() + SUFFIX_AUTOWIRED)
                            .getConstructor().newInstance();
                }
                autowiredHelper.inject(instance);
                classCache.put(className, autowiredHelper);
            } else {
                ILogger.logger.monitor("[autowire] " + className + "is in blacklist, ignore data inject");
            }
        } catch (Exception ex) {
            if (ex instanceof NullPointerException) { // may define custom exception better
                throw new NullPointerException(ex.getMessage());
            }
            ex.printStackTrace();
            blackList.add(className);    // This instance don't need autowired.
        }
    }

    @Override
    public void preCondition(@NonNull Class targetActivityClz, Bundle params) throws ParamException {
        String className = targetActivityClz.getName();

        try {
            ISyringe iSyringe = classCache.get(className);

            if (null == iSyringe) {  // No cache.
                iSyringe = (ISyringe) Class.forName(className + SUFFIX_AUTOWIRED)
                        .getConstructor().newInstance();
            }

            classCache.put(className, iSyringe);
            iSyringe.preCondition(params);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
