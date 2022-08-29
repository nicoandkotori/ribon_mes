package com.web.basicinfo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.web.sa.entity.OaSaleContract;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("formmain_0072")
public class OaCustomer extends Model<OaCustomer> {
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

    private String field0001;

    private String field0002;

    private String field0003;

    private String field0004;

    private String field0005;

    private String field0006;

    private String field0008;

    private String field0009;

    private String field0010;

    private String field0011;

    private Date field0012;

    private String field0017;

    private String field0018;

    private String field0019;

    private String field0020;

    private String field0021;

    private String field0022;

    private String field0023;

    private BigDecimal field0024;

    private String field0025;

    private BigDecimal field0026;

    private Date field0027;

    private BigDecimal field0028;

    private BigDecimal field0029;

    private BigDecimal field0030;

    private BigDecimal field0031;

    private BigDecimal field0032;

    private BigDecimal field0033;

    private BigDecimal field0034;

    private String field0007;

    private String field0014;

    private String field0015;

    private String field0016;

    private String field0035;

//    field0001	 	VARCHAR	 	20	 	拜访记录	 	表单自定义控件	 	NVARCHAR(20)
//    field0002	 	VARCHAR	 	20	 	查看成交记录	 	表单自定义控件	 	NVARCHAR(20)
//    field0003	 	VARCHAR	 	20	 	查看退货明细	 	表单自定义控件	 	NVARCHAR(20)
//    field0004	 	VARCHAR	 	20	 	查看开票详情	 	表单自定义控件	 	NVARCHAR(20)
//    field0005	 	VARCHAR	 	20	 	查看回款记录	 	表单自定义控件	 	NVARCHAR(20)
//    field0006	 	VARCHAR	 	20	 	变更记录	 	表单自定义控件	 	NVARCHAR(20)
//    field0007	 	VARCHAR	 	255	 	客户名称	 	文本	 	NVARCHAR(255)
//    field0008	 	VARCHAR	 	100	 	客户编号	 	文本	 	NVARCHAR(100)
//    field0009	 	VARCHAR	 	100	 	客户来源	 	文本	 	NVARCHAR(100)
//    field0010	 	VARCHAR	 	100	 	客户类型	 	文本	 	NVARCHAR(100)
//    field0011	 	VARCHAR	 	100	 	客户状态	 	文本	 	NVARCHAR(100)
//    field0012	 	TIMESTAMP	 	255	 	新增日期	 	日期	 	DATETIME
//    field0014	 	VARCHAR	 	255	 	客户联系人	 	文本	 	NVARCHAR(255)
//    field0015	 	VARCHAR	 	255	 	客户联系人电话	 	文本	 	NVARCHAR(255)
//    field0016	 	VARCHAR	 	255	 	客户地址	 	文本	 	NVARCHAR(255)
//    field0017	 	VARCHAR	 	100	 	客户传真	 	文本	 	NVARCHAR(100)
//    field0018	 	VARCHAR	 	100	 	详细信息	 	文本	 	NVARCHAR(100)
//    field0019	 	VARCHAR	 	100	 	客户电话	 	文本	 	NVARCHAR(100)
//    field0020	 	VARCHAR	 	100	 	销售区域	 	文本	 	NVARCHAR(100)
//    field0021	 	VARCHAR	 	20	 	销售经理	 	选人	 	NVARCHAR(20)
//    field0022	 	VARCHAR	 	100	 	跟进阶段	 	文本	 	NVARCHAR(100)
//    field0023	 	VARCHAR	 	100	 	跟进时限	 	文本	 	NVARCHAR(100)
//    field0024	 	DECIMAL	 	20	 	预计金额	 	文本	 	NUMERIC(20,0)
//    field0025	 	VARCHAR	 	100	 	预计成交	 	文本	 	NVARCHAR(100)
//    field0026	 	DECIMAL	 	20	 	拜访次数	 	文本	 	NUMERIC(20,0)
//    field0027	 	TIMESTAMP	 	255	 	最近拜访	 	日期	 	DATETIME
//    field0028	 	DECIMAL	 	20	 	成交总额	 	文本	 	NUMERIC(20,0)
//    field0029	 	DECIMAL	 	20	 	退货总额	 	文本	 	NUMERIC(20,0)
//    field0030	 	DECIMAL	 	20	 	回款总额	 	文本	 	NUMERIC(20,0)
//    field0031	 	DECIMAL	 	20	 	剩余未回款	 	文本	 	NUMERIC(20,0)
//    field0032	 	DECIMAL	 	20	 	开票总额	 	文本	 	NUMERIC(20,0)
//    field0033	 	DECIMAL	 	20	 	未开票总额	 	文本	 	NUMERIC(20,0)
//    field0034	 	DECIMAL	 	20	 	信息变更次数	 	文本	 	NUMERIC(20,0)
//    field0035	 	VARCHAR	 	255	 	商机编码	 	文本	 	NVARCHAR(255)

}