package com.web.om.entity;

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

/**
 * 委外材料表entity
 *
 * @author mijiahao
 * @TableName om_order_material
 * @date 2022/09/13
 */
@TableName(value ="om_order_material")
@Data
@EqualsAndHashCode(callSuper = false)
public class OmOrderMaterial extends BaseEntity implements Serializable{
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    private String recordId;

    private String partRowId;

    /**
     * 产品表ID
     */
    @TableField(value = "detail_id")
    private String detailId;


    /**
     * 部件表ID
     */
    @TableField(value = "part_id")
    private String partId;

    /**
     * 委外订单表ID
     */
    @TableField(value = "main_id")
    private String mainId;

    /**
     * 材料编码
     */
    @TableField(value = "inv_code")
    private String invCode;

    /**
     * 材料名称
     */
    @TableField(value = "inv_name")
    private String invName;

    /**
     * 材料规格
     */
    @TableField(value = "inv_std")
    private String invStd;

    /**
     * 材料单位
     */
    @TableField(value = "inv_unit")
    private String invUnit;



    /**
     * 材料单价
     */
    @TableField(value = "unit_material_price")
    private BigDecimal unitMaterialPrice;

    /**
     * 单件材料费
     */
    @TableField(value = "unit_material_amount")
    private BigDecimal unitMaterialAmount;

    /**
     * 厚度
     */
    @TableField(value = "inv_land")
    private String invLand;

    /**
     * 宽度
     */
    @TableField(value = "inv_width")
    private String invWidth;

    /**
     * 长
     */
    @TableField(value = "inv_len")
    private String invLen;

    /**
     * 外径
     */
    @TableField(value = "inv_external_diameter")
    private String invExternalDiameter;

    /**
     * 内径
     */
    @TableField(value = "inv_internal_diameter")
    private String invInternalDiameter;

    /**
     * 密度
     */
    @TableField(value = "inv_density")
    private String invDensity;

    /**
     * 下料尺寸
     */
    @TableField(value = "inv_size")
    private String invSize;

    /**
     * 单耗
     */
    @TableField(value = "iqty")
    private BigDecimal iqty;

    /**
     * 总量
     */
    @TableField(value = "tqty")
    private BigDecimal tqty;

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
    @TableField(value = "itype")
    private String itype;

    /**
     * 
     */
    @TableField(value = "u8_mo_material_id")
    private Integer u8MoMaterialId;

    /**
     * 
     */
    @TableField(value = "row_no")
    private Integer rowNo;

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
     * 部件名称
     */
    @TableField(value = "part_inv_name")
    private String partInvName;

    /**
     * 部件编码
     */
    @TableField(value = "part_inv_code")
    private String partInvCode;

    /**
     * 部件数量
     */
    @TableField(value = "part_qty")
    private BigDecimal partQty;

    /**
     * 部件规格
     */
    @TableField(value = "part_inv_std")
    private String partInvStd;

    /**
     * 部件单位
     */
    @TableField(value = "part_inv_unit")
    private String partInvUnit;

    /**
     * 产品规格
     */
    @TableField(value = "product_inv_std")
    private String productInvStd;

    /**
     * 产品单位
     */
    @TableField(value = "product_inv_unit")
    private String productInvUnit;

    /**
     * 产品数量
     */
    @TableField(value = "product_qty")
    private String productQty;

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