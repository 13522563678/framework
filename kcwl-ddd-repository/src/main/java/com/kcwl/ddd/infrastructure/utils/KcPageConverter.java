package com.kcwl.ddd.infrastructure.utils;

import com.kcwl.ddd.domain.entity.KcPage;
import com.kcwl.ddd.interfaces.dto.PageInfoDTO;
import com.kcwl.framework.utils.KcBeanConverter;

import java.util.List;

public class KcPageConverter {
    public static <T> PageInfoDTO<T> toPageInfoDTO(KcPage kcPage, Class<T> dtoClass) {
        List<T> list = KcBeanConverter.toList(kcPage.getList(), dtoClass);
        return new PageInfoDTO(kcPage.getRowsCount(), kcPage.getCurPagerNo(), kcPage.getPageSize(), list);
    }
}
