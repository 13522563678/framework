package com.kcwl.framework.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author ckwl
 */
public class KcBeanRepository {
    private static KcBeanRepository instance = new KcBeanRepository();
    private Map<String, Object> repository;

    private KcBeanRepository() {
        this.repository = new ConcurrentHashMap<>();
    }
    public static KcBeanRepository getInstance() {
        return instance;
    }
    public void saveBean(String key, Object bean){
        repository.put(key, bean);
    }
    public void saveString(String key, String str){
        repository.put(key, str);
    }
    public void saveLong(String key, Long val){
        repository.put(key, val);
    }
    public void saveInteger(String key, Integer val){
        repository.put(key, val);
    }
    public void saveBoolean(String key, Boolean flag){
        repository.put(key, flag);
    }
    public Object getBean(String key) {
        return repository.get(key);
    }
    public String getString(String key) {
        Object bean = getBean(key);
        if ( bean instanceof  String ) {
            return (String)bean;
        }
        return null;
    }
    public Boolean getBoolean(String key) {
        Object bean = getBean(key);
        if ( bean instanceof  Boolean ) {
            return (Boolean)bean;
        }
        return false;
    }
    public Long getLong(String key) {
        Object bean = getBean(key);
        if ( bean instanceof  Long ) {
            return (Long)bean;
        }
        return null;
    }
    public Integer getInteger(String key) {
        Object bean = getBean(key);
        if ( bean instanceof  Integer ) {
            return (Integer)bean;
        }
        return null;
    }
}
