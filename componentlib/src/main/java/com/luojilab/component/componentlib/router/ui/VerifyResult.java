package com.luojilab.component.componentlib.router.ui;

/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.router.ui </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> VerifyResult </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 23/02/2018.
 */

public class VerifyResult {
    private final boolean isSuccess;

    private Throwable throwable;

    public VerifyResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public VerifyResult(boolean isSuccess, Throwable throwable) {
        this.isSuccess = isSuccess;
        this.throwable = throwable;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
