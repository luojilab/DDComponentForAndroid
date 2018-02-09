package com.luojilab.component.basiclib;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mrzhang on 2018/1/16.
 */

public class ToastManager {

    private ToastManager() {
    }

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }

    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getString(resId), args));
    }

    public static void show(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

}
