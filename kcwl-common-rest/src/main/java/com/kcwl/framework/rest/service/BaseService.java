package com.kcwl.framework.rest.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcwl.ddd.interfaces.dto.PageInfoDTO;
import com.kcwl.framework.utils.BeanMapUtil;
import com.kcwl.framework.utils.CodeStyleUtil;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ckwl
 */
public class BaseService {

    private static ConcurrentHashMap<Class, List<String>> fieldCache = new ConcurrentHashMap<>();

    protected IPage getPageInfo(Map<String, Object> params) {
        int curPageNo = BeanMapUtil.parseInteger(params, "curPagerNo", PageInfoDTO.FIRST_PAGE_NO);
        int pageSize = BeanMapUtil.parseInteger(params, "pageSize", PageInfoDTO.DEFAULT_PAGE_SIZE);
        return new Page<>(curPageNo, pageSize);
    }

/*    protected IPage getPageInfo(PageQueryCondition pageQueryCondition) {
        return new Page<>(pageQueryCondition.getCurPagerNo(), pageQueryCondition.getPageSize());
    }*/

    protected QueryWrapper createQueryWrapper(Map<String, Object> params, Class entityClass) {
        QueryWrapper wrapper = new QueryWrapper();
        List<String> fieldNames = getEntityFieldNames(entityClass);
        for ( String fieldName : fieldNames ) {
            Object val = params.get(fieldName);
            if ( val != null ) {
                wrapper.eq(true, CodeStyleUtil.humpToLine(fieldName), val);
            }
        }
        return wrapper;
    }

    @SneakyThrows
    protected QueryWrapper createQueryWrapper(Object entity) {
        QueryWrapper wrapper = new QueryWrapper();
        List<Field> fieldList = getNoneStaticFields(entity);
        for (Field field : fieldList ) {
            field.setAccessible(true);
            Object val = field.get(entity);
            if ( val != null ) {
                wrapper.eq(true, CodeStyleUtil.humpToLine(field.getName()), val);
            }
        }
        return wrapper;
    }

    private List<Field> getNoneStaticFields(Object entity) {
        List<Field> fieldList = new ArrayList<Field>();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    private List<String> getEntityFieldNames(Class entityClass) {
        List<String> fieldNames = fieldCache.get(entityClass);
        if ( fieldNames == null ) {
            fieldNames = new ArrayList<String>();
            Class clazz = entityClass;
            while ( clazz != null ) {
                Field[] fields = clazz.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fieldNames.add(fields[i].getName());
                }
                clazz = clazz.getSuperclass();
            }
            fieldCache.put(entityClass, fieldNames);
        }
        return fieldNames;
    }
}
