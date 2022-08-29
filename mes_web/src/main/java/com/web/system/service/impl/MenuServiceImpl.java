package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.modules.security.LoginType;
import com.web.system.dto.MenuDTO;
import com.web.system.dto.MenuTreeDTO;
import com.web.system.dto.UserCurrent;
import com.web.system.entity.Menu;
import com.web.system.mapper.MenuMapper;
import com.web.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    //region autowired
    @Autowired
    RedisTemplate redisTemplate;

    //endregion

    //region select
    @Override
    public List<Menu> listByRoleId(String menuType, String roleId) {
        return baseMapper.listByRoleId(menuType,roleId);
    }

    @Override
    public List<MenuDTO> getAllMenuList() {
        List<MenuDTO> list = new ArrayList<MenuDTO>();
        //查询出所有菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Menu::getMenuType, LoginType.pc.name());
        queryWrapper.orderByAsc(Menu::getMenuCode);
        List<Menu> allList = baseMapper.selectList(queryWrapper);
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

            getChildMenuList(parentMenuDTO, list,allList);
        }
        return list;
    }

    /**
     * 查找子菜单，以及子菜单下的子菜单
     *
     * @return
     */
    private void getChildMenuList(MenuDTO parentMenuDTO, List<MenuDTO> list, List<Menu> allList) {
        List<Menu> childMenuList = allList.stream()
                .filter(g->CustomStringUtils.isNotBlank(g.getParentMenuCode())
                        && g.getParentMenuCode().equals(parentMenuDTO.getMenuCode()))
                .collect(Collectors.toList());
        if (childMenuList != null) {
            if (childMenuList.size() > 0) {
                parentMenuDTO.setIsleaf(false);
            } else {
                parentMenuDTO.setIsleaf(true);
            }
        } else {
            parentMenuDTO.setIsleaf(true);
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
            menuDTO.setMenulevel(parentMenuDTO.getMenulevel()+1);
            list.add(menuDTO);

            getChildMenuList(menuDTO, list, allList);
        }
    }

    /**
     * 电脑端 主界面  根据用户的权限 查找主菜单  （管理员 加载全部菜单）
     *
     * @throws Exception
     */
    @Override
    public List<MenuTreeDTO> getMenuList(UserCurrent user) throws Exception {
        if (user == null) {
            throw new Exception("获取登录信息失败！");
        }
        String userId = user.getId();

        if (user.getIzAdmin() == 1) {
            userId = "";
        }
        //返回的菜单
        List<MenuTreeDTO> parentMenuList = new ArrayList<>();
        //查询出 此用户授权的所有菜单
        List<Menu> authMenuList = baseMapper.selectAuthMenuByUserId(LoginType.pc.name(), userId);
        //查询出所有菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Menu::getMenuType, LoginType.pc.name());
        queryWrapper.orderByAsc(Menu::getMenuCode);
        List<Menu> allList = baseMapper.selectList(queryWrapper);
        //1. 找出第一级 菜单  再一个一个循环
        List<MenuTreeDTO> firstMenuList = allList.stream().filter(p -> p.getParentMenuCode() == null).map(tmp -> {
            MenuTreeDTO dto = new MenuTreeDTO();
            dto.setId(tmp.getId());
            dto.setMenuCode(tmp.getMenuCode());
            dto.setIcon(tmp.getMenuIcon());
            dto.setTitle(tmp.getMenuName());
            dto.setParentMenuCode(tmp.getParentMenuCode());
            dto.setHref(tmp.getMenuUrl());
            return dto;
        }).collect(Collectors.toList());

        for (MenuTreeDTO parentMenu : firstMenuList) {

            //有没有子菜单  在  授权的菜单中查找   如果有 放入菜单
            List<MenuTreeDTO> childMenuList = getChildMenuInAuthList(authMenuList, parentMenu.getMenuCode());
            if (childMenuList.size() > 0) {
                parentMenu.setChild(childMenuList);
                parentMenuList.add(parentMenu);
            }

        }


        return parentMenuList;
    }

    private List<MenuTreeDTO> getChildMenuInAuthList(List<Menu> authList, String parentcode) {
        try {
            List<MenuTreeDTO> childMenuList = authList.stream().filter(p -> p.getParentMenuCode() != null).filter(p -> p.getParentMenuCode().equals(parentcode))
                    .sorted(Comparator.comparing(Menu::getMenuCode))
                    .map(tmp -> {
                        MenuTreeDTO dto = new MenuTreeDTO();
                        dto.setId(tmp.getId());
                        dto.setMenuCode(tmp.getMenuCode());
                        dto.setIcon(tmp.getMenuIcon());
                        dto.setTitle(tmp.getMenuName());
                        dto.setParentMenuCode(tmp.getParentMenuCode());
                        dto.setHref(tmp.getMenuUrl());
                        return dto;
                    }).collect(Collectors.toList());


            for (MenuTreeDTO menu : childMenuList) {
                List<MenuTreeDTO> childMenuList2 = getChildMenuInAuthList(authList, menu.getMenuCode());
                menu.setChild(childMenuList2);
            }
            return childMenuList;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //endregion
}
