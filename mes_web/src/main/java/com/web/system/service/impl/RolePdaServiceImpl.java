package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.SnowFlakeUtils;
import com.common.util.StringUtils;
import com.web.system.entity.RoleMenuPda;
import com.web.system.entity.RolePda;
import com.web.system.entity.RoleUserPda;
import com.web.system.mapper.RoleMenuPdaMapper;
import com.web.system.mapper.RolePdaMapper;
import com.web.system.mapper.RoleUserPdaMapper;
import com.web.system.service.IRolePdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolePdaServiceImpl extends ServiceImpl<RolePdaMapper, RolePda> implements IRolePdaService {

	@Autowired
	private RolePdaMapper roleMapper;
	@Autowired
	private RoleUserPdaMapper roleUserMapper;
	@Autowired
	private RoleMenuPdaMapper roleMenuMapper;


	@Autowired
	RedisTemplate redisTemplate;

	@Transactional
	@Override
	public int saveOrUpdateRole(List<RolePda> list) throws Exception {


		int count = 0;
		Integer roleSort = 0;
		for (RolePda role : list) {
			//检查角色名称是否重复
			LambdaQueryWrapper<RolePda> queryWrapper=new LambdaQueryWrapper();
			queryWrapper=queryWrapper.eq(RolePda::getRoleName,role.getRoleName());
			if(StringUtils.isNotBlank(role.getId())){
				queryWrapper=queryWrapper.ne(RolePda::getId,role.getId());
			}

			RolePda duplicate = roleMapper.selectOne(queryWrapper);
			if(duplicate != null){
				continue;
			}
			roleSort++;
			role.setRoleSort(roleSort);
			RolePda oldrole = this.getById(role.getId());
			if(oldrole == null){
				role.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
				boolean b= this.save(role);
				if(b) count+=1;
			}else{//角色已存在
				LambdaUpdateWrapper<RolePda> update=new LambdaUpdateWrapper();
				update=update.eq(RolePda::getId,role.getId());
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
		LambdaQueryWrapper<RoleUserPda> queryWrapper=new LambdaQueryWrapper();
		queryWrapper=queryWrapper.eq(RoleUserPda::getRoleId,id);
		this.roleUserMapper.delete(queryWrapper);
		//菜单权限
		LambdaQueryWrapper<RoleMenuPda> queryWrapper1=new LambdaQueryWrapper<>();
		queryWrapper1=queryWrapper1.eq(RoleMenuPda::getRoleId,id);
		this.roleMenuMapper.delete(queryWrapper1);
		//角色
		return this.deleteById(id);
	}
}
