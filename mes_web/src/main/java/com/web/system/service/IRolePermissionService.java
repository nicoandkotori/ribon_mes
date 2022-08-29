package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.entity.RolePermission;

import java.util.List;

public interface IRolePermissionService extends IService<RolePermission> {

    void saveOrDeleteRolePermissions(String roleId, List<RolePermission> list);
}
