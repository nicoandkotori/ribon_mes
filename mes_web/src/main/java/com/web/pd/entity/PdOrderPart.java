package com.web.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @TableName pd_order_part
 */
@TableName(value ="pd_order_part")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class PdOrderPart extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 生产订单ID
     */
    @TableField(value = "main_id")
    private String mainId;

    /**
     * 产品表ID
     */
    @TableField(value = "detail_id")
    private String detailId;

    /**
     * 部件编码
     */
    @TableField(value = "part_inv_code")
    private String partInvCode;

    /**
     * 部件名称
     */
    @TableField(value = "part_inv_name")
    private String partInvName;

    /**
     * 规格
     */
    @TableField(value = "part_inv_std")
    private String partInvStd;

    /**
     * 单位
     */
    @TableField(value = "part_inv_unit")
    private String partInvUnit;

    /**
     * 数量
     */
    @TableField(value = "part_qty")
    private BigDecimal partQty;

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
    @TableField(value = "row_no")
    private Integer rowNo;

    /**
     * 产品行标识
     */
    @TableField(value = "record_id")
    private String recordId;

    /**
     * 部件行标识
     */
    @TableField(value = "part_row_id")
    private String partRowId;

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