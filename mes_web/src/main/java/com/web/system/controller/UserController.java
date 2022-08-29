package com.web.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.*;
import com.modules.security.PreSecurityUser;
import com.modules.security.util.SecurityUtil;
import com.web.common.controller.BasicController;
import com.web.system.dto.UserCurrent;
import com.web.system.dto.UserDTO;
import com.web.system.entity.User;
import com.web.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * 用户信息
 */
@RestController
@RequestMapping(value = "/sysinfo/user")
public class UserController extends BasicController {

    @Autowired
    private IUserService userService;


    //region select
    @RequestMapping(value = "/findlist")
    public ResponseResult findList(User query) {
        ResponseResult result = new ResponseResult();

        LambdaQueryWrapper<User> select=new LambdaQueryWrapper<>();
        select = select.eq(User::getIzDelete,0);
        if(query != null) {
            if (CustomStringUtils.isNotBlank(query.getUserName())) {
                select = select.like(User::getUserName, query.getUserName());
            }
        }
        select.orderByAsc(User::getUserName);

        List<User> List = userService.list(select);
        result.setResult(List);
        return result;
    }

    @RequestMapping(value = "/findpage")
    public TableResult<User> findPage(int page, int rows, String querystr) {
        TableResult<User> result = new TableResult<>();
        UserDTO query = JSON.parseObject(querystr, UserDTO.class);
        IPage<User> page1 = new Page<>(page, rows);

        LambdaQueryWrapper<User> select=new LambdaQueryWrapper<>();
        select = select.eq(User::getIzDelete, false);
        if(query != null) {
            if (CustomStringUtils.isNotBlank(query.getUserCode())) {
                select = select.like(User::getUserCode, query.getUserCode());
            }
            if (CustomStringUtils.isNotBlank(query.getUserName())) {
                select = select.like(User::getUserName, query.getUserName());
            }
        }
        select.orderByAsc(User::getUserCode);

        IPage<User> pg = userService.page(page1, select);
        result.setRecords(pg.getTotal());
        result.setTotal((int)Math.ceil(pg.getTotal()/(double)pg.getSize()));
        result.setRows(pg.getRecords());
        return result;
    }

    @RequestMapping(value = "/getbyid")
    public ResponseResult getById(String id) {
        ResponseResult result = new ResponseResult();
        result.setResult(userService.getById(id));
        return result;
    }

    /**
     * 获取当前登录人、部门
     */
    @RequestMapping(value = "/getcuruser")
    public ResponseResult getCurUser(HttpServletRequest request) {
        ResponseResult result = new ResponseResult();
        PreSecurityUser user = SecurityUtil.getUser();

        UserCurrent curUser = new UserCurrent();
        curUser.setUserCode(user.getMyusercode());
        curUser.setUserName(user.getMyusername());
        curUser.setId(user.getUserId());
        User u = userService.getById(user.getUserId());
 /*       if (u != null) {
            String depId = u.getDepId();
            if (CustomStringUtils.isNotBlank(depId)) {
                Department dep = departmentService.getById(depId);
                curUser.setDepId(depId);
                curUser.setDepCode(dep.getDepCode());
                curUser.setDepName(dep.getDepName());
            }
        }*/

        result.setResult(curUser);
        return result;
    }

    //endregion

    //region insert update
    @RequestMapping(value = "/saveorupdate")
    public ResponseResult saveOrUpdate(String data, HttpServletRequest request) throws Exception {
        ResponseResult result = new ResponseResult();
        User query = JSON.parseObject(data, User.class);

        if(CustomStringUtils.isBlank(query.getId())){
            LambdaQueryWrapper<User> select=new LambdaQueryWrapper<>();
            select.eq(User::getUserCode, query.getUserCode());
            List<User> userList = userService.list(select);
            if(userList.size() > 0){
                result.setSuccess(false);
                result.setMsg("账号已存在, 请重新输入！");
                return result;
            }
            query.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
            query.setCreateUser(SecurityUtil.getUser().getUsername());
            query.setCreateDate(new Date());
            query.setGroupId(0);
            query.setIzDelete(false);
            String crypwd=cry("8888");
            query.setPwd(crypwd);
            EncryptUtil encryptUtil = new EncryptUtil();
            String pwd = encryptUtil.encryptToSHA("8888");
            query.setPwd1(pwd);
            userService.save(query);
        }else{

            User oldUser = userService.getById(query.getId());
            query.setUpdateUser(SecurityUtil.getUser().getUsername());
            query.setUpdateDate(new Date());
            String userCode = getCurrentUser(request).getMyusercode();

            if (!"admin".equals(userCode)) {
                result.setSuccess(false);
                result.setMsg("admin才允许修改其他账号！");
                return result;
            }
            if ("admin".equals(oldUser.getUserCode())) {
                result.setSuccess(false);
                result.setMsg("admin的账号不允许修改！");
                return result;
            }

            if(CustomStringUtils.isNotBlank(query.getPwd())) {
                String crypwd=cry(query.getPwd());
                query.setPwd(crypwd);
                EncryptUtil encryptUtil = new EncryptUtil();
                String pwd = encryptUtil.encryptToSHA(query.getPwd());
                query.setPwd1(pwd);
            }else{
                query.setPwd(oldUser.getPwd());
                query.setPwd1(oldUser.getPwd1());
            }
            userService.updateById(query);
        }
        result.setResult(query.getId());
        return result;
    }

    @RequestMapping("/softdelete")
    public ResponseResult softDelete(String id) throws Exception {
        ResponseResult result = new ResponseResult();

        String userCode = userService.getById(id).getUserCode();
        if("admin".equals(userCode)){
            result.setSuccess(false);
            result.setMsg("admin账号不允许删除！");
            return result;
        }
        User user = new User();

        user.setIzDelete(true);
        user.setId(id);

        userService.updateById(user);
        result.setSuccess(true);
        result.setMsg("删除成功！");
        return result;
    }
    //endregion

    /**
     * 查找不在userrole表的user
     *
     * @param querystr
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/findUserNotInUserrole")
    public TableResult<User> findUserNotInUserrole(String querystr, int page, int rows) {
        TableResult<User> result = new TableResult<User>();
        User user = new User();
        if(StringUtils.isNotBlank(querystr)){
            user = JSON.parseObject(querystr, User.class);
        }
        IPage<User> page1 = new Page<>(1, page*rows);

        LambdaQueryWrapper<User> select=new LambdaQueryWrapper<>();
        select = select.eq(User::getIzDelete, 0);
        select=select.ne(User::getGroupId,1);  //非管理员
        if(StringUtils.isNotBlank(user.getUserCode())){
            select=select.like(User::getUserCode,user.getUserCode());
        }
        if(StringUtils.isNotBlank(user.getUserName())){
            select=select.like(User::getUserName,user.getUserName());
        }
        select = select.apply("id not in ( select user_id from sys_role_user )");

        IPage<User> pg = userService.page(page1, select);
        result.setRecords(pg.getTotal());
        result.setTotal((int)Math.ceil(pg.getTotal()/(double)pg.getSize()));
        result.setRows(pg.getRecords());
        return result;


    }
    /**
     * 查找不在userrole表的user  pda
     *
     * @param querystr
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/findUserNotInUserrole1")
    public TableResult<User> findUserNotInUserrole1(String querystr, int page, int rows) {
        TableResult<User> result = new TableResult<User>();
        User user = new User();
        if (StringUtils.isNotBlank(querystr)) {
            user = JSON.parseObject(querystr, User.class);
        }
        IPage<User> page1 = new Page<>(1, page * rows);

        LambdaQueryWrapper<User> select = new LambdaQueryWrapper<>();
        select = select.eq(User::getIzDelete, false);
        select = select.ne(User::getGroupId, 1);  //非管理员
        if (StringUtils.isNotBlank(user.getUserCode())) {
            select = select.like(User::getUserCode, user.getUserCode());
        }
        if (StringUtils.isNotBlank(user.getUserName())) {
            select = select.like(User::getUserName, user.getUserName());
        }
        select = select.apply("id not in ( select user_id from sys_role_user_pda )");
        select.orderByAsc(User::getUserCode);
        IPage<User> pg = userService.page(page1, select);
        result.setRecords(pg.getTotal());
        result.setTotal((int) Math.ceil(pg.getTotal() / (double) pg.getSize()));
        result.setRows(pg.getRecords());
        return result;


    }




    /**
     * 查找不在userrole表的user  客户端
     *
     * @param querystr
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/findUserNotInUserrole2")
    public TableResult<User> findUserNotInUserrole2(String querystr, int page, int rows) {
        TableResult<User> result = new TableResult<User>();
        User user = new User();
        if (StringUtils.isNotBlank(querystr)) {
            user = JSON.parseObject(querystr, User.class);
        }
        IPage<User> page1 = new Page<>(1, page * rows);

        LambdaQueryWrapper<User> select = new LambdaQueryWrapper<>();
        select = select.eq(User::getIzDelete, false);
        select = select.ne(User::getGroupId, 1);  //非管理员
        if (StringUtils.isNotBlank(user.getUserCode())) {
            select = select.like(User::getUserCode, user.getUserCode());
        }
        if (StringUtils.isNotBlank(user.getUserName())) {
            select = select.like(User::getUserName, user.getUserName());
        }
        select = select.apply("id not in ( select user_id from sys_role_user_client )");
        select.orderByAsc(User::getUserCode);
        IPage<User> pg = userService.page(page1, select);
        result.setRecords(pg.getTotal());
        result.setTotal((int) Math.ceil(pg.getTotal() / (double) pg.getSize()));
        result.setRows(pg.getRecords());
        return result;


    }






    /**
     * 修改密码
     * @param password
     * @return
     */
    @RequestMapping(value = "/modifypassword")
    @ResponseBody
    public ResponseResult modifyPassword(String password, HttpServletRequest request) throws  Exception{
        ResponseResult result = new ResponseResult();
        PreSecurityUser user = getCurrentUser(request);
        if (user == null){
            throw new Exception("登录失效,请重新登录");
        }
        User user2 = new User();
        user2.setPwd(user.getPassword());
        user2.setId(user.getUserId());
//            user2.setUserName(user.getUsername());
        String crypwd=cry(password);
        user2.setPwd(crypwd);

        EncryptUtil encryptUtil = new EncryptUtil();
        String pwd = encryptUtil.encryptToSHA(password);
        user2.setPwd1(pwd);

        LambdaUpdateWrapper<User> update = new LambdaUpdateWrapper();
        update.eq(User::getId,user2.getId());
        boolean flag =  userService.update(user2,update);
        if(flag){
            result.setSuccess(true);
            result.setMsg("修改成功");
        }else{
            result.setSuccess(false);
            result.setMsg("修改失败");
        }

        return result;
    }

    private String cry(String rawPwd){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(rawPwd);
        return  password;

    }
}
