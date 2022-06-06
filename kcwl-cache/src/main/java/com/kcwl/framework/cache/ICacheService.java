package com.kcwl.framework.cache;

/**
 * @author ckwl
 */
public interface ICacheService {
    public Object get(String key);
    public void save(String key,  Object obj);
    public void save(String key,  Object obj, int timeout);
    public void remove(String key);
    public void expire(String key,  int timeout);
    public void increment(String key);
    public void decrement(String key);
    public int incrementAndGet(String key, int timeout);
}
