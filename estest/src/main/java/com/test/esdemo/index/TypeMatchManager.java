package com.test.esdemo.index;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TypeMatchManager {
    public static void setFieldType(Field declaredField, IndexMappingModel indexMappingModel, boolean isAnalyzed) {
        declaredField.setAccessible(true);
        Class<?> type = declaredField.getType();
        indexMappingModel.setField(declaredField.getName());
        String typeName = type.getName();
        if (typeName.equals(BigDecimal.class.getName()) || typeName.equals(Double.class.getName()) || typeName.equals(double.class.getName())) {
            indexMappingModel.setFieldType("double");
        }
        if (typeName.equals(Float.class.getName()) || typeName.equals(float.class.getName())) {
            indexMappingModel.setFieldType("float");
        }
        if (typeName.equals(int.class.getName()) || typeName.equals(Integer.class.getName()) || typeName.equals(BigInteger.class.getName())) {
            indexMappingModel.setFieldType("integer");
        }
        if (typeName.equals(long.class.getName()) || typeName.equals(Long.class.getName())) {
            indexMappingModel.setFieldType("long");
        }
        if (typeName.equals(String.class.getName())) {
            indexMappingModel.setFieldType("string");
        }
        if (typeName.equals(Boolean.class.getName())) {
            indexMappingModel.setFieldType("boolean");
        }
        if (typeName.equals(Date.class.getName())) {
            indexMappingModel.setFieldType("date");
        }
        if (!isAnalyzed) {
            indexMappingModel.setIndexAnalyzed("not_analyzed");
        }
    }
}
