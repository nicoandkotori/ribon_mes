package com.common.util;

import lombok.Data;

/**
 * 结果类
 *
 * @author Yao
 * @date 2018-6-9
 */
@Data
public class ResponseResult {
    private boolean success;
    private String msg;
    private String code;
    private Object result;
    private Object result1;
    public ResponseResult() {
        success = true;
        code = "200";
        msg = "";
    }

    public static ResponseResult success() {
        ResponseResult r = new ResponseResult();
        r.setSuccess(true);
        r.setMsg("操作成功");
        return r;
    }

    public static ResponseResult success(Object data) {
        ResponseResult r = new ResponseResult();
        r.setSuccess(true);
        r.setMsg("操作成功");
        r.setResult(data);
        return r;
    }

    public static ResponseResult error() {
        ResponseResult r = new ResponseResult();
        r.setSuccess(false);
        r.setMsg("未知异常，请联系管理员");
        return r;
    }

    public static ResponseResult error(String msg) {
        ResponseResult r = new ResponseResult();
        r.setSuccess(false);
        r.setMsg(msg);
        return r;
    }

    public static ResponseResult error(String msg, Object data) {
        ResponseResult r = new ResponseResult();
        r.setSuccess(false);
        r.setMsg(msg);
        r.setResult(data);
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult1() {
        return result1;
    }

    public void setResult1(Object result1) {
        this.result1 = result1;
    }
}
