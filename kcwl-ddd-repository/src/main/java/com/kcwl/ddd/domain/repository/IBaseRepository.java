package com.kcwl.ddd.domain.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kcwl.ddd.domain.entity.BaseEntity;
import com.kcwl.ddd.domain.entity.KcPage;
import com.kcwl.framework.utils.KcBeanConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author ckwl
 */
public interface IBaseRepository<T extends BasePO> extends IService<T> {

    default boolean saveEntity(BaseEntity entity){
        T po = KcBeanConverter.toBean(entity, getPoClass());
        boolean result = save(po);
        if ( result ) {
            entity.setId(po.getId());
        }
        return result;
    }

    default Class<T> getPoClass() {
        Type[] types =  ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments();
        return (Class<T>)types[1];
    }

    default KcPage getPage(IPage iPage) {
        return new KcPage(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), iPage.getRecords());
    }

    default KcPage getPage(IPage iPage, Class entityClass) {
        return new KcPage(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), KcBeanConverter.toList(iPage.getRecords(), entityClass));
    }

    default KcPage getPage(long current, long size, long total, List list) {
        return new KcPage(total, current, size, list);
    }
}
