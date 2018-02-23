package com.luojilab.componentdemo;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.luojilab.component.componentlib.router.ui.AbsRouteActivity;
import com.luojilab.component.componentlib.router.ui.UIRouter;

public class DispatcherActivity extends AbsRouteActivity {
    private static final String TAG = "DispatcherActivity";

    @Override
    protected void onBeforeHandle() {
        Log.d(TAG, "onBeforeHandle");
    }

    @Override
    protected void onNullUri() {
        Log.e(TAG, "onNullUri");
        navigate2MainPage();
    }

    @Override
    protected void onVerifyFailed(@Nullable Throwable throwable) {
        Log.e(TAG, "onVerifyFailed", throwable);
        navigate2MainPage();
    }

    @Override
    protected void onExceptionWhenOpenUri(Uri uri, Exception e) {
        Log.e(TAG, e.getMessage());
        navigate2MainPage();
    }

    @Override
    protected void onHandled() {
        finish();
    }

    private void navigate2MainPage() {
        UIRouter.getInstance().openUri(this, "/main", null);
    }
}
