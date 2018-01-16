package com.luojilab.component.basicres;

import android.app.Application;
import android.support.annotation.Nullable;

/**
 * Created by mrzhang on 2018/1/16.
 */

public class BaseApplication extends Application {

    private static BaseApplication mAppCotext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppCotext = this;
    }

    @Nullable
    public static Application getAppContext() {
        return mAppCotext;
    }
}
