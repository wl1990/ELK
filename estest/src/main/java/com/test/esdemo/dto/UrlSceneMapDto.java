package com.test.esdemo.dto;

/**
 * url和场景的映射
 */
public class UrlSceneMapDto {
    private String projectName;
    private String url;
    // 操作对象
    private String optg;

    private String optgCode;
    // 操作类型
    private String opty;
    private String optyCode;
    // 描述
    private String desc;

    // 审计url 0 非审计 1
    private Integer  urlKind;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOptg() {
        return optg;
    }

    public void setOptg(String optg) {
        this.optg = optg;
    }

    public String getOpty() {
        return opty;
    }

    public void setOpty(String opty) {
        this.opty = opty;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getUrlKind() {
        return urlKind;
    }

    public void setUrlKind(Integer urlKind) {
        this.urlKind = urlKind;
    }

    public String getOptgCode() {
        return optgCode;
    }

    public void setOptgCode(String optgCode) {
        this.optgCode = optgCode;
    }

    public String getOptyCode() {
        return optyCode;
    }

    public void setOptyCode(String optyCode) {
        this.optyCode = optyCode;
    }
}
