package com.web.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MenuTreePdaDTO {

    private String id;
    private String menuCode;
    private String parentMenuCode;

    //@ApiModelProperty(notes = "菜单名称")
    private String name;

    //@ApiModelProperty(notes = "路径")
    private String url;
    //@ApiModelProperty(notes = "图标")
    private String icon;

    private List<MenuTreePdaDTO> children = new ArrayList<>();

}
