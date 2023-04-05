package com.kcwl.ddd.domain.entity;

public class PageCondition extends Criteria {
    public static final int  DEFAULT_PAGE_SIZE = 10;
    public static final int  FIRST_PAGE_NO = 1;

    private long curPagerNo = FIRST_PAGE_NO;
    private long pageSize = DEFAULT_PAGE_SIZE;

    public long getCurPagerNo() {
        return curPagerNo;
    }

    public void setCurPagerNo(long curPagerNo) {
        this.curPagerNo = curPagerNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getOffset() {
        return (getCurPagerNo() - 1) * getPageSize();
    }
}
