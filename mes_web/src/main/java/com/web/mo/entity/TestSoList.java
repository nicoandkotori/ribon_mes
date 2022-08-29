package com.web.mo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("production_test_so_list")
public class TestSoList {
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

    private String memo;

    private String statusId;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private Boolean izDelete;

    private String deleteUser;

    private Date deleteDate;

    private BigDecimal yqty;

    private String checkUser;

    private Date checkDate;


    @TableField(exist = false)
    private String invUnit;
    @TableField(exist = false)
    private String invCode;
    @TableField(exist = false)
    private String invName;
    @TableField(exist = false)
    private String invAddCode;
    @TableField(exist = false)
    private String invStd;
    @TableField(exist = false)
    private String whCode;
    @TableField(exist = false)
    private Date ddate;
     //当前库存量
     @TableField(exist = false)
    private BigDecimal iquantity;
    //1锯料，2自制激光，3剪料，4冲件  5安装
    @TableField(exist = false)
    private Boolean proType1;
    @TableField(exist = false)
    private Boolean proType2;
    @TableField(exist = false)
    private Boolean proType3;
    @TableField(exist = false)
    private Boolean proType4;
    @TableField(exist = false)
    private Boolean proType5;
    @TableField(exist = false)
    private List<String> listProType;
    @TableField(exist = false)
    private String u8DB;

    @TableField(exist = false)
    private BigDecimal oqty;
    @TableField(exist = false)
    private Date dateStart;
    @TableField(exist = false)
    private Date dateEnd;

    @TableField(exist = false)
    private BigDecimal nowAdQty;
    @TableField(exist = false)
    private BigDecimal hisAdQty;

    @TableField(exist = false)
    private String len;
    @TableField(exist = false)
    private String width;

}