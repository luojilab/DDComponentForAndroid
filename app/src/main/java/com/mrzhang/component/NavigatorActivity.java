package com.mrzhang.component;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.mrzhang.component.componentlib.router.UiActivityUri;
import com.mrzhang.component.componentlib.router.ui.UIRouter;
import com.mrzhang.component.compouirouter.$$PlaceHolder;

/**
 * <p>
 * created by leobert
 */
public class NavigatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (!UIRouter.getInstance().openUri(this, uri, null)) {
            //所以在sharelink里面加版本信息还是挺重要的，或者万一出幺蛾子，引导去homepage比较优雅
            UIRouter.getInstance().openUri(this,
                    new UiActivityUri($$PlaceHolder.AppCompo_Home.UI_INDEX), null);
        }
        finish();
    }
}
