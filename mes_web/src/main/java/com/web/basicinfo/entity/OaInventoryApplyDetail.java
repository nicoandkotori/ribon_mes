package com.web.basicinfo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("formson_0097")
public class OaInventoryApplyDetail extends Model<OaInventoryApplyDetail> {
    @TableId(value = "ID")
    private Long id;

    private Long formmainId;

    private Integer sort;

    private BigDecimal field0005;

    private String field0006;

    private String field0007;

    private String field0008;

    private String field0009;

    private String field0010;

    private String field0011;

    private String field0012;

    private String field0013;

    private BigDecimal field0014;

    private String field0015;

    private String field0016;

    private String field0017;

    private String field0022;

    private String field0024;

    @TableField(exist = false)
    private String surfaceMethod;

    @TableField(exist = false)
    private String mclass;

    @TableField(exist = false)
    private String izSelf;
    @TableField(exist = false)
    private String izPo;
    @TableField(exist = false)
    private String izForeign;
    @TableField(exist = false)
    private String izSale;
    @TableField(exist = false)
    private String className;




}