package com.luojilab.component.componentlib.utils;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.luojilab.router.facade.enums.Type;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mrzhang on 2017/8/28.
 * 负责解析URI的参数
 */

public class UriUtils {

    public static HashMap<String, String> parseParams(Uri uri) {
        if (uri == null) {
            return new HashMap<String, String>();
        }
        HashMap<String, String> temp = new HashMap<String, String>();
        Set<String> keys = getQueryParameterNames(uri);
        for (String key : keys) {
            temp.put(key, uri.getQueryParameter(key));
        }
        return temp;
    }

    public static Set<String> getQueryParameterNames(Uri uri) {
        String query = uri.getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<String>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = query.substring(start, separator);
            try {
                names.add(URLDecoder.decode(name, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            start = end + 1;
        } while (start < query.length());

        return Collections.unmodifiableSet(names);
    }


    public static int parseInt(String src) {
        return parseInt(src, 0);
    }

    public static int parseInt(String src, int defaultValue) {
        if (TextUtils.isEmpty(src)) {
            return defaultValue;
        }
        int index = src.indexOf(".");
        if (index > 0) {
            src = src.substring(0, index);
        }
        try {
            return Integer.parseInt(src);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void setBundleValue(Bundle bundle, Map<String, String> params, Map<String, Integer> paramsType) {
        if (paramsType != null && params != null && !paramsType.isEmpty() && !params.isEmpty()) {
            for (Map.Entry<String, Integer> param : paramsType.entrySet()) {
                UriUtils.setBundleValue(bundle, param.getValue(), param.getKey(), params.get(param.getKey()));
            }
        }
    }

    /**
     * @param typeDef type
     * @param key     key
     * @param value   value
     */
    public static void setBundleValue(Bundle bundle, Integer typeDef, String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }

        try {
            if (null != typeDef) {
                if (typeDef == Type.BOOLEAN.ordinal()) {
                    bundle.putBoolean(key, Boolean.parseBoolean(value));
                } else if (typeDef == Type.BYTE.ordinal()) {
                    bundle.putByte(key, Byte.valueOf(value));
                } else if (typeDef == Type.SHORT.ordinal()) {
                    bundle.putShort(key, Short.valueOf(value));
                } else if (typeDef == Type.INT.ordinal()) {
                    bundle.putInt(key, Integer.valueOf(value));
                } else if (typeDef == Type.LONG.ordinal()) {
                    bundle.putLong(key, Long.valueOf(value));
                } else if (typeDef == Type.FLOAT.ordinal()) {
                    bundle.putFloat(key, Float.valueOf(value));
                } else if (typeDef == Type.DOUBLE.ordinal()) {
                    bundle.putDouble(key, Double.valueOf(value));
                } else if (typeDef == Type.STRING.ordinal()) {
                    bundle.putString(key, value);
                } else if (typeDef == Type.PARCELABLE.ordinal()) {
                } else if (typeDef == Type.OBJECT.ordinal()) {
                    bundle.putString(key, value);
                } else {
                    bundle.putString(key, value);
                }
            } else {
                bundle.putString(key, value);
            }
        } catch (Throwable ex) {
        }
    }
}
