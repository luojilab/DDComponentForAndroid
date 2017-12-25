package com.luojilab.share.applike;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.share.serviceimpl.ShareInterceptor;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class ShareApplike implements IApplicationLike {


    @Override
    public void onCreate() {
        ShareInterceptor.isRegister = true;
    }

    @Override
    public void onStop() {
        ShareInterceptor.isRegister = false;
    }
}
