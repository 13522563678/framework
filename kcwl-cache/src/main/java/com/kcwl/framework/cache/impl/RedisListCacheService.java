package com.kcwl.framework.cache.impl;

import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.List;

/**
 * @author ckwl
 */
public class RedisListCacheService extends RedisCacheService {

    public RedisListCacheService(RedisConnectionFactory factory) {
        super(factory);
    }

    /**
     * @param key 缓存的key
     * @param obj 列表元素
     */
    public void addData(String key, Object obj) {
        opsForList().rightPush(key, obj);
    }

    /**
     * @param key list缓存的key
     * @param index list的下标
     * @return 返回list中的对象
     */
    public Object getData(String key, int index){
        return opsForList().index(key, index);
    }

    /**
     * 在原来的list中追加list数据
     * @param key  list缓存的key
     * @param dataList 对象列表
     * @param timeout 缓存数据超时时间
     */
    public void addList(String key, List<Object> dataList, int timeout) {
        for ( Object data : dataList ) {
            addData(key, data);
        }
        if ( timeout > 0 ) {
            expire(key, timeout);
        }
    }

    /**
     * 先清除原来list中的数据，然后添加list数据
     * @param key  list缓存的key
     * @param dataList 对象列表
     * @param timeout 缓存数据超时时间
     */
    public void saveList(String key, List<Object> dataList, int timeout) {
        remove(key);
        addList(key, dataList,timeout);
    }

    /**
     * @param key  list缓存的key
     * @return 返回该key对应的list
     */
    public List getList(String key){
        return opsForList().range(key, 0, -1);
    }
}
