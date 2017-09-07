package com.mrzhang.component.componentlib.router;

import com.mrzhang.component.componentlib.applicationlike.IApplicationLike;

import java.util.HashMap;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class Router {

    private HashMap<String, Object> services = new HashMap<>();

    private static volatile Router instance;

    private Router() {
    }

    public static Router getInstance() {
        if (instance == null) {
            synchronized (Router.class) {
                if (instance == null) {
                    instance = new Router();
                }
            }
        }
        return instance;
    }

    public synchronized void addService(String serviceName, Object serviceImpl) {
        if (serviceName == null || serviceImpl == null) {
            return;
        }
        services.put(serviceName, serviceImpl);
    }

    public synchronized Object getService(String serviceName) {
        if (serviceName == null) {
            return null;
        }
        return services.get(serviceName);
    }

    public synchronized void removeService(String serviceName) {
        if (serviceName == null) {
            return;
        }
        services.remove(serviceName);
    }

    public static void registerComponent(String classname) {
        try {
            Class clazz = Class.forName(classname);
            IApplicationLike applicationLike = (IApplicationLike) clazz.newInstance();
            applicationLike.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterComponent(String classname) {
        try {
            Class clazz = Class.forName(classname);
            IApplicationLike applicationLike = (IApplicationLike) clazz.newInstance();
            applicationLike.onStop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
