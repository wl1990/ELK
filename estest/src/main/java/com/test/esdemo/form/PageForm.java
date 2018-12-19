package com.test.esdemo.form;

import java.util.List;

public class PageForm<T> {
    private List<T> list;
    private int totalCount;
    private int crrentPage;

    public PageForm() {
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCrrentPage() {
        return this.crrentPage;
    }

    public void setCrrentPage(int crrentPage) {
        this.crrentPage = crrentPage;
    }
}
