package com.luojilab.reader.applike;

import com.luojilab.api.reader.readerbook.ReadBookService;
import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.reader.serviceimpl.ReadBookServiceImpl;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class ReaderAppLike implements IApplicationLike {

    Router router = Router.getInstance();
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("reader");
        router.addService(ReadBookService.class.getSimpleName(), new ReadBookServiceImpl());
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI("reader");
        router.removeService(ReadBookService.class.getSimpleName());
    }
}
