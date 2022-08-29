package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.entity.RoleMenuClient;

import java.util.List;

/**
 * <p>
 * 菜单  服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IRoleMenuClientService extends IService<RoleMenuClient> {
    List<RoleMenuClient> findRoleMenuByRoleId(String roleId);
    boolean saveOrDeleteRoleMenus(String roleId, List<String> list);
}
