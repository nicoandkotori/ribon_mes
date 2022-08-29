package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.dto.MenuTreeDTO;
import com.web.system.dto.UserCurrent;
import com.web.system.entity.Menu;
import com.web.system.entity.Role;

import java.util.List;

/**
 * <p>
 * 菜单  服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IRoleService extends IService<Role> {

    int saveOrUpdateRole(List<Role> list) throws Exception;
    int deleteById(String id);
}
