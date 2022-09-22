package com.web.om.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("om_order_detail")
public class OmOrderDetail extends BaseEntity {
    @TableId(value = "id")
    private String id;

    private String recordId;

    private String mainId;

    private String productInvCode;

    private String taxRate ;

    private String productInvName;

    private String productInvStd;

    private String productInvUnit;

    private BigDecimal productQty;

    private BigDecimal materialPrice;

    private BigDecimal workPriceWithoutTax;

    private BigDecimal materialAmount;

    private BigDecimal workPrice;

    private BigDecimal totalWorkAmount;

    private BigDecimal price;

    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planEndDate;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private Integer izDelete;

    private String deleteUser;

    private Date deleteDate;

    private Integer u8MoDetailId;

    private Integer rowNo;

    public void setId(String id) {
        this.id = id;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setIzDelete(Integer izDelete) {
        this.izDelete = izDelete;
    }

    public void setDeleteUser(String deleteUser) {
        this.deleteUser = deleteUser;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
}