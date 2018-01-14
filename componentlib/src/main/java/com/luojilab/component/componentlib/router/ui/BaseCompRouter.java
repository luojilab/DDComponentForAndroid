package com.luojilab.component.componentlib.router.ui;

/**
 * Created by mrzhang on 2017/12/19.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.luojilab.component.componentlib.exceptions.UiRouterException;
import com.luojilab.component.componentlib.service.AutowiredService;
import com.luojilab.component.componentlib.utils.UriUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseCompRouter implements IComponentRouter {
    protected Map<String, Class> routeMapper = new HashMap<>();

    protected Map<Class, Map<String, Integer>> paramsMapper = new HashMap<>();

    protected boolean hasInitMap = false;

    protected abstract String getHost();

    protected void initMap() {
        hasInitMap = true;
    }

    public boolean preCondition(String url, Bundle bundle) throws UiRouterException {
        return this.preCondition(Uri.parse(url),bundle);
    }

    public boolean preCondition(Uri uri, Bundle bundle) throws UiRouterException {
        if (!hasInitMap) {
            initMap();
        }
        if (uri == null) {
            return false;
        }
        String host = uri.getHost();
        if (!getHost().equals(host)) {
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

            AutowiredService.Factory.getSingletonImpl().preCondition(target, bundle);
            return true;
        }

        return false;
    }

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
        if (!hasInitMap) {
            initMap();
        }
        if (uri == null || context == null) {
            return true;
        }
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (!getHost().equals(host)) {
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
            if (requestCode > 0 && context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
                return true;
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyUri(Uri uri) {
        if (UIRouter.getLogger().isMonitorMode()) { // monitor log for developer
            UIRouter.getLogger().monitor("verify for: " + UriUtils.toSafeString(uri) +
                    " in " + getClass().getSimpleName() + " ;host is: " + getHost());
        }

        if (uri == null)
            return false;
        String host = uri.getHost();
        if (!getHost().equals(host)) {
            return false;
        }
        if (!hasInitMap) {
            initMap();
        }
        List<String> pathSegments = uri.getPathSegments();
        String path = "/" + TextUtils.join("/", pathSegments);
        return routeMapper.containsKey(path);
    }

}

