package com.kcwl.ddd.interfaces.query;


import com.kcwl.ddd.domain.entity.PageCondition;

/**
 * @author ckwl
 */
public class PageQuery extends BaseQuery {
    private long curPagerNo = PageCondition.FIRST_PAGE_NO;
    private long pageSize = PageCondition.DEFAULT_PAGE_SIZE;

    public long getCurPagerNo() {
        return curPagerNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public long getOffset() {
        return (getCurPagerNo() - 1) * getPageSize();
    }

    public void setCurPagerNo(long curPagerNo) {
        this.curPagerNo = curPagerNo;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
}
