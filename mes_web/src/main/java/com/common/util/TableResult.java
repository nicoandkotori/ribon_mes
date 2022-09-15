package com.common.util;

import lombok.Data;
import org.springframework.http.ResponseEntity;

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

    /**
     * 设置成功
     *
     * @param rows 行
     */
    public void setSuccess(List<E> rows){
        this.rows = rows;
    }

    /**
     * 成功
     *
     * @param rows 行
     * @return {@link TableResult}<{@link E}>
     */
    public static <E> TableResult<E> success(List<E> rows){
        TableResult<E> result = new TableResult<>();
        result.setSuccess(rows);
        return result;
    }


    public static <E> TableResult<E> error(String errormsg){
        TableResult<E> result = new TableResult<>();
        result.setErrormsg(errormsg);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public List<E> getRows() {
        return rows;
    }

    public void setRows(List<E> rows) {
        this.rows = rows;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
