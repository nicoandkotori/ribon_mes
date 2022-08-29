package com.web.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 菜单信息表
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_menu_pda")
public class MenuPda extends Model<MenuPda> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;
    private String pdaMenuCode;
    private String parentPdaMenuCode;
    private String pdaMenuName;
    private String pdaMenuOrder;
    private String pdaMenuUrl;
    private String pdaMenuIcon;



    @TableField(exist = false)
    private List<MenuPda> menus;
}
