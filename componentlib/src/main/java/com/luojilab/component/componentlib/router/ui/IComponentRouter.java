package com.luojilab.component.componentlib.router.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * router behaviors for component type
 * Created by mrzhang on 2017/6/20.
 */
public interface IComponentRouter {

    /**
     * 打开一个链接
     *
     * @param url    目标url可以是http 或者 自定义scheme
     * @param bundle 打开目录activity时要传入的参数。建议只传基本类型参数。
     * @return 是否正常打开
     */
    boolean openUri(Context context, String url, Bundle bundle);

    boolean openUri(Context context, Uri uri, Bundle bundle);

    boolean openUri(Context context, String url, Bundle bundle, Integer requestCode);

    boolean openUri(Context context, Uri uri, Bundle bundle, Integer requestCode);

    /**
     * use {@link #verifyUri(Uri, Bundle, boolean)} instead
     * @param uri the uri to be verified
     * @return true if sth accept it
     */
    @Deprecated

    boolean verifyUri(Uri uri);

    @NonNull VerifyResult verifyUri(Uri uri, Bundle bundle, boolean checkParams);
}