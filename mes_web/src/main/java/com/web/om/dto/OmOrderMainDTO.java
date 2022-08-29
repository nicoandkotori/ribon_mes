package com.web.om.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmOrderMainDTO {
    private Date dateStart;  //---
    private Date dateEnd;   //---

    private String id;

    private String vouchCode;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date vouchDate;

    private String contractOm;

    private String contractSale;

    private String venCode;

    private String venName;

    private String depCode;

    private String depName;

    private String personCode;

    private String personName;

    private String remark;

    private String transportWay;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private Integer izDelete;

    private String deleteUser;

    private Date deleteDate;

    private String checkUser;

    private Date checkDate;

    private Integer u8Id;

    private String statusId;



    private String productInvCode;

    private String productInvName;

    private String productInvStd;

    private String productInvUnit;

    private BigDecimal productQty;

    private BigDecimal materialPrice;

    private BigDecimal materialAmount;

    private BigDecimal workPrice;

    private BigDecimal totalWorkAmount;

    private BigDecimal price;

    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planEndDate;



    private Integer u8MoDetailId;

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


    private String itype;

    private Integer u8MoMaterialId;
}