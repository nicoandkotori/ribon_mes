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

/**
 * 委外材料表entity
 *
 * @author mijiahao
 * @TableName om_order_material
 * @date 2022/09/13
 */
@TableName(value ="om_order_material")
@Data
public class OmOrderMaterial extends BaseEntity implements Serializable{
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

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
     * 数量
     */
    @TableField(value = "qty")
    private BigDecimal qty;

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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OmOrderMaterial other = (OmOrderMaterial) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDetailId() == null ? other.getDetailId() == null : this.getDetailId().equals(other.getDetailId()))
            && (this.getPartId() == null ? other.getPartId() == null : this.getPartId().equals(other.getPartId()))
            && (this.getMainId() == null ? other.getMainId() == null : this.getMainId().equals(other.getMainId()))
            && (this.getInvCode() == null ? other.getInvCode() == null : this.getInvCode().equals(other.getInvCode()))
            && (this.getInvName() == null ? other.getInvName() == null : this.getInvName().equals(other.getInvName()))
            && (this.getInvStd() == null ? other.getInvStd() == null : this.getInvStd().equals(other.getInvStd()))
            && (this.getInvUnit() == null ? other.getInvUnit() == null : this.getInvUnit().equals(other.getInvUnit()))
            && (this.getQty() == null ? other.getQty() == null : this.getQty().equals(other.getQty()))
            && (this.getUnitMaterialPrice() == null ? other.getUnitMaterialPrice() == null : this.getUnitMaterialPrice().equals(other.getUnitMaterialPrice()))
            && (this.getUnitMaterialAmount() == null ? other.getUnitMaterialAmount() == null : this.getUnitMaterialAmount().equals(other.getUnitMaterialAmount()))
            && (this.getInvLand() == null ? other.getInvLand() == null : this.getInvLand().equals(other.getInvLand()))
            && (this.getInvWidth() == null ? other.getInvWidth() == null : this.getInvWidth().equals(other.getInvWidth()))
            && (this.getInvLen() == null ? other.getInvLen() == null : this.getInvLen().equals(other.getInvLen()))
            && (this.getInvExternalDiameter() == null ? other.getInvExternalDiameter() == null : this.getInvExternalDiameter().equals(other.getInvExternalDiameter()))
            && (this.getInvInternalDiameter() == null ? other.getInvInternalDiameter() == null : this.getInvInternalDiameter().equals(other.getInvInternalDiameter()))
            && (this.getInvDensity() == null ? other.getInvDensity() == null : this.getInvDensity().equals(other.getInvDensity()))
            && (this.getInvSize() == null ? other.getInvSize() == null : this.getInvSize().equals(other.getInvSize()))
            && (this.getIqty() == null ? other.getIqty() == null : this.getIqty().equals(other.getIqty()))
            && (this.getTqty() == null ? other.getTqty() == null : this.getTqty().equals(other.getTqty()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getUpdateDate() == null ? other.getUpdateDate() == null : this.getUpdateDate().equals(other.getUpdateDate()))
            && (this.getIzDelete() == null ? other.getIzDelete() == null : this.getIzDelete().equals(other.getIzDelete()))
            && (this.getDeleteUser() == null ? other.getDeleteUser() == null : this.getDeleteUser().equals(other.getDeleteUser()))
            && (this.getDeleteDate() == null ? other.getDeleteDate() == null : this.getDeleteDate().equals(other.getDeleteDate()))
            && (this.getItype() == null ? other.getItype() == null : this.getItype().equals(other.getItype()))
            && (this.getU8MoMaterialId() == null ? other.getU8MoMaterialId() == null : this.getU8MoMaterialId().equals(other.getU8MoMaterialId()))
            && (this.getRowNo() == null ? other.getRowNo() == null : this.getRowNo().equals(other.getRowNo()))
            && (this.getProductInvCode() == null ? other.getProductInvCode() == null : this.getProductInvCode().equals(other.getProductInvCode()))
            && (this.getProductInvName() == null ? other.getProductInvName() == null : this.getProductInvName().equals(other.getProductInvName()))
            && (this.getPartInvName() == null ? other.getPartInvName() == null : this.getPartInvName().equals(other.getPartInvName()))
            && (this.getPartInvCode() == null ? other.getPartInvCode() == null : this.getPartInvCode().equals(other.getPartInvCode()))
            && (this.getPartQty() == null ? other.getPartQty() == null : this.getPartQty().equals(other.getPartQty()))
            && (this.getPartInvStd() == null ? other.getPartInvStd() == null : this.getPartInvStd().equals(other.getPartInvStd()))
            && (this.getPartInvUnit() == null ? other.getPartInvUnit() == null : this.getPartInvUnit().equals(other.getPartInvUnit()))
            && (this.getProductInvStd() == null ? other.getProductInvStd() == null : this.getProductInvStd().equals(other.getProductInvStd()))
            && (this.getProductInvUnit() == null ? other.getProductInvUnit() == null : this.getProductInvUnit().equals(other.getProductInvUnit()))
            && (this.getProductQty() == null ? other.getProductQty() == null : this.getProductQty().equals(other.getProductQty()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDetailId() == null) ? 0 : getDetailId().hashCode());
        result = prime * result + ((getPartId() == null) ? 0 : getPartId().hashCode());
        result = prime * result + ((getMainId() == null) ? 0 : getMainId().hashCode());
        result = prime * result + ((getInvCode() == null) ? 0 : getInvCode().hashCode());
        result = prime * result + ((getInvName() == null) ? 0 : getInvName().hashCode());
        result = prime * result + ((getInvStd() == null) ? 0 : getInvStd().hashCode());
        result = prime * result + ((getInvUnit() == null) ? 0 : getInvUnit().hashCode());
        result = prime * result + ((getQty() == null) ? 0 : getQty().hashCode());
        result = prime * result + ((getUnitMaterialPrice() == null) ? 0 : getUnitMaterialPrice().hashCode());
        result = prime * result + ((getUnitMaterialAmount() == null) ? 0 : getUnitMaterialAmount().hashCode());
        result = prime * result + ((getInvLand() == null) ? 0 : getInvLand().hashCode());
        result = prime * result + ((getInvWidth() == null) ? 0 : getInvWidth().hashCode());
        result = prime * result + ((getInvLen() == null) ? 0 : getInvLen().hashCode());
        result = prime * result + ((getInvExternalDiameter() == null) ? 0 : getInvExternalDiameter().hashCode());
        result = prime * result + ((getInvInternalDiameter() == null) ? 0 : getInvInternalDiameter().hashCode());
        result = prime * result + ((getInvDensity() == null) ? 0 : getInvDensity().hashCode());
        result = prime * result + ((getInvSize() == null) ? 0 : getInvSize().hashCode());
        result = prime * result + ((getIqty() == null) ? 0 : getIqty().hashCode());
        result = prime * result + ((getTqty() == null) ? 0 : getTqty().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getUpdateDate() == null) ? 0 : getUpdateDate().hashCode());
        result = prime * result + ((getIzDelete() == null) ? 0 : getIzDelete().hashCode());
        result = prime * result + ((getDeleteUser() == null) ? 0 : getDeleteUser().hashCode());
        result = prime * result + ((getDeleteDate() == null) ? 0 : getDeleteDate().hashCode());
        result = prime * result + ((getItype() == null) ? 0 : getItype().hashCode());
        result = prime * result + ((getU8MoMaterialId() == null) ? 0 : getU8MoMaterialId().hashCode());
        result = prime * result + ((getRowNo() == null) ? 0 : getRowNo().hashCode());
        result = prime * result + ((getProductInvCode() == null) ? 0 : getProductInvCode().hashCode());
        result = prime * result + ((getProductInvName() == null) ? 0 : getProductInvName().hashCode());
        result = prime * result + ((getPartInvName() == null) ? 0 : getPartInvName().hashCode());
        result = prime * result + ((getPartInvCode() == null) ? 0 : getPartInvCode().hashCode());
        result = prime * result + ((getPartQty() == null) ? 0 : getPartQty().hashCode());
        result = prime * result + ((getPartInvStd() == null) ? 0 : getPartInvStd().hashCode());
        result = prime * result + ((getPartInvUnit() == null) ? 0 : getPartInvUnit().hashCode());
        result = prime * result + ((getProductInvStd() == null) ? 0 : getProductInvStd().hashCode());
        result = prime * result + ((getProductInvUnit() == null) ? 0 : getProductInvUnit().hashCode());
        result = prime * result + ((getProductQty() == null) ? 0 : getProductQty().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", detailId=").append(detailId);
        sb.append(", partId=").append(partId);
        sb.append(", mainId=").append(mainId);
        sb.append(", invCode=").append(invCode);
        sb.append(", invName=").append(invName);
        sb.append(", invStd=").append(invStd);
        sb.append(", invUnit=").append(invUnit);
        sb.append(", qty=").append(qty);
        sb.append(", unitMaterialPrice=").append(unitMaterialPrice);
        sb.append(", unitMaterialAmount=").append(unitMaterialAmount);
        sb.append(", invLand=").append(invLand);
        sb.append(", invWidth=").append(invWidth);
        sb.append(", invLen=").append(invLen);
        sb.append(", invExternalDiameter=").append(invExternalDiameter);
        sb.append(", invInternalDiameter=").append(invInternalDiameter);
        sb.append(", invDensity=").append(invDensity);
        sb.append(", invSize=").append(invSize);
        sb.append(", iqty=").append(iqty);
        sb.append(", tqty=").append(tqty);
        sb.append(", createUser=").append(createUser);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", izDelete=").append(izDelete);
        sb.append(", deleteUser=").append(deleteUser);
        sb.append(", deleteDate=").append(deleteDate);
        sb.append(", itype=").append(itype);
        sb.append(", u8MoMaterialId=").append(u8MoMaterialId);
        sb.append(", rowNo=").append(rowNo);
        sb.append(", productInvCode=").append(productInvCode);
        sb.append(", productInvName=").append(productInvName);
        sb.append(", partInvName=").append(partInvName);
        sb.append(", partInvCode=").append(partInvCode);
        sb.append(", partQty=").append(partQty);
        sb.append(", partInvStd=").append(partInvStd);
        sb.append(", partInvUnit=").append(partInvUnit);
        sb.append(", productInvStd=").append(productInvStd);
        sb.append(", productInvUnit=").append(productInvUnit);
        sb.append(", productQty=").append(productQty);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}