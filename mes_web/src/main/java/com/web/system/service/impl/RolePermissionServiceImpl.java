package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.SnowFlakeUtils;
import com.web.system.entity.RolePermission;
import com.web.system.mapper.RolePermissionMapper;
import com.web.system.service.IRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tao
 * @date 2020/3/9 15:35:16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper,RolePermission> implements IRolePermissionService {

    //region insert update
    @Override
    public void saveOrDeleteRolePermissions(String roleId, List<RolePermission> list) {
        List<String> actionidList = new ArrayList<String>();
        for (RolePermission query : list) {
            actionidList.add(query.getPermissionId());
            LambdaQueryWrapper<RolePermission> param = new LambdaQueryWrapper<>();
            param = param.eq(CustomStringUtils.isNotBlank(query.getRoleId()),RolePermission::getRoleId,query.getRoleId());
            param = param.eq(CustomStringUtils.isNotBlank(query.getPermissionId()), RolePermission::getPermissionId, query.getPermissionId());
            RolePermission result = this.baseMapper.selectOne(param);
            if (result == null) {
                query.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
                this.baseMapper.insert(query);
            }
        }
        List<RolePermission> leftList = this.baseMapper.selectNotInPermissionid(roleId, actionidList);
        for (RolePermission rolePermission : leftList) {
            this.baseMapper.deleteById(rolePermission.getId());
        }
    }

    //endregion
}
