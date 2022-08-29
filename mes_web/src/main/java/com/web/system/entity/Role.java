package com.web.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 菜单信息表
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role")
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;
    private Integer roleSort;

    private String roleName;
    private String roleType;

    private String remarks;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private int izDelete;
    private String deleteUser;
    private Date deleteDate;
}
