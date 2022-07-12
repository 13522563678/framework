package com.kcwl.ddd.domain.repository;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class GenericBasePO <T extends BasePO<?>> extends BasePO<T> {

    @TableField(value = "update_user_id",fill = FieldFill.UPDATE)
    private Long updateUserId;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(value = "create_user_id",fill = FieldFill.INSERT)
    private Long createUserId;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
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
