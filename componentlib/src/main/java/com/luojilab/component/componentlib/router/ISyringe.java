package com.luojilab.component.componentlib.router;

import android.os.Bundle;

import com.luojilab.component.componentlib.exceptions.ParamException;

/**
 * <p><b>Package:</b> com.luojilab.component.componentlib.di.route </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> ISyringe </p>
 * <p><b>Description:</b> desc the function of Syringe, one that used to inject
 * sth. to a container</p>
 * Created by leobert on 2017/9/18.
 */

public interface ISyringe {
    /**
     * @param self the container itself, members to be inject into have been annotated
     *             with one annotation called Autowired
     */
    void inject(Object self);

    void preCondition(Bundle bundle) throws ParamException;
}
