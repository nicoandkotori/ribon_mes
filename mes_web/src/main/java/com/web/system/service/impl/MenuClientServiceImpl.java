package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.web.system.dto.MenuDTO;
import com.web.system.dto.MenuPdaVM;
import com.web.system.entity.MenuClient;
import com.web.system.mapper.MenuClientMapper;
import com.web.system.mapper.MenuPdaMapper;
import com.web.system.service.IMenuClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuClientServiceImpl extends ServiceImpl<MenuClientMapper, MenuClient> implements IMenuClientService {

	@Autowired
	private MenuPdaMapper menuPdaMapper;



	/**
	 * 查询PDA的菜单 ， 只有一级， 所有不用递归。
	 * @param menuName
	 * @param userId
	 * @return
	 */
	@Override
	public List<MenuPdaVM> getPdaMenuList(String menuName,String userId){
		List<MenuPdaVM> list = menuPdaMapper.getPdaMenu(menuName, userId);
		return  list;
	}



	@Override
	public List<MenuDTO> getAllMenuList() {
		List<MenuDTO> list = new ArrayList<MenuDTO>();
		//查询出所有菜单
		LambdaQueryWrapper<MenuClient> queryWrapper = new LambdaQueryWrapper<>();

		queryWrapper.orderByAsc(MenuClient::getId);
		List<MenuClient> allList = baseMapper.selectList(queryWrapper);
		//1. 找出第一级 菜单  再一个一个循环
		List<MenuClient> parentMenuList = allList.stream()
				.filter(p -> CustomStringUtils.isBlank(p.getParentId()))
				.collect(Collectors.toList());
		for (MenuClient parentMenu : parentMenuList) {
			MenuDTO parentMenuDTO = new MenuDTO();
			parentMenuDTO.setId(parentMenu.getId());
			parentMenuDTO.setMenuName(parentMenu.getMenuName());
			parentMenuDTO.setMenuOrder(parentMenu.getMenuOrder());
			parentMenuDTO.setMenuUrl(parentMenu.getMenuUrl());
			parentMenuDTO.setMenuCode("");
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
	private void getChildMenuList(MenuDTO parentMenuDTO, List<MenuDTO> list, List<MenuClient> allList) {
		List<MenuClient> childMenuList = allList.stream()
				.filter(g->CustomStringUtils.isNotBlank(g.getParentId())
						&& g.getParentId().equals(parentMenuDTO.getId()))
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
		for (MenuClient menu : childMenuList) {
			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setId(menu.getId());
			menuDTO.setMenuName(menu.getMenuName());
			menuDTO.setMenuOrder(menu.getMenuOrder());
			menuDTO.setMenuUrl(menu.getMenuUrl());
			menuDTO.setMenuCode("");
			menuDTO.setParentMenuCode(menu.getParentId());
			menuDTO.setExpanded(true);
			menuDTO.setLoaded(true);
			menuDTO.setMenulevel(parentMenuDTO.getMenulevel()+1);
			list.add(menuDTO);

			getChildMenuList(menuDTO, list, allList);
		}
	}

}
