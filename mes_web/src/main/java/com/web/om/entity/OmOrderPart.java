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
@TableName("om_order_part")
public class OmOrderPart {
    @TableId(value = "id")
    private String id;

    private String mainId;

    private String detailId;

    private String partInvCode;

    private String partInvName;

    private String partInvStd;

    private String partInvUnit;

    private BigDecimal partQty;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private Integer izDelete;

    private String deleteUser;

    private Date deleteDate;
    private Integer rowNo;
}