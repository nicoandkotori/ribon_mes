package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.dto.MenuDTO;
import com.web.system.dto.MenuPdaVM;
import com.web.system.dto.MenuTreePdaDTO;
import com.web.system.dto.UserCurrent;
import com.web.system.entity.MenuPda;

import java.util.List;

/**
 * <p>
 * 菜单  服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IMenuPdaService extends IService<MenuPda> {

    List<MenuPdaVM> getPdaMenuList(String menuName, String userId) throws Exception ;
    //region select
    List<MenuTreePdaDTO> getMenuList(UserCurrent user) throws Exception ;

    List<MenuDTO> getAllMenuList();

    List<MenuPda> listByRoleId(String roleId);

    //endregion
}
