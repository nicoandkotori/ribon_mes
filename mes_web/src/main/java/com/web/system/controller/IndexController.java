package com.web.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.util.EntityUtils;
import com.common.util.ResponseResult;
import com.modules.security.PreSecurityUser;
import com.web.common.controller.BasicController;
import com.web.system.dto.LoginResult;
import com.web.system.dto.UserDTO;
import com.web.system.entity.User;
import com.web.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Yao on 2019/9/25.
 */
@RestController
public class IndexController extends BasicController {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

   /* @Value("${pre.url.address}")
    private String url;*/
    @Value("${jwt.key}")
    private String tokenKey;



    @RequestMapping(value = "/login")
    public LoginResult login(String username, String password, HttpServletRequest request) {
        try {
            // 登录
            //String token = request.getParameter("token");
            //String token= CookieUtils.getCookieValue(request,tokenKey,false);
            String token = request.getHeader("Authorization");
            if (StrUtil.isNotEmpty(token)) {
                return LoginResult.ok(token, "", username);
            }
            String tk = userService.login(username, password);
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper=queryWrapper.eq(User::getUserCode,username);
            queryWrapper=queryWrapper.eq(User::getIzDelete,0);
            List<User> listUser=userService.list(queryWrapper);
            if(listUser!=null&&listUser.size()>0)
            {
                username=listUser.get(0).getUserName();
            }
            return LoginResult.ok(tk, "", username);
        }catch (Exception e){
            e.printStackTrace();
            return LoginResult.error(402,"用户名或者密码错误");
        }
    }


    /**
     * @Description  返回当前登录 用户的信息
     * @Date 08:12 2019-06-22
     **/
    @RequestMapping("/info")
    public ResponseResult info(HttpServletRequest request) {
        ResponseResult res=new ResponseResult();
        try{
            PreSecurityUser preUser=getCurrentUser(request);
//            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
//            queryWrapper=queryWrapper.eq(User::getId,preUser.getUserId());
            User user=userService.getById(preUser.getUserId());
            UserDTO dto=EntityUtils.copyObject(user, UserDTO.class);
            if(dto!=null){
                res.setResult(dto);
            }else {
                res.setSuccess(false);
                res.setMsg("查询失败，请重试！");
            }

        }catch (Exception e){
            res.setSuccess(false);
            res.setMsg("查询出错，请重试！");
        }
        return  res;
    }

    /**
     * @Author 李号东
     * @Description 使用jwt前后分离 只需要前端清除token即可 暂时返回success
     * @Date 08:13 2019-06-22
     **/
    @RequestMapping("/logout")
    public String logout() {
        return "success";
    }



}
