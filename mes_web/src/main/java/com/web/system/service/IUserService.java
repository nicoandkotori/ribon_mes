package com.web.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.entity.User;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IUserService extends IService<User> {


    /**
     * 账户密码登录
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);


    /**
     * 通过用户去查找用户(id/用户名/手机号)
     * @param user
     * @return
     */
    User findSecurityUserByUser(User user);


    boolean setUserToRedis(String token, String userId);


    IPage<User> findUserNotInUserrole(Page<User> page1, User user );

    boolean modifyPassword(User user,String password,String newPassword) throws  Exception;
    /**
     * 获取用户角色
     */
    String getUserRole(String userId);

}
