package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.SnowFlakeUtils;
import com.common.util.StringUtils;
import com.web.system.entity.RoleClient;
import com.web.system.entity.RoleMenuClient;
import com.web.system.entity.RoleUserClient;
import com.web.system.mapper.RoleClientMapper;
import com.web.system.mapper.RoleMenuClientMapper;
import com.web.system.mapper.RoleUserClientMapper;
import com.web.system.service.IRoleClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleClientServiceImpl extends ServiceImpl<RoleClientMapper, RoleClient> implements IRoleClientService {

	@Autowired
	private RoleClientMapper roleMapper;
	@Autowired
	private RoleUserClientMapper roleUserMapper;
	@Autowired
	private RoleMenuClientMapper roleMenuMapper;


	@Autowired
	RedisTemplate redisTemplate;

	@Transactional
	@Override
	public int saveOrUpdateRole(List<RoleClient> list) throws Exception {


		int count = 0;
		Integer roleSort = 0;
		for (RoleClient role : list) {
			//检查角色名称是否重复
			LambdaQueryWrapper<RoleClient> queryWrapper=new LambdaQueryWrapper();
			queryWrapper=queryWrapper.eq(RoleClient::getRoleName,role.getRoleName());
			if(StringUtils.isNotBlank(role.getId())){
				queryWrapper=queryWrapper.ne(RoleClient::getId,role.getId());
			}

			RoleClient duplicate = roleMapper.selectOne(queryWrapper);
			if(duplicate != null){
				continue;
			}
			roleSort++;
			role.setRoleSort(roleSort);
			RoleClient oldrole = this.getById(role.getId());
			if(oldrole == null){
				role.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
				boolean b= this.save(role);
				if(b) count+=1;
			}else{//角色已存在
				LambdaUpdateWrapper<RoleClient> update=new LambdaUpdateWrapper();
				update=update.eq(RoleClient::getId,role.getId());
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
		LambdaQueryWrapper<RoleUserClient> queryWrapper=new LambdaQueryWrapper();
		queryWrapper=queryWrapper.eq(RoleUserClient::getRoleId,id);
		this.roleUserMapper.delete(queryWrapper);
		//菜单权限
		LambdaQueryWrapper<RoleMenuClient> queryWrapper1=new LambdaQueryWrapper<>();
		queryWrapper1=queryWrapper1.eq(RoleMenuClient::getRoleId,id);
		this.roleMenuMapper.delete(queryWrapper1);
		//角色
		return this.deleteById(id);
	}
}
