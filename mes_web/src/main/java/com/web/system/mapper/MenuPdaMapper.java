package com.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.system.dto.MenuPdaVM;
import com.web.system.entity.MenuPda;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuPdaMapper extends BaseMapper<MenuPda> {

    //PDA查找个模块下的菜单 ， 只有一层。
    List<MenuPdaVM> getPdaMenu(@Param("menuName") String menuName, @Param("userId") String userId);


    List<MenuPda> listByRoleId(@Param("roleId") String roleId);

    /**
     *

     * @param userId   当前用户ID
     * @return
     */
    List<MenuPda> selectAuthMenuByUserId(@Param("userId") String userId);
}