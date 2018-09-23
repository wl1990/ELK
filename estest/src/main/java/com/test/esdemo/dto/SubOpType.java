package com.test.esdemo.dto;

/**
 * 操作类型
 */
public class SubOpType {
    private String code;
    private String name;
    // 操作类型子节点
    private SubOpType subOpType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SubOpType getSubOpType() {
        return subOpType;
    }

    public void setSubOpType(SubOpType subOpType) {
        this.subOpType = subOpType;
    }
}
