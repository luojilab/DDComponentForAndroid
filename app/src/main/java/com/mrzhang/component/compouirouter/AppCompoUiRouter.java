package com.mrzhang.component.compouirouter;

import com.ljsw.router.facade.annotation.Router;
import com.mrzhang.component.componentlib.router.ui.IComponentRouter;

/**
 * <p><b>Package:</b> com.mrzhang.component.compouirouter </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> DemoUiRouter </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/9/25.
 */
@Router(host = "AppComponent",group = "home",alias = "AppCompo_Home")
public interface AppCompoUiRouter extends IComponentRouter {
}
