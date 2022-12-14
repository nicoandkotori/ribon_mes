package com.web.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.exception.BaseException;
import com.common.util.EncryptUtil;
import com.common.util.SnowFlakeUtils;
import com.common.util.StringUtils;
import com.modules.security.PreSecurityUser;
import com.modules.security.util.JwtUtil;
import com.web.system.dto.UserCurrent;
import com.web.system.entity.User;
import com.web.system.mapper.UserMapper;
import com.web.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private  JwtUtil jwtUtil;

	@Override
    public IPage<User> findUserNotInUserrole(Page<User> page1,User user ) {
        //List<User> list = this.userMapper.selectUserNotInUserrole(user);
		page1.setRecords(userMapper.selectUserNotInUserrole(page1, user));
		return page1;

    }

    public List<User> findUserListByRoleid(String roleid) {
		if (StringUtils.isNotBlank(roleid)) {
			List<User> list = this.userMapper.selectUserListByRoleid(roleid);
			return list;
		}
		return null;
	}

	/**
	 * ??????????????????
	 * @param user
	 * @return
	 */
	@Transactional
	public String saveUser(User user) {
		try {
			//1 ??????user  ????????????  ???????????????
			User entity=userMapper.selectUserByUsercode(user.getUserCode());
			if(entity!=null){
				throw new Exception("?????????????????????????????????????????????");
			}

			String userId =String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
			user.setId(userId);


			EncryptUtil encryptUtil=new EncryptUtil();
			String es= encryptUtil.encryptToSHA("8888");
			user.setPwd(es);


			return userId;
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}


	/**
	 * ????????????
	 * @param user
	 * @param password
	 * @param newPassword
	 * @return
	 */
	@Transactional
    public boolean modifyPassword(User user, String password, String newPassword) throws Exception{
		try{
			EncryptUtil encryptUtil=new EncryptUtil();
			String es= encryptUtil.encryptToSHA(password);
			if (!es.toLowerCase().equals(user.getPwd().toLowerCase())){
				throw new Exception("?????????????????????");
			}

			String newes = encryptUtil.encryptToSHA(newPassword);
			user.setPwd(newes);
			int row = this.userMapper.updatePasswordByUserId(user);
			if (row <= 0){
				throw new Exception("???????????????");
			}
		}catch (Exception e){
			throw e;
		}
		return false;
	}

	public int updateLastLoginInfo(User entity) {
		return 1;//userMapper.updateLastLoginInfo(entity);
	}



	/*****************************************************/
	@Override
	public String login(String username, String password) {
		//????????????
		Authentication authentication = null;
		try {
			// ?????????????????????UserDetailsServiceImpl.loadUserByUsername()??????????????????????????????
			// ?????????????????????????????????????????????security ??? context???
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception e) {
			if (e instanceof BadCredentialsException) {
				throw new BaseException("????????????????????????", 402);
			} else if (e instanceof DisabledException) {
				throw new BaseException("???????????????", 401);
			} else if (e instanceof AccountExpiredException) {
				throw new BaseException("????????????????????????", 403);
			} else {
				throw new BaseException("???????????????,????????????", 404);
			}
		}
		//??????????????????
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//??????token
		PreSecurityUser userDetail = (PreSecurityUser) authentication.getPrincipal();
		String token=JwtUtil.generateToken(userDetail);
		setUserToRedis(token,userDetail.getUserId());  //??????
		return JwtUtil.generateToken(userDetail);
	}


	@Override
	public User findSecurityUserByUser(User user) {
		LambdaQueryWrapper<User> select = Wrappers.<User>lambdaQuery()
				.select(User::getId, User::getUserCode, User::getUserName,User::getPwd, User::getGroupId)
		        .eq(User::getUserCode,user.getUserCode());

		if (StrUtil.isNotEmpty(user.getUserName())) {
			select=select.eq(User::getUserName, user.getUserName());
		}  else if (ObjectUtil.isNotNull(user.getId()) && StringUtils.isNotBlank(user.getId() )) {
			select=select.eq(User::getId, user.getId());
		}
		User ret=baseMapper.selectOne(select);
		return ret;
	}



	/**
	 * ??????????????????
	 * @param token
	 * @param userId
	 * @return
	 */
	@Override
	public  boolean setUserToRedis(String token, String userId){
		try{
			if(userId==null){
				//???null???  ???token????????? userid
				userId=jwtUtil.getUserFromToken(token).getUserId();

			}
			User user=baseMapper.selectById(userId);

			if(user!=null){
				UserCurrent userRedis=new UserCurrent();
				userRedis.setId(userId);
				userRedis.setIzAdmin(user.getGroupId());

				userRedis.setUserCode(user.getUserCode());
				userRedis.setUserName(user.getUserName());
				//userRedis.setAccount(user.getStoreName());

				//userRedis.setShopList();   ?????????????????? ???????????????
				redisTemplate.opsForValue().set(token,userRedis,3, TimeUnit.HOURS);  //3??????
				return true;
			}else{
				return false;
			}
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}

	public static void main(String[] args) {
		EncryptUtil encryptUtil=new EncryptUtil();
		System.out.println(encryptUtil.encryptToSHA("8888"));

	}

	/**
	 * ??????????????????
	 *
	 * @param userId
	 */
	@Override
	public String getUserRole(String userId) {
		return userMapper.getUserRole(userId);
	}
}
