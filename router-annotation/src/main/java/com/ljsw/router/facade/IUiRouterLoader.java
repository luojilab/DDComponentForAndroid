package com.ljsw.router.facade;

import java.util.Map;

/**
 * <p><b>Package:</b> com.ljsw.router.facade </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> IUiRouterUtil </p>
 * <p><b>Description:</b> used to add route mapper </p>
 * Created by leobert on 2017/9/19.
 */

public interface IUiRouterLoader {
    void addTo(Map<String, Class> atlas);
}
