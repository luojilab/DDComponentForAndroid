package com.ljsw.component.di.serviceimpl;

import android.util.LruCache;

import com.mrzhang.component.componentlib.router.facade.ISyringe;
import com.mrzhang.componentservice.di.AutowiredService;

import java.util.ArrayList;
import java.util.List;

import static com.mrzhang.component.componentlib.router.RouterConstants.SUFFIX_AUTOWIRED;

/**
 * <p><b>Package:</b> com.ljsw.component.di.serviceimpl </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> DepInjectServiceImpl </p>
 * <p><b>Description:</b> implement of {@link AutowiredService},used to fetch
 * data from bundles in the intent</p>
 * Created by leobert on 2017/9/18.
 */

public class AutowiredServiceImpl implements AutowiredService {

    private LruCache<String, ISyringe> classCache = new LruCache<>(50);
    private List<String> blackList = new ArrayList<>();


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
            }
        } catch (Exception ex) {
            blackList.add(className);    // This instance need not autowired.
        }
    }
}
