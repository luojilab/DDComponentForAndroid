package com.luojilab.share.runalone;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.luojilab.component.componentlib.router.ui.AbsDispatcherActivity;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.component.componentlib.service.JsonService;
import com.luojilab.componentservice.share.bean.Author;

import java.util.Arrays;
import java.util.List;

/**
 * 独立测试模块时可以这样做
 */
public class DispatcherActivity extends AbsDispatcherActivity {

    private static final String TAG = "DispatcherActivity";
/*
    * use ddcompo://www.luojilab.com/compodemo?target=share/shareBook?bookName=Dummy to display
    *
    * */

    private static final List<String> transferHost = Arrays.asList(
            "www.luojilab.com",
            "www.luojilab.cn"
    );

    @Override
    protected void onBeforeHandle() {
        Log.d(TAG, "onBeforeHandle");
    }

    @Override
    protected boolean needTransferUri(Uri uri) {
        if (uri == null)
            return false;

        String host = uri.getHost();
        return transferHost.contains(host);
    }

    @Override
    protected Uri transferUri(Uri uri) {
        String target = uri.getQueryParameter("target");
        if (!TextUtils.isEmpty(target))
            return Uri.parse("ddcompo://" + target);
        return uri;
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
