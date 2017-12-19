/*
 * MIT License
 *
 * Copyright (c) 2017 leobert-lan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.luojilab.router.compiler.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Description: Helper class to record method info needed by Processor
 */
public class MethodInfo {
    private String methodName;
    private List<Modifier> methodModifiers;
    private List<VariableElement> methodParameters;
    private TypeMirror methodReturnType;

    public MethodInfo() {
    }

    public MethodInfo setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public MethodInfo setMethodModifiers(List<Modifier> methodModifiers) {
        this.methodModifiers = methodModifiers;
        return this;
    }

    public MethodInfo setMethodParameters(List<VariableElement> methodParameters) {
        this.methodParameters = methodParameters;
        return this;
    }

    public MethodInfo setMethodReturnType(TypeMirror methodReturnType) {
        this.methodReturnType = methodReturnType;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Modifier> getMethodModifiers() {
        return methodModifiers;
    }

    public List<ParameterSpec> getMethodParameters() {
        List<ParameterSpec> parameterSpecs = new ArrayList<>();
        for (VariableElement variableElement : methodParameters) {
            parameterSpecs.add(ParameterSpec.get(variableElement));
        }
        return parameterSpecs;
    }

    public List<String> getMethodParametersSimple() {
        List<String> params = new ArrayList<>();
        for (VariableElement methodParameter : methodParameters) {
            params.add(methodParameter.getSimpleName().toString());
        }
        return params;
    }

    public TypeName getTypeName() {
        return ClassName.get(methodReturnType);
    }
}
