package com.web.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.SnowFlakeUtils;
import com.common.util.StringUtils;
import com.web.system.entity.Menu;
import com.web.system.entity.RoleMenu;
import com.web.system.mapper.RoleMenuMapper;
import com.web.system.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {


	@Autowired
	private RoleMenuMapper roleMenuMapper;



	//region select
	public List<RoleMenu> findRoleMenuByRoleId(String roleId) {

		LambdaQueryWrapper<RoleMenu> queryWrapper=new LambdaQueryWrapper();
		queryWrapper=queryWrapper.eq(RoleMenu::getRoleId,roleId);
		return this.roleMenuMapper.selectList(queryWrapper);
	}

	@Transactional
	public boolean saveOrDeleteRoleMenus(String roleId,  List<String> list) {

		// 1   先全部删除    2   再循环插入
		LambdaQueryWrapper<RoleMenu> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper=queryWrapper.eq(RoleMenu::getRoleId,roleId);

		int n=roleMenuMapper.delete(queryWrapper);
		//删除完成后 再循环插入。
		List<RoleMenu> roleMenuList=new ArrayList<>();
		for (int i=0; i<list.size();i++) {
			RoleMenu rm=new RoleMenu();
			rm.setRoleId(roleId);
			rm.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
			rm.setMenuId(list.get(i));
			roleMenuList.add(rm);

		}
		boolean b=	this.saveBatch(roleMenuList);

		return b;
	}

	//endregion
}
