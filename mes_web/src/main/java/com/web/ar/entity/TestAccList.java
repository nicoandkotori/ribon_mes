package com.web.ar.entity;

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
@TableName("ar_test_acc_list")
public class TestAccList {
    @TableId(value = "id")
    private String id;

    private Integer iyear;

    private Integer imonth;

    private String csid;

    private String pspCode;

    private String psCode;

    private String lj;

    private BigDecimal qty;

    private BigDecimal qtys;

    private Boolean bpu;

    private Boolean bpro;

    private String unit;

    private BigDecimal iprice;

    private BigDecimal imonery;

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
    private BigDecimal cprice;

    private BigDecimal cmonery;

}