package com.ljsw.router.facade;

import java.util.Map;

/**
 * <p><b>Package:</b> com.ljsw.router.facade </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> UiRouterMapperUtils </p>
 * <p><b>Description:</b> used to fetch mapper from generated XXLoader class for XX </p>
 * Created by leobert on 2017/9/19.
 */

public class UiRouterMapperUtils {
    public static void fetchRouteForMe(Class clz, Map<String, Class> mapper) {
        if (clz == null)
            throw new IllegalArgumentException("give me your component-uirouter class");
        if (mapper == null)
            throw new IllegalArgumentException("mapper cannot be null!");

        String name = clz.getName() + "Loader";
        try {
            Class util = Class.forName(name);
            IUiRouterLoader instance = (IUiRouterLoader) util.newInstance();
            instance.addTo(mapper);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
