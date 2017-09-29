package com.mrzhang.component.application;

import android.app.Application;

import com.mrzhang.component.componentlib.router.ui.UIRouter;
import com.mrzhang.component.compouirouter.AppCompoUiRouter;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UIRouter.getInstance().registerUI(UIRouter.fetch(AppCompoUiRouter.class));

        //如果isRegisterCompoAuto为false，则需要通过反射加载组件
//        Router.registerComponent("com.mrzhang.reader.applike.ReaderAppLike");
//        Router.registerComponent("com.mrzhang.share.applike.ShareApplike");
    }


}
