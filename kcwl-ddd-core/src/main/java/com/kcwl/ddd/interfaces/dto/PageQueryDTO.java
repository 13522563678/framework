package com.kcwl.ddd.interfaces.dto;

public class PageQueryDTO extends QueryDTO {
    private int curPagerNo = PageInfoDTO.FIRST_PAGE_NO;
    private int pageSize = PageInfoDTO.DEFAULT_PAGE_SIZE;

    public int getCurPagerNo() {
        return curPagerNo;
    }

    public void setCurPagerNo(int curPagerNo) {
        this.curPagerNo = curPagerNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (getCurPagerNo() - 1) * getPageSize();
    }
}
