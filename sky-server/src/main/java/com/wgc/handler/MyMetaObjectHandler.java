package com.wgc.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wgc.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始进行插入操作的自动填充...");

        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());

        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        this.strictInsertFill(metaObject, "createUser", Long.class, BaseContext.getCurrentId());

        this.strictInsertFill(metaObject, "updateUser", Long.class, BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始进行更新操作的填充...");

        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        this.strictUpdateFill(metaObject, "updateUser", Long.class, BaseContext.getCurrentId());
    }
}
