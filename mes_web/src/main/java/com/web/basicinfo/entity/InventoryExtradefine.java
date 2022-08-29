package com.web.basicinfo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Inventory_extradefine")
public class InventoryExtradefine extends Model<InventoryExtradefine> {
    @TableId(value = "cInvCode")
    private String cinvcode;

    private String cidefine1;
}