package com.web.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单树DTO
 * </p>
 *
 * @author Mybatis Plus
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuTreeDTO {

    public MenuTreeDTO(){
        target = "_self";
    }

    private String id;
    private String menuCode;
    private String parentMenuCode;

    //@ApiModelProperty(notes = "菜单名称")
    private String title;

    //@ApiModelProperty(notes = "路径")
    private String href;
    //@ApiModelProperty(notes = "图标")
    private String icon;

    private String target;

    private List<MenuTreeDTO> child = new ArrayList<>();

}
