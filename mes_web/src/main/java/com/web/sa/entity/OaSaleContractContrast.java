package com.web.sa.entity;

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
@TableName("formmain_0110")
public class OaSaleContractContrast {
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

    private Date field0003;

    private String field0004;

    private String field0005;

    private BigDecimal field0006;

    private Date field0007;

    private Date field0008;

    private Date field0009;

    private BigDecimal field0010;

    private BigDecimal field0011;

    private BigDecimal field0012;

    private String field0013;

    private String field0014;

    private String field0015;

    private String field0031;

    private Date field0032;

    private String field0033;

//    field0001	 	VARCHAR	 	100	 	合同号	 	文本	 	NVARCHAR(100)
//    field0002	 	VARCHAR	 	100	 	合同行号	 	文本	 	NVARCHAR(100)
//    field0003	 	TIMESTAMP	 	255	 	下单日期	 	日期	 	DATETIME
//    field0004	 	VARCHAR	 	20	 	销售业务员	 	选人	 	NVARCHAR(20)
//    field0005	 	VARCHAR	 	100	 	客户名称	 	文本	 	NVARCHAR(100)
//    field0006	 	DECIMAL	 	20	 	任务单类型	 	单选	 	NUMERIC(19,0)
//    field0007	 	TIMESTAMP	 	255	 	编制日期	 	日期	 	DATETIME
//    field0008	 	TIMESTAMP	 	255	 	计划设计完成日期	 	日期	 	DATETIME
//    field0009	 	TIMESTAMP	 	255	 	设计完成起止日期	 	日期	 	DATETIME
//    field0010	 	DECIMAL	 	20	 	设备数量	 	文本	 	NUMERIC(20,0)
//    field0011	 	DECIMAL	 	20	 	设备大类	 	下拉	 	NUMERIC(19,0)
//    field0012	 	DECIMAL	 	20	 	设备类型	 	下拉	 	NUMERIC(19,0)
//    field0013	 	VARCHAR	 	255	 	设备名称	 	文本	 	NVARCHAR(255)
//    field0014	 	VARCHAR	 	255	 	设备规格	 	文本	 	NVARCHAR(255)
//    field0015	 	VARCHAR	 	100	 	设备编码	 	文本	 	NVARCHAR(100)
//    field0031	 	VARCHAR	 	20	 	完成人	 	选人	 	NVARCHAR(20)
//    field0032	 	TIMESTAMP	 	255	 	完成日期	 	日期	 	DATETIME
//    field0033	 	VARCHAR	 	100	 	U8订单号	 	文本	 	NVARCHAR(100)

}