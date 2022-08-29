package com.web.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.SnowFlakeUtils;
import com.common.util.StringUtils;
import com.web.system.entity.Role;
import com.web.system.entity.RoleMenu;
import com.web.system.entity.RoleUser;
import com.web.system.entity.User;
import com.web.system.mapper.RoleMapper;
import com.web.system.mapper.RoleMenuMapper;
import com.web.system.mapper.RoleUserMapper;
import com.web.system.service.IRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements IRoleUserService {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleUserMapper roleUserMapper;
	@Autowired
	private RoleMenuMapper roleMenuMapper;


	//region insert update
	@Transactional
	@Override
	public int saveUsers (List<User> list, String roleId){

		int count = 0;
		for (User user : list) {
			String userid = user.getId();
			if (StringUtils.isEmpty(userid)) {
				continue;
			}

			LambdaQueryWrapper<RoleUser> queryWrapper=new LambdaQueryWrapper<>();
			queryWrapper=queryWrapper.eq(RoleUser::getUserId,user.getId());
			queryWrapper=queryWrapper.eq(RoleUser::getRoleId,roleId);

			RoleUser roleUserResult=roleUserMapper.selectOne(queryWrapper);
			if(roleUserResult==null){
				RoleUser roleUser = new RoleUser();
				roleUser.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
				roleUser.setUserId(userid);
				roleUser.setRoleId(roleId);
				count += this.roleUserMapper.insert(roleUser);
			}

		}
		return count;
	}

	/**
	 *  删除 此角色中  相应的  人员
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@Transactional
	@Override
	public int deleteUser(String userId, String roleId) {
		LambdaQueryWrapper<RoleUser> queryWrapper1=new LambdaQueryWrapper<>();
		queryWrapper1=queryWrapper1.eq(RoleUser::getRoleId,roleId);
		queryWrapper1=queryWrapper1.eq(RoleUser::getUserId,userId);

		return this.roleUserMapper.delete(queryWrapper1);
	}

	//endregion
}
