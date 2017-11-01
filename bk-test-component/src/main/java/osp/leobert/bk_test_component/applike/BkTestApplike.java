package osp.leobert.bk_test_component.applike;

import com.mrzhang.component.componentlib.applicationlike.IApplicationLike;
import com.mrzhang.component.componentlib.router.ui.IComponentRouter;
import com.mrzhang.component.componentlib.router.ui.UIRouter;

import osp.leobert.bk_test_component.compouirouter.BkCompoUiRouter;


public class BkTestApplike implements IApplicationLike {

    UIRouter uiRouter = UIRouter.getInstance();
//    ShareUIRouter shareUIRouter = ShareUIRouter.getInstance();
    IComponentRouter demoUiRouter = UIRouter.fetch(BkCompoUiRouter.class);

    @Override
    public void onCreate() {
        uiRouter.registerUI(demoUiRouter);
    }

    @Override
    public void onStop() {
        uiRouter.unregisterUI(demoUiRouter);
    }
}
