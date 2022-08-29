package com.modules.security;

import cn.hutool.core.util.ObjectUtil;
import com.web.system.entity.Permission;
import com.web.system.entity.User;
import com.web.system.service.IPermissionService;
import com.web.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname UserDetailsServiceImpl  c
 * @Description 用户身份验证   crm 的身份验证
 * @Author
 * @Date
 * @Version 1.0
 */
@Slf4j
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;
    @Autowired
    private IPermissionService permissionService;
    /**
     * 用户名密码登录
     *
     * @param usercode
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String usercode) throws UsernameNotFoundException {
        User user = new User();
        user.setUserCode(usercode);
        User retUser = userService.findSecurityUserByUser(user);
        if (ObjectUtil.isNull(retUser)) {
            log.info("登录用户：" + usercode + " 不存在.");
            throw new UsernameNotFoundException("登录用户：" + usercode + " 不存在");
        }
        Collection<? extends GrantedAuthority> authorities = getUserAuthorities(retUser.getId());

        return new PreSecurityUser(retUser.getId(), usercode, usercode,retUser.getUserName(), retUser.getPwd(),  authorities, LoginType.pc);
    }

    /**
     * 封装 根据用户Id获取权限
     *
     * @param userId
     * @return
     */
    private Collection<? extends GrantedAuthority> getUserAuthorities(String userId) {
        // 获取用户拥有的角色
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        // 权限集合
        List<Permission> permissions = permissionService.listByUserId(userId);
        List<String> permissionUrls = permissions.stream()
                .map(Permission::getPermissionUrl)
                .collect(Collectors.toList());
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissionUrls.toArray(new String[0]));
        return authorities;
    }
}
