package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.entity.RoleUserPda;
import com.web.system.entity.User;

import java.util.List;

/**
 * <p>
 * 菜单  服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IRoleUserPdaService extends IService<RoleUserPda> {
    int saveUsers(List<User> list, String roleId);
    int deleteUser(String userId, String roleId);
}
