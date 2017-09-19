package com.ljsw.router.compiler.utils;

/**
 * <p><b>Package:</b> com.ljsw.router.compiler.utils </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> Constants </p>
 * <p><b>Description:</b> Constants used by apt </p>
 * Created by leobert on 2017/9/18.
 */

public interface Constants {

    String ANNO_FACADE_PKG = "com.ljsw.router.facade";


    ///////////////////////////////////////////////////////////////////////////
    // Options of processor
    ///////////////////////////////////////////////////////////////////////////
    String KEY_MODULE_NAME = "ModuleName";

    String ANNOTATION_TYPE_ROUTE = ANNO_FACADE_PKG + ".annotation.Route";
    String ANNOTATION_TYPE_AUTOWIRED = ANNO_FACADE_PKG + ".annotation.Autowired";

    String PREFIX_OF_LOGGER = "[Router-Anno-Compiler]-- ";


    // System interface
    String ACTIVITY = "android.app.Activity";
    String FRAGMENT = "android.app.Fragment";
    String FRAGMENT_V4 = "android.support.v4.app.Fragment";
    String SERVICE = "android.app.Service";
    String PARCELABLE = "android.os.Parcelable";

    // Java type
    String LANG = "java.lang";
    String BYTE = LANG + ".Byte";
    String SHORT = LANG + ".Short";
    String INTEGER = LANG + ".Integer";
    String LONG = LANG + ".Long";
    String FLOAT = LANG + ".Float";
    String DOUBEL = LANG + ".Double";
    String BOOLEAN = LANG + ".Boolean";
    String STRING = LANG + ".String";

//    String TEMPLATE_PACKAGE = ".template";

//    /**
//     * see at {@link com.mrzhang.component.componentlib.router.facade.ISyringe}
//     */
    String ISYRINGE = "com.mrzhang.component.componentlib.router.facade.ISyringe";

    String JSON_SERVICE = "com.mrzhang.componentservice.json.JsonService";
    
}
