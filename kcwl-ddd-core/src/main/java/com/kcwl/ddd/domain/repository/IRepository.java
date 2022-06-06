package com.kcwl.ddd.domain.repository;

public interface IRepository {
    void insert(BasePO po);
    void update(BasePO po);
    BasePO findById(String id);
}
