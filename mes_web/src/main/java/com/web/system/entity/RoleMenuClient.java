package com.web.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role_menu_client")
public class RoleMenuClient extends Model<RoleMenuClient> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;

    private String roleId;

    private String menuId;


}