package com.kcwl.ddd.interfaces.dto;

public class PageResultDTO<T> extends BaseDTO {
    private PageInfoDTO<T> page;

    public PageResultDTO(PageInfoDTO<T> page) {
        this.page = page;
    }

    public PageInfoDTO<T> getPage() {
        return page;
    }

    public void setPage(PageInfoDTO<T> page) {
        this.page = page;
    }
}
