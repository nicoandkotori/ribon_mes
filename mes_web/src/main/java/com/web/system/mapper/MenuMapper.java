package com.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.system.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> listByRoleId(@Param("menuType") String menuType, @Param("roleId") String roleId);

    /**
     *
     * @param menuType  菜单类型
     * @param userId   当前用户ID
     * @return
     */
    List<Menu> selectAuthMenuByUserId(@Param("menuType") String menuType,@Param("userId") String userId);

}
