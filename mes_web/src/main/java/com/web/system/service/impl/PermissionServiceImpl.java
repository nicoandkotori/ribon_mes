package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.modules.security.LoginType;
import com.web.system.dto.MenuDTO;
import com.web.system.entity.Menu;
import com.web.system.entity.Permission;
import com.web.system.entity.User;
import com.web.system.mapper.PermissionMapper;
import com.web.system.service.IMenuService;
import com.web.system.service.IPermissionService;
import com.web.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tao
 * @date 2020/3/9 15:35:16
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper,Permission> implements IPermissionService{

    //region autowired
    @Autowired
    private IUserService userService;
    @Autowired
    private IMenuService menuService;

    //endregion

    //region select
    @Override
    public List<MenuDTO> listByRoleId(String roleId) {
        List<MenuDTO> list = new ArrayList<MenuDTO>();
        List<Menu> allList = menuService.listByRoleId(LoginType.pc.name(),roleId);
        //1. 找出第一级 菜单  再一个一个循环
        List<Menu> parentMenuList = allList.stream()
                .filter(p -> CustomStringUtils.isBlank(p.getParentMenuCode()))
                .collect(Collectors.toList());
        for (Menu parentMenu : parentMenuList) {
            MenuDTO parentMenuDTO = new MenuDTO();
            parentMenuDTO.setId(parentMenu.getId());
            parentMenuDTO.setMenuName(parentMenu.getMenuName());
            parentMenuDTO.setMenuOrder(parentMenu.getMenuOrder());
            parentMenuDTO.setMenuUrl(parentMenu.getMenuUrl());
            parentMenuDTO.setMenuCode(parentMenu.getMenuCode());
            parentMenuDTO.setExpanded(true);
            parentMenuDTO.setLoaded(true);
            parentMenuDTO.setMenulevel(1);
            list.add(parentMenuDTO);

            getChildMenuList(parentMenuDTO, list, allList);
        }
        return list;
    }

    /**
     * 查找子菜单，以及子菜单下的子菜单
     *
     * @param
     * @return
     */
    private void getChildMenuList(MenuDTO parentMenu, List<MenuDTO> list, List<Menu> allList) {
        List<Menu> childMenuList = allList.stream()
                .filter(g->CustomStringUtils.isNotBlank(g.getParentMenuCode())
                && g.getParentMenuCode().equals(parentMenu.getMenuCode()))
                .collect(Collectors.toList());
        if (childMenuList != null) {
            if (childMenuList.size() > 0) {
                parentMenu.setIsleaf(false);
            } else {
                parentMenu.setIsleaf(true);
            }
        } else {
            parentMenu.setIsleaf(true);
        }
        for (Menu menu : childMenuList) {
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setId(menu.getId());
            menuDTO.setMenuName(menu.getMenuName());
            menuDTO.setMenuOrder(menu.getMenuOrder());
            menuDTO.setMenuUrl(menu.getMenuUrl());
            menuDTO.setMenuCode(menu.getMenuCode());
            menuDTO.setParentMenuCode(menu.getParentMenuCode());
            menuDTO.setExpanded(true);
            menuDTO.setLoaded(true);
            menuDTO.setMenulevel(parentMenu.getMenulevel() + 1);
            list.add(menuDTO);

            List<Permission> actionList = this.getAllActionList(menu.getId());
            menuDTO.setPermissions(actionList);
            getChildMenuList(menuDTO, list, allList);
        }
    }

    private List<Permission> getAllActionList(String menuid) {
        if (CustomStringUtils.isEmpty(menuid)) {
            return null;
        }
        LambdaQueryWrapper<Permission> select = new LambdaQueryWrapper<>();
        select = select.eq(Permission::getMenuId,menuid);
        return baseMapper.selectList(select);
    }

    @Override
    public List<Permission> listByMenuIdAndUserId(String userId, String menuId) {
        if(CustomStringUtils.isBlank(menuId)){
            return new ArrayList<Permission>();
        }
        List<Permission> permissions = this.listByUserId(userId);
        List<Permission> collect = permissions.stream()
                .filter(g -> g.getMenuId().equals(menuId))
                .collect(Collectors.toList());
        //按顺序排序
        collect = collect.stream()
                .sorted(Comparator.comparing(Permission::getBtnSort))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Permission> listByUserId(String userId) {
        User user = userService.getById(userId);
        //管理员拥有所有权限
        if(user.getGroupId() == 1){
            return baseMapper.selectList(null);
        }
        return baseMapper.listByUserId(userId);
    }

    //endregion

}
