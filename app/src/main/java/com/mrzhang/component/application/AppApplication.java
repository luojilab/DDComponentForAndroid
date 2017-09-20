package com.mrzhang.component.application;

import android.app.Application;

import com.ljsw.component.json.serviceimpl.JsonServiceImpl;
import com.mrzhang.component.componentlib.router.Router;
import com.mrzhang.componentservice.json.JsonService;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //如果isRegisterCompoAuto为false，则需要通过反射加载组件
//        Router.registerComponent("com.mrzhang.reader.applike.ReaderAppLike");
//        Router.registerComponent("com.mrzhang.share.applike.ShareApplike");

        // register jsonService
        Router.getInstance().addService(JsonService.class.getSimpleName(), new JsonServiceImpl());
    }


}
