package com.mrzhang.component;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.mrzhang.component.componentlib.router.ui.UIRouter;

/**
 *
 * <p>
 * created by leobert
 */
public class NavigatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        UIRouter.getInstance().openUri(this,uri,null);
    }
}
