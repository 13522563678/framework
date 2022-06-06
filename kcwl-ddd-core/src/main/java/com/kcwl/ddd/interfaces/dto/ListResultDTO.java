package com.kcwl.ddd.interfaces.dto;

import java.util.List;

public class ListResultDTO extends BaseDTO{
    private List list;

    public ListResultDTO(List list) {
        this.list = list;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
