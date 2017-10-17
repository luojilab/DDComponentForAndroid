package com.mrzhang.component.componentlib.router.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by mrzhang on 2017/6/20.
 */

public class UIRouter implements IUIRouter {

    List<IComponentRouter> uiRouters = new ArrayList<IComponentRouter>();
    HashMap<IComponentRouter, Integer> priorities = new HashMap<IComponentRouter, Integer>();

    private static volatile UIRouter instance;

    private UIRouter() {
    }

    public static UIRouter getInstance() {
        if (instance == null) {
            synchronized (UIRouter.class) {
                if (instance == null) {
                    instance = new UIRouter();
                }
            }
        }
        return instance;
    }


    @Override
    public void registerUI(IComponentRouter router, int priority) {
        if (priorities.containsKey(router) && priority == priorities.get(router)) {
            return;
        }
        removeOldUIRouter(router);
        int i = 0;
        for (IComponentRouter temp : uiRouters) {
            Integer tp = priorities.get(temp);
            if (tp == null || tp <= priority) {
                break;
            }
            i++;
        }
        uiRouters.add(i, router);
        priorities.put(router, Integer.valueOf(priority));
    }

    @Override
    public void registerUI(IComponentRouter router) {
        registerUI(router, PRIORITY_NORMAL);
    }

    @Override
    public void unregisterUI(IComponentRouter router) {
        for (int i = 0; i < uiRouters.size(); i++) {
            if (router == uiRouters.get(i)) {
                uiRouters.remove(i);
                priorities.remove(router);
                break;
            }
        }
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle) {
        url = url.trim();
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("://") &&
                    (!url.startsWith("tel:") ||
                            !url.startsWith("smsto:") ||
                            !url.startsWith("file:"))) {
                url = "http://" + url;
            }
            Uri uri = Uri.parse(url);
            return openUri(context, uri, bundle);
        }
        return true;
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle) {
        for (IComponentRouter temp : uiRouters) {
            try {
                if (temp.verifyUri(uri) && temp.openUri(context, uri, bundle)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean verifyUri(Uri uri) {
        for (IComponentRouter temp : uiRouters) {
            if (temp.verifyUri(uri)) {
                return true;
            }
        }
        return false;
    }


    private void removeOldUIRouter(IComponentRouter router) {
        Iterator<IComponentRouter> iterator = uiRouters.iterator();
        while (iterator.hasNext()) {
            IComponentRouter tmp = iterator.next();
            if (tmp == router) {
                iterator.remove();
                priorities.remove(tmp);
            }
        }
    }
}
