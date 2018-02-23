package com.luojilab.share.runalone;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.luojilab.component.componentlib.router.ui.AbsRouteActivity;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.component.componentlib.service.JsonService;
import com.luojilab.componentservice.share.bean.Author;

public class DispatcherActivity extends AbsRouteActivity {

    private static final String TAG = "DispatcherActivity";

    @Override
    protected void onBeforeHandle() {
        Log.d(TAG, "onBeforeHandle");
    }

    @Override
    protected void onNullUri() {
        Log.e(TAG, "onNullUri");
        demo();
    }

    @Override
    protected void onVerifyFailed(@Nullable Throwable throwable) {
        Log.e(TAG, "onVerifyFailed", throwable);
        demo();
    }

    @Override
    protected void onExceptionWhenOpenUri(Uri uri, Exception e) {
        Log.e(TAG, e.getMessage());
        demo();
    }

    @Override
    protected void onHandled() {
        finish();
    }

    private void demo() {

        Author author = new Author();
        author.setName("leobert");
        author.setCounty("China");
        UIRouter.getInstance().openUri(this,
                "DDComp://share/shareBook?bookName=Demo&author="
                        + JsonService.Factory.getSingletonImpl().toJsonString(author), null);
    }
}
