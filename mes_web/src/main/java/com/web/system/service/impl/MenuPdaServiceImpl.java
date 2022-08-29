package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.web.system.dto.MenuDTO;
import com.web.system.dto.MenuPdaVM;
import com.web.system.dto.MenuTreePdaDTO;
import com.web.system.dto.UserCurrent;
import com.web.system.entity.MenuPda;
import com.web.system.mapper.MenuPdaMapper;
import com.web.system.service.IMenuPdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuPdaServiceImpl extends ServiceImpl<MenuPdaMapper, MenuPda> implements IMenuPdaService {

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

	//region select
	@Override
	public List<MenuPda> listByRoleId(String roleId) {
		return baseMapper.listByRoleId(roleId);
	}

	@Override
	public List<MenuDTO> getAllMenuList() {
		List<MenuDTO> list = new ArrayList<MenuDTO>();
		//查询出所有菜单
		LambdaQueryWrapper<MenuPda> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByAsc(MenuPda::getPdaMenuCode);
		List<MenuPda> allList = baseMapper.selectList(queryWrapper);
		//1. 找出第一级 菜单  再一个一个循环
		List<MenuPda> parentMenuList = allList.stream()
				.filter(p -> CustomStringUtils.isBlank(p.getParentPdaMenuCode()))
				.collect(Collectors.toList());
		for (MenuPda parentMenu : parentMenuList) {
			MenuDTO parentMenuDTO = new MenuDTO();
			parentMenuDTO.setId(parentMenu.getId());
			parentMenuDTO.setMenuName(parentMenu.getPdaMenuName());
			parentMenuDTO.setMenuOrder(parentMenu.getPdaMenuOrder());
			parentMenuDTO.setMenuUrl(parentMenu.getPdaMenuUrl());
			parentMenuDTO.setMenuCode(parentMenu.getPdaMenuCode());
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
	private void getChildMenuList(MenuDTO parentMenuDTO, List<MenuDTO> list, List<MenuPda> allList) {
		List<MenuPda> childMenuList = allList.stream()
				.filter(g->CustomStringUtils.isNotBlank(g.getParentPdaMenuCode())
						&& g.getParentPdaMenuCode().equals(parentMenuDTO.getMenuCode()))
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
		for (MenuPda menu : childMenuList) {
			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setId(menu.getId());
			menuDTO.setMenuName(menu.getPdaMenuName());
			menuDTO.setMenuOrder(menu.getPdaMenuOrder());
			menuDTO.setMenuUrl(menu.getPdaMenuUrl());
			menuDTO.setMenuCode(menu.getPdaMenuCode());
			menuDTO.setParentMenuCode(menu.getParentPdaMenuCode());
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
	public List<MenuTreePdaDTO> getMenuList(UserCurrent user) throws Exception {
		if (user == null) {
			throw new Exception("获取登录信息失败！");
		}
		String userId = user.getId();

		if (user.getIzAdmin() == 1) {
			userId = "";
		}
		//返回的菜单
		List<MenuTreePdaDTO> parentMenuList = new ArrayList<>();
		//查询出 此用户授权的所有菜单
		List<MenuPda> authMenuList = baseMapper.selectAuthMenuByUserId( userId);
		//查询出所有菜单
		LambdaQueryWrapper<MenuPda> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByAsc(MenuPda::getPdaMenuCode);
		List<MenuPda> allList = baseMapper.selectList(queryWrapper);
		//1. 找出第一级 菜单  再一个一个循环
		List<MenuTreePdaDTO> firstMenuList = allList.stream().filter(p -> p.getParentPdaMenuCode() == null).map(tmp -> {
			MenuTreePdaDTO dto = new MenuTreePdaDTO();
			dto.setId(tmp.getId());
			dto.setMenuCode(tmp.getPdaMenuCode());
			dto.setIcon(tmp.getPdaMenuIcon());
			dto.setName(tmp.getPdaMenuName());
			dto.setParentMenuCode(tmp.getParentPdaMenuCode());
			dto.setUrl(tmp.getPdaMenuUrl());
			return dto;
		}).collect(Collectors.toList());

		for (MenuTreePdaDTO parentMenu : firstMenuList) {

			//有没有子菜单  在  授权的菜单中查找   如果有 放入菜单
			List<MenuTreePdaDTO> childMenuList = getChildMenuInAuthList(authMenuList, parentMenu.getMenuCode());
			if (childMenuList.size() > 0) {
				parentMenu.setChildren(childMenuList);
				parentMenuList.add(parentMenu);
			}

		}


		return parentMenuList;
	}

	private List<MenuTreePdaDTO> getChildMenuInAuthList(List<MenuPda> authList, String parentcode) {
		try {
			List<MenuTreePdaDTO> childMenuList = authList.stream().filter(p -> p.getParentPdaMenuCode() != null).filter(p -> p.getParentPdaMenuCode().equals(parentcode))
					.sorted(Comparator.comparing(MenuPda::getPdaMenuCode))
					.map(tmp -> {
						MenuTreePdaDTO dto = new MenuTreePdaDTO();
						dto.setId(tmp.getId());
						dto.setMenuCode(tmp.getPdaMenuCode());
						dto.setIcon(tmp.getPdaMenuIcon());
						dto.setName(tmp.getPdaMenuName());
						dto.setParentMenuCode(tmp.getParentPdaMenuCode());
						dto.setUrl(tmp.getPdaMenuUrl());
						return dto;
					}).collect(Collectors.toList());


			for (MenuTreePdaDTO menu : childMenuList) {
				List<MenuTreePdaDTO> childMenuList2 = getChildMenuInAuthList(authList, menu.getMenuCode());
				menu.setChildren(childMenuList2);
			}
			return childMenuList;
		} catch (RuntimeException e) {
			throw e;
		}
	}
	//endregion
}
