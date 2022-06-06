package com.kcwl.framework.grayscale.utils;


/**
 * @author: renyp
 * @description: 针对异步请求，存储上游请求灰度标记
 * @date: created in 16:44 2021/9/18
 * @modify by:
 */
public class GrayMarkContextHolder {

    private static ThreadLocal<String> context = new ThreadLocal<>();

    public static String get() {
        return context.get();
    }

    public static void set(String value) {

        context.set(value);
    }

    public static void remove(){
        context.remove();
    }
}
