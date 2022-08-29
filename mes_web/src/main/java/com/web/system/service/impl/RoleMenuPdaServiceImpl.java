package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.SnowFlakeUtils;
import com.web.system.entity.RoleMenuPda;
import com.web.system.mapper.RoleMenuPdaMapper;
import com.web.system.service.IRoleMenuPdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class RoleMenuPdaServiceImpl extends ServiceImpl<RoleMenuPdaMapper, RoleMenuPda> implements IRoleMenuPdaService {


	@Autowired
	private RoleMenuPdaMapper roleMenuMapper;



	//region select
	public List<RoleMenuPda> findRoleMenuByRoleId(String roleId) {

		LambdaQueryWrapper<RoleMenuPda> queryWrapper=new LambdaQueryWrapper();
		queryWrapper=queryWrapper.eq(RoleMenuPda::getRoleId,roleId);
		return this.roleMenuMapper.selectList(queryWrapper);
	}

	@Transactional
	public boolean saveOrDeleteRoleMenus(String roleId,  List<String> list) {

		// 1   先全部删除    2   再循环插入
		LambdaQueryWrapper<RoleMenuPda> queryWrapper=new LambdaQueryWrapper<>();
		queryWrapper=queryWrapper.eq(RoleMenuPda::getRoleId,roleId);

		int n=roleMenuMapper.delete(queryWrapper);
		//删除完成后 再循环插入。
		List<RoleMenuPda> roleMenuList=new ArrayList<>();
		for (int i=0; i<list.size();i++) {
			RoleMenuPda rm=new RoleMenuPda();
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
