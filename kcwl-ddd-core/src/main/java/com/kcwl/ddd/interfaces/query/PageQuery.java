package com.kcwl.ddd.interfaces.query;


import com.kcwl.ddd.domain.entity.PageCondition;

public class PageQuery extends BaseQuery {
    private int curPagerNo = PageCondition.FIRST_PAGE_NO;
    private int pageSize = PageCondition.DEFAULT_PAGE_SIZE;

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
