package com.web.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.util.CustomStringUtils;
import com.common.util.ResponseResult;
import com.web.system.dto.MenuPdaVM;
import com.web.system.entity.User;
import com.web.system.service.IMenuClientService;
import com.web.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunyin on 2019/12/24.
 */
@RestController
@RequestMapping(value = "/api/login")
public class ApiLoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService userService;
    @Autowired
    private IMenuClientService menuClientService;



    @RequestMapping(value = "/getbyid")
    public ResponseResult getById(String id) {
        ResponseResult result = new ResponseResult();
        result.setResult(userService.getById(id));
        return result;
    }

//    @ApiOperation(value = "用户名密码校验成功后，生成token返回客户端")
//    @ApiImplicitParam(name = "entity", value = "用户对象", required = true,
//            dataType= "User", paramType = "body")
    @RequestMapping(value = "/checkLogin")
    public ResponseResult checkLogin(String  username,String password, HttpServletRequest request){
        ResponseResult result = new ResponseResult();
            User entity =new User();
            entity.setUserCode(username);
            entity.setPwd(password);

            LambdaQueryWrapper<User> select=new LambdaQueryWrapper<>();
            select.eq(User::getUserCode, entity.getUserCode());
            select.eq(User::getIzDelete, false);

            User user = this.userService.getOne(select);
            if(user == null){
                result.setSuccess(false);
                result.setMsg("用户名不存在！");
                return result;
            }

//        if(!user.getPwd().equals(pwd)){
//            result.setSuccess(false);
//            result.setMsg("用户名和密码不匹配！");
//            return result;
//        }
        //用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername()去验证用户名和密码，
            // 如果正确，则存储该用户名密码到security 的 context中

            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(entity.getUserCode(),entity.getPwd()));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {

                result.setSuccess(false);
                result.setMsg("用户名或密码错误！");
                return result;
            } else if (e instanceof DisabledException) {

                result.setSuccess(false);
                result.setMsg("账户被禁用！");
                return result;
            } else if (e instanceof AccountExpiredException) {

                result.setSuccess(false);
                result.setMsg("账户过期无法验证！");
                return result;
            } else {

                result.setSuccess(false);
                result.setMsg("账户被锁定,无法登录！");
                return result;
            }
        }

        //生成token
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("uid", user.getId());//用户Id
        payload.put("uname", user.getUserName());//用户Id、
        user.setPwd(password);

        result.setSuccess(true);
        result.setMsg("登陆成功");
        result.setResult(JSON.toJSON(user));
        return result;
    }
    @PostMapping(value = "/getmenulist")
    public ResponseResult getMenu(String menuName,HttpServletRequest request){
        ResponseResult result = new ResponseResult();
        try {
            String userId=request.getHeader("token");
            if(CustomStringUtils.isBlank(userId))
            {
                userId=null;
            }
            List<MenuPdaVM> list=this.menuClientService.getPdaMenuList(menuName,userId);//user.getId()
            result.setSuccess(true);
            result.setResult(list);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }
}
