package com.test.esdemo.biz;

import com.test.esdemo.dao.entity.LogAuditEntity;

import java.util.List;

public interface ILogAuditBiz {
    public void add(List<LogAuditEntity> list) throws IllegalAccessException;
}
