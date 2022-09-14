package com.web.om.entity;

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
@TableName("om_order_main")
public class OmOrderMain extends BaseEntity {
    @TableId(value = "id")
    private String id;

    private String vouchCode;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date vouchDate;

    private String contractOm;

    private String contractSale;

    private String venCode;

    private String venName;

    private String depCode;

    private String depName;

    private String personCode;

    private String personName;

    private String remark;

    private String transportWay;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private Integer izDelete;

    private String deleteUser;

    private Date deleteDate;

    private String checkUser;

    private Date checkDate;

    private Integer u8Id;

    private String statusId;

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