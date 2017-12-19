package com.luojilab.reader.applike;

import com.luojilab.component.componentlib.applicationlike.IApplicationLike;
import com.luojilab.component.componentlib.router.Router;
import com.luojilab.componentservice.readerbook.ReadBookService;
import com.luojilab.reader.serviceimpl.ReadBookServiceImpl;

/**
 * Created by mrzhang on 2017/6/15.
 */

public class ReaderAppLike implements IApplicationLike {

    Router router = Router.getInstance();

    @Override
    public void onCreate() {
        router.addService(ReadBookService.class.getSimpleName(), new ReadBookServiceImpl());
    }

    @Override
    public void onStop() {
        router.removeService(ReadBookService.class.getSimpleName());
    }
}
