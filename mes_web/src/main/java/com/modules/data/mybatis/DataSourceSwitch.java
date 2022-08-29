package com.modules.data.mybatis;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceSwitch {

    DBTypeEnum value() default DBTypeEnum.db1;
}
