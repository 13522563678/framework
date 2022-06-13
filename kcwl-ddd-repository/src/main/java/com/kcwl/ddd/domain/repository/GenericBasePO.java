package com.kcwl.ddd.domain.repository;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

public class GenericBasePO <T extends BasePO<?>> extends BasePO<T> {
    private Long updateUserId;
    private Date updateTime;
    private Long createUserId;
    private Date createTime;

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
