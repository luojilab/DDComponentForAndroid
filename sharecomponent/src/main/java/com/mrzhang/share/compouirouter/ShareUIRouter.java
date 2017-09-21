package com.mrzhang.share.compouirouter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.ljsw.router.facade.UiRouterMapperUtils;
import com.ljsw.router.facade.annotation.Router;
import com.mrzhang.component.componentlib.router.ui.IComponentRouter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrzhang on 2017/6/20.
 */
@Router(classPath = ShareUIRouter.class)
public class ShareUIRouter implements IComponentRouter {

    private static final String SCHEME = "componentdemo";

    private Map<String,Class> activitiesRouterMap = new HashMap<>();

    private static ShareUIRouter instance = new ShareUIRouter();

    private ShareUIRouter() {
        UiRouterMapperUtils.fetchRouteForMe(getClass(), activitiesRouterMap);
    }

    public static ShareUIRouter getInstance() {
        return instance;
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle) {
        if (TextUtils.isEmpty(url) || context == null) {
            return true;
        }
        return openUri(context, Uri.parse(url), bundle);
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle) {
        if (uri == null || context == null) {
            return true;
        }
        String host = uri.getHost();
        if (activitiesRouterMap.containsKey(host)) {
            Class target = activitiesRouterMap.get(host);
            Intent intent = new Intent(context, target);
            intent.putExtras(bundle == null ? new Bundle() : bundle);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyUri(Uri uri) {
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (SCHEME.equals(scheme)) {
            return activitiesRouterMap.containsKey(host);
        }
        return false;
    }
}
