package com.util;

import lombok.Data;

/**
 * 结果类
 * Created by Yao on 2018-6-9.
 */
@Data
public class ResponseResultPDA {
    private static final long serialVersionUID = -2782978187072174926L;
    private boolean success;
    private String msg;
    private String errormsg;
    private int code;
    private Object result;
    private Object other;
    private Object other1;
    public ResponseResultPDA(){
        success=true;
        code = this.code;
        msg=this.msg;
        errormsg = this.errormsg;
        other1 = this.other1;
    }


    public static ResponseResultPDA success() {
        ResponseResultPDA r = new ResponseResultPDA();
        r.setSuccess(true);
        r.setMsg("操作成功");
        return r;
    }

    public static ResponseResultPDA success(Object data) {
        ResponseResultPDA r = new ResponseResultPDA();
        r.setSuccess(true);
        r.setMsg("操作成功");
        r.setResult(data);
        return r;
    }

    public static ResponseResultPDA error() {
        ResponseResultPDA r = new ResponseResultPDA();
        r.setSuccess(false);
        r.setMsg("未知异常，请联系管理员");
        r.setErrormsg("未知异常，请联系管理员");
        return r;
    }

    public static ResponseResultPDA error(String msg) {
        ResponseResultPDA r = new ResponseResultPDA();
        r.setSuccess(false);
        r.setMsg(msg);
        r.setErrormsg(msg);
        return r;
    }

    public static ResponseResultPDA error(String msg, Object data) {
        ResponseResultPDA r = new ResponseResultPDA();
        r.setSuccess(false);
        r.setMsg(msg);
        r.setErrormsg(msg);
        r.setResult(data);
        return r;
    }

}
