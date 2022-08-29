package com.modules.data.mybatis;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *
 * 扩展Spring的AbstractRoutingDataSource抽象类，实现动态数据源。
 * AbstractRoutingDataSource中的抽象方法determineCurrentLookupKey是实现数据源的route的核心，
 * 这里对该方法进行Override。 【上下文DbContextHolder为一线程安全的ThreadLocal】
 * Created by Yao on 2019/11/18.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 取得当前使用哪个数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey(){
        return DbContextHolder.getDbType();
    }
}
