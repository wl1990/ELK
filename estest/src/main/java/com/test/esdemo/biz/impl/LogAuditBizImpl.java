package com.test.esdemo.biz.impl;

import com.test.esdemo.biz.ILogAuditBiz;
import com.test.esdemo.dao.ILogAuditDao;
import com.test.esdemo.dao.entity.LogAuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogAuditBizImpl implements ILogAuditBiz {
    @Autowired
    private ILogAuditDao logAuditDao;
    @Override
    public void add(List<LogAuditEntity> list) throws IllegalAccessException {
        logAuditDao.bulkInsert(list);
    }
}
