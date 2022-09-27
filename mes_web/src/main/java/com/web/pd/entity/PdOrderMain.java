package com.web.pd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @TableName pd_order_main
 */
@TableName(value ="pd_order_main")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class PdOrderMain extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private String id;

    /**
     * 
     */
    @TableField(value = "vouch_code")
    private String vouchCode;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "vouch_date")
    private Date vouchDate;

    /**
     * 
     */
    @TableField(value = "contract_pd")
    private String contractPd;

    /**
     * 
     */
    @TableField(value = "contract_sale")
    private String contractSale;

    /**
     * 
     */
    @TableField(value = "ven_code")
    private String venCode;

    /**
     * 
     */
    @TableField(value = "ven_name")
    private String venName;

    /**
     * 
     */
    @TableField(value = "dep_code")
    private String depCode;

    /**
     * 
     */
    @TableField(value = "dep_name")
    private String depName;

    /**
     * 
     */
    @TableField(value = "person_code")
    private String personCode;

    /**
     * 
     */
    @TableField(value = "person_name")
    private String personName;

    /**
     * 
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 
     */
    @TableField(value = "transport_way")
    private String transportWay;

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
    @TableField(value = "check_user")
    private String checkUser;

    /**
     * 
     */
    @TableField(value = "check_date")
    private Date checkDate;

    /**
     * 
     */
    @TableField(value = "u8_id")
    private Integer u8Id;

    /**
     * 
     */
    @TableField(value = "status_id")
    private String statusId;

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