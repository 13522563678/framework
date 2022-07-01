package com.kcwl.framework.mpp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class KcMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("createUserId", SessionContext.getSessionData().getUserId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(),metaObject);
        this.setFieldValByName("updateUserId", SessionContext.getSessionData().getUserId(), metaObject);
    }
}
