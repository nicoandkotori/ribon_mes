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
@TableName("ar_cost_sim_report")
public class CostSimReport {
    @TableId(value = "id")
    private String id;

    private Integer iyear;

    private Integer imonth;

    private String custName;

    private String custCode;

    private String orderNo;

    private String invCode;

    private String invName;

    private String invStd;

    private String invType;

    private String whName;

    private String unit;

    private BigDecimal iqty;

    private BigDecimal inum;

    private BigDecimal kqty;

    private BigDecimal knum;

    private BigDecimal mqty;

    private BigDecimal mnum;

    private BigDecimal wnum;

    private BigDecimal wmny;

    private BigDecimal omum;

    private BigDecimal tolnum;

    private BigDecimal iprice;


    private String soCode;

    private String invNames;
    private String invCodes;
    private String invStds;

    private BigDecimal qty;
    private BigDecimal qtys;
    private BigDecimal imonery;
    private BigDecimal wtol;
    private BigDecimal saleAmount;
    private BigDecimal cprice;
    private BigDecimal cmonery;
    private String cinvdefine3;

    private Date dateStart;
    private Date dateEnd;
    private String statusId;

}