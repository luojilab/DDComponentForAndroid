package com.luojilab.share.serviceimpl;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.luojilab.component.basiclib.MainLooper;

/**
 * Created by mrzhang on 2017/12/25.
 */
@Interceptor(priority = 1, name = "分享组件拦截器")
public class ShareInterceptor implements IInterceptor {

    public static boolean isRegister;
    Context mContext;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (isRegister) {
            callback.onContinue(postcard);
        } else {
            MainLooper.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "分享组件已经卸载", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
