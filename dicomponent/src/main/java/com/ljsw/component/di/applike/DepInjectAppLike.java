package com.ljsw.component.di.applike;

import com.ljsw.component.di.serviceimpl.AutowiredServiceImpl;
import com.mrzhang.component.componentlib.applicationlike.IApplicationLike;
import com.mrzhang.component.componentlib.router.Router;
import com.mrzhang.component.componentlib.router.ui.UIRouter;
import com.mrzhang.componentservice.di.AutowiredService;

/**
 * <p><b>Package:</b> com.ljsw.component.di.applike </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> DepInjectAppLike </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/18.
 */

public class DepInjectAppLike implements IApplicationLike {
    UIRouter uiRouter = UIRouter.getInstance();
    Router router = Router.getInstance();

    //no ui-router for register

    @Override
    public void onCreate() {
        router.addService(AutowiredService.class.getSimpleName(),new AutowiredServiceImpl());
    }

    @Override
    public void onStop() {
        router.removeService(AutowiredService.class.getSimpleName());
    }
}
