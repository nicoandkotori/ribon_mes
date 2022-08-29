package com.web.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.common.exception.BaseException;
import com.common.util.CustomStringUtils;
import com.common.util.ResponseResult;
import com.modules.security.util.SecurityUtil;
import com.web.system.dto.MenuDTO;
import com.web.system.entity.Permission;
import com.web.system.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tao
 * @date 2020/3/10 15:34:35
 */
@RestController
@RequestMapping("/sys/permission")
public class PermissionController {

    //region autowired
    @Autowired
    private IPermissionService permissionService;

    //endregion

    //region select
    @RequestMapping(value = "/listbyroleid")
    public List<MenuDTO> listByRoleId(String roleId) {
        if (CustomStringUtils.isEmpty(roleId)) {
            throw new BaseException("请选择一个角色");
        }
        return this.permissionService.listByRoleId(roleId);
    }

    @RequestMapping("/getbymenuid")
    public ResponseResult getAuthorityBtn(String query, String menuId) {
        ResponseResult result = new ResponseResult();
        List<Permission> permissions = JSONArray.parseArray(query, Permission.class);
        List<Permission> resultList = new ArrayList<>();
        List<Permission> permissionList = permissionService.listByMenuIdAndUserId(
                SecurityUtil.getUser().getUserId(), menuId);
        for (Permission permission : permissions) {
            boolean hasAuthority = false;
            long count = permissionList.stream()
                    .filter(g -> g.getPermissionUrl().equals(permission.getPermissionUrl()))
                    .count();
            if (count > 0) {
                hasAuthority = true;
            }
            permission.setHasAuthority(hasAuthority);
            resultList.add(permission);
        }
        result.setSuccess(true);
        result.setResult(resultList);
        return result;
    }

    //endregion

    //region insert update

    //endregion

}
