package com.kcwl.ddd.domain.entity;

public class SimpleThreadLocal<T> {

    private ThreadLocal<T> threadLocal = new ThreadLocal<>();

    public SimpleThreadLocal() {
    }

    public T get() {
        return threadLocal.get();
    }

    public void set(T obj) {
        if ( obj != null ) {
            threadLocal.set(obj);
        } else {
            threadLocal.remove();
        }
    }
}
