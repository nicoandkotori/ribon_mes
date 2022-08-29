package com.web.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.SnowFlakeUtils;
import com.common.util.StringUtils;
import com.modules.security.LoginType;
import com.web.system.dto.MenuTreeDTO;
import com.web.system.dto.UserCurrent;
import com.web.system.entity.Menu;
import com.web.system.entity.Role;
import com.web.system.entity.RoleMenu;
import com.web.system.entity.RoleUser;
import com.web.system.mapper.MenuMapper;
import com.web.system.mapper.RoleMapper;
import com.web.system.mapper.RoleMenuMapper;
import com.web.system.mapper.RoleUserMapper;
import com.web.system.service.IMenuService;
import com.web.system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleUserMapper roleUserMapper;
	@Autowired
	private RoleMenuMapper roleMenuMapper;


	@Autowired
	RedisTemplate redisTemplate;

	@Transactional
	@Override
	public int saveOrUpdateRole(List<Role> list) throws Exception {


		int count = 0;
		Integer roleSort = 0;
		for (Role role : list) {
			//检查角色名称是否重复
			LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper();
			queryWrapper=queryWrapper.eq(Role::getRoleName,role.getRoleName());
			if(StringUtils.isNotBlank(role.getId())){
				queryWrapper=queryWrapper.ne(Role::getId,role.getId());
			}

			Role duplicate = roleMapper.selectOne(queryWrapper);
			if(duplicate != null){
				continue;
			}
			roleSort++;
			role.setRoleSort(roleSort);
			Role oldrole = this.getById(role.getId());
			if(oldrole == null){
				role.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
				boolean b= this.save(role);
				if(b) count+=1;
			}else{//角色已存在
				LambdaUpdateWrapper<Role> update=new LambdaUpdateWrapper();
				update=update.eq(Role::getId,role.getId());
				boolean b=  this.update(role,update);
				if(b) count+=1;
			}
		}
		return count;
	}

	/**
	 * 删除角色，  同时删除  与角色相关的   角色人员  和角色菜单 信息
	 * @param id
	 * @return
	 */
	@Transactional
	@Override
	public int deleteById(String id) {

		//用户
		LambdaQueryWrapper<RoleUser> queryWrapper=new LambdaQueryWrapper();
		queryWrapper=queryWrapper.eq(RoleUser::getRoleId,id);
		this.roleUserMapper.delete(queryWrapper);
		//菜单权限
		LambdaQueryWrapper<RoleMenu> queryWrapper1=new LambdaQueryWrapper<>();
		queryWrapper1=queryWrapper1.eq(RoleMenu::getRoleId,id);
		this.roleMenuMapper.delete(queryWrapper1);
		//角色
		return this.deleteById(id);
	}
}
