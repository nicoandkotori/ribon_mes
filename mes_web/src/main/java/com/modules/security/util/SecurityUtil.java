package com.modules.security.util;

import com.alibaba.fastjson.JSON;
import com.common.util.R;
import com.modules.security.PreSecurityUser;
import com.common.exception.BaseException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname SecurityUtil
 * @Description 安全服务工具类
 * @Author 李号东 lihaodongmail@163.com
 * @Date 2019-05-08 10:12
 * @Version 1.0
 */
@UtilityClass
public class SecurityUtil {

    public void writeJavaScript(R r, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSON.toJSONString(r));
        printWriter.flush();
    }

    /**
     * 获取Authentication
     *   未登录的情况下，返回的是一个字符串：anonymousUser
     *   登录的情况下返回的是LoadUserByUsername的方法中返回的User对象。
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * @Author 李号东
     * @Description 获取用户
     * @Date 11:29 2019-05-10
     **/
    public PreSecurityUser getUser(){
        try {
            return (PreSecurityUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BaseException("登录状态过期", HttpStatus.UNAUTHORIZED.value());
        }
    }
}
