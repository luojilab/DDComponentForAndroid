package com.luojilab.router.compiler.utils;

import com.luojilab.router.compiler.model.MethodInfo;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * <p><b>Package:</b> com.luojilab.router.compiler.utils </p>
 * <p><b>Project:</b> DDComponentForAndroid </p>
 * <p><b>Classname:</b> AnnoUtils </p>
 * <p><b>Description:</b> AnnoUtils </p>
 * Created by leobert on 2017/9/25.
 */

public class AnnoUtils {
    public static MethodInfo generateMethodInfo(ExecutableElement methodElement) {
        //modifiers
        ArrayList<Modifier> methodModifiers = new ArrayList<>();
        methodModifiers.add(Modifier.PUBLIC);
        //name
        String methodName = getSimpleName(methodElement);
        //params
        List<VariableElement> methodParams = new ArrayList<>();
        for (VariableElement typeParameterElement : methodElement.getParameters()) {
            methodParams.add(typeParameterElement);
        }
        //return type
        TypeMirror methodReturnType = methodElement.getReturnType();
        return new MethodInfo().setMethodName(methodName)
                .setMethodModifiers(methodModifiers)
                .setMethodParameters(methodParams)
                .setMethodReturnType(methodReturnType);
    }

    public static String getSimpleName(Element element) {
        return element.getSimpleName().toString();
    }

    public static ParameterSpec generateMethodParameterSpec(TypeMirror typeMirror,
                                                            String paramName) {
        TypeName tn =
                ParameterizedTypeName.get(typeMirror);

        return ParameterSpec.builder(tn, paramName).build();
    }


}
