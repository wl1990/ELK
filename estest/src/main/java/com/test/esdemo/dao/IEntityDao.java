package com.test.esdemo.dao;

import com.test.esdemo.dao.entity.BaseEntity;

import java.util.List;

public interface IEntityDao<T extends BaseEntity>{
    void  bulkInsert(List<? extends Object> list) throws IllegalAccessException;
}
