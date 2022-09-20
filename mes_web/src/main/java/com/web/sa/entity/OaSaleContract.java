package com.web.sa.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.web.basicinfo.entity.Inventory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("formmain_0130")
public class OaSaleContract extends Model<OaSaleContract> {
    @TableId(value = "ID")
    private Long id;

    private Integer state;

    private String startMemberId;

    private Date startDate;

    private String approveMemberId;

    private Date approveDate;

    private Integer finishedflag;

    private Integer ratifyflag;

    private String ratifyMemberId;

    private Date ratifyDate;

    private Integer sort;

    private String modifyMemberId;

    private Date modifyDate;

    private String field0160;

    private String field0003;

    private String field0004;

    private String field0005;

    private String field0006;

    private BigDecimal field0014;

    private String field0015;

    private String field0016;

    private String field0017;

    private String field0019;

    private String field0020;

    private String field0021;

    private String field0022;

    private String field0018;

    private String field0023;

    private String deviceType;

    private String deviceClass;

    private Date field0056;
    private Date field0057;
//    字段名称	 	字段类型	 	字段长度	 	显示名称	 	字段输入类型	 	字段最终类型
//    field0006	 	VARCHAR	 	100	 	合同编号	 	文本	 	NVARCHAR(100)
//    field0002	 	VARCHAR	 	20	 	业务员	 	选人	 	NVARCHAR(20)
//    field0003	 	VARCHAR	 	100	 	业务员手机	 	文本	 	NVARCHAR(100)
//    field0004	 	VARCHAR	 	20	 	售后服务联系人	 	选人	 	NVARCHAR(20)
//    field0005	 	VARCHAR	 	100	 	售后联系人电话	 	文本	 	NVARCHAR(100)
//    field0014	 	DECIMAL	 	20	 	合计总价	 	文本	 	NUMERIC(20,0)
//    field0015	 	VARCHAR	 	2000	 	交货地点	 	文本域	 	NVARCHAR(2000)
//    field0016	 	VARCHAR	 	2000	 	运输方式及费用	 	文本域	 	NVARCHAR(2000)
//    field0017	 	VARCHAR	 	2000	 	技术服务费	 	文本域	 	NVARCHAR(2000)
//    field0018	 	VARCHAR	 	255	 	客户名称	 	文本	 	NVARCHAR(255)
//    field0019	 	VARCHAR	 	100	 	客户电话	 	文本	 	NVARCHAR(100)
//    field0020	 	VARCHAR	 	100	 	客户联系人	 	文本	 	NVARCHAR(100)
//    field0021	 	VARCHAR	 	100	 	客户传真	 	文本	 	NVARCHAR(100)
//    field0022	 	VARCHAR	 	255	 	收货地址	 	文本	 	NVARCHAR(255)
//    field0023	 	VARCHAR	 	100	 	客户编码	 	文本	 	NVARCHAR(100)



    @TableField(exist = false)
    private BigDecimal field0007;
    @TableField(exist = false)
    private BigDecimal field0010;
    @TableField(exist = false)
    private BigDecimal field0011;
    @TableField(exist = false)
    private BigDecimal field0012;
    @TableField(exist = false)
    private String field0008;
    @TableField(exist = false)
    private String field0009;
    @TableField(exist = false)
    private String field0013;
    @TableField(exist = false)
    private String field0123;
    @TableField(exist = false)
    private String field0103;
    @TableField(exist = false)
    private Long contrastId;
    @TableField(exist = false)
    private Long contrastMainId;
    @TableField(exist = false)
    private Long formmainId;
    @TableField(exist = false)
    private String stCode;
//    @TableField(exist = false)
//    private String personCode;

    @TableField(exist = false)
    private String personName;
    //制单人
    @TableField(exist = false)
    private String createUser;
    @TableField(exist = false)
    private String deviceName;
    @TableField(exist = false)
    private String deviceStd;
    @TableField(exist = false)
    private String remark;
    @TableField(exist = false)
    private String designUser;

    @TableField(exist = false)
    private String contractCode;
    @TableField(exist = false)
    private String custName;
    @TableField(exist = false)
    private String invCode;
    @TableField(exist = false)
    private BigDecimal qty;

    @TableField(exist = false)
    private String unit;

    @TableField(exist = false)
    private String trialCode;


}