package com.util;

import java.util.List;

/**
 * easyui datagrid  返回实体类
 * Created by Yao on 2018-7-24.
 */
public class DataGridResult<E> {
    private List<E> rows;
    //总数目
    private long total;

    public DataGridResult() {
        total = 0;
    }

    public List<E> getRows() {
        return rows;
    }

    public void setRows(List<E> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
