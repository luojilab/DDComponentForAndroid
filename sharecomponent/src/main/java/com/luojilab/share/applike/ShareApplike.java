package com.luojilab.share.applike;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.share.ShareService;
import com.luojilab.share.serviceimpl.ShareServiceImpl;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class ShareApplike implements IApplicationLike {
    Router router = Router.getInstance();
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("share");
        router.addService(ShareService.class.getSimpleName(), new ShareServiceImpl());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("share");
        router.removeService(ShareService.class.getSimpleName());
    }
}
