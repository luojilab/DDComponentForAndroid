package com.mrzhang.component.componentlib.router.ui;

/**
 * Created by mrzhang on 2017/6/20.
 */

public interface IUIRouter extends IComponentRouter {

    int PRIORITY_NORMAL = 0;
    int PRIORITY_LOW = -1000;
    int PRIORITY_HEIGHT = 1000;

    void registerUI(IComponentRouter router, int priority);

    void registerUI(IComponentRouter router);

    void unregisterUI(IComponentRouter router);
}
