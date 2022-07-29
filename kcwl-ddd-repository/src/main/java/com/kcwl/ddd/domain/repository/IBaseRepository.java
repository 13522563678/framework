package com.kcwl.ddd.domain.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kcwl.ddd.domain.entity.BaseEntity;
import com.kcwl.ddd.domain.entity.KcPage;
import com.kcwl.framework.utils.KcBeanConverter;

import java.util.List;

public interface IBaseRepository<T extends BasePO> extends IService<T> {

    default boolean saveEntity(BaseEntity entity){
        T po = KcBeanConverter.toBean(entity, getPoClass());
        boolean result = save(po);
        if ( result ) {
            entity.setId(po.getId());
        }
        return result;
    }

    Class<T> getPoClass();

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
