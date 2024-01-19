package com.druidelf.request;


public class PageBase {

    /**
     * 当前页码
     */
    private int page = 1;

    /**
     * 每页大小
     */
    private int size = 10;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
