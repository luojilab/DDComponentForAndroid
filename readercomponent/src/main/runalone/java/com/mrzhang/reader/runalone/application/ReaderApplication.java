package com.mrzhang.reader.runalone.application;

import android.app.Application;

import com.mrzhang.component.componentlib.router.Router;

/**
 * Created by mrzhang on 2017/6/20.
 */

public class ReaderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //如果isRegisterCompoAuto为false，则需要通过反射加载组件
        Router.registerComponent("com.mrzhang.share.applike.ShareApplike");
    }

}
