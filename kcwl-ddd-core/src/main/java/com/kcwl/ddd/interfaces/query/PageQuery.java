package com.kcwl.ddd.interfaces.query;


import com.kcwl.ddd.domain.entity.PageCondition;

public class PageQuery extends BaseQuery {
    private long curPagerNo = PageCondition.FIRST_PAGE_NO;
    private long pageSize = PageCondition.DEFAULT_PAGE_SIZE;

    public long getCurPagerNo() {
        return curPagerNo;
    }

    public void setCurPagerNo(int curPagerNo) {
        this.curPagerNo = curPagerNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getOffset() {
        return (getCurPagerNo() - 1) * getPageSize();
    }
}
