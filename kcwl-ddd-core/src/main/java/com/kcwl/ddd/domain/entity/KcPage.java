package com.kcwl.ddd.domain.entity;

import java.util.List;

public class KcPage<T> extends BaseEntity {

    private static final long serialVersionUID = 8572943675678665226L;

    public static final int  EXPORT_LIMIT_SIZE = 20000;
    public static final int  DEFAULT_PAGE_SIZE = 10;
    public static final int  FIRST_PAGE_NO = 1;

    /**
     * 当前页
     */
    private long curPagerNo = FIRST_PAGE_NO;

    /**
     * 每页显示的记录
     */
    private long pageSize =DEFAULT_PAGE_SIZE;

    /**
     * 记录行数
     */
    private long rowsCount;

    /**
     * 页数
     */
    private long totalPageNumber;

    private List<T> list;


    /**
     * @param allCount
     *            记录行数
     * @param pageSize
     *            每页显示的记录数
     */
    public KcPage(long allCount, long curPagerNo, long pageSize, List list) {
        this.curPagerNo = curPagerNo;
        this.pageSize = pageSize;
        this.rowsCount = allCount;
        this.list = list;
        this.totalPageNumber = (int) Math.ceil((double) allCount / pageSize);
    }

    public KcPage() {
    }

    public KcPage(KcPage pager) {
        this.curPagerNo = pager.curPagerNo;
        this.pageSize = pager.pageSize;
        this.rowsCount = pager.rowsCount;
        this.list = pager.list;
    }

    // getPageSize：返回分页大小
    public long getPageSize() {
        return pageSize;
    }

    // getRowsCount：返回总记录行数
    public long getRowsCount() {
        return rowsCount;
    }

    // getPageCount：返回总页数
    public long getTotalPageNumber() {
        return totalPageNumber=(int) Math.ceil((double) rowsCount / pageSize);
    }

    // 第一页
    public int first() {
        return 1;
    }

    // 最后一页
    public long last() {
        return totalPageNumber;
    }

    // 上一页
    public long previous() {
        return (curPagerNo - 1 < 1) ? 1 : curPagerNo - 1;
    }

    // 下一页
    public long next() {
        return (curPagerNo + 1 > totalPageNumber) ? totalPageNumber : curPagerNo + 1;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "curPagerNo=" + curPagerNo +
                ", pageSize=" + pageSize +
                ", rowsCount=" + rowsCount +
                ", totalPageNumber=" + totalPageNumber +
                ", list=" + list +
                '}';
    }

    public long getCurPagerNo() {
        return curPagerNo;
    }

    public void setCurPagerNo(long curPagerNo) {
        this.curPagerNo = curPagerNo;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public void setRowsCount(long rowsCount) {
        this.rowsCount = rowsCount;
    }

    public void setTotalPageNumber(long pageCount) {
        this.totalPageNumber = pageCount;
    }

    public void appendPage(KcPage pager) {
        if ( pager != null ) {
            this.curPagerNo = curPagerNo;
            this.pageSize = pageSize;
            this.rowsCount += pager.rowsCount;
            this.totalPageNumber = this.getTotalPageNumber();
            this.list.addAll(pager.list);
        }
    }
    public void setPageInfo(int pageNo, int pageSize) {
        this.curPagerNo = pageNo;
        this.pageSize = pageSize;
    }
}
