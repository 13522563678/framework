package com.kcwl.ddd.interfaces.dto;

import java.util.List;

public class ListResultDTO<T> extends BaseDTO{
    private List<T> list;

    public ListResultDTO() {
    }

    public ListResultDTO(List<T> list) {
        this.list = list;
    }

    public List getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
