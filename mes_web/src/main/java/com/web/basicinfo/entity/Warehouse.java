package com.web.basicinfo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("Warehouse")
public class Warehouse extends Model<Warehouse> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "cwhcode")
    private String cwhcode;

    private String cwhname;


}