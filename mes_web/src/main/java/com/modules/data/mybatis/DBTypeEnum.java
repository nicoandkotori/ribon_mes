package com.modules.data.mybatis;

/**
 * 多数据源 枚举
 * Created by Yao on 2019/11/18.
 */
public enum DBTypeEnum {
    db1("db1"), db2("db2"), db3("db3") ;
    private String value;
    DBTypeEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
