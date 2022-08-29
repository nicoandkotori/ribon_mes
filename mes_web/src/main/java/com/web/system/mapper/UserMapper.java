package com.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.system.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> selectUserNotInUserrole(Page<User> page, @Param("user") User user);

    List<User> selectUserListByRoleid(String roleid);

    User selectUserByUsercode(String userCode);

    User getUserByUserId(String userId);

    int softDelete(String userId);

    int updatePasswordByUserId(User user);
    /**
     * 获取用户角色
     *
     * @param userId
     */
    String getUserRole(String userId);
}
