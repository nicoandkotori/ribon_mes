package com.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.SnowFlakeUtils;
import com.common.util.StringUtils;
import com.web.system.entity.RoleUserPda;
import com.web.system.entity.User;
import com.web.system.mapper.RoleMenuPdaMapper;
import com.web.system.mapper.RolePdaMapper;
import com.web.system.mapper.RoleUserPdaMapper;
import com.web.system.service.IRoleUserPdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleUserPdaServiceImpl extends ServiceImpl<RoleUserPdaMapper, RoleUserPda> implements IRoleUserPdaService {

	@Autowired
	private RolePdaMapper roleMapper;
	@Autowired
	private RoleUserPdaMapper roleUserMapper;
	@Autowired
	private RoleMenuPdaMapper roleMenuMapper;


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

			LambdaQueryWrapper<RoleUserPda> queryWrapper=new LambdaQueryWrapper<>();
			queryWrapper=queryWrapper.eq(RoleUserPda::getUserId,user.getId());
			queryWrapper=queryWrapper.eq(RoleUserPda::getRoleId,roleId);

			RoleUserPda roleUserResult=roleUserMapper.selectOne(queryWrapper);
			if(roleUserResult==null){
				RoleUserPda roleUser = new RoleUserPda();
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
		LambdaQueryWrapper<RoleUserPda> queryWrapper1=new LambdaQueryWrapper<>();
		queryWrapper1=queryWrapper1.eq(RoleUserPda::getRoleId,roleId);
		queryWrapper1=queryWrapper1.eq(RoleUserPda::getUserId,userId);

		return this.roleUserMapper.delete(queryWrapper1);
	}

	//endregion
}
