package com.web.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @TableName pd_order_detail
 */
@TableName(value ="pd_order_detail")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class PdOrderDetail extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 
     */
    @TableField(value = "main_id")
    private String mainId;

    /**
     * 产品编码
     */
    @TableField(value = "product_inv_code")
    private String productInvCode;

    /**
     * 产品名称
     */
    @TableField(value = "product_inv_name")
    private String productInvName;

    /**
     * 规格型号
     */
    @TableField(value = "product_inv_std")
    private String productInvStd;

    /**
     * 单位
     */
    @TableField(value = "product_inv_unit")
    private String productInvUnit;

    /**
     * 数量
     */
    @TableField(value = "product_qty")
    private BigDecimal productQty;

    /**
     * 材料单价
     */
    @TableField(value = "material_price")
    private BigDecimal materialPrice;

    /**
     * 单件材料费
     */
    @TableField(value = "material_amount")
    private BigDecimal materialAmount;

    /**
     * 单件加工费
     */
    @TableField(value = "work_price")
    private BigDecimal workPrice;

    /**
     * 加工费合计
     */
    @TableField(value = "total_work_amount")
    private BigDecimal totalWorkAmount;

    /**
     * 单件价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 合计
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 计划开工日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "plan_start_date")
    private Date planStartDate;

    /**
     * 计划完工日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "plan_end_date")
    private Date planEndDate;

    /**
     * 
     */
    @TableField(value = "create_user")
    private String createUser;

    /**
     * 
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 
     */
    @TableField(value = "update_user")
    private String updateUser;

    /**
     * 
     */
    @TableField(value = "update_date")
    private Date updateDate;

    /**
     * 
     */
    @TableField(value = "iz_delete")
    private Integer izDelete;

    /**
     * 
     */
    @TableField(value = "delete_user")
    private String deleteUser;

    /**
     * 
     */
    @TableField(value = "delete_date")
    private Date deleteDate;

    /**
     * 
     */
    @TableField(value = "u8_mo_detail_id")
    private Integer u8MoDetailId;

    /**
     * 
     */
    @TableField(value = "row_no")
    private Integer rowNo;

    /**
     * 税率
     */
    @TableField(value = "tax_rate")
    private BigDecimal taxRate;

    /**
     * 产品表行标识
     */
    @TableField(value = "record_id")
    private String recordId;

    /**
     * 不含税单价
     */
    @TableField(value = "work_price_without_tax")
    private BigDecimal workPriceWithoutTax;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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