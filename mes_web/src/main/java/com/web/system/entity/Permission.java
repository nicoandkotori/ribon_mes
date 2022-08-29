package com.web.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author tao
 * @date 2020/3/9 15:30:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_permission")
public class Permission extends Model<Permission>{
    private static final long serialVersionUID = 1L;

    private String id;

    //按钮Id
    private String btnId;

    private String permissionName;

    private String permissionUrl;

    private String menuId;

    //按钮顺序
    private Integer btnSort;

    //方法名
    @TableField(exist = false)
    private String methodName;

    //是否有权限
    @TableField(exist = false)
    private boolean hasAuthority;

}
