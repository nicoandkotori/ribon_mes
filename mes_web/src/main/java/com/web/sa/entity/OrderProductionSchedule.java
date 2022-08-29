package com.web.sa.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("all_order_production_schedule")
public class OrderProductionSchedule {
    private String id;

    private Boolean freshEquipment;

    private String custName;

    private String contractCode;

    private String productInvName;

    private String productInvCode;

    private String productInvStd;

    private BigDecimal qty;

    private Date orderDate;

    private Date deliveryDate;

    private Date negotiateDate;

    private String invName;

    private String invCode;

    private String invStd;

    private BigDecimal tqty;

    private String person;

    private String unitCost;

    private String productionTeam;

    private String pmcType;

    private Integer soId;

    private Integer soDetailId;

    private Integer structureId;

    private Integer structureDetailId;

    private Date planDrawDate;

    private Date actualDrawDate;

    private Date planT6CreateDate;

    private Date actualT6CreateDate;

    private Date planRawMaWhDate;

    private Date actualRawMaWhDate;

    private Date planMwWhDate;

    private Date actualMwWhDate;

    private Date planRawMaMwDispDate;

    private Date actualRawMaMwDispDate;

    private Date planOutDispDate;

    private Date actualOutDispDate;

    private Date planProInsDate;

    private Date actualProInsDate;

    private Date planQuaInsWhDate;

    private Date actualQuaInsWhDate;

    private Date actualDeliveryDate;

    private String remark;

    private Boolean delayedShipments;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private String deleteUser;

    private Date deleteDate;

    private Boolean izDelete;

    private String comDepUser;

    private Date comDepDate;

    private String tecDepUser;

    private Date tecDepDate;

    private String pmcDepUser;

    private Date pmcDepDate;

    private String quaConDepUser;

    private Date quaConDepDate;

    private String assDepUser;

    private Date assDepDate;

    private String whUser;

    private Date whDate;

    private String productionTeamUser;

    private Date productionTeamDate;

    private String rollKneadingUnitUser;

    private Date rollKneadingUnitDate;

    private String specialRulesSetUser;

    private Date specialRulesSetDate;

    private String newProGroupUser;

    private Date newProGroupDate;

    private String purchasDepUser;

    private Date purchasDepDate;

    private String cwDepUser;

    private Date cwDepDate;

    private BigDecimal iprice;

    private BigDecimal iamount;

    private BigDecimal costMaterial;

    private BigDecimal costMachine;

    private BigDecimal costPrice;

    private BigDecimal costFactPrice;

    private Date chuchaiDate;

    private BigDecimal chuchaiAmount;

    private Date chuchaiDate1;

    private BigDecimal chuchaiAmount1;

    private Date chuchaiDate2;

    private BigDecimal chuchaiAmount2;


    @TableField(exist = false)
    private String database;
}