package com.test.esdemo.dao.impl;

import com.test.esdemo.dao.entity.BaseEntity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public  abstract class AbstractEntityDao<T extends BaseEntity> {
    protected Class<T> clazz = this.getClazz(this.getClass().getGenericSuperclass());

    private Class<T> getClazz(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] array = ((ParameterizedType)type).getActualTypeArguments();
            return (Class)array[0];
        } else {
            return null;
        }
    }
}
