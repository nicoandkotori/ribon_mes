package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.SnowFlakeUtils;
import com.common.util.StringUtils;
import com.web.system.entity.RoleUserClient;
import com.web.system.entity.User;
import com.web.system.mapper.RoleClientMapper;
import com.web.system.mapper.RoleMenuClientMapper;
import com.web.system.mapper.RoleUserClientMapper;
import com.web.system.service.IRoleUserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleUserClientServiceImpl extends ServiceImpl<RoleUserClientMapper, RoleUserClient> implements IRoleUserClientService {

	@Autowired
	private RoleClientMapper roleMapper;
	@Autowired
	private RoleUserClientMapper roleUserMapper;
	@Autowired
	private RoleMenuClientMapper roleMenuMapper;


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

			LambdaQueryWrapper<RoleUserClient> queryWrapper=new LambdaQueryWrapper<>();
			queryWrapper=queryWrapper.eq(RoleUserClient::getUserId,user.getId());
			queryWrapper=queryWrapper.eq(RoleUserClient::getRoleId,roleId);

			RoleUserClient roleUserResult=roleUserMapper.selectOne(queryWrapper);
			if(roleUserResult==null){
				RoleUserClient roleUser = new RoleUserClient();
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
		LambdaQueryWrapper<RoleUserClient> queryWrapper1=new LambdaQueryWrapper<>();
		queryWrapper1=queryWrapper1.eq(RoleUserClient::getRoleId,roleId);
		queryWrapper1=queryWrapper1.eq(RoleUserClient::getUserId,userId);

		return this.roleUserMapper.delete(queryWrapper1);
	}

	//endregion
}
