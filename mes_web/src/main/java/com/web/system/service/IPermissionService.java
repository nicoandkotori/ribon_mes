package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.dto.MenuDTO;
import com.web.system.entity.Permission;

import java.util.List;

public interface IPermissionService extends IService<Permission> {

    //region select
    //获取某一菜单下的用户权限
    List<Permission> listByMenuIdAndUserId(String userId,String menuId);

    //获取用户权限
    List<Permission> listByUserId(String userId);

    List<MenuDTO> listByRoleId(String roleId);

    //endregion
}
