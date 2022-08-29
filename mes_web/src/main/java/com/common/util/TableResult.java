package com.common.util;

import lombok.Data;

import java.util.List;

/**
 * jqgrid  返回实体类
 * Created by Yao on 2018-7-24.
 */
@Data
public class TableResult<E> {
    private boolean success;
    private String errorcode;
    private String errormsg;
    private List<E> rows;
    private long records;
    private int total;
    private Object data;
    public TableResult(){
        success=true;
        errorcode="ok";
        errormsg="";

        records=0;
        total=0;
    }

}
