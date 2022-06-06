package com.kcwl.ddd.interfaces.dto;


public class PageResultDTO extends BaseDTO {
    private PageInfoDTO page;

    public PageResultDTO(PageInfoDTO page) {
        this.page = page;
    }

    public PageInfoDTO getPage() {
        return page;
    }

    public void setPage(PageInfoDTO page) {
        this.page = page;
    }
}
