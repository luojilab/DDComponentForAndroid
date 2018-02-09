package com.luojilab.componentdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.luojilab.component.componentlib.router.ui.IComponentRouter;
import com.luojilab.component.componentlib.router.ui.UIRouter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <p><b>Package:</b> com.luojilab.componentdemo </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> UiRouterTest </p>
 * <p><b>Description:</b> test cases for {@link UIRouter} </p>
 * Created by leobert on 11/01/2018.
 */
public class UiRouterTest {

    private UIRouter uiRouter;

    private static final String TARGET_URI = "https://test/UiRouter/target";

    @Before
    public void setUp() throws Exception {
        uiRouter = UIRouter.getInstance();
//        UIRouter.enableDebug();

        uiRouter.registerUI(new IComponentRouter() {
            @Override
            public boolean openUri(Context context, String url, Bundle bundle) {
                return false;
            }

            @Override
            public boolean openUri(Context context, Uri uri, Bundle bundle) {
                return false;
            }

            @Override
            public boolean openUri(Context context, String url, Bundle bundle, Integer requestCode) {
                return false;
            }

            @Override
            public boolean openUri(Context context, Uri uri, Bundle bundle, Integer requestCode) {
                return false;
            }

            @Override
            public boolean verifyUri(Uri uri) {
                if (uri == null)
                    return false;
                return TARGET_URI.equals(uri.toString());
            }
        });
    }

    @Test
    public void testVerifyUri() {
        Uri[] errorCases = new Uri[]{
                Uri.parse("http://error/1"),
                Uri.EMPTY,
                null
        };

        for (Uri uri : errorCases) {
            Assert.assertEquals(false, uiRouter.verifyUri(uri));
        }

        Assert.assertEquals(true,uiRouter.verifyUri(Uri.parse(TARGET_URI)));
    }
}