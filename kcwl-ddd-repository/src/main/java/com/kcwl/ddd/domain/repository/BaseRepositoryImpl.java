package com.kcwl.ddd.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

public class BaseRepositoryImpl<M extends BaseMapper<T>, T> extends ServiceImpl<BaseMapper<T>, T> {
}
