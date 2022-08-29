package com.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.system.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    //获取用户权限
    List<Permission> listByUserId(@Param("userId") String userId);
}
