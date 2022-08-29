package com.web.basicinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("InventoryClass")
public class InventoryClass extends Model<Inventory> {

    @TableField(value="cInvCCode")
    private String cInvCCode;
    @TableField(value="cInvCName")
    private String cInvCName;
    @TableField(value="iInvCGrade")
    private Integer iInvCGrade;
    @TableField(value="bInvCEnd")
    private  Boolean bInvCEnd;

}
