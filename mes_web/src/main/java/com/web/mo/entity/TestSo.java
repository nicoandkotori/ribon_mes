package com.web.mo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("production_test_so")
public class TestSo {
    @TableId(value = "id")
    private String id;

    private String pspCode;

    private String psCode;

    private String develop;

    private BigDecimal qty;

    private BigDecimal qtys;

    private BigDecimal curQty;

    private BigDecimal poQty;

    private BigDecimal kyQty;

    private BigDecimal adQty;

    private String proType;

    private String mInvCode;

    private String mInvName;

    private String mInvStd;

    private String matSize;

    private String productInvCode;

    private String productInvName;

    private String partName;

    private String conNo;

    private String orderNo;

    private String fromNo;

    private String custName;

    private BigDecimal soQty;

    private BigDecimal yqty;

    private String memo;

    private String statusId;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private Boolean izDelete;

    private String deleteUser;

    private Date deleteDate;

    private String checkUser;

    private Date checkDate;
    @TableField(exist = false)
    private String u8DB;
}