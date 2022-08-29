package com.web.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class User extends Model<User> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;
    //@TableField("userName")
    private String userName;
    //@TableField("userCode")
    private String userCode;
    //@TableField("pwd")
    private String pwd;
    private String pwd1;
    //@TableField("groupId")
    private Integer groupId;
    //@TableField("depId")
    private String depId;
    //@TableField("depName")
    private String depName;
    //@TableField("lastLoginIp")
    private String lastLoginIp;
    //@TableField("lastLoginDate")
    private Date lastLoginDate;
    //@TableField("createUser")
    private String createUser;
    //@TableField("createDate")
    private Date createDate;
    //@TableField("updateUser")
    private String updateUser;
   // @TableField("updateDate")
    private Date updateDate;
    //@TableField("izDelete")
    private Boolean izDelete;


}