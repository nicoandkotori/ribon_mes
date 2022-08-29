package com.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.system.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<RolePermission> selectNotInPermissionid(@Param("roleId") String roleId,
                                                 @Param("permissionidArr") List<String> permissionid);
}
