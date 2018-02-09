package com.luojilab.component.componentlib.exceptions;

import android.net.Uri;

import com.luojilab.component.componentlib.utils.UriUtils;

/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.exceptions </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> UiRouterException </p>
 * <p><b>Description:</b> none matched node for the target navigation url exception </p>
 * Created by leobert on 11/01/2018.
 */

public abstract class UiRouterException extends Exception {
    public UiRouterException() {
    }

    public UiRouterException(String message) {
        super(message);
    }

    public UiRouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public UiRouterException(Throwable cause) {
        super(cause);
    }

    public static final class NonMatchedException extends UiRouterException {
        public NonMatchedException(String url) {
            super("none matched target for: " + url + "; check if uri error or component is not mounted");
        }

        public NonMatchedException(Uri uri) {
            super("none matched target for: " + UriUtils.toSafeString(uri) + "; check if uri error or component is not mounted");
        }
    }
}
