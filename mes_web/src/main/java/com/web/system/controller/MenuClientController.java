package com.web.system.controller;

import com.modules.security.util.JwtUtil;
import com.web.system.dto.MenuDTO;
import com.web.system.service.IMenuClientService;
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
@RequestMapping(value = "/sysinfo/menuclient")
public class MenuClientController {

    //region autowired
    @Autowired
    private IMenuClientService menuService;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    //endregion

    //region select


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
