package com.luojilab.component.componentlib.router.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.router.ui </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> AbsRouteActivity </p>
 * <p><b>Description:</b> It is the basic activity to handle intent call from web.
 * <p>
 * Register your own impl each component as follow in manifest:
 * <p>
 *
 * <pre class="prettyprint">
 *      &lt;activity android:name=".XXXXXXX"&gt;
 *
 *       &lt;intent-filter&gt;
 *          &lt;data
 *              android:host="AAA.BBB.CCC"
 *              android:scheme="http"/&gt;
 *
 *              &lt;action android:name="android.intent.action.VIEW"/&gt;
 *
 *              &lt;category android:name="android.intent.category.DEFAULT"/&gt;
 *              &lt;category android:name="android.intent.category.BROWSABLE"/&gt;
 *      &lt;/intent-filter&gt;
 *
 *      &lt;/activity&gt;
 * </pre>
 *
 *
 * </p>
 * Created by leobert on 14/01/2018.
 */

public abstract class AbsRouteActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleUriRequestFromOuter(getIntent().getData());
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        handleUriRequestFromOuter(getIntent().getData());
    }

    @Override
    protected final void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        handleUriRequestFromOuter(intent.getData());
    }

    private void handleUriRequestFromOuter(Uri uri) {
        onBeforeHandle();

        if (uri == null) {
            onNullUri();
            return;
        }

        VerifyResult verifyResult = UIRouter.getInstance().verifyUri(uri,null,true);

        if (verifyResult.isSuccess()) {
            try {
                UIRouter.getInstance().openUri(this, uri, generateBasicBundle());
            } catch (Exception e) {
                e.printStackTrace();
                onExceptionWhenOpenUri(uri, e);
            }
        } else {
           onVerifyFailed(verifyResult.getThrowable());
        }

        onHandled();
    }


    /**
     * maybe your app is just started by this call, open your splash page?
     */
    protected abstract void onBeforeHandle();

    /**
     * if got an null uri, this will be called
     */
    protected abstract void onNullUri();

//    /**
//     * missing required param on preCondition period;
//     */
//    @Deprecated
//    protected abstract void onParamException(ParamException e);
//
//    /**
//     * no one matched for the uri,may uri error or component is not mounted
//     */
//    @Deprecated
//    protected abstract void onNonMatchedException(UiRouterException.NonMatchedException e);

    protected abstract void onVerifyFailed(@Nullable Throwable throwable);

    /**
     * get exception when handle openUri
     */
    protected abstract void onExceptionWhenOpenUri(Uri uri, Exception e);

    /**
     * maybe finish this activity is better
     */
    protected abstract void onHandled();

    /**
     * override this if you have some basic params need to deliver.
     *
     * @return an Empty Bundle
     */
    protected Bundle generateBasicBundle() {
        return new Bundle();
    }
}
