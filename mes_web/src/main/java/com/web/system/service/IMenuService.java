package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.dto.MenuDTO;
import com.web.system.dto.MenuTreeDTO;
import com.web.system.dto.UserCurrent;
import com.web.system.entity.Menu;

import java.util.List;

/**
 * <p>
 * 菜单  服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IMenuService extends IService<Menu> {

    //region select
    List<MenuTreeDTO> getMenuList(UserCurrent user) throws Exception ;

    List<MenuDTO> getAllMenuList();

    List<Menu> listByRoleId(String menuType,String roleId);

    //endregion
}
