package com.web.system.dto;

import com.web.system.entity.Permission;
import lombok.Data;

import java.util.List;

/**
 * 菜单信息表
 * Created by sunyin on 2018/3/1.
 */
@Data
public class MenuDTO {

    private String id;
    private String menuCode;
    private String parentMenuCode;
    private String menuName;
    private String menuOrder;
    private String menuUrl;
    private String menuIcon;

    private Boolean isleaf;
    private Boolean loaded;
    private Boolean expanded;
    private Integer menulevel;

    private List<MenuDTO> menus;
    private List<Permission> permissions;



}
