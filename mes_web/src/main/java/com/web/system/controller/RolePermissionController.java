package com.web.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.util.CustomStringUtils;
import com.web.system.entity.RolePermission;
import com.web.system.service.IRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tao
 * @date 2020/3/10 17:45:27
 */
@RestController
@RequestMapping("/sys/rolepermission")
public class RolePermissionController {

    //region autowired
    @Autowired
    private IRolePermissionService rolePermissionService;

    //endregion

    //region select
    @RequestMapping(value = "/getbyroleid")
    @ResponseBody
    public List<RolePermission> getByRoleid(String roleId) {
        LambdaQueryWrapper<RolePermission> param = new LambdaQueryWrapper<RolePermission>();
        param = param.eq(RolePermission::getRoleId,roleId);
        return this.rolePermissionService.list(param);
    }

    //endregion

    //region insert update
    @RequestMapping(value = "/saveorupdate")
    @ResponseBody
    public Map<String, Object> saveOrUpdate(String mdatas, String roleId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONArray actionidArr = JSON.parseArray(mdatas);
            if (CustomStringUtils.isEmpty(roleId)) {
                throw new Exception("请选择一个角色！");
            }
            List<RolePermission> list = new ArrayList<RolePermission>();
            for (Object actionid : actionidArr) {
                RolePermission query = new RolePermission();
                query.setPermissionId((String) actionid);
                query.setRoleId(roleId);
                list.add(query);
            }
            this.rolePermissionService.saveOrDeleteRolePermissions(roleId, list);
            map.put("success", true);
        } catch (Exception e) {
            map.put("success", false);
            map.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    //endregion

}
