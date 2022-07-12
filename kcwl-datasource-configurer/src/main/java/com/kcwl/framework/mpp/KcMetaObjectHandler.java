package com.kcwl.framework.mpp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kcwl.ddd.infrastructure.constants.EmptyObject;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.ddd.infrastructure.session.SessionData;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class KcMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date currentDate = new Date();
        if ( metaObject.hasGetter("createTime") ) {
            this.setFieldValByName("createTime", currentDate, metaObject);
        }
        if ( metaObject.hasGetter("updateTime") ) {
            this.setFieldValByName("updateTime", currentDate, metaObject);
        }
        if ( metaObject.hasGetter("createUserId") ) {
            SessionData sessionData = SessionContext.getSessionData();
            if (sessionData != null) {
                this.setFieldValByName("createUserId", sessionData.getUserId(), metaObject);
            } else {
                this.setFieldValByName("createUserId", EmptyObject.INTEGER_ZERO, metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if ( metaObject.hasGetter("updateTime") ) {
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
        if ( metaObject.hasGetter("updateUserId") ) {
            SessionData sessionData = SessionContext.getSessionData();
            if (sessionData != null) {
                this.setFieldValByName("updateUserId", sessionData.getUserId(), metaObject);
            }
        }
    }
}
