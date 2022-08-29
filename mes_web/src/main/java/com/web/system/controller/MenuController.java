package com.web.system.controller;

import com.modules.security.PreSecurityUser;
import com.modules.security.util.JwtUtil;
import com.modules.security.util.SecurityUtil;
import com.web.system.dto.MenuDTO;
import com.web.system.dto.MenuTreeDTO;
import com.web.system.dto.UserCurrent;
import com.web.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 菜单信息
 *
 */
@RestController
@RequestMapping(value = "/sysinfo/menu")
public class MenuController {

    //region autowired
    @Autowired
    private IMenuService menuService;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    //endregion

    //region select
    @GetMapping(value = "/getmenu")
    public List<MenuTreeDTO> getUserMenu(HttpServletRequest request){
        List<MenuTreeDTO> menuTreeDTOS=new ArrayList<>();
        PreSecurityUser securityUser = SecurityUtil.getUser();
        if(securityUser == null){
            return null;
        }
        try {
            String token= jwtUtil.getToken(request);
            UserCurrent userCurrent=(UserCurrent)redisTemplate.opsForValue().get(token);
            menuTreeDTOS = menuService.getMenuList(userCurrent);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return menuTreeDTOS;
    }

    @GetMapping(value = "/getallmenu")
    public List<MenuDTO> getMenu(HttpServletRequest request){
        List<MenuDTO> list=new ArrayList<MenuDTO>();
        try {
            list = menuService.getAllMenuList();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }

    //endregion

}
