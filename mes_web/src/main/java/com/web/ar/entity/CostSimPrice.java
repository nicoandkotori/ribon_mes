package com.web.ar.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ar_cost_sim_price")
public class CostSimPrice {
    @TableId(value = "id")
    private String id;

    private Integer iyear;

    private Integer imonth;

    private String custCode;

    private String orderNo;

    private String invCode;

    private String whName;

    private BigDecimal saleAmount;


}