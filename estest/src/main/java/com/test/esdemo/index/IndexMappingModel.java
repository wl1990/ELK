package com.test.esdemo.index;

public class IndexMappingModel {
    private String field; //字段名称
    private String fieldType; //字段类型
    private String indexAnalyzed; //使用的分词器
    private Boolean store = true; //是否索引

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getIndexAnalyzed() {
        return indexAnalyzed;
    }

    public void setIndexAnalyzed(String indexAnalyzed) {
        this.indexAnalyzed = indexAnalyzed;
    }

    public Boolean getStore() {
        return store;
    }

    public void setStore(Boolean store) {
        this.store = store;
    }
}
