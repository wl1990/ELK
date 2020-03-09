package com.test.esdemo.dao.impl;

import com.test.esdemo.dao.ILogAuditDao;
import com.test.esdemo.dao.entity.LogAuditEntity;
import org.springframework.stereotype.Component;

@Component
public class LogAuditDaoImpl extends AbstractEntityDao<LogAuditEntity> implements ILogAuditDao {
}
