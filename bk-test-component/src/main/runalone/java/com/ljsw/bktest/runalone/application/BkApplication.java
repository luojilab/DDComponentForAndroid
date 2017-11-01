package com.ljsw.bktest.runalone.application;

import android.app.Application;

import butterknife.ButterKnife;

public class BkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(true);
    }

}