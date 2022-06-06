package com.kcwl.framework.cache.impl;

import com.kcwl.framework.cache.ICacheService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ckwl
 */
public class LocalCacheServiceImpl implements ICacheService {

    private Map<String, Object> map = new ConcurrentHashMap<String, Object>();

    @Override
    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public void save(String key, Object obj) {
        map.put(key, obj);
    }

    @Override
    public void save(String key, Object obj, int timeout) {
        map.put(key, obj);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public void expire(String key, int timeout) {
    }

    @Override
    public void increment(String key) {

    }

    @Override
    public void decrement(String key) {

    }

    @Override
    public int incrementAndGet(String key, int timeout){
        return 0;
    }
}
