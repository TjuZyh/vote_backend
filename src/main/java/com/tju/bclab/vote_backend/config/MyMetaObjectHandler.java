package com.tju.bclab.vote_backend.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

// 专门用来进行自动填充的接口类，Component注解说明这是一个pojo对象，然后需要被注入进去
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //  使用mp实现添加操作即可
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }
}