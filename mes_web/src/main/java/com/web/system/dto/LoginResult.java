package com.web.system.dto;

import com.common.util.R;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by Yao on 2019/11/27.
 */
@Getter
@Setter
public class LoginResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private int code = 200;
    private String msg;
    private String token;
    private String account;
    private String userName;

    public static LoginResult ok() {
        LoginResult r=new LoginResult();
        r.setMsg("操作成功");
        return r;
    }

    public static LoginResult ok(String token,String account,String userName) {
        LoginResult r=new LoginResult();
        r.setMsg("操作成功");
        r.setAccount(account);
        r.setUserName(userName);
        r.setToken(token);
        return r;
    }

    public static LoginResult error() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
    }

    public static LoginResult error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static LoginResult error(int code, String msg) {
        LoginResult r = new LoginResult();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}
