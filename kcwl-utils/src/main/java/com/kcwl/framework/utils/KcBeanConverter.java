package com.kcwl.framework.utils;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KcBeanConverter {

    /**
     *
     * @param source
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T toBean(Object source, Class<T> tClass) {
        return (source!=null)? BeanUtil.copyProperties(source, tClass):null;
    }

    public static <T> T toBean(Object source, Class<T> tClass, String... ignoreProperties) {
        return (source != null) ? BeanUtil.copyProperties(source, tClass, ignoreProperties) : null;
    }

    public static <T> List<T> toList(List sourceList, Class<T> tClass) {
        List<T> list = null;
        if ( sourceList != null ) {
            list = new ArrayList();
            for (Object val : sourceList) {
                list.add(BeanUtil.copyProperties(val, tClass));
            }
        }
        return list;
    }


    /**
     * Bean类型转Map
     * @param bean
     * @param map
     * @param ignoreNullField
     */
    public static void toHashMap(Object bean, HashMap<String, Object> map, boolean ignoreNullField) {
        BeanMap beanMap = BeanMap.create(bean);
        for (Object key : beanMap.keySet()) {
            Object value = beanMap.get(key);
            if ( isNull(value) && ignoreNullField )  {
                continue;
            }
            map.put(key.toString(), value);
        }
    }

    /**
     * @param source 源map
     * @param target 目标map
     * @param ignoreNullField
     */
    public static void copyToMap(Map source, Map target, boolean ignoreNullField) {
        for (Map.Entry entry : (Iterable<Map.Entry>) source.entrySet()) {
            Object value = entry.getValue();
            if (isNull(value) && ignoreNullField) {
                continue;
            }
            target.put(entry.getKey().toString(), value);
        }
    }

    private static boolean isNull(Object value) {
        return (value == null) || StringUtil.isNullOrEmpty(value.toString());
    }
}
