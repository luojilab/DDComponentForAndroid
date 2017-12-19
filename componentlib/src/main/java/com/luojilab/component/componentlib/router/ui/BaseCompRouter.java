package com.luojilab.component.componentlib.router.ui;

/**
 * Created by mrzhang on 2017/12/19.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCompRouter implements IComponentRouter {
    protected static Map<String, Class> routeMapper = new HashMap<String, Class>();

    protected static Map<Class, Map<String, Integer>> paramsMapper;

    protected static String HOST = "";

    @Override
    public boolean openUri(Context context, String url, Bundle bundle) {
        if (TextUtils.isEmpty(url) || context == null) {
            return true;
        }
        return openUri(context, Uri.parse(url), bundle, 0);
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle) {
        return openUri(context, uri, bundle, 0);
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle, Integer requestCode) {
        if (TextUtils.isEmpty(url) || context == null) {
            return true;
        }
        return openUri(context, Uri.parse(url), bundle, requestCode);
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle, Integer requestCode) {
        if (uri == null || context == null) {
            return true;
        }
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (!HOST.equals(host)) {
            return false;
        }
        List<String> pathSegments = uri.getPathSegments();
        String path = "/" + TextUtils.join("/", pathSegments);
        if (routeMapper.containsKey(path)) {
            Class target = routeMapper.get(path);
            if (bundle == null) {
                bundle = new Bundle();
            }
            Map<String, String> params = com.luojilab.component.componentlib.utils.UriUtils.parseParams(uri);
            Map<String, Integer> paramsType = paramsMapper.get(target);
            com.luojilab.component.componentlib.utils.UriUtils.setBundleValue(bundle, params, paramsType);
            Intent intent = new Intent(context, target);
            intent.putExtras(bundle);
            if (requestCode > 0 && context instanceof android.app.Activity) {
                ((android.app.Activity) context).startActivityForResult(intent, requestCode);
                return true;
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyUri(Uri uri) {
        String host = uri.getHost();
        List<String> pathSegments = uri.getPathSegments();
        String path = "/" + TextUtils.join("/", pathSegments);
        return HOST.equals(host) && routeMapper.containsKey(path);
    }
}

