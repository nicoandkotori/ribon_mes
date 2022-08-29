package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.dto.MenuDTO;
import com.web.system.dto.MenuPdaVM;
import com.web.system.entity.MenuClient;

import java.util.List;

/**
 * <p>
 * 菜单  服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IMenuClientService extends IService<MenuClient> {
    List<MenuPdaVM> getPdaMenuList(String menuName, String userId) throws Exception ;

    List<MenuDTO> getAllMenuList();
}
