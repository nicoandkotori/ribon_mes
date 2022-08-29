package com.web.om.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("om_order_material")
public class OmOrderMaterial {
    @TableId(value = "id")
    private String id;

    private String detailId;

    private String partId;

    private String mainId;

    private String invCode;

    private String invName;

    private String invStd;

    private String invUnit;

    private BigDecimal qty;

    private BigDecimal unitMaterialPrice;

    private BigDecimal unitMaterialAmount;

    private String invLand;

    private String invWidth;

    private String invLen;

    private String invExternalDiameter;

    private String invInternalDiameter;

    private String invDensity;

    private String invSize;

    private BigDecimal iqty;

    private BigDecimal tqty;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private Integer izDelete;

    private String deleteUser;

    private Date deleteDate;

    private String itype;

    private Integer u8MoMaterialId;
    private Integer rowNo;
}