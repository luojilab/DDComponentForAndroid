package com.luojilab.share.kotlin.applike

import com.luojilab.component.componentlib.applicationlike.IApplicationLike
import com.luojilab.component.componentlib.router.ui.UIRouter

/**
 * Created by mrzhang on 2018/1/3.
 */
class KotlinApplike : IApplicationLike {

    val uiRouter = UIRouter.getInstance()

    override fun onCreate() {
        uiRouter.registerUI("kotlin")
    }

    override fun onStop() {
        uiRouter.unregisterUI("kotlin")
    }
}