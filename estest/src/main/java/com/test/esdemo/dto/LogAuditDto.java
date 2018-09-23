package com.test.esdemo.dto;


import java.util.Map;

public class LogAuditDto {
    private static final long serialVersionUID = 5269717600684121537L;
    private String id;
    private String projectId;
    private String projectName;
    private Long userId;
    private String userName;
    private String url;
    private String referer;
    private String userIp;
    private String targetIp;
    private String contextPath;
    private String operatorTargetCode;
    private String operatorTargetName;
    // 操作类型
    private SubOpType opTypee;
    private Map<String,Object> paramsMap;
    private String createTime;
    private Map<String,Object> targetType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, Object> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public String getOperatorTargetCode() {
        return operatorTargetCode;
    }

    public void setOperatorTargetCode(String operatorTargetCode) {
        this.operatorTargetCode = operatorTargetCode;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public Map<String, Object> getTargetType() {
        return targetType;
    }

    public void setTargetType(Map<String, Object> targetType) {
        this.targetType = targetType;
    }

    public String getOperatorTargetName() {
        return operatorTargetName;
    }

    public void setOperatorTargetName(String operatorTargetName) {
        this.operatorTargetName = operatorTargetName;
    }

    public SubOpType getOpTypee() {
        return opTypee;
    }

    public void setOpTypee(SubOpType opTypee) {
        this.opTypee = opTypee;
    }


}

